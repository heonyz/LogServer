package com.kdh;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings(value = "unused")
@RestController
@RequestMapping(value = "/")
public class LoggingController {
    private final KdhLog log = new KdhLog(LoggingController.class);

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public LogResponse index() {
        return new LogResponse(0, "Log Server");
    }

    @PostMapping("/add_log")
    public LogResponse addLog(@RequestBody LogRecord logRecord) {
        final LogType logType = logRecord.getLogType();
        final String location = logRecord.getLocation();
        final String message = logRecord.getMessage();
        final String checkpoint = logRecord.getCheckpoint();
        final String varName = logRecord.getVarName();
        final String varValue = logRecord.getVarValue();

        if (logType == null) {
            return new LogResponse(1, "logType null");
        }

        if (logType == LogType.LOGTYPE_BEGIN_CHECKPOINT || logType == LogType.LOGTYPE_END_CHECKPOINT) {
            if (checkpoint == null) {
                return new LogResponse(1, "checkpoint null");
            }
        }

        if (logType == LogType.LOGTYPE_VAR || logType == LogType.LOGTYPE_INPUT || logType == LogType.LOGTYPE_OUTPUT) {
            if (varName == null) {
                return new LogResponse(1, "varName null");
            }

            if (varValue == null) {
                return new LogResponse(1, "varValue null");
            }
        }

        switch (logType) {
            case LOGTYPE_ERROR:
                log.error(location, message);
                break;
            case LOGTYPE_WARN:
                log.warn(location, message);
                break;
            case LOGTYPE_INFO:
                log.info(location, message);
                break;
            case LOGTYPE_DEBUG:
                log.debug(location, message);
                break;
            case LOGTYPE_TRACE:
                log.trace(location, message);
                break;
            case LOGTYPE_BEGIN_CHECKPOINT:
                log.beginCheckpoint(location, checkpoint);
                break;
            case LOGTYPE_END_CHECKPOINT:
                log.endCheckpoint(location, checkpoint);
                break;
            case LOGTYPE_VAR:
                log.var(location, varName, varValue);
                break;
            case LOGTYPE_INPUT:
                log.input(location, varName, varValue);
                break;
            case LOGTYPE_OUTPUT:
                log.output(location, varName, varValue);
                break;
        }

        return new LogResponse(0, "\n" +  "Log processing successful");
    }
}
