package com.example.apirecorder.recorder.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class Response {
    private Object result;

    private Integer statusCode;
    private String body;
}
