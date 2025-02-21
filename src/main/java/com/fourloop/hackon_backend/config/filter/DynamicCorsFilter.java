package com.fourloop.hackon_backend.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DynamicCorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String origin = httpRequest.getHeader("Origin");

        if (origin != null && origin.endsWith(".vusercontent.net")) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, ngrok-skip-browser-warning");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

            // Handle preflight OPTIONS request
            if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                return; // Stop further processing for OPTIONS request
            }
        }
        System.out.println("Incoming Request Origin: " + origin);
        System.out.println("Incoming Request Method: " + httpRequest.getMethod());
        System.out.println("Incoming Headers:");
        httpRequest.getHeaderNames().asIterator().forEachRemaining(header ->
                System.out.println(header + ": " + httpRequest.getHeader(header))
        );


        chain.doFilter(request, response);
    }
}
