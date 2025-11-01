package com.project.marginal.tax.calculator.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        String rid = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("rid", rid);
        long start = System.nanoTime();
        String path = request.getRequestURI();
        String method = request.getMethod();
        String ua = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();

        logger.info("REQ rid={} {} {} ip={} ua={}");

        try {
            filterChain.doFilter(request, response);
        } finally {
            long ms = (System.nanoTime() - start) / 1_000_000;
            int status = response.getStatus();
            logger.info("RES rid={} {} {} status={} in {}ms");
            MDC.remove("rid");
        }
    }
}

