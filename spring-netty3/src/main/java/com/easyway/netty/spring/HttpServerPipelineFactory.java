/**
 * 
 */
package com.easyway.netty.spring;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 
 * @author longgangbai 2015-1-19 上午9:36:42
 */
public class HttpServerPipelineFactory implements ChannelPipelineFactory {
    private HttpRequestHandler httpRequestHandler;

    public void setHttpRequestHandler(HttpRequestHandler httpRequestHandler) {
	this.httpRequestHandler = httpRequestHandler;
    }

    public HttpRequestHandler getHttpRequestHandler() {
	return httpRequestHandler;
    }

    /**
     * <pre>
     * 此处用于创建管道链的方法本来想着通过注入的方式注入一个Map，但是发现需要考虑很多东西
     *  1.采用Map注入保持map的中元素的顺序
     *  2.保存map中并发和同步问题 暂时不做优化
     * 具体参考org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder类，采用责任链模式可以实现
     * </pre>
     */
    public ChannelPipeline getPipeline() throws Exception {
	// Create a default pipeline implementation.
	ChannelPipeline pipeline = pipeline();

	// Uncomment the following line if you want HTTPS
	// SSLEngine engine =
	// SecureChatSslContextFactory.getServerContext().createSSLEngine();
	// engine.setUseClientMode(false);
	// pipeline.addLast("ssl", new SslHandler(engine));

	pipeline.addLast("decoder", new HttpRequestDecoder());
	// Uncomment the following line if you don't want to handle HttpChunks.
	// pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
	pipeline.addLast("encoder", new HttpResponseEncoder());
	// Remove the following line if you don't want automatic content
	// compression.
	pipeline.addLast("deflater", new HttpContentCompressor());
	pipeline.addLast("handler", httpRequestHandler);
	return pipeline;
    }
}
