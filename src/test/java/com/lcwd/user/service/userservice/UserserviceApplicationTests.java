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

}
