/**
 * 
 */
package com.easyway.spring.netty.hello;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * Netty 服务端代码
 * 
 * 既然是分布式的，自然要分多个服务。Netty中，需要区分Server和Client服务。所有的Client都是绑定在Server上的，
 * 他们之间是不能通过Netty直接通信的
 * 。（自己采用的其他手段，不包括在内。）。白话一下这个通信过程，Server端开放端口，供Client连接，Client发起请求
 * ，连接到Server指定的端口，完成绑定。随后便可自由通信。其实就是普通Socket连接通信的过程。
 * Netty框架是基于事件机制的，简单说，就是发生什么事
 * ，就找相关处理方法。就跟着火了找119，抢劫了找110一个道理。所以，这里，我们处理的是当客户端和服务端完成连接以后的这个事件
 * 。什么时候完成的连接，Netty知道，他告诉我了，我就负责处理。这就是框架的作用。Netty，提供的事件还有很多，以后会慢慢的接触和介绍。
 * 你应该已经可以上手了：）
 * 
 * @author longgangbai 2015-1-18 下午6:29:42
 */
public class HelloServer {

    public static void main(String args[]) {
	// Server服务启动器
	ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
		Executors.newCachedThreadPool()));
	// 设置一个处理客户端消息和各种消息事件的类(Handler)
	bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
	    @Override
	    public ChannelPipeline getPipeline() throws Exception {
		return Channels.pipeline(new HelloServerHandler());
	    }
	});
	// 开放8000端口供客户端访问。
	bootstrap.bind(new InetSocketAddress(8000));
    }

    private static class HelloServerHandler extends SimpleChannelHandler {

	/**
	 * 当有客户端绑定到服务端的时候触发，打印"Hello world, I'm server."
	 * 
	 * @alia OneCoder
	 * @author lihzh
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
	    System.out.println("Hello world, I'm server.");
	}
    }
}
