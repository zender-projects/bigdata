package mrjob;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.Objects;

public class HBaseReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        if(!Objects.isNull(key) && !"".equals(key.toString())) {
            int sum = 0;
            for(IntWritable value : values) {
                sum += value.get();
            }

            Put put = new Put(key.getBytes());
            put.addColumn("content".getBytes(), "count".getBytes(), Bytes.toBytes(sum));
            context.write(new ImmutableBytesWritable(Bytes.toBytes(key.toString())), put);
        }
    }
}