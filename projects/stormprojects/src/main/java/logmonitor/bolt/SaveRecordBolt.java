package logmonitor.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import logmonitor.domain.Record;
import logmonitor.utils.MonitorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将已经发送告警到记录保存到数据库.
 * @author mac
 * */
public class SaveRecordBolt extends BaseBasicBolt{

    private static Logger logger = LoggerFactory.getLogger(SaveRecordBolt.class);

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        Record record = (Record) tuple.getValueByField("record");
        MonitorHandler.save(record);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) { }
}
