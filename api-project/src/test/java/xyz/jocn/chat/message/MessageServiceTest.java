package xyz.jocn.chat.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.message.repo.message.MessageRepository;
import xyz.jocn.chat.reaction.repo.ReactionRepository;
import xyz.jocn.chat.participant.repo.ParticipantRepository;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
	@Mock
	ParticipantRepository roomParticipantRepository;
	@Mock
	MessageRepository roomMessageRepository;
	@Mock
	ReactionRepository roomMessageReactionRepository;
	@Mock
	MessagePublisher publisher;
	@InjectMocks
	MessageService service;

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