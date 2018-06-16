package bigdata.clickflow.bean;

import lombok.Builder;
import lombok.Data;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * 原始日志数据映射bean.
 * @author mac
 * */

@Data
@Builder
public class WebLogBean implements Writable{

    private boolean valid;         //判断数据是否合法
    private String remote_addr;    //客户端ip
    private String remote_user;    //客户端用户
    private String time_local;     //访问时间
    private String request;        //请求路径
    private String status;         //请求响应状态
    private String body_bytes_sent;   //响应体大小
    private String http_referer;   //访问来源
    private String http_user_agent; //客户端浏览器信息


    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(valid);
        dataOutput.writeUTF(Objects.isNull(remote_addr) ? "" : remote_addr);
        dataOutput.writeUTF(Objects.isNull(remote_user) ? "" : remote_user);
        dataOutput.writeUTF(Objects.isNull(time_local) ? "" : time_local);
        dataOutput.writeUTF(Objects.isNull(request) ? "" : request);
        dataOutput.writeUTF(Objects.isNull(status) ? "" : status);
        dataOutput.writeUTF(Objects.isNull(body_bytes_sent) ? "" : body_bytes_sent);
        dataOutput.writeUTF(Objects.isNull(http_referer) ? "" : http_referer);
        dataOutput.writeUTF(Objects.isNull(http_user_agent) ? "" : http_user_agent);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.valid = dataInput.readBoolean();
        this.remote_addr = dataInput.readUTF();
        this.remote_user = dataInput.readUTF();
        this.time_local = dataInput.readUTF();
        this.request = dataInput.readUTF();
        this.status = dataInput.readUTF();
        this.body_bytes_sent = dataInput.readUTF();
        this.http_referer = dataInput.readUTF();
        this.http_user_agent = dataInput.readUTF();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(valid)
                .append("\001").append(remote_addr)
                .append("\001").append(remote_user)
                .append("\001").append(time_local)
                .append("\001").append(request)
                .append("\001").append(status)
                .append("\001").append(body_bytes_sent)
                .append("\001").append(http_referer)
                .append("\001").append(http_user_agent);
        return sb.toString();
    }
}
