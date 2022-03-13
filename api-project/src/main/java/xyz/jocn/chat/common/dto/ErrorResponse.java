package xyz.jocn.chat.common.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;

import lombok.Data;

@Data
public class ErrorResponse {
	private int code;
	private String description;
	private Object detail;

	@Data
	public static class InvalidField {
		private String name;
		private Object value;
		private String reason;

		public InvalidField(String name, Object value, String reason) {
			this.name = name;
			this.value = value;
			this.reason = reason;
		}
	}

	public ErrorResponse(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public ErrorResponse(int code, String description, Object detail) {
		this.code = code;
		this.description = description;
		this.detail = detail;
	}

	public ErrorResponse(int code, String description, List<FieldError> fieldErrors) {
		this.code = code;
		this.description = description;
		this.detail = convertFieldError(fieldErrors);
	}

	private List<InvalidField> convertFieldError(List<FieldError> fields) {
		return fields.stream().map(
			fieldError -> new InvalidField(
				fieldError.getField(),
				fieldError.getRejectedValue(),
				fieldError.getDefaultMessage()
			)
		).collect(Collectors.toUnmodifiableList());
	}
}
