/**
 * 
 */
package com.easyway.netty.http;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * 测试内容如下： http://127.0.0.1:8080/sdf?username=test1&password=1
 * bbd886460827015e5d605ed44252221
 * 
 * @author longgangbai 2015-1-19 上午9:36:11
 */
public class HttpServer {
    public static void main(String[] args) {
	// Configure the server.
	//创建ServerBootstrap 并创建服务端Channel工厂，配置bossExecutor，workerExecutor线程池的线程数量
	ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
		Executors.newCachedThreadPool()));
	//设置时间的通道工厂
	// Set up the event pipeline factory.
	bootstrap.setPipelineFactory(new HttpServerPipelineFactory());
        //绑定服务端的端口
	// Bind and start to accept incoming connections.
	bootstrap.bind(new InetSocketAddress(8080));
    }
}
