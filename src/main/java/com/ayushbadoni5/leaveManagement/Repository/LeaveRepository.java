package com.ayushbadoni5.leaveManagement.Repository;

import com.ayushbadoni5.leaveManagement.Entities.Leave;
import com.ayushbadoni5.leaveManagement.Enums.LeaveStatus;
import com.ayushbadoni5.leaveManagement.Enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long> {
    List<Leave> findByUserId(Long userId);

    int countByUserIdAndLeaveTypeAndLeaveStatus(Long userId, LeaveType leaveType, LeaveStatus leaveStatus);

}
