package com.lcwd.user.service.userservice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="micro_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @Column(name="id")
    private String id;
    private String name;
    private String email;
    private String about;

    @Transient
    private List<Rating> rating;
}
