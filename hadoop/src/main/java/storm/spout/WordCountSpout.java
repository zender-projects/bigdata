package storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class WordCountSpout extends BaseRichSpout{

    private SpoutOutputCollector collector;

    private Map<String, Object> ackMap = new HashMap<>();

    //模拟数据
    private String[] lines = {
            "this is first line",
            "this is second line",
            "this is third line"
    };

    //初始化时调用
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
    }

    //🈶️由框架循环调用
    @Override
    public void nextTuple() {
        //随即发送数据
        Random random = new Random(47);
        String randomLine = lines[random.nextInt(lines.length)];
        String messageId = UUID.randomUUID().toString();
        //向下游发送数据
        Values tuple = new Values((randomLine));
        collector.emit(messageId, tuple);
        //保存tuple，用于ack-fail
        ackMap.put(messageId, tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //设置输出数据的名称
        outputFieldsDeclarer.declare(new Fields("line"));
    }

    @Override
    public void ack(Object msgId) {
        String messageid = msgId.toString();
        //移除处理成功的tuple
        ackMap.remove(messageid);
    }

    @Override
    public void fail(Object msgId) {
        //处理失败，重发
        Values tuple = (Values)ackMap.get(msgId);
        collector.emit(msgId.toString(), tuple);
    }
}
