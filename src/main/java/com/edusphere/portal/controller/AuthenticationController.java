package com.edusphere.portal.controller;


import com.edusphere.portal.dto.AuthDTO;
import com.edusphere.portal.dto.GenericResponse;
import com.edusphere.portal.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public GenericResponse authenticationUser(@RequestBody AuthDTO authDTO){
        return new GenericResponse(authenticationService.authenticationUser(authDTO));
    }
}
