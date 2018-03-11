package mapreduce.join;

import mapreduce.bean.InfoBean;
import org.apache.avro.Schema;
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
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 order:
 1001	20150701	P0001	2
 1002	20150710	P0001	3
 1003	20150710	P0002	4

 product:
 P0001	小米5	C01	2
 P0002	锤子T1	C01	3
 * */
public class OrderProductReduceJoin extends Configured implements Tool{




    public static class OPJoinMapper extends Mapper<LongWritable, Text, Text, InfoBean> {
        InfoBean infoBean = null;
        //Text key = new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String line = value.toString();
            //从分片中获取输入文件名称，区分order和product
            FileSplit fileSplit = (FileSplit)context.getInputSplit();
            String fileName = fileSplit.getPath().getName();
            //0-order 1-product
            String pId = "";
            if(fileName.startsWith("order")){
                String[] fileds = line.split("\t");
                pId = fileds[2];
                infoBean = InfoBean.builder().orderId(Integer.parseInt(fileds[0]))
                                    .orderDate(fileds[1])
                                    .productId(fileds[2])
                                    .productAmount(Integer.parseInt(fileds[3]))
                                    .productName("")
                                    .productCategoryId("")
                                    .productPrice(0)
                                    .flag("0").build();

            }else if(fileName.startsWith("product")){
                String[] fields = line.split("\t");
                pId = fields[0];
                infoBean = InfoBean.builder().orderId(0)
                        .orderDate("")
                        .productId(pId)
                        .productAmount(0)
                        .productName(fields[1])
                        .productCategoryId(fields[2])
                        .productPrice(Integer.parseInt(fields[3]))
                        .flag("1").build();
            }

            context.write(new Text(pId), infoBean);
        }
    }

    public static class OPJoinReducer extends Reducer<Text, InfoBean, InfoBean, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<InfoBean> values, Context context) throws IOException, InterruptedException {
           try {
               //一个产品对应多个订单
               InfoBean product = new InfoBean();
               List<InfoBean> orders = new ArrayList<InfoBean>();

               //分离订单数据和商品数据
               for (InfoBean infoBean : values) {

                   System.out.println("current bean:[" + infoBean +"]");

                   //商品信息
                   if ("1".equals(infoBean.getFlag())) {
                       BeanUtils.copyProperties(product, infoBean);
                   }else{
                       //订单信息
                       InfoBean tempOrder = InfoBean.builder().build();
                       BeanUtils.copyProperties(tempOrder, infoBean);
                       orders.add(tempOrder);
                   }
               }

               System.out.println("product information:[" + product +"]");
               System.out.println("order information:["+ orders +"]");

               //组装数据
               for(InfoBean order : orders) {
                   order.setProductName(product.getProductName());
                   order.setProductCategoryId(product.getProductCategoryId());
                   order.setProductPrice(product.getProductPrice());

                   context.write(order, NullWritable.get());
               }


           }catch (Exception ex) {
                ex.printStackTrace();
           }
        }
    }

    @Override
    public int run(String[] strings) throws Exception {

        //本地模式运行
        Configuration configuration =getConf();
        configuration.set("mapreduce.framework.name","local");
        configuration.set("fs.defaultFS","file:///");

        Job job = Job.getInstance(configuration);

        job.setJarByClass(OrderProductReduceJoin.class);

        job.setMapperClass(OPJoinMapper.class);
        job.setReducerClass(OPJoinReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(InfoBean.class);

        job.setOutputKeyClass(InfoBean.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception{

        args = new String[2];
        args[0] = "/Users/mac/IdeaProjects/bigdata/datas/input/join/reducejoin";
        args[1] = "/Users/mac/IdeaProjects/bigdata/datas/output/15";

        int result = ToolRunner.run(new OrderProductReduceJoin(), args);
        System.exit(result);
    }
}
