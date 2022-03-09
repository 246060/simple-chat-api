package xyz.jocn.chat.common.exception;

import xyz.jocn.chat.common.enums.ResourceType;

public class ResourceAlreadyExistException extends RuntimeException {

	private final String description = "%s already exist";
	private ResourceType resourceType;

	public String getDescription() {
		return String.format(description, resourceType);
	}

	public ResourceAlreadyExistException(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public ResourceAlreadyExistException(String message, ResourceType resourceType) {
		super(message);
		this.resourceType = resourceType;
	}

	public ResourceAlreadyExistException(String message, Throwable cause,
		ResourceType resourceType) {
		super(message, cause);
		this.resourceType = resourceType;
	}

	public ResourceAlreadyExistException(Throwable cause, ResourceType resourceType) {
		super(cause);
		this.resourceType = resourceType;
	}
}
