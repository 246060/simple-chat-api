package xyz.jocn.chat.channel;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import xyz.jocn.chat.channel.dto.ChannelDto;

class ChannelConverterTest {

	ChannelConverter converter = ChannelConverter.INSTANCE;

	@DisplayName("RoomDto toDto(RoomEntity entity)")
	@Test
	void toDto() {
		// given
		Long userId = 1L;
		Long roomId = 100L;

		ChannelEntity room =
			ChannelEntity.builder()
				.id(roomId)
				.build();

		// when
		ChannelDto roomDto = converter.toDto(room);
		System.out.println("roomDto = " + roomDto);

		// then
		assertThat(roomDto).extracting(ChannelDto::getId).isEqualTo(roomId);
	}
}