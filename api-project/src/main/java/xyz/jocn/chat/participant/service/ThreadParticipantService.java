package xyz.jocn.chat.participant.service;

import static xyz.jocn.chat.common.exception.ResourceType.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.participant.converter.ThreadParticipantConverter;
import xyz.jocn.chat.participant.dto.ThreadJoinDto;
import xyz.jocn.chat.participant.dto.ThreadParticipantDto;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.thread.ThreadEntity;
import xyz.jocn.chat.thread.repo.ThreadRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ThreadParticipantService {

	private final ThreadRepository threadRepository;
	private final RoomParticipantRepository roomParticipantRepository;
	private final ThreadParticipantRepository threadParticipantRepository;

	private ThreadParticipantConverter threadParticipantConverter = ThreadParticipantConverter.INSTANCE;

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void join(ThreadJoinDto dto) {

		ThreadEntity threadEntity = threadRepository
			.findById(dto.getThreadId())
			.orElseThrow(() -> new ResourceNotFoundException(THREAD));

		RoomParticipantEntity roomParticipantEntity = roomParticipantRepository
			.findByIdAndUserId(dto.getRoomId(), dto.getUserId())
			.orElseThrow(() -> new ResourceNotFoundException(ROOM_PARTICIPANT));

		ThreadParticipantEntity threadParticipantEntity = ThreadParticipantEntity.builder()
			.roomParticipant(roomParticipantEntity)
			.thread(threadEntity)
			.build();

		threadParticipantRepository.save(threadParticipantEntity);
	}

	/*
	 * Query ===============================================================================
	 * */

	public List<ThreadParticipantDto> fetchThreadParticipants(Long roomId, Long threadId) {
		return threadParticipantConverter.toDto(threadParticipantRepository.findAllByThreadId(threadId));
	}
}

