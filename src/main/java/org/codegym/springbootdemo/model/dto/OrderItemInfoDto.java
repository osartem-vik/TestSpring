package org.codegym.springbootdemo.model.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemInfoDto {

  private long id;
  private Long productId;
  private String productName;
  private Integer quantity;
  private BigDecimal price;
}
