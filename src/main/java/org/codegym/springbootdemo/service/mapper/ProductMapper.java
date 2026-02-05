package org.codegym.springbootdemo.service.mapper;

import org.codegym.springbootdemo.model.dto.PatchProductDto;
import org.codegym.springbootdemo.model.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patch(PatchProductDto source, @MappingTarget Product target);
}
