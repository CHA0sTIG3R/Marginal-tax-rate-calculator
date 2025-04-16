package com.project.marginal.tax.calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.marginal.tax.calculator.controller.TaxController;
import com.project.marginal.tax.calculator.dto.TaxInput;
import com.project.marginal.tax.calculator.entity.FilingStatus;
import com.project.marginal.tax.calculator.service.TaxService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = TaxController.class)
public class ControllerIntegrationTest {

    // Test cases for the controller can be added here
    // For example, you can use MockMvc to test the endpoints of your controller
    // and verify the responses.

    // Example:
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaxService taxService;

    private final ObjectMapper mapper = new ObjectMapper();

     @Test
     public void testGetYears() throws Exception {
         Mockito.when(taxService.listYears()).thenReturn(List.of(2020, 2021));
         mockMvc.perform(get("/api/v1/tax/years"))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$[0]").value(2020));
     }

    @Test
    public void testGetFilingStatus() throws Exception {
        Mockito.when(taxService.getFilingStatus()).thenReturn(Map.of("S", "Single", "MFJ", "Married Filing Jointly"));
        mockMvc.perform(get("/api/v1/tax/filing-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", hasKey("S")));
    }

    @Test
    public void testGetRate() throws Exception {
        mockMvc.perform(get("/api/v1/tax/rate")
                        .param("year", "2021"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    public void testPostTaxBreakdown() throws Exception {
        // Assuming taxService.calculateTaxBreakdown returns a valid TaxPaidResponse.
        // For simplicity, you can construct a simple response (or use a builder).
        // Here, we will just use Mockito to return an object.
        var dummyResponse = new com.project.marginal.tax.calculator.dto.TaxPaidResponse(List.of(), 5000f, 0.10f);
        Mockito.when(taxService.calculateTaxBreakdown(any(TaxInput.class))).thenReturn(dummyResponse);

        TaxInput input = new TaxInput(2021, FilingStatus.S, "50000");
        mockMvc.perform(post("/api/v1/tax/breakdown")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTaxPaid").exists());
    }
}
