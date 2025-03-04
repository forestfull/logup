package com.forestfull.log.up.util;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SourceInfo {
    private String className;
    private String methodName;
    private String fileName;
    private int lineNumber;
}
