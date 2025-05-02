// src/test/java/com/project/marginal/tax/calculator/controller/TaxControllerEdgeCasesIntegrationTest.java

package com.project.marginal.tax.calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.marginal.tax.calculator.controller.TaxController;
import com.project.marginal.tax.calculator.dto.Metric;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.exception.GlobalExceptionHandler;
import com.project.marginal.tax.calculator.service.TaxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaxController.class)
@Import(GlobalExceptionHandler.class)
public class TaxControllerEdgeCasesIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TaxService taxService;

    // 1. Missing 'status' param → 400 Missing Parameter
    @Test
    public void history_missingStatusParam_returns400() throws Exception {
        mvc.perform(get("/api/v1/tax/history")
                .param("metric", "TOP_RATE")
                .param("startYear", "2000")
                .param("endYear", "2020"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.error", is("Missing Parameter")))
           .andExpect(jsonPath("$.message", containsString("status parameter is required")));
    }

    // 2. Non-integer startYear → 400 Type Mismatch
    @Test
    public void history_invalidStartYear_returns400TypeMismatch() throws Exception {
        mvc.perform(get("/api/v1/tax/history")
                .param("status", "S")
                .param("metric", "TOP_RATE")
                .param("startYear", "notAnInt")
                .param("endYear", "2020"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.error", is("Type Mismatch")))
           .andExpect(jsonPath("$.message", containsString("Parameter 'startYear' must be 'Integer'")));
    }

    // 3. startYear > endYear → service throws → 400 Bad Request
    @Test
    public void history_startYearAfterEndYear_returns400() throws Exception {
        when(taxService.getHistory(eq(FilingStatus.S), eq(Metric.TOP_RATE), eq(2021), eq(2000)))
            .thenThrow(new IllegalArgumentException("startYear must be ≤ endYear"));

        mvc.perform(get("/api/v1/tax/history")
                .param("status", "S")
                .param("metric", "TOP_RATE")
                .param("startYear", "2021")
                .param("endYear", "2000"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.error", is("Bad Request")))
           .andExpect(jsonPath("$.message", is("startYear must be ≤ endYear")));
    }

    // 4. Unsupported metric → Enum binding failure → 400 Type Mismatch
    @Test
    public void history_unsupportedMetric_returns400TypeMismatch() throws Exception {
        mvc.perform(get("/api/v1/tax/history")
                .param("status", "S")
                .param("metric", "MAX_RATE")   // not in Metric enum
                .param("startYear", "2000")
                .param("endYear", "2020"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.error", is("Type Mismatch")))
           .andExpect(jsonPath("$.message", containsString("Parameter 'metric' must be 'Metric'")));
    }

    // 5. Empty result set → 200 OK with an empty array
    @Test
    public void history_noData_returnsEmptyArray() throws Exception {
        when(taxService.getHistory(any(), any(), anyInt(), anyInt()))
            .thenReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/tax/history")
                .param("status", "S")
                .param("metric", "BRACKET_COUNT")
                .param("startYear", "2000")
                .param("endYear", "2005"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()", is(0)));
    }

    // 6. Empty list in POST request → 200 OK with an empty array
    @Test
    public void simulate_emptyList_returnsEmptyArray() throws Exception {
        when(taxService.simulateBulk(anyList()))
            .thenReturn(Collections.emptyList());

        mvc.perform(post("/api/v1/tax/simulate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.length()", is(0)));
    }
}
