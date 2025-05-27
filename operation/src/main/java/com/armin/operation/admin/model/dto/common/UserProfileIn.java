package com.armin.operation.admin.model.dto.common;


import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemException;
import com.armin.operation.admin.model.dto.profile.ProfileIn;
import com.armin.operation.admin.model.dto.user.UserIn;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class UserProfileIn implements IValidation {
    @NotNull
    @Valid
    private UserIn user;
    @NotNull
    @Valid
    private ProfileIn profile;

    public UserIn getUser() {
        return user;
    }

    public void setUser(UserIn user) {
        this.user = user;
    }

    public ProfileIn getProfile() {
        return profile;
    }

    public void setProfile(ProfileIn profile) {
        this.profile = profile;
    }

    @Override
    public void validate() throws SystemException {
        if (user != null) {
            this.user.validate();
        }
        if (profile != null){
            this.profile.validate();
        }
    }
}
