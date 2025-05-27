package com.armin.operation.security.admin.dto.realm;


import com.armin.utility.repository.orm.service.FilterBase;

import jakarta.validation.constraints.Size;

/**
 * @author imax on 5/18/19
 * @author Mohammad Yasin Sadeghi
 */
public class RealmFilter implements FilterBase {

    private Integer id;
    @Size(max = 50)
    private String name;
//    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Integer getType() {
//        return type;
//    }
//
//    public void setType(Integer type) {
//        this.type = type;
//    }
}
