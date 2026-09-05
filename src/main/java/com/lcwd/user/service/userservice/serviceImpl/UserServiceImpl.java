package com.lcwd.user.service.userservice.serviceImpl;

import com.lcwd.user.service.userservice.Excxeption.ResourceNotFoundException;
import com.lcwd.user.service.userservice.entity.Hotel;
import com.lcwd.user.service.userservice.entity.Rating;
import com.lcwd.user.service.userservice.entity.User;
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
                            "http://localhost:8083/ms/rating/users/" + user.getId(), Rating[].class);

                    if (ratingOfUser != null && ratingOfUser.length > 0) {
                        List<Rating> ratingList = new ArrayList<>();

                        for (Rating rating : ratingOfUser) {
                            // Check for valid hotelId before calling HotelService
                            if (rating != null && rating.getHotelId() != null) {
                                try {
                                    ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(
                                            "http://localhost:8082/hotels/" + rating.getHotelId(), Hotel.class);

                                    Hotel hotel = forEntity.getBody();
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
        User user = userRepo.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("user id invalid"));

        Rating[] ratingOfUser = restTemplate.getForObject
                ("http://localhost:8083/ms/rating/users/"+user.getId(),Rating[].class);
        logger.info("{}",ratingOfUser);
        List<Rating> ratings= Arrays.stream(ratingOfUser).collect(Collectors.toList());
        List<Rating> ratingList = ratings.stream()
                        .map(rating -> {
                            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(
                                    "http://localhost:8082/hotels/"+rating.getHotelId(), Hotel.class);
                           Hotel hotel =forEntity.getBody();
                            logger.info("response status coe : {}",forEntity.getStatusCode());
                            rating.setHotel(hotel);
                            return rating;
                        }).collect(Collectors.toList());

        user.setRating(ratings);

        return user;
    }
}
