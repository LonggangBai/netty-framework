/**
 * 
 */
package com.easyway.netty.spring;

import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;

/**
 * 
 * @author longgangbai
 * 2015-1-19  上午9:47:49
 */
public class HttpRequestHandler extends SimpleChannelUpstreamHandler {

    private HttpRequest request;
    /** Buffer that stores the response content */
    private final StringBuilder buf = new StringBuilder();

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

	System.out.println("messageReceived");
	HttpRequest request = this.request = (HttpRequest) e.getMessage();

	buf.setLength(0);
	QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
	Map<String, List<String>> params = queryStringDecoder.getParameters();

	if (!params.isEmpty()) {
	    HttpResponseStatus httpResponseStatus = HttpResponseStatus.OK;
	    if (params.containsKey("username")) {
		if (params.containsKey("password")) {
		    List<String> values = params.get("username");
		    String username = "";
		    if (values.size() > 0) {
			username = values.get(0);
		    }
		    values = params.get("password");
		    String password = "";
		    if (values.size() > 0) {
			password = values.get(0);
		    }
		    try {
			buf.append("username=" + username + "   password" + password);
			// handle samething bussiness Logic
		    } catch (Exception e1) {
			e1.printStackTrace();
			buf.append("QUERY ERROR");
		    }
		} else {
		    buf.append("miss password");
		}
	    } else {
		buf.append("miss username");
	    }
	    writeResponse(e, httpResponseStatus, buf);
	} else {
	    buf.append("miss username and password");
	    writeResponse(e, OK, buf);
	}
    }

    private void writeResponse(MessageEvent e, HttpResponseStatus httpResponseStatus, StringBuilder buf) {
	// Decide whether to close the connection or not.
	boolean keepAlive = isKeepAlive(request);
	// Build the response object.
	HttpResponse response = new DefaultHttpResponse(HTTP_1_1, httpResponseStatus);
	response.setContent(ChannelBuffers.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));
	response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");

	// Write the response.
	ChannelFuture future = e.getChannel().write(response);

	// Close the non-keep-alive connection after the write operation is
	// done.
	future.addListener(ChannelFutureListener.CLOSE);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
	e.getCause().printStackTrace();
	e.getChannel().close();
    }

}
