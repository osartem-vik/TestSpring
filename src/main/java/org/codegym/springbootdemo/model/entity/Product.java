package org.codegym.springbootdemo.model.entity;

import java.math.BigDecimal;
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
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  private String description;

  private BigDecimal price;

  private Integer quantity;
}
