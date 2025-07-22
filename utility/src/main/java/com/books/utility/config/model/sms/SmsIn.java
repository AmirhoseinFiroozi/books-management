package com.books.utility.config.model.sms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author imax on 6/24/19
 */

@Getter
@Setter
@NoArgsConstructor
public class SmsIn {

    private String message;
    private String to;

    public SmsIn(SmsIn smsIn) {
        this.message = smsIn.getMessage();
        this.to = smsIn.getTo();
    }
}
