package org.codegym.springbootdemo.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.codegym.springbootdemo.model.dto.ProductDto;
import org.codegym.springbootdemo.model.dto.ProductInfoDto;
import org.codegym.springbootdemo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct_shouldReturn201() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setName("Monitor");
        dto.setPrice(BigDecimal.valueOf(299.99));
        dto.setQuantity(15);

        ProductDto saved = new ProductDto();
        saved.setId(42L);
        saved.setName("Monitor");

        when(productService.create(any(ProductDto.class))).thenReturn(saved);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.name").value("Monitor"));
    }

    @Test
    void getProductById_shouldReturnOk() throws Exception {
        ProductInfoDto info = new ProductInfoDto();
        info.setId(7L);
        info.setName("Headphones");

        when(productService.getProduct(7L)).thenReturn(info);

        mockMvc.perform(get("/api/products/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Headphones"));
    }
}
