package storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import domain.Message;
import util.MonitorHandler;

import java.util.Objects;

/**
 * 对Kafka中对数据进行过滤.
 * @author mac
 * */
public class FilterBolt extends BaseBasicBolt{
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //读取kafka中对数据
        String line = new String((byte[])tuple.getValue(0));
        Message message = MonitorHandler.parse(line);
        if(Objects.isNull(message)) {
            return;
        }
        if(MonitorHandler.trigger(message)) {
            basicOutputCollector.emit(new Values(message.getAppId(), message));
        }

        //定时更新规则信息
        MonitorHandler.scheduleLoad();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("appId", "message"));
    }
}
