package mapreduce.partitioner;

import mapreduce.bean.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;
import java.util.Objects;

/**
 * 按不同省份进行分区
 * @author mac
 * */
public class FlowSumProvincePartitioner extends Partitioner<Text, FlowBean> {


    public static HashMap<String, Integer> provinceDict = new HashMap<>();
    static {
        //加载字段表数据
        provinceDict.put("136", 0); //北京
        provinceDict.put("137", 1); //上海
        provinceDict.put("138", 2);  //广州
        provinceDict.put("139", 3); //深
    }

    @Override
    public int getPartition(Text text, FlowBean flowBean, int i) {

        //根据手机号获取省份
        String prefix = text.toString().substring(0,3);
        Integer provinceId = provinceDict.get(prefix);
        if(!Objects.isNull(provinceId)){
            return provinceId;
        }
        return 4;
    }
}
