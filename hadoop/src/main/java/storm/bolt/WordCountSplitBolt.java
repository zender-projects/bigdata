package storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * 将行数据分割为单词
 * */
public class WordCountSplitBolt extends BaseRichBolt{

    private OutputCollector collector;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        //获取上游数据
        String line = tuple.getStringByField("line");
        //分割，并发送到下游
        String[] words = line.split(" ");

        for(String word : words) {
            collector.emit(new Values(word));
        }
        //通知_ack_ task 进行异或运算
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //声明发往下游数据的字段名称
    }
}
