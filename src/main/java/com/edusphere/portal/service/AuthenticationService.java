package com.edusphere.portal.service;


import com.edusphere.portal.dao.User;
import com.edusphere.portal.dto.AuthDTO;
import com.edusphere.portal.dto.AuthResponseDTO;
import com.edusphere.portal.exception.InvalidCredentialsException;
import com.edusphere.portal.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO authenticationUser(AuthDTO authDTO){

        User user = userRepository.findByEmail(authDTO.getEmail());
        if(user == null){
            throw new InvalidCredentialsException("Email Not Found");
        }
        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return convertToDTO(user);
    }

    private AuthResponseDTO convertToDTO(User user) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setEmail(user.getEmail());
        authResponseDTO.setRole(user.getRole());
        return authResponseDTO;
    }
}
