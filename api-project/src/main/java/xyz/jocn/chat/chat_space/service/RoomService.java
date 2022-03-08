package xyz.jocn.chat.chat_space.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.chat_space.converter.RoomConverter;
import xyz.jocn.chat.chat_space.dto.RoomCreateDto;
import xyz.jocn.chat.chat_space.dto.RoomDto;
import xyz.jocn.chat.chat_space.entity.RoomEntity;
import xyz.jocn.chat.chat_space.repo.room.RoomRepository;
import xyz.jocn.chat.common.pubsub.ChatProducer;
import xyz.jocn.chat.participant.converter.RoomParticipantConverter;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.exception.NotFoundUserException;
import xyz.jocn.chat.user.repo.user.UserRepository;

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
	private final RoomParticipantConverter roomParticipantConverter = RoomParticipantConverter.INSTANCE;

	private final ChatProducer producer;

	@Transactional
	public RoomDto open(RoomCreateDto dto) {

		RoomEntity roomEntity = roomRepository.save(RoomEntity.builder().build());

		roomParticipantRepository.save(
			RoomParticipantEntity.builder()
				.room(roomEntity)
				.user(userRepository
					.findById(dto.getHostId())
					.orElseThrow(NotFoundUserException::new))
				.build()
		);

		roomParticipantRepository.save(RoomParticipantEntity.builder()
			.room(roomEntity)
			.user(userRepository
				.findById(dto.getInviteeId())
				.orElseThrow(NotFoundUserException::new))
			.build()
		);

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);

		return roomConverter.toDto(roomEntity);
	}

	public List<RoomDto> getRoomList(String userId) {

		UserEntity userEntity = userRepository
			.findById(Long.parseLong(userId))
			.orElseThrow(NotFoundUserException::new);

		List<RoomParticipantEntity> participantEntities =
			roomParticipantRepository.findAllByUser(userEntity);

		List<RoomDto> list = new ArrayList<>();
		for (RoomParticipantEntity participantEntity : participantEntities) {
			list.add(roomConverter.toDto(participantEntity.getRoom()));
		}

		return list;
	}

}
