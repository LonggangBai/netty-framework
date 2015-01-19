/**
 * 
 */
package com.easyway.sample;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 
 * Spring / Netty
 * 
 * Spring是什么呢？首先它是一个开源的项目，而且目前非常活跃；它是一个基于IOC和AOP的构架多层j2ee系统的框架。
 * 
 * 两个概念：IOC/DI 和AOP
 * 
 * IOC：Inversion of
 * Control，控制反转，不创建对象，但是描述创建它们的方式。在代码中不直接与对象和服务连接，但在配置文件中描述哪一个组件需要哪一项服务
 * 。容器负责将这些联系在一起
 * 。举个例子，你写了一个类A，要在类B中引用类A的对象，通常的做法是在类B中通过代码直接实例化；而IOC则可以由Spring框架来实例化类A的对象
 * ，实例化的过程由框架本身控制。
 * 
 * DI：Dependency Injiection，依赖注入，IOC的机制导致很多类的实例化都需要在配置文件中配置，
 * 这就决定了需要通过这种特殊的机制才能把不同类之间的调用关系组合起来。由框架把需要的依赖关系注入到不同的业务逻辑中。
 * 
 * AOP：Aspect Oriented
 * Programming，面向切面编程，可以通过预编译方式和运行期动态代理实现在不修改源代码的情况下给程序动态统一添加功能的一种技术。
 * 
 * ==========================================================
 * 
 * Netty 提供异步的、事件驱动的网络应用程序框架和工具，用以快速开发高性能、高可靠性的网络服务器和客户端程序。
 * 
 * Netty特性：
 * 
 * 设计
 * 
 * - 统一的API，适用于不同的协议（阻塞和非阻塞）
 * 
 * - 基于灵活、可扩展的事件驱动模型
 * 
 * - 高度可定制的线程模型
 * 
 * - 可靠的无连接数据Socket支持（UDP）
 * 
 * 性能
 * 
 * *- 更好的吞吐量，低延迟
 * 
 * *- 更省资源
 * 
 * *- 尽量减少不必要的内存拷贝
 * 
 * 安全
 * 
 * *- 完整的SSL/TLS和STARTTLS的支持
 * 
 * *- 能在Applet与谷歌Android的限制环境运行良好
 * 
 * 健壮性
 * 
 * *- 不再因过快、过慢或超负载连接导致OutOfMemoryError
 * 
 * *- 不再有在高速网络环境下NIO读写频率不一致的问题
 * 
 * 易用
 * 
 * *- 完善的Java DOC，用户指南和样例
 * 
 * *- 简洁简单
 * 
 * *- 仅依赖于JDK 1.5
 * 
 * @author longgangbai 2015-1-16 下午3:36:32
 */
public class HelloServerHandler extends SimpleChannelHandler {

    /**
     * 当有客户端绑定到服务端的时候触发，打印"Hello world, I'm server."
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx,

    ChannelStateEvent e) {

	System.out.println("Hello world, I'm server.");

    }

}
