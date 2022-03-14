package xyz.jocn.chat.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.enums.UserState;
import xyz.jocn.chat.user.repo.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	UserService service;

	@Test
	void signUp() {
		// given
		UserSignUpRequestDto dto = new UserSignUpRequestDto();
		dto.setPassword("password");
		dto.setEmail("test@email.org");

		given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
		given(passwordEncoder.encode(anyString())).willReturn("encoded_password");
		given(userRepository.save(any(UserEntity.class))).willReturn(any(UserEntity.class));

		// when
		service.signUp(dto);

		// then
		then(userRepository).should().findByEmail(anyString());
		then(passwordEncoder).should().encode(anyString());
		then(userRepository).should().save(any(UserEntity.class));
	}

	@Test
	void signUp_ResourceAlreadyExistException() {
		// given
		UserSignUpRequestDto dto = new UserSignUpRequestDto();
		dto.setPassword("password");
		dto.setEmail("test@email.org");

		UserEntity userEntity = UserEntity.builder().id(1L).build();
		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(userEntity));

		// when + then
		assertThatThrownBy(() -> service.signUp(dto))
			.isInstanceOf(ResourceAlreadyExistException.class);
	}

	@Test
	void getUser() {
		// given
		Long userId = 1L;
		UserEntity userEntity = UserEntity.builder().id(userId).build();

		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(userEntity));

		// when
		UserDto dto = service.getUser(userId);
		System.out.println("dto = " + dto);

		// then
		assertThat(dto)
			.extracting(UserDto::getId).as("userId").isEqualTo(userId);
	}

	@Test
	void getUser_ResourceNotFoundException() {
		// given
		Long userId = 1L;

		given(userRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when + then
		assertThatThrownBy(() -> service.getUser(userId))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void withdrawal() {
		// given
		Long userId = 1L;

		UserEntity userEntity =
			UserEntity.builder()
				.id(userId)
				.state(UserState.ACTIVE)
				.build();

		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(userEntity));

		// when
		service.withdrawal(userId);

		// then
		assertThat(userEntity)
			.extracting(UserEntity::getState)
			.isEqualTo(UserState.DELETED);
	}

	@Test
	void withdrawal_ResourceNotFoundException() {
		// given
		Long userId = 1L;

		given(userRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when + then
		assertThatThrownBy(() -> service.withdrawal(userId))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void isNotResourceOwner1() {
		// given
		Long id1 = 1L;
		Long id2 = 2L;

		// when
		boolean isTrue = service.isNotResourceOwner(id1, id2);

		// then
		assertThat(isTrue).isTrue();
	}

	@Test
	void isNotResourceOwner2() {
		// given
		Long id1 = 1L;
		Long id2 = 1L;

		// when
		boolean isFalse = service.isNotResourceOwner(id1, id2);

		// then
		assertThat(isFalse).isFalse();
	}

	@Test
	void isResourceOwner1() {
		// given
		Long id1 = 1L;
		Long id2 = 1L;

		// when
		boolean isTrue = service.isResourceOwner(id1, id2);

		// then
		assertThat(isTrue).isTrue();
	}

	@Test
	void isResourceOwner2() {
		// given
		Long id1 = 1L;
		Long id2 = 2L;

		// when
		boolean isFalse = service.isResourceOwner(id1, id2);

		// then
		assertThat(isFalse).isFalse();
	}

}