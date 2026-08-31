package com.lcwd.user.service.userservice.controller;

import com.lcwd.user.service.userservice.entity.User;
import com.lcwd.user.service.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ms/user")
public class UserController {
    @Autowired
    private UserService userService;

    //create
    @PostMapping("/")
    public ResponseEntity<?> createuser(@RequestBody User user){
        User user1 = userService.saveUser(user);
        return new ResponseEntity<User>(user1,HttpStatus.CREATED);
    }

    //get single user
    @GetMapping("/{userId}")
    public ResponseEntity<?> getSingleUser(@PathVariable String userId){
        User user1 = userService.getUser(userId);
        return new ResponseEntity<User>(user1,HttpStatus.OK);
    }
    //get all user
    @GetMapping("/")
    public ResponseEntity<?> getAllUser(){
       List<User> allUser = userService.getAllUser();
        return new ResponseEntity<List<User>>( allUser,HttpStatus.OK);
    }

}
