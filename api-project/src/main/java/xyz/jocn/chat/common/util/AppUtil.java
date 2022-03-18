package xyz.jocn.chat.common.util;

public class AppUtil {

	public static boolean isResourceOwner(Long id1, Long id2) {
		return id1 == id2;
	}

	public static  boolean isNotResourceOwner(Long id1, Long id2) {
		return !isResourceOwner(id1, id2);
	}
}
