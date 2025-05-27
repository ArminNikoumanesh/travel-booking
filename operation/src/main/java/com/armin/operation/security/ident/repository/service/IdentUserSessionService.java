package com.armin.operation.security.ident.repository.service;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.Dao;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.user.entity.UserSessionEntity;
import com.armin.operation.security.ident.model.dto.UserSessionFilter;
import com.armin.operation.security.ident.model.dto.UserSessionOut;
import com.armin.operation.security.ident.model.dto.UserSessionPageableFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exceptions error code range: 2201-2250
 *
 * @author imax
 */
@Service
public class IdentUserSessionService extends AbstractFilterableService<UserSessionEntity, UserSessionFilter, Dao<UserSessionEntity>> {


    public List<UserSessionOut> getAll(int userId, int currentSessionId, UserSessionPageableFilter filter, String[] include) {
        filter.setUserId(userId);
        List<UserSessionEntity> userSessionEntities = super.getAllEntities(filter(filter), include);
        return userSessionEntities.stream().map(entity -> new UserSessionOut(entity, currentSessionId)).collect(Collectors.toList());
    }

    public int count(UserSessionFilter filter) {
        return super.countEntity(filter(filter));
    }

    public boolean delete(int id) {
        return super.deleteById(id);
    }

    public UserSessionOut getById(int userId, int id, int currentSessionId) throws SystemException {
        UserSessionEntity entity = getEntityById(id, null);
        if (entity.getUserId() == userId) {
            return new UserSessionOut(entity, currentSessionId);
        } else {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "session by user id:" + userId, 2201);
        }
    }

    public boolean delete(int userId, int id, int currentSessionId) throws SystemException {
        UserSessionEntity entity = getEntityById(id, null);
        if (entity.getId() == currentSessionId) {
            throw new SystemException(SystemError.LOGICAL_UN_AUTHORIZED, "id:" + currentSessionId, 2209);
        }
        if (entity.getUserId() == userId) {
            return delete(id);
        } else {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "session delete by user", 2202);
        }
    }

    public boolean deleteAll(int currentSessionId, int userId) {
        UserSessionFilter filter = new UserSessionFilter();
        filter.setUserId(userId);
        List<UserSessionEntity> userSessionEntities = super.getAllEntities(filter(filter), null);
        for (UserSessionEntity userSessionEntity : userSessionEntities) {
            if (userSessionEntity.getId() != currentSessionId) {
                delete(userSessionEntity.getId());
            }
        }
        return true;
    }


    @Override
    public ReportFilter filter(UserSessionFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof UserSessionPageableFilter) {
            UserSessionPageableFilter userSessionPageableFilter = (UserSessionPageableFilter) filter;
            reportOption.setPageNumber(userSessionPageableFilter.getPage());
            reportOption.setPageSize(userSessionPageableFilter.getSize());
            reportOption.setSortOptions(userSessionPageableFilter.getSort());
        }

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("userId", filter.getUserId());
        reportCondition.addLikeCondition("uniqueId", filter.getUniqueId());
        reportCondition.addLikeCondition("os", filter.getOs());
        reportCondition.addLikeCondition("agent", filter.getAgent());
        reportCondition.addLikeCondition("ip", filter.getIp());
        reportCondition.addLikeCondition("firebaseToken", filter.getFirebaseToken());

        reportCondition.addMinZonedTimeCondition("created", filter.getCreatedMin());
        reportCondition.addMaxZonedTimeCondition("created", filter.getCreatedMax());

        return new ReportFilter(reportCondition, reportOption);
    }
}
