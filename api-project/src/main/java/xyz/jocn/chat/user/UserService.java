package xyz.jocn.chat.user;

import static xyz.jocn.chat.common.enums.ResourceType.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repo.user.UserRepository;

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
				throw new ResourceAlreadyExistException(USER);
			});

		userRepository.save(UserEntity.builder()
			.email(userSignUpRequestDto.getEmail())
			.name(userSignUpRequestDto.getName())
			.password(passwordEncoder.encode(userSignUpRequestDto.getPassword()))
			.build());
	}
}
