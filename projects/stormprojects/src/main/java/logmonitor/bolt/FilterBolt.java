package logmonitor.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import logmonitor.domain.Message;
import logmonitor.utils.MonitorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class FilterBolt extends BaseBasicBolt
{
    private static Logger logger = LoggerFactory.getLogger(FilterBolt.class);

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        byte[] value = (byte[])tuple.getValue(0);
        String line = new String(value);
        Message message = MonitorHandler.parse(line);
        if(Objects.isNull(message)) {
            return ;
        }

        //判断日志信息是否需要触发告警规则
        if(MonitorHandler.trigger(message)) {
            //发送到下一个bolt
            basicOutputCollector.emit(new Values(message.getAppId(), message));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("appId", "message"));
    }
}
