package io.github.kubaj12.personal_task_manager_api.mapper;

import static org.junit.jupiter.api.Assertions.*;

import io.github.kubaj12.personal_task_manager_api.dto.RegistrationRequestDto;
import io.github.kubaj12.personal_task_manager_api.dto.RegistrationResponseDto;
import io.github.kubaj12.personal_task_manager_api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

  @Mock private PasswordEncoder passwordEncoder;
  private UserMapper userMapper;

  @BeforeEach
  public void setUp() {
    UserMapperImpl userMapperImpl = new UserMapperImpl();
    userMapperImpl.setPasswordEncoder(passwordEncoder);
    userMapper = userMapperImpl;
  }

  @Test
  void registrationRequestDtoToUser() {
    Mockito.when(passwordEncoder.encode(Mockito.anyString()))
            .thenAnswer(
                    invocationOnMock -> {
                      String rawPassword = invocationOnMock.getArgument(0);
                      return "hashed:" + rawPassword;
                    });
    RegistrationRequestDto registrationRequestDto =
        new RegistrationRequestDto("test@test.com", "test");
    User user = userMapper.registrationRequestDtoToUser(registrationRequestDto);
    assertEquals("test@test.com", user.getEmail());
    assertEquals("hashed:test", user.getPasswordHash());
  }

  @Test
  void userToRegistrationResponseDto() {
    User user = User.builder().email("test@test.com").id(1L)
            .passwordHash("hashed:test")
            .build();
    RegistrationResponseDto registrationResponseDto = userMapper.userToRegistrationResponseDto(user);
    assertEquals(1L, registrationResponseDto.id());
    assertEquals("test@test.com", registrationResponseDto.email());
  }
}
