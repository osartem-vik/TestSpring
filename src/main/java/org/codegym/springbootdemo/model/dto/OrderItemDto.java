package org.codegym.springbootdemo.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemDto {

  @NotNull
  private Long productId;

  @NotNull
  @Positive
  private Integer quantity;
}
