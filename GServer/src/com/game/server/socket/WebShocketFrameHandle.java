package com.game.server.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.game.message.Message;
import com.game.socket.DispatchMessage;
import com.game.utils.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebShocketFrameHandle {
	
	private final static Logger logger = LoggerFactory.getLogger(WebShocketFrameHandle.class);
	
	/** 处理websocket收到的消息   */
	public static void handleMsg(Channel channel, Object msg) {
		if (msg == null) {
			return;
		}
		// 二进制消息
		if (msg instanceof BinaryWebSocketFrame) {
			BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) msg;
			ByteBuf buf = binaryWebSocketFrame.content();
			Message message = (Message) Codec.decode(buf);
			if (message == null) {
				logger.error("消息解码为空");
				return;
			}
			DispatchMessage.getInst().dispatch(channel, message);
		}
		// 文本消息
		else if (msg instanceof TextWebSocketFrame) {
			String text = ((TextWebSocketFrame) msg).text();
			JSONObject object = (JSONObject) JSON.parse(text);
			String mid = String.valueOf(object.get("messageId"));
			int midInt = Integer.parseInt(mid);
			Class<?> messageClass = Codec.getMessageMap().get(midInt);
			if (messageClass != null) {
				Message message = (Message) JSON.parseObject(text, messageClass);
				if (message != null) {
					DispatchMessage.getInst().dispatch(channel, message);
				}
			}
		}
	}
}
