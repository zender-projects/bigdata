package mrjob;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HBaseMapReduceJob extends Configured implements Tool{


    @Override
    public int run(String[] strings) throws Exception {

        Configuration conf = getConf();
        conf.set("hbase.zookeeper.quorum", "hadoop01,hadoop02,hadoop03");
        Job job = Job.getInstance(conf);

        job.setJarByClass(HBaseMapReduceJob.class);
        job.setNumReduceTasks(1);

        //查询条件
        Scan scan = new Scan();
        TableMapReduceUtil.initTableMapperJob("word", scan, HBaseMapper.class, Text.class , IntWritable.class, job);
        TableMapReduceUtil.initTableReducerJob("stat", HBaseReducer.class, job);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int result = ToolRunner.run(new HBaseMapReduceJob(), args);
        System.exit(result);
    }
}
