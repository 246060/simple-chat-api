package xyz.jocn.chat.room.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.room.RoomEntity;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long>, RoomRepositoryExt {

}
