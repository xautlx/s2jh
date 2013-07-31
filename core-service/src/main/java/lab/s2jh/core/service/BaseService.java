package lab.s2jh.core.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lab.s2jh.core.audit.envers.EntityRevision;
import lab.s2jh.core.audit.envers.ExtDefaultRevisionEntity;
import lab.s2jh.core.dao.BaseDao;
import lab.s2jh.core.exception.ServiceException;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

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
     * 创建数据保存数据之前额外操作回调方法
     * 默认为空逻辑，子类根据需要覆写添加逻辑即可
     * @param entity 待创建数据对象
     */
    protected void preInsert(T entity) {

    }

    /**
     * 更新数据保存数据之前额外操作回调方法
     * 默认为空逻辑，子类根据需要覆写添加逻辑即可
     * @param entity 待更新数据对象
     */
    protected void preUpdate(T entity) {

    }

    /**
     * 数据保存操作
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
     * 批量数据保存操作
     * 其实现只是简单循环集合每个元素调用 {@link #save(Persistable)}
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     * @param entities 待批量操作数据集合
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
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public T findOne(ID id) {
        Assert.notNull(id);
        return getEntityDao().findOne(id);
    }

    /**
     * 基于主键集合查询集合数据对象
     * @param ids 主键集合
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findAll(final Iterable<ID> ids) {
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
     * @param entity 待操作数据
     */
    public void delete(T entity) {
        getEntityDao().delete(entity);
    }

    /**
     * 批量数据删除操作
     * 其实现只是简单循环集合每个元素调用 {@link #delete(Persistable)}
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     * @param entities 待批量操作数据集合
     * @return
     */
    public void delete(Iterable<T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    /**
     * 根据泛型对象属性和值查询唯一对象
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
     * 通用的对象属性和值查询接口，根据泛型参数确定返回类型数据
     * @param baseDao 泛型参数对象DAO接口
     * @param property 属性名，即对象中数量变量名称
     * @param value 参数值
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
     * 基于动态组合条件对象查询数据集合
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
     * @param groupPropertyFilter
     * @param sort 
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findByFilters(GroupPropertyFilter groupPropertyFilter, Sort sort) {
        Specification<T> spec = buildSpecification(groupPropertyFilter);
        return getEntityDao().findAll(spec, sort);
    }

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     * @param groupPropertyFilter
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<T> findByPage(GroupPropertyFilter groupPropertyFilter, Pageable pageable) {
        return getEntityDao().findAll(buildSpecification(groupPropertyFilter), pageable);
    }

    /**
     * 基于JPA通用的查询条件count记录数据
     * @param spec
     * @return
     */
    @Transactional(readOnly = true)
    public long count(Specification<T> spec) {
        return getEntityDao().count(spec);
    }

    @SuppressWarnings("unchecked")
    private <X> Predicate buildPredicate(String propertyName, PropertyFilter filter, Root<X> root,
            CriteriaQuery<?> query, CriteriaBuilder builder) {
        Object matchValue = filter.getMatchValue();
        if (matchValue == null) {
            return null;
        }
        if (matchValue instanceof String) {
            if (StringUtils.isBlank(String.valueOf(matchValue))) {
                return null;
            }
        }

        if (filter.getMatchType().equals(MatchType.FETCH)) {
            JoinType joinType = JoinType.INNER;
            if (matchValue != null) {
                if (matchValue instanceof String) {
                    joinType = Enum.valueOf(JoinType.class, (String) matchValue);
                } else {
                    joinType = (JoinType) filter.getMatchValue();
                }
            }
            //Hack for Bug: https://jira.springsource.org/browse/DATAJPA-105
            //如果是在count计算总记录，则添加join；否则说明正常分页查询添加fetch
            if (!Long.class.isAssignableFrom(query.getResultType())) {
                root.fetch(propertyName, joinType);
            } else {
                root.join(propertyName, joinType);
            }
            return null;
        }

        Predicate predicate = null;
        String[] names = StringUtils.split(propertyName, ".");
        Path expression = root.get(names[0]);
        for (int i = 1; i < names.length; i++) {
            expression = expression.get(names[i]);
        }

        if ("NULL".equalsIgnoreCase(String.valueOf(matchValue))) {
            return expression.isNull();
        }

        // logic operator
        switch (filter.getMatchType()) {
        case EQ:
            //对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
            //而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
            if (matchValue instanceof Date) {
                DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0
                        && dateTime.getSecondOfMinute() == 0) {
                    return builder.and(builder.greaterThanOrEqualTo(expression, dateTime.toDate()),
                            builder.lessThan(expression, dateTime.plusDays(1).toDate()));
                }
            }
            return builder.equal(expression, matchValue);
        case NE:
            //对日期特殊处理：一般用于区间日期的结束时间查询,如查询2012-01-01之前,一般需要显示2010-01-01当天及以前的数据,
            //而数据库一般存有时分秒,因此需要特殊处理把当前日期+1天,转换为<2012-01-02进行查询
            if (matchValue instanceof Date) {
                DateTime dateTime = new DateTime(((Date) matchValue).getTime());
                if (dateTime.getHourOfDay() == 0 && dateTime.getMinuteOfHour() == 0
                        && dateTime.getSecondOfMinute() == 0) {
                    return builder.or(builder.greaterThan(expression, dateTime.toDate()),
                            builder.lessThanOrEqualTo(expression, dateTime.plusDays(1).toDate()));
                }
            }
            return builder.notEqual(expression, matchValue);
        case NU:
            return builder.isNull(expression);
        case NN:
            return builder.isNotNull(expression);
        case CN:
            return builder.like(expression, "%" + matchValue + "%");
        case NC:
            return builder.notLike(expression, "%" + matchValue + "%");
        case BW:
            return builder.like(expression, matchValue + "%");
        case BN:
            return builder.notLike(expression, matchValue + "%");
        case EW:
            return builder.like(expression, "%" + matchValue);
        case EN:
            return builder.notLike(expression, "%" + matchValue);
        case GT:
            return builder.greaterThan(expression, (Comparable) matchValue);
        case GE:
            return builder.greaterThanOrEqualTo(expression, (Comparable) matchValue);
        case LT:
            return builder.lessThan(expression, (Comparable) matchValue);
        case LE:
            return builder.lessThanOrEqualTo(expression, (Comparable) matchValue);
        case IN:
            if (matchValue.getClass().isArray()) {
                predicate = expression.in((Object[]) matchValue);
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
        Assert.notNull(predicate, "Undefined match type: " + filter.getMatchType());
        return predicate;
    }

    /**
     * 根据条件集合对象组装JPA规范条件查询集合对象，基类默认实现进行条件封装组合
     * 子类可以调用此方法在返回的List<Predicate>额外追加其他PropertyFilter不易表单的条件如exist条件处理等
     * @param filters
     * @param root
     * @param query
     * @param builder
     * @return
     */
    private <X> List<Predicate> buildPredicatesFromFilters(final Collection<PropertyFilter> filters, Root<X> root,
            CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(filters)) {
            for (PropertyFilter filter : filters) {
                if (!filter.hasMultiProperties()) { //只有一个属性需要比较的情况.
                    Predicate predicate = buildPredicate(filter.getPropertyName(), filter, root, query, builder);
                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                } else {//包含多个属性需要比较的情况,进行or处理.
                    List<Predicate> orpredicates = Lists.newArrayList();
                    for (String param : filter.getPropertyNames()) {
                        Predicate predicate = buildPredicate(param, filter, root, query, builder);
                        if (predicate != null) {
                            orpredicates.add(predicate);
                        }
                    }
                    predicates.add(builder.or(orpredicates.toArray(new Predicate[orpredicates.size()])));
                }
            }
        }
        return predicates;
    }

    private Specification<T> buildSpecification(final GroupPropertyFilter groupPropertyFilter) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
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
        List<Predicate> predicates = buildPredicatesFromFilters(groupPropertyFilter.getFilters(), root, query, builder);
        if (CollectionUtils.isNotEmpty(groupPropertyFilter.getGroups())) {
            for (GroupPropertyFilter group : groupPropertyFilter.getGroups()) {
                predicates.add(buildPredicatesFromFilters(group, root, query, builder));
            }
        }
        Predicate predicate = null;
        if (groupPropertyFilter.getGroupType().equals(GroupPropertyFilter.GROUP_OPERATION_OR)) {
            predicate = builder.or(predicates.toArray(new Predicate[predicates.size()]));
        } else {
            predicate = builder.and(predicates.toArray(new Predicate[predicates.size()]));
        }

        List<Predicate> appendAndPredicates = buildPredicatesFromFilters(groupPropertyFilter.getAppendAndFilters(),
                root, query, builder);
        if (CollectionUtils.isNotEmpty(appendAndPredicates)) {
            appendAndPredicates.add(predicate);
            predicate = builder.and(appendAndPredicates.toArray(new Predicate[appendAndPredicates.size()]));
        }

        List<Predicate> appendOrPredicates = buildPredicatesFromFilters(groupPropertyFilter.getAppendOrFilters(), root,
                query, builder);
        if (CollectionUtils.isNotEmpty(appendOrPredicates)) {
            appendOrPredicates.add(predicate);
            predicate = builder.and(appendOrPredicates.toArray(new Predicate[appendOrPredicates.size()]));
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

    /**
     * 查询对象历史记录版本集合
     * @param id 实体主键
     * @param property 过滤属性
     * @param changed 过滤方式，有无变更
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
     * @param id 当前关联主对象主键，如User对象主键
     * @param r2EntityIds 关联对象的主键集合，如用户关联角色的Role对象集合的主键
     * @param r2PropertyName 主对象中关联集合对象属性的名称，如User对象中定义的userR2Roles属性名
     * @param r2EntityPropertyName 被关联对象在R2关联对象定义中的属性名称，如UserR2Role中定义的role属性名
     * @param op 关联操作类型，如add、del等， @see #R2OperationEnum
     */
    protected void updateRelatedR2s(ID id, Collection<ID> r2EntityIds, String r2PropertyName,
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
                //双循环处理需要新增关联的项目
                for (ID r2EntityId : r2EntityIds) {
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
                //双循环处理需要删除关联的项目
                List tobeDleteList = Lists.newArrayList();
                for (Object r2 : oldR2s) {
                    boolean tobeDlete = true;
                    for (ID r2EntityId : r2EntityIds) {
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
                //双循环处理需要删除关联的项目
                List tobeDleteList = Lists.newArrayList();
                for (Object r2 : oldR2s) {
                    boolean tobeDlete = false;
                    for (ID r2EntityId : r2EntityIds) {
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Transactional(readOnly = true)
    public Object findEntity(Class entityClass, Serializable id) {
        return entityManager.find(entityClass, id);
    }

    public void detachEntity(Object entity) {
        entityManager.detach(entity);
    }
}
