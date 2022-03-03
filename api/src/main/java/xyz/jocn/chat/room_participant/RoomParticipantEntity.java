package xyz.jocn.chat.room_participant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RoomParticipantEntity {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
}
