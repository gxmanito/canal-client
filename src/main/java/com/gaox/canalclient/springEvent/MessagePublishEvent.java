package com.gaox.canalclient.springEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description //TODO 事件发布
 * @Author GaoX
 * @Date 2020/6/29 16:41
 */
@RestController
public class MessagePublishEvent {

    private ApplicationContext applicationContext;

    public MessagePublishEvent(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @GetMapping("/publishEvent")
    public void publishEvent(String s){
        MessageEvent messageEvent = new MessageEvent("测试发送", "test="+s);
        applicationContext.publishEvent(messageEvent);
    }

}
