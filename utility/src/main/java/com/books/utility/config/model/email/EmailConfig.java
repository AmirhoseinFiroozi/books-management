package com.books.utility.config.model.email;

import com.books.utility.config.model.statics.EmailProtocolType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author imax on 8/29/19
 */
@NoArgsConstructor
@Getter
@Setter
public class EmailConfig {

    private String userName;
    private String password;
    private String host;
    private String port;
    private EmailProtocolType protocol;
    private boolean auth;
    private boolean starttls;
    private int priority;

    public EmailConfig(EmailConfig emailConfig) {
        this.userName = emailConfig.getUserName();
        this.password = emailConfig.getPassword();
        this.host = emailConfig.getHost();
        this.port = emailConfig.getPort();
        this.protocol = emailConfig.getProtocol();
        this.auth = emailConfig.isAuth();
        this.starttls = emailConfig.isStarttls();
        this.priority = emailConfig.getPriority();
    }
}
