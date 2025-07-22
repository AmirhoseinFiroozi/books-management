package com.books.utility.config.model.email;

import com.books.utility.config.model.Notificaiton.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author imax on 8/29/19
 * @author imax
 */
@Getter
@Setter
@NoArgsConstructor
public class Gmail extends Notification<EmailConfig> {
    public Gmail(Gmail gmail) {
        super.setConfigs(gmail.getConfigs());
        super.setPriority(gmail.getPriority());
    }
}
