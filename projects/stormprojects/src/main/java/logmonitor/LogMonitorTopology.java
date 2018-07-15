package logmonitor;

import backtype.storm.topology.TopologyBuilder;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

public class LogMonitorTopology {

    public static void main(String[] args) {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaSpout",
                new KafkaSpout(
                    new SpoutConfig(
                            new ZkHosts("hadoop01:2181,hadoop02:2181,hadoop03:2181"),
                            "logTopic","/logMq", "logMonitorSpout")
        ),1);


    }
}
