package com.ayushbadoni5.leaveManagement.Controllers;

import com.ayushbadoni5.leaveManagement.DTOs.LeaveRequestDto;
import com.ayushbadoni5.leaveManagement.DTOs.LeaveResponseDto;
import com.ayushbadoni5.leaveManagement.DTOs.LeaveStatsDto;
import com.ayushbadoni5.leaveManagement.Service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LeaveResponseDto> applyLeave(@RequestBody LeaveRequestDto leaveRequestDto, Principal loggedInUser){
        LeaveResponseDto leave = leaveService.applyLeave(leaveRequestDto,loggedInUser);
        return new ResponseEntity<>(leave, HttpStatus.CREATED);
    }

    @GetMapping("/myLeaves")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LeaveResponseDto>> getMyLeaves(Principal loggedInUser){
        List<LeaveResponseDto> leaves = leaveService.getMyLeaves(loggedInUser);
        return new ResponseEntity<>(leaves, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> cancelLeave(@PathVariable Long id, Principal loggedInUser){
        leaveService.cancelLeave(id, loggedInUser.getName());
        return new ResponseEntity<>("Leave canceled successfully", HttpStatus.OK);

    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LeaveResponseDto>> getAllLeaves(){
        List<LeaveResponseDto> list = leaveService.getAllLeaves();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveResponseDto> approveLeave(@PathVariable Long userId){
        LeaveResponseDto leave = leaveService.approveLeave(userId);
        return new ResponseEntity<>(leave,HttpStatus.OK);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveResponseDto> rejectLeave(@PathVariable Long userId){
        LeaveResponseDto leave = leaveService.rejectLeave(userId);
        return new ResponseEntity<>(leave,HttpStatus.OK);
    }


    @GetMapping("/stats/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<LeaveStatsDto>> myLeaveStatus(@PathVariable Long userId, Principal loggedInUser) throws AccessDeniedException {
        List<LeaveStatsDto> list = leaveService.getLeaveStatusOfUser(userId,loggedInUser);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
}
