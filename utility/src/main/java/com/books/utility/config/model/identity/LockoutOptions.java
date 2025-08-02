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
public class LockoutOptions implements Diffable<LockoutOptions> {
    private int maxFailedAccessAttempts = 5;

    public LockoutOptions(LockoutOptions lockoutOptions) {
        this.maxFailedAccessAttempts = lockoutOptions.getMaxFailedAccessAttempts();
    }

    @Override
    public DiffResult<LockoutOptions> diff(LockoutOptions lockoutOptions) {
        return new DiffBuilder(this, lockoutOptions, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("maxFailedAccessAttempts", this.maxFailedAccessAttempts, lockoutOptions.maxFailedAccessAttempts)
                .build();
    }
}
