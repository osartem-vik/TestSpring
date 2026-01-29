package org.codegym.springbootdemo.model.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchUserDto {
  @Size(min = 2, max = 100)
  private String firstName;
  @Size(min = 2, max = 100)
  private String lastName;
  @Past
  private LocalDate birthDate;
  @PositiveOrZero
  private Double salary;
  @Email
  private String email;
  @Min(1)
  private Integer age;
}
