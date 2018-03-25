package mapreduce.groupingcomparator;

import com.sun.tools.corba.se.idl.constExpr.Or;
import mapreduce.bean.OrderBean;
import org.apache.avro.Schema;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 利用Reduce端的Comparator来实现将一组bean堪称相同的key
 * @author mac
 * */
public class OrderGroupingComparator extends WritableComparator{

    protected OrderGroupingComparator(){
        super(OrderBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean bean1 = (OrderBean)a;
        OrderBean bean2 = (OrderBean)b;

        //根据order id断定两个bean是否相同
        return bean1.getOrderId().compareTo(bean2.getOrderId());
    }
}
