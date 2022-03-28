package xyz.jocn.chat.participant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.participant.repo.ParticipantRepository;
import xyz.jocn.chat.channel.repo.ChannelRepository;
import xyz.jocn.chat.user.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

	@Mock
	UserRepository userRepository;
	@Mock
	ChannelRepository roomRepository;
	@Mock
	ParticipantRepository roomParticipantRepository;

	@InjectMocks
	ParticipantService service;

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