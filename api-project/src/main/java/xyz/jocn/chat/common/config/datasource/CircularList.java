package xyz.jocn.chat.common.config.datasource;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CircularList<T> {

	private final List<T> list;
	private Integer counter = 0;

	public CircularList(List<T> list) {
		this.list = list;
	}

	public T getOne() {
		if (counter + 1 >= list.size()) {
			counter = -1;
		}

		log.info("Replica Count : {}", list.size());
		return list.get(++counter);
	}
}
