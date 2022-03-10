package xyz.jocn.chat.participant.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;

@ExtendWith(MockitoExtension.class)
class ThreadParticipantServiceTest {
	@Mock
	ThreadParticipantRepository threadParticipantRepository;
	@InjectMocks
	ThreadParticipantService service;
}