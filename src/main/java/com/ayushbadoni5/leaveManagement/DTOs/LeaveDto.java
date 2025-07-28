package com.ayushbadoni5.leaveManagement.DTOs;

import com.ayushbadoni5.leaveManagement.Enums.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveDto {
    private Long id;
    private Long userId;
    private Long leaveTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus leaveStatus;
}
