package com.test;

import com.test.server.HandlerServer;

public class Start {
	
	
	
	
	public static void main(String[] args) throws Exception {
		System.out.println("-------------");
		new HandlerServer().run();
		System.out.println("------end-----");
	}
}
