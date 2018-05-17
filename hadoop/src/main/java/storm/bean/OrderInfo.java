package storm.bean;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderInfo implements Serializable{

    private String orderId;       //订单id
    private Date createOrderTime; //订单创建时间
    private String paymentId;     //支付编号
    private String paymentTime;   //支付时间
    private String productId;     //商品编号
    private String productName;   //商品名称
    private long productPrice;    //商品价格
    private long promotionPrice;  //促销价格
    private String shopId;        //店铺id
    private String shopName;      //店铺名称
    private String shopMobile;    //店铺电话
    private long payPrice;        //订单支付价格
    private Integer num;          //订单数量

    public String random() {
        this.productId = "12121212";
        this.orderId = UUID.randomUUID().toString().replaceAll("-", "");
        this.paymentId = UUID.randomUUID().toString().replaceAll("-", "");
        this.productPrice = new Random().nextInt(1000);
        this.promotionPrice = new Random().nextInt(500);
        this.payPrice = new Random().nextInt(480);

        String date = "2015-11-11 12:22:12";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.createOrderTime = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Gson().toJson(this);
    }
}
