package org.codegym.springbootdemo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.codegym.springbootdemo.exception.InsufficientStockException;
import org.codegym.springbootdemo.exception.OrderNotFoundException;
import org.codegym.springbootdemo.exception.ProductNotFoundException;
import org.codegym.springbootdemo.exception.UserNotFoundException;
import org.codegym.springbootdemo.model.dto.CreateOrderDto;
import org.codegym.springbootdemo.model.dto.GeneralOrderInfoDto;
import org.codegym.springbootdemo.model.dto.OrderInfoDto;
import org.codegym.springbootdemo.model.dto.OrderItemDto;
import org.codegym.springbootdemo.model.dto.OrderItemInfoDto;
import org.codegym.springbootdemo.model.dto.PatchOrderDto;
import org.codegym.springbootdemo.model.entity.Order;
import org.codegym.springbootdemo.model.entity.OrderItem;
import org.codegym.springbootdemo.model.entity.OrderStatus;
import org.codegym.springbootdemo.model.entity.Product;
import org.codegym.springbootdemo.model.entity.User;
import org.codegym.springbootdemo.repository.OrderRepository;
import org.codegym.springbootdemo.repository.ProductRepository;
import org.codegym.springbootdemo.repository.UserRepository;
import org.codegym.springbootdemo.service.mapper.OrderMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;
  private final OrderMapper orderMapper;

  @Transactional
  public OrderInfoDto create(CreateOrderDto createOrderDto) {
    User user = userRepository.findById(createOrderDto.getUserId())
        .orElseThrow(() -> new UserNotFoundException(
            "User with %d id not found".formatted(createOrderDto.getUserId())));

    Order order = new Order();
    order.setUser(user);
    order.setStatus(OrderStatus.PENDING);
    order.setCreatedAt(LocalDateTime.now());

    List<OrderItem> orderItems = new ArrayList<>();
    BigDecimal totalPrice = BigDecimal.ZERO;

    for (OrderItemDto itemDto : createOrderDto.getItems()) {
      Product product = productRepository.findById(itemDto.getProductId())
          .orElseThrow(() -> new ProductNotFoundException(
              "Product with %d id not found".formatted(itemDto.getProductId())));

      if (product.getQuantity() < itemDto.getQuantity()) {
        throw new InsufficientStockException(
            "Insufficient stock for product '%s'. Available: %d, Requested: %d"
                .formatted(product.getName(), product.getQuantity(), itemDto.getQuantity()));
      }

      product.setQuantity(product.getQuantity() - itemDto.getQuantity());
      productRepository.save(product);

      OrderItem orderItem = new OrderItem();
      orderItem.setOrder(order);
      orderItem.setProduct(product);
      orderItem.setQuantity(itemDto.getQuantity());
      orderItem.setPrice(product.getPrice());

      orderItems.add(orderItem);

      totalPrice = totalPrice.add(
          product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
    }

    order.setItems(orderItems);
    order.setTotalPrice(totalPrice);

    Order savedOrder = orderRepository.save(order);
    return mapToOrderInfoDto(savedOrder);
  }

  public PatchOrderDto patch(PatchOrderDto patchOrderDto, Long id) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(
            "Order with %d id not found".formatted(id)));
    orderMapper.patch(patchOrderDto, order);
    return modelMapper.map(orderRepository.save(order), PatchOrderDto.class);
  }

  public void delete(Long id) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(
            "Order with %d id not found".formatted(id)));
    orderRepository.delete(order);
  }

  public List<GeneralOrderInfoDto> getOrders() {
    return orderRepository.findAll()
        .stream()
        .map(order -> modelMapper.map(order, GeneralOrderInfoDto.class))
        .toList();
  }

  public OrderInfoDto getOrder(Long id) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(
            "Order with %d id not found".formatted(id)));
    return mapToOrderInfoDto(order);
  }

  public List<GeneralOrderInfoDto> getOrdersByUser(Long userId) {
    userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(
            "User with %d id not found".formatted(userId)));

    return orderRepository.findByUserId(userId)
        .stream()
        .map(order -> modelMapper.map(order, GeneralOrderInfoDto.class))
        .toList();
  }

  private OrderInfoDto mapToOrderInfoDto(Order order) {
    OrderInfoDto dto = new OrderInfoDto();
    dto.setId(order.getId());
    dto.setUserId(order.getUser().getId());
    dto.setUserFirstName(order.getUser().getFirstName());
    dto.setUserLastName(order.getUser().getLastName());
    dto.setStatus(order.getStatus());
    dto.setTotalPrice(order.getTotalPrice());
    dto.setCreatedAt(order.getCreatedAt());

    List<OrderItemInfoDto> itemDtos = order.getItems().stream()
        .map(this::mapToOrderItemInfoDto)
        .toList();
    dto.setItems(itemDtos);

    return dto;
  }

  private OrderItemInfoDto mapToOrderItemInfoDto(OrderItem item) {
    OrderItemInfoDto dto = new OrderItemInfoDto();
    dto.setId(item.getId());
    dto.setProductId(item.getProduct().getId());
    dto.setProductName(item.getProduct().getName());
    dto.setQuantity(item.getQuantity());
    dto.setPrice(item.getPrice());
    return dto;
  }
}
