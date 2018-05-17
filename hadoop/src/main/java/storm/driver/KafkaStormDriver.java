package storm.driver;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import clojure.lang.Obj;
import storm.bolt.RedisBolt;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

import java.util.Objects;

public class KafkaStormDriver {


    public static void main(String[] args) throws Exception{

        TopologyBuilder builder = new TopologyBuilder();
        /*builder.setSpout("kafkaSpout",
                new KafkaSpout(
                        new ZkHosts("node01:2181,node02:2181,node03:2181"),
                        "kafkaMq",
                        "/myKafka",
                        "kafkaSpout"
                )), 1);*/

        builder.setSpout("kafkaSpout",
                new KafkaSpout(
                        new SpoutConfig(
                                new ZkHosts("node01:2181,node02:2181,node03:2181"),
                                "demoTopic",
                                "/myKafka",
                                "kafkaSpout"
                        )
                ),
                1);

        builder.setBolt("redisBolt", new RedisBolt(), 1)
                .shuffleGrouping("kafkaSpout");

        Config config = new Config();
        config.setNumWorkers(1);

        if(!Objects.isNull(args) && args.length > 0) {
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        }else{
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("kafkaToStorm", config, builder.createTopology());
        }

    }
}
