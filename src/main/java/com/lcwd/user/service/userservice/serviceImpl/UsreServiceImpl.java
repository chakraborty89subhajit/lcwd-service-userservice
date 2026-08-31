package com.lcwd.user.service.userservice.serviceImpl;

import com.lcwd.user.service.userservice.Excxeption.ResourceNotFoundException;
import com.lcwd.user.service.userservice.entity.User;
import com.lcwd.user.service.userservice.repo.UserRepo;
import com.lcwd.user.service.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UsreServiceImpl implements UserService {
   @Autowired
    private UserRepo userRepo;
    @Override
    public User saveUser(User user) {
        String ramdomUserId= UUID.randomUUID().toString();
        user.setId(ramdomUserId);
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public User getUser(String userId) {
        return userRepo.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("user id invalid"));
    }
}
