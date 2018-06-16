package bigdata.clickflow.mrjobs;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
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

import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class WebLogPreValidProcessJob extends Configured implements Tool{

    enum ValidFiltCounter {
        VALID
    }

    /**
     * 从所有源数据中过滤出有效数据
     * */
    static class WebLogPreValidProcessMapper extends Mapper<
            LongWritable, Text, Text, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\001");
            if(fields.length > 0 && "true".equals(fields[0])){
                context.getCounter(ValidFiltCounter.VALID).increment(1);
                context.write(value, NullWritable.get());
            }
        }
    }


    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = getConf();
        Job job = Job.getInstance(configuration);

        job.setJarByClass(WebLogPreValidProcessJob.class);

        job.setMapperClass(WebLogPreValidProcessMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.addInputPath(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        job.setNumReduceTasks(0);

        return job.waitForCompletion(true) ?  0 : 1;
    }

    public static void main(String[] args) throws  Exception{
        int res = ToolRunner.run(new WebLogPreValidProcessJob(), args);
        System.exit(res);
    }
}
