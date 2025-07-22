package com.books.utility.config.model.sms;

import com.books.utility.config.model.Notificaiton.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author imax on 6/30/19
 * @author imax
 */

@Getter
@Setter
@NoArgsConstructor
public class Magfa extends Notification<SmsConfig> {
    public Magfa(Magfa magfa) {
        super.setConfigs(magfa.getConfigs());
        super.setPriority(magfa.getPriority());
    }
}
