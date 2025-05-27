package com.armin.operation.security.admin.repository.service;

import com.armin.security.statics.constants.ClientType;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.user.entity.UserSessionEntity;
import com.armin.operation.security.admin.dto.session.UserSessionFilter;
import com.armin.operation.security.admin.dto.session.UserSessionIn;
import com.armin.operation.security.admin.dto.session.UserSessionOut;
import com.armin.operation.security.admin.dto.session.UserSessionPageableFilter;
import com.armin.operation.security.admin.repository.dao.UserSessionDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exceptions error code range: 2201-2250
 *
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */
@Service
public class UserSessionService extends AbstractFilterableService<UserSessionEntity, UserSessionFilter, UserSessionDao> {
    private final ModelMapper modelMapper;

    @Autowired
    public UserSessionService(ModelMapper modelMapper, UserSessionDao userSessionDao) {
        super(userSessionDao);
        this.modelMapper = modelMapper;
    }

    public List<UserSessionOut> getAll(UserSessionPageableFilter filter, String[] include) {
        List<UserSessionEntity> userSessionEntities = super.getAllEntities(filter(filter), include);
        return userSessionEntities.stream().map(UserSessionOut::new).collect(Collectors.toList());
    }

    public UserSessionOut getById(int id, String[] include) throws SystemException {
        UserSessionEntity sessionEntity = super.getEntityById(id, include);
        return new UserSessionOut(sessionEntity);
    }

    public UserSessionEntity getEntityByUniqueId(Integer userId, String uniqueId) {
        return getDao().getByUniqueId(userId, uniqueId);
    }

    public UserSessionOut getExistingSessionOrCreateNewSession(UserSessionIn model) {
        UserSessionEntity userSessionEntity = getDao().getByUniqueId(model.getUserId(), model.getUniqueId());
        if (userSessionEntity == null) {
            userSessionEntity = modelMapper.map(model, UserSessionEntity.class);
            userSessionEntity.setCreated(LocalDateTime.now());
            userSessionEntity.setClientType(model.getClientType());
            super.createEntity(userSessionEntity);
        } else {
            if (model.getFirebaseToken() == null) {
                model.setFirebaseToken(userSessionEntity.getFirebaseToken());
            }
            modelMapper.map(model, userSessionEntity);
            super.updateEntity(userSessionEntity);
        }
        return modelMapper.map(userSessionEntity, UserSessionOut.class);
    }

    public UserSessionOut create(UserSessionIn model) {
        UserSessionEntity userSessionEntity = modelMapper.map(model, UserSessionEntity.class);
        userSessionEntity.setCreated(LocalDateTime.now());
        userSessionEntity.setClientType(model.getClientType());
        super.createEntity(userSessionEntity);
        return new UserSessionOut(userSessionEntity);

    }

    public void deleteAllByUserId(int userId) {
        getDao().deleteByUserId(userId);
    }

    public int count(UserSessionFilter filter) {
        return super.countEntity(filter(filter));
    }

    public boolean delete(int id) {
        return super.deleteById(id);
    }

    public UserSessionOut userGetById(int id, int userId, String[] include) throws SystemException {
        UserSessionEntity entity = getEntityById(id, null);
        if (entity.getUserId() == userId) {
            return getById(id, include);
        } else {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "session by user id:" + userId, 2201, null);
        }
    }

    public boolean userDelete(int id, int userId) throws SystemException {
        UserSessionEntity entity = getEntityById(id, null);
        if (entity.getUserId() == userId) {
            return delete(id);
        } else {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "session delete by user", 2202, null);
        }
    }

    public List<String> getFirebaseTokenByUserIdsAndClientTypes(List<Integer> userIds, List<ClientType> clientTypes) {
        return this.getDao().getFirebaseTokenByUserIdsAndClientTypes(userIds, clientTypes);
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
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addEqualCondition("userId", filter.getUserId());
        reportCondition.addLikeCondition("uniqueId", filter.getUniqueId());
        reportCondition.addCaseInsensitiveLikeCondition("os", filter.getOs());
        reportCondition.addLikeCondition("agent", filter.getAgent());
        reportCondition.addLikeCondition("ip", filter.getIp());
        reportCondition.addLikeCondition("firebaseToken", filter.getFirebaseToken());

        reportCondition.addMinZonedTimeCondition("created", filter.getCreatedMin());
        reportCondition.addMaxZonedTimeCondition("created", filter.getCreatedMax());

        return new ReportFilter(reportCondition, reportOption);
    }
}
