package org.codegym.springbootdemo.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.codegym.springbootdemo.model.entity.OrderStatus;
import lombok.Data;

@Data
public class OrderInfoDto {

  private long id;
  private Long userId;
  private String userFirstName;
  private String userLastName;
  private OrderStatus status;
  private BigDecimal totalPrice;
  private LocalDateTime createdAt;
  private List<OrderItemInfoDto> items;
}
