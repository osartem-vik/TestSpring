package org.codegym.springbootdemo.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.codegym.springbootdemo.model.dto.UserDto;
import org.codegym.springbootdemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createUserShouldReturn201WhenValidRequest() throws Exception {
    UserDto userDto = createUserDto("john@gmail.com");
    when(userService.create(any(UserDto.class))).thenReturn(userDto);

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value("john@gmail.com"))
        .andExpect(jsonPath("$.firstName").value("John"));
  }

  private UserDto createUserDto(String email) {
    UserDto userDto = new UserDto();
    userDto.setFirstName("John");
    userDto.setLastName("Doe");
    userDto.setEmail(email);
    return userDto;
  }
}
