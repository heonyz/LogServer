package com.kdh;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class KdhLogGeneralMethodTest {

    private KdhLog kdhLog;
    private ListAppender<ILoggingEvent> listAppender;
    private Logger logger;

    @BeforeEach
    public void setUp() {
        kdhLog = new KdhLog(KdhLogGeneralMethodTest.class);
        logger = (Logger) LoggerFactory.getLogger(KdhLogGeneralMethodTest.class);
        logger.setLevel(Level.TRACE);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    private ILoggingEvent getLatestLog() {
        int size = listAppender.list.size();
        return size > 0 ? listAppender.list.get(size - 1) : null;
    }

    @Test
    public void testVarMethod() {
        String location = "TestLocation";
        String name = "varName";
        String value = "varValue";
        kdhLog.var(location, name, value);

        ILoggingEvent event = getLatestLog();
        assertNotNull(event);
        String expected = String.format("%s|var->%s=%s", location, name, value);
        assertEquals(expected, event.getFormattedMessage());
        assertEquals(Level.TRACE, event.getLevel());
    }

    @Test
    public void testInputMethod() {
        String location = "TestLocation";
        String name = "inputName";
        String value = "inputValue";
        kdhLog.input(location, name, value);

        ILoggingEvent event = getLatestLog();
        assertNotNull(event);
        String expected = String.format("%s|input->%s=%s", location, name, value);
        assertEquals(expected, event.getFormattedMessage());
        assertEquals(Level.TRACE, event.getLevel());
    }

    @Test
    public void testOutputMethod() {
        String location = "TestLocation";
        String name = "outputName";
        String value = "outputValue";
        kdhLog.output(location, name, value);

        ILoggingEvent event = getLatestLog();
        assertNotNull(event);
        String expected = String.format("%s|output->%s=%s", location, name, value);
        assertEquals(expected, event.getFormattedMessage());
        assertEquals(Level.TRACE, event.getLevel());
    }

    @Test
    public void testErrorMethodWithString() {
        String location = "TestLocation";
        String message = "Error occurred";
        kdhLog.error(location, message);

        ILoggingEvent event = getLatestLog();
        assertNotNull(event);
        String expected = location + "|" + message;
        assertEquals(expected, event.getFormattedMessage());
        assertEquals(Level.ERROR, event.getLevel());
    }

    @Test
    public void testWarnMethod() {
        String location = "TestLocation";
        String message = "Warning message";
        kdhLog.warn(location, message);

        ILoggingEvent event = getLatestLog();
        assertNotNull(event);
        String expected = location + "|" + message;
        assertEquals(expected, event.getFormattedMessage());
        assertEquals(Level.WARN, event.getLevel());
    }

    @Test
    public void testInfoMethod() {
        String location = "TestLocation";
        String message = "Info message";
        kdhLog.info(location, message);

        ILoggingEvent event = getLatestLog();
        assertNotNull(event);
        String expected = location + "|" + message;
        assertEquals(expected, event.getFormattedMessage());
        assertEquals(Level.INFO, event.getLevel());
    }

    @Test
    public void testDebugMethod() {
        String location = "TestLocation";
        String message = "Debug message";
        kdhLog.debug(location, message);

        ILoggingEvent event = getLatestLog();
        assertNotNull(event);
        String expected = location + "|" + message;
        assertEquals(expected, event.getFormattedMessage());
        assertEquals(Level.DEBUG, event.getLevel());
    }

    @Test
    public void testTraceMethod() {
        String location = "TestLocation";
        String message = "Trace message";
        kdhLog.trace(location, message);

        ILoggingEvent event = getLatestLog();
        assertNotNull(event);
        String expected = location + "|" + message;
        assertEquals(expected, event.getFormattedMessage());
        assertEquals(Level.TRACE, event.getLevel());
    }

    @Test
    public void testErrorMethodWithException() {
        String location = "TestLocation";
        Exception ex = new RuntimeException("Test exception");
        kdhLog.error(location, ex);

        ILoggingEvent event = getLatestLog();
        assertNotNull(event);
        String logMessage = event.getFormattedMessage();
        assertTrue(logMessage.startsWith(location + "|"));
        assertTrue(logMessage.contains("Test exception"));
        assertEquals(Level.ERROR, event.getLevel());
    }
}