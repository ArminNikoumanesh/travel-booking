package com.armin.operation.address.admin.repository.service;

import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.database.address.address.entity.UserAddressEntity;
import com.armin.database.address.address.repository.service.BaseUserAddressService;
import com.armin.operation.address.admin.model.UserAddressFilter;
import com.armin.operation.address.admin.model.UserAddressOut;
import com.armin.operation.address.admin.model.UserAddressPageableFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther : Armin.Nik
 * @date : 20.11.22
 */
@Service
public class AdminUserAddressService {
    private final BaseUserAddressService baseUserAddressService;

    @Autowired
    public AdminUserAddressService(BaseUserAddressService baseUserAddressService) {
        this.baseUserAddressService = baseUserAddressService;
    }

    public int countEntity(UserAddressFilter filter) {
        return baseUserAddressService.countEntity(filter(filter));
    }

    public List<UserAddressOut> getAll(UserAddressPageableFilter pageableFilter, String[] includes) {
        List<UserAddressEntity> addressEntities = baseUserAddressService.getAllEntities(filter(pageableFilter), includes);
        return addressEntities.stream()
                .map(UserAddressOut::new)
                .collect(Collectors.toList());
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
