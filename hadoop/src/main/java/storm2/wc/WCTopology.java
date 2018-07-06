package storm2.wc;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import java.util.Objects;

public class WCTopology {

    public static void main(String[] args) throws Exception{

        TopologyBuilder builder = new TopologyBuilder();
        //名称，spout实体，并发度
        builder.setSpout("wcspout", new WCSpout(), 2);

        //名称，实体，并发度，规约规则
        builder.setBolt("wcsplitbolt", new WCSplitBolt(), 2).shuffleGrouping("wcspout");

        //名称，实体，并发度，规约规则，规约字段
        builder.setBolt("wccountbolt", new WCCountBolt(), 4).fieldsGrouping("wcsplitbolt", new Fields("word"));

        Config config = new Config();

        //设置worker数量
        config.setNumWorkers(3);

        if(!Objects.isNull(args) && args.length > 0) {
            //提交到集群
            StormSubmitter.submitTopology("wordcount-topology", config, builder.createTopology());
        }else{
            //本地多线程运行
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("wordcount-topolog", config, builder.createTopology());
        }
    }

}
