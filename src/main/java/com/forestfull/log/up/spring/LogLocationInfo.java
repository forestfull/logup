package com.forestfull.log.up.spring;

import lombok.Getter;

@Getter
public class LogLocationInfo {
    private final String className;
    private final String methodName;
    private final String fileName;
    private final int lineNumber;

    public LogLocationInfo(LogUpStackTrace e) {
        final StackTraceElement traceElement = e.getStackTrace()[1];

        this.className = traceElement.getClassName();
        this.methodName = traceElement.getMethodName();
        this.fileName = traceElement.getFileName();
        this.lineNumber = traceElement.getLineNumber();
    }
}
