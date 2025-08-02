package com.books.utility.config.model.identity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationOptions implements Diffable<RegistrationOptions> {
    private boolean registerEnabled = true;


    public RegistrationOptions(RegistrationOptions registrationOptions) {
        this.registerEnabled = registrationOptions.isRegisterEnabled();
    }


    @Override
    public DiffResult<RegistrationOptions> diff(RegistrationOptions registrationOptions) {
        return new DiffBuilder(this, registrationOptions, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("registerEnabled", this.registerEnabled, registrationOptions.isRegisterEnabled())
                .build();
    }
}
