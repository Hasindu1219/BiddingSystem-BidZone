package com.groupeight.BidZone.Operations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    private int id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    private Integer age;
    private String gender;
    private String likedCategories;
    private String category;

}
