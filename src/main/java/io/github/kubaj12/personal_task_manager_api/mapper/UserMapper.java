package io.github.kubaj12.personal_task_manager_api.mapper;

import io.github.kubaj12.personal_task_manager_api.dto.RegistrationRequestDto;
import io.github.kubaj12.personal_task_manager_api.dto.RegistrationResponseDto;
import io.github.kubaj12.personal_task_manager_api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
  @Autowired
  PasswordEncoder passwordEncoder;

  // setter for unit tests
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Mapping(
      target = "passwordHash",
      expression = "java(passwordEncoder.encode(registrationRequestDto.rawPassword()))")
  public abstract User registrationRequestDtoToUser(RegistrationRequestDto registrationRequestDto);

  public abstract RegistrationResponseDto userToRegistrationResponseDto(User user);
}
