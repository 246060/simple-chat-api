package xyz.jocn.chat.room.dto;

import lombok.Data;

@Data
public class RoomCreateRequestDto {
	private long hostId;
	private long inviteeId;

	public void setHostId(String hostId) {
		setHostId(Integer.parseInt(hostId));
	}

	public void setHostId(long hostId) {
		this.hostId = hostId;
	}
}
