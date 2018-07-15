package logmonitor.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import logmonitor.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * 数据库访问工具类.
 * @author mac
 * */
public class DataSourceUtil {
    private static Logger logger = LoggerFactory.getLogger(DataSourceUtil.class);
    private static ComboPooledDataSource dataSource;

    private static String url = "jdbc:mysql://192.168.58.5:3306/db_logmonitor?useUnicode=true&characterEncoding=utf-8";
    private static String username = "root";
    private static String password = "root";


    static {
       init();
    }

    private static void init() {
        dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setInitialPoolSize(10);
        dataSource.setIdleConnectionTestPeriod(300000);
    }

    public static synchronized DataSource getDataSource() {
        if(Objects.isNull(dataSource)) {
           // dataSource = new ComboPooledDataSource();
            init();
        }
        return dataSource;
    }


    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<User> users = jdbcTemplate.query("select * from t_users", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = User.builder()
                    .id(resultSet.getInt(1))
                    .name(resultSet.getString(2))
                    .mobile(resultSet.getString(3))
                    .email(resultSet.getString(4))
                    .isValid(resultSet.getInt(5))
                        .build();

                return user;
            }
        });

        System.out.println(users);
    }
}
