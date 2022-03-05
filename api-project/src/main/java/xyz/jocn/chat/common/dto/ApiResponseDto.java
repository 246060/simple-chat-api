package xyz.jocn.chat.common.dto;

import lombok.Data;

@Data
public class ApiResponseDto {
	private boolean success;
	private Object meta;
	private Object data;
	private ErrorResponse error;

	private ApiResponseDto() {
	}

	public static ApiResponseDto success(Object data) {
		ApiResponseDto res = new ApiResponseDto();
		res.setSuccess(true);
		res.setData(data);
		return res;
	}

	public static ApiResponseDto success(Object data, Object meta) {
		ApiResponseDto res = new ApiResponseDto();
		res.setSuccess(true);
		res.setData(data);
		res.setMeta(meta);
		return res;
	}

	public static ApiResponseDto fail(ErrorResponse error) {
		ApiResponseDto res = new ApiResponseDto();
		res.setSuccess(false);
		res.setError(error);
		return res;
	}
}
