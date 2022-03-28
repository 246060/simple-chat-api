package xyz.jocn.chat.participant.service;

import static xyz.jocn.chat.common.exception.ResourceType.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.participant.converter.RoomParticipantConverter;
import xyz.jocn.chat.participant.dto.RoomExitDto;
import xyz.jocn.chat.participant.dto.RoomInviteRequestDto;
import xyz.jocn.chat.participant.dto.RoomParticipantDto;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.room.RoomEntity;
import xyz.jocn.chat.room.repo.RoomRepository;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

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

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void invite(Long roomId, RoomInviteRequestDto dto) {

		RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException(ROOM));
		List<UserEntity> userEntities = userRepository.findByIdIn(dto.getInvitees());

		List<RoomParticipantEntity> newParticipants = userEntities.stream()
			.map(userEntity -> RoomParticipantEntity.builder().room(roomEntity).user(userEntity).build())
			.collect(Collectors.toList());

		roomParticipantRepository.saveAll(newParticipants);
	}

	@Transactional
	public void exit(RoomExitDto dto) {

		RoomParticipantEntity roomParticipantEntity = roomParticipantRepository
			.findByIdAndUserId(dto.getParticipantId(), dto.getUserId())
			.orElseThrow(() -> new ResourceNotFoundException(ROOM_PARTICIPANT));

		roomParticipantEntity.exit();

		threadParticipantRepository
			.findAllByRoomParticipant(roomParticipantEntity)
			.forEach(ThreadParticipantEntity::exit);
	}

	/*
	 * Query ===============================================================================
	 * */

	public List<RoomParticipantDto> fetchParticipants(Long roomId) {
		return roomParticipantConverter.toDto(roomParticipantRepository.findAllByRoomId(roomId));
	}

	public RoomParticipantEntity fetchRoomParticipant(long roomId, long uid) {
		return roomParticipantRepository
			.findByRoomIdAndUserId(roomId, uid)
			.orElseThrow(() -> new ResourceNotFoundException(ROOM_PARTICIPANT));
	}

	public void checkRoomParticipant(long roomId, long uid) {
		this.fetchRoomParticipant(roomId, uid);
	}
}
