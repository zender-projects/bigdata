package bigdata.clickflow.mrjobs;

import bigdata.clickflow.bean.WebLogBean;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 统计pv数据
 * @author mac
 * */
public class ClickFlowPvJob extends Configured implements Tool
{
    enum PVCounter  {
        MAP_COUNTER,
        MAP_INVALID_COUNTER,
        REDUCE_COUNTER
    }

    static class PVMapper extends Mapper<LongWritable, Text, Text, WebLogBean> {

        Text keyContainer = new Text();
        //WebLogBean valueContainer = new WebLogBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();

            String[] fields = line.split("\001");
            if(fields.length < 9) {
                context.getCounter(PVCounter.MAP_INVALID_COUNTER).increment(1);
                return;
            }

            WebLogBean bean = new WebLogBean(Boolean.valueOf(fields[0]),
                                             fields[1],
                                             fields[2],
                    fields[3],
                    fields[4],
                    fields[5],
                    fields[6],
                    fields[7],
                    fields[8]);

            //把remote_addr作为key
            keyContainer.set(bean.getRemote_addr());
            context.getCounter(PVCounter.MAP_COUNTER).increment(1);
            context.write(keyContainer, bean);
        }
    }


    static class PVReducer extends Reducer<Text, WebLogBean, NullWritable, Text> {

        Text value = new Text();
        @Override
        protected void reduce(Text key, Iterable<WebLogBean> values, Context context) throws IOException, InterruptedException {

            context.getCounter(PVCounter.REDUCE_COUNTER).increment(1);
            List<WebLogBean> beans = new ArrayList<WebLogBean>();
            try{
                //收集同一个ip下的所有访问记录
                for(WebLogBean bean : values) {
                    WebLogBean webLogBean = new WebLogBean();
                    BeanUtils.copyProperties(webLogBean, bean);
                    beans.add(webLogBean);
                }

                //按时间排序
                Collections.sort(beans, new Comparator<WebLogBean>() {
                    @Override
                    public int compare(WebLogBean o1, WebLogBean o2) {
                        try {
                            Date d1 = toDate(o1.getTime_local());
                            Date d2 = toDate(o2.getTime_local());
                            if(Objects.isNull(d1) || Objects.isNull(d2)) {
                                return 0;
                            }
                            return d1.compareTo(d2);
                        }catch (Exception ex) {
                            ex.printStackTrace();
                            return 0;
                        }
                    }
                });

                /**
                 * 从有序的bean中分辨出各次访问，
                 * 并对每一次访问中的所有page按顺序标号
                 * */

                int step = 1;
                String session = UUID.randomUUID().toString();
                for(int i = 0;i < beans.size();i ++) {
                    WebLogBean bean = beans.get(i);
                    //如果只有一条数据，则直接输出
                    if(beans.size() == 1) {
                        //设置默认停留时间为60s
                        value.set(
                                session + "\001" +
                                key.toString() + "\001" +
                                bean.getRemote_user() + "\001" +
                                bean.getTime_local() + "\001" +
                                bean.getRequest() + "\001" +
                                step + "\001" +
                                        (60)  + "\001" +
                                bean.getHttp_referer() + "\001" +
                                bean.getHttp_user_agent() + "\001" +
                                bean.getBody_bytes_sent() + "\001"  +
                                bean.getStatus()
                        );

                        context.write(NullWritable.get(), value);
                        session = UUID.randomUUID().toString();
                        break;
                    }

                    //如果不止一条，则跳过第一条不输出，遍历第二条时再输出
                    if(i == 0) {
                        continue;
                    }

                    //求近两次的时间差
                    long timeDiff = timeDiff(toDate(bean.getTime_local()), toDate(beans.get(i - 1).getTime_local()));

                    //如果本地-上次时间差<30分钟，则输出前一次的页面访问信息
                    if(timeDiff < 30 * 60 * 1000) {
                        value.set(
                                session + "\001" +
                                        key.toString() + "\001" +
                                        beans.get(i - 1).getRemote_user() + "\001" +
                                        beans.get(i - 1).getTime_local() + "\001" +
                                        beans.get(i - 1).getRequest() + "\001" +
                                        step + "\001" +
                                        (timeDiff / 1000)  + "\001" +
                                        beans.get(i - 1).getHttp_referer() + "\001" +
                                        beans.get(i - 1).getHttp_user_agent() + "\001" +
                                        beans.get(i - 1).getBody_bytes_sent() + "\001"  +
                                        beans.get(i - 1).getStatus()
                        );
                        context.write(NullWritable.get(), value);
                        step ++;
                    }else {
                        //如果本次-上次时间差>30分钟，则输出前一次访问信息，并且将step重置，
                        //以分隔为新的visit
                        value.set(
                                session + "\001" +
                                        key.toString() + "\001" +
                                        beans.get(i - 1).getRemote_user() + "\001" +
                                        beans.get(i - 1).getTime_local() + "\001" +
                                        beans.get(i - 1).getRequest() + "\001" +
                                        step + "\001" +
                                        (60) + "\001" +
                                        beans.get(i - 1).getHttp_referer() + "\001" +
                                        beans.get(i - 1).getHttp_user_agent() + "\001" +
                                        beans.get(i - 1).getBody_bytes_sent() + "\001"  +
                                        beans.get(i - 1).getStatus()
                        );
                        context.write(NullWritable.get(), value);
                        step = 1;
                        session = UUID.randomUUID().toString();
                        context.write(NullWritable.get(), value);
                    }

                    //如果是最后一条，则将本条数据输出
                    if(i == beans.size() - 1) {
                        value.set(
                                session + "\001" +
                                key.toString() + "\001" +
                                bean.getRemote_user() + "\001" +
                                bean.getTime_local() + "\001" +
                                bean.getRequest() + "\001" +
                                step + "\001" +
                                        (60) + "\001" +
                                bean.getHttp_referer() + "\001" +
                                bean.getHttp_user_agent() + "\001" +
                                        bean.getBody_bytes_sent() + "\001" +
                                bean.getStatus()
                        );
                        context.write(NullWritable.get(), value);
                    }

                }

            }catch (Exception ex) {

            }
        }

        public Date toDate(String str) throws ParseException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            return sdf.parse(str);
        }

        public long timeDiff(Date time1, Date time2) {
            return time1.getTime() - time2.getTime();
        }
    }

    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = getConf();
        Job job = Job.getInstance(configuration);

        job.setJarByClass(ClickFlowPvJob.class);

        job.setMapperClass(PVMapper.class);
        job.setReducerClass(PVReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(WebLogBean.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    /**
     * input path:/data/weblog/preprocess/valid_output
     * outputpath:/data/weblog/preprocess/click_pv_out
     * */
    public static void main(String[] args) throws Exception {
        int rs = ToolRunner.run(new ClickFlowPvJob(), args);
        System.exit(rs);
    }
}
