package xyz.jocn.chat.chat_space.converter;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import xyz.jocn.chat.thread.ThreadConverter;
import xyz.jocn.chat.thread.dto.ThreadDto;
import xyz.jocn.chat.room.RoomEntity;
import xyz.jocn.chat.thread.ThreadEntity;
import xyz.jocn.chat.message.entity.RoomMessageEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;

class ThreadConverterTest {

	ThreadConverter converter = ThreadConverter.INSTANCE;

	@DisplayName("ThreadDto toDto(ThreadEntity entity)")
	@Test
	void test1() {

		// given
		Long threadId = 1L;
		Long roomId = 100L;
		Long messageId = 20L;

		ThreadEntity thread = ThreadEntity.builder()
			.id(threadId)
			.room(new RoomEntity(roomId))
			.roomMessage(new RoomMessageEntity(messageId))
			.build();

		// when
		ThreadDto threadDto = converter.toDto(thread);
		System.out.println("threadDto = " + threadDto);

		// then
		assertThat(threadDto).extracting(ThreadDto::getThreadId).isEqualTo(threadId);
		assertThat(threadDto).extracting(ThreadDto::getRoomId).isEqualTo(roomId);
		assertThat(threadDto).extracting(ThreadDto::getRoomMessageId).isEqualTo(messageId);
	}

	@DisplayName("ThreadDto toDto(ThreadParticipantEntity threadParticipantEntity)")
	@Test
	void test2() {
		// given
		Long threadId = 1L;
		Long roomId = 100L;
		Long messageId = 20L;
		Long threadParticipantId = 2L;

		ThreadEntity thread = ThreadEntity.builder()
			.id(threadId)
			.room(new RoomEntity(roomId))
			.roomMessage(new RoomMessageEntity(messageId))
			.build();

		ThreadParticipantEntity threadParticipant = ThreadParticipantEntity
			.builder()
			.id(threadParticipantId)
			.thread(thread)
			.build();

		// when
		ThreadDto threadDto = converter.toDto(threadParticipant);
		System.out.println("threadDto = " + threadDto);

		// then
		assertThat(threadDto).extracting(ThreadDto::getThreadId).isEqualTo(threadId);
		assertThat(threadDto).extracting(ThreadDto::getRoomId).isEqualTo(roomId);
		assertThat(threadDto).extracting(ThreadDto::getRoomMessageId).isEqualTo(messageId);
	}
}