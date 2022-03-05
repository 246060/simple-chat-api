package xyz.jocn.chat.friend_group;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FriendGroupEntity {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
}
