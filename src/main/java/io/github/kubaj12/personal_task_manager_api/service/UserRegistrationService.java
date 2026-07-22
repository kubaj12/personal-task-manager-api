package io.github.kubaj12.personal_task_manager_api.service;

import io.github.kubaj12.personal_task_manager_api.dto.RegistrationRequestDto;
import io.github.kubaj12.personal_task_manager_api.dto.RegistrationResponseDto;
import io.github.kubaj12.personal_task_manager_api.entity.User;
import io.github.kubaj12.personal_task_manager_api.mapper.UserMapper;
import io.github.kubaj12.personal_task_manager_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public RegistrationResponseDto registerUser(RegistrationRequestDto requestDto) {
        User user = userMapper.registrationRequestDtoToUser(requestDto);

        return userMapper.userToRegistrationResponseDto(user);
    }
}
