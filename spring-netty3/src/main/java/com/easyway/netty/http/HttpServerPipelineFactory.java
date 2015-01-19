/**
 * 
 */
package com.easyway.netty.http;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 重写通道信息
 * @author longgangbai 2015-1-19 上午9:36:42
 */
public class HttpServerPipelineFactory implements ChannelPipelineFactory {

    /**
     * 重写通道信息
     */
    @Override
    public ChannelPipeline getPipeline() throws Exception {
	// Create a default pipeline implementation.
	ChannelPipeline pipeline = pipeline();
        //配置需要的消息通道信息
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
	pipeline.addLast("handler", new HttpRequestHandler());
	return pipeline;
    }
}
