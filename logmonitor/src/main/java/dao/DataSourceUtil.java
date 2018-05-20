package dao;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.java.Log;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * 数据库连接工具类.
 * @author mac
 * */
@Log
public class DataSourceUtil {

    private static DruidDataSource dataSource;

    static {
       init();
    }

    public static void init(){
        dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setMaxActive(10);
        dataSource.setMaxWait(3000);
    }

    public static synchronized DataSource getDataSource(){
        if(Objects.isNull(dataSource)) {
           init();
        }
        return dataSource;
    }
}
