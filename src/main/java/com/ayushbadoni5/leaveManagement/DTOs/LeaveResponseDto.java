package com.ayushbadoni5.leaveManagement.DTOs;

import com.ayushbadoni5.leaveManagement.Enums.LeaveStatus;
import com.ayushbadoni5.leaveManagement.Enums.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LeaveResponseDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveType leaveType;
    private LeaveStatus leaveStatus;
    private Long userId;
}
