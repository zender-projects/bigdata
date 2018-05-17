package kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import storm.bean.OrderInfo;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class OrderSender {


    public static void main(String[] args) {
        String TOPIC = "orderMq";
        Properties props = new Properties();
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "kafka01:9092,kafka02:9092,kafka03:9092");
        props.put("request.required.acks", "1");
        props.put("partitioner.class", "kafka.producer.DefaultPartitioner");
        //Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(props));
        for (int messageNo = 1; messageNo < 100000; messageNo++) {
            //producer.send(new KeyedMessage<String, String>(TOPIC, messageNo + "",new OrderInfo().random() ));
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

}
