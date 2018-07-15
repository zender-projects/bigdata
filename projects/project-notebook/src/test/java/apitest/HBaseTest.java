package apitest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;
import sun.tools.jconsole.Tab;

import java.util.ArrayList;
import java.util.List;

public class HBaseTest {


    static Configuration config = null;
    private Connection connection = null;
    private Table table = null;

    @Before
    public void init() throws Exception {
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "hadoop01,hadoop02,hadoop03");
        //config.set("hbase.zookeeper.quorum", "192.168.58.200,192.168.58.201,192.168.58.202");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        connection = ConnectionFactory.createConnection(config);
        table = connection.getTable(TableName.valueOf("user"));
    }


    /**
     * 创建一个表.
     *
     * @throws Exception
     * */
    @Test
    public void createTable() throws Exception {
        HBaseAdmin admin = new HBaseAdmin(config);
        //表名
        TableName tableName = TableName.valueOf("testtable");

        HTableDescriptor desc = new HTableDescriptor(tableName);

        //创建列族
        HColumnDescriptor family1 = new HColumnDescriptor("info1");
        HColumnDescriptor family2 = new HColumnDescriptor("info2");
        desc.addFamily(family1);
        desc.addFamily(family2);

        admin.createTable(desc);
    }

    /**
     * 删除表.
     *
     * @throws Exception
     * */
    @Test
    public void deleteTable() throws Exception {
        HBaseAdmin admin = new HBaseAdmin(config);
        admin.disableTable("testtable".getBytes());
        admin.deleteTable("testtable".getBytes());
    }


    /**
     * 向表中增加数据
     *
     * @throws Exception
     * */
    @Test
    public void insertData() throws Exception {
        Table table = connection.getTable(TableName.valueOf("testtable"));
        table.setWriteBufferSize(534534534);
        List<Put> puts = new ArrayList<>();
        for(int i = 20;i <= 30;i ++) {
            Put put = new Put(Bytes.toBytes("testrowkey_" + i));
            put.addColumn("info1".getBytes(), "name".getBytes(), "lisi".getBytes());
            put.addColumn("info1".getBytes(), "age".getBytes(), "44".getBytes());

            put.addColumn("info2".getBytes(), "address".getBytes(), "peking".getBytes());
            put.addColumn("info2".getBytes(), "position".getBytes(), "changping".getBytes());
            puts.add(put);
        }
        table.put(puts);
        table.close();
    }

    /**
     * 添加一条记录
     *
     * @throws  Exception
     * */
    @Test
    public void insertData2() throws Exception {
        Table table = connection.getTable(TableName.valueOf("testtable"));
        Put put = new Put("testrowkey_10".getBytes());
        put.addColumn("info1".getBytes(), "name".getBytes(), "zhangsan".getBytes());
        put.addColumn("info2".getBytes(), "address".getBytes(), "peking".getBytes());
        table.put(put);
        table.close();
    }

    /**
     * 删除一行
     *
     * @throws Exception
     * */
    @Test
    public void deleteDate() throws Exception {
        Table table = connection.getTable(TableName.valueOf("testtable"));
        Delete delete = new Delete("testrowkey_20".getBytes());
        table.delete(delete);
        table.close();
    }


    /**
     * 删除某列
     *
     * @throws Exception
     * */
    @Test
    public void deleteData2() throws Exception {
        Table table = connection.getTable(TableName.valueOf("testtable"));
        Delete delete = new Delete("testrowkey_21".getBytes());
        delete.addColumn("info1".getBytes(), "name".getBytes());
        table.delete(delete);
        table.close();
    }


    /**
     * 查询单条数据
     *
     * @throws Exception
     * */
    @Test
    public void getRow() throws Exception {
        Table table = connection.getTable(TableName.valueOf("testtable"));
        Get get = new Get("testrowkey_10".getBytes());
        Result result = table.get(get);
        System.out.println(Bytes.toString(result.getValue("info1".getBytes(), "name".getBytes())));
        System.out.println(Bytes.toString(result.getValue("info2".getBytes(), "address".getBytes())));
    }

    /**
     * 全表扫描
     *
     *
     * */
    @Test
    public void scanTable() throws Exception {
         Scan scan = new Scan();
         scan.setStartRow("testrowkey_20".getBytes());
         scan.setStopRow("testrowkey_25".getBytes());
         Table table = connection.getTable(TableName.valueOf("testtable".getBytes()));
         ResultScanner rs = table.getScanner(scan);
         for(Result result : rs) {
             System.out.println(Bytes.toString(result.getValue("info1".getBytes(), "name".getBytes())) + ","
                     + Bytes.toString(result.getValue("info2".getBytes(), "address".getBytes())));
             //System.out.println(Bytes.toString(result.getValue("info2".getBytes(), "address".getBytes())));
         }
    }

    /**
     * 值过滤器
     *
     * @exception Exception
     * */
    @Test
    public void simpleValueFilter() throws Exception {
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        filterList.addFilter(new SingleColumnValueFilter("info1".getBytes(), "age".getBytes(),
                CompareFilter.CompareOp.EQUAL,Bytes.toBytes(44)));

        filterList.addFilter(new SingleColumnValueFilter("info1".getBytes(), "name".getBytes(),
                CompareFilter.CompareOp.EQUAL, Bytes.toBytes("lisi")));

        Scan scan = new Scan();
        scan.addColumn("info1".getBytes(), "name".getBytes());
        scan.addColumn("info1".getBytes(), "age".getBytes());
        scan.setFilter(filterList);

        Table table = connection.getTable(TableName.valueOf("testtable".getBytes()));
        ResultScanner results = table.getScanner(scan);

        for(Result result : results) {
            System.out.println(Bytes.toString(result.getValue("info1".getBytes(), "name".getBytes())) + ","
                    + Bytes.toString(result.getValue("info1".getBytes(), "age".getBytes())));
        }
    }

    /**
     * 单列前缀过滤器.
     *
     * @throws Exception
     * */
    @Test
    public void columnPrefixFilter() throws Exception {
        //以n开头
        ColumnPrefixFilter filter = new ColumnPrefixFilter(Bytes.toBytes("n"));
        Scan scan = new Scan();
        scan.addFamily("info1".getBytes());
        scan.setFilter(filter);


        Table table = connection.getTable(TableName.valueOf("testtable".getBytes()));
        ResultScanner results = table.getScanner(scan);

        for(Result result : results) {
            System.out.println(Bytes.toString(result.getValue("info1".getBytes(), "name".getBytes())) + ","
                    + Bytes.toString(result.getValue("info1".getBytes(), "age".getBytes())));
        }
    }


    /**
     * 多个列前缀过滤器
     *
     * @throws Exception
     * */
    @Test
    public void multiColumnPrefixFilter() throws Exception {
        byte[][] prefixs = new byte[][]{Bytes.toBytes("n"), Bytes.toBytes("a")};
        MultipleColumnPrefixFilter filter = new MultipleColumnPrefixFilter(prefixs);

        Scan scan = new Scan();
        scan.setFilter(filter);

        Table table = connection.getTable(TableName.valueOf("testtable".getBytes()));
        ResultScanner results = table.getScanner(scan);

        for(Result result : results) {
            System.out.println(Bytes.toString(result.getValue("info1".getBytes(), "name".getBytes())) + ","
                    + Bytes.toString(result.getValue("info1".getBytes(), "age".getBytes())));
        }
    }

    /**
     * Row Key过滤器
     *
     * @throws Exception
     * */
    @Test
    public void rowkeyFilter() throws Exception {
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.GREATER_OR_EQUAL, new RegexStringComparator("^testrowkey_25"));
        Scan scan = new Scan();
        scan.setFilter(rowFilter);

        Table table = connection.getTable(TableName.valueOf("testtable".getBytes()));
        ResultScanner results = table.getScanner(scan);

        for(Result result : results) {
            System.out.println(Bytes.toString(result.getValue("info1".getBytes(), "name".getBytes())) + ","
                    + Bytes.toString(result.getValue("info1".getBytes(), "age".getBytes())));
        }
    }
}
