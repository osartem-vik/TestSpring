package org.codegym.springbootdemo.service;

import java.util.List;
import org.codegym.springbootdemo.exception.UserAlreadyExistException;
import org.codegym.springbootdemo.exception.UserNotFoundException;
import org.codegym.springbootdemo.model.dto.GeneralUserInfoDto;
import org.codegym.springbootdemo.model.dto.PatchUserDto;
import org.codegym.springbootdemo.model.dto.UserDto;
import org.codegym.springbootdemo.model.dto.UserInfoDto;
import org.codegym.springbootdemo.model.entity.User;
import org.codegym.springbootdemo.repository.UserRepository;
import org.codegym.springbootdemo.service.mapper.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final UserMapper userMapper;

  public UserDto create(UserDto userDto) {

    checkEmail(userDto.getEmail());
    User saveUsed = userRepository.save(modelMapper.map(userDto, User.class));

    return modelMapper.map(saveUsed, UserDto.class);
  }

  public UserDto update(UserDto userDto, Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with %d id not found".formatted(id)));
    if (userDto.getEmail() != null) {
      checkEmail(userDto.getEmail());
    }

    modelMapper.map(userDto, user);

    return modelMapper.map(userRepository.save(user), UserDto.class);

  }

  public PatchUserDto patch(PatchUserDto userDto, Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with %d id not found".formatted(id)));
    if (userDto.getEmail() != null) {
      checkEmail(userDto.getEmail());
    }
    userMapper.patch(userDto, user);
    return modelMapper.map(userRepository.save(user), PatchUserDto.class);
  }

  public void delete(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with %d id not found".formatted(id)));
    userRepository.delete(user);
  }

  public List<GeneralUserInfoDto> getUsers() {
    return userRepository.findAll()
        .stream()
        .map(user -> modelMapper.map(user, GeneralUserInfoDto.class))
        .toList();
  }

  public UserInfoDto getUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User with %d id not found".formatted(id)));

    return modelMapper.map(user, UserInfoDto.class);
  }

  public List<UserInfoDto> userSearch(Integer minAge, Integer maxAge, String email) {
    return userRepository.searchUsers(minAge, maxAge, email)
        .stream()
        .map(u -> modelMapper.map(u, UserInfoDto.class))
        .toList();
  }

  private void checkEmail(String email) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new UserAlreadyExistException("Email '%s' already exist".formatted(email));
    }
  }
}
