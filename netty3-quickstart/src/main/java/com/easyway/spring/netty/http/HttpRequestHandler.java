/**
 * 
 */
package com.easyway.spring.netty.http;

import static org.jboss.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.COOKIE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunkTrailer;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.stream.ChunkedFile;
import org.jboss.netty.util.CharsetUtil;

/**
 * @author longgangbai 2015-1-16 下午5:11:30
 */
public class HttpRequestHandler extends SimpleChannelUpstreamHandler {

    private HttpRequest request;
    private boolean readingChunks;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
	if (!readingChunks) {
	    HttpRequest request = this.request = (HttpRequest) e.getMessage();
	    String uri = request.getUri();
	    System.out.println("-----------------------------------------------------------------");
	    System.out.println("uri:" + uri);
	    System.out.println("-----------------------------------------------------------------");
	    /**
	     * 100 Continue
	     * 是这样的一种情况：HTTP客户端程序有一个实体的主体部分要发送给服务器，但希望在发送之前查看下服务器是否会
	     * 接受这个实体，所以在发送实体之前先发送了一个携带100
	     * Continue的Expect请求首部的请求。服务器在收到这样的请求后，应该用 100 Continue或一条错误码来进行响应。
	     */
	    if (is100ContinueExpected(request)) {
		send100Continue(e);
	    }
	    // 解析http头部
	    for (Map.Entry<String, String> h : request.getHeaders()) {
		System.out.println("HEADER: " + h.getKey() + " = " + h.getValue() + "\r\n");
	    }
	    // 解析请求参数
	    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
	    Map<String, List<String>> params = queryStringDecoder.getParameters();
	    if (!params.isEmpty()) {
		for (Entry<String, List<String>> p : params.entrySet()) {
		    String key = p.getKey();
		    List<String> vals = p.getValue();
		    for (String val : vals) {
			System.out.println("PARAM: " + key + " = " + val + "\r\n");
		    }
		}
	    }
	    if (request.isChunked()) {
		readingChunks = true;
	    } else {
		ChannelBuffer content = request.getContent();
		if (content.readable()) {
		    System.out.println(content.toString(CharsetUtil.UTF_8));
		}
		writeResponse(e, uri);
	    }
	} else {// 为分块编码时
	    HttpChunk chunk = (HttpChunk) e.getMessage();
	    if (chunk.isLast()) {
		readingChunks = false;
		// END OF CONTENT\r\n"
		HttpChunkTrailer trailer = (HttpChunkTrailer) chunk;
		if (!trailer.getHeaderNames().isEmpty()) {
		    for (String name : trailer.getHeaderNames()) {
			for (String value : trailer.getHeaders(name)) {
			    // System.out.println("TRAILING HEADER: " + name +
			    // " = " + value);
			}
		    }
		}
		// writeResponse(e, "/");
	    } else {
		// System.out.println("CHUNK: " +
		// chunk.getContent().toString(CharsetUtil.UTF_8));
	    }
	}
    }

    private void writeResponse(MessageEvent e, String uri) {
	// 解析Connection首部，判断是否为持久连接
	boolean keepAlive = isKeepAlive(request);

	// Build the response object.
	HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
	response.setStatus(HttpResponseStatus.OK);
	// 服务端可以通过location首部将客户端导向某个资源的地址。
	// response.addHeader("Location", uri);
	if (keepAlive) {
	    // Add 'Content-Length' header only for a keep-alive connection.
	    response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
	}
	// 得到客户端的cookie信息，并再次写到客户端
	String cookieString = request.getHeader(COOKIE);
	if (cookieString != null) {
	    CookieDecoder cookieDecoder = new CookieDecoder();
	    Set<Cookie> cookies = cookieDecoder.decode(cookieString);
	    if (!cookies.isEmpty()) {
		CookieEncoder cookieEncoder = new CookieEncoder(true);
		for (Cookie cookie : cookies) {
		    cookieEncoder.addCookie(cookie);
		}
		response.addHeader(SET_COOKIE, cookieEncoder.encode());
	    }
	}
	final String path = Config.getRealPath(uri);
	File localFile = new File(path);
	// 如果文件隐藏或者不存在
	if (localFile.isHidden() || !localFile.exists()) {
	    // 逻辑处理
	    return;
	}
	// 如果请求路径为目录
	if (localFile.isDirectory()) {
	    // 逻辑处理
	    return;
	}
	RandomAccessFile raf = null;
	try {
	    raf = new RandomAccessFile(localFile, "r");
	    long fileLength = raf.length();
	    response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(fileLength));
	    Channel ch = e.getChannel();
	    ch.write(response);
	    // 这里又要重新温习下http的方法，head方法与get方法类似，但是服务器在响应中只返回首部，不会返回实体的主体部分
	    if (!request.getMethod().equals(HttpMethod.HEAD)) {
		ch.write(new ChunkedFile(raf, 0, fileLength, 8192));
	    }
	} catch (Exception e2) {
	    e2.printStackTrace();
	} finally {
	    if (keepAlive) {
		response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
	    }
	    if (!keepAlive) {
		e.getFuture().addListener(ChannelFutureListener.CLOSE);
	    }
	}
    }

    private void send100Continue(MessageEvent e) {
	HttpResponse response = new DefaultHttpResponse(HTTP_1_1, CONTINUE);
	e.getChannel().write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
	e.getCause().printStackTrace();
	e.getChannel().close();
    }
}
