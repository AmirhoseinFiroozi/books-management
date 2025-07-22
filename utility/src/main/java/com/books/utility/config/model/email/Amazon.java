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
public class Amazon extends Notification<EmailConfig> {
    public Amazon(Amazon amazon) {
        super.setConfigs(amazon.getConfigs());
        super.setPriority(amazon.getPriority());
    }
}
