package xyz.jocn.chat.people_book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleBookRepository extends JpaRepository<PeopleBookEntity, Long> {
}
