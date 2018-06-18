package bigdata.clickflow.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UVBean implements Writable{

    private String session;
    private String remote_addr;
    private String in_time;
    private String out_time;
    private String in_page;
    private String out_page;
    private String referer;
    private int page_visits;

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(session);
        dataOutput.writeUTF(remote_addr);
        dataOutput.writeUTF(in_time);
        dataOutput.writeUTF(out_time);
        dataOutput.writeUTF(in_page);
        dataOutput.writeUTF(out_page);
        dataOutput.writeUTF(referer);
        dataOutput.writeInt(page_visits);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.session = dataInput.readUTF();
        this.remote_addr = dataInput.readUTF();
        this.in_time = dataInput.readUTF();
        this.out_time = dataInput.readUTF();
        this.in_page = dataInput.readUTF();
        this.out_page = dataInput.readUTF();
        this.referer = dataInput.readUTF();
        this.page_visits = dataInput.readInt();
    }

    @Override
    public String toString() {
        return session + "\001" +
                remote_addr + "\001" +
                in_time + "\001" +
                out_time + "\001" +
                in_page + "\001" +
                out_page + "\001" +
                referer + "\001" +
                page_visits;
    }
}
