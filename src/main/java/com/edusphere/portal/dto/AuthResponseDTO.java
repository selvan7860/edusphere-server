package com.edusphere.portal.dto;


import com.edusphere.portal.dao.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponseDTO {

    private String email;
    private Role role;
}
