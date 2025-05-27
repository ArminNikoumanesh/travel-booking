package com.armin.utility.repository.orm;

import com.armin.utility.bl.ValidationEngine;
import com.armin.utility.repository.common.ConditionParameters;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.armin.utility.statics.constants.UtilityConstant.MAXIMUM_REPORT_PAGE_SIZE;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Repository("dao")
@Transactional
public class Dao<T> extends Parameterized<T> {

    /**
     * The Batch Size for Flush a Batch of Inserts and Release Memory
     * 20, Same as the JDBC Batch Size
     */
    private static final int BATCH_SIZE = 20;
    private static final String FETCH_GRAPH = "jakarta.persistence.fetchgraph";

    /**
     * The {@link SessionFactory} Instance Representing Database Session Factory
     */
    @PersistenceContext
    private EntityManager entityManager;

    static void addAttributesToEntityGraph(EntityGraph entityGraph, String[] include, List<String> columns) {
        Map<String, Subgraph> subGraphs = new HashMap<>();
        if (include != null) {
            for (String property : include) {
                if (property != null) {
                    if (columns.contains(property)) {
                        entityGraph.addAttributeNodes(property);
                    } else {
                        String[] subGraphSplit = property.split("\\.");
                        if (subGraphSplit.length > 1) {
                            Subgraph subGraph;
                            if (subGraphs.containsKey(subGraphSplit[0])) {
                                subGraph = subGraphs.get(subGraphSplit[0]);
                            } else {
                                subGraph = entityGraph.addSubgraph(subGraphSplit[0]);
                                subGraphs.put(subGraphSplit[0], subGraph);
                            }
                            for (int i = 1; i < subGraphSplit.length - 1; i++) {
                                String[] commaSeparateSubGraph = subGraphSplit[i].split(",");
                                if (commaSeparateSubGraph.length > 1) {
                                    for (int j = 0; j < commaSeparateSubGraph.length - 1; j++) {
                                        subGraph.addAttributeNodes(commaSeparateSubGraph[j]);
                                    }
                                    subGraph = subGraph.addSubgraph(commaSeparateSubGraph[commaSeparateSubGraph.length - 1]);
                                } else {
                                    subGraph = subGraph.addSubgraph(subGraphSplit[i]);
                                }
                            }
                            subGraph.addAttributeNodes(subGraphSplit[subGraphSplit.length - 1].split(","));
                        }
                    }
                }
            }
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Session getSession() {
        return getEntityManager().unwrap(Session.class);
    }


    public Integer count() {
        return this.count(getClazz());
    }

    public Integer count(Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        criteriaQuery.select(criteriaBuilder.count(rootQuery));
        return getEntityManager().createQuery(criteriaQuery).getSingleResult().intValue();
    }


    public T exist(Map<String, Object> conditions) {
        return this.exist(conditions, getClazz());
    }

    public T exist(Map<String, Object> conditions, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> pair : conditions.entrySet())
            predicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));

        criteriaQuery.select(rootQuery);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        List<T> result = getEntityManager().createQuery(criteriaQuery)
                .setFirstResult(0)
                .setMaxResults(1)
                .getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }


    public boolean existByCondition(Map<String, Object> conditions) {
        return this.existByCondition(conditions, getClazz());
    }

    public boolean existByCondition(Map<String, Object> conditions, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> pair : conditions.entrySet())
            predicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));

        criteriaQuery.select(rootQuery);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        return getEntityManager().createQuery(criteriaQuery)
                .setFirstResult(0)
                .setMaxResults(1) != null;
    }


    public T byId(Integer id) {
        return this.byId(id, getClazz());
    }

    public T byId(Integer id, Class<T> clazz) {
        IdentifierLoadAccess<T> identifierLoadAccess = getSession().byId(clazz);
        return identifierLoadAccess.load(id);
    }

    /**
     * Same semantic as "byId" except that here Optional is returned to handle nullability.
     * Params: id – The identifier
     * Returns: The persistent instance, if one, wrapped in Optional
     * author: Armin.Nik
     */
    public Optional<T> optionalById(Integer id) {
        return this.optionalById(id, getClazz());
    }

    public Optional<T> optionalById(Integer id, Class<T> clazz) {
        IdentifierLoadAccess<T> identifierLoadAccess = getSession().byId(clazz);
        return identifierLoadAccess.loadOptional(id);
    }

    public T get(Serializable id) {
        return this.get(id, getClazz());
    }

    public T get(Serializable id, Class<T> clazz) {
        return getSession().get(clazz, id);
    }


    public T find(Serializable id) {
        return this.find(id, getClazz());
    }

    public T find(Serializable id, Class<T> clazz) {
        return getSession().find(clazz, id);
    }


    public T load(Serializable id) {
        return this.load(id, getClazz());
    }

    public T load(Serializable id, Class<T> clazz) {
        return getSession().load(clazz, id);
    }


    public T getByAndConditions(Map<String, Object> conditions) {
        return this.byAndConditions(conditions, getClazz());
    }

    public T byAndConditions(Map<String, Object> conditions, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> pair : conditions.entrySet())
            predicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));

        criteriaQuery.select(rootQuery);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        List<T> result = getEntityManager().createQuery(criteriaQuery).getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }


    public T getByAndConditions(Map<String, Object> conditions, String[] include) {
        return this.byAndConditions(conditions, include, getClazz());
    }

    public T byAndConditions(Map<String, Object> conditions, String[] include, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> pair : conditions.entrySet())
            predicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));

        EntityGraph eg = getEntityManager().createEntityGraph(clazz);
        List<String> columns = ValidationEngine.fieldNames(clazz);
        addAttributesToEntityGraph(eg, include, columns);

        criteriaQuery.select(rootQuery);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        List<T> result = getEntityManager()
                .createQuery(criteriaQuery)
                .setHint(FETCH_GRAPH, eg)
                .getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }


    public T getByOrConditions(Map<String, Object> conditions) {
        return this.getByOrConditions(conditions, getClazz());
    }

    public T getByOrConditions(Map<String, Object> conditions, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> pair : conditions.entrySet())
            predicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));

        criteriaQuery.select(rootQuery);
        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[]{})));
        List<T> result = getEntityManager().createQuery(criteriaQuery).getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }


    public List<T> list() {
        return this.list(getClazz());
    }

    public List<T> list(Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        criteriaQuery.select(rootQuery);
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }


    public List<T> listByIds(Collection<Integer> ids) {
        return this.listByIds(ids, getClazz());
    }

    public List<T> listByIds(Collection<Integer> ids, Class<T> clazz) {
        MultiIdentifierLoadAccess<T> multiIdentifierLoadAccess = getEntityManager().unwrap(Session.class).byMultipleIds(clazz);
        return multiIdentifierLoadAccess.multiLoad(new ArrayList<>(ids));
    }


    public List<T> listByAndConditions(Map<String, Object> conditions) {
        return this.getListByAndConditions(conditions, getClazz());
    }

    public List<T> getListByAndConditions(Map<String, Object> conditions, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> pair : conditions.entrySet())
            predicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));

        criteriaQuery.select(rootQuery);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }


    public List<T> listByAndConditions(Map<String, Object> conditions, String[] includes) {
        return this.getListByAndConditions(conditions, includes, getClazz());
    }

    public List<T> getListByAndConditions(Map<String, Object> conditions, String[] includes, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> pair : conditions.entrySet())
            predicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));

        criteriaQuery.select(rootQuery);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        EntityGraph eg = getEntityManager().createEntityGraph(clazz);
        List<String> columns = ValidationEngine.fieldNames(clazz);

        addAttributesToEntityGraph(eg, includes, columns);
        /*List<String> finalIncludes = columns.stream().filter(item -> ArrayUtils.contains(includes, item)).collect(Collectors.toList());

        for (String property : finalIncludes) {
            eg.addAttributeNodes(property);
        }*/
        return getEntityManager()
                .createQuery(criteriaQuery)
                .setHint(FETCH_GRAPH, eg)
                .getResultList();
    }


    public List<T> listByOrConditions(Map<String, Object> conditions) {
        return this.getListByOrConditions(conditions, getClazz());
    }

    public List<T> getListByOrConditions(Map<String, Object> conditions, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> pair : conditions.entrySet())
            predicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));

        criteriaQuery.select(rootQuery);
        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[]{})));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }


    public List<T> listByOrAndConditions(List<Map<String, Object>> conditions) {
        return this.getListByOrAndConditions(conditions, getClazz());
    }

    public List<T> getListByOrAndConditions(List<Map<String, Object>> conditions, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        List<Predicate> orPredicates = new ArrayList<>();

        for (Map<String, Object> eachCondition : conditions) {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> pair : eachCondition.entrySet())
                predicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));
            orPredicates.add(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        }

        criteriaQuery.select(rootQuery);
        criteriaQuery.where(criteriaBuilder.or(orPredicates.toArray(new Predicate[]{})));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }


    public List<T> listByInConditions(Map<String, List> conditions) {
        return this.listByInConditions(conditions, getClazz());
    }

    public List<T> listByInConditions(Map<String, List> conditions, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, List> pair : conditions.entrySet()) {
            Expression<String> parentExpression = rootQuery.get(pair.getKey());
            predicates.add(parentExpression.in(pair.getValue()));
        }

        criteriaQuery.select(rootQuery);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public <T> List<T> queryHql(String hql, Map<String, Object> parameters) {
        Query query = getEntityManager().createQuery(hql);

        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        return query.getResultList();
    }

    public <T> List<T> queryHql(Query query, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        return query.getResultList();
    }

    public <T> List<T> queryHql(String hql, ReportFilter reportFilter) {
        String where = generateWhereClause(reportFilter.getConditions(), true);
        if (!where.equals(" where ")) {
            hql += where;
        }
        Query query = getEntityManager().createQuery(hql);
        return query.getResultList();
    }

    public <T> List<T> queryHql(Query query) {
        return query.getResultList();
    }

    public <T> List<T> queryHql(String hql, Map<String, Object> parameters, Class<T> cls, String[] include) {
        Query query = getEntityManager().createQuery(hql);

        EntityGraph eg = getEntityManager().createEntityGraph(cls);
        List<String> columns = ValidationEngine.fieldNames(cls);
        addAttributesToEntityGraph(eg, include, columns);
        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        return query.setHint(FETCH_GRAPH, eg)
                .getResultList();
    }

    public <T> List<T> queryHql(Query query, Map<String, Object> parameters, Class<T> cls, String[] include) {
        EntityGraph eg = getEntityManager().createEntityGraph(cls);
        List<String> columns = ValidationEngine.fieldNames(cls);
        addAttributesToEntityGraph(eg, include, columns);
        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        return query.setHint(FETCH_GRAPH, eg)
                .getResultList();
    }

    public <T> List<T> queryHql(String hql, Map<String, Object> parameters, Class<T> cls, int maxResultNumber) {
        TypedQuery<T> query = getEntityManager().createQuery(hql, cls);

        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        int maxResult = MAXIMUM_REPORT_PAGE_SIZE < maxResultNumber ? MAXIMUM_REPORT_PAGE_SIZE : maxResultNumber;
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    public <T> List<T> queryHql(Query query, Map<String, Object> parameters, int maxResultNumber) {
        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        int maxResult = MAXIMUM_REPORT_PAGE_SIZE < maxResultNumber ? MAXIMUM_REPORT_PAGE_SIZE : maxResultNumber;
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    public <T> List<T> queryHql(String hql, Map<String, Object> parameters, Integer pageSize, Integer pageNumber) {
        if (pageNumber < 1) pageNumber = 1;
        Query query = getEntityManager().createQuery(hql);
        query.setFirstResult((pageNumber - 1) * pageSize);
        int maxResult = MAXIMUM_REPORT_PAGE_SIZE < pageSize ? MAXIMUM_REPORT_PAGE_SIZE : pageSize;
        query.setMaxResults(maxResult);

        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        return query.getResultList();
    }

//    public <T> List<T> queryHqlLimit(String hql, Map<String, Object> parameters, Integer pageSize, Integer pageNumber, Class<T> clazz) {
//        String[] splitQuery = hql.split("from");
//        String tempSelect = "select ";
//        String select = "";
//        StringBuilder from = new StringBuilder();
//        if (splitQuery.length > 1) {
//            select = splitQuery[0];
//            from = new StringBuilder(splitQuery[1]);
//        } else {
//            from = new StringBuilder(splitQuery[0]);
//        }
//
//        String[] splitFrom = from.toString().replace("  ", " ").split(" ");
//        if (splitFrom.length > 2) {
//            int i;
//            for (i = 0; i < splitFrom.length; i++) {
//                if (Arrays.asList("where", "join").contains(splitFrom[i].toLowerCase())) {
//                    break;
//                }
//            }
//            if (i == 1) {
//                tempSelect += splitFrom[0].toLowerCase() + ".id ";
//                from = new StringBuilder(splitFrom[0] + " " + splitFrom[0].toLowerCase());
//                for (int i1 = 1; i1 < splitFrom.length; i1++) {
//                    from.append(" ").append(splitFrom[i1]);
//                }
//            } else if (i == 2) {
//                tempSelect += splitFrom[1] + ".id ";
//            }
//        }
//
//
//        if (pageNumber < 1) pageNumber = 1;
////        Query query = getEntityManager().createQuery(hql);
//        Query query = getEntityManager().createQuery((tempSelect + from).replace("fetch" , " "));
//        query.setFirstResult((pageNumber - 1) * pageSize);
//        int maxResult = MAXIMUM_REPORT_PAGE_SIZE < pageSize ? MAXIMUM_REPORT_PAGE_SIZE : pageSize;
//        query.setMaxResults(maxResult);
//
//        for (Map.Entry<String, Object> pair : parameters.entrySet())
//            query.setParameter(pair.getKey(), pair.getValue());
//        List<Integer> ids = query.getResultList();
//
//        if (ids.isEmpty()){
//            return new ArrayList<>();
//        }
//
//
//
//    }

    public <T> List<T> queryHql(Query query, Map<String, Object> parameters, Integer pageSize, Integer pageNumber) {
        if (pageNumber < 1) pageNumber = 1;
        query.setFirstResult((pageNumber - 1) * pageSize);
        int maxResult = MAXIMUM_REPORT_PAGE_SIZE < pageSize ? MAXIMUM_REPORT_PAGE_SIZE : pageSize;
        query.setMaxResults(maxResult);

        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        return query.getResultList();
    }

    public <T> List<T> querySql(String sql, Map<String, Object> parameters) {
        Query query = getEntityManager().createNativeQuery(sql);
        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        return query.getResultList();
    }

    public <T> List<T> querySql(Query query, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        return query.getResultList();
    }

    public <T> List<T> querySql(NativeQuery query, Map<String, Object> parameters, int size, int page, Class<T> cls) {
        query.setResultTransformer(Transformers.aliasToBean(cls));
        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        query.setMaxResults(size);
        query.setFirstResult((page - 1) * size);
        return query.getResultList();
    }

    public <T> List<T> querySql(String sql) {
        Query query = getEntityManager().createNativeQuery(sql);
        return query.getResultList();
    }


    public <T> List<T> querySql(Query query) {
        return query.getResultList();
    }

    public <T> List<T> querySql(String sql, Map<String, Object> parameters, Class<T> cls) {
        org.hibernate.query.Query query = getEntityManager().unwrap(Session.class).createNativeQuery(sql);
        query.setResultTransformer(Transformers.aliasToBean(cls));

        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        return query.getResultList();
    }

    public <T> List<T> querySql(org.hibernate.query.Query query, Map<String, Object> parameters, Class<T> cls) {
        query.setResultTransformer(Transformers.aliasToBean(cls));
        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        return query.getResultList();
    }


    public void updateSql(Query query, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> pair : parameters.entrySet()) {
            query.setParameter(pair.getKey(), pair.getValue());
        }
        query.executeUpdate();
    }

    public void updateSql(String sql, Map<String, Object> parameters) {
        Query query = getEntityManager().createNativeQuery(sql);
        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        query.executeUpdate();
    }
    /* ****************************************************************************************************************** */

    public void updateHqlQuery(String hql, Map<String, Object> parameters) {
        Query query = getEntityManager().createQuery(hql);

        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        query.executeUpdate();
    }

    public void updateHqlQuery(Query query, Map<String, Object> parameters) {

        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        query.executeUpdate();
    }


    public void deleteById(Object id) {
        this.deleteById(id, getClazz());
    }

    public void deleteById(Object id, Class<T> clazz) {
        IdentifierLoadAccess<T> identifierLoadAccess = getSession().byId(clazz);
        T entity = identifierLoadAccess.load((Serializable) id);
        if (entity != null) {
            getSession().delete(entity);
        }
    }


    public <Y extends Number> void deleteByIdList(List<Y> ids, String idName) {
        this.deleteByIdList(ids, idName, getClazz());
    }

    public <Y extends Number> void deleteByIdList(List<Y> ids, String idName, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(clazz);
        Root<T> rootDelete = criteriaDelete.from(clazz);

        criteriaDelete.where(rootDelete.get(idName).in(ids));
        getEntityManager().createQuery(criteriaDelete).executeUpdate();
    }


    public Boolean deleteByAndConditions(Map<String, Object> conditions) {
        return this.deleteByAndConditions(conditions, getClazz());
    }

    public Boolean deleteByAndConditions(Map<String, Object> conditions, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(clazz);
        Root<T> rootDelete = criteriaDelete.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> pair : conditions.entrySet())
            predicates.add(criteriaBuilder.equal(rootDelete.get(pair.getKey()), pair.getValue()));

        criteriaDelete.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        int count = getEntityManager().createQuery(criteriaDelete).executeUpdate();
        return count > 0;
    }


    public Boolean updateByAndConditions(Map<String, Object> conditions, Map<String, Object> parameters) {
        return this.updateByAndConditions(conditions, parameters, getClazz());
    }

    public Boolean updateByAndConditions(Map<String, Object> conditions, Map<String, Object> parameters, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaUpdate<T> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(clazz);
        Root<T> rootUpdate = criteriaUpdate.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> pair : conditions.entrySet())
            predicates.add(criteriaBuilder.equal(rootUpdate.get(pair.getKey()), pair.getValue()));

        for (Map.Entry<String, Object> pair : parameters.entrySet())
            criteriaUpdate.set(pair.getKey(), pair.getValue());
        criteriaUpdate.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        int count = getEntityManager().createQuery(criteriaUpdate).executeUpdate();
        return count > 0;
    }


    public void saveOrUpdate(T entity) {
        getSession().saveOrUpdate(entity);
    }


    public void update(T entity) {
        getSession().update(entity);
    }


    public void delete(T entity) {
        getSession().delete(entity);
    }


    public T saveEntity(T entity) {
        getSession().save(entity);
        return entity;
    }


    public T updateEntity(T entity) {
        getSession().update(entity);
        return entity;
    }

    public T mergeEntity(T entity) {
        return getEntityManager().merge(entity);
    }

    public void saveCollection(Collection<T> entities) {
        int i = 0;
        for (T entity : entities) {
            getSession().save(entity);
            if (i % BATCH_SIZE == 0) {
                getEntityManager().flush();
                getEntityManager().clear();
            }
            i++;
        }
    }


    public void updateCollection(Collection<T> entities) {
        int i = 0;
        for (T entity : entities) {
            getSession().update(entity);
            if (i % BATCH_SIZE == 0) {
                getEntityManager().flush();
                getEntityManager().clear();
            }
            i++;
        }
    }


    public void saveOrUpdateCollection(Collection<T> entities) {
        int i = 0;
        for (T entity : entities) {
            getSession().saveOrUpdate(entity);
            if (i % BATCH_SIZE == 0) {
                getEntityManager().flush();
                getEntityManager().clear();
            }
            i++;
        }
    }


    public void flush() {
        getEntityManager().flush();
        getEntityManager().clear();
    }


    public void detach(T entity) {
        getEntityManager().detach(entity);
    }

    /* ****************************************************************************************************************** */

    public <T> List<T> reportEntityWithSqlQuery(String select, String groupBy, ReportFilter reportFilter, Class<T> cls) {
        String where = generateWhereClause(reportFilter.getConditions(), true);
        String sql = select;
        if (!where.equals(" where ")) {
            sql += where;
        }
        if (groupBy != null) {
            sql += groupBy;
        }
        sql += generateSqlReportOption(reportFilter.getOptions());
        NativeQuery query = this.getSession().createNativeQuery(sql);
        query.setResultTransformer(Transformers.aliasToBean(cls));

        return query.getResultList();
    }

    public <T> List<T> reportEntityWithSqlQueryWithoutMaxLimit(String select, String groupBy, ReportFilter reportFilter, Class<T> cls) {
        String where = generateWhereClause(reportFilter.getConditions(), true);
        String sql = select;
        if (!where.equals(" where ")) {
            sql += where;
        }
        if (groupBy != null) {
            sql += groupBy;
        }
        NativeQuery query = this.getSession().createNativeQuery(sql);
        query.setResultTransformer(Transformers.aliasToBean(cls));

        return query.getResultList();
    }

    public <T> List<T> reportEntityWithHqlQuery(String select, String groupBy, ReportFilter reportFilter) {
        String where = generateWhereClause(reportFilter.getConditions(), true);
        String hql = select;
        if (!where.equals(" where ")) {
            if (select.contains("where")) {
                hql += where.replace("where", "and");
            } else {
                hql += where;
            }
        }
        if (groupBy != null) {
            hql += groupBy;
        }
        Query query = this.getEntityManager().createQuery(hql);
        generateHqlReportOption(reportFilter.getOptions(), query);
        return query.getResultList();
    }

    private String generateSqlReportOption(ReportOption reportOption) {
        StringBuilder option = new StringBuilder();
        if (reportOption != null && reportOption.getPageSize() != null) {
            option.append(" limit ").append(reportOption.getPageSize());
            option.append(" offset ").append((reportOption.getPageNumber() - 1) * reportOption.getPageSize());
        }
        return option.toString();
    }

    private void generateHqlReportOption(ReportOption reportOption, Query query) {
        if (reportOption != null && reportOption.getPageSize() != null) {
            int maxResult = MAXIMUM_REPORT_PAGE_SIZE < reportOption.getPageSize() ? MAXIMUM_REPORT_PAGE_SIZE : reportOption.getPageSize();
            query.setFirstResult((reportOption.getPageNumber() - 1) * reportOption.getPageSize())
                    .setMaxResults(maxResult);
        }
    }

    public String generateWhereClause(ReportCondition reportCondition, boolean isAnd) {
        StringBuilder where = new StringBuilder();
        List<String> condition = new ArrayList<>();
        for (ConditionParameters conditionParameters : reportCondition.getEqualCondition()) {
            if (conditionParameters.getValue() != null)
                condition.add(conditionParameters.getKey() + " = " + conditionParameters.getValue());
        }
        for (ConditionParameters conditionParameters : reportCondition.getNotEqualCondition()) {
            if (conditionParameters.getValue() != null)
                condition.add(conditionParameters.getKey() + " != " + conditionParameters.getValue());
        }
        for (ConditionParameters conditionParameters : reportCondition.getInCondition()) {
            if (conditionParameters.getValue() != null && !((ArrayList) conditionParameters.getValue()).isEmpty())
                condition.add(conditionParameters.getKey() + " in " + ((ArrayList) (conditionParameters.getValue())).stream().map(s -> s.toString()).collect(Collectors.joining(",", "(", ")")));
        }
        for (ConditionParameters conditionParameters : reportCondition.getNotInCondition()) {
            if (conditionParameters.getValue() != null && !((ArrayList) conditionParameters.getValue()).isEmpty())
                condition.add(conditionParameters.getKey() + " not in " + ((ArrayList) (conditionParameters.getValue())).stream().map(s -> s.toString()).collect(Collectors.joining(",", "(", ")")));
        }
        for (ConditionParameters conditionParameters : reportCondition.getLikeCondition()) {
            if (conditionParameters.getValue() != null)
                condition.add(conditionParameters.getKey() + " like '%" + conditionParameters.getValue() + "%'");
        }
        for (ConditionParameters conditionParameters : reportCondition.getCaseInsensitiveLikeCondition()) {
            if (conditionParameters.getValue() != null)
                condition.add("lower(" + conditionParameters.getKey() + ") like '%" + conditionParameters.getValue() + "'%");
        }
        for (String s : reportCondition.getNullCondition()) {
            condition.add(s + " is null ");
        }
        for (String s : reportCondition.getNotNullCondition()) {
            condition.add(s + " is not null ");
        }
        for (ConditionParameters conditionParameters : reportCondition.getMaxDateCondition()) {
            if (conditionParameters.getValue() != null)
                condition.add(conditionParameters.getKey() + " <= " + conditionParameters.getValue());
        }
        for (ConditionParameters conditionParameters : reportCondition.getMinDateCondition()) {
            if (conditionParameters.getValue() != null)
                condition.add(conditionParameters.getKey() + " >= " + conditionParameters.getValue());
        }
        for (ConditionParameters conditionParameters : reportCondition.getMaxNumberCondition()) {
            if (conditionParameters.getValue() != null)
                condition.add(conditionParameters.getKey() + " <= " + conditionParameters.getValue());
        }
        for (ConditionParameters conditionParameters : reportCondition.getMinNumberCondition()) {
            if (conditionParameters.getValue() != null)
                condition.add(conditionParameters.getKey() + " >= " + conditionParameters.getValue());
        }
        for (ConditionParameters conditionParameters : reportCondition.getMaxTimeCondition()) {
            if (conditionParameters.getValue() != null)
                condition.add(conditionParameters.getKey() + " <= '" + conditionParameters.getValue() + "'");
        }
        for (ConditionParameters conditionParameters : reportCondition.getMinTimeCondition()) {
            if (conditionParameters.getValue() != null)
                condition.add(conditionParameters.getKey() + " >= '" + conditionParameters.getValue() + "'");
        }
        for (ConditionParameters conditionParameters : reportCondition.getMinSqlCondition()) {
            if (conditionParameters.getValue() != null) {
                condition.add(conditionParameters.getKey() + " > " + conditionParameters.getValue());
            }
        }
        for (ConditionParameters conditionParameters : reportCondition.getMaxSqlCondition()) {
            if (conditionParameters.getValue() != null) {
                condition.add(conditionParameters.getKey() + " < " + conditionParameters.getValue());
            }
        }
        if (reportCondition.getAndConditions() != null && !reportCondition.getAndConditions().isEmpty()) {
            for (ReportCondition andCondition : reportCondition.getAndConditions()) {
                condition.add("( " + generateWhereClause(andCondition, true) + ")");
            }
        }
        if (reportCondition.getOrConditions() != null && !reportCondition.getOrConditions().isEmpty()) {
            for (ReportCondition orCondition : reportCondition.getOrConditions()) {
                condition.add("( " + generateWhereClause(orCondition, false) + ")");
            }
        }
        if (reportCondition.getOrCondition() != null) {
            condition.add("( " + generateWhereClause(reportCondition.getOrCondition(), false) + ")");
        }
        if (isAnd) {
            where.append(" where ");
            where.append(String.join(" and ", condition));
        } else {
            where.append(String.join(" or ", condition));
        }

        return where.toString();
    }
}
