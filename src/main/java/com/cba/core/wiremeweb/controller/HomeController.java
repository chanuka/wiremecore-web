package com.cba.core.wiremeweb.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Home Controller(Optional)", description = "Just to Check whether the API is up and running")
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, World!";
    }
}
