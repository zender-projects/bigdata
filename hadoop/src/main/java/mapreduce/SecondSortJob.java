package mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * 二次排序
 *

 4	3
 4	2
 4	1
 3	4
 2	7
 2	3
 3	1
 3	2
 3	3

 * */
public class SecondSortJob extends Configured implements Tool {


    public static class KeyPartitioner extends HashPartitioner<Text, NullWritable>
    {
        @Override
        public int getPartition(Text key, NullWritable value, int numReduceTasks) {
            //根據第一個值排序
            return (key.toString().split("\t")[0]).hashCode() & Integer.MAX_VALUE
                    % numReduceTasks;
        }
    }

    public static class SecondSortComparator extends WritableComparator {

        protected SecondSortComparator(){
            super(Text.class, true);
        }

        @SuppressWarnings("rawtypes")
        @Override
        public int compare(WritableComparable a, WritableComparable b) {

            //如果第一个字段值相等，则比较第二个
            if(Integer.valueOf(a.toString().split("\t")[0]) ==
                    Integer.valueOf(b.toString().split("\t")[0])){
                if(Integer.valueOf(a.toString().split("\t")[1]) >
                        Integer.valueOf(b.toString().split("\t")[1])) {
                    return 1;
                }else if(Integer.valueOf(a.toString().split("\t")[1]) <
                        Integer.valueOf(b.toString().split("\t")[1])) {
                    return -1;
                }else{
                    return 0;
                }

            }else{
                //否则比较第一个字段值
                if(Integer.valueOf(a.toString().split("\t")[0]) >
                        Integer.valueOf(b.toString().split("\t")[0])) {
                    return 1;
                }else if(Integer.valueOf(a.toString().split("\t")[0]) <
                        Integer.valueOf(b.toString().split("\t")[0])) {
                    return -1;
                }
            }
            return 0;
        }
    }

    public static class SecondSortJobMapper extends Mapper<LongWritable,
            Text, Text, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value,
                           Mapper<LongWritable, Text, Text, NullWritable>.Context context)
                throws IOException, InterruptedException {

            context.write(value, NullWritable.get());
        }
    }

    public static class SecondSortJobReducer extends
            Reducer<Text, NullWritable, Text, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<NullWritable> values,
                              Reducer<Text, NullWritable, Text, NullWritable>.Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }


    @Override
    public int run(String[] args) throws Exception {

        Configuration conf = getConf();

        Job job = Job.getInstance(conf);
        job.setJarByClass(SecondSortJob.class);

        job.setMapperClass(SecondSortJobMapper.class);
        job.setReducerClass(SecondSortJobReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        //按照第一個字段進行分區
        job.setPartitionerClass(KeyPartitioner.class);
        job.setSortComparatorClass(SecondSortComparator.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        job.setNumReduceTasks(1);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int rs = ToolRunner.run(new SecondSortJob(), args);
        System.exit(rs);
    }

}
