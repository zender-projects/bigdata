package mrjob;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.Objects;

public class HBaseMapper extends TableMapper<Text, IntWritable> {

    IntWritable one = new IntWritable(1);
    Text text = new Text();

    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        String words = Bytes.toString(value.getValue(Bytes.toBytes("content"), Bytes.toBytes("info")));
        String[] wordsArray = words.split(" ");
        for(String word : wordsArray) {
            if(!Objects.isNull(word) && !"".equals(word)) {
                text.set(word);
                context.write(text, one);
            }
        }
    }
}
