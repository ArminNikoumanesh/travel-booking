package com.armin.operation.security.admin.dto.session;

import com.armin.utility.repository.orm.service.FilterBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


/**
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserSessionFilter implements FilterBase {
    private Integer id;
    private Integer userId;
    private String uniqueId;
    private String os;
    private String agent;
    private String firebaseToken;
    private LocalDateTime createdMin;
    private LocalDateTime createdMax;
    private String ip;

}
