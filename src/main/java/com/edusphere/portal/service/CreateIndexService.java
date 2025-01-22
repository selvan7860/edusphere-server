package com.edusphere.portal.service;


import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CreateIndexService {

    @Autowired
    private RestHighLevelClient client;

    public void createIndexWithSchema(){
        try{
            String schema = new String(Files.readAllBytes(Paths.get("src/main/resources/schema.json")));
            Request request = new Request("PUT", "/colleges");
            request.setJsonEntity(schema);
            client.getLowLevelClient().performRequest(request);
            System.out.println("Index created successfully with settings and mappings.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
