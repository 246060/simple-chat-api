package xyz.jocn.chat.common.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SliceCriteria<T> {

	private T basePoint;
	private SliceDirection direction;
	private Integer limit;

	public SliceCriteria(T basePoint) {
		this(basePoint, SliceDirection.up);
	}

	public SliceCriteria(T basePoint, Integer limit) {
		this(basePoint, SliceDirection.up, limit);
	}

	public SliceCriteria(T basePoint, SliceDirection direction) {
		this(basePoint, direction, 10);
	}

	public SliceCriteria(T basePoint, SliceDirection direction, Integer limit) {
		this.basePoint = basePoint;
		this.direction = direction;
		this.limit = limit;
	}

	public void updateNextBasePoint(T nextBasePoint) {
		this.basePoint = nextBasePoint;
	}
}
