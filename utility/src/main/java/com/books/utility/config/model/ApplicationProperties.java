package com.books.utility.config.model;

import com.books.utility.config.model.email.EmailProvider;
import com.books.utility.config.model.file.FileCrud;
import com.books.utility.config.model.identity.IdentitySettings;
import com.books.utility.config.model.minio.MinioConfig;
import com.books.utility.config.model.oap.OpenApiConfig;
import com.books.utility.config.model.sms.SmsProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
public class ApplicationProperties {
    private boolean initDB;
    private IdentitySettings identitySettings;
    private SmsProvider smsProviders;
    private EmailProvider emailProvider;
    private Boolean smsOtpSandbox;
    private FileCrud fileCrud;
    private List<String> widgetAreas;
    private OpenApiConfig openApiConfig;
    private MinioConfig minioConfig;
}
