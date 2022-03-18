package xyz.jocn.chat.file.repo;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageLocalRepository implements StorageRepository {

	@Override
	public void save() {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());
	}

	@Override
	public Path find() {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());
		return null;
	}

	@Override
	public void delete() {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());
	}
}
