package cn.com.bright.workflow.web;

import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import cn.com.bright.workflow.util.DateUtil;
import cn.com.bright.workflow.util.RandomUtil;

public class TestJava {

    public static void main(String[] args) throws Exception {
        Date dueDate = DateUtil.stringToDate("2015-09-17 12:00:33");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dueDate);

        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        Duration duration = datatypeFactory.newDuration("-" + "PT5M");
        duration.addTo(calendar);

        Date noticeDate = calendar.getTime();
        String gg = DateUtil.date24ToString(noticeDate);
        // System.out.print(gg);

        String ggs = RandomUtil.getRandomString("F");
        System.out.println(ggs);
    }
}
