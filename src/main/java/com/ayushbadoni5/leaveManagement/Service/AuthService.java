package com.ayushbadoni5.leaveManagement.Service;

import com.ayushbadoni5.leaveManagement.DTOs.RegisterRequestDto;
import com.ayushbadoni5.leaveManagement.DTOs.RegisterResponseDto;
import com.ayushbadoni5.leaveManagement.Entities.Role;
import com.ayushbadoni5.leaveManagement.Entities.User;
import com.ayushbadoni5.leaveManagement.Repository.RoleRepository;
import com.ayushbadoni5.leaveManagement.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public RegisterResponseDto registerUser(RegisterRequestDto request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("User already exists with this email.");
        }

        String inputRole = (request.getRoleCode() == null ) ? "" : request.getRoleCode().trim().toUpperCase();

        Role selectedRole;

        if (inputRole.equals("ADMIN") || inputRole.equals("ROLE_ADMIN")){
            selectedRole = roleRepository.findByRoleCode("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
        }else{
            selectedRole = roleRepository.findByRoleCode("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("User role not found"));
        }


        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(selectedRole)
                .build();

        userRepository.save(user);

        String uiRoleName = selectedRole.getRoleCode().equals("ROLE_ADMIN") ? "Employer" : "Employee";

        return RegisterResponseDto.builder()
                .message("User registered successfully as " + uiRoleName)
                .email(user.getEmail())
                .role(user.getRole().getRoleCode())
                .build();
    }
}
