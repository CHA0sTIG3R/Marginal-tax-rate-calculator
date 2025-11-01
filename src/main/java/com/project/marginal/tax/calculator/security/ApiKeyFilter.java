package com.project.marginal.tax.calculator.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.ingest.api-key:}")
    private String expectedApiKey;
    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        // TODO: If a servlet context path is introduced, consider using AntPathMatcher
        //  or startsWith to ensure the filter still protects the upload endpoint.
        if ("/api/v1/tax/upload".equals(request.getRequestURI())) {
            if (expectedApiKey == null || expectedApiKey.isBlank()) {
                log.error("API key not configured; rejecting upload uri={} clientIp={}",
                        request.getRequestURI(), request.getRemoteAddr());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String apiKey = request.getHeader("X-API-KEY");
            if (apiKey == null || !apiKey.equals(expectedApiKey)) {
                log.warn("Invalid or missing API key; rejecting upload uri={} clientIp={}",
                        request.getRequestURI(), request.getRemoteAddr());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            log.info("Valid API key provided uri={} clientIp={}", request.getRequestURI(), request.getRemoteAddr());
        }
        filterChain.doFilter(request,response);
    }
}
