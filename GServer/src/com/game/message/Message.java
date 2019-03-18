package com.game.message;

public interface Message {

	default int getMessageId() {
		MessageID annotation = this.getClass().getAnnotation(MessageID.class);
		if (annotation != null) {
			return annotation.ID();
		}
		return 0;
	}
}
