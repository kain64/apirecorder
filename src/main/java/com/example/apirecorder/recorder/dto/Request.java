package com.example.apirecorder.recorder.dto;

import lombok.*;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Request {
    CallType callType;
    private Map<String, String[]> arguments;
    private String body;
}
