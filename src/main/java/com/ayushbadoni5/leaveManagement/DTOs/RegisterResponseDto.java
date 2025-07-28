package com.ayushbadoni5.leaveManagement.DTOs;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDto {
    private String message;
    private String email;
    private String role;
}
