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

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.ingest.api-key}")
    private String expectedApiKey;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        if ("/api/v1/tax/upload".equals(request.getRequestURI())) {
            String apiKey = request.getHeader("X-API-KEY");
            if (apiKey == null || !apiKey.equals(expectedApiKey)) {
                System.out.println("🔑 Invalid or missing API Key");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            System.out.println("🔑 Valid API Key provided");
        }
        filterChain.doFilter(request,response);
    }
}
