package org.codegym.springbootdemo.model.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class GeneralProductInfoDto {

  private long id;
  private String name;
  private BigDecimal price;
}
