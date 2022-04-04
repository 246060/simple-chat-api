package xyz.jocn.chat.reaction;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static xyz.jocn.chat.message.enums.MessageState.*;
import static xyz.jocn.chat.message.enums.MessageType.*;
import static xyz.jocn.chat.reaction.ReactionType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.message.MessageEntity;
import xyz.jocn.chat.message.repo.MessageRepository;
import xyz.jocn.chat.notification.ChatPushService;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.ParticipantService;
import xyz.jocn.chat.reaction.dto.ReactionAddRequestDto;
import xyz.jocn.chat.reaction.dto.ReactionDto;
import xyz.jocn.chat.reaction.repo.ReactionRepository;

@ExtendWith(MockitoExtension.class)
class ReactionServiceTest {

	@Mock
	ReactionRepository reactionRepository;
	@Mock
	MessageRepository messageRepository;
	@Mock
	ParticipantService participantService;
	@Mock
	ChatPushService chatPushService;

	@InjectMocks
	ReactionService reactionService;


	@Test
	void addReaction() {
		// given
		long uid = 1L;
		long channelId = 1L;
		long messageId = 1L;
		ReactionAddRequestDto dto = new ReactionAddRequestDto();
		dto.setType(Agree);

		ParticipantEntity participant = ParticipantEntity.builder().build();
		given(participantService.fetchParticipant(channelId, uid)).willReturn(participant);

		MessageEntity message = MessageEntity.builder().build();
		given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
		given(reactionRepository.save(any(ReactionEntity.class))).willReturn(null);

		// when
		reactionService.addReaction(uid, channelId, messageId, dto);

		// then
		then(participantService).should(times(1)).fetchParticipant(channelId, uid);
		then(messageRepository).should(times(1)).findById(messageId);
		then(reactionRepository).should(times(1)).save(any(ReactionEntity.class));
	}

	@Test
	void cancelReaction() {
		// given
		long uid = 1L;
		long channelId = 1L;
		long messageId = 1L;
		long reactionId = 1L;

		ParticipantEntity participant = ParticipantEntity.builder().build();
		given(participantService.fetchParticipant(channelId, uid)).willReturn(participant);

		ReactionEntity reaction = ReactionEntity.builder().build();
		given(reactionRepository.findByIdAndParticipant(reactionId, participant))
			.willReturn(Optional.of(reaction));

		willDoNothing().given(reactionRepository).delete(reaction);

		// when
		reactionService.cancelReaction(uid, channelId, messageId, reactionId);

		// then
		then(participantService).should(times(1)).fetchParticipant(channelId, uid);
		then(reactionRepository).should(times(1)).findByIdAndParticipant(reactionId, participant);
		then(reactionRepository).should(times(1)).delete(reaction);
	}

	@Test
	void fetchReactions() {
		// given
		long uid = 1L;
		long channelId = 1L;
		long messageId = 1L;
		long participantId = 1L;
		long participantId2 = 2L;
		long reactionId = 1L;

		willDoNothing().given(participantService).checkParticipant(channelId, uid);

		ParticipantEntity participant = ParticipantEntity.builder()
			.id(participantId)
			.build();

		ParticipantEntity participant2 = ParticipantEntity.builder()
			.id(participantId2)
			.build();

		MessageEntity message = MessageEntity.builder()
			.type(short_text)
			.text("hello")
			.sender(participant)
			.state(active)
			.build();

		ReactionEntity reaction = ReactionEntity.builder()
			.id(reactionId)
			.message(message)
			.participant(participant2)
			.build();

		List<ReactionEntity> entities = new ArrayList<>();
		entities.add(reaction);

		given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
		given(reactionRepository.findAllByMessage(message)).willReturn(entities);


		// when
		List<ReactionDto> result = reactionService.fetchReactions(uid, channelId, messageId);
		result.forEach(System.out::println);

		// then
		assertThat(result).extracting(ReactionDto::getId).contains(reaction.getId());


		then(participantService).should().checkParticipant(channelId, uid);
	}
}