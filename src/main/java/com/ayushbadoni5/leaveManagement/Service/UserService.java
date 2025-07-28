package com.ayushbadoni5.leaveManagement.Service;

import com.ayushbadoni5.leaveManagement.DTOs.UserDto;
import com.ayushbadoni5.leaveManagement.DTOs.UserUpdateDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
   List<UserDto> getAllUsers();

   UserDto getUserById(Long userId, String loggedInEmail) throws AccessDeniedException;

   UserDto updateUser(Long id, UserUpdateDto userUpdateDto, String loggedInEmail) throws AccessDeniedException;

   void deleteUser(Long id, String loggedInEmail) throws AccessDeniedException;

   UserDto getMyProfile(String email);
}
