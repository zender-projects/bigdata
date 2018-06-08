package mapreduce;

import mapreduce.bean.FlowBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.File;
import java.io.IOException;

/**
 * 流量求和
 *

 1363157985066 	13726230503	00-FD-07-A4-72-B8:CMCC	120.196.100.82	i02.c.aliimg.com		24	27	2481	24681	200
 1363157995052 	13826544101	5C-0E-8B-C7-F1-E0:CMCC	120.197.40.4			4	0	264	0	200
 1363157991076 	13926435656	20-10-7A-28-CC-0A:CMCC	120.196.100.99			2	4	132	1512	200
 1363154400022 	13926251106	5C-0E-8B-8B-B1-50:CMCC	120.197.40.4			4	0	240	0	200
 1363157993044 	18211575961	94-71-AC-CD-E6-18:CMCC-EASY	120.196.100.99	iface.qiyi.com	视频网站	15	12	1527	2106	200
 1363157995074 	84138413	5C-0E-8B-8C-E8-20:7DaysInn	120.197.40.4	122.72.52.12		20	16	4116	1432	200
 1363157993055 	13560439658	C4-17-FE-BA-DE-D9:CMCC	120.196.100.99			18	15	1116	954	200
 1363157995033 	15920133257	5C-0E-8B-C7-BA-20:CMCC	120.197.40.4	sug.so.360.cn	信息安全	20	20	3156	2936	200
 1363157983019 	13719199419	68-A1-B7-03-07-B1:CMCC-EASY	120.196.100.82			4	0	240	0	200
 1363157984041 	13660577991	5C-0E-8B-92-5C-20:CMCC-EASY	120.197.40.4	s19.cnzz.com	站点统计	24	9	6960	690	200
 1363157973098 	15013685858	5C-0E-8B-C7-F7-90:CMCC	120.197.40.4	rank.ie.sogou.com	搜索引擎	28	27	3659	3538	200
 1363157986029 	15989002119	E8-99-C4-4E-93-E0:CMCC-EASY	120.196.100.99	www.umeng.com	站点统计	3	3	1938	180	200
 1363157992093 	13560439658	C4-17-FE-BA-DE-D9:CMCC	120.196.100.99			15	9	918	4938	200
 1363157986041 	13480253104	5C-0E-8B-C7-FC-80:CMCC-EASY	120.197.40.4			3	3	180	180	200
 1363157984040 	13602846565	5C-0E-8B-8B-B6-00:CMCC	120.197.40.4	2052.flash2-http.qq.com	综合门户	15	12	1938	2910	200
 1363157995093 	13922314466	00-FD-07-A2-EC-BA:CMCC	120.196.100.82	img.qfc.cn		12	12	3008	3720	200
 1363157982040 	13502468823	5C-0A-5B-6A-0B-D4:CMCC-EASY	120.196.100.99	y0.ifengimg.com	综合门户	57	102	7335	110349	200
 1363157986072 	18320173382	84-25-DB-4F-10-1A:CMCC-EASY	120.196.100.99	input.shouji.sogou.com	搜索引擎	21	18	9531	2412	200
 1363157990043 	13925057413	00-1F-64-E1-E6-9A:CMCC	120.196.100.55	t3.baidu.com	搜索引擎	69	63	11058	48243	200
 1363157988072 	13760778710	00-FD-07-A4-7B-08:CMCC	120.196.100.82			2	2	120	120	200
 1363157985066 	13726238888	00-FD-07-A4-72-B8:CMCC	120.196.100.82	i02.c.aliimg.com		24	27	2481	24681	200
 1363157993055 	13560436666	C4-17-FE-BA-DE-D9:CMCC	120.196.100.99			18	15	1116	954	200


 1.统计每个用户的对应的总流量并按流量倒序排列

 1)汇总
 2)排序

 * */

public class FlowSumSort extends Configured implements org.apache.hadoop.util.Tool{




    public static class FlowSumMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            //过滤数据
            if(fields.length != 9 ){
                return;
            }

            String phone = fields[1];
            Long upFlow = Long.parseLong(fields[fields.length - 3]);
            Long downFlow = Long.parseLong(fields[fields.length - 2]);

            FlowBean bean = new FlowBean(upFlow, downFlow);
            //写入reduce
            context.write(new Text(phone), bean);
        }
    }




    public static class FlowSumReducer extends Reducer<Text, FlowBean, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
            long sumUpFlow = 0l;
            long sumDownFlow = 0l;
            for(FlowBean bean : values) {
                sumDownFlow += bean.getDownFlow();
                sumUpFlow += bean.getUpFlow();
            }

            //写出数据
            context.write(key, new Text(new FlowBean(sumUpFlow, sumDownFlow).toString()));
        }
    }


    //获取到上一个汇总统计程序到输出   13888888888  343434   232323  23243433434
    public static class FlowSumSortMapper extends Mapper<LongWritable, Text, FlowBean, Text> {

        FlowBean flowBean = new FlowBean();
        Text phoen = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            String phone = fields[0];
            long upFlow = Long.parseLong(fields[1]);
            long downFlow = Long.parseLong(fields[2]);
            //long totalFlow = Long.parseLong(fields[3]);

            flowBean.setUpFlow(upFlow);
            flowBean.setDownFlow(downFlow);
            phoen.set(phone);
            context.write(flowBean, phoen);
        }
    }

    public static class FlowSumSortReducer extends Reducer<FlowBean, Text, Text, FlowBean>{
        @Override
        protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //直接输出
            for(Text phone : values){
                context.write(phone, key);
            }
        }
    }


    @Override
    public int run(String[] strings) throws Exception {


        Configuration configuration = getConf();
        Job sumJob = Job.getInstance(configuration);

        sumJob.setJarByClass(FlowSumSort.class);

        sumJob.setMapOutputKeyClass(FlowSumMapper.class);
        sumJob.setReducerClass(FlowSumReducer.class);

        sumJob.setMapOutputKeyClass(Text.class);
        sumJob.setMapOutputKeyClass(FlowBean.class);

        sumJob.setOutputValueClass(Text.class);
        sumJob.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(sumJob, new Path(strings[0]));
        FileOutputFormat.setOutputPath(sumJob, new Path(strings[1]));

        boolean stepJobCompleted = sumJob.waitForCompletion(true);

        //进行第二步
        //if(stepJobCompleted) {
            Job sortJob = Job.getInstance(configuration);

            sortJob.setJarByClass(FlowSumSort.class);

            sortJob.setMapperClass(FlowSumSortMapper.class);
            sortJob.setReducerClass(FlowSumSortReducer.class);

            sortJob.setMapOutputKeyClass(FlowBean.class);
            sortJob.setMapOutputKeyClass(Text.class);

            sortJob.setOutputKeyClass(Text.class);
            sortJob.setOutputValueClass(FlowBean.class);

            //CombineFileInputFormat解决大小小文件产生大量maptask的问题
            //将多个小文件从逻辑上划分到一个分片
            /*sortJob.setInputFormatClass(CombineFileInputFormat.class);
            CombineFileInputFormat.setMaxInputSplitSize(sortJob, 4194304); //4mb
            CombineFileInputFormat.setMinInputSplitSize(sortJob, 2097152); //2mb*/


            FileInputFormat.addInputPath(sortJob, new Path(strings[1]));
            FileOutputFormat.setOutputPath(sortJob, new Path(strings[2]));
        TextInputFormat
            return  sortJob.waitForCompletion(true) ? 0 : 1;
    //    }

        //return 0;
       // return 0;
    }


    public static void main(String[] args) throws Exception {
        args = new String[2];
        args[0] = "/Users/mac/IdeaProjects/bigdata/datas/input/flow";
        args[1] = "/Users/mac/IdeaProjects/bigdata/datas/output/11";
        args[2] = "/Users/mac/IdeaProjects/bigdata/datas/output/12";
        int result = ToolRunner.run(new FlowSumSort(), args);
        System.exit(result);
    }
}
