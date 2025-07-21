package com.books.utility.system.exception;

import lombok.Getter;

import java.util.List;

/**
 * System Exception Class,
 * A Customized {@link RuntimeException} for Whole System with a proper {@link SystemError} Type and Error Code
 *
 * @author Bijan Ghahremani
 * @version 1.0
 * @since 2016-09-22
 */
@Getter
public class SystemException extends Exception {
    private final SystemError error;
    private final Integer errorCode;
    private final Object argument;
    private final List<ErrorResult> errorResults;

    public SystemException(SystemError error, Object argument, Integer errorCode) {
        super(generateErrorMessage(error, argument, errorCode, null));
        this.error = error;
        this.argument = argument;
        this.errorCode = errorCode;
        this.errorResults = null;
    }

    public SystemException(SystemError error, Object argument, Integer errorCode, List<ErrorResult> errorResults) {
        super(generateErrorMessage(error, argument, errorCode, errorResults));
        this.error = error;
        this.argument = argument;
        this.errorCode = errorCode;
        this.errorResults = errorResults;
    }

    private static String generateErrorMessage(SystemError error, Object argument, Integer errorCode, List<ErrorResult> errorResults) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("[");
        if (error != null || errorCode != null || argument != null) {
            errorMessage.append("\n")
                    .append("{error: ")
                    .append(error)
                    .append(",\n")
                    .append("status: ")
                    .append(error != null ? error.getValue() : null)
                    .append(",\n")
                    .append("errorCode: ")
                    .append(errorCode)
                    .append(",\n")
                    .append("argument: ")
                    .append(argument)
                    .append("}");
        }
        if (errorResults != null && !errorResults.isEmpty()) {
            for (ErrorResult errorResult : errorResults) {
                if (errorMessage.length() != 1) {
                    errorMessage.append(",");
                }
                errorMessage.append("\n{error: ")
                        .append(errorResult.getCode())
                        .append(",\n")
                        .append("status: ")
                        .append(errorResult.getStatus())
                        .append(",\n")
                        .append("errorCode: ")
                        .append(errorResult.getErrorCode())
                        .append(",\n")
                        .append("argument: ")
                        .append(errorResult.getData())
                        .append("}");
            }
        }
        errorMessage.append("\n]");
        return errorMessage.toString();
    }
}
