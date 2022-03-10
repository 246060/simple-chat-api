package xyz.jocn.chat.participant.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.chat_space.repo.room.RoomRepository;
import xyz.jocn.chat.chat_space.repo.thread.ThreadRepository;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.user.repo.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class RoomParticipantServiceTest {

	@Mock
	UserRepository userRepository;
	@Mock
	RoomRepository roomRepository;
	@Mock
	RoomParticipantRepository roomParticipantRepository;
	@Mock
	ThreadParticipantRepository threadParticipantRepository;
	@Mock
	ThreadRepository threadRepository;
	@InjectMocks
	RoomParticipantService service;

	@Test
	void invite() {
		// given

		// when

		// then
	}

	@Test
	void getParticipants() {
		// given

		// when

		// then
	}

	@Test
	void exit() {
		// given

		// when

		// then
	}
}