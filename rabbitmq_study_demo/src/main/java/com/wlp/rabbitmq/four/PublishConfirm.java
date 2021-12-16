package com.wlp.rabbitmq.four;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.wlp.rabbitmq.utils.RabbitMQUtils;

/**
 * 发布确认模式
 * 1、单个确认
 * 2、批量确认
 * 3、异步确认
 */
public class PublishConfirm {

    public static final String SINGLE_CONFIRM_QUEUE = "single_confirm_queue";
    public static final String BATCH_CONFIRM_QUEUE = "batch_confirm_queue";
    public static final String ASYNC_CONFIRM_QUEUE = "async_confirm_queue";

    public static final Integer MESSAGE_SIZE = 1000;

    public static void main(String[] args) throws Exception {

//        1、单个确认
//        singleConfirm();         //单个确认1000条消息，耗时：591

//        2、批量确认
//        batchConfirm();         //批量确认1000条消息，耗时：101ms

//        3、异步确认
        asyncConfirm();         //异步确认1000条消息，耗时：61ms
    }


    //1、单个确认
    public static void singleConfirm() throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        //开启发布确认
        channel.confirmSelect();

        channel.queueDeclare(SINGLE_CONFIRM_QUEUE, false, false, false, null);

        long begin = System.currentTimeMillis();

        for (Integer i = 0; i < MESSAGE_SIZE; i++) {
            channel.basicPublish("", SINGLE_CONFIRM_QUEUE, null, (i + "").getBytes());

            //确认
            boolean b = channel.waitForConfirms();
            if (b) {
                System.out.println("消息已经确认.....");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("批量确认" + MESSAGE_SIZE + "条消息，耗时：" + (end - begin) + "ms");
    }

    // 2、批量确认
    public static void batchConfirm() throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        //开启发布确认
        channel.confirmSelect();

        channel.queueDeclare(BATCH_CONFIRM_QUEUE, false, false, false, null);

        long begin = System.currentTimeMillis();

        int divide = 100;

        for (Integer i = 1; i <= MESSAGE_SIZE; i++) {
            channel.basicPublish("", SINGLE_CONFIRM_QUEUE, null, (i + "").getBytes());

            if (i % divide == 0) {
                //发布确认
                boolean b = channel.waitForConfirms();
                if (b) {
                    System.out.println("消息已经确认.....");
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("批量确认" + MESSAGE_SIZE + "条消息，耗时：" + (end - begin) + "ms");
    }


    // 3、异步确认
    public static void asyncConfirm() throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        //开启发布确认
        channel.confirmSelect();

        channel.queueDeclare(ASYNC_CONFIRM_QUEUE, false, false, false, null);

        //监听器回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {

            System.out.println("消息" + deliveryTag +"已经确认");
        };
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {

            System.out.println("消息" + deliveryTag +"未确认");
        };

        //添加消息监听器(异步确认)
        channel.addConfirmListener(ackCallback,nackCallback);

        long begin = System.currentTimeMillis();

        for (Integer i = 1; i <= MESSAGE_SIZE; i++) {
            channel.basicPublish("", SINGLE_CONFIRM_QUEUE, null, (i + "").getBytes());

        }

        long end = System.currentTimeMillis();
        System.out.println("异步确认" + MESSAGE_SIZE + "条消息，耗时：" + (end - begin) + "ms");
    }
}
