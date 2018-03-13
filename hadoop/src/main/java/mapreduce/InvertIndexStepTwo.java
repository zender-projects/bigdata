package mapreduce;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
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
 * 倒排索引 第二步
 *
 *
 hello-a.txt	3
 hello-b.txt	2
 hello-c.txt	2
 jerry-a.txt	1
 jerry-b.txt	3

 -》

 hello a.txt 3 |
 * */
public class InvertIndexStepTwo extends Configured implements Tool{



    public static class InvertIndexStepTwoMapper extends Mapper<LongWritable, Text, Text, Text>{

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");

            String fieldOne = fields[0];
            String fieldTwo = fields[1];

            String[] keyFields = fieldOne.split("-");

            String finalValue = keyFields[1] + fieldTwo + "|";

            context.write(new Text(keyFields[0]), new Text(finalValue));
        }
    }

    public static class InvertIndexStepTwoReducer extends Reducer<Text, Text, Text, Text> {

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuffer sb = new StringBuffer();
            for(Text value : values) {
                sb.append(value.toString());
            }
            context.write(key, new Text(sb.toString()));
        }
    }


    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = getConf();
        configuration.set("mapreduce.framework.name", "local");
        configuration.set("fs.defaultFS", "file:///");

        Job job = Job.getInstance(configuration);

        job.setJarByClass(InvertIndexStepTwo.class);

        job.setMapperClass(InvertIndexStepTwoMapper.class);
        job.setReducerClass(InvertIndexStepTwoReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {

        args = new String[2];
        args[0] = "/Users/mac/IdeaProjects/bigdata/datas/output/21";
        args[1] = "/Users/mac/IdeaProjects/bigdata/datas/output/22";

        Integer result = ToolRunner.run(new InvertIndexStepTwo(), args);
        System.exit(result);
    }

}
