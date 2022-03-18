package xyz.jocn.chat.user.service;

import static xyz.jocn.chat.common.enums.ResourceType.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.user.converter.FriendBlockConverter;
import xyz.jocn.chat.user.converter.FriendConverter;
import xyz.jocn.chat.user.dto.FriendBlockCancelRequestDto;
import xyz.jocn.chat.user.dto.FriendBlockCreateRequestDto;
import xyz.jocn.chat.user.dto.FriendBlockDto;
import xyz.jocn.chat.user.dto.FriendCreateRequestDto;
import xyz.jocn.chat.user.dto.FriendDeleteRequestDto;
import xyz.jocn.chat.user.dto.FriendDto;
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

	private final FriendConverter friendController = FriendConverter.INSTANCE;
	private final FriendBlockConverter friendBlockConverter = FriendBlockConverter.INSTANCE;

	@Transactional
	public Long addFriend(FriendCreateRequestDto dto) {

		UserEntity target =
			userRepository
				.findById(dto.getTargetId())
				.orElseThrow(() -> new ResourceNotFoundException(USER));

		FriendEntity friendEntity = FriendEntity.builder()
			.source(UserEntity.builder().id(dto.getUserId()).build())
			.target(target)
			.build();

		friendRepository.save(friendEntity);

		return friendEntity.getId();
	}

	public List<FriendDto> fetchFriends(long userId) {
		return friendController.toDto(friendRepository.findAllBySourceId(userId));
	}

	@Transactional
	public void deleteFriends(FriendDeleteRequestDto dto) {
		friendRepository.deleteAllBySourceIdAndTargetIdIn(dto.getSourceId(), dto.getTargetIds());
	}

	@Transactional
	public long addBlock(FriendBlockCreateRequestDto dto) {

		UserEntity target =
			userRepository
				.findById(dto.getTargetId())
				.orElseThrow(() -> new ResourceNotFoundException(FRIEND_BLOCK));

		friendBlockRepository
			.findBySourceIdAndTargetId(dto.getSourceId(), dto.getTargetId())
			.ifPresent(friendBlockEntity -> new ResourceAlreadyExistException(FRIEND_BLOCK));

		FriendBlockEntity friendBlockEntity = FriendBlockEntity.builder()
			.source(UserEntity.builder().id(dto.getSourceId()).build())
			.target(target)
			.build();

		friendBlockRepository.save(friendBlockEntity);

		return friendBlockEntity.getId();
	}

	public List<FriendBlockDto> fetchBlocks(long userId) {
		return friendBlockConverter.toDto(friendBlockRepository.findAllBySourceId(userId));
	}

	@Transactional
	public void cancelBlock(FriendBlockCancelRequestDto dto) {
		friendBlockRepository.deleteAllBySourceIdAndTargetIdIn(dto.getSourceId(), dto.getTargetIds());
	}
}
