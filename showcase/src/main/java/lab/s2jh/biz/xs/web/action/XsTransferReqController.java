package lab.s2jh.biz.xs.web.action;

import java.util.Date;

import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.xs.entity.XsTransferReq;
import lab.s2jh.biz.xs.entity.XsTransferReq.XsTransferReqStateEnum;
import lab.s2jh.biz.xs.service.XsJbxxService;
import lab.s2jh.biz.xs.service.XsTransferReqService;
import lab.s2jh.biz.xx.entity.XxBj;
import lab.s2jh.biz.xx.service.XxBjService;
import lab.s2jh.biz.xx.service.XxJcxxService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.audit.envers.ExtRevisionListener;
import lab.s2jh.core.exception.DataAccessDeniedException;
import lab.s2jh.core.exception.DataOperationDeniedException;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;


@MetaData(title = "学籍异动(转学)")
public class XsTransferReqController extends BaseBizController<XsTransferReq, String> {

    @Autowired
    private XsTransferReqService xsTransferReqService;

    @Autowired
    private XxJcxxService xxJcxxService;

    @Autowired
    private XsJbxxService xsJbxxService;

    @Autowired
    private XxBjService xxBjService;
    
    @Autowired
    private AclService aclService;

    @Override
    protected BaseService<XsTransferReq, String> getEntityService() {
        return xsTransferReqService;
    }
    
    
    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }
    
    @Override
    protected void checkEntityAclPermission(XsTransferReq entity) {
        aclService.validateAuthUserAclCodePermission(entity.getSourceXx().getXxdm(), entity.getTargetXx().getXxdm());
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupFilter) {
        super.appendFilterProperty(groupFilter);
        String userAclCode = AuthContextHolder.getAclCode();
        String type = this.getParameter("type", "in");
        if ("in".equalsIgnoreCase(type)) {
            groupFilter.and(new PropertyFilter(MatchType.NE, "state", XsTransferReqStateEnum.S10DRAFT));
            groupFilter.and(new PropertyFilter(MatchType.EQ, "targetXx.xxdm", userAclCode));
        } else {
            groupFilter.and(new PropertyFilter(MatchType.EQ, "sourceXx.xxdm", userAclCode));
        }
    }

    @Override
    @MetaData(title = "创建")
    public HttpHeaders doCreate() {
        String userAclCode = AuthContextHolder.getAclCode();
        if (bindingEntity.getTargetXx().getXxdm().equals(userAclCode)) {
            setModel(OperationResult.buildFailureResult("不能转入自己学校"));
            return buildDefaultHttpHeaders();
        }
        bindingEntity.setReqTime(new Date());
        bindingEntity.setReqUserId(AuthContextHolder.getAuthUserPin());
        bindingEntity.setSourceXx(xxJcxxService.findByXxdm(AuthContextHolder.getAclCode()));
        bindingEntity.setXsJbxx(xsJbxxService.findByXxdmAndXh(AuthContextHolder.getAclCode(), bindingEntity.getXsJbxx()
                .getXh()));
        bindingEntity.setTargetXx(xxJcxxService.findByXxdm(bindingEntity.getTargetXx().getXxdm()));
        return super.doCreate();
    }

    public boolean isDisallowUpdate() {
        if (bindingEntity.getState().equals(XsTransferReqStateEnum.S10DRAFT)) {
            return false;
        }
        return true;
    }

    @Override
    @MetaData(title = "更新")
    public HttpHeaders doUpdate() {
        if (isDisallowUpdate()) {
            throw new DataOperationDeniedException();
        }
        String userAclCode = AuthContextHolder.getAclCode();
        if (!bindingEntity.getSourceXx().getXxdm().equals(userAclCode)) {
            throw new DataAccessDeniedException();
        }
        if (bindingEntity.getTargetXx().getXxdm().equals(userAclCode)) {
            setModel(OperationResult.buildFailureResult("不能转入自己学校"));
            return buildDefaultHttpHeaders();
        }
        bindingEntity.setXsJbxx(xsJbxxService.findByXxdmAndXh(AuthContextHolder.getAclCode(), bindingEntity.getXsJbxx()
                .getXh()));
        bindingEntity.setTargetXx(xxJcxxService.findByXxdm(bindingEntity.getTargetXx().getXxdm()));
        return super.doUpdate();
    }

    public boolean isDisallowSubmit() {
        if (bindingEntity.isNew()) {
            return true;
        }
        if (!bindingEntity.getState().equals(XsTransferReqStateEnum.S10DRAFT)) {
            return true;
        }
        return false;
    }

    @MetaData(title = "提请审批")
    public HttpHeaders doSubmit() {
        if (isDisallowSubmit()) {
            throw new DataOperationDeniedException();
        }
        String userAclCode = AuthContextHolder.getAclCode();
        if (!bindingEntity.getSourceXx().getXxdm().equals(userAclCode)) {
            throw new DataAccessDeniedException();
        }
        ExtRevisionListener.setOperationEvent(XsTransferReq.XsTransferReqEventEnum.SUBMIT.name());
        ExtRevisionListener.setOldState(bindingEntity.getState().name());
        ExtRevisionListener.setOperationExplain(bindingEntity.getReqExplain());
        bindingEntity.setState(XsTransferReq.XsTransferReqStateEnum.S20SBMTD);
        ExtRevisionListener.setNewState(bindingEntity.getState().name());
        
        xsTransferReqService.save(bindingEntity);
        setModel(OperationResult.buildSuccessResult("提交操作完成,请等待上级主管审批"));
        return buildDefaultHttpHeaders();
    }

    @MetaData(title = "操作记录")
    public HttpHeaders logs() {
        setModel(bindingEntity);
        return buildDefaultHttpHeaders("logs");
    }

    protected boolean isDisallowDelete(XsTransferReq entity) {
        if (entity.isNew()) {
            return true;
        }
        if (entity.getState().equals(XsTransferReqStateEnum.S10DRAFT)) {
            return false;
        }
        return true;
    }

    /**
     * 禁止删除判断
     * 
     * @return
     */
    public boolean isDisallowDelete() {
        return isDisallowDelete(bindingEntity);
    }

    /**
     * 禁止学籍转入判断
     * 
     * @return
     */
    public boolean isDisallowTransferIn() {
        if (bindingEntity.getState().equals(XsTransferReqStateEnum.S50TGTPASSD)) {
            return false;
        }
        return true;
    }

    @MetaData(title = "学籍转入接收")
    public HttpHeaders doTransferIn() {
        if (isDisallowTransferIn()) {
            throw new DataOperationDeniedException();
        }
        String userAclCode = AuthContextHolder.getAclCode();
        if (!bindingEntity.getTargetXx().getXxdm().equals(userAclCode)) {
            throw new DataAccessDeniedException();
        }
        XxBj xxBj = xxBjService.findByXxdmAndBh(userAclCode, this.getRequiredParameter("bh"));
        ExtRevisionListener.setOperationEvent(XsTransferReq.XsTransferReqEventEnum.TRANSFER.name());
        ExtRevisionListener.setOldState(bindingEntity.getState().name());
        ExtRevisionListener.setOperationExplain(bindingEntity.getReqExplain());
        bindingEntity.setState(XsTransferReq.XsTransferReqStateEnum.S80TRANSED);
        ExtRevisionListener.setNewState(bindingEntity.getState().name());
        xsTransferReqService.transferIn(xxBj, this.getRequiredParameter("xh"), bindingEntity);
        setModel(OperationResult.buildSuccessResult("学籍转入接收操作完成"));
        return buildDefaultHttpHeaders();
    }

    @Override
    @MetaData(title = "版本数据列表")
    public HttpHeaders revisionList() {
        return super.revisionList();
    }

    @Override
    @MetaData(title = "版本数据对比")
    public HttpHeaders revisionCompare() {
        return super.revisionCompare();
    }
}