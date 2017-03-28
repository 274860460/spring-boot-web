package com.example.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Send {
    private final static String TASK_QUEUE_NAME = "hello_task";

    public static void main(String[] argv) throws java.io.IOException {
        try {
            ConnectionFactory factory = new ConnectionFactory();    // 连接工厂对象
            factory.setHost("rabbitmq-server");                            // 设置消息broker服务器的域名地址,其他配置项使用默认值
            Connection connection = factory.newConnection();        // 创建一个连接
            Channel channel = connection.createChannel();            // 通过connection得到一个channel
//             定义一个队列，
            channel.queueDeclare(TASK_QUEUE_NAME,    // 队列名
                    true,                            // 队列可持久化
                    false,                            // 队列非独占，如果是true则只被一个连接（connection）使用，而且当连接关闭后队列即被删除
                    false,                             // 当最后一个消费者退订后，队列立即删除
                    null                            // ther properties (construction arguments) for the queue,一些消息代理用他来完成类似与TTL的某些额外功能
            );
            String message = "Hello World .......";
//            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
//            System.out.println(" [x] Sent '" + message + "'");

//            message = getMessage(argv);

            channel.basicPublish("",    // exchangeName, 使用默认exchange，名字有服务器生产。
                    TASK_QUEUE_NAME,    // routingKey, 这儿使用与队列名相同的名字
                    MessageProperties.PERSISTENT_TEXT_PLAIN,    // 消息的属性， 内容是文本，并可以持久化
                    message.getBytes()        // 消息的内容
            );
            System.out.println(" [x] Sent '" + message + "'");
            channel.close();    // 关闭channel
            connection.close(); // 关闭连接

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
        }
    }
}