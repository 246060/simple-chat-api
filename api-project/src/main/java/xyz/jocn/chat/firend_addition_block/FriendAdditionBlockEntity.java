package xyz.jocn.chat.firend_addition_block;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FriendAdditionBlockEntity {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
}
