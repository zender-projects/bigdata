package bigdata.clickflow.mrjobs;

import bigdata.clickflow.bean.WebLogBean;
import bigdata.clickflow.utils.WebLogParseUtil;
import lombok.extern.java.Log;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Log
public class WebLogPreProcessJob extends Configured implements Tool{

    enum PreProcessCounter{
        VALID_URL, INVALID_URL
    }

    public static class WebLogPreProcessMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

        Set<String> validUrlCache = new HashSet<String>();
        Text keyContainer = new Text();
        NullWritable valueContainer = NullWritable.get();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            validUrlCache.add("/about");
            validUrlCache.add("ack-ip-list/");
            validUrlCache.add("/cassandra-clustor/");
            validUrlCache.add("/finance-rhive-repurchase/");
            validUrlCache.add("/hadoop-family-roadmap/");
            validUrlCache.add("/hadoop-hive-intro/");
            validUrlCache.add("/hadoop-zookeeper-intro/");
            validUrlCache.add("/hadoop-mahout-roadmap/");
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            //解析Log
            WebLogBean bean = WebLogParseUtil.parse(line);
            //过滤静态资源请求
            WebLogParseUtil.filtStaticResource(bean, validUrlCache);
            //记录有效url和无效url的数量
            if(bean.isValid())
                context.getCounter(PreProcessCounter.VALID_URL).increment(1);
            else
                context.getCounter(PreProcessCounter.INVALID_URL).increment(1);

            //输出到hdfs
            keyContainer.set(bean.toString());
            context.write(keyContainer, valueContainer);
        }
    }


    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = getConf();
        Job job = Job.getInstance(configuration);

        job.setJarByClass(WebLogPreProcessJob.class);

        job.setMapperClass(WebLogPreProcessMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }


    /**
     * input path:/data/weblog/preprocess/input
     * outputpath:/data/weblog/preprocess/output
     * */
    public static void main(String[] args) {
        try{
            int res = ToolRunner.run(new WebLogPreProcessJob(), args);
        }catch (Exception ex) {
            log.info("run job 'WebLogPreProcessJob' exception, " + ex.toString());
            ex.printStackTrace();
        }
    }
}
