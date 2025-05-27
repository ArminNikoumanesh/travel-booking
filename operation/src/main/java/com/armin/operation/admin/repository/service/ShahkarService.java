package com.armin.operation.admin.repository.service;

import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import com.armin.database.user.entity.UserEntity;
import com.armin.database.user.repository.service.BaseUserRoleRealmService;
import com.armin.database.user.repository.service.BaseUserService;
import com.armin.operation.admin.model.dto.account.UserShahkarIn;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.webservice.shahkar.client.ShahkarClient;
import com.armin.webservice.shahkar.model.ShahkarIn;
import com.armin.webservice.shahkar.model.ShahkarOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShahkarService {
    private final String[] INCLUDE = {"profile"};
    private final ShahkarClient shahkarClient;
    private final BaseUserService userService;
    private final BaseUserRoleRealmService baseUserRoleRealmService;

    @Autowired
    public ShahkarService(ShahkarClient shahkarClient, BaseUserService userService, BaseUserRoleRealmService baseUserRoleRealmService) {
        this.shahkarClient = shahkarClient;
        this.userService = userService;
        this.baseUserRoleRealmService = baseUserRoleRealmService;
    }

    public boolean verify(int id, UserShahkarIn model) throws SystemException {
        UserEntity userEntity = userService.getEntityById(id, INCLUDE);
        if (userEntity.isVerified()) {
            return true;
        }
        userEntity.setNationalId(model.getNationalId());
        userEntity.getProfile().setBirthDate(model.getBirthDate());

        ShahkarOut result = new ShahkarOut();
        try {
            ShahkarIn shahkarIn = new ShahkarIn();
            shahkarIn.setMobile(userEntity.getMobile());
            shahkarIn.setNationalId(userEntity.getNationalId());
//            result = shahkarClient.verify(shahkarIn);
            result.setMatched(true); 
        } catch (Exception e) {
            throw new SystemException(SystemError.SERVICE_UNAVAILABLE, "service unavailable", 3081);
        }

        //imaginary 3 seconds loading
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        if (result.isMatched()) {
            userEntity.setVerified(true);

            SecurityUserRoleRealmEntity userRoleRealmEntity = new SecurityUserRoleRealmEntity(userEntity);
            baseUserRoleRealmService.createUserMemberRoleRealm(userRoleRealmEntity);
        }
        userService.updateEntity(userEntity);
        return result.isMatched();
    }
}
