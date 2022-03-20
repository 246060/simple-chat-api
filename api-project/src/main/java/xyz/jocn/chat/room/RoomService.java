package xyz.jocn.chat.room;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.common.pubsub.EventTarget.*;
import static xyz.jocn.chat.common.pubsub.EventType.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.pubsub.EventDto;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.room.dto.RoomDto;
import xyz.jocn.chat.room.repo.RoomRepository;
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
	private final ThreadParticipantRepository threadParticipantRepository;

	private final RoomConverter roomConverter = RoomConverter.INSTANCE;

	private final MessagePublisher publisher;

	@Value("${app.publish-event-trigger}")
	public boolean isPublishEventTrigger;

	@Transactional
	public RoomDto open(Long hostId, Long inviteeId) {

		UserEntity invitee = userRepository
			.findById(inviteeId)
			.orElseThrow(() -> new ResourceNotFoundException(USER));
		UserEntity host = userRepository
			.findById(hostId)
			.orElseThrow(() -> new ResourceNotFoundException(USER));

		RoomEntity room = roomRepository.save(RoomEntity.builder().build());

		RoomParticipantEntity participant1 = RoomParticipantEntity.builder()
			.name(host.getName())
			.user(host)
			.build();
		RoomParticipantEntity participant2 = RoomParticipantEntity.builder()
			.name(invitee.getName())
			.user(invitee)
			.build();

		room.join(participant1);
		room.join(participant2);

		roomParticipantRepository.save(participant1);
		roomParticipantRepository.save(participant2);

		if (isPublishEventTrigger) {
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

	public List<RoomDto> fetchMyRooms(long uid) {
		return roomParticipantRepository
			.findAllByUserId(uid)
			.stream()
			.map(roomParticipantEntity -> roomConverter.toDto(roomParticipantEntity.getRoom()))
			.collect(Collectors.toList());
	}
}
