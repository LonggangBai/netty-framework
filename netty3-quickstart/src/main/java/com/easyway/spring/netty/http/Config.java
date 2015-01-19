/**
 * 
 */
package com.easyway.spring.netty.http;

/**
 * @author longgangbai 2015-1-16 下午5:12:06
 */
public class Config {

    public static String getRealPath(String uri) {
	StringBuilder sb = new StringBuilder("D:/jee-workspace/AllinPayWeb/WebContent");
	sb.append(uri);
	if (!uri.endsWith("/")) {
	    sb.append('/');
	}
	return sb.toString();
    }
}