import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class URLTest {



    public static void main(String[] args) throws Exception{

        String url = "http://blog.fens.me/wp-includes/js/comment-reply.min.js?ver=3.6";
        String[] fields1 = url.split("//");
        System.out.println(fields1.length);
        for(String s : fields1) {
            System.out.println(s);
        }

        System.out.println(fields1[1].substring(0,fields1[1].indexOf("/")));
        //System.out.println(fields1);
        String log = "58.215.204.118 - - [18/Sep/2013:06:51:35 +0000] \"GET /wp-includes/js/jquery/jquery.js?ver=1.10.2 HTTP/1.1\" 304 0 \"http://blog.fens.me/nodejs-socketio-chat/\" \"Mozilla/5.0 (Windows NT 5.1; rv:23.0) Gecko/20100101 Firefox/23.0\"";
        SimpleDateFormat originFormater = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
        SimpleDateFormat targetFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String[] fs = log.split(" ");
        System.out.println(fs.length);
        System.out.println(fs[3].substring(1));
        String time = fs[3].substring(1);
        Date d = originFormater.parse(time);
        String rs = targetFormater.format(d);
        System.out.println(rs);

        String time1 = "2013-09-18 10:21:29";
        SimpleDateFormat targetFormater1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = targetFormater1.parse(time1);
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.MONTH));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(calendar.get(Calendar.HOUR_OF_DAY));

        System.out.println("2013-09-18 10:21:29".split(" ")[1]);

        /*
        insert into table dw_pvs_hour partition(datestr='2013-09-18')
        select
        a.month,
                a.day,
                a.hour,
                count(1) as pvs
        from ods_weblog_detail a
        where a.daystr = '2013-09-18'
        group by a.month, a.day, a.hour
        order by cast(a.hour as int);

        */
    }

}
