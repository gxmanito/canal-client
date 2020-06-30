package com.gaox.canalclient.springEvent;

import org.springframework.context.ApplicationEvent;

/**
 * @Description //TODO 容器事件
 * @Author GaoX
 * @Date 2020/6/29 16:33
 */
public class MessageEvent extends ApplicationEvent {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageEvent(Object source) {
        super(source);
    }

    public MessageEvent(Object source, String text) {
        super(source);
        this.text = text;
    }

    public void print(){
        System.out.println("监听器调用此方法！！！！！！！！！！！！");
    }

}
