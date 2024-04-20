package com.example.apirecorder.recorder;

import com.example.apirecorder.recorder.dto.ApiCall;
import com.example.apirecorder.recorder.dto.CallType;
import com.example.apirecorder.recorder.dto.Request;
import com.example.apirecorder.recorder.dto.Response;
import com.example.apirecorder.recorder.services.ElasticServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@Order(1)
@AllArgsConstructor
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {


    private final ElasticServices elasticServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        ApiCall apiCall = new ApiCall();
        apiCall.setPath(requestWrapper.getRequestURI());
        // Log request details


        filterChain.doFilter(requestWrapper, responseWrapper);

        // Log response details
        logResponse(apiCall, responseWrapper);
        logRequest(apiCall, requestWrapper);
        responseWrapper.copyBodyToResponse();
    }

    private void logRequest(ApiCall apiCall, ContentCachingRequestWrapper requestWrapper) throws IOException {

        Request request = new Request();
        request.setCallType(CallType.valueOf(requestWrapper.getMethod()));
        request.setArguments(requestWrapper.getParameterMap());
        request.setBody(new String(requestWrapper.getContentAsByteArray()));
        requestWrapper.getReader();
        apiCall.setRequest(request);
    }

    private void logResponse(ApiCall apiCall, ContentCachingResponseWrapper responseWrapper) throws IOException {
        Response response = new Response();
        response.setStatusCode(responseWrapper.getStatus());
        response.setBody(new String(responseWrapper.getContentAsByteArray()));
        response.setResult(new String(responseWrapper.getContentAsByteArray()));
        apiCall.setResponse(response);
    }

}

