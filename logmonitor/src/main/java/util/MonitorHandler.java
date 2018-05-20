package util;

import dao.LogMonitorDao;
import domain.App;
import domain.Message;
import domain.Rule;
import domain.User;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;

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
     * 从日志信息中分离出 appid 和 日志内容
     *
     * 解析输入日志，将数据按照一定规则进行分割
     * 判断日志是否合法，主要校验appid的合法性
     *
     * @param message
     * @return Message
     * */
    public static Message parse(String message) {

        String[] messageStr = message.split("\\$\\$\\$\\$\\$");
        if(messageStr.length != 2 ||StringUtils.isBlank(messageStr[0])
                || StringUtils.isBlank(messageStr[1])) {
            log.info("message parse error:" + message);
            return null;
        }
        if(appIdIsValid(messageStr[0])) {
            Message mesg = new Message();
            mesg.setAppId(Integer.parseInt(messageStr[0].trim()));
            mesg.setContent(messageStr[1].trim());
            return mesg;
        }
        return null;
    }


    /**
     * 判断当前Message是否触发过滤规则
     * @param message
     * @return boolean
     * */
    public static boolean trigger(Message message) {
        if(Objects.isNull(ruleMap)){
            load();
        }
        //获取当前appid下的所有规则
        List<Rule> ruleList = ruleMap.get(String.valueOf(message.getAppId()));
        if(!Objects.isNull(ruleList)){
            for(Rule rule : ruleList) {
                if(message.getContent().contains(rule.getKeyword())) {
                    message.setRuleId(rule.getId());
                    message.setKeyword(rule.getKeyword());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 定时加载配置信息
     *
     * 1.获取分钟数值，当分钟数据是10的倍数，就会触发reloadDataModel
     * 2.reloadDataModel是线程安全的方法，在当前worker中只能有一个线程成功执行
     * 3.为了保证当前线程操作完成，其他线程不再重复操作
     *      在非reload时间段，reloaded一直倍设置为true
     *      在reload时间段，第一个线程进入reloadDataModel方法，加载完成后设置reloaded为false
     * */
    public static void scheduleLoad() {
        String date = DateUtil.getDateTime();
        int now = Integer.parseInt(date.split(":")[1]);
        //分钟数是10的整数倍
        if(now % 10 == 0) {
            //更新数据
            reloadDataModel();
        }else{
            //设置更新标记
            reloaded = true;
        }
    }

    /**
     * 重新加载数据模型
     * */
    public static synchronized void reloadDataModel() {
        if(reloaded)
        {
            long start = System.currentTimeMillis();
            userList = loadUserList();
            appList = loadAppList();
            ruleMap = loadRuleMap();
            userMap = loadUserMap();
            reloaded = false;
            log.info("配置文件reload完成，" +
                    "时间：" + DateUtil.getDateTime() +
                    ",耗时：" + (System.currentTimeMillis() - start));
        }
    }



    /**
     * 验证appid是否有效
     * @param appId
     * @return boolean
     */
    private static boolean appIdIsValid(String appId) {
        try{
            for(App app : appList) {
                if(app.getId() == Integer.parseInt(appId)) {
                    return true;
                }
            }
        }catch (NumberFormatException ex) {
            return false;
        }
        return false;
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
