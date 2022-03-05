package xyz.jocn.chat.user.entity;

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
