/**
 * 
 */
package com.game.player.message;

import java.util.ArrayList;
import java.util.List;

import com.game.message.Message;
import com.game.message.MessageID;
import com.game.message.MessageIds;

/**
 * @author caiweikai
 * @date 2019年3月18日
 */
@MessageID(ID = MessageIds.TEST)
public class ReqTestMessage implements Message {
	private int id;
	private String name;
	private List<TestBean> beans = new ArrayList<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TestBean> getBeans() {
		return beans;
	}
	public void setBeans(List<TestBean> beans) {
		this.beans = beans;
	}
}
