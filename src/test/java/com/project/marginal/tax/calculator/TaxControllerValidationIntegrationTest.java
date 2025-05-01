package com.project.marginal.tax.calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.marginal.tax.calculator.controller.TaxController;
import com.project.marginal.tax.calculator.dto.TaxInput;
import com.project.marginal.tax.calculator.dto.TaxPaidResponse;
import com.project.marginal.tax.calculator.exception.GlobalExceptionHandler;
import com.project.marginal.tax.calculator.service.TaxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaxController.class)
@Import(GlobalExceptionHandler.class)
class TaxControllerValidationIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TaxService taxService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void breakdown_negativeIncome_returns400() throws Exception {
        // Simulate service rejecting negative income
        when(taxService.calculateTaxBreakdown(any(TaxInput.class)))
            .thenThrow(new IllegalArgumentException("Income must be non-negative"));

        TaxInput input = new TaxInput(2021, null, "-1000");
        String json = mapper.writeValueAsString(input);

        mvc.perform(post("/api/v1/tax/breakdown")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.error", is("Bad Request")))
           .andExpect(jsonPath("$.message", containsString("non-negative")))
           .andExpect(jsonPath("$.path", is("/api/v1/tax/breakdown")));
    }

    @Test
    public void breakdown_yearTooLow_returns400() throws Exception {
        // Simulate service rejecting invalid year
        when(taxService.calculateTaxBreakdown(any(TaxInput.class)))
            .thenThrow(new IllegalArgumentException("Invalid year: 1800"));

        TaxInput input = new TaxInput(1800, null, "50000");
        String json = mapper.writeValueAsString(input);

        mvc.perform(post("/api/v1/tax/breakdown")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.error", is("Bad Request")))
           .andExpect(jsonPath("$.message", is("Invalid year: 1800")));
    }



    @Test
    public void breakdown_decimalIncome_returns200() throws Exception {
        // Simulate successful processing of decimal income
        TaxPaidResponse dummy = new TaxPaidResponse(List.of(), 1234.56f, 0.10f);
        when(taxService.calculateTaxBreakdown(any(TaxInput.class)))
            .thenReturn(dummy);

        TaxInput input = new TaxInput(2021, null, "12345.67");
        String json = mapper.writeValueAsString(input);

        mvc.perform(post("/api/v1/tax/breakdown")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.totalTaxPaid", containsString("1,234")))
           .andExpect(jsonPath("$.avgRate", containsString("%")));
    }
}
