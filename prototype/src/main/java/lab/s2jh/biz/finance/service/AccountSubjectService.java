package lab.s2jh.biz.finance.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lab.s2jh.biz.finance.dao.AccountSubjectDao;
import lab.s2jh.biz.finance.entity.AccountSubject;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.service.BaseService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class AccountSubjectService extends BaseService<AccountSubject, Long> {

    @Autowired
    private AccountSubjectDao accountSubjectDao;

    @Override
    protected BaseDao<AccountSubject, Long> getEntityDao() {
        return accountSubjectDao;
    }

    @Cacheable(value = "AccountSubjectSpringCache", key = "#root.methodName")
    public List<AccountSubject> findPaymentAccountSubjects() {
        List<AccountSubject> items = Lists.newArrayList();
        List<AccountSubject> all = accountSubjectDao.findAllCached();
        for (AccountSubject accountSubject : all) {
            if (accountSubject.getCode().equals("1001") || accountSubject.getCode().equals("1002")
                    || accountSubject.getCode().equals("1015") || accountSubject.getCode().equals("1123")) {
                items.addAll(findLeaves(null, accountSubject));
            }
        }
        Collections.sort(items);
        return items;
    }

    private List<AccountSubject> findLeaves(List<AccountSubject> leaves, AccountSubject item) {
        if (leaves == null) {
            leaves = Lists.newArrayList();
        }
        List<AccountSubject> children = findChildren(item);
        if (CollectionUtils.isEmpty(children)) {
            leaves.add(item);
        } else {
            for (AccountSubject child : children) {
                findLeaves(leaves, child);
            }
        }
        return leaves;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "AccountSubjectSpringCache", key = "{#root.methodName, #parent?.id}")
    public List<AccountSubject> findRoots() {
        List<AccountSubject> items = Lists.newArrayList();
        List<AccountSubject> allItems = accountSubjectDao.findAllCached();
        for (AccountSubject item : allItems) {
            if (item.getParent() == null) {
                items.add(item);
            }
        }
        Collections.sort(items);
        return items;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "AccountSubjectSpringCache", key = "{#root.methodName, #parent?.id}")
    public List<AccountSubject> findChildren(AccountSubject parent) {
        List<AccountSubject> children = new ArrayList<AccountSubject>();
        if (parent == null) {
            return findRoots();
        }
        List<AccountSubject> allItems = accountSubjectDao.findAllCached();
        for (AccountSubject item : allItems) {
            if (parent.equals(item.getParent())) {
                children.add(item);
            }
        }
        Collections.sort(children);
        return children;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "AccountSubjectSpringCache", key = "{#root.methodName, #code}")
    public AccountSubject findByCode(String code) {
        return findByProperty("code", code);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "AccountSubjectSpringCache", key = "{#root.methodName, #codes}")
    public List<AccountSubject> findLeavesByCodes(String... codes) {
        List<AccountSubject> accountSubjects = Lists.newArrayList();
        for (String code : codes) {
            accountSubjects.addAll(findLeaves(null, findByCode(code)));
        }
        return accountSubjects;
    }

    @Override
    @CacheEvict(value = "AccountSubjectSpringCache", allEntries = true)
    public AccountSubject save(AccountSubject entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(value = "AccountSubjectSpringCache", allEntries = true)
    public void delete(AccountSubject entity) {
        super.delete(entity);
    }

    @Override
    @CacheEvict(value = "AccountSubjectSpringCache", allEntries = true)
    public List<AccountSubject> save(Iterable<AccountSubject> entities) {
        return super.save(entities);
    }

    @Override
    @CacheEvict(value = "AccountSubjectSpringCache", allEntries = true)
    public void delete(Iterable<AccountSubject> entities) {
        super.delete(entities);
    }
}
