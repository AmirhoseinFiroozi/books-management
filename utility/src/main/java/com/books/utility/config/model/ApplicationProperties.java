package com.books.utility.config.model;

import com.books.utility.config.model.file.FileCrud;
import com.books.utility.config.model.identity.IdentitySettings;
import com.books.utility.config.model.oap.OpenApiConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationProperties {
    private IdentitySettings identitySettings;
    private FileCrud fileCrud;
    private OpenApiConfig openApiConfig;
}
