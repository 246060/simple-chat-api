package xyz.jocn.chat.friend;

import static xyz.jocn.chat.common.exception.ResourceType.*;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.friend.dto.FriendDto;
import xyz.jocn.chat.friend.dto.FriendRequestDto;
import xyz.jocn.chat.friend.dto.FriendSearchDto;
import xyz.jocn.chat.friend.repo.FriendRepository;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FriendService {

	private final FriendRepository friendRepository;
	private final UserRepository userRepository;

	private final FriendConverter friendConverter = FriendConverter.INSTANCE;

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public Long addFriend(long uid, FriendRequestDto dto) {

		UserEntity target = userRepository
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
	public void deleteFriend(long uid, long friendId) {
		FriendEntity friendEntity = friendRepository
			.findByIdAndSourceId(friendId, uid)
			.orElseThrow(() -> new ResourceNotFoundException(FRIEND));

		friendRepository.delete(friendEntity);
	}

	@Transactional
	public FriendDto updateFriend(long uid, long friendId, FriendDto dto) {

		FriendEntity friend = friendRepository
			.findByIdAndSourceId(friendId, uid)
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

	public FriendDto fetchOne(long uid, long friendId) {
		return friendConverter.toDto(
			friendRepository
				.findByIdAndSourceId(friendId, uid)
				.orElseThrow(() -> new ResourceNotFoundException(FRIEND))
		);
	}

}
