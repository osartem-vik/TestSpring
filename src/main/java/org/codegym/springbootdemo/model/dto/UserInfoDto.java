package org.codegym.springbootdemo.model.dto;

import lombok.Data;

@Data
public class UserInfoDto {
  private String firstName;
  private String lastName;
  private String email;
  private int age;
}
