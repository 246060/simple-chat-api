package xyz.jocn.chat.firend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FriendEntity {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
}
