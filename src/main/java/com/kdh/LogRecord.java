package com.kdh;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@SuppressWarnings(value = "unused")
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class LogRecord {
    private String Location;
    private String Message;
    private LogType LogType;
    private String Checkpoint;
    private String VarName;
    private String VarValue;

    public String getLocation() {
        return Location;
    }

    public String getMessage() {
        return Message;
    }

    public LogType getLogType() {
        return LogType;
    }

    public String getCheckpoint() {
        return Checkpoint;
    }

    public String getVarName() {
        return VarName;
    }

    public String getVarValue() {
        return VarValue;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setLogType(LogType logType) {
        LogType = logType;
    }

    public void setCheckpoint(String checkpoint) {
        Checkpoint = checkpoint;
    }

    public void setVarName(String varName) {
        VarName = varName;
    }

    public void setVarValue(String varValue) {
        VarValue = varValue;
    }
}
