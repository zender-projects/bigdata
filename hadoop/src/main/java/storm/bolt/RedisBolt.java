package storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import storm.bean.OrderInfo;

import java.util.HashMap;
import java.util.Map;

public class RedisBolt extends BaseRichBolt{

    private JedisPool pool;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setMaxTotal(1000 * 100);
        config.setMaxWaitMillis(30);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        pool = new JedisPool(config, "127.0.0.1", 6379);
    }

    @Override
    public void execute(Tuple tuple) {
        Jedis jedis = pool.getResource();
        //从kafka获取一条json数据
        String json = new String((byte[])tuple.getValue(0));
        //解析json成对象
        OrderInfo orderInfo = (OrderInfo)new Gson().fromJson(json, OrderInfo.class);
        //插入统计数据
        jedis.incrBy("totalAmount", orderInfo.getProductPrice());
        String bid = getDescByProductId(orderInfo.getProductId(), "b");
        jedis.incrBy(bid + "Amount", orderInfo.getProductPrice());
        //释放redis链接
        jedis.close();
    }

    public String getDescByProductId(String productId, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("b", "3c");
        map.put("c", "phone");
        map.put("s", "iphone");
        return map.get(type);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
