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
 * session string,
 remote_addr string,
 remote_user string,
 time_local string,
 request string,
 visit_step string,
 page_staylong string,
 http_referer string,
 http_user_agent string,
 body_bytes_sent string,
 status string
 * */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PVBean implements Writable{

    private String session;
    private String remote_addr;
    private String remote_user;
    private String time_local;
    private String request;
    private int visit_step;
    private String page_staylong;
    private String http_referer;
    private String http_user_agent;
    private String body_bytes_sent;
    private String status;


    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(session);
        dataOutput.writeUTF(remote_addr);
        dataOutput.writeUTF(remote_user);
        dataOutput.writeUTF(time_local);
        dataOutput.writeUTF(request);
        dataOutput.writeInt(visit_step);
        dataOutput.writeUTF(page_staylong);
        dataOutput.writeUTF(http_referer);
        dataOutput.writeUTF(http_user_agent);
        dataOutput.writeUTF(body_bytes_sent);
        dataOutput.writeUTF(status);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.session = dataInput.readUTF();
        this.remote_addr = dataInput.readUTF();
        this.remote_user = dataInput.readUTF();
        this.time_local = dataInput.readUTF();
        this.request = dataInput.readUTF();
        this.visit_step = dataInput.readInt();
        this.page_staylong = dataInput.readUTF();
        this.http_referer =  dataInput.readUTF();
        this.http_user_agent = dataInput.readUTF();
        this.body_bytes_sent = dataInput.readUTF();
        this.status = dataInput.readUTF();
    }

    @Override
    public String toString() {
       return session + "\001" +
               remote_addr + "\001" +
               remote_user + "\001" +
               time_local + "\001" +
               request + "\001" +
               visit_step + "\001" +
               page_staylong + "\001" +
               http_referer + "\001" +
               http_user_agent + "\001" +
               body_bytes_sent + "\001" +
               status;
    }
}
