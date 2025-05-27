package com.armin.utility.config.cloud.email;

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
@Getter
@Setter
@NoArgsConstructor
public class EmailProvider implements Diffable<EmailProvider> {

    private Amazon amazon;
    private Gmail gmail;

    public EmailProvider(BaseApplicationProperties applicationProperties) {
        this.amazon = new Amazon(applicationProperties.getEmailProvider().getAmazon());
        this.gmail = new Gmail(applicationProperties.getEmailProvider().getGmail());
    }

    @Override
    public DiffResult<EmailProvider> diff(EmailProvider emailProvider) {
        return new DiffBuilder(this, emailProvider, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("amazon", this.amazon, emailProvider.getAmazon())
                .append("gmail", this.gmail, emailProvider.getGmail())
                .build();
    }
}
