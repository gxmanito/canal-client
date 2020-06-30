package com.gaox.canalclient.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.gaox.canalclient.config.CanalConfig;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @Description //TODO 客户端
 * @Author GaoX
 * @Date 2020/6/30 9:12
 */
@Component
@Slf4j
public class CanalClient implements DisposableBean {
    private CanalConnector canalConnector;

    @Resource
    private CanalConfig canalConfig;


    @Bean
    public CanalConnector getCanalConnector() {
        if(Objects.isNull(canalConfig)){
            throw new RuntimeException("ERROR ,CanalConfig is null");
        }
        String ip = canalConfig.getIp();
        int port = canalConfig.getPort();
        String destination = canalConfig.getDestination();
        String username = canalConfig.getUsername();
        String password = canalConfig.getPassword();
        String tables = canalConfig.getTables();
        if (StringUtils.isEmpty(ip)) {
            ip = AddressUtils.getHostIp();
        }
        // 创建链接
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
        canalConnector = CanalConnectors.newClusterConnector(Lists.newArrayList(inetSocketAddress), destination, username, password);
        // 链接canal
        canalConnector.connect();
        // 指定filter，格式 {database}.{table}，逗号分割
        canalConnector.subscribe(tables);
        // 回滚寻找上次中断的位置
        canalConnector.rollback();
        log.info("canal客户端启动成功");
        return canalConnector;
    }

    /*
     * @Author: GaoX
     * @Description : 当bean销毁时断开canal的链接
     * @Params: []
     * @Return: void
     * @Date: 2020/6/30 9:28
     */
    @Override
    public void destroy() {
        if (Objects.nonNull(canalConnector)) {
            log.info("关闭canalConnector");
            canalConnector.disconnect();
        }
    }
}