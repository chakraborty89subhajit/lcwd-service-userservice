package com.lcwd.user.service.userservice.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {
    private String Id;
    private String userId;
    private String hotelId;
    private int rating;
    private String feedback;

    private Hotel hotel;
}
