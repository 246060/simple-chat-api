package xyz.jocn.chat.room;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import xyz.jocn.chat.room.RoomConverter;
import xyz.jocn.chat.room.dto.RoomDto;
import xyz.jocn.chat.room.RoomEntity;
import xyz.jocn.chat.user.UserEntity;

class RoomConverterTest {

	RoomConverter converter = RoomConverter.INSTANCE;

	@DisplayName("RoomDto toDto(RoomEntity entity)")
	@Test
	void toDto() {
		// given
		Long userId = 1L;
		Long roomId = 100L;

		RoomEntity room =
			RoomEntity.builder()
				.id(roomId)
				.build();

		// when
		RoomDto roomDto = converter.toDto(room);
		System.out.println("roomDto = " + roomDto);

		// then
		assertThat(roomDto).extracting(RoomDto::getRoomId).isEqualTo(roomId);
	}
}