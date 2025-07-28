package com.ayushbadoni5.leaveManagement.DTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDto {
    private Long Id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String roleName;
    private String roleCode;
}
