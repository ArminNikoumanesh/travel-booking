package com.armin.utility.repository.orm;

import com.armin.utility.bl.ValidationEngine;
import com.armin.utility.repository.common.ConditionParameters;
import com.armin.utility.repository.common.SortOption;
import com.armin.utility.repository.orm.object.*;
import com.armin.utility.statics.enums.SortType;
import com.github.jknack.handlebars.internal.lang3.ArrayUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.armin.utility.repository.orm.Dao.addAttributesToEntityGraph;
import static com.armin.utility.statics.constants.UtilityConstant.*;


/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Repository
@Transactional
public class ReportDao<T> extends Parameterized<T> {

    /**
     * The {@link SessionFactory} Instance Representing Database Session Factory
     */
    @PersistenceContext(unitName = "")
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    protected Integer countEntity(ReportFilter reportFilter) {
        return this.countEntity(reportFilter, getClazz());
    }

    Integer countEntity(ReportFilter reportFilter, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        if (reportFilter.getOptions().isDistinct()) {
            criteriaQuery.select(criteriaBuilder.countDistinct(rootQuery));
        } else {
            criteriaQuery.select(criteriaBuilder.count(rootQuery));
        }
        Predicate predicate = this.generateCriteriaMainPredicate(false, reportFilter.getConditions(), criteriaBuilder, rootQuery, true);
        if (predicate != null)
            criteriaQuery.where(predicate);

        return getEntityManager().createQuery(criteriaQuery).getSingleResult().intValue();
    }

    Integer countEntityGroupBy(ReportFilter reportFilter, List<String> groupBy, List<String> selects, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<T> rootQuery = criteriaQuery.from(clazz);
        criteriaQuery.groupBy(groupBy.stream().map(rootQuery::get).collect(Collectors.toList()));
        criteriaQuery.multiselect(selects.stream().map(rootQuery::get).collect(Collectors.toList()));

        criteriaQuery.distinct(reportFilter.getOptions().isDistinct());

        List<Predicate> predicates = this.generateCriteriaMainPredicateGroupBy(true, reportFilter.getConditions(), criteriaBuilder, rootQuery, true);

        if (predicates.get(0) != null) {
            criteriaQuery.where(predicates.get(0));
        }
        if (predicates.get(1) != null) {
            criteriaQuery.having(predicates.get(1));
        }

        return getEntityManager().createQuery(criteriaQuery).getResultList().size();
    }

    protected List<T> reportEntity(ReportFilter reportFilter) {
        return this.reportEntity(reportFilter, getClazz());
    }

    List<T> reportEntity(ReportFilter reportFilter, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        criteriaQuery.select(rootQuery);
        Predicate predicate = this.generateCriteriaMainPredicate(true, reportFilter.getConditions(), criteriaBuilder, rootQuery, true);
        if (predicate != null)
            criteriaQuery.where(predicate);

        if (!reportFilter.getOptions().getSortOptions().isEmpty()) {
            List<Order> sorts = new ArrayList<>();
            for (SortOption item : reportFilter.getOptions().getSortOptions()) {
                if (item.getType() == SortType.ASCENDING)
                    sorts.add(criteriaBuilder.asc(rootQuery.get(item.getColumn())));
                else
                    sorts.add(criteriaBuilder.desc(rootQuery.get(item.getColumn())));
            }
            criteriaQuery.orderBy(sorts);
        }

        int maxResult = MAXIMUM_REPORT_PAGE_SIZE < reportFilter.getOptions().getPageSize() ? MAXIMUM_REPORT_PAGE_SIZE : reportFilter.getOptions().getPageSize();
        Query query = getEntityManager().createQuery(criteriaQuery);
        query.setFirstResult((reportFilter.getOptions().getPageNumber() - 1) * reportFilter.getOptions().getPageSize())
                .setMaxResults(maxResult);

        return query.getResultList();
    }

    <R> List<R> reportEntityGroupBy(ReportFilter reportFilter, List<String> groupBy, Class<T> root, Class<R> output) {
        List<String> selects = Arrays.stream(root.getFields()).map(Field::getName).collect(Collectors.toList());
        return reportEntityGroupBy(reportFilter, groupBy, selects, root, output);
    }

    <R> List<R> reportEntityGroupBy(ReportFilter reportFilter, List<String> groupBy, List<String> selects, Class<T> root, Class<R> output) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<R> criteriaQuery = criteriaBuilder.createQuery(output);
        Root<T> rootQuery = criteriaQuery.from(root);

        if (selects == null || selects.isEmpty()) {
            selects = groupBy;
        }
        criteriaQuery.multiselect(selects.stream().map(rootQuery::get).collect(Collectors.toList()));
        criteriaQuery.groupBy(groupBy.stream().map(rootQuery::get).collect(Collectors.toList()));

        List<Predicate> predicates = this.generateCriteriaMainPredicateGroupBy(true, reportFilter.getConditions(), criteriaBuilder, rootQuery, true);

        if (predicates.get(0) != null) {
            criteriaQuery.where(predicates.get(0));
        }
        if (predicates.get(1) != null) {
            criteriaQuery.having(predicates.get(1));
        }

        createSortExpression(criteriaQuery, rootQuery, criteriaBuilder, reportFilter.getOptions());

        criteriaQuery.distinct(reportFilter.getOptions().isDistinct());

        int maxResult = MAXIMUM_REPORT_PAGE_SIZE < reportFilter.getOptions().getPageSize() ? MAXIMUM_REPORT_PAGE_SIZE : reportFilter.getOptions().getPageSize();
        Query query = getEntityManager().createQuery(criteriaQuery);
        query.setFirstResult((reportFilter.getOptions().getPageNumber() - 1) * reportFilter.getOptions().getPageSize())
                .setMaxResults(maxResult);

        return query.getResultList();
    }


    protected List<T> reportEntityWithInclude(ReportFilter reportFilter, String[] include) {
        return this.reportEntityWithInclude(reportFilter, include, getClazz());
    }

    List<T> reportEntityWithInclude(ReportFilter reportFilter, String[] include, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);

        criteriaQuery.select(rootQuery);
        Predicate predicate = this.generateCriteriaMainPredicate(true, reportFilter.getConditions(), criteriaBuilder, rootQuery, true);
        if (predicate != null)
            criteriaQuery.where(predicate);

        EntityGraph eg = getEntityManager().createEntityGraph(clazz);
        List<String> columns = ValidationEngine.fieldNames(clazz);
        List<String> finalIncludes = columns.stream().filter(item -> ArrayUtils.contains(include, item)).collect(Collectors.toList());

        for (String property : finalIncludes) {
            eg.addAttributeNodes(property);
        }

        if (!reportFilter.getOptions().getSortOptions().isEmpty()) {
            List<Order> sorts = new ArrayList<>();
            for (SortOption item : reportFilter.getOptions().getSortOptions()) {
                if (item.getType() == SortType.ASCENDING)
                    sorts.add(criteriaBuilder.asc(rootQuery.get(item.getColumn())));
                else
                    sorts.add(criteriaBuilder.desc(rootQuery.get(item.getColumn())));
            }
            criteriaQuery.orderBy(sorts);
        }

        TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery.distinct(reportFilter.getOptions().isDistinct()));
        int maxResult = MAXIMUM_REPORT_PAGE_SIZE < reportFilter.getOptions().getPageSize() ? MAXIMUM_REPORT_PAGE_SIZE : reportFilter.getOptions().getPageSize();
        query.setFirstResult((reportFilter.getOptions().getPageNumber() - 1) * reportFilter.getOptions().getPageSize())
                .setMaxResults(maxResult);

        return query.setHint("jakarta.persistence.fetchgraph", eg)
                .getResultList();
    }


    protected List<T> reportEntityWithIncludeJoin(ReportFilter reportFilter, String[] include) {
        return this.reportEntityWithIncludeJoin(reportFilter, include, getClazz());
    }


    List<T> reportEntityWithIncludeJoin(ReportFilter reportFilter, String[] include, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);
        criteriaQuery.select(rootQuery);
        Predicate predicate = this.generateCriteriaMainPredicate(true, reportFilter.getConditions(), criteriaBuilder, rootQuery, true);
        if (predicate != null) {
            criteriaQuery.where(predicate);
        }
        EntityGraph eg = getEntityManager().createEntityGraph(clazz);
        List<String> columns = ValidationEngine.fieldNames(clazz);

        addAttributesToEntityGraph(eg, include, columns);
        createSortExpression(criteriaQuery, rootQuery, criteriaBuilder, reportFilter.getOptions());
        TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery.distinct(reportFilter.getOptions().isDistinct()));

        if (reportFilter.getOptions().getPageSize() != null && reportFilter.getOptions().getPageSize() > 0) {
            int maxResult = MAXIMUM_REPORT_PAGE_SIZE < reportFilter.getOptions().getPageSize() ? MAXIMUM_REPORT_PAGE_SIZE : reportFilter.getOptions().getPageSize();
            query.setMaxResults(maxResult);
            if (reportFilter.getOptions().getPageNumber() != null && reportFilter.getOptions().getPageNumber() > 0) {
                query.setFirstResult((reportFilter.getOptions().getPageNumber() - 1) * reportFilter.getOptions().getPageSize());
            }
        } else {
            query.setMaxResults(DEFAULT_REPORT_PAGE_SIZE);
        }
        if (reportFilter.getOptions().getPageNumber() == null || reportFilter.getOptions().getPageNumber() < 0) {
            query.setFirstResult(DEFAULT_REPORT_PAGE_NUMBER);
        }

        return query
                .setHint("jakarta.persistence.fetchgraph", eg)
                .getResultList();
    }

    List<T> reportEntityWithIncludeJoinWithSizeCheck(ReportFilter reportFilter, String[] include, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<T> rootQuery = criteriaQuery.from(clazz);

        Predicate predicate = this.generateCriteriaMainPredicate(false, reportFilter.getConditions(), criteriaBuilder, rootQuery, true);
        if (predicate != null) {
            criteriaQuery.where(predicate);
        }

        if (reportFilter.getOptions().getSortOptions() == null || reportFilter.getOptions().getSortOptions().isEmpty()) {
            criteriaQuery.select(criteriaBuilder.tuple(rootQuery.get("id")));
        } else {
            List<Path> paths = createSortExpression(criteriaQuery, rootQuery, criteriaBuilder, reportFilter.getOptions());
            if (paths.isEmpty()) {
                paths.addAll(reportFilter.getOptions().getSortOptions().stream().map(o -> rootQuery.get(o.getColumn())).collect(Collectors.toList()));
            }
            paths.add(0, rootQuery.get("id"));
            Selection[] selections = new Selection[paths.size()];
            selections = paths.toArray(selections);
            criteriaQuery.select(criteriaBuilder.tuple(selections));
        }

        TypedQuery<Tuple> query = getEntityManager().createQuery(criteriaQuery.distinct(reportFilter.getOptions().isDistinct()));

        if (reportFilter.getOptions().getPageSize() != null && reportFilter.getOptions().getPageSize() > 0) {
            int maxResult = MAXIMUM_REPORT_PAGE_SIZE < reportFilter.getOptions().getPageSize() ? MAXIMUM_REPORT_PAGE_SIZE : reportFilter.getOptions().getPageSize();
            query.setMaxResults(maxResult);
            if (reportFilter.getOptions().getPageNumber() != null && reportFilter.getOptions().getPageNumber() > 0) {
                query.setFirstResult((reportFilter.getOptions().getPageNumber() - 1) * reportFilter.getOptions().getPageSize());
            }
        } else {
            query.setMaxResults(DEFAULT_REPORT_PAGE_SIZE);
        }
        if (reportFilter.getOptions().getPageNumber() == null || reportFilter.getOptions().getPageNumber() < 0) {
            query.setFirstResult(DEFAULT_REPORT_PAGE_NUMBER);
        }

        List<Tuple> resultIds = query.getResultList();
        List<Object> ids = resultIds.stream().map(o -> o.get(0)).collect(Collectors.toList());
        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addInCondition("id", ids);
        reportFilter.getOptions().setPageSize(null);
        reportFilter.getOptions().setPageNumber(null);
        reportFilter.getOptions().setDistinct(false);
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        return reportEntityWithIncludeJoinWithoutMaxLimit(new ReportFilter(reportCondition, reportFilter.getOptions()), include, clazz);

    }

    List<T> reportEntityWithIncludeJoinWithoutMaxLimit(ReportFilter reportFilter, String[] include, Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> rootQuery = criteriaQuery.from(clazz);
        criteriaQuery.select(rootQuery);
        Predicate predicate = this.generateCriteriaMainPredicate(true, reportFilter.getConditions(), criteriaBuilder, rootQuery, true);
        if (predicate != null) {
            criteriaQuery.where(predicate);
        }
        EntityGraph eg = getEntityManager().createEntityGraph(clazz);
        List<String> columns = ValidationEngine.fieldNames(clazz);

        addAttributesToEntityGraph(eg, include, columns);
        createSortExpression(criteriaQuery, rootQuery, criteriaBuilder, reportFilter.getOptions());
        TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery.distinct(reportFilter.getOptions().isDistinct()));
        return query
                .setHint("jakarta.persistence.fetchgraph", eg)
                .getResultList();
    }

    protected <R> List<R> reportHqlWithIncludeJoin(Class<R> cls, String hql, ReportOption reportOption, Map<String, Object> parameters, String[] include) {
        Query query = getEntityManager().createQuery(hql);
        EntityGraph eg = getEntityManager().createEntityGraph(cls);
        List<String> columns = ValidationEngine.fieldNames(cls);

        for (Map.Entry<String, Object> pair : parameters.entrySet())
            query.setParameter(pair.getKey(), pair.getValue());
        addAttributesToEntityGraph(eg, include, columns);
        int maxResult = MAXIMUM_REPORT_PAGE_SIZE < reportOption.getPageSize() ? MAXIMUM_REPORT_PAGE_SIZE : reportOption.getPageSize();
        query.setFirstResult((reportOption.getPageNumber() - 1) * reportOption.getPageSize())
                .setMaxResults(maxResult);
        return query.setHint("jakarta.persistence.fetchgraph", eg)
                .getResultList();
    }

    private List<Path> createSortExpression(CriteriaQuery criteriaQuery, From rootQuery, CriteriaBuilder criteriaBuilder, ReportOption reportOption) {
        List<Order> sorts = new ArrayList<>();
        List<Path> paths = new ArrayList<>();
        List<SortOption> lstSortOptions = reportOption.getSortOptions();
        if (lstSortOptions != null && !lstSortOptions.isEmpty()) {
            criteriaQuery.orderBy(addSorts(rootQuery, criteriaBuilder, lstSortOptions, sorts, paths));
        }
        return paths;
    }

    private List<Order> addSorts(From rootQuery, CriteriaBuilder criteriaBuilder, List<SortOption> lstSortOptions, List<Order> sorts, List<Path> paths) {

        for (SortOption sortOption : lstSortOptions) {
            String[] sortColumns = sortOption.getColumn().split("\\.");
            if (sortColumns.length > 1) {
                try {
                    int[] splitNumber = {0};
                    From chainJoin = setJoinPath(rootQuery, sortColumns, JoinType.LEFT, splitNumber);
                    Path pathJoin = chainJoin.get(sortColumns[splitNumber[0]]);
                    paths.add(pathJoin);

                    if (sortOption.getType() == SortType.ASCENDING) {
                        sorts.add(criteriaBuilder.asc(pathJoin));
                    } else {
                        sorts.add(criteriaBuilder.desc(pathJoin));
                    }
                } catch (IllegalArgumentException ignored) {
                    /** handle invalid entity and column name
                     * can't throw exception because exception must add to method
                     * and must add it to all getAllEntities in project
                     */
                }
            } else {
                if (sortOption.getType() == SortType.ASCENDING) {
                    sorts.add(criteriaBuilder.asc(rootQuery.get(sortOption.getColumn())));
                } else {
                    sorts.add(criteriaBuilder.desc(rootQuery.get(sortOption.getColumn())));
                }
            }
        }
        return sorts;
    }

    private From setJoinPath(From query, String[] sortColumns, JoinType joinType, int[] splitNumber) {
        From chainJoin = query;
        while (splitNumber[0] < sortColumns.length - 1) {
            chainJoin = getJoin(chainJoin, sortColumns, joinType, splitNumber);
            splitNumber[0] = splitNumber[0] + 1;
        }
        return chainJoin;
    }

    private From getJoin(From query, String[] sortColumns, JoinType joinType, int[] splitNumber) {
        List<Join> prevJoins = new ArrayList<>(query.getJoins());
        From chainJoin = null;
        for (Join currentJoin : prevJoins) {
            if (sortColumns[splitNumber[0]].equals(currentJoin.getAttribute().getName()) &&
                    joinType.equals(currentJoin.getJoinType())) {
                chainJoin = currentJoin;
                break;
            }
        }
        if (chainJoin == null) {
            chainJoin = query.join(sortColumns[splitNumber[0]], joinType);
        }
        return chainJoin;
    }

    private Predicate generateCriteriaMainPredicate(boolean fetch, ReportCondition reportCondition, CriteriaBuilder criteriaBuilder, From rootQuery, boolean generateAndPredicates) {
        List<Predicate> mainPredicates = generatePredicates(fetch, reportCondition, criteriaBuilder, rootQuery, generateAndPredicates);
        Predicate main = null;
        if (!mainPredicates.isEmpty() && generateAndPredicates) {
            main = criteriaBuilder.and(mainPredicates.toArray(new Predicate[]{}));
        } else if (!mainPredicates.isEmpty()) {
            main = criteriaBuilder.or(mainPredicates.toArray(new Predicate[]{}));
        }
        return main;
    }

    private List<Predicate> generateCriteriaMainPredicateGroupBy(boolean fetch, ReportCondition reportCondition, CriteriaBuilder criteriaBuilder, From rootQuery, boolean generateAndPredicates) {
        List<Predicate> haveCondition = new ArrayList<>();
        List<Predicate> mainPredicates = generatePredicatesGroupBy(fetch, reportCondition, criteriaBuilder, rootQuery, haveCondition);
        Predicate main = null;
        Predicate havePredicate = null;
        if (!mainPredicates.isEmpty() && generateAndPredicates) {
            main = criteriaBuilder.and(mainPredicates.toArray(new Predicate[]{}));
        } else if (!mainPredicates.isEmpty()) {
            main = criteriaBuilder.or(mainPredicates.toArray(new Predicate[]{}));
        }
//        generate have predicate
        if (!haveCondition.isEmpty() && generateAndPredicates) {
            havePredicate = criteriaBuilder.and(haveCondition.toArray(new Predicate[]{}));
        } else if (!haveCondition.isEmpty()) {
            havePredicate = criteriaBuilder.or(haveCondition.toArray(new Predicate[]{}));
        }

        return Arrays.asList(main, havePredicate);
    }

    private List<Predicate> generateHaveCondition(List<HaveCondition> haveConditions, CriteriaBuilder criteriaBuilder, From rootQuery) {
        List<Predicate> mainPredicates = new ArrayList<>();
        for (HaveCondition eachAggregate : haveConditions) {
            Expression expression;
            Predicate predicate = null;
            if (eachAggregate.getAction().equals(ColumnAction.SIZE)) {
                expression = criteriaBuilder.size(rootQuery.get(eachAggregate.getColumn()));
            } else if (eachAggregate.getAction().equals(ColumnAction.SUM)) {
                expression = criteriaBuilder.sum(rootQuery.get(eachAggregate.getColumn()));
            } else {
                expression = rootQuery.get(eachAggregate.getColumn());
            }
            switch (eachAggregate.getOperation()) {
                case E:
                    predicate = criteriaBuilder.equal(expression, eachAggregate.getValue());
                    break;
                case GE:
                    predicate = criteriaBuilder.ge(expression, (Number) eachAggregate.getValue());
                    break;
                case LE:
                    predicate = criteriaBuilder.le(expression, (Number) eachAggregate.getValue());
                    break;
//                    case BETWEEN:
//                        predicate = criteriaBuilder.equal(expression, eachAggregate.getType());
//                        break;
                case G:
                    predicate = criteriaBuilder.gt(expression, (Number) eachAggregate.getValue());
                    break;
                case L:
                    predicate = criteriaBuilder.lt(expression, (Number) eachAggregate.getValue());
                    break;
            }
            if (predicate != null) {
                mainPredicates.add(predicate);
            }
        }
        return mainPredicates;
    }

    private List<Predicate> generatePredicates(boolean fetch, ReportCondition reportCondition, CriteriaBuilder criteriaBuilder, From rootQuery, boolean generateAndPredicates) {
        List<Predicate> mainPredicates = new ArrayList<>();
        for (String item : reportCondition.getNullCondition())
            mainPredicates.add(criteriaBuilder.isNull(rootQuery.get(item)));
        for (String item : reportCondition.getNotNullCondition())
            mainPredicates.add(criteriaBuilder.isNotNull(rootQuery.get(item)));

        for (ConditionParameters pair : reportCondition.getEqualCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));
            }
        }
        for (ConditionParameters pair : reportCondition.getNotEqualCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.notEqual(rootQuery.get(pair.getKey()), pair.getValue()));
            }
        }

        for (ConditionParameters parameters : reportCondition.getLikeCondition()) {
            if (parameters.getValue() != null) {
                mainPredicates.add(criteriaBuilder.like(rootQuery.get(parameters.getKey()), "%" + parameters.getValue() + "%"));
            }
        }

        for (ConditionParameters parameters : reportCondition.getCaseInsensitiveLikeCondition()) {
            if (parameters.getValue() != null) {
                mainPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(rootQuery.get(parameters.getKey())), criteriaBuilder.lower(criteriaBuilder.literal("%" + parameters.getValue() + "%"))));
            }
        }

        for (ConditionParameters pair : reportCondition.getInCondition()) {
            if (pair.getValue() != null && !((Collection) pair.getValue()).isEmpty()) {
                mainPredicates.add(rootQuery.get(pair.getKey()).in((ArrayList) pair.getValue()));
            }
        }

        for (ConditionParameters pair : reportCondition.getMinNumberCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.ge(rootQuery.get(pair.getKey()), (Number) pair.getValue()));
            }
        }
        for (ConditionParameters pair : reportCondition.getMaxNumberCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.le(rootQuery.get(pair.getKey()), (Number) pair.getValue()));
            }
        }

        for (ConditionParameters pair : reportCondition.getMinDateCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.greaterThanOrEqualTo(rootQuery.get(pair.getKey()), (Date) pair.getValue()));
            }
        }

        for (ConditionParameters pair : reportCondition.getMaxDateCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.lessThanOrEqualTo(rootQuery.get(pair.getKey()), (Date) pair.getValue()));
            }
        }

        for (ConditionParameters pair : reportCondition.getMinTimeCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.greaterThanOrEqualTo(rootQuery.get(pair.getKey()), (Comparable) pair.getValue()));
            }
        }

        for (ConditionParameters pair : reportCondition.getMaxTimeCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.lessThanOrEqualTo(rootQuery.get(pair.getKey()), (Comparable) pair.getValue()));
            }
        }

        if (reportCondition.getOrCondition() != null) {
            Predicate orPredicate = generateCriteriaMainPredicate(fetch, reportCondition.getOrCondition(), criteriaBuilder, rootQuery, false);
            if (orPredicate != null) {
                mainPredicates.add(orPredicate);
            }
        }

        if (reportCondition.getOrConditions() != null && !reportCondition.getOrConditions().isEmpty()) {
            List<Predicate> orPredicates = new ArrayList<>();
            for (ReportCondition orCondition : reportCondition.getOrConditions()) {
                Predicate orPredicate = generateCriteriaMainPredicate(fetch, orCondition, criteriaBuilder, rootQuery, false);
                if (orPredicate != null) {
                    orPredicates.add(orPredicate);
                }
            }

//            if (generateAndPredicates) {
//                mainPredicates.add(criteriaBuilder.and(orPredicates.toArray(new Predicate[]{})));
//            } else {
            if (!orPredicates.isEmpty()) {
                mainPredicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[]{})));
            }
//            }
        }
        if (reportCondition.getAndConditions() != null && !reportCondition.getAndConditions().isEmpty()) {
            List<Predicate> andPredicates = new ArrayList<>();
            for (ReportCondition andCondition : reportCondition.getAndConditions()) {
                Predicate andPredicate = generateCriteriaMainPredicate(fetch, andCondition, criteriaBuilder, rootQuery, true);
                if (andPredicate != null) {
                    andPredicates.add(andPredicate);
                }
            }
            if (generateAndPredicates) {
            mainPredicates.add(criteriaBuilder.and(andPredicates.toArray(new Predicate[]{})));
            } else {
                mainPredicates.add(criteriaBuilder.or(andPredicates.toArray(new Predicate[]{})));
            }
//            mainPredicates.addAll(andPredicates);
        }
        for (ConditionParameters pair : reportCondition.getNotInCondition()) {
            if (pair.getValue() != null && !((Collection) pair.getValue()).isEmpty()) {
                mainPredicates.add(criteriaBuilder.not(rootQuery.get(pair.getKey()).in((ArrayList) pair.getValue())));
            }
        }
        for (ReportCriteriaJoinCondition joinCondition : reportCondition.getJoinCondition()) {
            From join = (fetch && joinCondition.isFetch()) ? (Join) rootQuery.fetch(joinCondition.getName(), joinCondition.getJoinType()) : rootQuery.join(joinCondition.getName(), joinCondition.getJoinType());
            mainPredicates.addAll(generatePredicates(fetch, joinCondition, criteriaBuilder, join, false));
        }
        return mainPredicates;
    }

    private List<Predicate> generatePredicatesGroupBy(boolean fetch, ReportCondition reportCondition, CriteriaBuilder criteriaBuilder, From rootQuery, List<Predicate> havePredicate) {
        List<Predicate> mainPredicates = new ArrayList<>();
        for (String item : reportCondition.getNullCondition())
            mainPredicates.add(criteriaBuilder.isNull(rootQuery.get(item)));
        for (String item : reportCondition.getNotNullCondition())
            mainPredicates.add(criteriaBuilder.isNotNull(rootQuery.get(item)));

        for (ConditionParameters pair : reportCondition.getEqualCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.equal(rootQuery.get(pair.getKey()), pair.getValue()));
            }
        }
        for (ConditionParameters pair : reportCondition.getNotEqualCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.notEqual(rootQuery.get(pair.getKey()), pair.getValue()));
            }
        }

        for (ConditionParameters parameters : reportCondition.getLikeCondition()) {
            if (parameters.getValue() != null) {
                mainPredicates.add(criteriaBuilder.like(rootQuery.get(parameters.getKey()), "%" + parameters.getValue() + "%"));
            }
        }

        for (ConditionParameters parameters : reportCondition.getCaseInsensitiveLikeCondition()) {
            if (parameters.getValue() != null) {
                mainPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(rootQuery.get(parameters.getKey())), criteriaBuilder.lower(criteriaBuilder.literal("%" + parameters.getValue() + "%"))));
            }
        }

        for (ConditionParameters pair : reportCondition.getInCondition()) {
            if (pair.getValue() != null && !((Collection) pair.getValue()).isEmpty()) {
                mainPredicates.add(rootQuery.get(pair.getKey()).in((ArrayList) pair.getValue()));
            }
        }

        for (ConditionParameters pair : reportCondition.getMinNumberCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.ge(rootQuery.get(pair.getKey()), (Number) pair.getValue()));
            }
        }
        for (ConditionParameters pair : reportCondition.getMaxNumberCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.le(rootQuery.get(pair.getKey()), (Number) pair.getValue()));
            }
        }

        for (ConditionParameters pair : reportCondition.getMinDateCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.greaterThanOrEqualTo(rootQuery.get(pair.getKey()), (Date) pair.getValue()));
            }
        }

        for (ConditionParameters pair : reportCondition.getMaxDateCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.lessThanOrEqualTo(rootQuery.get(pair.getKey()), (Date) pair.getValue()));
            }
        }

        for (ConditionParameters pair : reportCondition.getMinTimeCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.greaterThanOrEqualTo(rootQuery.get(pair.getKey()), (Comparable) pair.getValue()));
            }
        }

        for (ConditionParameters pair : reportCondition.getMaxTimeCondition()) {
            if (pair.getValue() != null && pair.getValue() != "") {
                mainPredicates.add(criteriaBuilder.lessThanOrEqualTo(rootQuery.get(pair.getKey()), (Comparable) pair.getValue()));
            }
        }

        if (reportCondition.getOrCondition() != null) {
            Predicate orPredicate = generateCriteriaMainPredicate(fetch, reportCondition.getOrCondition(), criteriaBuilder, rootQuery, false);
            if (orPredicate != null) {
                mainPredicates.add(orPredicate);
            }
        }
        havePredicate.addAll(generateHaveCondition(reportCondition.getHaveCondition(), criteriaBuilder, rootQuery));

        for (ConditionParameters pair : reportCondition.getNotInCondition()) {
            if (pair.getValue() != null && !((Collection) pair.getValue()).isEmpty()) {
                mainPredicates.add(criteriaBuilder.not(rootQuery.get(pair.getKey()).in((ArrayList) pair.getValue())));
            }
        }
        for (ReportCriteriaJoinCondition joinCondition : reportCondition.getJoinCondition()) {
            From join = (fetch && joinCondition.isFetch()) ? (Join) rootQuery.fetch(joinCondition.getName(), joinCondition.getJoinType()) : rootQuery.join(joinCondition.getName(), joinCondition.getJoinType());
            mainPredicates.addAll(generatePredicatesGroupBy(fetch, joinCondition, criteriaBuilder, join, havePredicate));
        }
        return mainPredicates;
    }

}