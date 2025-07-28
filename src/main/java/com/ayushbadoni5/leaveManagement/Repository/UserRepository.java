package com.ayushbadoni5.leaveManagement.Repository;

import com.ayushbadoni5.leaveManagement.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail (String email);
}
