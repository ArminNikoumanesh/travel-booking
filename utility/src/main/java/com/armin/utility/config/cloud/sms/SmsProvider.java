package com.armin.utility.config.cloud.sms;

import com.armin.utility.config.cloud.BaseApplicationProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

@NoArgsConstructor
@Getter
@Setter
public class SmsProvider implements Diffable<SmsProvider> {
    private Magfa magfa;

    public SmsProvider(BaseApplicationProperties applicationProperties) {
        this.magfa = new Magfa(applicationProperties.getSmsProviders().getMagfa());
    }

    @Override
    public DiffResult<SmsProvider> diff(SmsProvider smsProvider) {
        return new DiffBuilder(this, smsProvider, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("magfa", this.magfa.diff(smsProvider.getMagfa()))
                .build();
    }
}
