package com.armin.operation.address.ident.model;

import com.armin.utility.repository.orm.service.FilterBase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @auther : Armin.Nik
 * @date : 20.11.22
 */
@Getter
@Setter
public class UserAddressFilter implements FilterBase {
    private Integer id;
    @Setter(AccessLevel.PRIVATE)
    private Integer userId;

    public void putUserId(Integer userId) {
        this.userId = userId;
    }
}
