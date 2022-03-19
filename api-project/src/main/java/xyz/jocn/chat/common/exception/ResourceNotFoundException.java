package xyz.jocn.chat.common.exception;

public class ResourceNotFoundException extends RuntimeException {
	private final String description = "%s not found";
	private ResourceType resourceType;

	public String getDescription() {
		return String.format(description, resourceType);
	}

	public ResourceNotFoundException(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public ResourceNotFoundException(String message, ResourceType resourceType) {
		super(message);
		this.resourceType = resourceType;
	}

	public ResourceNotFoundException(String message, Throwable cause, ResourceType resourceType) {
		super(message, cause);
		this.resourceType = resourceType;
	}

	public ResourceNotFoundException(Throwable cause, ResourceType resourceType) {
		super(cause);
		this.resourceType = resourceType;
	}
}
