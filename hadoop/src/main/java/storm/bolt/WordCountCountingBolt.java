package storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计每个单词出现的次数
 * */
public class WordCountCountingBolt extends BaseRichBolt{

    private Map<String, Integer> counter = new HashMap<String, Integer>();
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        //直接打印，不输出到下游
        String word = tuple.getStringByField("word");
        Integer count = counter.get(word);
        if(count == null) {
            counter.put(word, 1);
        }else{
            counter.put(word, counter.get(word) + 1);
        }
        System.out.println("Bold [" + Thread.currentThread().getName() + "]: word - "
                + word + ", count - " + counter.get(word));

        //BaseRichBolt
        //ack
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) { }
}
