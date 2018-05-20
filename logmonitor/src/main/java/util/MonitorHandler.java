package util;

import dao.LogMonitorDao;
import domain.App;
import domain.Rule;
import domain.User;
import lombok.extern.java.Log;

import java.util.*;

/**
 * 日志监控核心工具类.
 * */
@Log
public class MonitorHandler {

    //appid 与 过滤规则映射关系
    private static Map<String, List<Rule>> ruleMap;
    //appid 与 用户映射关系
    private static Map<String, List<User>> userMap;
    //所用应用信息
    private static List<App> appList;
    //所有用户信息
    private static List<User> userList;
    //定时加载配置文件对标记
    private static boolean reloaded = false;

    static {
        load();
    }

    /**
     * 加载全局配置信息
     * */
    public static synchronized void load() {
        if(Objects.isNull(userList)) {
            userList = loadUserList();
        }
        if(Objects.isNull(appList)) {
            //加载应用信息
            appList = loadAppList();
        }
        if(Objects.isNull(ruleMap)) {
            //加载app-rule映射
            ruleMap = loadRuleMap();
        }
        if(Objects.isNull(userMap)){
            //加载app-users映射
            userMap = loadUserMap();
        }
    }

    /**
     * 加载应用列表
     * @return List<App>
     * */
    public static List<App> loadAppList(){
        return new LogMonitorDao().getAppList();
    }

    /**
     * 加载用户列表
     * @return List<User>
     * */
    public static List<User> loadUserList(){
        return new LogMonitorDao().getUserList();
    }

    /**
     * 加载并封装app-rule map
     * @return Map<String, List<Rule>>
     * */
    public static Map<String, List<Rule>> loadRuleMap() {
        Map<String, List<Rule>> map = new HashMap<>();
        LogMonitorDao dao = new LogMonitorDao();
        List<Rule> ruleList = dao.getRuleList();
        for(Rule rule : ruleList) {
            List<Rule> ruleListByAppId = map.get(rule.getAppId() + "");
            if(Objects.isNull(ruleListByAppId)) {
                ruleListByAppId = new ArrayList<>();
                map.put(rule.getAppId() + "", ruleListByAppId);
            }
            ruleListByAppId.add(rule);
            map.put(rule.getAppId() + "", ruleListByAppId);
        }
        return map;
    }

    public static Map<String, List<User>> loadUserMap() {
        Map<String, List<User>> map = new HashMap<>();
        if(Objects.isNull(appList))
            appList = new LogMonitorDao().getAppList();

        if(Objects.isNull(userList))
            userList = new LogMonitorDao().getUserList();

        for(App app : appList) {
            String userIds = app.getUserIds();
            List<User> userListInApp = map.get(app.getId() + "");
            if(Objects.isNull(userListInApp)) {
                userListInApp = new ArrayList<>();
                map.put(app.getId() + "", userListInApp);
            }
            String[] userIdArray = userIds.split(",");
            for (String userId : userIdArray) {
                userListInApp.add(queryUserById(userId));
            }
            map.put(app.getId() + "", userListInApp);
        }
        return map;
    }

    private static User queryUserById(String userId) {
        for(User user : userList) {
            if(user.getId() == Integer.valueOf(userId)) {
                return user;
            }
        }
        return null;
    }

}
