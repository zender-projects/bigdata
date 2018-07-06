package storm2.wc;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Arrays;
import java.util.Map;

public class WCSplitBolt extends BaseRichBolt{

    private OutputCollector _collector;
    private TopologyContext context;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this._collector = outputCollector;
        this.context = topologyContext;
    }

    @Override
    public void execute(Tuple tuple) {
        String line = tuple.getString(0);
        /*Arrays.asList(line.split(" ")).stream().forEach(word -> {
            this._collector.emit(new Values(word, 1));
        });*/
        String[] arr = line.split(" ");
        for(int i = 0;i < arr.length;i ++) {
            this._collector.emit(new Values(arr[i], 1));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word", "count"));
    }
}
