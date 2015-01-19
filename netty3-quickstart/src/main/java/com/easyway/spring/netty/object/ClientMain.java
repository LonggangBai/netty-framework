/**
 * 
 */
package com.easyway.spring.netty.object;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 通过Netty传递，都需要基于流，以ChannelBuffer的形式传递。所以，Object -> ChannelBuffer.
  *Netty提供了转换工具，需要我们配置到Handler。
  *样例从客户端 -> 服务端，单向发消息，所以在客户端配置了编码，服务端解码。如果双向收发，则需要全部配置Encoder和Decoder。
  *这里需要注意，注册到Server的Handler是有顺序的，如果你颠倒一下注册顺序：
 * @author longgangbai 2015-1-18 下午7:06:15
 */
public class ClientMain {

    public static void main(String[] args) {
	// Client服务启动器
	ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
		Executors.newCachedThreadPool()));
	// 设置一个处理客户端消息和各种消息事件的类(Handler)

	bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

	    @Override
	    public ChannelPipeline getPipeline() throws Exception {

		return Channels.pipeline(new ObjectEncoder(), new ObjectClientHandler());

	    }

	});
	// 开放8000端口供客户端访问。
	bootstrap.connect(new InetSocketAddress(8000));
    }

}
