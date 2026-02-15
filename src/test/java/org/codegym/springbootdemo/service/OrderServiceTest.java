package org.codegym.springbootdemo.service;

import org.codegym.springbootdemo.exception.InsufficientStockException;
import org.codegym.springbootdemo.exception.ProductNotFoundException;
import org.codegym.springbootdemo.model.dto.CreateOrderDto;
import org.codegym.springbootdemo.model.dto.OrderItemDto;
import org.codegym.springbootdemo.model.entity.Order;
import org.codegym.springbootdemo.model.entity.OrderItem;
import org.codegym.springbootdemo.model.entity.Product;
import org.codegym.springbootdemo.model.entity.User;
import org.codegym.springbootdemo.repository.OrderRepository;
import org.codegym.springbootdemo.repository.ProductRepository;
import org.codegym.springbootdemo.repository.UserRepository;
import org.codegym.springbootdemo.service.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    void create_shouldDecreaseStockAndSaveOrder() {
        User user = new User();
        user.setId(100L);

        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Mouse");
        p1.setPrice(BigDecimal.valueOf(25));
        p1.setQuantity(10);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Keyboard");
        p2.setPrice(BigDecimal.valueOf(80));
        p2.setQuantity(5);

        CreateOrderDto dto = new CreateOrderDto();
        dto.setUserId(100L);

        OrderItemDto item1 = new OrderItemDto();
        item1.setProductId(1L);
        item1.setQuantity(3);

        OrderItemDto item2 = new OrderItemDto();
        item2.setProductId(2L);
        item2.setQuantity(2);

        dto.setItems(List.of(item1, item2));

        when(userRepository.findById(100L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(productRepository.findById(2L)).thenReturn(Optional.of(p2));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        orderService.create(dto);
        verify(productRepository, times(2)).save(productCaptor.capture());

        List<Product> savedProducts = productCaptor.getAllValues();
        assertThat(savedProducts)
                .extracting(Product::getQuantity)
                .containsExactly(7, 3);

        verify(orderRepository).save(argThat(order -> {
            assertThat(order.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(25*3 + 80*2));
            assertThat(order.getItems()).hasSize(2);
            return true;
        }));
    }

    @Test
    void createShouldThrowWhenInsufficientStock() {
        User user = new User();
        Product product = new Product();
        product.setId(1L);
        product.setName("Phone");
        product.setQuantity(2);
        product.setPrice(BigDecimal.TEN);

        CreateOrderDto dto = new CreateOrderDto();
        dto.setUserId(100L);
        OrderItemDto item = new OrderItemDto();
        item.setProductId(1L);
        item.setQuantity(5);
        dto.setItems(List.of(item));

        when(userRepository.findById(100L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> orderService.create(dto))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Insufficient stock for product 'Phone'");

        verify(productRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }
}
