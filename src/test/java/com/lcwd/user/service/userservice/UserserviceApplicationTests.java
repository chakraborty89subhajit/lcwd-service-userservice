package com.lcwd.user.service.userservice;

import com.lcwd.user.service.userservice.entity.Rating;
import com.lcwd.user.service.userservice.external.service.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserserviceApplicationTests {

	@Autowired
	private RatingService retingService;

	@Test
	void contextLoads() {
	}


	//post method
	@Test
	public void createRating(){
		Rating rating = Rating.builder()
				.rating(10)
				.userId("")
				.hotelId("")
				.feedback(" this is created by feign client")
				.build();

		Rating savedRating = retingService.createRating(rating);
		System.out.println("new rating service created by feign client");
	}

	//put method
	@Test
	public void updateRating(){

		String ratingId= "9b8df4fb-89b1-47ca-a00a-c9574bc059cb";

		Rating rating = Rating.builder()
				.rating(7)
				.userId("")
				.hotelId("")
				.feedback("this is updated by feign client")
				.build();

		Rating updateRating= retingService.updateRating(ratingId,rating);
		System.out.println("rating service updated by feign client");
	}
	//delete method
	public void deleteRating(){
		String ratingId = ""; // Pass existing rating ID here

		retingService.deleteRating(ratingId);
		System.out.println("Rating deleted successfully for ID: " + ratingId);

	}

}
