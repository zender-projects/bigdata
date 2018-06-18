package bigdata.clickflow.mrjobs;

import bigdata.clickflow.bean.WebLogDetailBean;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * 梳理宽表数据.
 * @author mac
 * */
public class ClickFlowDetailJob extends Configured implements Tool
{

    enum ClickFlowDetailJobCounter {
        SUCCESS, FAILED
    }

    static class DetailMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            try {
                String[] fieleds = value.toString().split("\001");

                String timeStr = fieleds[3];
                String[] timeFields = timeStr.split(" ");

                //String time1 = "2013-09-18 10:21:29";
                SimpleDateFormat targetFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                Date date = targetFormater1.parse(timeStr);
                Calendar calendar = Calendar.getInstance(Locale.US);
                calendar.setTime(date);

                String httpReferer = fieleds[7];
                String httpRefererHost = "";
                if(!Objects.isNull(httpReferer)) {
                    String[] httpFields = httpReferer.split("//");
                    if (!Objects.isNull(httpFields) && httpFields.length >= 2) {
                        httpRefererHost = httpFields[1].substring(0,httpFields[1].indexOf("/"));
                    }
                }

                WebLogDetailBean bean = WebLogDetailBean.builder()
                        .valid(Boolean.valueOf(fieleds[0]))
                        .remote_addr(fieleds[1])
                        .remote_user(fieleds[2])
                        .time_local(fieleds[3])
                        .daystr(timeFields[0])
                        .timestr(timeFields[1])
                        .month(calendar.get(Calendar.MONTH) + "")
                        .day(calendar.get(Calendar.DAY_OF_MONTH) + "")
                        .hour(calendar.get(Calendar.HOUR_OF_DAY) + "")
                        .request(fieleds[4])
                        .status(fieleds[5])
                        .body_bytes_sent(fieleds[6])
                        .http_referer(fieleds[7])
                        .ref_host(httpRefererHost)
                        .ref_path("")
                        .ref_quer("")
                        .ref_query_id("")
                        .http_user_agent(fieleds[8])
                        .build();

                context.getCounter(ClickFlowDetailJobCounter.SUCCESS).increment(1);
                context.write(NullWritable.get(), new Text(bean.toString()));
            }catch (Exception ex) {
                context.getCounter(ClickFlowDetailJobCounter.FAILED).increment(1);
            }
        }
    }



    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = getConf();
        Job job = Job.getInstance(configuration);

        job.setJarByClass(ClickFlowDetailJob.class);

        job.setMapperClass(DetailMapper.class);
        //job.setReducerClass(DetaiR);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        job.setNumReduceTasks(0);

        FileInputFormat.setInputPaths(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));


        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception{
        int res = ToolRunner.run(new ClickFlowDetailJob(), args);
        System.exit(res);
    }
}
