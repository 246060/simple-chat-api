package xyz.jocn.chat.file.repo;

import java.io.File;
import java.nio.file.Path;

public interface StorageRepository {

	void save();

	Path find();

	void delete();
}
