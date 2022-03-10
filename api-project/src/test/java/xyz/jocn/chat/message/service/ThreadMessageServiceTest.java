package xyz.jocn.chat.message.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.chat_space.repo.thread.ThreadRepository;
import xyz.jocn.chat.common.pubsub.ChatProducer;
import xyz.jocn.chat.message.repo.thread_message.ThreadMessageRepository;
import xyz.jocn.chat.message.repo.thread_message_mark.ThreadMessageMarkRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;

@ExtendWith(MockitoExtension.class)
class ThreadMessageServiceTest {

	@Mock
	ThreadRepository threadRepository;
	@Mock
	ThreadParticipantRepository threadParticipantRepository;
	@Mock
	ThreadMessageRepository threadMessageRepository;
	@Mock
	ThreadMessageMarkRepository threadMessageMarkRepository;
	@Mock
	ChatProducer chatProducer;
	@InjectMocks
	ThreadMessageService service;

	@Test
	void send() {
		// given

		// when

		// then
	}

	@Test
	void getMessages() {
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
	void cancelThreadMessageMark() {
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