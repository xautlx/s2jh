package lab.s2jh.sys.dao;

import java.util.List;

import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.sys.entity.AttachmentFile;

import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentFileDao extends BaseDao<AttachmentFile, String> {
    List<AttachmentFile> findByEntityClassNameAndEntityId(String entityClassName, String entityId);
}
