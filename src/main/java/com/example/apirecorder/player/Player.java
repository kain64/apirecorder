package com.example.apirecorder.player;

import com.example.apirecorder.recorder.dto.ApiCall;
import com.example.apirecorder.recorder.dto.Request;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.UUID;

@Component
@AllArgsConstructor
public class Player {
    private final RestTemplate restTemplate;
    public void makeApiCall(ApiCall apiCall) throws IOException {
        if (apiCall.getPath() != null && apiCall.getRequest() != null) {
            String url = "http://localhost:8080" + apiCall.getPath();
            Request request = apiCall.getRequest();
            String method = request.getCallType().name();
            Object result = switch (request.getCallType()) {
                case GET -> restTemplate.getForObject(url, Object.class, request.getArguments());
                case POST -> restTemplate.postForObject(url, request.getBody(), Object.class, request.getArguments());
                case PUT -> {
                    restTemplate.put(url, request.getBody(), request.getArguments());
                    yield "PUT request sent successfully";
                }
                case PATCH -> {
                    restTemplate.patchForObject(url, request.getBody(), Object.class, request.getArguments());
                    yield "PATCH request sent successfully";
                }
                default -> throw new IllegalArgumentException("Unsupported method: " + method);
            };
            apiCall.getResponse().setResult(result);
            apiCall.getResponse().setBody(result.toString());

            if (!compareJsonObjects(result, apiCall)) {
                throw new RuntimeException("response is wrong");
            }
        }
    }

    public boolean compareJsonObjects(Object obj1, ApiCall apiCall) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json1 = mapper.writeValueAsString(obj1);
        String json2 = mapper.writeValueAsString(apiCall);
        JsonNode node1 = mapper.readTree(json1);
        JsonNode node2 = mapper.readTree(json2);
        removeFields(node1);
        removeFields(node2);
        return node1.equals(node2);
    }

    private void removeFields(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            objectNode.fields().forEachRemaining(entry -> {
                JsonNode value = entry.getValue();
                if (value.isTextual()) {
                    String textValue = value.asText();
                    if (isUUID(textValue) || isDate(textValue)) {
                        objectNode.remove(entry.getKey());
                    }
                }
                removeFields(value);
            });
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            arrayNode.forEach(this::removeFields);
        }
    }

    private boolean isUUID(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isDate(String value) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.readValue("\"" + value + "\"", java.util.Date.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
