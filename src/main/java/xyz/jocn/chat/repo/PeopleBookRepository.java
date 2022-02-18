package xyz.jocn.chat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.entity.PeopleBookEntity;

@Repository
public interface PeopleBookRepository extends JpaRepository<PeopleBookEntity, Long> {
}
