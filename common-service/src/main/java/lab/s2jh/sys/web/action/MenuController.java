package lab.s2jh.sys.web.action;

import java.util.List;
import java.util.Map;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.sys.entity.Menu;
import lab.s2jh.sys.service.MenuService;

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
            groupPropertyFilter.and(new PropertyFilter(MatchType.NU, "parent.id", true));
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
        return super.doSave();
    }

    @Override
    @MetaData(value = "删除")
    public HttpHeaders doDelete() {
        return super.doDelete();
    }

    @MetaData(value = "树形表格数据")
    public HttpHeaders treeGridData() {
        Map<String, Menu> menuDatas = Maps.newLinkedHashMap();
        loopTreeGridData(menuDatas, menuService.findRoots());
        setModel(menuDatas.values());
        return buildDefaultHttpHeaders();
    }

    private void loopTreeGridData(Map<String, Menu> menuDatas, List<Menu> menus) {
        for (Menu menu : menus) {
            Menu parent = menu.getParent();
            List<Menu> children = menuService.findChildren(menu);
            menu.addExtraAttribute("level", menu.getLevel());
            menu.addExtraAttribute("parent", parent == null ? "" : parent.getId());
            menu.addExtraAttribute("isLeaf", CollectionUtils.isEmpty(children) ? true : false);
            menu.addExtraAttribute("expanded", true);
            menu.addExtraAttribute("loaded", true);
            menuDatas.put(menu.getId(), menu);
            if (!CollectionUtils.isEmpty(children)) {
                loopTreeGridData(menuDatas, children);
            }
        }
    }
    

    @MetaData(value = "列表")
    public HttpHeaders list() {
        List<Map<String, Object>> menuList = Lists.newArrayList();
        Iterable<Menu> menus = menuService.findRoots();
        for (Menu menu : menus) {
            loopMenu(menuList, menu);
        }
        List<Map<String, Object>> rootList = Lists.newArrayList();
        Map<String, Object> root = Maps.newHashMap();
        rootList.add(root);
        root.put("id", "");
        root.put("name", "根节点");
        root.put("open", true);
        root.put("disabled", false);
        root.put("children", menuList);
        setModel(rootList);
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
