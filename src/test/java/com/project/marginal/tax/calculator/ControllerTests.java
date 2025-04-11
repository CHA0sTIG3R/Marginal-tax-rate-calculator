package com.project.marginal.tax.calculator;

import com.project.marginal.tax.calculator.controller.MarginalTaxController;
import com.project.marginal.tax.calculator.service.MarginalTaxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = MarginalTaxController.class)
public class ControllerTests {

    // Test cases for the controller can be added here
    // For example, you can use MockMvc to test the endpoints of your controller
    // and verify the responses.

    // Example:
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MarginalTaxService marginalTaxService;

//     Test the hello endpoint
     @Test
     public void testHelloEndpoint() throws Exception {
         // expect the response to be "Sending Message" and content type to be text/plain;charset=UTF-8
         mockMvc.perform(get("/"))
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                 .andExpect(content().string("Sending Message"));
        }

    // Test the getYears endpoint
    @Test
    public void testGetYearsEndpoint() throws Exception {
        mockMvc.perform(get("/get-years"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    // Test the getFilingStatus endpoint
    @Test
    public void testGetFilingStatusEndpoint() throws Exception {
        mockMvc.perform(get("/get-filing-status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    // Test the getTaxRateByYear endpoint
    @Test
    public void testGetTaxRateByYearEndpoint() throws Exception {
        mockMvc.perform(get("/get-tax-rate/2021"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    // Test the getTaxRateByYearAndStatus endpoint
    @Test
    public void testGetTaxRateByYearAndStatusEndpoint() throws Exception {
        mockMvc.perform(get("/get-tax-rate/2021/S"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }
}
