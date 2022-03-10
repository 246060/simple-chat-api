package xyz.jocn.chat.chat_space.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.chat_space.repo.thread.ThreadRepository;
import xyz.jocn.chat.common.pubsub.ChatProducer;
import xyz.jocn.chat.message.repo.room_message.RoomMessageRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;

@ExtendWith(MockitoExtension.class)
class ThreadServiceTest {

	@Mock
	RoomMessageRepository roomMessageRepository;
	@Mock
	ThreadRepository threadRepository;
	@Mock
	ThreadParticipantRepository threadParticipantRepository;
	@Mock
	ChatProducer chatProducer;
	@InjectMocks
	ThreadService service;

	@Test
	void open() {
		// given

		// when

		// then
	}

	@Test
	void getThreads() {
		// given

		// when

		// then
	}
}