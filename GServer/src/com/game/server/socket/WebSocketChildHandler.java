package com.game.server.socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 *  WebSocket连接使用的
 * @author caiweikai
 * @date 2019年4月2日
 */
public class WebSocketChildHandler extends ChannelInitializer<SocketChannel> {

	private final static WebSocketServerHandler WEB_SOCKET_HANDLER = new WebSocketServerHandler();

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline()
				// 将请求和应答消息解码为http消息
				.addLast("http_code", new HttpServerCodec())
				// 将http消息的多个部分合成一个完整的http消息
				.addLast("aggregator", new HttpObjectAggregator(65536))
				// 向客户端发送html5文件
				.addLast("http-chuked", new ChunkedWriteHandler())
				//用于处理websocket, /ws为访问websocket时的uri
		        .addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/"))
				// 自定义handler
				.addLast("websocket-handler", WEB_SOCKET_HANDLER);
	}

}
