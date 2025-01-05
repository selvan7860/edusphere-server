package com.edusphere.portal.dto;

import com.edusphere.portal.dao.Role;
import lombok.*;

@Data
@NoArgsConstructor
public class UserDTO {

    private String email;
    private String password;
    private String confirmPassword;
    private Role role;
}
