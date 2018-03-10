package mapreduce.bean;


import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 流量Bean
 * */
public class FlowBean implements Writable{

    private long upFlow;
    private long downFlow;

    private long sumFlow;

    public FlowBean(){}

    public FlowBean(long upFlow, long downFlow) {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = upFlow + downFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public long getUpFlow() {
        return upFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(upFlow);
        dataOutput.writeLong(downFlow);
        dataOutput.writeLong(sumFlow);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
       this.upFlow =  dataInput.readLong();
       this.downFlow =  dataInput.readLong();
       this.sumFlow = dataInput.readLong();
    }

    @Override
    public String toString() {
        return upFlow + "\t" + downFlow + "\t" + sumFlow;
    }
}
