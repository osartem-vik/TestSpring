package org.codegym.springbootdemo.model.dto;

import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User create DTO")
public class UserDto {

  @Size(min = 2, max = 100)
  private String firstName;

  @Size(min = 2, max = 100)
  private String lastName;
  private LocalDate birthDate;
  private Double salary;
  @NotBlank
  @Email
  private String email;
  @Min(1)
  private Integer age;

}
