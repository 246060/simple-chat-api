package xyz.jocn.chat.common.dto;

import lombok.Data;

@Data
public class PageDto<T> {
	private PageMeta meta;
	private T data;

	public PageDto() {

	}

	public PageDto(PageMeta meta, T data) {
		this.meta = meta;
		this.data = data;
	}
}
