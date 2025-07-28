package com.ayushbadoni5.leaveManagement.Controllers;

import com.ayushbadoni5.leaveManagement.DTOs.UserDto;
import com.ayushbadoni5.leaveManagement.DTOs.UserUpdateDto;
import com.ayushbadoni5.leaveManagement.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <List<UserDto>> getAllUsers(){
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity <UserDto> getUserById(@PathVariable Long id , Principal loggedInEmail) throws AccessDeniedException {
        UserDto user = userService.getUserById(id,loggedInEmail.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto, Principal loggedInEmail) throws AccessDeniedException {
        UserDto updatedUser = userService.updateUser(id,userUpdateDto,loggedInEmail.getName());
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Principal loggedInEmail) throws AccessDeniedException {
        userService.deleteUser(id,loggedInEmail.getName());
        return new ResponseEntity<>("User Deleted", HttpStatus.OK);
    }

    @GetMapping("/myProfile")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity <UserDto> getMyProfile(Principal loggedInUser){
        UserDto user = userService.getMyProfile(loggedInUser.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
