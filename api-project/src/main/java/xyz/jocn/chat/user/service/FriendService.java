package xyz.jocn.chat.user.service;

import static xyz.jocn.chat.common.enums.ResourceType.*;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.user.converter.FriendBlockConverter;
import xyz.jocn.chat.user.converter.FriendConverter;
import xyz.jocn.chat.user.dto.FriendBlockDto;
import xyz.jocn.chat.user.dto.FriendDto;
import xyz.jocn.chat.user.dto.FriendRequestDto;
import xyz.jocn.chat.user.dto.FriendSearchDto;
import xyz.jocn.chat.user.dto.FriendUidsDto;
import xyz.jocn.chat.user.entity.FriendBlockEntity;
import xyz.jocn.chat.user.entity.FriendEntity;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repo.friend.FriendRepository;
import xyz.jocn.chat.user.repo.friend_block.FriendBlockRepository;
import xyz.jocn.chat.user.repo.user.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FriendService {

	private final FriendRepository friendRepository;
	private final FriendBlockRepository friendBlockRepository;
	private final UserRepository userRepository;

	private final FriendConverter friendConverter = FriendConverter.INSTANCE;
	private final FriendBlockConverter friendBlockConverter = FriendBlockConverter.INSTANCE;


	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public Long addFriend(long uid, FriendRequestDto dto) {

		UserEntity target =
			userRepository
				.findByEmail(dto.getTargetEmail())
				.orElseThrow(() -> new ResourceNotFoundException(USER));

		FriendEntity friendEntity = FriendEntity.builder()
			.source(UserEntity.builder().id(uid).build())
			.target(target)
			.build();

		friendRepository.save(friendEntity);

		return friendEntity.getId();
	}

	@Transactional
	public void deleteFriends(long uid, FriendUidsDto dto) {
		friendRepository.deleteAllBySourceIdAndTargetIdIn(uid, dto.getTargets());
	}

	@Transactional
	public long addBlock(long uid, FriendRequestDto dto) {

		UserEntity target =
			userRepository
				.findById(dto.getTargetUid())
				.orElseThrow(() -> new ResourceNotFoundException(FRIEND_BLOCK));

		friendBlockRepository
			.findBySourceIdAndTargetId(uid, dto.getTargetUid())
			.ifPresent(friendBlockEntity -> new ResourceAlreadyExistException(FRIEND_BLOCK));

		FriendBlockEntity friendBlockEntity = FriendBlockEntity.builder()
			.source(UserEntity.builder().id(uid).build())
			.target(target)
			.build();

		friendBlockRepository.save(friendBlockEntity);

		return friendBlockEntity.getId();
	}

	@Transactional
	public void cancelBlock(long uid, FriendUidsDto dto) {
		friendBlockRepository.deleteAllBySourceIdAndTargetIdIn(uid, dto.getTargets());
	}

	@Transactional
	public FriendDto update(long uid, FriendDto dto) {

		FriendEntity friend = friendRepository
			.findByIdAndSourceId(dto.getId(), uid)
			.orElseThrow(() -> new ResourceNotFoundException(FRIEND));

		if (Objects.nonNull(dto.getFavorite())) {
			friend.changeFavorite(dto.getFavorite());
		}
		if (Objects.nonNull(dto.getHidden())) {
			friend.changeHidden(dto.getHidden());
		}
		if (Objects.nonNull(dto.getName())) {
			friend.changeName(dto.getName());
		}

		return friendConverter.toDto(friend);
	}

	/*
	 * Query ===============================================================================
	 * */

	public List<FriendDto> fetchFriends(long uid, FriendSearchDto friendSearchDto) {
		return friendConverter.toDto(friendRepository.findAllBySourceIdAndCondition(uid, friendSearchDto));
	}

	public List<FriendBlockDto> fetchBlocks(long uid) {
		return friendBlockConverter.toDto(friendBlockRepository.findAllBySourceId(uid));
	}
}
