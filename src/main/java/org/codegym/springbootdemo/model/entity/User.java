package org.codegym.springbootdemo.model.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "fisrt_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastName;
  private String password;
  private LocalDate birthDate;
  private Double salary;
  @Column(unique = true)
  private String email;
  private Integer age;
}
