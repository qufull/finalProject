package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser {
       private String username;
       private String firstName;
       private String lastName;
       private String phoneNumber;
       private String currency;
}
