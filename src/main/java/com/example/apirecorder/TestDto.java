package com.example.apirecorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDto {
    private Long id;
    private String name;
    private List<String> data ;
}
