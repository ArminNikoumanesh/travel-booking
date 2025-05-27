package com.armin.operation.security.ident.model.dto;

import com.armin.utility.repository.orm.service.FilterBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserSessionFilter implements FilterBase {
    private Integer userId;
    @Size(max = 100)
    private String uniqueId;
    @Size(max = 50)
    private String os;
    @Size(max = 100)
    private String agent;
    private String firebaseToken;
    private LocalDateTime createdMin;
    private LocalDateTime createdMax;
    @Size(max = 39)
    private String ip;

}
