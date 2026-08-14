package io.github.kubaj12.personal_task_manager_api.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.github.kubaj12.personal_task_manager_api.dto.RegistrationRequestDto;
import io.github.kubaj12.personal_task_manager_api.dto.RegistrationResponseDto;
import io.github.kubaj12.personal_task_manager_api.entity.User;
import io.github.kubaj12.personal_task_manager_api.exception.EmailAlreadyExistsException;
import io.github.kubaj12.personal_task_manager_api.mapper.UserMapper;
import io.github.kubaj12.personal_task_manager_api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationServiceTest {
        @Mock
        UserRepository userRepository;

        @Mock
        UserMapper userMapper;

        @InjectMocks
        UserRegistrationService userRegistrationService;

        @Test
        void shouldRegisterUserWhenEmailIsAvailable() {
                RegistrationRequestDto requestDto = new RegistrationRequestDto("test@test.com", "rawPassword");
                when(userRepository.existsByEmail("test@test.com")).thenReturn(false);

                User user = User.builder().email("test@test.com").passwordHash("passwordHash")
                                .build();

                when(userMapper.registrationRequestDtoToUser(requestDto))
                                .thenReturn(user);
                when(userRepository.save(user)).thenReturn(user);

                RegistrationResponseDto responseDtoReturn = new RegistrationResponseDto(1, "test@test.com");

                when(userMapper.userToRegistrationResponseDto(user))
                                .thenReturn(responseDtoReturn);

                RegistrationResponseDto responseDto = userRegistrationService.registerUser(requestDto);

                assertNotNull(responseDto);

                assertEquals("test@test.com", responseDto.email());

                verify(userMapper).registrationRequestDtoToUser(requestDto);
                verify(userRepository).save(user);
        }

        @Test
        void shouldNotRegisterUserWhenEmailIsNotAvailable() {
                RegistrationRequestDto requestDto = new RegistrationRequestDto("test@test.com", "rawPassword");
                when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

                EmailAlreadyExistsException result = assertThrows(EmailAlreadyExistsException.class,
                                () -> userRegistrationService.registerUser(requestDto));

                assertEquals("test@test.com", result.getEmail());

                verify(userMapper, never())
                                .registrationRequestDtoToUser(any(RegistrationRequestDto.class));
                verify(userRepository, never()).save(any(User.class));
        }
}
