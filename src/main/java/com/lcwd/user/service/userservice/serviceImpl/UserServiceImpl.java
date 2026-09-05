package com.lcwd.user.service.userservice.serviceImpl;

import com.lcwd.user.service.userservice.Excxeption.ResourceNotFoundException;
import com.lcwd.user.service.userservice.entity.User;
import com.lcwd.user.service.userservice.repo.UserRepo;
import com.lcwd.user.service.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
   @Autowired
    private UserRepo userRepo;
   @Autowired
   private RestTemplate restTemplate;

   private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public User saveUser(User user) {
        String ramdomUserId= UUID.randomUUID().toString();
        user.setId(ramdomUserId);
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUser() {

        List<User> userList= userRepo.findAll();
        for(User user:userList) {
            ArrayList ratingOfUser = restTemplate.getForObject
                    ("http://localhost:8083/ms/rating/users/" + user.getId(), ArrayList.class);

            logger.info("{}:{}", user.getId(),ratingOfUser);
            user.setRating(ratingOfUser);
        }
        return userList;
    }

    @Override
    public User getUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("user id invalid"));

        ArrayList ratingOfUser = restTemplate.getForObject
                ("http://localhost:8083/ms/rating/users/"+user.getId(),ArrayList.class);

        logger.info("{}",ratingOfUser);
        user.setRating(ratingOfUser);

        return user;
    }
}
