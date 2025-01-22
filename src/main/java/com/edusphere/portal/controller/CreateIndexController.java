package com.edusphere.portal.controller;

import com.edusphere.portal.service.CreateIndexService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/index")
public class CreateIndexController {

    private final CreateIndexService service;


    public CreateIndexController(CreateIndexService service) {
        this.service = service;
    }

    @GetMapping
    public String createIndex(){
        service.createIndexWithSchema();
        return "Index created successfully with settings and mappings.";
    }
}
