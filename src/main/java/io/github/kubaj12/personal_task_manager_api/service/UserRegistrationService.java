package io.github.kubaj12.personal_task_manager_api.service;

import io.github.kubaj12.personal_task_manager_api.dto.RegistrationRequestDto;
import io.github.kubaj12.personal_task_manager_api.dto.RegistrationResponseDto;
import io.github.kubaj12.personal_task_manager_api.entity.User;
import io.github.kubaj12.personal_task_manager_api.exception.EmailAlreadyExistsException;
import io.github.kubaj12.personal_task_manager_api.mapper.UserMapper;
import io.github.kubaj12.personal_task_manager_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public RegistrationResponseDto registerUser(RegistrationRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.email())) {
            throw new EmailAlreadyExistsException(requestDto.email());
        }

        User user = userMapper.registrationRequestDtoToUser(requestDto);
        userRepository.save(user);

        return userMapper.userToRegistrationResponseDto(user);
    }
}
