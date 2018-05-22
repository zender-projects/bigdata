package storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import domain.Message;
import domain.Record;
import lombok.extern.java.Log;
import org.apache.commons.beanutils.BeanUtils;
import util.MonitorHandler;

import java.lang.reflect.InvocationTargetException;

@Log
public class PrepareRecordBolt extends BaseBasicBolt{


    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        Message message = (Message) tuple.getValueByField("message");
        String appId = tuple.getStringByField("appId");
        //发送短信和邮件通知
        MonitorHandler.notify(appId, message);

        Record record = new Record();

            try {
                BeanUtils.copyProperties(record, message);
                basicOutputCollector.emit(appId, new Values(record));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                log.info("copy record data exception");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                log.info("copy record data exception");
            }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("record"));
    }
}
