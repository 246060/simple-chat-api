package xyz.jocn.chat.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.AlreadyExistUserException;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public void signUp(UserSignUpRequestDto userSignUpRequestDto) {
		userRepository
			.findByEmail(userSignUpRequestDto.getEmail())
			.ifPresent(entity -> {
				throw new AlreadyExistUserException();
			});

		userRepository.save(UserEntity.builder()
			.email(userSignUpRequestDto.getEmail())
			.name(userSignUpRequestDto.getName())
			.password(passwordEncoder.encode(userSignUpRequestDto.getPassword()))
			.build());
	}
}
