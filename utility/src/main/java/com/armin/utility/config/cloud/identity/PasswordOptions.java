package com.armin.utility.config.cloud.identity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
public class PasswordOptions implements Diffable<PasswordOptions> {
    @Min(value = 6)
    private int requiredLength = 6;
    @Max(value = 50)
    private int maxLength = 50;
    private int requiredUniqueChars = 0;
    private boolean requireNonAlphaNumeric = false;
    private boolean requireLowercase = false;
    private boolean requireUppercase = false;
    private boolean requireDigit = false;

    public PasswordOptions(PasswordOptions passwordOptions) {
        this.requiredLength = passwordOptions.getRequiredLength();
        this.maxLength = passwordOptions.getMaxLength();
        this.requiredUniqueChars = passwordOptions.getRequiredUniqueChars();
        this.requireNonAlphaNumeric = passwordOptions.isRequireNonAlphaNumeric();
        this.requireLowercase = passwordOptions.isRequireLowercase();
        this.requireUppercase = passwordOptions.isRequireUppercase();
        this.requireDigit = passwordOptions.isRequireDigit();
    }

    @Override
    public DiffResult<PasswordOptions> diff(PasswordOptions passwordOptions) {
        return new DiffBuilder(this, passwordOptions, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("requiredLength", this.requiredLength, passwordOptions.getRequiredLength())
                .append("maxLength", this.maxLength, passwordOptions.getMaxLength())
                .append("requiredUniqueChars", this.requiredUniqueChars, passwordOptions.getRequiredUniqueChars())
                .append("requireNonAlphaNumeric", this.requireNonAlphaNumeric, passwordOptions.isRequireNonAlphaNumeric())
                .append("requireLowercase", this.requireLowercase, passwordOptions.isRequireLowercase())
                .append("requireUppercase", this.requireUppercase, passwordOptions.isRequireUppercase())
                .append("requireDigit", this.requireDigit, passwordOptions.isRequireDigit())
                .build();
    }
}
