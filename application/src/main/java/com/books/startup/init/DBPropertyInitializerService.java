package com.books.startup.init;

import com.books.startup.global.service.BaseGlobalPropertyService;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @auther : Armin.Nik
 * @project : vrp-helm
 * @date : 09.10.22
 */
@Service("pr-init-db")
public class DBPropertyInitializerService implements IPropertyInitializer {
    private final BaseGlobalPropertyService baseGlobalPropertyService;
    @Value("${spring.application.name}")
    private String name;
    @Value("${spring.cloud.config.profile}")
    private String profile;

    @Autowired
    public DBPropertyInitializerService(BaseGlobalPropertyService baseGlobalPropertyService) {
        this.baseGlobalPropertyService = baseGlobalPropertyService;
    }

    @Override
    public Object initialize() throws SystemException {
        Object properties;
        try {
            properties = baseGlobalPropertyService.getById(name, profile).getProperty();
        } catch (Exception e) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "Couldn't Get Properties From DataBase", 100001);
        }
        return properties;
    }

}
