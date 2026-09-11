package com.lcwd.user.service.userservice.external.service;

import com.lcwd.user.service.userservice.entity.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Service
@FeignClient(name = "RATINGSERVICE")
public interface RatingService {

    @PostMapping("/ms/rating/create")
    public Rating createRating(Rating rating);

    @PutMapping("/ms/rating/{ratingId}")
    public Rating updateRating(@PathVariable("ratingId")
                               String ratingId,
                               Rating rating
                               );

    @DeleteMapping("/ms/rating/{ratingId}")
    public void deleteRating(@PathVariable String ratingId);

}
