package xyz.jocn.chat.user;

import static xyz.jocn.chat.common.exception.ResourceType.*;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.file.FileService;
import xyz.jocn.chat.file.dto.FileDto;
import xyz.jocn.chat.friend.repo.friend.FriendRepository;
import xyz.jocn.chat.friend.repo.friend_block.FriendBlockRepository;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.dto.UserUpdateRequestDto;
import xyz.jocn.chat.user.enums.UserState;
import xyz.jocn.chat.user.repo.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

	private final UserRepository userRepository;
	private final FriendRepository friendRepository;
	private final FriendBlockRepository friendBlockRepository;
	private final RoomParticipantRepository roomParticipantRepository;
	private final ThreadParticipantRepository threadParticipantRepository;

	private final FileService fileService;

	private final PasswordEncoder passwordEncoder;
	private final UserConverter userConverter = UserConverter.INSTANCE;

	@Transactional
	public long signUp(UserSignUpRequestDto userSignUpRequestDto) {

		Optional<UserEntity> opt = userRepository.findByEmail(userSignUpRequestDto.getEmail());
		String password = passwordEncoder.encode(userSignUpRequestDto.getPassword());

		if (opt.isPresent()) {
			UserEntity user = opt.get();

			if (user.getState() == UserState.DELETED) {
				user.reactivate();
				user.changePassword(password);
				return user.getId();
			} else {
				throw new ResourceAlreadyExistException(USER);
			}
		} else {
			UserEntity userEntity = UserEntity.builder()
				.email(userSignUpRequestDto.getEmail())
				.name(userSignUpRequestDto.getName())
				.password(password)
				.build();

			userRepository.save(userEntity);
			return userEntity.getId();
		}
	}

	public UserDto fetchMe(Long userId) {
		return userConverter.toDto(
			userRepository
				.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(USER))
		);
	}

	@Transactional
	public void exit(Long userId) {
		roomParticipantRepository.findAllByUserId(userId).forEach(RoomParticipantEntity::exit);
		threadParticipantRepository.findAllByUserId(userId).forEach(ThreadParticipantEntity::exit);

		friendBlockRepository.deleteAllBySourceId(userId);
		friendRepository.deleteAllBySourceId(userId);
		userRepository.deleteById(userId);

		// TODO : 파일 삭제
		// 메시지 파일 남겨두고, 그외는 스케줄러로 삭제
	}

	@Transactional
	public UserDto updateMe(long uid, UserUpdateRequestDto dto) {

		UserEntity me = userRepository
			.findById(uid)
			.orElseThrow(() -> new ResourceNotFoundException(USER));

		if (Objects.nonNull(dto.getName())) {
			me.changeName(dto.getName().trim());
		}
		if (Objects.nonNull(dto.getStateMessage())) {
			me.changeStateMessage(dto.getStateMessage());
		}

		return userConverter.toDto(me);
	}

	@Transactional
	public void changePhoto(long uid, MultipartFile file) {
		UserEntity userEntity = userRepository
			.findById(uid)
			.orElseThrow(() -> new ResourceNotFoundException(USER));

		FileDto fileDto = fileService.save(uid, file);
		userEntity.changePhoto(fileDto.getPath());
	}
}
