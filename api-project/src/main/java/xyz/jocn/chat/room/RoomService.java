package xyz.jocn.chat.room;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.common.pubsub.EventTarget.*;
import static xyz.jocn.chat.common.pubsub.EventType.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.room.dto.RoomCreateDto;
import xyz.jocn.chat.room.dto.RoomDto;
import xyz.jocn.chat.room.repo.RoomRepository;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.pubsub.EventDto;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomService {

	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final RoomParticipantRepository roomParticipantRepository;

	private final RoomConverter roomConverter = RoomConverter.INSTANCE;

	private final MessagePublisher publisher;

	@Transactional
	public RoomDto open(RoomCreateDto dto) {

		if (dto.getHostId() == dto.getInviteeId()) {
			throw new RoomException("cannot invite yourself");
		}

		UserEntity invitee =
			userRepository.findById(dto.getInviteeId()).orElseThrow(() -> new ResourceNotFoundException(USER));

		UserEntity host = new UserEntity(dto.getHostId());

		RoomEntity room = roomRepository.save(RoomEntity.builder().user(host).build());

		roomParticipantRepository.save(RoomParticipantEntity.builder().room(room).user(host).build());
		roomParticipantRepository.save(RoomParticipantEntity.builder().room(room).user(invitee).build());

		{ // pub event
			publisher.emit(
				EventDto.builder()
					.target(INDIVIDUAL)
					.type(ROOM_EVENT)
					.receiver(List.of(host.getId(), invitee.getId()))
					.spaceId(room.getId())
					.build()
			);
		}

		return roomConverter.toDto(room);
	}

	public List<RoomDto> getRoomList(Long userId) {
		return roomParticipantRepository
			.findAllByUserId(userId)
			.stream()
			.map(roomParticipantEntity -> roomConverter.toDto(roomParticipantEntity.getRoom()))
			.collect(Collectors.toList());
	}
}
