package xyz.jocn.chat.common.util;

import static org.assertj.core.api.Assertions.*;
import static xyz.jocn.chat.common.util.AppUtil.*;

import org.junit.jupiter.api.Test;

class AppUtilTest {

	@Test
	void isNotResourceOwner1() {
		// given
		Long id1 = 1L;
		Long id2 = 2L;

		// when
		boolean isTrue = isNotResourceOwner(id1, id2);

		// then
		assertThat(isTrue).isTrue();
	}

	@Test
	void isNotResourceOwner2() {
		// given
		Long id1 = 1L;
		Long id2 = 1L;

		// when
		boolean isFalse = isNotResourceOwner(id1, id2);

		// then
		assertThat(isFalse).isFalse();
	}

	@Test
	void isResourceOwner1() {
		// given
		Long id1 = 1L;
		Long id2 = 1L;

		// when
		boolean isTrue = isResourceOwner(id1, id2);

		// then
		assertThat(isTrue).isTrue();
	}

	@Test
	void isResourceOwner2() {
		// given
		Long id1 = 1L;
		Long id2 = 2L;

		// when
		boolean isFalse = isResourceOwner(id1, id2);

		// then
		assertThat(isFalse).isFalse();
	}
}