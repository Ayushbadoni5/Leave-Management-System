package com.ayushbadoni5.leaveManagement.Service;

import com.ayushbadoni5.leaveManagement.DTOs.LeaveRequestDto;
import com.ayushbadoni5.leaveManagement.DTOs.LeaveResponseDto;
import com.ayushbadoni5.leaveManagement.DTOs.LeaveStatsDto;
import com.ayushbadoni5.leaveManagement.Entities.Leave;
import com.ayushbadoni5.leaveManagement.Entities.User;
import com.ayushbadoni5.leaveManagement.Enums.LeaveStatus;
import com.ayushbadoni5.leaveManagement.Enums.LeaveType;
import com.ayushbadoni5.leaveManagement.Exceptions.ResourceNotFoundException;
import com.ayushbadoni5.leaveManagement.Repository.LeaveRepository;
import com.ayushbadoni5.leaveManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService{

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public LeaveResponseDto applyLeave(LeaveRequestDto requestDto, Principal loggedInEmail) {
        User user = userRepository.findByEmail(loggedInEmail.getName()).orElseThrow(()-> new ResourceNotFoundException("User","Email",loggedInEmail.getName()));

        Leave leave = Leave.builder()
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .reason(requestDto.getReason())
                .leaveType(requestDto.getLeaveType())
                .leaveStatus(LeaveStatus.PENDING)
                .user(user)
                .build();

        Leave saved = leaveRepository.save(leave);

        return LeaveResponseDto.builder()
                .id(saved.getId())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .reason(saved.getReason())
                .leaveType(saved.getLeaveType())
                .leaveStatus(saved.getLeaveStatus())
                .userId(saved.getUser().getId())
                .build();
    }

    @Override
    public List<LeaveResponseDto> getMyLeaves(Principal loggedInEmail) {
       User user = userRepository.findByEmail(loggedInEmail.getName())
               .orElseThrow(() -> new ResourceNotFoundException("User", "email", loggedInEmail.getName()));

       return user.getLeaveList().stream().map(leave -> LeaveResponseDto.builder()
               .id(leave.getId())
               .startDate(leave.getStartDate())
               .endDate(leave.getEndDate())
               .reason(leave.getReason())
               .leaveStatus(leave.getLeaveStatus())
               .leaveType(leave.getLeaveType())
               .userId(user.getId())
               .build()).collect(Collectors.toList());
    }

    @Override
    public void cancelLeave(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave", "id", id));

        if (!leave.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not authorized to cancel this leave.");
        }

        if (leave.getLeaveStatus() != LeaveStatus.PENDING){
            throw new RuntimeException("Only pending leaves can be cancelled.");
        }

        leaveRepository.delete(leave);
    }

    @Override
    public List<LeaveResponseDto> getAllLeaves() {
        List<Leave> leaves = leaveRepository.findAll();
        return leaves.stream().map(leave -> LeaveResponseDto.builder()
                .id(leave.getId())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .reason(leave.getReason())
                .leaveType(leave.getLeaveType())
                .leaveStatus(leave.getLeaveStatus())
                .userId(leave.getId())
                .build()).collect(Collectors.toList());
    }

    @Override
    public LeaveResponseDto approveLeave(Long leaveId) {
        Leave leave = leaveRepository.findById(leaveId).orElseThrow(()-> new ResourceNotFoundException("Leave","id",leaveId));

        leave.setLeaveStatus(LeaveStatus.APPROVED);
        Leave update = leaveRepository.save(leave);

        return LeaveResponseDto.builder()
                .id(update.getId())
                .startDate(update.getStartDate())
                .endDate(update.getEndDate())
                .reason(update.getReason())
                .leaveType(update.getLeaveType())
                .leaveStatus(update.getLeaveStatus())
                .userId(update.getUser().getId())
                .build();
    }

    @Override
    public LeaveResponseDto rejectLeave(Long leaveId) {
        Leave leave = leaveRepository.findById(leaveId).orElseThrow(()-> new ResourceNotFoundException("Leave","id",leaveId));
        leave.setLeaveStatus(LeaveStatus.REJECTED);
        Leave update = leaveRepository.save(leave);

        return LeaveResponseDto.builder()
                .id(update.getId())
                .startDate(update.getStartDate())
                .endDate(update.getEndDate())
                .reason(update.getReason())
                .leaveType(update.getLeaveType())
                .leaveStatus(update.getLeaveStatus())
                .userId(update.getUser().getId())
                .build();
    }

    @Override
    public List<LeaveStatsDto> getLeaveStatusOfUser(Long userId, Principal loggedInUser) throws AccessDeniedException {
        User user = userRepository.findByEmail(loggedInUser.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loggedInUser.getName()));

        boolean isAdmin = user.getRole().getRoleCode().equalsIgnoreCase("ROLE_ADMIN");
        boolean isSelf = user.getId().equals(userId);

        if (!isSelf && !isAdmin){
            throw new AccessDeniedException("You are not authorized to access this data.");
        }


        List<LeaveStatsDto> list = new ArrayList<>();

        for (LeaveType leaveType: LeaveType.values()){
            int totalTaken = leaveRepository.countByUserIdAndLeaveType(userId,leaveType);

            int maxAllowed = getMaxAllowedLeave(leaveType);

            int remaining = maxAllowed - totalTaken;

            LeaveStatsDto stats = LeaveStatsDto.builder()
                    .leaveType(leaveType)
                    .totalTaken(totalTaken)
                    .maxAllowed(maxAllowed)
                    .remaining(Math.max(remaining,0))
                    .build();
            list.add(stats);
        }
       return list;
    }



    private int getMaxAllowedLeave(LeaveType leaveType) {
        return switch (leaveType){
            case SICK_LEAVE ->10;
            case CASUAL_LEAVE -> 8;
            case EARNED_LEAVE -> 12;
            case UNPAID_LEAVE -> 999;
        };
    }
}
