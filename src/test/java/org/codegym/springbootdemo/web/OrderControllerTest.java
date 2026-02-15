package org.codegym.springbootdemo.web;

import org.codegym.springbootdemo.model.dto.*;
import org.codegym.springbootdemo.model.entity.OrderStatus;
import org.codegym.springbootdemo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_shouldReturn201AndOrderInfoDto_whenValidRequest() throws Exception {
        CreateOrderDto createDto = new CreateOrderDto();
        createDto.setUserId(42L);

        OrderItemDto item = new OrderItemDto();
        item.setProductId(1L);
        item.setQuantity(2);
        createDto.setItems(List.of(item));

        OrderInfoDto responseDto = new OrderInfoDto();
        responseDto.setId(100L);
        responseDto.setUserId(42L);
        responseDto.setStatus(OrderStatus.PENDING);
        responseDto.setTotalPrice(BigDecimal.valueOf(299.98));
        responseDto.setCreatedAt(LocalDateTime.now());

        when(orderService.create(any(CreateOrderDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.userId").value(42))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalPrice").value(299.98));

        verify(orderService).create(any(CreateOrderDto.class));
    }

    @Test
    void getOrderById_shouldReturn200AndOrderInfoDto() throws Exception {
        OrderInfoDto dto = new OrderInfoDto();
        dto.setId(15L);
        dto.setUserId(7L);
        dto.setStatus(OrderStatus.CONFIRMED);
        dto.setTotalPrice(BigDecimal.valueOf(1499.00));

        when(orderService.getOrder(15L)).thenReturn(dto);

        mockMvc.perform(get("/api/orders/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.userId").value(7))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.totalPrice").value(1499.00));

        verify(orderService).getOrder(15L);
    }

    @Test
    void getOrdersByUserId_shouldReturn200AndListOfOrders() throws Exception {
        GeneralOrderInfoDto order1 = new GeneralOrderInfoDto();
        order1.setId(101L);
        order1.setStatus(OrderStatus.PENDING);
        order1.setTotalPrice(BigDecimal.valueOf(450.00));

        GeneralOrderInfoDto order2 = new GeneralOrderInfoDto();
        order2.setId(102L);
        order2.setStatus(OrderStatus.SHIPPED);
        order2.setTotalPrice(BigDecimal.valueOf(1200.00));

        when(orderService.getOrdersByUser(55L)).thenReturn(List.of(order1, order2));

        mockMvc.perform(get("/api/orders/user/55"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[1].status").value("SHIPPED"));

        verify(orderService).getOrdersByUser(55L);
    }

    @Test
    void patchOrderShouldReturn200AndUpdatedPatchDtoWhenStatusChanged() throws Exception {

        PatchOrderDto patchDto = new PatchOrderDto();
        patchDto.setStatus(OrderStatus.CONFIRMED);

        PatchOrderDto responseDto = new PatchOrderDto();
        responseDto.setStatus(OrderStatus.CONFIRMED);

        when(orderService.patch(any(PatchOrderDto.class), eq(88L)))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/api/orders/88")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(orderService).patch(any(PatchOrderDto.class), eq(88L));
    }

    @Test
    void deleteOrder_shouldReturn204_whenOrderExists() throws Exception {
        mockMvc.perform(delete("/api/orders/777"))
                .andExpect(status().isNoContent());

        verify(orderService).delete(777L);
    }

    @Test
    void getAllOrders_shouldReturn200AndListOfGeneralOrders() throws Exception {
        GeneralOrderInfoDto order = new GeneralOrderInfoDto();
        order.setId(300L);
        order.setStatus(OrderStatus.DELIVERED);
        order.setTotalPrice(BigDecimal.valueOf(89.99));

        when(orderService.getOrders()).thenReturn(List.of(order));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(300))
                .andExpect(jsonPath("$[0].status").value("DELIVERED"));

        verify(orderService).getOrders();
    }
}