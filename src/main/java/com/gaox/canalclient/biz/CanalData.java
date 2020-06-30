package com.gaox.canalclient.biz;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description //TODO 数据处理
 * @Author GaoX
 * @Date 2020/6/30 9:12
 */
@Slf4j
@Component
public class CanalData {

    @Resource
    private CanalConnector canalConnector;

    @Scheduled(fixedDelay = 1000)
    public void run() {
        try {
            //获取指定数量的数据
            int batchSize = 1000;
            Message message = canalConnector.getWithoutAck(batchSize);
            // 数据批号
            long batchId = message.getId();
            try {
                List<CanalEntry.Entry> entries = message.getEntries();
                if (batchId != -1 && entries.size() > 0) {
                    printEntries(entries);
                }
                canalConnector.ack(batchId);// 提交确认
            } catch (Exception e) {
                log.info("发送监听事件失败！batchId回滚,batchId=" + batchId, e);
                //这次回滚后下次激活会继续收到这个binlog推送
                canalConnector.rollback(batchId);
            }
        } catch (Exception e) {
            log.error("canal异常！", e);
        }
    }

    private static void printEntries(List<CanalEntry.Entry> entries) {
        for (CanalEntry.Entry entry : entries) {
            if (entry.getEntryType() != CanalEntry.EntryType.ROWDATA) {
                continue;
            }

            CanalEntry.RowChange rowChange;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }


            CanalEntry.EventType eventType = rowChange.getEventType();
            log.info(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(), eventType));
            //entry.getHeader().getTableName()  此处可以更新表名处理不同业务
            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                switch (rowChange.getEventType()) {
                    case INSERT:
                        log.info("------->INSERT ");
                        printColumns(rowData.getAfterColumnsList());
                        break;
                    case UPDATE:
                        log.info("-------> UPDATE before");
                        printColumns(rowData.getBeforeColumnsList());
                        log.info("-------> UPDATE after");
                        printColumns(rowData.getAfterColumnsList());
                        break;
                    case DELETE:
                        log.info("------->DELETE ");
                        printColumns(rowData.getBeforeColumnsList());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static void printColumns(List<CanalEntry.Column> columns) {
        Map<String, Object> map = Maps.newHashMap();
        for (CanalEntry.Column column : columns) {
            map.put(column.getName(), column.getValue());
        }
        System.out.println(map);
        //以下可以对数据进行存储，比如更新Redis，存kafka等。。。。。。
    }

}
