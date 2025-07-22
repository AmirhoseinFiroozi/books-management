package com.books.utility.config.model.email;

import com.books.utility.config.model.ApplicationProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author imax on 8/29/19
 */
@Getter
@Setter
@NoArgsConstructor
public class EmailProvider implements Diffable<EmailProvider> {

    private Amazon amazon;
    private Gmail gmail;

    public EmailProvider(ApplicationProperties applicationProperties) {
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
