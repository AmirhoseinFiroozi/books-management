package com.books.utility.config.model.identity;

import com.books.utility.config.model.ApplicationProperties;
import jakarta.validation.Valid;
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
public class IdentitySettings implements Diffable<IdentitySettings> {
    private RegistrationOptions registration;
    @Valid
    private PasswordOptions password;
    private LockoutOptions lockout;
    private JwtOptions jwt;

    public IdentitySettings(ApplicationProperties applicationProperties) {
        this.registration = new RegistrationOptions(applicationProperties.getIdentitySettings().getRegistration());
        this.password = new PasswordOptions(applicationProperties.getIdentitySettings().getPassword());
        this.lockout = new LockoutOptions(applicationProperties.getIdentitySettings().getLockout());
        this.jwt = new JwtOptions(applicationProperties.getIdentitySettings().getJwt());
    }

    @Override
    public DiffResult<IdentitySettings> diff(IdentitySettings identitySettings) {
        return new DiffBuilder(this, identitySettings, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("registration", this.registration.diff(identitySettings.getRegistration()))
                .append("password", this.password.diff(identitySettings.getPassword()))
                .append("lockout", this.lockout.diff(identitySettings.getLockout()))
                .append("jwt", this.jwt.diff(identitySettings.getJwt()))
                .build();
    }
}
