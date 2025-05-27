package com.armin.operation.address.ident.model;

import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import static com.armin.utility.bl.NormalizeEngine.normalizePersianString;

/**
 * @auther : Armin.Nik
 * @date : 20.11.22
 */
@Getter
@Setter
public class UserAddressIn implements IValidation {
    @NotBlank
    private String address;
    @NotBlank
    private String zipCode;
    @NotNull
    private Double lat;
    @NotNull
    private Double lng;
    @NotNull
    private String name;
    @NotNull
    private String province;
    @NotNull
    private String city;

    @Override
    public void validate() throws SystemException {
        this.name = normalizePersianString(this.name);
        this.province = normalizePersianString(this.province);
        this.city = normalizePersianString(this.city);
        this.address = normalizePersianString(this.address);
    }

}
