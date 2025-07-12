package com.armin.operation.address.ident.repository.service;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.address.address.entity.UserAddressEntity;
import com.armin.database.address.address.repository.service.BaseUserAddressService;
import com.armin.database.address.record.entity.AddressRecordEntity;
import com.armin.database.address.record.repository.service.BaseAddressRecordService;
import com.armin.operation.address.ident.model.UserAddressFilter;
import com.armin.operation.address.ident.model.UserAddressIn;
import com.armin.operation.address.ident.model.UserAddressOut;
import com.armin.operation.address.ident.model.UserAddressPageableFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther : Armin.Nik
 * @project : chabok
 * @date : 20.11.22
 */
@Service
public class IdentUserAddressService {
    private final String[] INCLUDE = {"addressRecord"};
    private final ModelMapper modelMapper;
    private final BaseUserAddressService baseUserAddressService;
    private final BaseAddressRecordService baseAddressRecordService;

    @Autowired
    public IdentUserAddressService(ModelMapper modelMapper, BaseUserAddressService baseUserAddressService,
                                   BaseAddressRecordService baseAddressRecordService) {
        this.modelMapper = modelMapper;
        this.baseUserAddressService = baseUserAddressService;
        this.baseAddressRecordService = baseAddressRecordService;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserAddressOut create(UserAddressIn modelIn, Integer userId) throws SystemException {
        AddressRecordEntity recordEntity = createAddressRecord(modelIn, userId, null);
        UserAddressEntity userAddress = new UserAddressEntity(userId, recordEntity.getId());
        userAddress.setAddressRecordId(recordEntity.getId());
        userAddress.setDefaultAddress(baseUserAddressService.checkUserDefaultAddress(userId));
        baseUserAddressService.createEntity(userAddress);
        baseUserAddressService.flush();
        recordEntity.setUserAddressId(userAddress.getId());
        baseAddressRecordService.updateEntity(recordEntity);
        return new UserAddressOut(userAddress);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserAddressOut update(UserAddressIn editIn, int id, int userId) throws SystemException {
        UserAddressEntity addressEntity = baseUserAddressService.getEntityById(id, null);
        AddressRecordEntity recordEntity = createAddressRecord(editIn, userId, addressEntity.getId());
        addressEntity.setAddressRecordId(recordEntity.getId());
        baseUserAddressService.updateEntity(addressEntity);
        return new UserAddressOut(addressEntity);
    }

    public int countEntity(UserAddressFilter filter, int userId) {
        filter.putUserId(userId);
        return baseUserAddressService.countEntity(filter(filter));
    }

    public List<UserAddressOut> getAll(int userId) {
        List<UserAddressEntity> addressEntities = baseUserAddressService.getAll(userId);
        return addressEntities.stream()
                .map(UserAddressOut::new)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean setDefault(int userId, int id) {
        baseUserAddressService.updateFalseDefaultForAll(userId);
        baseUserAddressService.setDefault(userId, id);
        return true;
    }

    public boolean delete(int userId, int id) throws SystemException {
        UserAddressEntity entity = baseUserAddressService.getByIdAndUserId(userId, id);
        if (entity.isDefaultAddress()){
            throw new SystemException(SystemError.DATA_NOT_FOUND, "can't delete default" , 54545);
        }
        entity.setDeleted(LocalDateTime.now());
        baseUserAddressService.updateEntity(entity);
        return true;
    }

    private AddressRecordEntity createAddressRecord(UserAddressIn userAddressIn, int userId, Integer addressId) {
        AddressRecordEntity recordEntity = modelMapper.map(userAddressIn, AddressRecordEntity.class);
        recordEntity.setUserId(userId);
        recordEntity.setCreated(LocalDateTime.now());
        recordEntity.setUserAddressId(addressId);
        recordEntity.setFullName();
        baseAddressRecordService.createEntity(recordEntity);
        baseAddressRecordService.flush();
        return recordEntity;
    }

    public ReportFilter filter(UserAddressFilter filter) {
        ReportOption reportOption = new ReportOption();
        if (filter instanceof UserAddressPageableFilter pageableFilter) {
            reportOption.setPageSize(pageableFilter.getSize());
            reportOption.setPageNumber(pageableFilter.getPage());
            reportOption.setSortOptions(pageableFilter.getSort());
        }
        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addEqualCondition("id", filter.getId());
        reportCondition.addEqualCondition("userId", filter.getUserId());

        return new ReportFilter(reportCondition, reportOption);
    }
}
