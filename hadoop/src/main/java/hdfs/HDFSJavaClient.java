package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

public class HDFSJavaClient {


    private Configuration configuration;
    private FileSystem hdfs;

    //@Before
    public void init() throws Exception{
        //初始化配置
        configuration = new Configuration();
        //configuration.set("fs.defaultFS", "hdfs://master:9000");
        //configuration.set("","1");
        //初始化文件系统对象
        //jvm user参数 -DHADOOP_USER_NAME=root
        configuration.set("hadoop.home.dir","/Users/mac/myapp/devenvironment/hadoop");
        hdfs = FileSystem.get(URI.create("hdfs://master:9000"), configuration, "root");
    }

    //@After
    public void destroy() throws Exception{
        if(hdfs != null) {
            hdfs.close();
        }
    }

    //@Test
    /**
     * 上传本地文件到HDFS
     * @param  localPath
     * @param  hdfsPath
     * */
    public void uploadFromLocal(String localPath, String hdfsPath) throws Exception{
        hdfs.copyFromLocalFile(new Path(localPath), new Path(hdfsPath));
    }






    public static void main(String[] args) throws Exception{

        args = new String[3];

        args[0] = "uploadFromLocal";
        args[1] = "/Users/mac/IdeaProjects/bigdata/datas/test_hdfs/a.text";
        args[2] = "/data/wc/in";

        if(args.length < 3) {
            throw new RuntimeException("HDFS Client parameter exception.");
        }

        HDFSJavaClient client = new HDFSJavaClient();
        client.init();

        String method = args[0];
        String localPath = args[1];
        String hdfsPath = args[2];

        switch (method) {
            case "uploadFromLocal": {
                client.uploadFromLocal(localPath, hdfsPath);
            }
        }

        if("uploadFromLocal".equals(method)) {
            client.uploadFromLocal(localPath, hdfsPath);
        }


        client.destroy();
    }


}
