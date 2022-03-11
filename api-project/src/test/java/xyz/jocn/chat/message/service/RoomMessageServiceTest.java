package xyz.jocn.chat.message.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.message.repo.room_message.RoomMessageRepository;
import xyz.jocn.chat.message.repo.room_message_mark.RoomMessageMarkRepository;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;

@ExtendWith(MockitoExtension.class)
class RoomMessageServiceTest {
	@Mock
	RoomParticipantRepository roomParticipantRepository;
	@Mock
	RoomMessageRepository roomMessageRepository;
	@Mock
	RoomMessageMarkRepository roomMessageMarkRepository;
	@Mock
	ChatProducer chatProducer;
	@InjectMocks
	RoomMessageService service;

	@Test
	void sendMessageToRoom() {
		// given

		// when

		// then
	}

	@Test
	void getMessagesInRoom() {
		// given

		// when

		// then
	}

	@Test
	void mark() {
		// given

		// when

		// then
	}

	@Test
	void getMarks() {
		// given

		// when

		// then
	}

	@Test
	void cancelMark() {
		// given

		// when

		// then
	}

	@Test
	void change() {
		// given

		// when

		// then
	}
}