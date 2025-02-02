package com.edusphere.portal.controller;


import com.edusphere.portal.dto.GenericResponse;
import com.edusphere.portal.dto.UserDTO;
import com.edusphere.portal.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public GenericResponse createUser(@RequestBody UserDTO userDTO){
        return new GenericResponse(userService.createUser(userDTO));
    }
}
