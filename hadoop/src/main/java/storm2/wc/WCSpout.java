package storm2.wc;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import org.omg.CORBA.portable.ValueInputStream;

import java.util.Map;
import java.util.Random;

public class WCSpout extends BaseRichSpout{

    private TopologyContext _context;
    private SpoutOutputCollector _collector;
    private String[] sentences = {
            "this is first line",
            "this is second line",
            "this is third line"
    };

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this._context = topologyContext;
        this._collector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
        //Random random = new Random();
        //int index = random.nextInt(sentences.length);
        //Values values = new Values(sentences[index]);
        Utils.sleep(1000);
        this._collector.emit(new Values("this is the only line"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word"));
    }
}
