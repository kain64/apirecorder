package com.example.apirecorder.recorder.dto;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.UUID;

@Data
@Document(indexName = "apicalls")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiCall {
    private UUID id = UUID.randomUUID();
    private Date date = new Date();
    private String path;
    private CallType callType;
    private Request request;
    private Response response;
}
