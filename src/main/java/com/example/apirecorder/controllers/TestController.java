package com.example.apirecorder.controllers;

import com.example.apirecorder.TestDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("test")
@Log4j2
public class TestController {
    @GetMapping("/get2")
    TestDto get2(@RequestParam Long id, @RequestParam String name){
        log.debug("GET call");
        return new TestDto(id, name, List.of("test","test1", "test2"));
    }
    @GetMapping("/get/{id}/{name}")
    TestDto get(@PathVariable Long id, @PathVariable String name){
        log.debug("GET call");
        return new TestDto(id, name, List.of("test","test1", "test2"));
    }
    @GetMapping("/get1/{id}/{name}")
    TestDto get1(@PathVariable Long id, @PathVariable String name){
        log.debug("GET call");
        return new TestDto(id, name, List.of("test","test1", "test2"));
    }

    @PostMapping("/post/{id}")
    TestDto post(@PathVariable Long id, @RequestBody TestDto testDto){
        log.debug("POST call");
        return new TestDto(id, testDto.getName(), List.of("ololo"));
    }
    @PatchMapping("/path/{id}/{name}")
    void patch(@PathVariable Long id, @PathVariable String name){
        log.debug("PATCH call");
    }

    @DeleteMapping("/delete/{id}/{name}")
    void delete(@PathVariable Long id, @PathVariable String name){
        log.debug("DELETE call");
    }

}
