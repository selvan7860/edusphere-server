package com.edusphere.portal.service;


import com.edusphere.portal.dao.User;
import com.edusphere.portal.dto.UserDTO;
import com.edusphere.portal.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private static final String EMAIL_ALREADY_IN_USE = "Email already in use";
    private static final String INVALID_EMAIL = "Invalid email";
    private static final String EMAIL_CANNOT_BE_EMPTY = "Email cannot be empty";

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserDTO createUser(UserDTO userDTO) {
        validateUser(userDTO);
        User saveUser = new User();
        saveUser.setEmail(userDTO.getEmail());
        saveUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        saveUser.setConfirmPassword(passwordEncoder.encode(userDTO.getConfirmPassword()));
        saveUser.setRole(userDTO.getRole());

        User user = userRepository.save(saveUser);
        return convertToDTO(user);

    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setConfirmPassword(user.getConfirmPassword());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private void validateUser(UserDTO userDTO) {

        String email = userDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException(EMAIL_ALREADY_IN_USE);
        }

        if(email == null || email.trim().isEmpty()){
            throw new IllegalStateException(EMAIL_CANNOT_BE_EMPTY);
        }

        if(!isValidEmail(email)) {
            throw new IllegalStateException(INVALID_EMAIL);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

}
