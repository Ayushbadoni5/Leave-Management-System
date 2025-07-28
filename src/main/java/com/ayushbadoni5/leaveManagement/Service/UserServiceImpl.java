package com.ayushbadoni5.leaveManagement.Service;


import com.ayushbadoni5.leaveManagement.DTOs.UserDto;
import com.ayushbadoni5.leaveManagement.DTOs.UserUpdateDto;
import com.ayushbadoni5.leaveManagement.Entities.User;
import com.ayushbadoni5.leaveManagement.Exceptions.ResourceNotFoundException;
import com.ayushbadoni5.leaveManagement.Repository.RoleRepository;
import com.ayushbadoni5.leaveManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setPhoneNumber(user.getPhoneNumber());
            userDto.setRoleCode(user.getRole().getRoleCode());
            return userDto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId, String loggedInEmail) throws AccessDeniedException {
        User loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loggedInEmail));

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));


        return UserDto.builder()
                .id(targetUser.getId())
                .email(targetUser.getEmail())
                .firstName(targetUser.getFirstName())
                .lastName(targetUser.getLastName())
                .phoneNumber(targetUser.getPhoneNumber())
                .roleCode(targetUser.getRole().getRoleCode())
                .build();
    }

    @Override
    public UserDto updateUser(Long id, UserUpdateDto userUpdateDto, String loggedInEmail) throws AccessDeniedException {
        User loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(()-> new ResourceNotFoundException("user","email",loggedInEmail));

        User targetUser = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("user","id",id));


        targetUser.setFirstName(userUpdateDto.getFirstName());
        targetUser.setLastName(userUpdateDto.getLastName());
        targetUser.setPhoneNumber(userUpdateDto.getPhoneNumber());

        User updatedUser = userRepository.save(targetUser);

        return UserDto.builder()
                .id(targetUser.getId())
                .email(targetUser.getEmail())
                .firstName(targetUser.getFirstName())
                .lastName(targetUser.getLastName())
                .phoneNumber(targetUser.getPhoneNumber())
                .roleCode(targetUser.getRole().getRoleCode())
                .build();
    }

    @Override
    public void deleteUser(Long id, String loggedInEmail) throws AccessDeniedException {
        User loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loggedInEmail));

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));



        userRepository.delete(targetUser);
    }

    @Override
    public UserDto getMyProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .roleCode(user.getRole().getRoleCode())
                .build();
    }
}
