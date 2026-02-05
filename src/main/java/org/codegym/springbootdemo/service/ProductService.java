package org.codegym.springbootdemo.service;

import java.util.List;
import org.codegym.springbootdemo.exception.ProductNotFoundException;
import org.codegym.springbootdemo.model.dto.GeneralProductInfoDto;
import org.codegym.springbootdemo.model.dto.PatchProductDto;
import org.codegym.springbootdemo.model.dto.ProductDto;
import org.codegym.springbootdemo.model.dto.ProductInfoDto;
import org.codegym.springbootdemo.model.entity.Product;
import org.codegym.springbootdemo.repository.ProductRepository;
import org.codegym.springbootdemo.service.mapper.ProductMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {

  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;
  private final ProductMapper productMapper;

  public ProductDto create(ProductDto productDto) {
    Product saved = productRepository.save(modelMapper.map(productDto, Product.class));
    return modelMapper.map(saved, ProductDto.class);
  }

  public ProductDto update(ProductDto productDto, Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product with %d id not found".formatted(id)));
    modelMapper.map(productDto, product);
    return modelMapper.map(productRepository.save(product), ProductDto.class);
  }

  public PatchProductDto patch(PatchProductDto productDto, Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product with %d id not found".formatted(id)));
    productMapper.patch(productDto, product);
    return modelMapper.map(productRepository.save(product), PatchProductDto.class);
  }

  public void delete(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product with %d id not found".formatted(id)));
    productRepository.delete(product);
  }

  public List<GeneralProductInfoDto> getProducts() {
    return productRepository.findAll()
        .stream()
        .map(product -> modelMapper.map(product, GeneralProductInfoDto.class))
        .toList();
  }

  public ProductInfoDto getProduct(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product with %d id not found".formatted(id)));
    return modelMapper.map(product, ProductInfoDto.class);
  }
}
