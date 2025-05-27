package com.armin.utility.repository.orm;

import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.service.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Exceptions error code range: 1201-1250
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public abstract class AbstractService<T, D extends Dao<T>> extends Parameterized<T> implements Service<T> {

    @Autowired
    private ReportDao<T> reportDao;

    private D dao;

    public AbstractService() {
        dao = (D) StaticApplicationContext.getContext().getBean("dao");
    }

    public AbstractService(D dao) {
        this.dao = dao;
    }

    @Override
    public List<T> getAllEntities(ReportFilter reportFilter, String[] include) {
        if (reportFilter.getOptions().isExport()) {
            reportFilter.getOptions().setPageNumber(null);
            reportFilter.getOptions().setPageSize(null);
            return getAllEntitiesWithoutMaxLimit(reportFilter, include);
        } else {
            return reportDao.reportEntityWithIncludeJoin(reportFilter, include, getClazz());

        }
    }

    @Override
    public List<T> getAllEntitiesLimit(ReportFilter reportFilter, String[] include) {
        if (reportFilter.getOptions().isExport()) {
            reportFilter.getOptions().setPageNumber(null);
            reportFilter.getOptions().setPageSize(null);
            return getAllEntitiesWithoutMaxLimit(reportFilter, include);
        } else if (include != null || !reportFilter.getConditions().getJoinCondition().isEmpty()) {
            return reportDao.reportEntityWithIncludeJoinWithSizeCheck(reportFilter, include, getClazz());
        } else {
            return reportDao.reportEntityWithIncludeJoin(reportFilter, null, getClazz());

        }
    }


    @Override
    public <R> List<R> getAllEntitiesGroupBy(ReportFilter reportFilter, List<String> groupBy, List<String> selects, Class<R> rClass) {
        return reportDao.reportEntityGroupBy(reportFilter, groupBy, selects, getClazz(), rClass);
    }

    @Override
    public <R> List<R> getAllEntitiesGroupBy(ReportFilter reportFilter, List<String> groupBy, Class<R> output) {
        return reportDao.reportEntityGroupBy(reportFilter, groupBy, getClazz(), output);
    }

    @Override
    public List<T> getAllEntitiesWithoutMaxLimit(ReportFilter reportFilter, String[] include) {
        return reportDao.reportEntityWithIncludeJoinWithoutMaxLimit(reportFilter, include, getClazz());
    }

    @Override
    public List<T> getAllEntitiesWithFilter(ReportFilter reportFilter, String[] include) {
        return reportDao.reportEntityWithIncludeJoin(reportFilter, include, getClazz());
    }

    @Override
    public List<T> getAllEntitiesJoin(ReportFilter reportFilter, String[] include) {
        return reportDao.reportEntityWithIncludeJoin(reportFilter, include, getClazz());
    }

    @Override
    public int countEntity(ReportFilter reportFilter) {
        return reportDao.countEntity(reportFilter, getClazz());
    }

    @Override
    public int countEntityGroupBy(ReportFilter reportFilter, List<String> groupBy, List<String> selects) {
        return reportDao.countEntityGroupBy(reportFilter, groupBy, selects, getClazz());
    }

    @Override
    public <R> int countEntityGroupBy(ReportFilter reportFilter, List<String> groupBy, Class<R> source) {
        List<String> selects = Arrays.stream(source.getFields()).map(Field::getName).collect(Collectors.toList());
        return countEntityGroupBy(reportFilter, groupBy, selects);
    }

    @Override
    public T getEntityById(int id, String[] includes) throws SystemException {
        return getEntityById((Object) id, includes);
    }

    @Override
    public T getEntityById(Object id, String[] includes) throws SystemException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        T result = dao.byAndConditions(map, includes, getClazz());
        if (result == null) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "id:" + id, 1201);
        }
        return result;
    }

    @Override
    public T getEntityById(int id, String[] includes, int errorCode) throws SystemException {
        return getEntityById((Object) id, includes, errorCode);
    }

    @Override
    public T getEntityById(Object id, String[] includes, int errorCode) throws SystemException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        T result = dao.byAndConditions(map, includes, getClazz());
        if (result == null) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "id:" + id, errorCode);
        }
        return result;
    }

    @Override
    public T createEntity(T entity) {
        return dao.saveEntity(entity);
    }

    @Override
    public T updateEntity(T entity) {
        return dao.updateEntity(entity);
    }

    @Override
    public T mergeEntity(T entity) {
        return dao.mergeEntity(entity);
    }

    @Override
    public void detach(T entity) {
        dao.detach(entity);
    }

    @Override
    public void flush() {
        dao.flush();
    }

    @Override
    public void createOrUpdateEntity(T entity) {
        dao.saveOrUpdate(entity);
    }

    @Override
    public void saveOrUpdateEntityCollection(Collection<T> collection) {
        dao.saveOrUpdateCollection(collection);
    }

    @Override
    public void saveOrUpdateEntity(T entity) {
        dao.saveOrUpdate(entity);
    }

    @Override
    public boolean deleteById(int id) {
        dao.deleteById(id, getClazz());
        return true;
    }

    @Override
    public Optional<T> optionalById(Object id, String[] includes) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        T result = dao.byAndConditions(map, includes, getClazz());
        return Optional.of(result);
    }

    @Override
    public boolean deleteById(Object id) {
        dao.deleteById(id, getClazz());
        return true;
    }

    @Override
    public void deleteEntity(T entity) {
        dao.delete(entity);
    }

    @Override
    public List<T> getEntitiesByIds(List<Integer> ids) {
        return dao.listByIds(ids, getClazz());
    }

    public <T> List<T> getEntitiesWithSqlFilter(String select, String groupBy, ReportFilter reportFilter, Class<T> cls) {
        return dao.reportEntityWithSqlQuery(select, groupBy, reportFilter, cls);
    }

    protected D getDao() {
        return dao;
    }
}
