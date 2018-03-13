package mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * 倒排索引
 *
 * a.txt
 * a b
 * b c
 *
 * b.txt
 * c d
 * e f
 *
 * c.txt
 * i k
 * e f
 * */
public class InvertIndexStepOne extends Configured implements Tool{




    /**
     * a-a.txt b-a.txt
     * b-a.txt c-a.txt
     * */
    public static class InvertIndexStepOneMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();

            String[] fields = line.split("\t");
            FileSplit fileSplit = (FileSplit)context.getInputSplit();

            String fileName = fileSplit.getPath().getName();

            for(String field : fields) {
                Text oKey = new Text(field + "-" + fileName);
                context.write(oKey, new IntWritable(1));
            }
        }
    }


    /**
     * a-a.txt 1
     * b-a.txt 2
     * c-a.txt 3
     * */
    public static class InvertIndexStepOneReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            Integer count = 0;
            for (IntWritable value : values) {
                count += value.get();
            }
            context.write(key, new IntWritable(count));
        }
    }




    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = getConf();
        configuration.set("mapreduce.framework.name", "local");
        configuration.set("fs.defaultFS", "local");

        Job job = Job.getInstance(configuration);

        job.setJarByClass(InvertIndexStepOne.class);

        job.setMapperClass(InvertIndexStepOneMapper.class);
        job.setReducerClass(InvertIndexStepOneReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }


    public static void main(String[] args) throws Exception{

        args = new String[2];
        args[0] = "/Users/mac/IdeaProjects/bigdata/datas/input/ii";
        args[1] = "/Users/mac/IdeaProjects/bigdata/datas/output/21";

        Integer result = ToolRunner.run(new InvertIndexStepOne(), args);
        System.exit(result);
    }

}
