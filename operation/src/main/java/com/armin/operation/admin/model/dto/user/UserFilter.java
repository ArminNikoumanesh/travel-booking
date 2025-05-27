package com.armin.operation.admin.model.dto.user;

import com.armin.utility.bl.NormalizeEngine;
import com.armin.utility.repository.orm.service.FilterBase;
import com.armin.database.user.statics.Gender;
import com.armin.database.user.statics.RoleType;
import com.armin.database.user.statics.UserMedium;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */
@Data
public class UserFilter implements FilterBase {
    private Integer id;
    @Size(max = 16)
    private String mobile;
    private Boolean mobileConfirmed;
    @Email
    @Size(max = 254)
    private String email;
    private Boolean emailConfirmed;
    private Boolean legal;
    @Size(max = 100)
    private String firstName;
    @Size(max = 100)
    private String lastName;
    @Size(max = 100)
    private String legalName;
    @Size(max = 11)
    private String nationalId;
    private Boolean twoFactorEnabled;
    private LocalDateTime createdMin;
    private LocalDateTime createdMax;
    private LocalDateTime lockExpireMin;
    private LocalDateTime lockExpireMax;
    private Boolean lock;
    private Boolean suspended;
    private Gender gender;
    private String fullName;
    private UserMedium medium;
    private Integer roleId;
    private Integer refererId;
    private Integer userLevelId;
    private List<Integer> userGroupIds;
    private Integer departmentId;
    private String queryParam;
    private Boolean pod;
    private Boolean haveReferer;
    private RoleType type;
    private boolean export;
    private Boolean completeInfo;

    public void setMobile(String mobile) {
        if (mobile.startsWith("0"))
            mobile = mobile.substring(1);
        this.mobile = mobile;
    }

    public void setQueryParam(String query) {
        if (query.startsWith("0"))
            query = query.substring(1);
        this.queryParam = NormalizeEngine.trimAndNormalizePersianString(query);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFilter that = (UserFilter) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(mobileConfirmed, that.mobileConfirmed) &&
                Objects.equals(email, that.email) &&
                Objects.equals(emailConfirmed, that.emailConfirmed) &&
                Objects.equals(legal, that.legal) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(legalName, that.legalName) &&
                Objects.equals(nationalId, that.nationalId) &&
                Objects.equals(twoFactorEnabled, that.twoFactorEnabled) &&
                Objects.equals(createdMin, that.createdMin) &&
                Objects.equals(createdMax, that.createdMax) &&
                Objects.equals(lockExpireMin, that.lockExpireMin) &&
                Objects.equals(lockExpireMax, that.lockExpireMax) &&
                Objects.equals(suspended, that.suspended) &&
                gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mobile, mobileConfirmed, email, emailConfirmed, legal, firstName, lastName, legalName, nationalId, twoFactorEnabled, createdMin, createdMax, lockExpireMin, lockExpireMax, suspended, gender);
    }
}


