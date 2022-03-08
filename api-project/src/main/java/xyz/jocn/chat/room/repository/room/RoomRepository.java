package xyz.jocn.chat.room.repository.room;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.room.entity.RoomEntity;
import xyz.jocn.chat.room.entity.RoomParticipantEntity;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long>, RoomRepositoryExt {

}
