/**
 * 
 */
package com.easyway.spring.netty.object;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 
 * 说了这么多废话，才提到对象的传输，不知道您是不是已经不耐烦了。一个系统内部的消息传递，没有对象传递是不太现实的。下面就来说说，怎么传递对象。
 * 如果，您看过前面的介绍
 * ，如果您善于专注本质，勤于思考。您应该也会想到，我们说过，Netty的消息传递都是基于流，通过ChannelBuffer传递的，那么自然
 * ，Object也需要转换成ChannelBuffer来传递。好在Netty本身已经给我们写好了这样的转换工具。
 * ObjectEncoder和ObjectDecoder。
 * 工具怎么用？再一次说说所谓的本质，我们之前也说过，Netty给我们处理自己业务的空间是在灵活的可子定义的Handler上的
 * ，也就是说，如果我们自己去做这个转换工作
 * ，那么也应该在Handler里去做。而Netty，提供给我们的ObjectEncoder和Decoder也恰恰是一组
 * Handler。于是，修改Server和Client的启动代码： 对象传递服务端代码
 * 
 * @author longgangbai 2015-1-18 下午7:09:33
 */
public class ObjectServerHandler extends SimpleChannelHandler {

    /**
     * 当接受到消息的时候触发
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
	Command command = (Command) e.getMessage();
	// 打印看看是不是我们刚才传过来的那个

	System.out.println(command.getActionName());

    }
}
