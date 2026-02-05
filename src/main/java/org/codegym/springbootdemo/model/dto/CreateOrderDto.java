package org.codegym.springbootdemo.model.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderDto {

  @NotNull
  private Long userId;

  @NotEmpty
  @Valid
  private List<OrderItemDto> items;
}
