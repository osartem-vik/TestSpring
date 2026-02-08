package org.codegym.springbootdemo.web;

import org.codegym.springbootdemo.model.dto.PatchProductDto;
import org.codegym.springbootdemo.model.dto.ProductDto;
import org.codegym.springbootdemo.model.entity.Product;
import org.codegym.springbootdemo.repository.ProductRepository;
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
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void createProduct_shouldPersistAndReturn201() throws Exception {
        ProductDto request = new ProductDto();
        request.setName("Bluetooth Speaker");
        request.setPrice(BigDecimal.valueOf(89.99));
        request.setQuantity(25);
        request.setDescription("Portable speaker with good bass");

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bluetooth Speaker"))
                .andExpect(jsonPath("$.price").value(89.99))
                .andExpect(jsonPath("$.quantity").value(25))
                .andExpect(jsonPath("$.id").isNumber());

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Bluetooth Speaker");
        assertThat(products.get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(89.99));
    }

    @Test
    void getAllProducts_shouldReturnSavedProducts() throws Exception {
        Product p1 = new Product();
        p1.setName("USB-C Cable");
        p1.setPrice(BigDecimal.valueOf(12.90));
        p1.setQuantity(100);

        Product p2 = new Product();
        p2.setName("Wireless Charger");
        p2.setPrice(BigDecimal.valueOf(34.50));
        p2.setQuantity(40);

        productRepository.saveAll(List.of(p1, p2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("USB-C Cable"))
                .andExpect(jsonPath("$[0].price").value(12.90))
                .andExpect(jsonPath("$[1].name").value("Wireless Charger"));
    }

    @Test
    void getProductById_shouldReturnCorrectProduct() throws Exception {
        Product product = new Product();
        product.setName("4K Monitor");
        product.setPrice(BigDecimal.valueOf(349.99));
        product.setQuantity(12);
        product = productRepository.save(product);

        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("4K Monitor"))
                .andExpect(jsonPath("$.price").value(349.99))
                .andExpect(jsonPath("$.quantity").value(12))
                .andExpect(jsonPath("$.id").value(product.getId()));
    }

    @Test
    void getProductById_shouldReturn404_whenNotFound() throws Exception {
        mockMvc.perform(get("/api/products/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchProduct_shouldUpdateNameAndQuantity() throws Exception {
        Product existing = new Product();
        existing.setName("Old Mouse");
        existing.setPrice(BigDecimal.valueOf(19.99));
        existing.setQuantity(50);
        existing = productRepository.save(existing);

        PatchProductDto patch = new PatchProductDto();
        patch.setName("Gaming Mouse");
        patch.setQuantity(35);

        mockMvc.perform(patch("/api/products/" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gaming Mouse"))
                .andExpect(jsonPath("$.quantity").value(35))
                .andExpect(jsonPath("$.price").value(19.99));

        Product updated = productRepository.findById(existing.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Gaming Mouse");
        assertThat(updated.getQuantity()).isEqualTo(35);
    }

    @Test
    void deleteProduct_shouldRemoveProductAndReturn204() throws Exception {
        Product product = new Product();
        product.setName("To Delete");
        product.setPrice(BigDecimal.TEN);
        product.setQuantity(10);
        product = productRepository.save(product);

        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(status().isNoContent());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }

    @Test
    void createProduct_shouldReturn400_whenNameIsEmpty() throws Exception {
        ProductDto invalid = new ProductDto();
        invalid.setName("");           // нарушение @NotBlank
        invalid.setPrice(BigDecimal.valueOf(10));
        invalid.setQuantity(5);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}