package logmonitor.utils;

import logmonitor.dao.LogMonitorDao;
import logmonitor.domain.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MonitorHandler {


    private static Logger logger = LoggerFactory.getLogger(MonitorHandler.class);
    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    //appid - rules
    private static Map<String, List<Rule>> ruleMap;
    //appid - users
    private static Map<String, List<User>> userMap;
    //app list
    private static List<App> appList;
    //user list
    private static List<User> userList;

    //定时加载配置文件标识
    private static boolean reloadFlag = false;


    //加载数据
    static {
        load();
        scheduleLoad();
    }

    public static synchronized void load() {
        if(Objects.isNull(userList)) {
            userList = loadUserList();
        }

        if(Objects.isNull(appList)) {
            appList = loadAppList();
        }

        if(Objects.isNull(ruleMap)) {
            ruleMap = loadRuleMap();
        }

        if(Objects.isNull(userMap)) {
            userMap = loadUserMap();
        }
        reloadFlag = true;
    }

    /**
     * 定时刷新数据.
     * */
    private static void scheduleLoad() {
        executorService.scheduleAtFixedRate(() -> {
            if(reloadFlag) {
                userList = loadUserList();
                appList = loadAppList();
                ruleMap = loadRuleMap();
                userMap = loadUserMap();
            }
        },0l, 10, TimeUnit.MINUTES);
    }

    /**
     * 解析输入日志，将数据按照一定的规则进行分割
     * 判断日志是否合法，主要校验日志所属应用的appId是否存在
     *
     * @param line
     * @return Message
     * */
    public static Message parse(String line) {
        String[] messageFields = line.split("\\$\\$\\$\\$\\$");
        if(messageFields.length != 2) {
            return null;
        }
        if(StringUtils.isBlank(messageFields[0]) || StringUtils.isBlank(messageFields[1])) {
            return null;
        }
        if(appIdIsValid(messageFields[0].trim())) {
            Message message = new Message();
            message.setAppId(messageFields[0].trim());
            message.setLine(messageFields[1].trim());
            return message;
        }

        return null;
    }

    /**
     * 验证appId是否有效.
     * @param appId
     * @return boolean
     * */
    private static boolean appIdIsValid(String appId) {
        try{
            for(App app : appList) {
                if(app.getId() == Integer.parseInt(appId)) {
                    return true;
                }
            }
        }catch (Exception ex) {
            return false;
        }
        return false;
    }


    /**
     * 验证日志是否需要触发告警规则
     * @param message
     * @return boolean
     * */
    public static boolean trigger(Message message) {
        if(Objects.isNull(message)) {
            load();
        }
        //获取当前appId下的所有规则
        List<Rule> ruleList = ruleMap.get(message.getAppId());
        for(Rule rule : ruleList) {
            if(message.getLine().contains(rule.getKeyword())) {
                message.setRuleId(rule.getId() + "");
                message.setKeyword(rule.getKeyword());
                return true;
            }
        }
        return false;
    }


    /**
     * 查询有效App列表
     * @return List<App>
     * */
    private static List<App> loadAppList() {
        return new LogMonitorDao().getAppList();
    }

    /**
     * 查询有效用户列表
     * @return List<User>
     * */
    private static List<User> loadUserList(){
        return new LogMonitorDao().getUserList();
    }

    /**
     * 封装应用与用户map
     * @return Map<String, List<User>>
     * */
    private static Map<String, List<User>> loadUserMap() {
        HashMap<String, List<User>> map = new HashMap<String, List<User>>();
        for(App app : appList) {
            String userIds = app.getUserIds();
            List<User> users = map.get(app.getId());
            if(users == null) {
                users = new ArrayList<User>();
                map.put(app.getId() + "", users);
            }
            String[] ids = userIds.split(",");
            for(String userId : ids) {
                users.add(queryUserById(userId));
            }
            map.put(app.getId() + "", users);
        }
        return map;
    }

    /**
     * 加载应用与规则map
     * @return Map<String, List<Rule>>
     * */
    public static Map<String, List<Rule>> loadRuleMap() {
        Map<String, List<Rule>> map = new HashMap<>();
        List<Rule> ruleList = new LogMonitorDao().getRuleList();
        for(Rule rule : ruleList) {
            List<Rule> ruListByAppId = map.get(rule.getAppId() + "");
            if(Objects.isNull(ruListByAppId)) {
                ruListByAppId = new ArrayList<>();
                map.put(rule.getAppId() + "", ruListByAppId);
            }
            ruListByAppId.add(rule);
            map.put(rule.getAppId() + "", ruListByAppId);
        }
        return map;
    }


    /**
     * 根据id查询用户信息.
     * @param userId
     * @return User
     * */
    private static User queryUserById(String userId)  {
        for(User user : userList) {
            if(user.getId() == Integer.parseInt(userId)) {
                return user;
            }
        }
        return null;
    }


    /**
     * 发送短信.
     * @param appId
     * @param users
     * @param message
     * @return boolean
     * */
    private static boolean sendSMS(String appId, List<User> users, Message message) {
        List<String> mobileList = new ArrayList<>();
        users.stream().forEach(user -> { mobileList.add(user.getMobile()); });
        appList.stream().filter(app -> app.getId() == Integer.parseInt(appId)).forEach(app -> { message.setAppName(app.getName()); });
        String content = "系统【" + message.getAppName() + "】在" + DateUtils.getDateTime() + "触发规则" + message.getRuleId() + ",关键字：" + message.getKeyword() + " 错误内容： " + message.getLine();
        //TODO
        //sendSMS
        return true;
    }

    /**
     * 发送邮件.
     * @param appId
     * @param users
     * @param message
     * @return true
     * */
    private static boolean sendMail(String appId, List<User> users, Message message) {
        List<String> receiver = new ArrayList<>();
        users.stream().forEach(user -> { receiver.add(user.getEmail()); });
        appList.stream().filter(app -> app.getId() == Integer.parseInt(appId)).forEach(app -> { message.setAppName(app.getName()); });
        if(receiver.size() >= 1) {
            String content = "系统【" + message.getAppName() + "】在" + DateUtils.getDateTime() + " 触发规则 " + message.getRuleId() + ", 关键字：" + message.getKeyword() + " 错误内容： " + message.getLine();
            //TODO
            //sendMail
        }
        return true;
    }

    /**
     * 告警通知
     * @param appId
     * @param message
     * */
    public static void toNotify(String appId, Message message) {
        List<User> users = userMap.get(appId);
        if(sendMail(appId, users, message)) {
            message.setIsEmail(1);
        }
        if (sendSMS(appId, users, message)) {
            message.setIsPhone(1);
        }
    }

    /**
     * 保存告警记录
     * @param record
     * */
    public static void save(Record record) {
        new LogMonitorDao().saveRecord(record);
    }

}
