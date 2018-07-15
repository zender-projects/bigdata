package logmonitor.dao;

import logmonitor.domain.App;
import logmonitor.domain.Record;
import logmonitor.domain.Rule;
import logmonitor.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LogMonitorDao {

    private static Logger logger = LoggerFactory.getLogger(LogMonitorDao.class);

    private JdbcTemplate jdbcTemplate;

    public LogMonitorDao() {
        jdbcTemplate = new JdbcTemplate(DataSourceUtil.getDataSource());
    }

    /**
     * 查询所有规则信息
     * @return List<Rule>
     * */
    public List<Rule> getRuleList(){
      String sql = "select " +
                        "r.id, " +
                        "r.name, " +
                        "r.keyword," +
                        "r.app_id, " +
                        "r.description " +
                    "from `t_rules` r " +
                    "where r.is_valid = 1 ";
      return jdbcTemplate.query(sql, new RowMapper<Rule>() {
          @Override
          public Rule mapRow(ResultSet resultSet, int i) throws SQLException {
              Rule rule = Rule.builder()
                      .id(resultSet.getInt(1))
                      .name(resultSet.getString(2))
                      .keyword(resultSet.getString(3))
                      .appId(resultSet.getInt(4))
                      .description(resultSet.getString(5))
                      .build();
              return rule;
          }
      });
    }


    /**
     * 查询所有应用信息.
     * @return List<App>
     * */
    public List<App> getAppList() {
        String sql = "select " +
                        "app.id, " +
                        "app.name, " +
                        "app.type_id, " +
                        "app.business, " +
                        "app.is_online, " +
                        "app.user_ids, " +
                        "app.description " +
                     "from `t_apps` app " +
                     "where app.is_valid = 1 ";

        return jdbcTemplate.query(sql, new RowMapper<App>() {
            @Override
            public App mapRow(ResultSet resultSet, int i) throws SQLException {
                App app = App.builder()
                        .id(resultSet.getInt(1))
                        .name(resultSet.getString(2))
                        .typeId(resultSet.getInt(3))
                        .business(resultSet.getString(4))
                        .isOnline(resultSet.getInt(5))
                        .userIds(resultSet.getString(6))
                        .description(resultSet.getString(7))
                        .build();
                return  app;
            }
        });
    }


    /**
     * 查询所有用户信息
     * @return List<User>
     * */
    public List<User> getUserList() {
        String sql = "select " +
                        "u.id, " +
                        "u.name, " +
                        "u.mobile, " +
                        "u.email " +
                     "from t_users u " +
                     "where u.is_valid = 1 ";

        return jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = User.builder()
                                .id(resultSet.getInt(1))
                        .name(resultSet.getString(2))
                        .mobile(resultSet.getString(3))
                        .email(resultSet.getString(4))
                        .build();
                return user;
            }
        });
    }


    /**
     * 插入触发规则的信息.
     * @param
     * */
    public void saveRecord(Record record) {
        String sql = "insert into t_trigger_record (app_id, rule_id, is_email, is_phone, is_close, notice_info,is_valid, create_user, update_user) values " +
                "(?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, record.getAppId(),
                record.getRuleId(),
                record.getIsEmail(),
                record.getIsPhone(),
                record.getIsClose(),
                record.getNoticeInfo(),
                record.getIsValid(),
                record.getCreateUser(),
                record.getUpdateUser());
    }

    public static void main(String[] args) {
        LogMonitorDao dao = new LogMonitorDao();
        System.out.println(dao.getAppList());
        System.out.println(dao.getRuleList());
        System.out.println(dao.getUserList());

        Record record = Record.builder().appId(2).ruleId(1).isEmail(1).isPhone(1).isClose(0).noticeInfo("notice info").isValid(1).createUser(1).updateUser(1).build();
        dao.saveRecord(record);
    }
}
