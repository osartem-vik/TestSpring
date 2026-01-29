package org.codegym.springbootdemo.service.mapper;

import org.codegym.springbootdemo.model.dto.PatchUserDto;
import org.codegym.springbootdemo.model.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patch(PatchUserDto user, @MappingTarget User userDto);
}
