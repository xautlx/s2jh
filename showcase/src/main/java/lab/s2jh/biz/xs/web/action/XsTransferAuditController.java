package lab.s2jh.biz.xs.web.action;

import java.util.Date;
import java.util.Map;

import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.xs.entity.XsTransferReq;
import lab.s2jh.biz.xs.entity.XsTransferReq.XsTransferReqStateEnum;
import lab.s2jh.biz.xs.service.XsJbxxService;
import lab.s2jh.biz.xs.service.XsTransferReqService;
import lab.s2jh.biz.xx.service.XxJcxxService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.audit.envers.ExtRevisionListener;
import lab.s2jh.core.exception.DataOperationDeniedException;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.util.EnumUtil;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

@MetaData(title = "学籍异动(转学)审批")
public class XsTransferAuditController extends BaseBizController<XsTransferReq, String> {

    @Autowired
    private XsTransferReqService xsTransferReqService;

    @Autowired
    private XxJcxxService xxJcxxService;

    @Autowired
    private XsJbxxService xsJbxxService;

    @Autowired
    private AclService aclService;

    @Override
    protected BaseService<XsTransferReq, String> getEntityService() {
        return xsTransferReqService;
    }

    @Override
    protected void checkEntityAclPermission(XsTransferReq entity) {
        aclService.validateAuthUserAclCodePermission(entity.getSourceXx().getXxdm(), entity.getTargetXx().getXxdm());
    }

    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    @Override
    @MetaData(title = "更新")
    public HttpHeaders doUpdate() {
        return super.doUpdate();
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupFilter) {
        super.appendFilterProperty(groupFilter);
        String userAclCode = AuthContextHolder.getAclCode();
        groupFilter.and(new PropertyFilter(MatchType.NE, "state", XsTransferReqStateEnum.S10DRAFT));
        groupFilter.and(new PropertyFilter(MatchType.EQ, new String[] { "sourceXx.sszgdwm", "targetXx.sszgdwm" },
                userAclCode));
    }

    public Map<String, String> getXsTransferReqAuditStateMap() {
        Map<String, String> enumDataMap = Maps.newLinkedHashMap();
        enumDataMap.putAll(EnumUtil.getEnumDataMap(XsTransferReqStateEnum.class));
        enumDataMap.remove(XsTransferReqStateEnum.S10DRAFT.name());
        return enumDataMap;
    }

    public boolean isDisallowAudit() {
        String userAclCode = AuthContextHolder.getAclCode();
        if (bindingEntity.getState().equals(XsTransferReqStateEnum.S20SBMTD)
                && bindingEntity.getSourceXx().getSszgdwm().equals(userAclCode)) {
            return false;
        }
        if (bindingEntity.getState().equals(XsTransferReqStateEnum.S30SRCPASSD)
                && bindingEntity.getTargetXx().getSszgdwm().equals(userAclCode)) {
            return false;
        }
        return true;
    }

    @MetaData(title = "主管审批")
    public HttpHeaders doAudit() {
        if (isDisallowAudit()) {
            throw new DataOperationDeniedException();
        }
        //审计记录原状态
        ExtRevisionListener.setOldState(bindingEntity.getState().name());
        //审计记录业务操作说明
        ExtRevisionListener.setOperationExplain(bindingEntity.getAuditExplain());
        boolean auditResult = BooleanUtils.toBoolean(this.getRequiredParameter("auditResult"));
        String userAclCode = AuthContextHolder.getAclCode();
        if (bindingEntity.getState().equals(XsTransferReqStateEnum.S20SBMTD)
                && bindingEntity.getSourceXx().getSszgdwm().equals(userAclCode)) {
            bindingEntity.setState(auditResult ? XsTransferReqStateEnum.S30SRCPASSD
                    : XsTransferReqStateEnum.S40SRCNPASS);
            //审计记录操作动作
            ExtRevisionListener.setOperationEvent(XsTransferReq.XsTransferReqEventEnum.SRC_AUDIT.name());
        } else if (bindingEntity.getState().equals(XsTransferReqStateEnum.S30SRCPASSD)
                && bindingEntity.getTargetXx().getSszgdwm().equals(userAclCode)) {
            bindingEntity.setState(auditResult ? XsTransferReqStateEnum.S50TGTPASSD
                    : XsTransferReqStateEnum.S60TGTNPASS);
            //审计记录操作动作
            ExtRevisionListener.setOperationEvent(XsTransferReq.XsTransferReqEventEnum.TGT_AUDIT.name());
        }
        //审计记录新状态
        ExtRevisionListener.setNewState(bindingEntity.getState().name());
        bindingEntity.setLastAuditTime(new Date());
        xsTransferReqService.save(bindingEntity);
        setModel(OperationResult.buildSuccessResult("审批操作完成"));
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