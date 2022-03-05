package xyz.jocn.chat.room_invite_block;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RoomInviteBlockEntity {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
}
