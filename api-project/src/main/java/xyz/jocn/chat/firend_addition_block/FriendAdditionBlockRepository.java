package xyz.jocn.chat.firend_addition_block;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendAdditionBlockRepository extends JpaRepository<PeopleBookEntity, Long> {
}
