package com.forestfull.log.up.util;

import com.forestfull.log.up.Level;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SourceInfo {
    private String className;
    private String methodName;
    private String fileName;
    private int lineNumber;

    public String getSourceInfo() {
        return Level.COLOR.WHITE + " >> " + className + '.' + methodName + '(' + fileName + ':' + lineNumber + ')' + Level.COLOR.RESET;
    }
}
