package org.codegym.springbootdemo.model.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchProductDto {

  @Size(min = 2, max = 255)
  private String name;

  @Size(max = 1000)
  private String description;

  @Positive
  private BigDecimal price;

  @PositiveOrZero
  private Integer quantity;
}
