package mapreduce.partitioner;

import mapreduce.bean.OrderBean;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 订单分区函数.
 * */
public class OrderIdPartitioner extends Partitioner<OrderBean, NullWritable>{

    @Override
    public int getPartition(OrderBean orderBean, NullWritable nullWritable, int reduceNum) {
        return (orderBean.getOrderId().hashCode() & Integer.MAX_VALUE) % reduceNum;
    }
}
