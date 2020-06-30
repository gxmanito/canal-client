package com.gaox.canalclient.springEvent;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description //TODO 监听器
 * @Author GaoX
 * @Date 2020/6/29 16:36
 */
@Component
public class MessageListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        //此处可以定义多个ApplicationEvent
        if(event instanceof MessageEvent){
            MessageEvent messageEvent = (MessageEvent)event;
            messageEvent.print();
            System.out.println(messageEvent.getSource());
            System.out.println(messageEvent.getText());
        }
    }
}
