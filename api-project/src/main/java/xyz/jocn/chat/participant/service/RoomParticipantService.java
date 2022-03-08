package xyz.jocn.chat.participant.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.chat_space.entity.RoomEntity;
import xyz.jocn.chat.chat_space.exception.NotFoundRoomException;
import xyz.jocn.chat.chat_space.repo.room.RoomRepository;
import xyz.jocn.chat.participant.converter.RoomParticipantConverter;
import xyz.jocn.chat.participant.dto.RoomInviteRequestDto;
import xyz.jocn.chat.participant.dto.RoomParticipantDto;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.exception.NotFoundRoomParticipantException;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;

import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repo.user.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomParticipantService {

	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final RoomParticipantRepository roomParticipantRepository;
	private final ThreadParticipantRepository threadParticipantRepository;

	private final RoomParticipantConverter roomParticipantConverter = RoomParticipantConverter.INSTANCE;

	@Transactional
	public void invite(RoomInviteRequestDto roomInviteRequestDto) {

		RoomEntity roomEntity =
			roomRepository.findById(roomInviteRequestDto.getRoomId()).orElseThrow(NotFoundRoomException::new);

		List<UserEntity> userEntities =
			userRepository.findByIdIn(roomInviteRequestDto.getInvitees());

		List<RoomParticipantEntity> newParticipants = new ArrayList<>();

		for (UserEntity userEntity : userEntities) {
			newParticipants.add(
				RoomParticipantEntity.builder()
					.room(roomEntity)
					.user(userEntity)
					.build()
			);
		}

		roomParticipantRepository.saveAll(newParticipants);

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
	}

	public List<RoomParticipantDto> getParticipants(Long roomId) {

		RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(NotFoundRoomException::new);

		List<RoomParticipantEntity> participants = roomParticipantRepository.findAllByRoom(roomEntity);
		for (RoomParticipantEntity participant : participants) {
			participant.getUser().getName();
		}

		return roomParticipantConverter.toDto(participants);
	}

	@Transactional
	public void exit(Long participantId) {
		RoomParticipantEntity roomParticipantEntity = roomParticipantRepository
			.findById(participantId)
			.orElseThrow(NotFoundRoomParticipantException::new);

		Long roomId = roomParticipantEntity.getRoom().getId();

		threadParticipantRepository.deleteAllInBatch(
			threadParticipantRepository.findAllByRoomParticipant(roomParticipantEntity)
		);
		roomParticipantRepository.delete(roomParticipantEntity);

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
	}
}
