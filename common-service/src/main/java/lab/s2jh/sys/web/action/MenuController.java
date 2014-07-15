package lab.s2jh.sys.web.action;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.service.Validation;
import lab.s2jh.sys.entity.Menu;
import lab.s2jh.sys.service.MenuService;
import lab.s2jh.web.action.BaseController;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MenuController extends BaseController<Menu, String> {

    @Autowired
    private MenuService menuService;

    @Override
    protected BaseService<Menu, String> getEntityService() {
        return menuService;
    }

    @Override
    protected void checkEntityAclPermission(Menu entity) {
        //Do nothing check
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupPropertyFilter) {
        if (groupPropertyFilter.isEmpty()) {
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
    @MetaData(value = "保存")
    public HttpHeaders doSave() {
        Validation.notDemoMode();
        return super.doSave();
    }

    @Override
    @MetaData(value = "删除")
    public HttpHeaders doDelete() {
        Validation.notDemoMode();
        return super.doDelete();
    }

    @MetaData(value = "列表")
    public HttpHeaders list() {
        List<Map<String, Object>> menuList = Lists.newArrayList();
        Iterable<Menu> menus = menuService.findRoots();
        for (Menu menu : menus) {
            loopMenu(menuList, menu);
        }
        setModel(menuList);
        return buildDefaultHttpHeaders();
    }

    private void loopMenu(List<Map<String, Object>> menuList, Menu menu) {
        Map<String, Object> row = Maps.newHashMap();
        menuList.add(row);
        row.put("id", menu.getId());
        row.put("name", menu.getTitle());
        row.put("open", menu.getInitOpen());
        row.put("disabled", menu.getDisabled());
        List<Menu> children = menu.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            List<Map<String, Object>> childrenList = Lists.newArrayList();
            row.put("children", childrenList);
            for (Menu child : children) {
                loopMenu(childrenList, child);
            }
        }
    }
}
