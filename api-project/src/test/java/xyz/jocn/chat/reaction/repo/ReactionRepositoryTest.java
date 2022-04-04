package xyz.jocn.chat.reaction.repo;

import static org.assertj.core.api.Assertions.*;
import static xyz.jocn.chat.message.enums.MessageType.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.message.MessageEntity;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.reaction.ReactionEntity;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ReactionRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	ReactionRepository repo;

	@BeforeEach
	void setUp() {

	}

	@Test
	void findAllByMessage() {
		// given
		MessageEntity message = MessageEntity.builder().text("hello").type(short_text).build();
		em.persist(message);

		ReactionEntity reaction = ReactionEntity.builder().message(message).build();
		em.persist(reaction);
		em.flush();
		em.clear();

		// when
		List<ReactionEntity> result = repo.findAllByMessage(message);
		result.forEach(System.out::println);

		// then
		assertThat(result).extracting(ReactionEntity::getId).contains(reaction.getId());
		assertThat(result)
			.extracting(ReactionEntity::getMessage)
			.extracting(MessageEntity::getId)
			.contains(message.getId());
	}

	@Test
	void findByIdAndParticipant() {
		// given
		ParticipantEntity participant = ParticipantEntity.builder().build();
		em.persist(participant);

		ReactionEntity reaction = ReactionEntity.builder().participant(participant).build();
		em.persist(reaction);
		em.flush();
		em.clear();

		// when
		ReactionEntity result = repo.findByIdAndParticipant(reaction.getId(), participant).get();
		System.out.println("result = " + result);

		// then
		assertThat(result).extracting(ReactionEntity::getId).isEqualTo(reaction.getId());
		assertThat(result).extracting(ReactionEntity::getParticipant)
			.extracting(ParticipantEntity::getId)
			.isEqualTo(participant.getId());
	}
}