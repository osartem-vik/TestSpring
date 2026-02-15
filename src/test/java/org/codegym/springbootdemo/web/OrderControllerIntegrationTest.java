package org.codegym.springbootdemo.web;

import org.codegym.springbootdemo.model.dto.CreateOrderDto;
import org.codegym.springbootdemo.model.dto.OrderItemDto;
import org.codegym.springbootdemo.model.entity.Order;
import org.codegym.springbootdemo.model.entity.Product;
import org.codegym.springbootdemo.model.entity.User;
import org.codegym.springbootdemo.repository.OrderRepository;
import org.codegym.springbootdemo.repository.ProductRepository;
import org.codegym.springbootdemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void clean() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createOrder_shouldSaveOrderAndDecreaseStock() throws Exception {
        User user = new User();
        user.setFirstName("Anna");
        user.setEmail("anna@test.com");
        user = userRepository.save(user);

        Product p = new Product();
        p.setName("USB-C Cable");
        p.setPrice(BigDecimal.valueOf(12.50));
        p.setQuantity(20);
        p = productRepository.save(p);

        CreateOrderDto dto = new CreateOrderDto();
        dto.setUserId(user.getId());

        OrderItemDto item = new OrderItemDto();
        item.setProductId(p.getId());
        item.setQuantity(8);
        dto.setItems(List.of(item));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(100.00))
                .andExpect(jsonPath("$.items[0].quantity").value(8));

        Product updated = productRepository.findById(p.getId()).orElseThrow();
        assertThat(updated.getQuantity()).isEqualTo(12);
    }

    @Test
    void createOrder_shouldReturn400_whenStockNotEnough() throws Exception {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test.stock@example.com");
        user.setAge(25);
        user = userRepository.save(user);

        Product product = new Product();
        product.setName("Wireless Mouse");
        product.setPrice(BigDecimal.valueOf(29.99));
        product.setQuantity(3);
        product = productRepository.save(product);

        CreateOrderDto orderDto = new CreateOrderDto();
        orderDto.setUserId(user.getId());

        OrderItemDto item = new OrderItemDto();
        item.setProductId(product.getId());
        item.setQuantity(5);

        orderDto.setItems(List.of(item));


        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(containsString("Insufficient stock for product")))
                .andExpect(jsonPath("$.message")
                        .value(containsString("Wireless Mouse")))
                .andExpect(jsonPath("$.message")
                        .value(containsString("Available: 3")))
                .andExpect(jsonPath("$.message")
                        .value(containsString("Requested: 5")));

        Product productAfterAttempt = productRepository.findById(product.getId()).orElseThrow();
        assertThat(productAfterAttempt.getQuantity()).isEqualTo(3);

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).isEmpty();
    }
}
