package com.yiqzq.miaoshasystem.mq;

import com.yiqzq.miaoshasystem.config.MQConfig;
import com.yiqzq.miaoshasystem.pojo.MessageInSeckillqueue;
import com.yiqzq.miaoshasystem.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yiqzq
 * @date 2020/6/8 15:55
 */
@Slf4j
@Service
public class MQSender {


    @Autowired
    AmqpTemplate amqpTemplate;

    //    public void sendSeckillMessage(SeckillMessage mm) {
//        String msg = RedisUtil.beanToString(mm);
//        log.info("send message:" + msg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
//    }
    public void sendMsg(MessageInSeckillqueue message) {
        String msg = RedisUtil.beanToString(message);
        log.info("send message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
    }

}
