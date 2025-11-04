/*
 * Copyright 2025 Hamzat Olowu
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * GitHub: https//github.com/CHA0sTIG3R
 */

package com.project.marginal.tax.calculator.config;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NotNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> requestLoggingFilter() {
        final Logger log = LoggerFactory.getLogger("http.request");

        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/*");
        registration.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(@NotNull HttpServletRequest request,
                                            @NotNull HttpServletResponse response,
                                            @NotNull FilterChain filterChain) throws ServletException, IOException {
                final long startNs = System.nanoTime();

                String rid = request.getHeader("X-Request-Id");
                if (rid == null || rid.isBlank()) {
                    rid = UUID.randomUUID().toString();
                }
                MDC.put("rid", rid);
                try {
                    filterChain.doFilter(request, response);
                } finally {
                    long durationMs = (System.nanoTime() - startNs) / 1_000_000L;
                    String method = request.getMethod();
                    String path = request.getRequestURI();
                    int status = response.getStatus();
                    String ip = extractClientIp(request);
                    String ua = headerOrDash(request, "User-Agent");
                    boolean hasApiKey = request.getHeader("X-API-KEY") != null;

                    if (hasApiKey) {
                        log.info("rid={} method={} path={} status={} duration_ms={} ip={} ua={} x_api_key=redacted",
                                rid, method, path, status, durationMs, ip, ua);
                    } else {
                        log.info("rid={} method={} path={} status={} duration_ms={} ip={} ua={}",
                                rid, method, path, status, durationMs, ip, ua);
                    }
                    MDC.remove("rid");
                }
            }

            private String extractClientIp(HttpServletRequest request) {
                String xff = request.getHeader("X-Forwarded-For");
                if (xff != null && !xff.isBlank()) {
                    int comma = xff.indexOf(',');
                    return comma > 0 ? xff.substring(0, comma).trim() : xff.trim();
                }
                return request.getRemoteAddr();
            }

            private String headerOrDash(HttpServletRequest request, String name) {
                String v = request.getHeader(name);
                return (v == null || v.isBlank()) ? "-" : v;
            }
        });
        return registration;
    }
}
