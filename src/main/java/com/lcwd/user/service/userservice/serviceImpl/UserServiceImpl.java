package com.lcwd.user.service.userservice.serviceImpl;

import com.lcwd.user.service.userservice.Excxeption.ResourceNotFoundException;
import com.lcwd.user.service.userservice.entity.Hotel;
import com.lcwd.user.service.userservice.entity.Rating;
import com.lcwd.user.service.userservice.entity.User;
import com.lcwd.user.service.userservice.external.service.HotelService;
import com.lcwd.user.service.userservice.repo.UserRepo;
import com.lcwd.user.service.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
   @Autowired
    private UserRepo userRepo;
   @Autowired
   private RestTemplate restTemplate;

   @Autowired
   private HotelService hotelService;

   private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public User saveUser(User user) {
        String ramdomUserId= UUID.randomUUID().toString();
        user.setId(ramdomUserId);
        return userRepo.save(user);
    }

//get all user
    @Override
    public List<User> getAllUser() {


            List<User> userList = userRepo.findAll();

            for (User user : userList) {
                try {
                    // 1. Fetch ratings for the current user
                    Rating[] ratingOfUser = restTemplate.getForObject(
                            "http://RATINGSERVICE/ms/rating/users/" + user.getId(), Rating[].class);

                    if (ratingOfUser != null && ratingOfUser.length > 0) {
                        List<Rating> ratingList = new ArrayList<>();

                        for (Rating rating : ratingOfUser) {
                            // Check for valid hotelId before calling HotelService
                            if (rating != null && rating.getHotelId() != null) {
                                try {
                                   // ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(
                                     //       "http://HOTELSERVICE/hotels/" + rating.getHotelId(), Hotel.class);

                                   Hotel hotel =hotelService.getHotel(rating.getHotelId());
                                    //Hotel hotel = forEntity.getBody();
                                    rating.setHotel(hotel);
                                } catch (Exception e) {
                                    logger.error("Failed to fetch hotel with ID {}: {}", rating.getHotelId(), e.getMessage());
                                    rating.setHotel(null); // Fallback if Hotel Service fails
                                }
                            }
                            ratingList.add(rating);
                        }
                        user.setRating(ratingList);
                    } else {
                        user.setRating(new ArrayList<>()); // Assign empty list if no ratings found
                    }
                } catch (Exception e) {
                    logger.error("Failed to fetch ratings for user ID {}: {}", user.getId(), e.getMessage());
                    user.setRating(new ArrayList<>()); // Assign empty list if Rating Service fails/returns 404
                }
            }

            return userList;
        }


    //get single user
    @Override
    public User getUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: " + userId));

        try {
            // Calling via Eureka Service Name
            Rating[] ratingsOfUser = restTemplate.getForObject(
                    "http://RATINGSERVICE/ms/rating/users/" + user.getId(), Rating[].class);

            if (ratingsOfUser != null) {
                List<Rating> ratingList = new ArrayList<>();

                for (Rating rating : ratingsOfUser) {
                    try {
                        //ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(
                          //      "http://HOTELSERVICE/hotels/" + rating.getHotelId(), Hotel.class);

                        Hotel hotel = hotelService.getHotel(rating.getHotelId());

                        //rating.setHotel(forEntity.getBody());
                        rating.setHotel(hotel);
                    } catch (Exception e) {
                        logger.error("Failed to fetch hotel {}: {}", rating.getHotelId(), e.getMessage());
                        rating.setHotel(null);
                    }
                    ratingList.add(rating);
                }
                user.setRating(ratingList);
            }
        } catch (Exception e) {
            logger.error("Failed to fetch ratings for user {}: {}", userId, e.getMessage());
            user.setRating(new ArrayList<>()); // Fallback to empty list
        }

        return user;
    }
}
