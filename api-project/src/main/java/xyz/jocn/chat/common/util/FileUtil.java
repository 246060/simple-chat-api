package xyz.jocn.chat.common.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.util.StringUtils;

public class FileUtil {

	public static Path getSavedDir() {
		LocalDate now = LocalDate.now(ZoneOffset.UTC);
		return Path.of(Integer.toString(now.getYear()))
			.resolve(Integer.toString(now.getMonth().getValue()))
			.resolve(Integer.toString(now.getDayOfMonth()))
			.normalize();
	}

	public static String generateSavedFileName(String originFileName) {
		return new StringBuilder()
			.append(UUID.randomUUID().toString().replaceAll("-", ""))
			.append(".")
			.append(StringUtils.getFilenameExtension(originFileName))
			.toString();
	}

	public static Path generateTempFile(String ext) throws IOException {
		String prefix = UUID.randomUUID().toString().replaceAll("-", "");
		return Files.createTempFile(prefix, String.format(".%s", ext));
	}
}
