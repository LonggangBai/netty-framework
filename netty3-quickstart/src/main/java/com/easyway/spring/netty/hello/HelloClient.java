/**
 * 
 */
package com.easyway.spring.netty.hello;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * 先啰嗦两句，如果你还不知道Netty是做什么的能做什么。那可以先简单的搜索了解一下。我只能说Netty是一个NIO的框架，可以用于开发分布式的Java程序
 * 。具体能做什么，各位可以尽量发挥想象。技术，是服务于人而不是局限住人的。
 * 如果你已经万事具备，那么我们先从一段代码开始。程序员们习惯的上手第一步，自然是"Hello world"
 * ，不过Netty官网的例子却偏偏抛弃了"Hello world"。那我们就自己写一个最简单的"Hello world"的例子，作为上手。 Netty
 * 客户端代码
 * 
 * @author longgangbai 2015-1-18 下午6:30:51
 */
public class HelloClient {

    public static void main(String args[]) {
	// Client服务启动器
	ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
		Executors.newCachedThreadPool()));
	// 设置一个处理服务端消息和各种消息事件的类(Handler)
	bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
	    @Override
	    public ChannelPipeline getPipeline() throws Exception {
		return Channels.pipeline(new HelloClientHandler());
	    }
	});
	// 连接到本地的8000端口的服务端
	bootstrap.connect(new InetSocketAddress("127.0.0.1", 8000));
    }

    private static class HelloClientHandler extends SimpleChannelHandler {

	/**
	 * 当绑定到服务端的时候触发，打印"Hello world, I'm client."
	 * 
	 * @alia OneCoder
	 * @author lihzh
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
	    System.out.println("Hello world, I'm client.");
	}
    }
}
