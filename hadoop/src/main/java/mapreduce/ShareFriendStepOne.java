package mapreduce;

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
 * 过滤两个人的共同好友
 *
 A:B,C,D,F,E,O
 B:A,C,E,K
 C:F,A,D,I
 D:A,E,F,L
 E:B,C,D,M,L
 F:A,B,C,D,E,O,M
 G:A,C,D,E,F
 H:A,C,D,E,O
 I:A,O
 J:B,O
 K:A,C,D
 L:D,E,F
 M:E,F,G
 O:A,H,I,J

 ->

 B - A  C- A D - A F -A

 ->

 B : A E F J



 * */
public class ShareFriendStepOne extends Configured implements Tool{


    //输入  H:A,C,D,E,O   输出  A - H  C - H  D-H E-H O-H
    public static class ShareFriendStepOneMapper extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split(":");
            String person = fields[0];
            String[] friedns = fields[1].split(",");

            for(String friend : friedns) {
                context.write(new Text(friend), new Text(person));
            }
        }
    }

    //输入 A - H  C - H  D-H E-H O-H  输出：每个人都是哪些人的好友
    public static class ShareFriendStepOneReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuilder stringBuilder = new StringBuilder();
            for(Text text : values) {
                stringBuilder.append(text.toString() + ",");
            }
            System.out.println(key.toString() + ":" + stringBuilder.toString());
            context.write(key, new Text(stringBuilder.toString()));
        }
    }

    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = getConf();
        configuration.set("mapreduce.framework.name","local");
        configuration.set("fs.defaultFS", "file:///");

        Job job = Job.getInstance(configuration);

        job.setJarByClass(ShareFriendStepOne.class);

        job.setMapperClass(ShareFriendStepOneMapper.class);
        job.setReducerClass(ShareFriendStepOneReducer.class);

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
        args[0] = "/Users/mac/IdeaProjects/bigdata/datas/input/qq";
        args[1] = "/Users/mac/IdeaProjects/bigdata/datas/output/25";

        int result = ToolRunner.run(new ShareFriendStepOne(), args);
        System.exit(result);
    }
}
