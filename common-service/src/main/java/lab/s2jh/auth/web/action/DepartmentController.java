package lab.s2jh.auth.web.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lab.s2jh.auth.entity.Department;
import lab.s2jh.auth.entity.User;
import lab.s2jh.auth.service.DepartmentService;
import lab.s2jh.auth.service.UserService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.web.action.BaseController;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@MetaData(value = "部门管理")
public class DepartmentController extends BaseController<Department, String> {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Override
    protected BaseService<Department, String> getEntityService() {
        return departmentService;
    }

    @Override
    protected void checkEntityAclPermission(Department entity) {
        //Do nothing check
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupPropertyFilter) {
        if (groupPropertyFilter.isEmptySearch()) {
            groupPropertyFilter.forceAnd(new PropertyFilter(MatchType.NU, "parent", true));
        }
        super.appendFilterProperty(groupPropertyFilter);
    }

    @Override
    @MetaData(value = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    @Override
    @MetaData(value = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @Override
    @MetaData(value = "保存")
    public HttpHeaders doSave() {
        return super.doSave();
    }

    @MetaData(value = "树形结构数据")
    public HttpHeaders treeData() {
        List<Map<String, Object>> treeDatas = Lists.newArrayList();
        Iterable<Department> items = departmentService.findRoots();
        for (Department item : items) {
            loopTreeData(treeDatas, item);
        }
        setModel(treeDatas);
        return buildDefaultHttpHeaders();
    }

    private void loopTreeData(List<Map<String, Object>> treeDatas, Department item) {
        Map<String, Object> row = Maps.newHashMap();
        treeDatas.add(row);
        row.put("id", item.getId());
        row.put("name", item.getDisplay());
        row.put("open", false);
        List<Department> children = departmentService.findChildren(item);
        if (!CollectionUtils.isEmpty(children)) {
            List<Map<String, Object>> childrenList = Lists.newArrayList();
            row.put("children", childrenList);
            for (Department child : children) {
                loopTreeData(childrenList, child);
            }
        }
    }

    public Map<Long, String> getUsersMap() {
        Map<Long, String> dataMap = new LinkedHashMap<Long, String>();
        List<User> items = userService.findUsersEnabled();
        for (User item : items) {
            dataMap.put(item.getId(), item.getDisplay());
        }
        return dataMap;
    }
}