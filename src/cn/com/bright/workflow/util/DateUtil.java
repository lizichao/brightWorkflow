package cn.com.bright.workflow.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cn.brightcom.jraf.util.StringUtil;

public final class DateUtil {
    private DateUtil() {

    }

    public static String date24ToString(Date inputDate) {
        if(null == inputDate){
            return null;
        }
        SimpleDateFormat formate24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formate24.format(inputDate);
    }

    /**
     * 转换成12小时格式字符串
     * @param inputDate
     * @return
     */
    public static String date12ToString(Date inputDate) {
        if(null == inputDate){
            return null;
        }
        SimpleDateFormat formate12 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formate12.format(inputDate);
    }

    /**
     * 转换成24小时格式数字串
     * @param inputDate
     * @return
     */
    public static String date24ToNumberString(Date inputDate) {
        SimpleDateFormat formate24numstr = new SimpleDateFormat("yyyyMMddHHmmss");
        return formate24numstr.format(inputDate);
    }

    /**
     * 转换成24小时格式数字串
     * @param inputDate
     * @return
     */
    public static String dateToDateString(Date inputDate) {
        SimpleDateFormat formateDate = new SimpleDateFormat("yyyy-MM-dd");
        return formateDate.format(inputDate);
    }

    /**
     * 按指定格式转换成24小时格式数字串
     * @param inputDate
     * @param pattern
     * @return
     */
    public static String dateToDateString(Date inputDate, String pattern) {
        SimpleDateFormat formateDate = new SimpleDateFormat(pattern);
        return formateDate.format(inputDate);
    }

    /**
     * 在某个时间中增加添加月
     * @param month
     * @return String
     */
    public static String addMonth(int month, Date inputDate) {
        // 创建一个日历对象
        GregorianCalendar cd = new GregorianCalendar();
        cd.setTime(inputDate);
        // 增加月
        cd.add(Calendar.MONTH, month);
        SimpleDateFormat formate12 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formate12.format(cd.getTime());
    }

    /**
     * 比较两个日期的大小 d1>d2则 return 1,d1==d2则 return 0, d1<d2则return -1.
     * @param date
     * @return int
     */
    public static int dateCompare(Date d1, Date d2) {
        Calendar cdA = Calendar.getInstance();
        cdA.setTime(d1);
        Calendar cdB = Calendar.getInstance();
        cdB.setTime(d2);
        return cdA.compareTo(cdB);
    }

    /**
     * @param str
     * @return Date
     * @throws ParseException
     */
    public static Date stringToDate(String str)  {
        if(StringUtil.isEmpty(str)){
            return null;
        }
        Date resultDate = null;
        SimpleDateFormat formateDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            resultDate =  formateDate.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultDate;
    }

    public static Date stringToDateTime(String str) throws ParseException {
        if(StringUtil.isEmpty(str)){
            return null;
        }
        Date resultDate = null;
        SimpleDateFormat formateDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            resultDate =  formateDate.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    /**
     * @param str
     * @return Date
     * @throws ParseException
     */
    public static String dateToString(Date inputDate) {
        SimpleDateFormat formateDate = new SimpleDateFormat("yyyy/MM/dd");
        return formateDate.format(inputDate);
    }
}
