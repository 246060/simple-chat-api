package xyz.jocn.chat.notification.enums;

public enum RoutingType {
	all("to.all"), channel("to.channel"), user("to.user");

	private String type;

	RoutingType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}

