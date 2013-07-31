package lab.s2jh.rpt.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import lab.s2jh.auth.dao.RoleDao;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.service.R2OperationEnum;
import lab.s2jh.rpt.dao.ReportDefDao;
import lab.s2jh.rpt.dao.ReportDefR2RoleDao;
import lab.s2jh.rpt.entity.ReportDef;
import lab.s2jh.rpt.entity.ReportDefR2Role;
import lab.s2jh.sys.dao.AttachmentFileDao;
import lab.s2jh.sys.entity.AttachmentFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class ReportDefService extends BaseService<ReportDef, String> {

    @Autowired
    private ReportDefDao reportDefDao;

    @Autowired
    private ReportDefR2RoleDao reportDefR2RoleDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AttachmentFileDao attachmentFileDao;

    @Override
    protected BaseDao<ReportDef, String> getEntityDao() {
        return reportDefDao;
    }

    @Transactional(readOnly = true)
    public ReportDef findByCode(String code) {
        return reportDefDao.findByCode(code);
    }

    @Transactional(readOnly = true)
    public List<String> findCategories() {
        return reportDefDao.findCategories();
    }

    @Transactional(readOnly = true)
    public List<ReportDefR2Role> findRelatedRoleR2s(String id) {
        return reportDefR2RoleDao.findByReportDef_Id(id);
    }

    @CacheEvict(value = "SpringSecurityCache", allEntries = true)
    public void updateRelatedRoleR2s(String id, Collection<String> roleIds, R2OperationEnum op) {
        updateRelatedR2s(id, roleIds, "reportDefR2Roles", "role", op);
    }

    @Transactional(readOnly = true)
    public AttachmentFile findRelatedAttachmentFile(String id) {
        List<AttachmentFile> attachmentFiles = attachmentFileDao.findByEntityClassNameAndEntityId(
                ReportDef.class.getName(), id);
        if (CollectionUtils.isEmpty(attachmentFiles)) {
            return null;
        } else {
            Assert.isTrue(attachmentFiles.size() == 1);
            return attachmentFiles.get(0);
        }
    }

    @Override
    public ReportDef save(ReportDef entity) {
        AttachmentFile attachmentFile = entity.getTemplateFile();
        if (attachmentFile != null) {
            attachmentFile.setLastTouchTime(new Date());
            attachmentFile.setLastTouchBy(AuthContextHolder.getAuthUserPin());
            attachmentFileDao.save(attachmentFile);
        }
        return super.save(entity);
    }
}
