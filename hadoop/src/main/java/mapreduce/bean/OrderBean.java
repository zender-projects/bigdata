package mapreduce.bean;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class OrderBean implements Writable,WritableComparable<OrderBean> {

    private String orderId;   //订单ID
    private Double amount;    //Item价格

    public OrderBean(){

    }

    public OrderBean(String orderId, Double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public int compareTo(OrderBean o) {
        //先比较订单ID，若相同，再比较价格
        int cmp = this.orderId.compareTo(o.getOrderId());
        if(cmp == 0) {
            cmp = -this.amount.compareTo(o.getAmount());
        }
        return cmp;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.orderId);
        dataOutput.writeDouble(this.amount);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.orderId = dataInput.readUTF();
        this.amount = dataInput.readDouble();
    }
}
