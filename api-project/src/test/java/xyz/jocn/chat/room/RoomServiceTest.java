package xyz.jocn.chat.room;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.room.dto.RoomDto;
import xyz.jocn.chat.room.dto.RoomOpenRequestDto;
import xyz.jocn.chat.room.repo.RoomRepository;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

	@Mock
	RoomRepository roomRepository;
	@Mock
	UserRepository userRepository;
	@Mock
	RoomParticipantRepository roomParticipantRepository;
	@Mock
	MessagePublisher publisher;
	@InjectMocks
	RoomService roomService;

	@Test
	void open() {
		// given
		Long hostID = 1L;
		Long inviteeId = 2L;
		UserEntity invitee = UserEntity.builder().id(inviteeId).build();
		RoomEntity room = RoomEntity.builder().id(1L).build();

		given(userRepository.findById(anyLong())).willReturn(Optional.of(invitee));

		given(roomRepository.save(any(RoomEntity.class))).willReturn(room);
		given(roomParticipantRepository.save(any(RoomParticipantEntity.class))).willReturn(null);

		// willDoNothing().given(publisher).emit(any(EventDto.class));

		RoomOpenRequestDto dto = new RoomOpenRequestDto();
		dto.setInviteeId(inviteeId);

		// when
		RoomDto result = roomService.open(hostID, inviteeId);

		// then
		then(userRepository).should().findById(anyLong());
		then(roomRepository).should().save(any(RoomEntity.class));
		then(roomParticipantRepository).should(times(2)).save(any(RoomParticipantEntity.class));

		assertThat(result).isNotNull();
		assertThat(result).extracting(RoomDto::getRoomId).isNotNull();
		assertThat(result).extracting(RoomDto::getRoomId).isEqualTo(room.getId());
	}

	@Test
	void getRoomList() {
		// given
		Long userId = 1L;
		List<RoomParticipantEntity> entities = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			entities.add(RoomParticipantEntity.builder()
				.id(1L + i)
				.room(RoomEntity.builder().id(1L + i).build())
				.build());
		}
		given(roomParticipantRepository.findAllByUserId(anyLong())).willReturn(entities);

		// when
		List<RoomDto> result = roomService.fetchMyRooms(userId);

		// then
		then(roomParticipantRepository).should().findAllByUserId(anyLong());

		assertThat(result).isNotEmpty();
		assertThat(result).extracting(RoomDto::getRoomId)
			.containsAll(entities.stream()
				.map(RoomParticipantEntity::getRoom)
				.map(RoomEntity::getId)
				.collect(Collectors.toList())
			);

	}
}