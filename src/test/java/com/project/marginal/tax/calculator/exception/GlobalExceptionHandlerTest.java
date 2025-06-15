package com.project.marginal.tax.calculator.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest servletRequest;

    @BeforeEach
    public void setUp() {
        handler = new GlobalExceptionHandler();
        servletRequest = new MockHttpServletRequest();
        // set a fake URI so ErrorResponse.path is populated
        ((MockHttpServletRequest) servletRequest).setRequestURI("/api/test");
    }

    @Test
    public void whenTypeMismatch_thenReturnsBadRequest() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException("year", Integer.class, "year", null, null);

        var resp = handler.handleTypeMismatch(ex, servletRequest);
        var body = resp.getBody();

        assertEquals(BAD_REQUEST, resp.getStatusCode());
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("Type Mismatch", body.getError());
        assertTrue(body.getMessage().contains("Parameter 'year' must be 'Integer'"));
        assertEquals("/api/test", body.getPath());
    }

    @Test
    public void whenIllegalArgument_thenReturnsBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("bad input");
        var resp = handler.handleIllegalArgument(ex, servletRequest);
        var body = resp.getBody();

        assertEquals(BAD_REQUEST, resp.getStatusCode());
        assertNotNull(body);
        assertEquals("Bad Request", body.getError());
        assertEquals("bad input", body.getMessage());
        assertEquals("/api/test", body.getPath());
    }

    @Test
    public void whenNumberFormatException_thenReturnsBadRequest() {
        NumberFormatException ex = new NumberFormatException("For input string: \"foo\"");
        var resp = handler.handleNumberFormat(ex, servletRequest);
        var body = resp.getBody();

        assertEquals(BAD_REQUEST, resp.getStatusCode());
        assertNotNull(body);
        assertEquals("Invalid Number", body.getError());
        assertEquals("For input string: \"foo\"", body.getMessage());
        assertEquals("/api/test", body.getPath());
    }

    @Test
    public void whenUnhandledException_thenReturnsInternalServerError() {
        Exception ex = new RuntimeException("oops");
        var resp = handler.handleAll(ex, servletRequest);
        var body = resp.getBody();

        assertEquals(INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertNotNull(body);
        assertEquals("Internal Server Error", body.getError());
        assertEquals("oops", body.getMessage());
        assertEquals("/api/test", body.getPath());
    }
}