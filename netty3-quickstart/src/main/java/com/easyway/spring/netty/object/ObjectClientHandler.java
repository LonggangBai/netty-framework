/**
 * 
 */
package com.easyway.spring.netty.object;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 对象传递，客户端代码
 * @author longgangbai 2015-1-18 下午7:10:48
 */
public class ObjectClientHandler extends SimpleChannelHandler {

    /**
     * 当绑定到服务端的时候触发，给服务端发消息。
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
	// 向服务端发送Object信息
	sendObject(e.getChannel());
    }

    /**
     * 发送Object
     */
    private void sendObject(Channel channel) {
	Command command = new Command();
	command.setActionName("Hello action.");
	channel.write(command);
    }

}