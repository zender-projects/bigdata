package mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @WordCount MapReduce
 * @author mac
 * */
public class WordCount {


    public static class WCMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            IntWritable intValue = new IntWritable(1);
            String[] words = value.toString().split(" ");

            for(String word : words) {
                context.write(new Text(word), intValue);
            }
        }
    }


    public static class WCReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

            Integer counter = 0;
            for(IntWritable intWritable : values) {
                counter ++;
            }
            context.write(key, new IntWritable(counter));
        }
    }

    public static void main(String[] args) throws Exception {

        args = new String[2];
        args[0] = "/Users/mac/IdeaProjects/bigdata/datas/input/wc";
        args[1] = "/Users/mac/IdeaProjects/bigdata/datas/output/6";

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        /*configuration.set("mapreduce.framework.name", "yarn");
        configuration.set("yarn.resourcemanager.hostname", "yarn-host");*/

        /*configuration.set("mapreduce.framework.name", "local");
        configuration.set("fs.defaultFS", "file:///");*/

        job.setJarByClass(WordCount.class);

        job.setMapperClass(WCMapper.class);
        job.setReducerClass(WCReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //合并小文件
        job.setInputFormatClass(CombineFileInputFormat.class);
        //CombineFileInputFormat.setMaxInputSplitSize(job, 4194304); //4mb
        CombineFileInputFormat.setMinInputSplitSize(job, 2097152); //2mb

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean reuslt = job.waitForCompletion(true);

        if(reuslt) {
            System.exit(0);
        }
    }
}
