package org.codegym.springbootdemo.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.codegym.springbootdemo.model.entity.OrderStatus;
import lombok.Data;

@Data
public class GeneralOrderInfoDto {

  private long id;
  private OrderStatus status;
  private BigDecimal totalPrice;
  private LocalDateTime createdAt;
}
