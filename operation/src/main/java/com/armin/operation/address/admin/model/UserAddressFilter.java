package com.armin.operation.address.admin.model;

import com.armin.utility.repository.orm.service.FilterBase;
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
    private Integer userId;
}
