package xyz.jocn.chat.user;

import static xyz.jocn.chat.common.exception.ResourceType.*;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.util.StringUtil;
import xyz.jocn.chat.file.FileService;
import xyz.jocn.chat.file.dto.FileDto;
import xyz.jocn.chat.friend.repo.FriendRepository;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.repo.ParticipantRepository;
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.dto.UserUpdateRequestDto;
import xyz.jocn.chat.user.repo.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

	private final UserRepository userRepository;
	private final FriendRepository friendRepository;
	private final ParticipantRepository participantRepository;

	private final FileService fileService;

	private final PasswordEncoder passwordEncoder;
	private final UserConverter userConverter = UserConverter.INSTANCE;

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public long signUp(UserSignUpRequestDto userSignUpRequestDto) {

		userRepository
			.findByEmail(userSignUpRequestDto.getEmail())
			.ifPresent(userEntity -> {
				throw new ResourceAlreadyExistException(USER);
			});

		UserEntity userEntity = UserEntity.builder()
			.email(userSignUpRequestDto.getEmail())
			.password(passwordEncoder.encode(userSignUpRequestDto.getPassword()))
			.name(userSignUpRequestDto.getName())
			.build();

		userRepository.save(userEntity);
		return userEntity.getId();
	}

	@Transactional
	public void exit(long userId) {
		participantRepository.findAllByUserId(userId).forEach(ParticipantEntity::exit);
		friendRepository.deleteAllBySourceId(userId);
		userRepository.deleteById(userId);
	}

	@Transactional
	public UserDto updateMe(long uid, UserUpdateRequestDto dto) {

		UserEntity me = userRepository
			.findById(uid)
			.orElseThrow(() -> new ResourceNotFoundException(USER));

		if (StringUtil.isNotBlank(dto.getName())) {
			me.changeName(dto.getName().trim());
		}
		if (StringUtil.isNotBlank(dto.getStateMessage())) {
			me.changeStateMessage(dto.getStateMessage());
		}

		return userConverter.toDto(me);
	}

	@Transactional
	public void updateProfileImg(long uid, MultipartFile file) {

		UserEntity userEntity = userRepository
			.findById(uid)
			.orElseThrow(() -> new ResourceNotFoundException(USER));

		FileDto fileDto = fileService.save(uid, file);
		userEntity.changeProfileImgUrl(fileDto.getPath());
	}

	/*
	 * Query ===============================================================================
	 * */

	public UserDto fetchMe(long userId) {
		return userConverter.toDto(
			userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER))
		);
	}

	public List<UserDto> fetchUsers() {
		return userConverter.toDto(userRepository.findAll());
	}
}
