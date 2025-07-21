package com.books.security.authorization.exception;

import com.books.utility.system.exception.ErrorResult;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Access Denied Handler Class,
 * Handling Requests after Authorization Failure
 *
 * @author Bijan Ghahremani
 * @version 1.0
 * @since 2016-09-22
 */

/**
 * Exceptions error code range: 2011-2020
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exp) throws IOException {
        List<ErrorResult> results = new ArrayList<>();
        ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.ACCESS_DENIED, "user", 2011), response);
        results.add(errorResult);

        SecurityContextHolder.clearContext();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(results));
    }
}
