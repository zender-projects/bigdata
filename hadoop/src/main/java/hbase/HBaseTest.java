package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HBaseTest {

    Configuration hbaseConfiguration = null;
    Connection conn;
    HBaseAdmin admin;

    @Before
    public void init() throws Exception {
        hbaseConfiguration = HBaseConfiguration.create();
        hbaseConfiguration.set("hbase.zookeeper.quorum", "node01,node02,node03");
        hbaseConfiguration.set("hbase.zookeeper.property.clientPort", "2181");
        conn = ConnectionFactory.createConnection(hbaseConfiguration);
    }

    @After
    public void close() throws Exception {
        if(!Objects.isNull(admin)) {
            admin.close();
        }
    }


    /**
     * 创建表：
     * 指定表名，列族名称
     * */
    //@Test
    @SuppressWarnings("deprecation")
    public void createTable() throws Exception {
        admin = new HBaseAdmin(hbaseConfiguration);
        HTableDescriptor table = new HTableDescriptor(Bytes.toBytes("user"));
        //添加列族
        table.addFamily(new HColumnDescriptor(Bytes.toBytes("base")));
        table.addFamily(new HColumnDescriptor(Bytes.toBytes("ext")));

        admin.createTable(table);
    }

    /**
     * 创建表
     *
     * */
    @SuppressWarnings("deprecation")
    //@Test
    public void createTable2() throws Exception {
        admin = new HBaseAdmin(hbaseConfiguration);

        TableName tableName = TableName.valueOf("user");
        //表的 描述
        HTableDescriptor desc = new HTableDescriptor(tableName);
        //列族描述
        HColumnDescriptor info = new HColumnDescriptor("info");
        HColumnDescriptor ext = new HColumnDescriptor("ext");

        desc.addFamily(info);
        desc.addFamily(ext);

        admin.createTable(desc);
    }


    /**
     * 删除表
     * */
    //@Test
    @SuppressWarnings("deprecation")
    public void deleteTable() throws Exception {
        admin = new HBaseAdmin(hbaseConfiguration);
        //先禁用，再删
        admin.disableTable("user".getBytes());
        admin.deleteTable("user".getBytes());
    }


    /**
     * 向表中增加数据， 批量
     * */
    //@Test
    @SuppressWarnings("deprecation")
    public void insertData() throws Exception {
        Table table = conn.getTable(TableName.valueOf("user"));
        table.setWriteBufferSize(534534534);
        List<Put> puts = new ArrayList<Put>();

        for(int i = 20;i <=30;i ++) {
            Put put = new Put(Bytes.toBytes("testrowkey_" + i));
            put.add("info".getBytes(), "name".getBytes(), "zhangsan".getBytes());
            put.add("info".getBytes(), "age".getBytes(), ("" + i).getBytes());
            puts.add(put);
        }
        table.put(puts);
        table.close();

    }


    /**
     * 修改数据，与增加逻辑相同，即覆盖
     * */
    //@Test
    public void updateData() throws Exception {
        Table table = conn.getTable(TableName.valueOf("user"));
        Put put = new Put("testrowkey_20".getBytes());
        put.addColumn("info".getBytes(), "name".getBytes(), "lisi".getBytes());
        put.addColumn("info".getBytes(), "age".getBytes(), "222".getBytes());
        table.put(put);
        table.close();
    }

    /**
     * 删除一行数据
     * */
    //@Test
    public void deleteData() throws Exception {
        Table table = conn.getTable(TableName.valueOf("user"));
        Delete delete = new Delete("testrowkey_20".getBytes());
        table.delete(delete);
        table.close();
    }

    /**
     * 删除某个列
     * */
    //@Test
    public void deleteData2() throws Exception {
        Table table = conn.getTable(TableName.valueOf("user"));
        Delete delete = new Delete("testrowkey_21".getBytes());
        delete.addColumn("info".getBytes(), "name".getBytes());
        table.delete(delete);
        table.close();
    }



    /**
     * 查询单条数据
     * */
    //@Test
    public void getRow() throws Exception {
        Table table = conn.getTable(TableName.valueOf("user"));
        Get get = new Get("testrowkey_22".getBytes());
        Result result = table.get(get);

        System.out.println(Bytes.toString(result.getValue("info".getBytes(), "name".getBytes())));
        System.out.println(Bytes.toString(result.getValue("info".getBytes(), "age".getBytes())));
    }


    /**
     * 全表扫描
     * */
    @Test
    public void scanTable() throws Exception {
        Scan scan = new Scan();
        scan.setStartRow("testrowkey_23".getBytes());
        scan.setStopRow("testrowkey_25".getBytes());

        Table table = conn.getTable(TableName.valueOf("user".getBytes()));
        ResultScanner rs = table.getScanner(scan);
        for(Result r : rs) {
            System.out.println(Bytes.toString(r.getValue("info".getBytes(), "name".getBytes())));
            System.out.println(Bytes.toString(r.getValue("info".getBytes(), "age".getBytes())));
        }
    }
    /**
     * 值过滤器
     * */
    @Test
    public void simpleValueFilter() throws Exception {
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        filterList.addFilter(new SingleColumnValueFilter("info".getBytes(),
                "age".getBytes(),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(23)));
        filterList.addFilter(new SingleColumnValueFilter("info".getBytes(),
                "age".getBytes(),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(25)));

        Scan scan = new Scan();
        scan.addColumn("info".getBytes(), "age".getBytes());
        scan.setFilter(filterList);

        Table table = conn.getTable(TableName.valueOf("user".getBytes()));
        ResultScanner result = table.getScanner(scan);

        for(Result r : result) {
            //System.out.println(Bytes.toString(r.getValue("info".getBytes(), "name".getBytes())));
            System.out.println(Bytes.toString(r.getValue("info".getBytes(), "age".getBytes())));
        }
    }

    /**
     * 指定单个列前缀的过滤器
     * */
    @Test
    public void columnPrefixFilter() throws Exception {

        //值过滤以n为开头的column的值
        ColumnPrefixFilter prefixFilter = new ColumnPrefixFilter(Bytes.toBytes("n"));

        Scan scan = new Scan();
        scan.addFamily("info".getBytes());
        scan.setFilter(prefixFilter);

        Table table = conn.getTable(TableName.valueOf("user".getBytes()));
        ResultScanner result = table.getScanner(scan);

        for(Result r : result) {
            System.out.println(Bytes.toString(r.getValue("info".getBytes(), "name".getBytes())));
            //System.out.println(Bytes.toString(r.getValue("info".getBytes(), "age".getBytes())));
        }
    }


    /**
     * 可指定多个列前缀的过滤器
     * */
    @Test
    public void multiColumnPrefixFilter() throws Exception {
        //以n为开头的column 和 以 a 为开头的column
        byte[][] prefixes = new byte[][]{Bytes.toBytes("n"), Bytes.toBytes("a")};

        MultipleColumnPrefixFilter filter = new MultipleColumnPrefixFilter(prefixes);

        Scan scan = new Scan();
        scan.setFilter(filter);

        Table table = conn.getTable(TableName.valueOf("user".getBytes()));
        ResultScanner result = table.getScanner(scan);

        for(Result r : result) {
            System.out.println(Bytes.toString(r.getValue("info".getBytes(), "name".getBytes())));
            System.out.println(Bytes.toString(r.getValue("info".getBytes(), "age".getBytes())));
        }
    }

    /**
     * row key 过滤器
     * */
    @Test
    public void rowKeyFilter() throws Exception {
        //匹配以123开头的testrowkey_3
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^testrowkey_3"));
        Scan scan = new Scan();
        scan.setFilter(rowFilter);

        Table table = conn.getTable(TableName.valueOf("user".getBytes()));
        ResultScanner result = table.getScanner(scan);

        for(Result r : result) {
            System.out.println(Bytes.toString(r.getValue("info".getBytes(), "name".getBytes())));
            System.out.println(Bytes.toString(r.getValue("info".getBytes(), "age".getBytes())));
        }
    }
}
