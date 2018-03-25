package mapreduce;

import mapreduce.bean.OrderBean;
import mapreduce.groupingcomparator.OrderGroupingComparator;
import mapreduce.partitioner.OrderIdPartitioner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * 求每个订单中最大金额的条目
 *
 Order_0000001, Pdt_01, 222.8
 Order_0000001, Pdt_05, 25.8
 Order_0000002, Pdt_05, 325.8
 Order_0000002, Pdt_03, 522.8
 Order_0000002, Pdt_04, 122.4
 Order_0000003, Pdt_01, 222.8
 * */
public class OrderMaxPrice extends Configured implements Tool {




    public static class OrderMaxPriceMapper extends Mapper<LongWritable, Text,
            OrderBean, NullWritable> {

        OrderBean orderBean = new OrderBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split(",");
            orderBean.setOrderId(fields[0]);
            orderBean.setAmount(Double.parseDouble(fields[2]));
            context.write(orderBean, NullWritable.get());
        }
    }

    public static class OrderMaxPriceReducer extends Reducer<OrderBean, NullWritable, Text, NullWritable> {
        Text k = new Text();
        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            //到达reduce时，orderid相同的bean已经被看成一组，且金额最大的那个排在第一个，直接将输入输出即可
            String kStr = key.getOrderId() + "-" + key.getAmount();
            k.set(kStr);
            context.write(k, NullWritable.get());
        }
    }


    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = getConf();
        configuration.set("mapreduce.framework.name", "local");
        configuration.set("fs.defaultFS", "file:///");

        Job job = Job.getInstance(configuration);

        job.setJarByClass(OrderMaxPrice.class);

        job.setMapperClass(OrderMaxPriceMapper.class);
        job.setReducerClass(OrderMaxPriceReducer.class);

        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);

        //设置分组比较器
        job.setGroupingComparatorClass(OrderGroupingComparator.class);
        //设置分区函数
        job.setPartitionerClass(OrderIdPartitioner.class);

        job.setNumReduceTasks(1);

        FileInputFormat.setInputPaths(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        return job.waitForCompletion(true) ?  0 : 1;
    }

    public static void main(String[] args) {
        args = new String[2];
        args[0] = "/Users/mac/IdeaProjects/bigdata/datas/input/order";
        args[1] = "/Users/mac/IdeaProjects/bigdata/datas/output/28";
        try{
            int result = ToolRunner.run(new OrderMaxPrice(), args);
            System.exit(result);
        }catch (Exception ex) {

        }
    }

}
