package lab.s2jh.biz.finance.web.action;

import lab.s2jh.biz.finance.entity.AccountSubject;
import lab.s2jh.biz.finance.service.AccountSubjectService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.web.action.BaseController;

import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

@MetaData("会计科目管理")
public class AccountSubjectController extends BaseController<AccountSubject, Long> {

    @Autowired
    private AccountSubjectService accountSubjectService;

    @Override
    protected BaseService<AccountSubject, Long> getEntityService() {
        return accountSubjectService;
    }

    @Override
    protected void checkEntityAclPermission(AccountSubject entity) {
        // TODO Add acl check code logic
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupPropertyFilter) {
        if (groupPropertyFilter.isEmpty()) {
            groupPropertyFilter.forceAnd(new PropertyFilter(MatchType.NU, "parent.id", true));
        }
        super.appendFilterProperty(groupPropertyFilter);
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
}