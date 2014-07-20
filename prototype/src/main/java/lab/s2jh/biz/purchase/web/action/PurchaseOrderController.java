package lab.s2jh.biz.purchase.web.action;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lab.s2jh.auth.entity.Department;
import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.security.AuthUserHolder;
import lab.s2jh.auth.service.DepartmentService;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.biz.core.constant.VoucherTypeEnum;
import lab.s2jh.biz.core.service.VoucherNumGenerateService;
import lab.s2jh.biz.finance.service.BizTradeUnitService;
import lab.s2jh.biz.md.entity.Commodity;
import lab.s2jh.biz.md.service.CommodityService;
import lab.s2jh.biz.purchase.entity.PurchaseOrder;
import lab.s2jh.biz.purchase.entity.PurchaseOrderDetail;
import lab.s2jh.biz.purchase.service.PurchaseOrderService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.web.action.BaseController;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

@MetaData("采购订单")
public class PurchaseOrderController extends BaseController<PurchaseOrder, Long> {

    private final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private BizTradeUnitService bizTradeUnitService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;

    @Autowired
    private VoucherNumGenerateService voucherNumGenerateService;

    @Override
    protected BaseService<PurchaseOrder, Long> getEntityService() {
        return purchaseOrderService;
    }

    @Override
    protected void checkEntityAclPermission(PurchaseOrder entity) {
        // TODO Add acl check code logic
    }

    protected void setupDetachedBindingEntity(Long id) {
        bindingEntity = getEntityService().findDetachedOne(id, "purchaseOrderDetails");
    }

    @Override
    @MetaData("查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    public Map<String, Object> getTaskVariables() {
        Map<String, Object> variables = taskService.getVariables(this.getRequiredParameter("taskId"));
        if (logger.isDebugEnabled()) {
            for (Map.Entry<String, Object> me : variables.entrySet()) {
                logger.debug("{} - {}", me.getKey(), me.getValue());
            }
        }
        return variables;
    }

    public Map<String, String> getDepartmentsMap() {
        Map<String, String> departmentsMap = new LinkedHashMap<String, String>();
        GroupPropertyFilter groupPropertyFilter = GroupPropertyFilter.buildDefaultAndGroupFilter();
        Iterable<Department> departments = departmentService.findByFilters(groupPropertyFilter);
        Iterator<Department> it = departments.iterator();
        while (it.hasNext()) {
            Department department = it.next();
            departmentsMap.put(department.getId(), department.getDisplay());
        }
        return departmentsMap;
    }

    public Map<Long, String> getUsersMap() {
        Map<Long, String> usersMap = new LinkedHashMap<Long, String>();
        GroupPropertyFilter groupPropertyFilter = GroupPropertyFilter.buildDefaultAndGroupFilter();
        groupPropertyFilter.append(new PropertyFilter(MatchType.EQ, "enabled", Boolean.TRUE));
        Iterable<User> users = userService.findByFilters(groupPropertyFilter);
        Iterator<User> it = users.iterator();
        while (it.hasNext()) {
            User user = it.next();
            usersMap.put(user.getId(), user.getDisplay());
        }
        return usersMap;
    }

    @Override
    @MetaData("保存")
    public HttpHeaders doSave() {
        if (bindingEntity.isNew()) {
            List<PurchaseOrderDetail> purchaseOrderDetails = bindingEntity.getPurchaseOrderDetails();
            for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
                purchaseOrderDetail.setPurchaseOrder(bindingEntity);
            }
        }
        return super.doSave();
    }

    public void prepareBpmNew() {
        String clone = this.getParameter("clone");
        if (BooleanUtils.toBoolean(clone)) {
            bindingEntity.resetCommonProperties();
        } else {
            bindingEntity = new PurchaseOrder();
        }
        bindingEntity.setVoucher(voucherNumGenerateService.getVoucherNumByType(VoucherTypeEnum.JHD));
        bindingEntity.setVoucherDate(new Date());
        bindingEntity.setVoucherUser(AuthUserHolder.getLogonUser());
        bindingEntity.setVoucherDepartment(AuthUserHolder.getLogonUserDepartment());

    }

    @MetaData("采购订单创建初始化")
    public HttpHeaders bpmNew() {
        return buildDefaultHttpHeaders("bpmInput");
    }

    @MetaData("采购订单保存")
    public HttpHeaders bpmSave() {

        Map<String, Object> variables = Maps.newHashMap();
        String submitToAudit = this.getParameter("submitToAudit");
        if (BooleanUtils.toBoolean(submitToAudit)) {
            bindingEntity.setLastOperationSummary(bindingEntity.buildLastOperationSummary("提交"));
            bindingEntity.setSubmitDate(new Date());
        }

        List<PurchaseOrderDetail> purchaseOrderDetails = bindingEntity.getPurchaseOrderDetails();
        for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
            purchaseOrderDetail.setPurchaseOrder(bindingEntity);
        }
        if (StringUtils.isBlank(bindingEntity.getTitle())) {
            Commodity commodity = commodityService.findOne(purchaseOrderDetails.get(0).getCommodity().getId());
            String commodityTitle = commodity.getTitle();
            commodityTitle = StringUtils.substring(commodityTitle, 0, 30);
            bindingEntity.setTitle(commodityTitle + "...等" + purchaseOrderDetails.size() + "项商品");
        }

        if (bindingEntity.isNew()) {
            purchaseOrderService.bpmCreate(bindingEntity, variables);
            setModel(OperationResult.buildSuccessResult("采购订单创建完成，并同步启动处理流程", bindingEntity));
        } else {
            purchaseOrderService.bpmUpdate(bindingEntity, this.getRequiredParameter("taskId"), variables);
            setModel(OperationResult.buildSuccessResult("采购订单任务提交完成", bindingEntity));
        }
        return buildDefaultHttpHeaders();
    }

    @MetaData("采购订单定价")
    public HttpHeaders bpmPrice() {
        Map<String, Object> variables = Maps.newHashMap();
        List<PurchaseOrderDetail> purchaseOrderDetails = bindingEntity.getPurchaseOrderDetails();
        for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
            purchaseOrderDetail.setPurchaseOrder(bindingEntity);
        }
        purchaseOrderService.bpmUpdate(bindingEntity, this.getRequiredParameter("taskId"), variables);
        setModel(OperationResult.buildSuccessResult("采购订单定价完成", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    @MetaData("一线审核")
    public HttpHeaders bpmLevel1Audit() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("auditLevel1Time", new Date());
        variables.put("auditLevel1User", AuthContextHolder.getAuthUserPin());
        Boolean auditLevel1Pass = new Boolean(getRequiredParameter("auditLevel1Pass"));
        variables.put("auditLevel1Pass", auditLevel1Pass);
        variables.put("auditLevel1Explain", getParameter("auditLevel1Explain"));
        bindingEntity.setLastOperationSummary(bindingEntity.buildLastOperationSummary("审核"));
        if (!auditLevel1Pass) {
            bindingEntity.setSubmitDate(null);
        }
        purchaseOrderService.bpmUpdate(bindingEntity, this.getRequiredParameter("taskId"), variables);
        setModel(OperationResult.buildSuccessResult("采购订单一线审核完成", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    @MetaData("二线审核")
    public HttpHeaders bpmLevel2Audit() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("auditLevel2Time", new Date());
        variables.put("auditLevel2User", AuthContextHolder.getAuthUserPin());
        Boolean auditLevel2Pass = new Boolean(getRequiredParameter("auditLevel2Pass"));
        variables.put("auditLevel2Pass", auditLevel2Pass);
        variables.put("auditLevel2Explain", getParameter("auditLevel2Explain"));
        if (!auditLevel2Pass) {
            bindingEntity.setSubmitDate(null);
        }
        purchaseOrderService.bpmUpdate(bindingEntity, this.getRequiredParameter("taskId"), variables);
        setModel(OperationResult.buildSuccessResult("采购订单二线审核完成", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    @MetaData("采购订单付款界面显示")
    public HttpHeaders bpmPayInput() {
        return buildDefaultHttpHeaders("bpmPay");
    }

    @MetaData("(预)付款任务")
    public HttpHeaders bpmPay() {
        purchaseOrderService.bpmPay(bindingEntity, this.getRequiredParameter("taskId"));
        setModel(OperationResult.buildSuccessResult("采购(预)付款任务处理完成", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    @MetaData("保存卖家发货信息")
    public HttpHeaders bpmDelivery() {
        bindingEntity.setDeliveryTime(new Date());
        purchaseOrderService.bpmDelivery(bindingEntity, this.getRequiredParameter("taskId"));
        setModel(OperationResult.buildSuccessResult("录入卖家发货信息完成", bindingEntity));
        return buildDefaultHttpHeaders();
    }

    @MetaData("行项数据")
    public HttpHeaders purchaseOrderDetails() {
        List<PurchaseOrderDetail> purchaseOrderDetails = bindingEntity.getPurchaseOrderDetails();
        if (BooleanUtils.toBoolean(getParameter("clone"))) {
            for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
                purchaseOrderDetail.resetCommonProperties();
            }
        }
        setModel(buildPageResultFromList(purchaseOrderDetails));
        return buildDefaultHttpHeaders();
    }

    @MetaData("单据红冲")
    public HttpHeaders doRedword() {
        Assert.isTrue(bindingEntity.getRedwordDate() == null);
        purchaseOrderService.redword(bindingEntity);
        setModel(OperationResult.buildSuccessResult("红冲完成"));
        return buildDefaultHttpHeaders();
    }

    @Override
    public HttpHeaders revisionList() {
        return super.revisionList();
    }

    @Override
    public HttpHeaders revisionCompare() {
        return super.revisionCompare();
    }
}