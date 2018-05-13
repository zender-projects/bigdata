package storm.driver;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import storm.bolt.WordCountCountingBolt;
import storm.bolt.WordCountSplitBolt;
import storm.spout.WordCountSpout;

import javax.security.auth.login.Configuration;

/**
 * WordCoun驱动类.
 * @author mac
 * */
public class WordCountTopologyDriver {

    public static void main(String[] args) {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("data-source", new WordCountSpout(), 2);
        //随即分配到下游的SplitBolt
        builder.setBolt("split-task", new WordCountSplitBolt(), 2)
                                .shuffleGrouping("data-source");

        //按字段值hash分发到下游到统计数量的Bolt
        builder.setBolt("word-counter", new WordCountCountingBolt(), 4)
                    .fieldsGrouping("split-task", new Fields("word"));

        Config config = new Config();

        config.setDebug(true);
        //设置占用worker的数量
        config.setNumWorkers(2);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("wordcount-topology", config, builder.createTopology());
    }
}
