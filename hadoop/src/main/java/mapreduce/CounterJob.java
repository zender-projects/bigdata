package mapreduce;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class CounterJob extends Configured implements Tool{

    public static enum MyCounter {
        SUCCESS,
        FAILED
    }

    public static class CounterJobMapper extends
            Mapper<LongWritable, Text, NullWritable, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value,
                           Mapper<LongWritable, Text, NullWritable, NullWritable>.Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            if("success".equals(line))
                context.getCounter(MyCounter.SUCCESS).increment(1);
            else if("failed".equals(line))
                context.getCounter(MyCounter.FAILED).increment(1);
        }
    }

    public static class CounterJobReducer extends
            Reducer<NullWritable, NullWritable, Text, LongWritable> {
        @Override
        protected void reduce(NullWritable key, Iterable<NullWritable> values,
                              Reducer<NullWritable, NullWritable, Text, LongWritable>.Context context)
                throws IOException, InterruptedException {
            Counter successCounter = context.getCounter(MyCounter.SUCCESS);
            Counter failedCounter = context.getCounter(MyCounter.FAILED);

            context.write(new Text(MyCounter.SUCCESS.name()),
                    new LongWritable(successCounter.getValue()));
            context.write(new Text(MyCounter.FAILED.name()),
                    new LongWritable(failedCounter.getValue()));

        }
    }

    @Override
    public int run(String[] args) throws Exception {

        Configuration conf = getConf();

        Job job = Job.getInstance(conf);
        job.setJarByClass(CounterJob.class);

        job.setMapperClass(CounterJobMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(NullWritable.class);

        job.setReducerClass(CounterJobReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int rs = ToolRunner.run(new CounterJob(), args);
        System.exit(rs);
    }




}