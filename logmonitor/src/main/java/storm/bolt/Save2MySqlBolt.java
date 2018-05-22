package storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import domain.Record;
import lombok.extern.java.Log;
import util.MonitorHandler;

import javax.management.monitor.Monitor;

/**
 * 将Record保存到数据库
 * @author mac
 * */
@Log
public class Save2MySqlBolt extends BaseBasicBolt{
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        Record record = (Record)tuple.getValueByField("record");
        MonitorHandler.save(record);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
