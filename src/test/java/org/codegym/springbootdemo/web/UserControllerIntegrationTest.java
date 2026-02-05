package org.codegym.springbootdemo.web;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.codegym.springbootdemo.model.dto.UserDto;
import org.codegym.springbootdemo.model.entity.User;
import org.codegym.springbootdemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
  }

  @Test
  void createUserShouldPersistAndCreate201() throws Exception {
    String email = "test@gmail.com";
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createUserDto(email))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value(email))
        .andExpect(jsonPath("$.firstName").value("John"));

    assertThat(userRepository.findByEmail(email)).isPresent();

  }

  @Test
  void createUserShouldReturn400WhenDuplicateEmail() throws Exception {
    UserDto userDto = createUserDto("test@gmail.com");

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createUserShouldReturn400WhenValidationFails() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setFirstName("J");
    userDto.setLastName("S");
    userDto.setEmail("");

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFindAndReturnAllUsers() throws Exception {
    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("mail@gmail.com");
    user.setAge(18);
    userRepository.save(user);

    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$.[0].firstName").value("John"))
        .andExpect(jsonPath("$.[0].lastName").value("Doe"));
  }

  private UserDto createUserDto(String email) {
    UserDto userDto = new UserDto();
    userDto.setFirstName("John");
    userDto.setLastName("Doe");
    userDto.setEmail(email);
    return userDto;
  }
}
