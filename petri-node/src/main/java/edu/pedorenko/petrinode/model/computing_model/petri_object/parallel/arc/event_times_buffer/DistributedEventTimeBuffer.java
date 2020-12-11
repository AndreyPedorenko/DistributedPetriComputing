package edu.pedorenko.petrinode.model.computing_model.petri_object.parallel.arc.event_times_buffer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import edu.pedorenko.petrinode.util.DoubleToByteArrayUtil;

public class DistributedEventTimeBuffer implements EventTimesBuffer {

    private ParallelEventTimesBuffer parallelEventTimesBuffer = new ParallelEventTimesBuffer();

    private String arcId;

    private Channel channel;

    public DistributedEventTimeBuffer(String arcId, boolean sender) {
        this.arcId = arcId;

        if (!sender) {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");

                Connection connection = factory.newConnection();

                channel = connection.createChannel();

                channel.queueDeclare(arcId, false, false, false, null);

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    receiveTime(DoubleToByteArrayUtil.toDouble(delivery.getBody()));
                };

                channel.basicConsume(arcId, true, deliverCallback, consumerTag -> {
                });
            } catch (Exception ex) {
                throw new RuntimeException(ex);//TODO something
            }
        }
    }

    public double getNearestTime() {
        return parallelEventTimesBuffer.getNearestTime();
    }

    public double getAndRemoveNearestTime() {
        return parallelEventTimesBuffer.getAndRemoveNearestTime();
    }

    public void addTime(double time) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channelOut = connection.createChannel()) {

            channelOut.basicPublish("", arcId, null, DoubleToByteArrayUtil.toByteArray(time));

        } catch (Exception ex) {
            throw new RuntimeException(ex);//TODO something
        }

    }

    public void receiveTime(double time) {
        parallelEventTimesBuffer.addTime(time);
        if (channel != null && time == Double.MAX_VALUE) {
            try {
                channel.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int getBufferSize() {
        return parallelEventTimesBuffer.getBufferSize();
    }
}
