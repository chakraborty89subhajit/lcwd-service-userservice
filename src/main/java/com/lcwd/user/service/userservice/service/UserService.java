package com.lcwd.user.service.userservice.service;

import com.lcwd.user.service.userservice.entity.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    List<User> getAllUser();
    User getUser(String userId);

}
