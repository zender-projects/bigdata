package dao;

import domain.App;
import domain.Rule;
import domain.User;
import lombok.extern.java.Log;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Log
public class LogMonitorDao {
    private JdbcTemplate jdbcTemplate;
    public LogMonitorDao(){
        jdbcTemplate = new JdbcTemplate(DataSourceUtil.getDataSource());
    }

    /**
     * 查询所有用户信息
     * @return List<User>
     * */
    public List<User> getUserList(){
        String sql = "select " +
                        "id, name, mobile, email, isValid " +
                     "from log_monitor.tb_log_monitor_user where isValid = 1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<User>(User.class));
    }

    /**
     * 查询所有应用信息
     * @return List<App>
     * */
    public List<App> getAppList(){
        String sql = "select " +
                        " id, name, isOnline, typeId, userId " +
                     "from log_monitor.tb_log_monitor_app where isValid = 1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<App>(App.class));
    }

    /**
     * 查询所有规则信息
     * @return List<Rule>
     * */
    public List<Rule> getRuleList() {
        String sql = "select " +
                        " id, name, keywod, appId, isValid " +
                     "from log_monitor.tb_log_monitor_rule where isValid = 1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Rule>(Rule.class));
    }



}
