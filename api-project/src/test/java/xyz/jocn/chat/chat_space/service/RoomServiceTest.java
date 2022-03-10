package xyz.jocn.chat.chat_space.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.chat_space.repo.room.RoomRepository;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.user.repo.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

	@Mock
	RoomRepository roomRepository;
	@Mock
	UserRepository userRepository;
	@Mock
	RoomParticipantRepository roomParticipantRepository;
	@InjectMocks
	RoomService service;

	@Test
	void open() {
		// given

		// when

		// then
	}

	@Test
	void getRoomList() {
		// given

		// when

		// then
	}
}