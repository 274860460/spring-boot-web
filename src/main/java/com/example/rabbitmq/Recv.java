package com.example.rabbitmq;


import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {
    private final static String TASK_QUEUE_NAME = "hello_task";

    public static void main(String[] argv) throws java.io.IOException, java.lang.InterruptedException {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("rabbitmq-server");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
//            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            channel.basicQos(1);    // 设置限速。在多个消费者共享一个队列的案例中，明确指定在收到下一个确认回执前每个消费者一次可以接受多少条消息
            final Consumer consumer = new DefaultConsumer(channel) {    // 定义一个消费者类
                int i = 0;

                // 当有消息到来则该方法被调用。
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");

                    System.out.println(String.format(" [%d] Received '%s'", ++i, message));
                    try {
                        doWork(message);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
//                        System.out.println(" [x] " + message + " Done");
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            channel.basicConsume(TASK_QUEUE_NAME, false, consumer);    // 把上面new出的消费者注册到channel中。

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private static void doWork(String task) throws InterruptedException {
//        for (char ch : task.toCharArray()) {
//            if (ch == '.')
//                Thread.sleep(3000);
//        }
    }
}