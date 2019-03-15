package com.game.message;

public class Message {

	public int getMessageId() {
		MessageID annotation = this.getClass().getAnnotation(MessageID.class);
		if (annotation != null) {
			return annotation.ID();
		}
		return 0;
	}
}