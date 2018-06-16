package bigdata.clickflow.utils;

import bigdata.clickflow.bean.WebLogBean;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Log
public class WebLogParseUtil {


    /*
		157.55.35.40    0
		-               1
		-               2
		[18/Sep/2013:06:51:43   3
		+0000]  4
		"GET    5
		/rhadoop-java-basic/   6
		HTTP/1.1"    7
		200          8
		26780        9
		"-"          10
		"Mozilla/5.0 11
		(compatible; 12
		bingbot/2.0; 13
		+http://www.bing.com/bingbot.htm)"  14
	*/

    /**
     * 解析日志数据->WebLogBean
     * @param line 一行日志数据
     * @return  WebLogbean
     * */
    public static WebLogBean parse(String line) {
        if(StringUtils.isEmpty(line)) {
            return null;
        }

        String[] fields = line.split(" ");
        WebLogBean logBean = WebLogBean.builder().build();

        if(fields.length > 11) {
            logBean.setValid(true);
            logBean.setRemote_addr(fields[0]);
            logBean.setRemote_user(fields[1]);
            logBean.setTime_local(formatDate(fields[3].substring(1)));
            logBean.setRequest(fields[6]);
            logBean.setStatus(fields[8]);
            logBean.setBody_bytes_sent(fields[9]);
            logBean.setHttp_referer(fields[10]);

            //拼接客户端信息
            if(fields.length > 12) {
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 11;i < fields.length;i ++) {
                    stringBuilder.append(fields[i]);
                }
                logBean.setHttp_user_agent(stringBuilder.toString());
            }else{
                logBean.setHttp_user_agent(fields[11]);
            }

            //校验请求状态
            if(Integer.parseInt(logBean.getStatus()) > 400) {
                logBean.setValid(false);
            }

            //校验请求时间
            if("invalid-time-".equals(logBean.getTime_local())) {
                logBean.setValid(false);
            }
        }else{
            logBean.setValid(false);
        }
        return logBean;
    }

    /**
     * 格式化请求时
     * @param time_local
     * @return String
     * */
    private static String formatDate(String time_local) {
        SimpleDateFormat originFormater = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss", Locale.US);
        SimpleDateFormat targetFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try{
            return targetFormater.format(originFormater.parse(time_local));
        }catch (ParseException ex) {
            log.info("date format exception," + ex.toString());
            ex.printStackTrace();
            return "-invalid_time-";
        }
    }

    /**
     * @param bean
     * @param validUrls 有效到url
     * */
    public static void filtStaticResource(WebLogBean bean, Set<String> validUrls) {
        if(!Objects.isNull(bean)) {
            for(String validUrl : validUrls) {
                if(!Objects.isNull(bean.getRequest()) && bean.getRequest().contains(validUrl)) {
                    bean.setValid(true);
                    return;
                }
            }
            bean.setValid(false);
        }
    }
}
