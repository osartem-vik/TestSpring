package org.codegym.springbootdemo.web;

import java.util.List;
import org.codegym.springbootdemo.model.dto.CreateOrderDto;
import org.codegym.springbootdemo.model.dto.GeneralOrderInfoDto;
import org.codegym.springbootdemo.model.dto.OrderInfoDto;
import org.codegym.springbootdemo.model.dto.PatchOrderDto;
import org.codegym.springbootdemo.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderInfoDto createOrder(@Validated @RequestBody CreateOrderDto createOrderDto) {
    return orderService.create(createOrderDto);
  }

  @PatchMapping("/{id}")
  public PatchOrderDto patchOrder(@PathVariable Long id,
      @Validated @RequestBody PatchOrderDto patchOrderDto) {
    return orderService.patch(patchOrderDto, id);
  }

  @DeleteMapping("/{id}")
  public void deleteOrder(@PathVariable Long id) {
    orderService.delete(id);
  }

  @GetMapping
  public List<GeneralOrderInfoDto> findAll() {
    return orderService.getOrders();
  }

  @GetMapping(path = "/{id}")
  public OrderInfoDto getById(@PathVariable Long id) {
    return orderService.getOrder(id);
  }

  @GetMapping(path = "/user/{userId}")
  public List<GeneralOrderInfoDto> getByUserId(@PathVariable Long userId) {
    return orderService.getOrdersByUser(userId);
  }
}
