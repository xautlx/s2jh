package lab.s2jh.biz.sale.web.action;

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
import lab.s2jh.biz.finance.entity.AccountSubject;
import lab.s2jh.biz.finance.service.AccountSubjectService;
import lab.s2jh.biz.sale.entity.SaleDelivery;
import lab.s2jh.biz.sale.entity.SaleDeliveryDetail;
import lab.s2jh.biz.sale.service.SaleDeliveryService;
import lab.s2jh.biz.stock.service.StorageLocationService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.sys.service.DataDictService;
import lab.s2jh.web.action.BaseController;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

@MetaData("销售(发货)单管理")
public class SaleDeliveryController extends BaseController<SaleDelivery, Long> {

    @Autowired
    private SaleDeliveryService saleDeliveryService;
    @Autowired
    private VoucherNumGenerateService voucherNumGenerateService;
    @Autowired
    private StorageLocationService storageLocationService;
    @Autowired
    private AccountSubjectService accountSubjectService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private DataDictService dataDictService;

    @Override
    protected BaseService<SaleDelivery, Long> getEntityService() {
        return saleDeliveryService;
    }

    @Override
    protected void checkEntityAclPermission(SaleDelivery entity) {
        // TODO Add acl check code logic
    }

    @Override
    protected void setupDetachedBindingEntity(Long id) {
        bindingEntity = getEntityService().findDetachedOne(id, "saleDeliveryDetails");
    }

    @Override
    @MetaData("保存")
    public HttpHeaders doSave() {
        return super.doSave();
    }

    @Override
    @MetaData("删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData("查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
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

    public Map<String, String> getReferenceSourceMap() {
        return dataDictService.findMapDataByPrimaryKey("BIZ_SALE_DELIVERY_ORDER_SOURCE");
    }

    @MetaData(value = "付款会计科目数据")
    public Map<Long, String> getPaymentAccountSubjects() {
        Map<Long, String> datas = Maps.newLinkedHashMap();
        Iterable<AccountSubject> items = accountSubjectService.findPaymentAccountSubjects();
        for (AccountSubject item : items) {
            datas.put(item.getId(), item.getDisplay());
        }
        return datas;
    }

    @Override
    public void prepareEdit() {
        super.prepareEdit();
        if (bindingEntity.isNew()) {
            bindingEntity.setVoucher(voucherNumGenerateService.getVoucherNumByType(VoucherTypeEnum.XS));
            bindingEntity.setVoucherDate(new Date());
            User user = AuthUserHolder.getLogonUser();
            bindingEntity.setVoucherUser(user);
            bindingEntity.setVoucherDepartment(user.getDepartment());
        }
    }

    @MetaData("行项数据")
    public HttpHeaders saleDeliveryDetails() {
        List<SaleDeliveryDetail> saleDeliveryDetails = bindingEntity.getSaleDeliveryDetails();
        if (BooleanUtils.toBoolean(getParameter("clone"))) {
            long i = new Date().getTime();
            for (SaleDeliveryDetail saleDeliveryDetail : saleDeliveryDetails) {
                saleDeliveryDetail.setId(-(i++));
                saleDeliveryDetail.addExtraAttribute(PersistableEntity.EXTRA_ATTRIBUTE_DIRTY_ROW, true);
            }
        }
        setModel(buildPageResultFromList(saleDeliveryDetails));
        return buildDefaultHttpHeaders();
    }
}