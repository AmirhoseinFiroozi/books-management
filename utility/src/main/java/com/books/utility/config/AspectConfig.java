package com.books.utility.config;

import com.books.utility.commons.dto.ClientIpInfo;
import com.books.utility.commons.interfaces.IValidation;
import com.books.utility.commons.statics.Env;
import com.books.utility.system.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
@Aspect
@Slf4j
public class AspectConfig {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestControllerAdvice * )")
    public void controllerAdvice() {
        /*
        This method contains controller Advice
         */
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController * || @org.springframework.stereotype.Controller *)")
    public void controller() {
        /*
        This method contains all controller class methods
         */
    }

    @Pointcut("execution(public * *(..))")
    public void allMethod() {
        /*
        This method contains all methods
         */
    }

    @Before(value = "controller()")
    public void validate(JoinPoint joinPoint) throws SystemException {
        Object[] signatureArgs = joinPoint.getArgs();
        for (Object signatureArg : signatureArgs) {
            if (signatureArg instanceof IValidation) {
                ((IValidation) signatureArg).validate();
            }
        }
    }


    @Before(value = "controller() && args(..,request)", argNames = "request")
    public void ipLog(HttpServletRequest request) {
        log.info("Request remote address : {}", ClientIpInfo.getMainIP(request));
    }


    @AfterThrowing(pointcut = "(@annotation(com.books.utility.commons.annotations.LogAnnotation) || controller() || controllerAdvice()) && allMethod()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        if (!(exception instanceof SystemException) &&
                !(exception instanceof ConstraintViolationException) &&
                !(exception instanceof AuthenticationException) &&
                !(exception instanceof AccessDeniedException)) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            exception.printStackTrace(printWriter);
            log.error(joinPoint.getSignature().getDeclaringTypeName() + joinPoint.getSignature().getName() +
                    "host: {},stackTrace: {}", Env.HOST_NAME, stringWriter);
        }
    }
}

