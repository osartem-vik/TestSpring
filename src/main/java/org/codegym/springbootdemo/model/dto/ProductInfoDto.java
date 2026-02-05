package org.codegym.springbootdemo.model.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductInfoDto {

  private long id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer quantity;
}
