package com.books.utility.config.model.sms;

import com.books.utility.commons.statics.SmsType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author imax on 6/24/19
 */
@Getter
@Setter
@NoArgsConstructor
public class SmsConfig {

    private String username;
    private String password;
    private String sourceNumber;
    private String domain;
    private SmsType smsType;
    private int priority;

    public SmsConfig(SmsConfig smsConfig) {
        this.username = smsConfig.getUsername();
        this.password = smsConfig.getPassword();
        this.sourceNumber = smsConfig.getSourceNumber();
        this.domain = smsConfig.getDomain();
        this.smsType = smsConfig.getSmsType();
        this.priority = smsConfig.getPriority();
    }
}
