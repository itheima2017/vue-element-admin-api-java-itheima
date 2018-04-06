package itcast.research.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 11:13
 * Description: 日期工具类
 */
public class DateUtil {
    /**
     * 日期转换-  String -> Date
     *
     * @param dateString 字符串时间
     * @return Date类型信息
     * @throws ParseException 抛出异常
     */
    public static Date parseString2Date(String dateString) throws ParseException {
        if (dateString == null) {
            return null;
        }
        return parseString2Date(dateString, "yyyy-MM-dd");
    }

    /**
     * 日期转换-  String -> Date
     *
     * @param dateString 字符串时间
     * @param pattern    格式模板
     * @return Date类型信息
     * @throws ParseException 抛出异常
     */
    public static Date parseString2Date(String dateString, String pattern) throws ParseException {
        if (dateString == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = sdf.parse(dateString);
        return date;
    }

    /**
     * 日期转换 Date -> String
     *
     * @param date Date类型信息
     * @return 字符串时间
     * @throws ParseException 抛出异常
     */
    public static String parseDate2String(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        return parseDate2String(date, "yyyy-MM-dd");
    }

    /**
     * 日期转换 Date -> String
     *
     * @param date    Date类型信息
     * @param pattern 格式模板
     * @return 字符串时间
     * @throws ParseException 抛出异常
     */
    public static String parseDate2String(Date date, String pattern) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String strDate = sdf.format(date);
        return strDate;
    }
}
