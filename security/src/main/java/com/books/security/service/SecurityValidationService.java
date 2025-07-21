package com.books.security.service;

import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.springframework.stereotype.Service;

/**
 * The Validation Service Class,
 * Containing Methods about String Patterns and Input Validation
 *
 * @author Bijan Ghahremani
 * @version 1.0
 * @since 2016-09-22
 */

/**
 * Exceptions error code range: 2101-2150
 */
@Service
public class SecurityValidationService {

    public String validateAuthHeaderToken(String value) throws SystemException {
        // 7 characters for "Bearer " and 2 characters for two dots
        if (value == null || value.length() < 10)
            throw new SystemException(SystemError.INVALID_TOKEN_HEADER, "value", 2101);
        return value.replaceAll("Bearer ", "");
    }

    public String validatePublicHeaderToken(String value) {
        // 7 characters for "Bearer " and 2 characters for two dots
        if (value == null || value.length() < 10)
            return null;
        return value.replaceAll("Bearer ", "");
    }
}
