/**
 * 
 */
package com.easyway.spring.netty.object;

import java.io.Serializable;

/**
 * 要传递对象，自然要有一个被传递模型，一个简单的Pojo，当然，实现序列化接口是必须的。
 * @author longgangbai
 * 2015-1-18  下午7:08:01
 */
public class Command implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String actionName;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    

}
