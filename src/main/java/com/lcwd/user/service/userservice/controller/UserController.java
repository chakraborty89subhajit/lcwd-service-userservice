package com.lcwd.user.service.userservice.controller;

import com.lcwd.user.service.userservice.entity.User;
import com.lcwd.user.service.userservice.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ms/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<User> createuser(@RequestBody User user) {
        User user1 = userService.saveUser(user);
        return new ResponseEntity<>(user1, HttpStatus.CREATED);
    }


    int retrycount = 1;

    @GetMapping("/{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    @Retry(name="ratingHotelService",fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
       logger.info("get single user handler: userController");
       logger.info("retry count: {}",retrycount);
       retrycount++;


        User user1 = userService.getUser(userId);
        return new ResponseEntity<>(user1, HttpStatus.OK);
    }

    // Fallback Method
    public ResponseEntity<User> ratingHotelFallback(String userId, Throwable ex) {
      //  logger.info("Fallback executed due to: {}", ex.getMessage());

        User dummyUser = User.builder()
                .id("123456")
                .name("Dummy User")
                .email("dummy@gmail.com")
                .about("This dummy user is returned because RATINGSERVICE is down.")
                .build();

        return new ResponseEntity<>(dummyUser, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> allUser = userService.getAllUser();
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }
}