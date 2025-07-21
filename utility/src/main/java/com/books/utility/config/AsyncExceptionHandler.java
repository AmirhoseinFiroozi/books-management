package com.books.utility.config;

import com.books.utility.commons.statics.Env;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        StackTraceElement[] stackTrace = ex.getStackTrace();
        String className = null;
        String methodName = null;
        if (stackTrace.length > 0) {
            className = stackTrace[0].getClassName();
            methodName = stackTrace[0].getMethodName();
        }
        log.error(className + methodName + "host: {},stackTrace: {}", Env.HOST_NAME, stringWriter);
    }
}
