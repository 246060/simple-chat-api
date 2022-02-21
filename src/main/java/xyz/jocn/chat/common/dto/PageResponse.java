package xyz.jocn.chat.common.dto;

import lombok.Data;

@Data
public class PageResponse<D> {
	private D data;
	private PageMeta metaData;

	public PageResponse(D data, PageMeta metaData) {
		this.data = data;
		this.metaData = metaData;
	}

	@Data
	public static class PageMeta {
		private int page;
		private int size;
		private long totalElements;
		private int totalPages;
		private boolean prev;
		private boolean next;
	}
}
