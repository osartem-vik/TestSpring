package org.codegym.springbootdemo.service;

import org.codegym.springbootdemo.exception.ProductNotFoundException;
import org.codegym.springbootdemo.model.dto.PatchProductDto;
import org.codegym.springbootdemo.model.dto.ProductDto;
import org.codegym.springbootdemo.model.entity.Product;
import org.codegym.springbootdemo.repository.ProductRepository;
import org.codegym.springbootdemo.service.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void create_shouldSaveAndReturnMappedDto() {
        ProductDto inputDto = new ProductDto();
        inputDto.setName("Laptop");
        inputDto.setPrice(BigDecimal.valueOf(999.99));
        inputDto.setQuantity(10);

        Product entity = new Product();
        entity.setId(1L);

        Product savedEntity = new Product();
        savedEntity.setId(1L);
        savedEntity.setName("Laptop");

        ProductDto resultDto = new ProductDto();
        resultDto.setId(1L);

        when(modelMapper.map(inputDto, Product.class)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, ProductDto.class)).thenReturn(resultDto);

        ProductDto result = productService.create(inputDto);

        assertThat(result.getId()).isEqualTo(1L);
        verify(productRepository).save(entity);
    }

    @Test
    void getProduct_shouldThrowWhenNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProduct(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void patch_shouldUpdateFieldsAndReturnDto() {
        Product existing = new Product();
        existing.setId(5L);
        existing.setName("Old Name");
        existing.setPrice(BigDecimal.TEN);

        PatchProductDto patch = new PatchProductDto();
        patch.setName("New Name");

        Product updated = new Product();
        updated.setId(5L);
        updated.setName("New Name");

        PatchProductDto resultDto = new PatchProductDto();
        resultDto.setName("New Name");

        when(productRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(updated);
        when(modelMapper.map(updated, PatchProductDto.class)).thenReturn(resultDto);

        PatchProductDto result = productService.patch(patch, 5L);

        assertThat(result.getName()).isEqualTo("New Name");
        verify(productMapper).patch(patch, existing);
        verify(productRepository).save(existing);
    }

    @Test
    void delete_shouldCallRepositoryWhenExists() {
        Product product = new Product();
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        productService.delete(10L);

        verify(productRepository).delete(product);
    }
}