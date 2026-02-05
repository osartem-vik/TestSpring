package org.codegym.springbootdemo.web;

import java.util.List;
import org.codegym.springbootdemo.model.dto.GeneralProductInfoDto;
import org.codegym.springbootdemo.model.dto.PatchProductDto;
import org.codegym.springbootdemo.model.dto.ProductDto;
import org.codegym.springbootdemo.model.dto.ProductInfoDto;
import org.codegym.springbootdemo.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductDto createProduct(@Validated @RequestBody ProductDto productDto) {
    return productService.create(productDto);
  }

  @PutMapping("/{id}")
  public ProductDto updateProduct(@PathVariable Long id,
      @Validated @RequestBody ProductDto productDto) {
    return productService.update(productDto, id);
  }

  @PatchMapping("/{id}")
  public PatchProductDto patchProduct(@PathVariable Long id,
      @Validated @RequestBody PatchProductDto productDto) {
    return productService.patch(productDto, id);
  }

  @DeleteMapping("/{id}")
  public void deleteProduct(@PathVariable Long id) {
    productService.delete(id);
  }

  @GetMapping
  public List<GeneralProductInfoDto> findAll() {
    return productService.getProducts();
  }

  @GetMapping(path = "/{id}")
  public ProductInfoDto getById(@PathVariable Long id) {
    return productService.getProduct(id);
  }
}
