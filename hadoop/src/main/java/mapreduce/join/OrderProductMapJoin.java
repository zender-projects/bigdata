package mapreduce.join;

import mapreduce.bean.InfoBean;
import mapreduce.bean.ProductBean;
import org.apache.commons.lang.StringUtils;
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

import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Mapper端Join:将小文件缓存到每一个maptask节点的工作目录
 * @author mac
 * */
public class OrderProductMapJoin extends Configured implements Tool{


    public static class MJMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

        Map<String, ProductBean> productInfoMap = new HashMap<String, ProductBean>();
        Text k = new Text();


        //加载cache 数据
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //读取工作目录/classpath下到product资源文件
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("product.txt")));
            String line;
            while(!StringUtils.isEmpty(line = br.readLine())){
                String[] fields = line.split("\t");
                String pId = fields[0];
                ProductBean productBean = ProductBean.builder()
                                            .pId(pId)
                                            .pName(fields[1])
                                            .pCategoryId(fields[2])
                                            .pPrice(fields[3]).build();

                productInfoMap.put(pId, productBean);
            }
            br.close();
        }


        //mapper 用来读取order数据，Map中到product进行Join
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            /*1001	20150701	P0001	2
            1002	20150710	P0001	3
            1003	20150710	P0002	4*/
            String pId = fields[2];
            ProductBean productBean = productInfoMap.get(pId);
            if(!Objects.isNull(productBean)) {
                InfoBean infoBean = InfoBean.builder()
                        .orderId(Integer.parseInt(fields[0]))
                        .orderDate(fields[1])
                        .productId(pId)
                        .productAmount(Integer.parseInt(fields[3]))
                        .productName(productBean.getPName())
                        .productCategoryId(productBean.getPCategoryId())
                        .productPrice(Integer.parseInt(productBean.getPPrice()))
                        .build();
                k.set(infoBean.toString());
                context.write(k, NullWritable.get());
            }
        }
    }

    @Override
    public int run(String[] strings) throws Exception {

        Configuration configuration = getConf();
        configuration.set("mapreduce.framework.name", "local");
        configuration.set("fs.defaultFS","file:///");

        Job job = Job.getInstance(configuration);

        job.setJarByClass(OrderProductMapJoin.class);

        job.setMapperClass(MJMapper.class);

        //没有reduce端，mapper直接对应output
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setNumReduceTasks(0);
        /*
        job.addArchiveToClassPath(Path(archive));  //缓存压缩文件到task运行节点的classpath
        job.addFileToClassPath();   //缓存普通文件到task运行节点的classpath
        job.addCacheArchive();  //缓存压缩文件到task运行节点到工作目录
        job.addCacheFile();     //缓存普通文件
        */
        job.addCacheFile(new URI("/Users/mac/IdeaProjects/bigdata/datas/input/cache/mjcache/product.txt"));


        FileInputFormat.addInputPath(job,new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));


        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception{
        args = new String[2];
        args[0] = "/Users/mac/IdeaProjects/bigdata/datas/input/join/reducejoin/order.txt";
        args[1] = "/Users/mac/IdeaProjects/bigdata/datas/output/20";

        int result = ToolRunner.run(new OrderProductMapJoin(), args);
        System.exit(result);
    }
}
