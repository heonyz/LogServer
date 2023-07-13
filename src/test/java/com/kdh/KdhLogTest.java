package com.kdh;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KdhLogTest {

    private KdhLog kdhLog;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    public void setUp() {
        kdhLog = new KdhLog(KdhLogTest.class);

        Logger logger = (Logger) LoggerFactory.getLogger(KdhLogTest.class);
        logger.setLevel(Level.TRACE);  // TRACE 레벨 활성화
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    public void testBeginAndEndCheckpoint() throws InterruptedException {
        String location = "TestLocation";
        String checkpoint = "cpTest";

        kdhLog.beginCheckpoint(location, checkpoint);
        Thread.sleep(50);
        kdhLog.endCheckpoint(location, checkpoint);

        List<ILoggingEvent> logsList = listAppender.list;
        boolean beginFound = false;
        boolean endFound = false;
        long timing = -1;

        for (ILoggingEvent event : logsList) {
            String message = event.getFormattedMessage();
            if (message.contains("Begin checkpoint")) {
                beginFound = true;
            }
            if (message.contains("End checkpoint") && message.contains("timing statistics:")) {
                endFound = true;
                int index = message.indexOf("timing statistics:");
                if (index != -1) {
                    String sub = message.substring(index);
                    String[] parts = sub.split(":");
                    if (parts.length >= 2) {
                        String numStr = parts[1].replaceAll("[^0-9]", "");
                        try {
                            timing = Long.parseLong(numStr);
                        } catch (NumberFormatException e) {
                            timing = -1;
                        }
                    }
                }
            }
        }
        assertTrue(beginFound, "Begin checkpoint 로그가 기록되지 않음");
        assertTrue(endFound, "End checkpoint 로그가 기록되지 않음");
        assertTrue(timing >= 50, "측정된 시간(" + timing + "ms)이 50ms 이상이어야 함");
    }

    @Test
    public void testEndCheckpointWithoutBegin() {
        String location = "TestLocation";
        String checkpoint = "nonexistent";
        kdhLog.endCheckpoint(location, checkpoint);

        List<ILoggingEvent> logsList = listAppender.list;
        boolean endFound = false;
        for (ILoggingEvent event : logsList) {
            String message = event.getFormattedMessage();
            if (message.contains("End checkpoint: " + checkpoint)) {
                endFound = true;
                break;
            }
        }
        assertTrue(endFound, "등록되지 않은 체크포인트. End checkpoint 로그가 기록되지 않음");
    }
}