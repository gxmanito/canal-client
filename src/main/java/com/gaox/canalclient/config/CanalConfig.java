package com.gaox.canalclient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Description //TODO Canal配置
 * @Author GaoX
 * @Date 2020/6/30 9:12
 */
@Configuration
@ConfigurationProperties(prefix = "canal")
@Data
public class CanalConfig {
    //对应canal.properties
    //canal.ip =
    private String ip;
    //canal.port = 11111
    private int port = 11111;
    //canal.destinations = example
    private String destination = "example";
    //canal.user
    private String username;
    //canal.passwd
    private String password;
    //指定监听的表 格式 {database}.{table}，逗号分割
    private String tables;
}