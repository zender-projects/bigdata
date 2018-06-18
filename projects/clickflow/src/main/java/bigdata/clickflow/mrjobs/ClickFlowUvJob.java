package bigdata.clickflow.mrjobs;


import bigdata.clickflow.bean.PVBean;
import bigdata.clickflow.bean.UVBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import sun.security.krb5.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 从pv数据中进一步梳理出uv
 * @author mac
 * */
public class ClickFlowUvJob extends Configured implements Tool
{



    static class UVMapper extends Mapper<LongWritable, Text, Text, PVBean> {

        //PVBean valueContainer = PVBean.builder().build();
        Text keyContainer = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\001");
            int visit_step = Integer.parseInt(fields[5]);

            PVBean pvBean = PVBean.builder().session(fields[0])
                                .remote_addr(fields[1])
                    .remote_user(fields[2])
                    .time_local(fields[3])
                    .request(fields[4])
                    .visit_step(visit_step)
                    .page_staylong(fields[6])
                    .http_referer(fields[7])
                    .http_user_agent(fields[8])
                    .body_bytes_sent(fields[9])
                    .status(fields[10]).build();

            keyContainer.set(pvBean.getSession());

            //以sessionid为key，处理每个会话内的数据
            context.write(keyContainer, pvBean);
        }
    }

    static class UVReducer extends Reducer<Text, PVBean, NullWritable, Text> {

        @Override
        protected void reduce(Text key, Iterable<PVBean> values, Context context) throws IOException, InterruptedException {
            List<PVBean> pvBeanList = new ArrayList<PVBean>();
            for(PVBean bean : values) {
                PVBean pvBean = PVBean.builder().build();
                try{
                    BeanUtils.copyProperties(pvBean, bean);
                    pvBeanList.add(pvBean);
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            //按step排序
            Collections.sort(pvBeanList, new Comparator<PVBean>() {
                @Override
                public int compare(PVBean o1, PVBean o2) {
                    return o1.getVisit_step() > o2.getVisit_step() ? 1 : -1;
                }
            });

            //获取visit的首位pv数据

            /*private String session;
            private String remote_addr;
            private String in_time;
            private String out_time;
            private String in_page;
            private String out_page;
            private String referal;
            private int page_visits;*/

            UVBean uvBean = UVBean.builder()
                    .session(key.toString())
                    .remote_addr(pvBeanList.get(0).getRemote_addr())
                    .in_time(pvBeanList.get(0).getTime_local())
                    .out_page(pvBeanList.get(pvBeanList.size() - 1).getTime_local())
                    .in_page(pvBeanList.get(0).getRequest())
                    .out_page(pvBeanList.get(pvBeanList.size() - 1).getRequest())
                    .referer(pvBeanList.get(0).getHttp_referer())
                    .page_visits(pvBeanList.size())
                    .build();

            context.write(NullWritable.get(), new Text(uvBean.toString())
            );
        }
    }

    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = getConf();
        Job job = Job.getInstance(configuration);

        job.setJarByClass(ClickFlowUvJob.class);

        job.setMapperClass(UVMapper.class);
        job.setReducerClass(UVReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(PVBean.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new ClickFlowUvJob(), args) ;
        System.exit(res);
    }
}
