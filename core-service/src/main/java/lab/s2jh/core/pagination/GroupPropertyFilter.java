/**
 * Copyright (c) 2012
 */
package lab.s2jh.core.pagination;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.exception.ServiceException;
import lab.s2jh.core.web.json.JacksonMapperFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * 用于jqGrid自定义高级查询条件封装条件组合
 */
public class GroupPropertyFilter {
    public static final String GROUP_OPERATION_AND = "and";
    public static final String GROUP_OPERATION_OR = "or";

    /** 组合类型:AND/OR */
    private String groupType = GROUP_OPERATION_AND;

    /** 组合条件列表 */
    private List<PropertyFilter> filters = Lists.newArrayList();

    /** 额外AND条件 */
    private List<PropertyFilter> appendAndFilters = Lists.newArrayList();

    /** 额外OR条件 */
    private List<PropertyFilter> appendOrFilters = Lists.newArrayList();

    /** 组合条件组 */
    private List<GroupPropertyFilter> groups = Lists.newArrayList();

    public List<GroupPropertyFilter> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupPropertyFilter> groups) {
        this.groups = groups;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public List<PropertyFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<PropertyFilter> filters) {
        this.filters = filters;
    }

    public GroupPropertyFilter() {
    }

    public List<PropertyFilter> getAppendAndFilters() {
        return appendAndFilters;
    }

    public void setAppendAndFilters(List<PropertyFilter> appendAndFilters) {
        this.appendAndFilters = appendAndFilters;
    }

    public List<PropertyFilter> getAppendOrFilters() {
        return appendOrFilters;
    }

    public void setAppendOrFilters(List<PropertyFilter> appendOrFilters) {
        this.appendOrFilters = appendOrFilters;
    }

    public GroupPropertyFilter(String groupType) {
        this.groupType = groupType;
    }

    public GroupPropertyFilter and(PropertyFilter filter) {
        appendAndFilters.add(filter);
        return this;
    }

    public GroupPropertyFilter or(PropertyFilter filter) {
        appendOrFilters.add(filter);
        return this;
    }

    public static GroupPropertyFilter buildGroupFilterFromHttpRequest(Class<?> entityClass, HttpServletRequest request) {

        try {
            String filtersJson = request.getParameter("filters");

            GroupPropertyFilter groupPropertyFilter = new GroupPropertyFilter();

            if (StringUtils.isNotBlank(filtersJson)) {
                JqGridSearchFilter jqFilter = JacksonMapperFactory.getObjectMapper().readValue(filtersJson,
                        JqGridSearchFilter.class);
                convertJqGridToFilter(entityClass, groupPropertyFilter, jqFilter);
            }

            List<PropertyFilter> filters = PropertyFilter.buildFiltersFromHttpRequest(entityClass, request);
            if (CollectionUtils.isNotEmpty(filters)) {

                GroupPropertyFilter comboGroupPropertyFilter = new GroupPropertyFilter();
                comboGroupPropertyFilter.setGroupType(GROUP_OPERATION_AND);

                GroupPropertyFilter normalGroupPropertyFilter = new GroupPropertyFilter();
                normalGroupPropertyFilter.setGroupType(GROUP_OPERATION_AND);
                normalGroupPropertyFilter.setFilters(filters);
                comboGroupPropertyFilter.getGroups().add(normalGroupPropertyFilter);

                comboGroupPropertyFilter.getGroups().add(groupPropertyFilter);
                return comboGroupPropertyFilter;
            }

            return groupPropertyFilter;

        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private static void convertJqGridToFilter(Class<?> entityClass, GroupPropertyFilter jqGroupPropertyFilter,
            JqGridSearchFilter jqFilter) {
        jqGroupPropertyFilter.setGroupType(jqFilter.getGroupOp().equalsIgnoreCase("OR") ? GROUP_OPERATION_OR
                : GROUP_OPERATION_AND);

        List<JqGridSearchRule> rules = jqFilter.getRules();
        List<PropertyFilter> filters = Lists.newArrayList();
        for (JqGridSearchRule rule : rules) {
            if (StringUtils.isBlank(rule.getData())) {
                continue;
            }
            PropertyFilter filter = new PropertyFilter(entityClass, rule.getOp().toUpperCase() + "_" + rule.getField(),
                    rule.getData());
            filters.add(filter);
        }
        jqGroupPropertyFilter.setFilters(filters);

        List<JqGridSearchFilter> groups = jqFilter.getGroups();
        for (JqGridSearchFilter group : groups) {
            GroupPropertyFilter jqChildGroupPropertyFilter = new GroupPropertyFilter();
            jqGroupPropertyFilter.getGroups().add(jqChildGroupPropertyFilter);
            convertJqGridToFilter(entityClass, jqChildGroupPropertyFilter, group);
        }
    }

    /**
     * filters =
     * {"groupOp":"AND","rules":[{"field":"code","op":"eq","data":"123"}]}
     */
    public static class JqGridSearchFilter {
        private String groupOp;
        private List<JqGridSearchRule> rules = Lists.newArrayList();
        private List<JqGridSearchFilter> groups = Lists.newArrayList();

        public List<JqGridSearchFilter> getGroups() {
            return groups;
        }

        public void setGroups(List<JqGridSearchFilter> groups) {
            this.groups = groups;
        }

        public String getGroupOp() {
            return groupOp;
        }

        public void setGroupOp(String groupOp) {
            this.groupOp = groupOp;
        }

        public List<JqGridSearchRule> getRules() {
            return rules;
        }

        public void setRules(List<JqGridSearchRule> rules) {
            this.rules = rules;
        }
    }

    public static class JqGridSearchRule {
        private String field;

        /**
         * [
         * 'eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc'
         * ] The corresponding texts are in language file and mean the
         * following: ['equal','not equal', 'less', 'less or
         * equal','greater','greater or equal', 'begins with','does not begin
         * with','is in','is not in','ends with','does not end
         * with','contains','does not contain']
         */
        private String op;
        private String data;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getOp() {
            return op;
        }

        public void setOp(String op) {
            this.op = op;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(groups) && CollectionUtils.isEmpty(filters);
    }
}
