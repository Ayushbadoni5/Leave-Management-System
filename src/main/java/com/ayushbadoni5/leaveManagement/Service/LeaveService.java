package com.ayushbadoni5.leaveManagement.Service;

import com.ayushbadoni5.leaveManagement.DTOs.LeaveRequestDto;
import com.ayushbadoni5.leaveManagement.DTOs.LeaveResponseDto;
import com.ayushbadoni5.leaveManagement.DTOs.LeaveStatsDto;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

public interface LeaveService {

    LeaveResponseDto applyLeave(LeaveRequestDto leaveRequestDto, Principal loggedInUser);

    List<LeaveResponseDto> getMyLeaves(Principal loggedInUser);

    List<LeaveResponseDto> getAllLeaves();

    LeaveResponseDto approveLeave(Long leaveId);

    LeaveResponseDto rejectLeave(Long leaveId);

    List<LeaveStatsDto> getLeaveStatusOfUser(Long userId, Principal loggedInUser) throws AccessDeniedException;

    void cancelLeave(Long id, String email);
}
