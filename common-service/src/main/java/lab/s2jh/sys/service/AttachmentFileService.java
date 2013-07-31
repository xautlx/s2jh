package lab.s2jh.sys.service;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.sys.dao.AttachmentFileDao;
import lab.s2jh.sys.entity.AttachmentFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttachmentFileService extends BaseService<AttachmentFile, String> {

    @Autowired
    private AttachmentFileDao attachmentFileDao;

    @Override
    protected BaseDao<AttachmentFile, String> getEntityDao() {
        return attachmentFileDao;
    }
}
