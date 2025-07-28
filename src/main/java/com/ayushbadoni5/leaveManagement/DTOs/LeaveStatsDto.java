package com.ayushbadoni5.leaveManagement.DTOs;

import com.ayushbadoni5.leaveManagement.Enums.LeaveType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LeaveStatsDto {
    private LeaveType leaveType;
    private int totalTaken;
    private int remaining;
    private int maxAllowed;
}
