package logmonitor.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import logmonitor.domain.Message;
import logmonitor.domain.Record;
import logmonitor.utils.MonitorHandler;
import org.apache.commons.beanutils.BeanUtils;

public class NotifyBolt extends BaseBasicBolt
{


    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        Message message = (Message)tuple.getValueByField("message");
        String appId = tuple.getStringByField("appId");
        //告警通知
        MonitorHandler.toNotify(appId, message);
        Record record = new Record();
        try{
            BeanUtils.copyProperties(record, message);
            //发送到下一个Bolt
            basicOutputCollector.emit(new Values(record));
        }catch (Exception ex) { }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("record"));
    }
}
