package lab.s2jh.rpt.web.action;

import java.util.List;

import lab.s2jh.auth.entity.Role;
import lab.s2jh.auth.service.RoleService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.annotation.SecurityControlIgnore;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.rpt.entity.ReportDef;
import lab.s2jh.rpt.entity.ReportDefR2Role;
import lab.s2jh.rpt.service.ReportDefService;
import lab.s2jh.sys.service.AttachmentFileService;
import lab.s2jh.web.action.BaseController;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@MetaData(value = "报表定义管理")
public class ReportDefController extends BaseController<ReportDef, String> {

    @Autowired
    private ReportDefService reportDefService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AttachmentFileService attachmentFileService;

    @Override
    protected BaseService<ReportDef, String> getEntityService() {
        return reportDefService;
    }

    @Override
    protected void checkEntityAclPermission(ReportDef entity) {
        // According to URL access control logic
    }

    @Override
    public void prepareCreate() {
        super.prepareCreate();
        bindingEntity.setCode("RPT_" + RandomStringUtils.randomNumeric(6));
    }

    @Override
    @MetaData(value = "创建")
    public HttpHeaders doCreate() {
        String templateFileId = this.getParameter("templateFileId");
        if (StringUtils.isNotBlank(templateFileId)) {
            bindingEntity.setTemplateFile(attachmentFileService.findOne(templateFileId));
        }
        return super.doCreate();
    }

    @Override
    @MetaData(value = "更新")
    public HttpHeaders doUpdate() {
        String templateFileId = this.getParameter("templateFileId");
        if (StringUtils.isNotBlank(templateFileId)) {
            bindingEntity.setTemplateFile(attachmentFileService.findOne(templateFileId));
        } else {
            bindingEntity.setTemplateFile(null);
        }
        return super.doUpdate();
    }

    @Override
    @MetaData(value = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData(value = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    public List<String> getCategories() {
        return reportDefService.findCategories();
    }

    @MetaData(value = "计算显示角色关联数据")
    @SecurityControlIgnore
    public HttpHeaders findRelatedRoles() {
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildFromHttpRequest(Role.class, getRequest());
        List<Role> roles = roleService.findByFilters(groupFilter, new Sort(Direction.DESC, "aclType", "code"));
        List<ReportDefR2Role> r2s = reportDefService.findRelatedRoleR2s(this.getId());
        for (Role role : roles) {
            role.addExtraAttribute("related", false);
            for (ReportDefR2Role r2 : r2s) {
                if (r2.getRole().equals(role)) {
                    role.addExtraAttribute("r2CreatedDate", r2.getCreatedDate());
                    role.addExtraAttribute("related", true);
                    break;
                }
            }
        }
        setModel(buildPageResultFromList(roles));
        return buildDefaultHttpHeaders();
    }

    @MetaData(value = "更新角色关联")
    public HttpHeaders doUpdateRelatedRoleR2s() {
        reportDefService.updateRelatedRoleR2s(getId(), getParameterIds("r2ids"));
        setModel(OperationResult.buildSuccessResult("角色关联操作完成"));
        return buildDefaultHttpHeaders();
    }
}