package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Objects;

public class HBaseMR extends Configured implements Tool {


    private static class HBaseMapper extends TableMapper<Text, IntWritable> {

        IntWritable one = new IntWritable();
        Text text = new Text();
        @Override
        protected void map(ImmutableBytesWritable key, Result value,
                           Mapper<ImmutableBytesWritable, Result, Text, IntWritable>.Context context)
                throws IOException, InterruptedException {

            String words = Bytes.toString(value.getValue("line".getBytes(), "words".getBytes()));
            String[] wordsArray = words.split(",");

            for(String word : wordsArray) {
                if(!Objects.isNull(word) && !"".equals(word)) {
                    text.set(word);
                    context.write(text, one);
                }
            }
        }
    }

    private static class HBaseReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values,
                              Reducer<Text, IntWritable, ImmutableBytesWritable, Mutation>.Context context)
                throws IOException, InterruptedException {
            if(!java.util.Objects.isNull(key) && !"".equals(key.toString())) {
                int sum = 0;
                for(IntWritable value : values) {
                    sum += value.get();
                }
                Put row = new Put(key.getBytes());
                row.addColumn("info".getBytes(), "count".getBytes(), Bytes.toBytes(sum));

                context.write(null, row);
            }
        }
    }

    @Override
    public int run(String[] args) throws Exception {

        Configuration conf = getConf();
        conf.set("hbase.zookeeper.quorum", "node01,node02,node03");

        Job job = Job.getInstance(conf);

        job.setJarByClass(HBaseMapper.class);
        job.setNumReduceTasks(1);

        //Query data condition
        Scan scan = new Scan();
        //Set Mapper input form hbase
        TableMapReduceUtil.initTableMapperJob("words", scan, HBaseMapper.class, Text.class, IntWritable.class, job);
        //set reduce output to hbase
        TableMapReduceUtil.initTableReducerJob("wordcount", HBaseReducer.class, job);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int reuslt = ToolRunner.run(new HBaseMR(), args);
        System.exit(reuslt);
    }
}
