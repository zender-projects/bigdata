package storm.driver;

import backtype.storm.topology.TopologyBuilder;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

public class LogMonitorTopologyDriver {

    public static void main(String[] args) {

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        //配置kafka spout
        topologyBuilder.setSpout("kafkaSpout",
                    new KafkaSpout(
                            new SpoutConfig(
                                    new ZkHosts(""),
                                    "topic_monitor_log",  //topic名称
                                    "/logmonitor",  //zookeeper路径
                                    "kafkaSpout"  //zookeper路径上的id
                            )
                    ),
                1);  //并发度


    }
}
