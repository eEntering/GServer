package com.game.message;

import com.game.message.annotation.MessageID;

public interface Message {

	default int getMessageId() {
		MessageID annotation = this.getClass().getAnnotation(MessageID.class);
		if (annotation != null) {
			return annotation.ID();
		}
		return 0;
	}
}
