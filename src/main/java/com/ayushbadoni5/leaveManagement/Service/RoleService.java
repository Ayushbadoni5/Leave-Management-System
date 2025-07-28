package com.ayushbadoni5.leaveManagement.Service;

import com.ayushbadoni5.leaveManagement.Entities.Role;
import com.ayushbadoni5.leaveManagement.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<String> getAllRoles(){
        return roleRepository.findAll()
                .stream()
                .map(Role::getRoleCode)
                .collect(Collectors.toList());
    }
}
