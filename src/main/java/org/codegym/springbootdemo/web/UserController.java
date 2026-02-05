package org.codegym.springbootdemo.web;

import java.util.List;
import org.codegym.springbootdemo.model.dto.GeneralUserInfoDto;
import org.codegym.springbootdemo.model.dto.PatchUserDto;
import org.codegym.springbootdemo.model.dto.UserDto;
import org.codegym.springbootdemo.model.dto.UserInfoDto;
import org.codegym.springbootdemo.service.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition
@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto createUser(@Validated @RequestBody UserDto userDto) {
    return userService.create(userDto);
  }

  @PutMapping("/{id}")
  public UserDto updateUser(@PathVariable Long id,
      @Validated @RequestBody UserDto userDto) {
    return userService.update(userDto, id);
  }

  @PatchMapping("/{id}")
  public PatchUserDto patchUser(@PathVariable Long id,
      @Validated @RequestBody PatchUserDto userDto) {
    return userService.patch(userDto, id);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id) {
    userService.delete(id);
  }

  @GetMapping
  public List<GeneralUserInfoDto> findAll() {
    return userService.getUsers();
  }

  @Operation(
      summary = "Get user by id",
      description = "Returns user if exist"
  )

  @ApiResponse(responseCode = "200", description = "User found")
  @ApiResponse(responseCode = "404", description = "User not found.")
  @GetMapping(path = "/{id}")
  public UserInfoDto getById(@PathVariable Long id) {
    return userService.getUser(id);
  }

  @GetMapping(path = "/search")
  public List<UserInfoDto> searchUsers(
      @RequestParam(required = false) Integer minAge,
      @RequestParam(required = false) Integer maxAge,
      @RequestParam(required = false) String email
  ) {
    return userService.userSearch(minAge, maxAge, email);
  }
}
