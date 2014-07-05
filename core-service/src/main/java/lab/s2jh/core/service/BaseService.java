package lab.s2jh.core.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.audit.envers.EntityRevision;
import lab.s2jh.core.audit.envers.ExtDefaultRevisionEntity;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.exception.ServiceException;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Transactional
public abstract class BaseService<T extends Persistable<? extends Serializable>, ID extends Serializable> {

    private final Logger logger = LoggerFactory.getLogger(BaseService.class);

    /** 泛型对应的Class定义 */
    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    /** 子类设置具体的DAO对象实例 */
    abstract protected BaseDao<T, ID> getEntityDao();

    @SuppressWarnings("unchecked")
    public BaseService() {
        super();
        // 通过反射取得Entity的Class.
        try {
            Object genericClz = getClass().getGenericSuperclass();
            if (genericClz instanceof ParameterizedType) {
                entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
            }
        } catch (Exception e) {
            logger.error("error detail:", e);
        }
    }

    /**
     * 创建数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     * 
     * @param entity
     *            待创建数据对象
     */
    protected void preInsert(T entity) {

    }

    /**
     * 更新数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     * 
     * @param entity
     *            待更新数据对象
     */
    protected void preUpdate(T entity) {

    }

    /**
     * 数据保存操作
     * 
     * @param entity
     * @return
     */
    public T save(T entity) {
        if (entity.isNew()) {
            preInsert(entity);
        } else {
            preUpdate(entity);
        }
        return getEntityDao().save(entity);
    }

    /**
     * 批量数据保存操作 其实现只是简单循环集合每个元素调用 {@link #save(Persistable)}
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     * 
     * @param entities
     *            待批量操作数据集合
     * @return
     */
    public List<T> save(Iterable<T> entities) {
        List<T> result = new ArrayList<T>();
        if (entities == null) {
            return result;
        }
        for (T entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    /**
     * 基于主键查询单一数据对象
     * 
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public T findOne(ID id) {
        Assert.notNull(id);
        return getEntityDao().findOne(id);
    }

    /**
     * 基于主键查询单一数据对象
     * 
     * @param id 主键
     * @param initLazyPropertyNames 需要预先初始化的lazy集合属性名称
     * @return
     */
    @Transactional(readOnly = true)
    public T findDetachedOne(ID id, String... initLazyPropertyNames) {
        Assert.notNull(id);
        T entity = getEntityDao().findOne(id);
        if (initLazyPropertyNames != null && initLazyPropertyNames.length > 0) {
            for (String name : initLazyPropertyNames) {
                try {
                    Object propValue = MethodUtils.invokeMethod(entity, "get" + StringUtils.capitalize(name));
                    if (propValue != null && propValue instanceof Collection<?>) {
                        ((Collection<?>) propValue).size();
                    } else if (propValue != null && propValue instanceof Persistable<?>) {
                        ((Persistable<?>) propValue).getId();
                    }
                } catch (Exception e) {
                    throw new ServiceException("error.init.detached.entity", e);
                }
            }
        }
        entityManager.detach(entity);
        return entity;
    }

    /**
     * 基于主键集合查询集合数据对象
     * 
     * @param ids 主键集合
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findAll(final ID... ids) {
        Assert.isTrue(ids != null && ids.length > 0, "必须提供有效查询主键集合");
        Specification<T> spec = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                @SuppressWarnings("rawtypes")
                Path expression = root.get("id");
                return expression.in(ids);
            }
        };
        return this.getEntityDao().findAll(spec);
    }

    /**
     * 数据删除操作
     * 
     * @param entity
     *            待操作数据
     */
    public void delete(T entity) {
        getEntityDao().delete(entity);
    }

    /**
     * 批量数据删除操作 其实现只是简单循环集合每个元素调用 {@link #delete(Persistable)}
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     * 
     * @param entities
     *            待批量操作数据集合
     * @return
     */
    public void delete(Iterable<T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    /**
     * 根据泛型对象属性和值查询唯一对象
     * 
     * @param property 属性名，即对象中数量变量名称
     * @param value 参数值
     * @return 未查询到返回null，如果查询到多条数据则抛出异常
     */
    public T findByProperty(final String property, final Object value) {
        Specification<T> spec = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                @SuppressWarnings("rawtypes")
                Path expression = root.get(property);
                return builder.equal(expression, value);
            }
        };

        List<T> entities = this.getEntityDao().findAll(spec);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        } else {
            Assert.isTrue(entities.size() == 1);
            return entities.get(0);
        }
    }

    /**
     * 根据泛型对象属性和值查询唯一对象
     * 
     * @param property 属性名，即对象中数量变量名称
     * @param value 参数值
     * @return 未查询到返回null，如果查询到多条数据则返回第一条
     */
    public T findFirstByProperty(final String property, final Object value) {
        Specification<T> spec = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                @SuppressWarnings("rawtypes")
                Path expression = root.get(property);
                return builder.equal(expression, value);
            }
        };

        List<T> entities = this.getEntityDao().findAll(spec);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        } else {
            return entities.get(0);
        }
    }

    /**
     * 通用的对象属性和值查询接口，根据泛型参数确定返回类型数据
     * 
     * @param baseDao
     *            泛型参数对象DAO接口
     * @param property
     *            属性名，即对象中数量变量名称
     * @param value
     *            参数值
     * @return 未查询到返回null，如果查询到多条数据则抛出异常
     */
    public <X> X findByProperty(BaseDao<X, ID> baseDao, final String property, final Object value) {
        Specification<X> spec = new Specification<X>() {
            @Override
            public Predicate toPredicate(Root<X> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                String[] names = StringUtils.split(property, ".");
                @SuppressWarnings("rawtypes")
                Path expression = root.get(names[0]);
                for (int i = 1; i < names.length; i++) {
                    expression = expression.get(names[i]);
                }
                return builder.equal(expression, value);
            }
        };
        List<X> entities = baseDao.findAll(spec);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        } else {
            Assert.isTrue(entities.size() == 1);
            return entities.get(0);
        }
    }

    /**
     * 单一条件对象查询数据集合
     * 
     * @param propertyFilter
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findByFilter(PropertyFilter propertyFilter) {
        GroupPropertyFilter groupPropertyFilter = GroupPropertyFilter.buildDefaultAndGroupFilter(propertyFilter);
        Specification<T> spec = buildSpecification(groupPropertyFilter);
        return getEntityDao().findAll(spec);
    }

    /**
     * 基于动态组合条件对象查询数据集合
     * 
     * @param groupPropertyFilter
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findByFilters(GroupPropertyFilter groupPropertyFilter) {
        Specification<T> spec = buildSpecification(groupPropertyFilter);
        return getEntityDao().findAll(spec);
    }

    /**
     * 基于动态组合条件对象和排序定义查询数据集合
     * 
     * @param groupPropertyFilter
     * @param sort
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findByFilters(GroupPropertyFilter groupPropertyFilter, Sort sort) {
        Specification<T> spec = buildSpecification(groupPropertyFilter);
        return getEntityDao().findAll(spec, sort);
    }

    @Transactional(readOnly = true)
    public <X extends Persistable> List<X> findByFilters(Class<X> clazz, GroupPropertyFilter groupPropertyFilter,
            Sort sort) {
        Specification<X> spec = buildSpecification(groupPropertyFilter);
        return ((BaseDao) spec).findAll(spec, sort);
    }

    /**
    * 基于动态组合条件对象和排序定义，限制查询数查询数据集合
    * 主要用于Autocomplete这样的查询避免返回太多数据
    * @param groupPropertyFilter
    * @param sort
    * @return
    */
    @Transactional(readOnly = true)
    public List<T> findByFilters(GroupPropertyFilter groupPropertyFilter, Sort sort, int limit) {
        Pageable pageable = new PageRequest(0, limit, sort);
        return findByPage(groupPropertyFilter, pageable).getContent();
    }

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     * 
     * @param groupPropertyFilter
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<T> findByPage(GroupPropertyFilter groupPropertyFilter, Pageable pageable) {
        Specification<T> specifications = buildSpecification(groupPropertyFilter);
        return getEntityDao().findAll(specifications, pageable);
    }

    public String toSql(Criteria criteria) {
        CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
        SessionImplementor session = criteriaImpl.getSession();
        SessionFactoryImplementor factory = session.getFactory();
        CriteriaQueryTranslator translator = new CriteriaQueryTranslator(factory, criteriaImpl,
                criteriaImpl.getEntityOrClassName(), CriteriaQueryTranslator.ROOT_SQL_ALIAS);
        String[] implementors = factory.getImplementors(criteriaImpl.getEntityOrClassName());

        CriteriaJoinWalker walker = new CriteriaJoinWalker(
                (OuterJoinLoadable) factory.getEntityPersister(implementors[0]), translator, factory, criteriaImpl,
                criteriaImpl.getEntityOrClassName(), session.getLoadQueryInfluencers());

        String sql = walker.getSQLString();
        return sql;
    }

    private class GroupAggregateProperty {
        @MetaData(value = "字面属性", comments = "最后用于前端JSON输出的key")
        private String label;
        @MetaData(value = "JPA表达式", comments = "传入JPA CriteriaBuilder组装的内容")
        private String name;
        @MetaData(value = "JPA表达式alias", comments = "用于获取聚合值的别名")
        private String alias;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

    }

    /**
     * 分组聚合统计，常用于类似按账期时间段统计商品销售利润，按会计科目总帐统计等
     * 
     * @param groupFilter 过滤参数对象
     * @param pageable 分页排序参数对象，TODO：目前有个限制未实现总记录数处理，直接返回一个固定大数字
     * @param properties 属性集合，判断规则：属性名称包含"("则标识为聚合属性，其余为分组属性 
     * @return Map结构的集合分页对象
     */
    @Transactional(readOnly = false)
    public Page<Map<String, Object>> findByGroupAggregate(GroupPropertyFilter groupFilter, Pageable pageable,
            String... properties) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<T> root = criteriaQuery.from(entityClass);

        //挑出分组和聚合属性组，以是否存在“(”作为标识
        List<GroupAggregateProperty> groupProperties = Lists.newArrayList();
        List<GroupAggregateProperty> aggregateProperties = Lists.newArrayList();
        for (String prop : properties) {
            GroupAggregateProperty groupAggregateProperty = new GroupAggregateProperty();
            //聚合类型表达式
            if (prop.indexOf("(") > -1) {
                //处理as别名
                prop = prop.replace(" AS ", " as ").replace(" As ", " as ").replace(" aS ", " as ");
                String[] splits = prop.split(" as ");
                String alias = null;
                String name = null;
                if (splits.length > 1) {
                    name = splits[0].trim();
                    alias = splits[1].trim();
                    groupAggregateProperty.setAlias(alias);
                    groupAggregateProperty.setLabel(alias);
                    groupAggregateProperty.setName(name);
                } else {
                    name = splits[0].trim();
                    alias = fixCleanAlias(name);
                    groupAggregateProperty.setAlias(alias);
                    groupAggregateProperty.setLabel(name);
                    groupAggregateProperty.setName(name);
                }
                aggregateProperties.add(groupAggregateProperty);
            } else {
                //直接的属性表达式
                groupAggregateProperty.setAlias(fixCleanAlias(prop));
                groupAggregateProperty.setLabel(prop);
                groupAggregateProperty.setName(prop);
                groupProperties.add(groupAggregateProperty);
            }
        }

        //构建JPA Expression
        Expression<?>[] groupExpressions = buildExpressions(root, criteriaBuilder, groupProperties);
        Expression<?>[] aggregateExpressions = buildExpressions(root, criteriaBuilder, aggregateProperties);
        Expression<?>[] selectExpressions = ArrayUtils.addAll(groupExpressions, aggregateExpressions);
        CriteriaQuery<Tuple> select = criteriaQuery.multiselect(selectExpressions);

        //基于前端动态条件对象动态where条件组装
        Predicate where = buildPredicatesFromFilters(groupFilter, root, criteriaQuery, criteriaBuilder, false);
        if (where != null) {
            select.where(where);
        }
      //基于前端动态条件对象动态having条件组装
        Predicate having = buildPredicatesFromFilters(groupFilter, root, criteriaQuery, criteriaBuilder, true);
        if (having != null) {
            select.having(having);
        }

        //分页和排序处理
        if (pageable != null && pageable.getSort() != null) {
            Sort sort = pageable.getSort();
            Order order = sort.iterator().next();
            String prop = order.getProperty();
            String alias = fixCleanAlias(prop);
            //目前发现JPA不支持传入alias作为排序属性，因此只能基于alias找到匹配的Expression表达式作为排序参数
            List<Selection<?>> selections = select.getSelection().getCompoundSelectionItems();
            for (Selection<?> selection : selections) {
                if (selection.getAlias().equals(alias)) {
                    if (order.isAscending()) {
                        select.orderBy(criteriaBuilder.desc((Expression<?>) selection));
                    } else {
                        select.orderBy(criteriaBuilder.asc((Expression<?>) selection));
                    }
                    break;
                }
            }
        }

        //追加分组参数
        select.groupBy(groupExpressions);

        //创建查询对象
        TypedQuery<Tuple> query = entityManager.createQuery(select);
        //动态追加分页参数
        if (pageable != null) {
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        //获取结果集合，并组装为前端便于JSON序列化的Map结构
        List<Tuple> tuples = query.getResultList();
        List<Map<String, Object>> mapDatas = Lists.newArrayList();
        for (Tuple tuple : tuples) {
            Map<String, Object> data = Maps.newHashMap();
            for (GroupAggregateProperty groupAggregateProperty : groupProperties) {
                data.put(groupAggregateProperty.getLabel(), tuple.get(groupAggregateProperty.getAlias()));
            }
            for (GroupAggregateProperty groupAggregateProperty : aggregateProperties) {
                data.put(groupAggregateProperty.getLabel(), tuple.get(groupAggregateProperty.getAlias()));
            }
            mapDatas.add(data);
        }
 
        //TODO：目前有个限制未实现总记录数处理，直接返回一个固定大数字
        return new PageImpl(mapDatas, pageable, Integer.MAX_VALUE);
    }

    /**
     * 基于Native SQL和分页(不含排序，排序直接在native sql中定义)对象查询数据集合
     * 
     * @param pageable 分页(不含排序，排序直接在native sql中定义)对象
     * @param sql Native SQL(自行组装好动态条件和排序的原生SQL语句，不含order by部分)
     * @return Map结构的集合分页对象
     */
    @Transactional(readOnly = true)
    public Page<Map> findByPageNativeSQL(Pageable pageable, String sql) {
        return findByPageNativeSQL(pageable, sql, null);
    }

    /**
     * 基于Native SQL和分页(不含排序，排序直接在native sql中定义)对象查询数据集合
     * 
     * @param pageable 分页(不含排序，排序直接在native sql中定义)对象
     * @param sql Native SQL(自行组装好动态条件和排序的原生SQL语句，不含order by部分)
     * @param orderby order by部分
     * @return Map结构的集合分页对象
     */
    @Transactional(readOnly = true)
    public Page<Map> findByPageNativeSQL(Pageable pageable, String sql, String orderby) {
        Query query = null;
        if (StringUtils.isNotBlank(orderby)) {
            query = entityManager.createNativeQuery(sql + " " + orderby);
        } else {
            query = entityManager.createNativeQuery(sql);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Query queryCount = entityManager.createNativeQuery("select count(*) from (" + sql + ") cnt");
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Object count = queryCount.getSingleResult();
        return new PageImpl(query.getResultList(), pageable, Long.valueOf(count.toString()));
    }

    /**
     * 基于JPA通用的查询条件count记录数据
     * 
     * @param spec
     * @return
     */
    @Transactional(readOnly = true)
    public long count(Specification<T> spec) {
        return getEntityDao().count(spec);
    }

    @SuppressWarnings("unchecked")
    private <X> Predicate buildPredicate(String propertyName, PropertyFilter filter, Root<X> root,
            CriteriaQuery<?> query, CriteriaBuilder builder, Boolean having) {
        Object matchValue = filter.getMatchValue();
        String[] names = StringUtils.split(propertyName, ".");

        if (matchValue == null) {
            return null;
        }
        if (having && propertyName.indexOf("(") == -1) {
            return null;
        }
        if (!having && propertyName.indexOf("(") > -1) {
            return null;
        }
        if (matchValue instanceof String) {
            if (StringUtils.isBlank(String.valueOf(matchValue))) {
                return null;
            }
        }

        if (filter.getMatchType().equals(MatchType.FETCH)) {
            if (names.length == 1) {
                JoinType joinType = JoinType.INNER;
                if (matchValue instanceof String) {
                    joinType = Enum.valueOf(JoinType.class, (String) matchValue);
                } else {
                    joinType = (JoinType) filter.getMatchValue();
                }

                // Hack for Bug: https://jira.springsource.org/browse/DATAJPA-105
                // 如果是在count计算总记录，则添加join；否则说明正常分页查询添加fetch
                if (!Long.class.isAssignableFrom(query.getResultType())) {
                    root.fetch(names[0], joinType);
                } else {
                    root.join(names[0], joinType);
                }
            } else {
                JoinType[] joinTypes = new JoinType[names.length];
                if (matchValue instanceof String) {
                    String[] joinTypeSplits = StringUtils.split(String.valueOf(matchValue), ".");
                    Assert.isTrue(joinTypeSplits.length == names.length, filter.getMatchType() + " 操作属性个数和Join操作个数必须一致");
                    for (int i = 0; i < joinTypeSplits.length; i++) {
                        joinTypes[i] = Enum.valueOf(JoinType.class, joinTypeSplits[i].trim());
                    }
                } else {
                    joinTypes = (JoinType[]) filter.getMatchValue();
                    Assert.isTrue(joinTypes.length == names.length);
                }

                // Hack for Bug: https://jira.springsource.org/browse/DATAJPA-105
                // 如果是在count计算总记录，则添加join；否则说明正常分页查询添加fetch
                if (!Long.class.isAssignableFrom(query.getResultType())) {
                    Fetch fetch = root.fetch(names[0], joinTypes[0]);
                    for (int i = 1; i < names.length; i++) {
                        fetch.fetch(names[i], joinTypes[i]);
                    }
                } else {
                    Join join = root.join(names[0], joinTypes[0]);
                    for (int i = 1; i < names.length; i++) {
                        join.join(names[i], joinTypes[i]);
                    }
                }
            }

            return null;
        }

        Predicate predicate = null;
        Expression expression = null;

        // 处理集合子查询
        Subquery<Long> subquery = null;
        Root subQueryFrom = null;
        if (filter.getSubQueryCollectionPropetyType() != null) {
            subquery = query.subquery(Long.class);
            subQueryFrom = subquery.from(filter.getSubQueryCollectionPropetyType());
            Path path = subQueryFrom.get(names[1]);
            if (names.length > 2) {
                for (int i = 2; i < names.length; i++) {
                    path = path.get(names[i]);
                }
            }
            expression = (Expression) path;
        } else {
            expression = buildExpression(root, builder, propertyName, null);
        }

        if ("NULL".equalsIgnoreCase(String.valueOf(matchValue))) {
            return expression.isNull();
        } else if ("EMPTY".equalsIgnoreCase(String.valueOf(matchValue))) {
            return builder.or(builder.isNull(expression), builder.equal(expression, ""));
        } else if ("NONULL".equalsIgnoreCase(String.valueOf(matchValue))) {
            return expression.isNotNull();
        } else if ("NOEMPTY".equalsIgnoreCase(String.valueOf(matchValue))) {
            return builder.and(builder.isNotNull(expression), builder.notEqual(expression, ""));
        }

        // logic operator
        switch (filter.getMatchType()) {
        case EQ:
            // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
            // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
            if (matchValue instanceof Date) {
                DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0
                        && dateTime.getSecondOfMinute() == 0) {
                    return builder.and(builder.greaterThanOrEqualTo(expression, dateTime.toDate()),
                            builder.lessThan(expression, dateTime.plusDays(1).toDate()));
                }
            }
            predicate = builder.equal(expression, matchValue);
            break;
        case NE:
            // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
            // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
            if (matchValue instanceof Date) {
                DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0
                        && dateTime.getSecondOfMinute() == 0) {
                    return builder.or(builder.lessThan(expression, dateTime.toDate()),
                            builder.greaterThanOrEqualTo(expression, dateTime.plusDays(1).toDate()));
                }
            }
            predicate = builder.notEqual(expression, matchValue);
            break;
        case BK:
            predicate = builder.or(builder.isNull(expression), builder.equal(expression, ""));
            break;
        case NB:
            predicate = builder.and(builder.isNotNull(expression), builder.notEqual(expression, ""));
            break;
        case NU:
            if (matchValue instanceof Boolean && (Boolean) matchValue == false) {
                predicate = builder.isNotNull(expression);
            } else {
                predicate = builder.isNull(expression);
            }
            break;
        case NN:
            if (matchValue instanceof Boolean && (Boolean) matchValue == false) {
                predicate = builder.isNull(expression);
            } else {
                predicate = builder.isNotNull(expression);
            }
            break;
        case CN:
            predicate = builder.like(expression, "%" + matchValue + "%");
            break;
        case NC:
            predicate = builder.notLike(expression, "%" + matchValue + "%");
            break;
        case BW:
            predicate = builder.like(expression, matchValue + "%");
            break;
        case BN:
            predicate = builder.notLike(expression, matchValue + "%");
            break;
        case EW:
            predicate = builder.like(expression, "%" + matchValue);
            break;
        case EN:
            predicate = builder.notLike(expression, "%" + matchValue);
            break;
        case BT:
            Assert.isTrue(matchValue.getClass().isArray(), "Match value must be array");
            Object[] matchValues = (Object[]) matchValue;
            Assert.isTrue(matchValues.length == 2, "Match value must have two value");
            // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
            // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
            if (matchValues[0] instanceof Date) {
                DateTime dateFrom = new DateTime(((Date) matchValues[0]).getTime());
                DateTime dateTo = new DateTime(((Date) matchValues[1]).getTime());
                if (dateFrom.getHourOfDay() == 0 && dateFrom.getMinuteOfHour() == 0
                        && dateFrom.getSecondOfMinute() == 0) {
                    return builder.and(builder.greaterThanOrEqualTo(expression, dateFrom.toDate()),
                            builder.lessThan(expression, dateTo.plusDays(1).toDate()));

                }
            } else {
                return builder.between(expression, (Comparable) matchValues[0], (Comparable) matchValues[1]);
            }
            predicate = builder.equal(expression, matchValue);
            break;
        case GT:
            // 对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
            // 而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
            if (matchValue instanceof Date) {
                DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0
                        && dateTime.getSecondOfMinute() == 0) {
                    return builder.greaterThanOrEqualTo(expression, dateTime.plusDays(1).toDate());
                }
            }
            predicate = builder.greaterThan(expression, (Comparable) matchValue);
            break;
        case GE:
            predicate = builder.greaterThanOrEqualTo(expression, (Comparable) matchValue);
            break;
        case LT:
            predicate = builder.lessThan(expression, (Comparable) matchValue);
            break;
        case LE:
            predicate = builder.lessThanOrEqualTo(expression, (Comparable) matchValue);
            break;
        case IN:
            if (matchValue.getClass().isArray()) {
                predicate = expression.in((Object[]) matchValue);
            } else if (matchValue instanceof Collection) {
                predicate = expression.in((Collection) matchValue);
            } else {
                predicate = builder.equal(expression, matchValue);
            }
            break;
        case ACLPREFIXS:
            List<Predicate> aclPredicates = Lists.newArrayList();
            Collection<String> aclCodePrefixs = (Collection<String>) matchValue;
            for (String aclCodePrefix : aclCodePrefixs) {
                if (StringUtils.isNotBlank(aclCodePrefix)) {
                    aclPredicates.add(builder.like(expression, aclCodePrefix + "%"));
                }

            }
            if (aclPredicates.size() == 0) {
                return null;
            }
            predicate = builder.or(aclPredicates.toArray(new Predicate[aclPredicates.size()]));
            break;
        default:
            break;
        }

        //处理集合子查询
        if (filter.getSubQueryCollectionPropetyType() != null) {
            String owner = StringUtils.uncapitalize(entityClass.getSimpleName());
            subQueryFrom.join(owner);
            subquery.select(subQueryFrom.get(owner)).where(predicate);
            predicate = builder.in(root.get("id")).value(subquery);
        }

        Assert.notNull(predicate, "Undefined match type: " + filter.getMatchType());
        return predicate;
    }

    /**
     * 根据条件集合对象组装JPA规范条件查询集合对象，基类默认实现进行条件封装组合
     * 子类可以调用此方法在返回的List<Predicate>额外追加其他PropertyFilter不易表单的条件如exist条件处理等
     * 
     * @param filters
     * @param root
     * @param query
     * @param builder
     * @return
     */
    private <X> List<Predicate> buildPredicatesFromFilters(final Collection<PropertyFilter> filters, Root<X> root,
            CriteriaQuery<?> query, CriteriaBuilder builder, Boolean having) {
        List<Predicate> predicates = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(filters)) {
            for (PropertyFilter filter : filters) {
                if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
                    Predicate predicate = buildPredicate(filter.getPropertyName(), filter, root, query, builder, having);
                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                } else {// 包含多个属性需要比较的情况,进行or处理.
                    List<Predicate> orpredicates = Lists.newArrayList();
                    for (String param : filter.getPropertyNames()) {
                        Predicate predicate = buildPredicate(param, filter, root, query, builder, having);
                        if (predicate != null) {
                            orpredicates.add(predicate);
                        }
                    }
                    if (orpredicates.size() > 0) {
                        predicates.add(builder.or(orpredicates.toArray(new Predicate[orpredicates.size()])));
                    }
                }
            }
        }
        return predicates;
    }

    private <X extends Persistable> Specification<X> buildSpecification(final GroupPropertyFilter groupPropertyFilter) {
        return new Specification<X>() {
            @Override
            public Predicate toPredicate(Root<X> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (groupPropertyFilter != null) {
                    return buildPredicatesFromFilters(groupPropertyFilter, root, query, builder);
                } else {
                    return null;
                }
            }
        };
    }

    protected Predicate buildPredicatesFromFilters(GroupPropertyFilter groupPropertyFilter, Root root,
            CriteriaQuery<?> query, CriteriaBuilder builder) {
        return buildPredicatesFromFilters(groupPropertyFilter, root, query, builder, false);
    }

    protected Predicate buildPredicatesFromFilters(GroupPropertyFilter groupPropertyFilter, Root root,
            CriteriaQuery<?> query, CriteriaBuilder builder, Boolean having) {
        if (groupPropertyFilter == null) {
            return null;
        }
        List<Predicate> predicates = buildPredicatesFromFilters(groupPropertyFilter.getFilters(), root, query, builder,
                having);
        if (CollectionUtils.isNotEmpty(groupPropertyFilter.getGroups())) {
            for (GroupPropertyFilter group : groupPropertyFilter.getGroups()) {
                if (CollectionUtils.isEmpty(group.getFilters()) && CollectionUtils.isEmpty(group.getForceAndFilters())) {
                    continue;
                }
                Predicate subPredicate = buildPredicatesFromFilters(group, root, query, builder, having);
                if (subPredicate != null) {
                    predicates.add(subPredicate);
                }
            }
        }
        Predicate predicate = null;
        if (CollectionUtils.isNotEmpty(predicates)) {
            if (predicates.size() == 1) {
                predicate = predicates.get(0);
            } else {
                if (groupPropertyFilter.getGroupType().equals(GroupPropertyFilter.GROUP_OPERATION_OR)) {
                    predicate = builder.or(predicates.toArray(new Predicate[predicates.size()]));
                } else {
                    predicate = builder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            }
        }

        List<Predicate> appendAndPredicates = buildPredicatesFromFilters(groupPropertyFilter.getForceAndFilters(),
                root, query, builder, having);
        if (CollectionUtils.isNotEmpty(appendAndPredicates)) {
            if (predicate != null) {
                appendAndPredicates.add(predicate);
            }
            predicate = builder.and(appendAndPredicates.toArray(new Predicate[appendAndPredicates.size()]));
        }

        return predicate;
    }

    /**
     * 子类额外追加过滤限制条件的入口方法，一般基于当前登录用户强制追加过滤条件
     * 
     * @param filters
     */
    protected List<Predicate> appendPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return null;
    }

    private Expression parseExpr(Root<?> root, CriteriaBuilder criteriaBuilder, String expr,
            Map<String, Expression<?>> parsedExprMap) {
        if (parsedExprMap == null) {
            parsedExprMap = Maps.newHashMap();
        }
        Expression<?> expression = null;
        if (expr.indexOf("(") > -1) {
            int left = 0;
            char[] chars = expr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    left = i;
                }
            }
            String leftStr = expr.substring(0, left);
            String op = null;
            char[] leftStrs = leftStr.toCharArray();
            for (int i = leftStrs.length - 1; i > 0; i--) {
                if (leftStrs[i] == '(' || leftStrs[i] == ')' || leftStrs[i] == ',') {
                    op = leftStr.substring(i + 1);
                    break;
                }
            }
            if (op == null) {
                op = leftStr;
            }
            String rightStr = expr.substring(left + 1);
            String arg = StringUtils.substringBefore(rightStr, ")");
            String[] args = arg.split(",");
            //logger.debug("op={},arg={}", op, arg);
            if (op.equalsIgnoreCase("case")) {
                Case selectCase = criteriaBuilder.selectCase();

                Expression caseWhen = parsedExprMap.get(args[0]);

                String whenResultExpr = args[1];
                Object whenResult = parsedExprMap.get(whenResultExpr);
                if (whenResult == null) {
                    Case<Long> whenCase = selectCase.when(caseWhen, new BigDecimal(whenResultExpr));
                    selectCase = whenCase;
                } else {
                    Case<Expression<?>> whenCase = selectCase.when(caseWhen, whenResult);
                    selectCase = whenCase;
                }
                String otherwiseResultExpr = args[2];
                Object otherwiseResult = parsedExprMap.get(otherwiseResultExpr);
                if (otherwiseResult == null) {
                    expression = selectCase.otherwise(new BigDecimal(otherwiseResultExpr));
                } else {
                    expression = selectCase.otherwise((Expression<?>) otherwiseResult);
                }
            } else {
                Object[] subExpressions = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    subExpressions[i] = parsedExprMap.get(args[i]);
                    if (subExpressions[i] == null) {
                        String name = args[i];
                        try {
                            Path<?> item = null;
                            if (name.indexOf(".") > -1) {
                                String[] props = StringUtils.split(name, ".");
                                item = root.get(props[0]);
                                for (int j = 1; j < props.length; j++) {
                                    item = item.get(props[j]);
                                }
                            } else {
                                item = root.get(name);
                            }
                            subExpressions[i] = (Expression) item;
                        } catch (Exception e) {
                            subExpressions[i] = new BigDecimal(name);
                        }
                    }
                }
                try {
                    //criteriaBuilder.quot();
                    expression = (Expression) MethodUtils.invokeMethod(criteriaBuilder, op, subExpressions);
                } catch (Exception e) {
                    logger.error("Error for aggregate  setting ", e);
                }
            }

            String exprPart = op + "(" + arg + ")";
            String exprPartConvert = exprPart.replace(op + "(", op + "_").replace(arg + ")", arg + "_")
                    .replace(",", "_");
            expr = expr.replace(exprPart, exprPartConvert);
            parsedExprMap.put(exprPartConvert, expression);

            if (expr.indexOf("(") > -1) {
                expression = parseExpr(root, criteriaBuilder, expr, parsedExprMap);
            }
        } else {
            String name = expr;
            Path<?> item = null;
            if (name.indexOf(".") > -1) {
                String[] props = StringUtils.split(name, ".");
                item = root.get(props[0]);
                for (int j = 1; j < props.length; j++) {
                    item = item.get(props[j]);
                }
            } else {
                item = root.get(name);
            }
            expression = item;
        }
        return expression;
    }

    private String fixCleanAlias(String name) {
        return StringUtils.remove(StringUtils.remove(
                StringUtils.remove(StringUtils.remove(StringUtils.remove(name, "("), ")"), "."), ","), "-");
    }

    private Expression<?> buildExpression(Root<?> root, CriteriaBuilder criteriaBuilder, String name, String alias) {
        Expression<?> expr = parseExpr(root, criteriaBuilder, name, null);
        if (alias != null) {
            expr.alias(alias);
        }
        return expr;
    }

    private Expression<?>[] buildExpressions(Root<?> root, CriteriaBuilder criteriaBuilder,
            List<GroupAggregateProperty> groupAggregateProperties) {
        Expression<?>[] parsed = new Expression<?>[groupAggregateProperties.size()];
        int i = 0;
        for (GroupAggregateProperty groupAggregateProperty : groupAggregateProperties) {
            parsed[i++] = buildExpression(root, criteriaBuilder, groupAggregateProperty.getName(),
                    groupAggregateProperty.getAlias());
        }
        return parsed;
    }

    private Selection<?>[] mergeSelections(Root<?> root, Selection<?>[] path1, Selection<?>... path2) {
        Selection<?>[] parsed = new Selection<?>[path1.length + path2.length];
        int i = 0;
        for (Selection<?> path : path1) {
            parsed[i++] = path;
        }
        for (Selection<?> path : path2) {
            parsed[i++] = path;
        }
        return parsed;
    }

    /**
     * 查询对象历史记录版本集合
     * 
     * @param id
     *            实体主键
     * @param property
     *            过滤属性
     * @param changed
     *            过滤方式，有无变更
     * @return
     */
    @Transactional(readOnly = true)
    public List<EntityRevision> findEntityRevisions(final Object id, String property, Boolean changed) {
        List<EntityRevision> entityRevisions = Lists.newArrayList();
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager).createQuery()
                .forRevisionsOfEntity(entityClass, false, true);
        auditQuery.add(AuditEntity.id().eq(id)).addOrder(AuditEntity.revisionNumber().desc());
        if (StringUtils.isNotBlank(property) && changed != null) {
            if (changed) {
                auditQuery.add(AuditEntity.property(property).hasChanged());
            } else {
                auditQuery.add(AuditEntity.property(property).hasNotChanged());
            }
        }
        List list = auditQuery.getResultList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object obj : list) {
                Object[] itemArray = (Object[]) obj;
                EntityRevision entityRevision = new EntityRevision();
                entityRevision.setEntity(itemArray[0]);
                entityRevision.setRevisionEntity((ExtDefaultRevisionEntity) itemArray[1]);
                entityRevision.setRevisionType((RevisionType) itemArray[2]);
                entityRevisions.add(entityRevision);
            }
        }
        return entityRevisions;
    }

    /**
     * 查询对象历史记录版本集合
     * 
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<EntityRevision> findEntityRevisions(final Object id, Number... revs) {
        List<EntityRevision> entityRevisions = Lists.newArrayList();
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager).createQuery()
                .forRevisionsOfEntity(entityClass, false, true);
        auditQuery.add(AuditEntity.id().eq(id)).add(AuditEntity.revisionNumber().in(revs));
        List list = auditQuery.getResultList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object obj : list) {
                Object[] itemArray = (Object[]) obj;
                EntityRevision entityRevision = new EntityRevision();
                entityRevision.setEntity(itemArray[0]);
                entityRevision.setRevisionEntity((ExtDefaultRevisionEntity) itemArray[1]);
                entityRevision.setRevisionType((RevisionType) itemArray[2]);
                entityRevisions.add(entityRevision);
            }
        }
        return entityRevisions;
    }

    /**
     * 供子类调用的关联对象关联关系操作辅助方法
     * 
     * @param id
     *            当前关联主对象主键，如User对象主键
     * @param r2EntityIds
     *            关联对象的主键集合，如用户关联角色的Role对象集合的主键
     * @param r2PropertyName
     *            主对象中关联集合对象属性的名称，如User对象中定义的userR2Roles属性名
     * @param r2EntityPropertyName
     *            被关联对象在R2关联对象定义中的属性名称，如UserR2Role中定义的role属性名
     * @param op
     *            关联操作类型，如add、del等， @see #R2OperationEnum
     */
    protected void updateRelatedR2s(ID id, Collection<? extends Serializable> r2EntityIds, String r2PropertyName,
            String r2EntityPropertyName, R2OperationEnum op) {
        try {
            T entity = findOne(id);
            List oldR2s = (List) FieldUtils.readDeclaredField(entity, r2PropertyName, true);

            Field r2field = FieldUtils.getField(entityClass, r2PropertyName, true);
            Class r2Class = (Class) (((ParameterizedType) r2field.getGenericType()).getActualTypeArguments()[0]);
            Field entityField = null;
            Field[] fields = r2Class.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().equals(entityClass)) {
                    entityField = field;
                    break;
                }
            }

            Field r2EntityField = FieldUtils.getField(r2Class, r2EntityPropertyName, true);
            Class r2EntityClass = r2EntityField.getType();

            if (R2OperationEnum.update.equals(op)) {
                if (CollectionUtils.isEmpty(r2EntityIds) && !CollectionUtils.isEmpty(oldR2s)) {
                    oldR2s.clear();
                }
            }

            if (R2OperationEnum.update.equals(op) || R2OperationEnum.add.equals(op)) {
                // 双循环处理需要新增关联的项目
                for (Serializable r2EntityId : r2EntityIds) {
                    Object r2Entity = entityManager.find(r2EntityClass, r2EntityId);
                    boolean tobeAdd = true;
                    for (Object r2 : oldR2s) {
                        if (FieldUtils.readDeclaredField(r2, r2EntityPropertyName, true).equals(r2Entity)) {
                            tobeAdd = false;
                            break;
                        }
                    }
                    if (tobeAdd) {
                        Object newR2 = r2Class.newInstance();
                        FieldUtils.writeDeclaredField(newR2, r2EntityField.getName(), r2Entity, true);
                        FieldUtils.writeDeclaredField(newR2, entityField.getName(), entity, true);
                        oldR2s.add(newR2);
                    }
                }
            }

            if (R2OperationEnum.update.equals(op)) {
                // 双循环处理需要删除关联的项目
                List tobeDleteList = Lists.newArrayList();
                for (Object r2 : oldR2s) {
                    boolean tobeDlete = true;
                    for (Serializable r2EntityId : r2EntityIds) {
                        Object r2Entity = entityManager.find(r2EntityClass, r2EntityId);
                        if (FieldUtils.readDeclaredField(r2, r2EntityPropertyName, true).equals(r2Entity)) {
                            tobeDlete = false;
                            break;
                        }
                    }
                    if (tobeDlete) {
                        tobeDleteList.add(r2);
                    }
                }
                oldR2s.removeAll(tobeDleteList);
            }

            if (R2OperationEnum.delete.equals(op)) {
                // 双循环处理需要删除关联的项目
                List tobeDleteList = Lists.newArrayList();
                for (Object r2 : oldR2s) {
                    boolean tobeDlete = false;
                    for (Serializable r2EntityId : r2EntityIds) {
                        Object r2Entity = entityManager.find(r2EntityClass, r2EntityId);
                        if (FieldUtils.readDeclaredField(r2, r2EntityPropertyName, true).equals(r2Entity)) {
                            tobeDlete = true;
                            break;
                        }
                    }
                    if (tobeDlete) {
                        tobeDleteList.add(r2);
                    }
                }
                oldR2s.removeAll(tobeDleteList);
            }

        } catch (SecurityException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /**
     * 供子类调用的关联对象关联关系操作辅助方法
     * 
     * @param id
     *            当前关联主对象主键，如User对象主键
     * @param r2EntityIds
     *            关联对象的主键集合，如用户关联角色的Role对象集合的主键
     * @param r2PropertyName
     *            主对象中关联集合对象属性的名称，如User对象中定义的userR2Roles属性名
     * @param r2EntityPropertyName
     *            被关联对象在R2关联对象定义中的属性名称，如UserR2Role中定义的role属性名
     */
    protected void updateRelatedR2s(ID id, Serializable[] r2EntityIds, String r2PropertyName,
            String r2EntityPropertyName) {
        try {
            T entity = findOne(id);
            List oldR2s = (List) MethodUtils.invokeExactMethod(entity, "get" + StringUtils.capitalize(r2PropertyName),
                    null);

            if ((r2EntityIds == null || r2EntityIds.length == 0) && !CollectionUtils.isEmpty(oldR2s)) {
                oldR2s.clear();
            } else {
                Field r2field = FieldUtils.getField(entityClass, r2PropertyName, true);
                Class r2Class = (Class) (((ParameterizedType) r2field.getGenericType()).getActualTypeArguments()[0]);
                Field entityField = null;
                Field[] fields = r2Class.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType().equals(entityClass)) {
                        entityField = field;
                        break;
                    }
                }

                Field r2EntityField = FieldUtils.getField(r2Class, r2EntityPropertyName, true);
                Class r2EntityClass = r2EntityField.getType();

                // 双循环处理需要删除关联的项目
                List tobeDleteList = Lists.newArrayList();
                for (Object r2 : oldR2s) {
                    boolean tobeDlete = true;
                    for (Serializable r2EntityId : r2EntityIds) {
                        Object r2Entity = entityManager.find(r2EntityClass, r2EntityId);
                        if (FieldUtils.readDeclaredField(r2, r2EntityPropertyName, true).equals(r2Entity)) {
                            tobeDlete = false;
                            break;
                        }
                    }
                    if (tobeDlete) {
                        tobeDleteList.add(r2);
                    }
                }
                oldR2s.removeAll(tobeDleteList);

                // 双循环处理需要新增关联的项目
                for (Serializable r2EntityId : r2EntityIds) {
                    Object r2Entity = entityManager.find(r2EntityClass, r2EntityId);
                    boolean tobeAdd = true;
                    for (Object r2 : oldR2s) {
                        if (FieldUtils.readDeclaredField(r2, r2EntityPropertyName, true).equals(r2Entity)) {
                            tobeAdd = false;
                            break;
                        }
                    }
                    if (tobeAdd) {
                        Object newR2 = r2Class.newInstance();
                        FieldUtils.writeDeclaredField(newR2, r2EntityField.getName(), r2Entity, true);
                        FieldUtils.writeDeclaredField(newR2, entityField.getName(), entity, true);
                        oldR2s.add(newR2);
                    }
                }
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Transactional(readOnly = true)
    public Object findEntity(Class entityClass, Serializable id) {
        return entityManager.find(entityClass, id);
    }

    public void detachEntity(Object entity) {
        entityManager.detach(entity);
    }

    /**
     * 基于Native SQL返回Map结构集合数据
     */
    @SuppressWarnings("rawtypes")
    protected List<Map<String, Object>> queryForMapDatasByNativeSQL(String sql) {
        //TODO 防止SQL字符过滤处理
        Query query = entityManager.createNativeQuery(sql);
        //Hibernate特定语法
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query.getResultList();
        entityManager.close();
        return list;
    }
}
