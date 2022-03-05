package xyz.jocn.chat.common.dto;

import lombok.Data;

@Data
public class PageMeta {
	private int pageIndex;
	private int sizePerPage;
	private long totalItems;
	private int totalPages;
	private boolean hasPrev;
	private boolean hasNext;
}
