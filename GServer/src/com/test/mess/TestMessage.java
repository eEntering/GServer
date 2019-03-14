package com.test.mess;

/** 测试消息 */
@MessageID(ID = 1)
public class TestMessage extends Message {

	private int i;
	private String string;

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
