package org.codegym.springbootdemo.service.mapper;

import org.codegym.springbootdemo.model.dto.PatchOrderDto;
import org.codegym.springbootdemo.model.entity.Order;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patch(PatchOrderDto source, @MappingTarget Order target);
}
