package com.wjjzst.queue.rabbitmq.rabbitmqapi.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Wjj
 * @Date: 2019/8/22 1:03
 * @desc:
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1 创建连接工厂ConnectionFactory 并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("129.204.35.106");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("wjj");
        connectionFactory.setPassword("wzzst310");

        // 2 通过连接工厂创建一个连接
        Connection connection = connectionFactory.newConnection();
        // 3 通过connection创建一个channel
        Channel channel = connection.createChannel();
        // 4 指定我们的消息投递模式: 消息的确认模式
        channel.confirmSelect();
        // 5 声明
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.save";

        // 5 通过channel发送数据
        for (int i = 0; i < 100; i++) {
            String msg = "Hello RabbitMQ! 4 DLX Message!";
            msg += " ----> " + i;
            // basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
            // 不传exchange: AMQP default(第一个)
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)  // 1持久化 2非持久化
                    .contentEncoding("UTF-8")
                    .expiration("10000")
                    .build();
            channel.basicPublish(exchangeName, routingKey, true, properties, msg.getBytes());
        }
        // 6 记得关闭相关的连接
        // channel.close();
        // connection.close();
    }
}
