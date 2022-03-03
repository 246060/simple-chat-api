package xyz.jocn.chat.people_book;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PeopleBookEntity {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
}
