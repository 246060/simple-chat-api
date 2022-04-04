package xyz.jocn.chat.message.repo;

import static org.assertj.core.api.Assertions.*;
import static xyz.jocn.chat.common.dto.SliceDirection.*;
import static xyz.jocn.chat.message.enums.MessageType.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.FakeUser;
import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.common.dto.SliceCriteria;
import xyz.jocn.chat.message.MessageEntity;
import xyz.jocn.chat.message.QMessageEntity;
import xyz.jocn.chat.message.dto.MessageDto;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.user.entity.UserEntity;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class MessageRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	MessageRepository repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void test() {
		// given
		UserEntity user1 = UserEntity.builder()
			.name("user01")
			.email("user01@test.org")
			.profileImgUrl("http://~")
			.build();
		UserEntity user2 = UserEntity.builder()
			.name("user02")
			.email("user02@test.org")
			.profileImgUrl("http://~")
			.build();
		em.persist(user1);
		em.persist(user2);

		ChannelEntity channel = ChannelEntity.builder().build();
		ParticipantEntity participant1 = ParticipantEntity.builder().channel(channel).user(user1).build();
		ParticipantEntity participant2 = ParticipantEntity.builder().channel(channel).user(user2).build();
		em.persist(channel);
		em.persist(participant1);
		em.persist(participant2);

		em.flush();
		em.clear();

		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		QMessageEntity message = QMessageEntity.messageEntity;

		Long aLong = queryFactory
			.select(message.id)
			.from(message)
			.where(message.channel().id.eq(channel.getId()))
			.orderBy(message.id.desc())
			.fetchOne();

		System.out.println("aLong = " + aLong);

		Long aLong1 = Optional.ofNullable(aLong).orElseGet(() -> null);
		System.out.println("aLong1 = " + aLong1);

	}

	@Test
	void findLastMessageIdInChannel() {
		// given
		UserEntity user1 = FakeUser.generateUser("user01");
		UserEntity user2 = FakeUser.generateUser("user02");
		em.persist(user1);
		em.persist(user2);

		ChannelEntity channel = ChannelEntity.builder().build();
		ParticipantEntity participant1 = ParticipantEntity.builder().channel(channel).user(user1).build();
		ParticipantEntity participant2 = ParticipantEntity.builder().channel(channel).user(user2).build();
		em.persist(channel);
		em.persist(participant1);
		em.persist(participant2);

		MessageEntity message1 = MessageEntity.builder()
			.channel(channel)
			.type(short_text)
			.sender(participant1)
			.text("user01 message")
			.build();

		em.persist(message1);
		em.flush();
		em.clear();

		// when
		Long lastMessageId = repo.findLastMessageIdInChannel(channel.getId()).orElse(null);
		System.out.println("lastMessageId = " + lastMessageId);

		// then
		assertThat(lastMessageId).isGreaterThan(0);
	}

	@Test
	void findMessagesInChannel() {
		// given
		UserEntity user1 = UserEntity.builder()
			.name("user01")
			.email("user01@test.org")
			.profileImgUrl("http://~")
			.build();
		UserEntity user2 = UserEntity.builder()
			.name("user02")
			.email("user02@test.org")
			.profileImgUrl("http://~")
			.build();
		em.persist(user1);
		em.persist(user2);

		ChannelEntity channel = ChannelEntity.builder().build();
		ParticipantEntity participant1 = ParticipantEntity.builder().channel(channel).user(user1).build();
		ParticipantEntity participant2 = ParticipantEntity.builder().channel(channel).user(user2).build();
		em.persist(channel);
		em.persist(participant1);
		em.persist(participant2);

		MessageEntity message1 = MessageEntity.builder()
			.channel(channel)
			.type(short_text)
			.sender(participant1)
			.text("user01 message")
			.build();
		MessageEntity message2 = MessageEntity.builder()
			.channel(channel)
			.type(short_text)
			.sender(participant2)
			.text("user02 message")
			.build();
		MessageEntity message3 = MessageEntity.builder()
			.channel(channel)
			.type(short_text)
			.sender(participant1)
			.text("user01 message")
			.build();

		em.persist(message1);
		em.persist(message2);
		em.persist(message3);

		em.flush();
		em.clear();

		System.out.println("message1 = " + message1);
		System.out.println("message2 = " + message2);
		System.out.println("message3 = " + message3);

		SliceCriteria criteria = new SliceCriteria<Long>(message1.getId(), up, 10);
		System.out.println("messageId = " + message1.getId());

		// when
		List<MessageDto> messageDtos = repo.findMessageDtosInChannel(channel.getId(), criteria);
		// List<MessageEntity> messageDtos = repo.findAll();
		messageDtos.forEach(System.out::println);

		// then
		assertThat(messageDtos).isNotEmpty();
	}
}