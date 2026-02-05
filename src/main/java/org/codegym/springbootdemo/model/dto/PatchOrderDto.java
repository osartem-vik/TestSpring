package org.codegym.springbootdemo.model.dto;

import org.codegym.springbootdemo.model.entity.OrderStatus;
import lombok.Data;

@Data
public class PatchOrderDto {

  private OrderStatus status;
}
