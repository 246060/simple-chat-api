package xyz.jocn.chat.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.file.FileService;
import xyz.jocn.chat.file.dto.FileDto;
import xyz.jocn.chat.friend.repo.FriendRepository;
import xyz.jocn.chat.participant.repo.ParticipantRepository;
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.dto.UserUpdateRequestDto;
import xyz.jocn.chat.user.enums.UserState;
import xyz.jocn.chat.user.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	UserRepository userRepository;
	@Mock
	FileService fileService;
	@Mock
	PasswordEncoder passwordEncoder;
	@Mock
	ParticipantRepository participantRepository;
	@Mock
	FriendRepository friendRepository;

	@InjectMocks
	UserService service;

	@BeforeEach
	void setUp() {
	}

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
		dto.setEmail("test@email.org");

		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(UserEntity.builder().build()));

		// when + then
		assertThatThrownBy(() -> service.signUp(dto))
			.isInstanceOf(ResourceAlreadyExistException.class);
	}

	@Test
	void fetchMe() {
		// given
		Long userId = 1L;
		UserEntity userEntity = UserEntity.builder().id(userId).build();

		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(userEntity));

		// when
		UserDto dto = service.fetchMe(userId);
		System.out.println("dto = " + dto);

		// then
		assertThat(dto)
			.extracting(UserDto::getId).as("userId").isEqualTo(userId);
	}

	@Test
	void fetchMe_ResourceNotFoundException() {
		// given
		Long userId = 1L;

		given(userRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when + then
		assertThatThrownBy(() -> service.fetchMe(userId))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void exit() {
		// given
		Long userId = 1L;

		UserEntity userEntity =
			UserEntity.builder()
				.id(userId)
				.state(UserState.ACTIVE)
				.build();

		given(participantRepository.findAllByUserId(userId)).willReturn(Collections.emptyList());
		willDoNothing().given(friendRepository).deleteAllBySourceId(userId);
		willDoNothing().given(userRepository).deleteById(userId);

		// when
		service.exit(userId);

		// then
		then(participantRepository).should(times(1)).findAllByUserId(userId);
		then(friendRepository).should(times(1)).deleteAllBySourceId(userId);
		then(userRepository).should(times(1)).deleteById(userId);
	}

	@Test
	void updateMe() {
		// given
		long uid = 1L;
		UserUpdateRequestDto dto = new UserUpdateRequestDto();
		dto.setName("user02");
		dto.setStateMessage("change message");

		UserEntity user = UserEntity.builder().id(uid).name("user01").stateMessage("origin message").build();
		given(userRepository.findById(uid)).willReturn(Optional.of(user));

		// when
		UserDto result = service.updateMe(uid, dto);
		System.out.println("result = " + result);

		// then
		assertThat(result).extracting(UserDto::getId).isEqualTo(uid);
		assertThat(result).extracting(UserDto::getName).isEqualTo("user02");
		assertThat(result).extracting(UserDto::getStateMessage).isEqualTo("change message");
		then(userRepository).should(times(1)).findById(uid);
	}

	@Test
	void updateProfileImg() {
		// given
		long uid = 1L;
		MockMultipartFile file = new MockMultipartFile(
			"file",
			"hello.txt",
			MediaType.TEXT_PLAIN_VALUE,
			"Hello, World!".getBytes()
		);

		UserEntity user = UserEntity.builder().id(uid)
			.profileImgUrl("/files/1")
			.build();
		given(userRepository.findById(uid)).willReturn(Optional.of(user));

		FileDto fileDto = new FileDto();
		fileDto.setPath("/files/2");
		given(fileService.save(uid, file)).willReturn(fileDto);

		// when
		service.updateProfileImg(uid, file);

		// then
		then(userRepository).should(times(1)).findById(uid);
		then(fileService).should(times(1)).save(uid, file);
	}

}