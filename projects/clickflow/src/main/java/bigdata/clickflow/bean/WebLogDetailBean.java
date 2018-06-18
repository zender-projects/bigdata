package bigdata.clickflow.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 宽表Bean.
 * @author mac
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebLogDetailBean implements Writable{

    private Boolean valid;
    private String remote_addr;
    private String remote_user;
    private String time_local;
    private String daystr;
    private String timestr;
    private String month;
    private String day;
    private String hour;
    private String request;
    private String status;
    private String body_bytes_sent;
    private String http_referer;
    private String ref_host;
    private String ref_path;
    private String ref_quer;
    private String ref_query_id;
    private String http_user_agent;

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBoolean(valid);
        dataOutput.writeUTF(remote_addr);
        dataOutput.writeUTF(remote_user);
        dataOutput.writeUTF(time_local);
        dataOutput.writeUTF(daystr);
        dataOutput.writeUTF(timestr);
        dataOutput.writeUTF(month);
        dataOutput.writeUTF(day);
        dataOutput.writeUTF(hour);
        dataOutput.writeUTF(request);
        dataOutput.writeUTF(status);
        dataOutput.writeUTF(body_bytes_sent);
        dataOutput.writeUTF(http_referer);
        dataOutput.writeUTF(ref_host);
        dataOutput.writeUTF(ref_path);
        dataOutput.writeUTF(ref_quer);
        dataOutput.writeUTF(ref_query_id);
        dataOutput.writeUTF(http_user_agent);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.valid = dataInput.readBoolean();
        this.remote_addr = dataInput.readUTF();
        this.remote_user = dataInput.readUTF();
        this.time_local = dataInput.readUTF();
        this.daystr = dataInput.readUTF();
        this.timestr = dataInput.readUTF();
        this.month = dataInput.readUTF();
        this.day = dataInput.readUTF();
        this.hour = dataInput.readUTF();
        this.request = dataInput.readUTF();
        this.status = dataInput.readUTF();
        this.body_bytes_sent = dataInput.readUTF();
        this.http_referer = dataInput.readUTF();
        this.ref_host = dataInput.readUTF();
        this.ref_path = dataInput.readUTF();
        this.ref_quer = dataInput.readUTF();
        this.ref_query_id = dataInput.readUTF();
        this.http_user_agent = dataInput.readUTF();
    }


    @Override
    public String toString() {
       return this.valid + "\001" +
               this.remote_addr + "\001" +
               this.remote_user + "\001" +
               this.time_local + "\001" +
               this.daystr + "\001" +
               this.timestr + "\001" +
               this.month  + "\001" +
               this.day  + "\001" +
               this.hour + "\001" +
               this.request + "\001" +
               this.status + "\001" +
               this.body_bytes_sent + "\001" +
                  this.http_referer + "\001" +
                this.ref_host + "\001" +
                this.ref_path + "\001" +
                this.ref_quer + "\001" +
                this.ref_query_id + "\001" +
                this.http_user_agent;
    }
}
