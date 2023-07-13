package com.kdh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoggingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //  GET "/" 엔드포인트 응답 확인
    @Test
    public void testIndexEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.Code").value(0))
                .andExpect(jsonPath("$.Message").value("Log Server"));
    }

    // POST "/add_log" - logType null 체크
    @Test
    public void testAddLog_MissingLogType() throws Exception {
        // logType 필드가 누락된 JSON
        String payload = "{ \"Location\": \"TestLocation\", \"Message\": \"Test Message\" }";
        mockMvc.perform(post("/add_log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Code").value(1))
                .andExpect(jsonPath("$.Message").value(containsString("logType null")));
    }

    // POST "/add_log" - CHECKPOINT 타입에서 checkpoint null 체크
    @Test
    public void testAddLog_MissingCheckpointForBeginCheckpoint() throws Exception {
        String payload = "{ \"LogType\": \"LOGTYPE_BEGIN_CHECKPOINT\", \"Location\": \"TestLocation\", \"Message\": \"Test Message\" }";
        mockMvc.perform(post("/add_log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Code").value(1))
                .andExpect(jsonPath("$.Message").value(containsString("checkpoint null")));
    }

    // POST "/add_log" - VAR 타입에서 varName null 체크
    @Test
    public void testAddLog_MissingVarNameForVar() throws Exception {
        String payload = "{ \"LogType\": \"LOGTYPE_VAR\", \"Location\": \"TestLocation\" }";
        mockMvc.perform(post("/add_log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Code").value(1))
                .andExpect(jsonPath("$.Message").value(containsString("varName null")));
    }

    // POST "/add_log" - VAR 타입에서 varValue null 체크
    @Test
    public void testAddLog_MissingVarValueForVar() throws Exception {
        String payload = "{ \"LogType\": \"LOGTYPE_VAR\", \"Location\": \"TestLocation\", \"VarName\": \"testVar\" }";
        mockMvc.perform(post("/add_log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Code").value(1))
                .andExpect(jsonPath("$.Message").value(containsString("varValue null")));
    }

    // POST "/add_log" - 정상 요청 처리 (LOGTYPE_ERROR, LOGTYPE_INFO 등)
    @Test
    public void testAddLog_ValidRequest_Error() throws Exception {
        String payload = "{ \"LogType\": \"LOGTYPE_ERROR\", \"Location\": \"TestLocation\", \"Message\": \"Error occurred\" }";
        mockMvc.perform(post("/add_log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Code").value(0))
                .andExpect(jsonPath("$.Message").value(containsString("Log processing successful")));
    }

    @Test
    public void testAddLog_ValidRequest_Info() throws Exception {
        String payload = "{ \"LogType\": \"LOGTYPE_INFO\", \"Location\": \"TestLocation\", \"Message\": \"Informational message\" }";
        mockMvc.perform(post("/add_log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Code").value(0))
                .andExpect(jsonPath("$.Message").value(containsString("Log processing successful")));
    }

    // POST "/add_log" - CHECKPOINT 정상 처리 (begin과 end를 차례로 호출)
    @Test
    public void testAddLog_ValidRequest_BeginAndEndCheckpoint() throws Exception {
        // Begin checkpoint 호출
        String beginPayload = "{ \"LogType\": \"LOGTYPE_BEGIN_CHECKPOINT\", \"Location\": \"TestLocation\", \"Checkpoint\": \"cp1\" }";
        mockMvc.perform(post("/add_log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beginPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Code").value(0))
                .andExpect(jsonPath("$.Message").value(containsString("Log processing successful")));

        // 약간의 지연을 두고 end checkpoint 호출
        Thread.sleep(50);

        String endPayload = "{ \"LogType\": \"LOGTYPE_END_CHECKPOINT\", \"Location\": \"TestLocation\", \"Checkpoint\": \"cp1\" }";
        mockMvc.perform(post("/add_log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(endPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Code").value(0))
                .andExpect(jsonPath("$.Message").value(containsString("Log processing successful")));
    }

}