package mapreduce.bean;

import lombok.*;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
@author mac
 P0001	小米5	C01	2
 P0002	锤子T1	C01	3
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductBean implements Writable{

    private String pId;
    private String pName;
    private String pCategoryId;
    private String pPrice;




    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.pId);
        dataOutput.writeUTF(this.pName);
        dataOutput.writeUTF(this.pCategoryId);
        dataOutput.writeUTF(this.pPrice);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.pId = dataInput.readUTF();
        this.pName = dataInput.readUTF();
        this.pCategoryId = dataInput.readUTF();
        this.pPrice = dataInput.readUTF();
    }
}
