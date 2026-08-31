package com.lcwd.user.service.userservice.repo;

import com.lcwd.user.service.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String> {
}
