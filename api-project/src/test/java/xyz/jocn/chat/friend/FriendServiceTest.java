package xyz.jocn.chat.friend;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.friend.dto.FriendDto;
import xyz.jocn.chat.friend.dto.FriendRequestDto;
import xyz.jocn.chat.friend.dto.FriendSearchDto;
import xyz.jocn.chat.friend.repo.FriendRepository;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {

	@Mock
	FriendRepository friendRepository;
	@Mock
	UserRepository userRepository;
	@InjectMocks
	FriendService service;

	@BeforeEach
	void setUp() {
	}

	@Test
	void addFriend() {

		// given
		long uid = 1L;
		long targetId = 2L;
		String email = "user00@test.org";

		UserEntity target = UserEntity.builder().id(targetId).name("user00").email(email).build();
		given(userRepository.findByEmail(email)).willReturn(Optional.of(target));

		FriendRequestDto friendRequestDto = new FriendRequestDto();
		friendRequestDto.setTargetEmail(email);

		FriendEntity friendEntity = FriendEntity.builder().build();
		given(friendRepository.save(any(FriendEntity.class))).willReturn(friendEntity);

		// when
		Long result = service.addFriend(uid, friendRequestDto);
		System.out.println("result = " + result);

		// then
		then(userRepository).should(times(1)).findByEmail(email);
		then(friendRepository).should(times(1)).save(any(FriendEntity.class));
	}

	@Test
	void deleteFriend() {
		// given
		long uid = 1L;
		long friendId = 1L;

		FriendEntity friendEntity = FriendEntity.builder().build();
		given(friendRepository.findByIdAndSourceId(friendId, uid)).willReturn(Optional.of(friendEntity));
		willDoNothing().given(friendRepository).delete(friendEntity);

		// when
		service.deleteFriend(uid, friendId);

		// then
		then(friendRepository).should(times(1)).findByIdAndSourceId(friendId, uid);
		then(friendRepository).should(times(1)).delete(friendEntity);
	}

	@Test
	void updateFriend() {
		// given
		long uid = 1L;
		long friendId = 1L;

		FriendDto dto = new FriendDto();
		dto.setFavorite(false);
		dto.setHidden(false);
		dto.setName("user0001");

		FriendEntity friend = FriendEntity.builder().build();
		given(friendRepository.findByIdAndSourceId(friendId, uid)).willReturn(Optional.of(friend));

		// when
		FriendDto friendDto = service.updateFriend(uid, friendId, dto);
		System.out.println("friendDto = " + friendDto);

		// then
		then(friendRepository).should(times(1)).findByIdAndSourceId(friendId, uid);

		assertThat(friendDto).isNotNull();
		assertThat(friendDto).extracting(FriendDto::getFavorite).isEqualTo(dto.getFavorite());
		assertThat(friendDto).extracting(FriendDto::getHidden).isEqualTo(dto.getHidden());
		assertThat(friendDto).extracting(FriendDto::getName).isEqualTo(dto.getName());
	}

	@Test
	void fetchFriends() {
		// given
		long sourceId = 1L;
		FriendSearchDto condition = new FriendSearchDto();
		condition.setHidden(true);
		condition.setFavorite(true);

		UserEntity userEntity = UserEntity.builder().id(sourceId).name("user01").build();

		List<FriendEntity> entities = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			FriendEntity friendEntity = FriendEntity.builder().source(userEntity).hidden(true).favorite(true).build();
			entities.add(friendEntity);
		}

		given(friendRepository.findAllBySourceIdAndCondition(sourceId, condition)).willReturn(entities);

		// when
		List<FriendDto> result = service.fetchFriends(sourceId, condition);
		System.out.println("result = " + result);

		// then
		then(friendRepository).should(times(1)).findAllBySourceIdAndCondition(sourceId, condition);

		assertThat(result).isNotEmpty();
		assertThat(result).extracting(FriendDto::getFavorite).allMatch(aBoolean -> aBoolean == condition.getFavorite());
		assertThat(result).extracting(FriendDto::getHidden).allMatch(aBoolean -> aBoolean == condition.getHidden());
	}

	@Test
	void fetchOne() {
		// given
		long uid = 1L;
		long friendId = 1L;

		FriendEntity friendEntity = FriendEntity.builder().favorite(true).hidden(true).build();
		given(friendRepository.findByIdAndSourceId(friendId, uid)).willReturn(Optional.of(friendEntity));

		// when
		FriendDto result = service.fetchOne(uid, friendId);
		System.out.println("result = " + result);

		// then
		then(friendRepository).should(times(1)).findByIdAndSourceId(friendId, uid);

		assertThat(result).isNotNull();
		assertThat(result).extracting(FriendDto::getHidden).isEqualTo(true);
		assertThat(result).extracting(FriendDto::getFavorite).isEqualTo(true);
	}
}