package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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


    /**
     * x下载
     * @param localPath
     * @param hdfsPath
     * */
    public void downLoadFromHdfs(String localPath, String hdfsPath) throws Exception {
        //hdfs.copyToLocalFile(new Path(hdfsPath, new Path(localPath)));
        hdfs.copyToLocalFile(new Path(hdfsPath), new Path(localPath));
    }


    public void downloadFormHdfsNew(String localPath, String hdfsPath) throws Exception {
        InputStream inputStream = hdfs.open(new Path(hdfsPath));

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localPath));

        IOUtils.copyBytes(inputStream, out, 1024);

        IOUtils.closeStream(inputStream);
        IOUtils.closeStream(out);
    }

    /**
     * 压缩上传
     *
     * */
    public void compressUpload(String localPath, String hdfsPath) throws Exception{
        String codecClassName = "org.apache.hadoop.io.compress.GzipCodec";
        Class<?> codecClass = Class.forName(codecClassName);

        Configuration configuration = new Configuration();
        CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, configuration);

        FileOutputStream outputStream = new FileOutputStream(hdfsPath);
        CompressionOutputStream out = codec.createOutputStream(outputStream);

        FileInputStream in = new FileInputStream(localPath);

        IOUtils.copyBytes(in, out, 1024);

        out.finish();
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
