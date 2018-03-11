package mapreduce.bean;

import lombok.*;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Product-Order Information bean.
 * @author mac
 * */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class InfoBean implements Writable{

    private Integer orderId;   //订单id
    private String orderDate;  //订单日期
    private String productId;       //商品Id
    private Integer productAmount;    //商品数量
    private String productName;        //商品名称
    private String productCategoryId;    //商品类别
    private Integer productPrice;     //商品价格

    private String flag;  // 0-order, 1-product


    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(orderId);
        dataOutput.writeUTF(orderDate);
        dataOutput.writeUTF(productId);
        dataOutput.writeInt(productAmount);
        dataOutput.writeUTF(productName);
        dataOutput.writeUTF(productCategoryId);
        dataOutput.writeInt(productPrice);
        dataOutput.writeUTF(flag);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.orderId = dataInput.readInt();
        this.orderDate = dataInput.readUTF();
        this.productId = dataInput.readUTF();

        this.productAmount = dataInput.readInt();
        this.productName = dataInput.readUTF();
        this.productCategoryId = dataInput.readUTF();
        this.productPrice = dataInput.readInt();
        this.flag = dataInput.readUTF();
    }


}
