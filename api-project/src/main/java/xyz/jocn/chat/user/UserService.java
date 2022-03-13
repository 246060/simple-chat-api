package xyz.jocn.chat.user;

import static xyz.jocn.chat.common.enums.ResourceType.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.user.converter.UserConverter;
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repo.user.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserConverter userConverter = UserConverter.INSTANCE;

	public static boolean isNotResourceOwner(Long id1, Long id2) {
		return !String.valueOf(id1).equals(id2);
	}

	@Transactional
	public void signUp(UserSignUpRequestDto userSignUpRequestDto) {

		userRepository
			.findByEmail(userSignUpRequestDto.getEmail())
			.ifPresent(entity -> {
				throw new ResourceAlreadyExistException(USER);
			});

		UserEntity userEntity =
			UserEntity.builder()
				.email(userSignUpRequestDto.getEmail())
				.name(userSignUpRequestDto.getName())
				.password(passwordEncoder.encode(userSignUpRequestDto.getPassword()))
				.build();

		userRepository.save(userEntity);
	}

	public UserDto getUser(Long userId) {
		return userConverter.toDto(
			userRepository
				.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(USER))
		);
	}

	@Transactional
	public void withdrawal(Long userId) {
		userRepository
			.findById(userId)
			.orElseThrow(() -> new ResourceNotFoundException(USER))
			.delete();
	}
}
