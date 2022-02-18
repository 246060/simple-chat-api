package xyz.jocn.chat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ThreadParticipantEntity {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
}
