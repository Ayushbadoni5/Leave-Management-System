package com.ayushbadoni5.leaveManagement.Repository;

import com.ayushbadoni5.leaveManagement.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleCode(String roleCode);

}
