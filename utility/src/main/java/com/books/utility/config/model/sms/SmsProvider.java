package com.books.utility.config.model.sms;

import com.books.utility.config.model.ApplicationProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author imax on 6/30/19
 */

@NoArgsConstructor
@Getter
@Setter
public class SmsProvider implements Diffable<SmsProvider> {
    private Magfa magfa;

    public SmsProvider(ApplicationProperties applicationProperties) {
        this.magfa = new Magfa(applicationProperties.getSmsProviders().getMagfa());
    }

    @Override
    public DiffResult<SmsProvider> diff(SmsProvider smsProvider) {
        return new DiffBuilder(this, smsProvider, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("magfa", this.magfa.diff(smsProvider.getMagfa()))
                .build();
    }
}
