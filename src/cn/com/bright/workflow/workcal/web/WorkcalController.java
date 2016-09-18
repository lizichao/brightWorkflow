package cn.com.bright.workflow.workcal.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.JsonMapper;
import cn.com.bright.workflow.workcal.domain.WorkcalRule;
import cn.com.bright.workflow.workcal.manager.WorkcalRuleManager;

public class WorkcalController {
    
    public static final int STATUS_WEEK = 0;
    public static final int STATUS_HOLIDAY = 1;
    public static final int STATUS_HOLIDAY_TO_WORKDAY = 2;
    public static final int STATUS_WORKDAY_TO_HOLIDAY = 3;
    // private WorkcalPartManager workcalPartManager;
    // private WorkcalRuleManager workcalRuleManager;
    private JsonMapper jsonMapper = new JsonMapper();

    private XmlDocPkgUtil xmlDocUtil = null;

    public Document doPost(Document xmlDoc) {
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("getWorkcalCalendar".equals(action)) {
            getWorkcalCalendar();
        }
        return xmlDoc;
    }

    public void getWorkcalCalendar() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String weekStr = "";
        // String
        // holidayStr="[{'date':'20150101','name':'Ԫ��'},{'date':'20150218','name':'����'},{'date':'20150219","name":"����"},{"date":"20150220","name":"����"},{"date":"20150221","name":"����"},{"date":"20150222","name":"����"},{"date":"20150223","name":"����"},{"date":"20150224","name":"����"},{"date":"20150405","name":"������"},{"date":"20150501","name":"�Ͷ���"},{"date":"20150620","name":"�����"},{"date":"20150927","name":"�����"},{"date":"20151001","name":"�����"},{"date":"20151002","name":"�����"},{"date":"20151003","name":"�����"},{"date":"20151004","name":"�����"},{"date":"20151005","name":"�����"},{"date":"20151006","name":"�����"},{"date":"20151007","name":"�����"}]";
        // String
        // workdayStr="[{"date":"20150104","name":"Ԫ������"},{"date":"20150215","name":"���ڵ���"},{"date":"20150228","name":"���ڵ���"}]";
        // String
        // extrdayStr="[{"date":"20150102","name":"Ԫ������"},{"date":"20150406","name":"�����ڲ���"},{"date":"20150622","name":"����ڲ���"},{"date":"20150928","name":"����ڲ���"}]";
        WorkcalRuleManager workcalRuleManager = ApplicationContextHelper.getWorkcalRuleManager();
        // ÿ�ܵĹ�������
        List<WorkcalRule> workcalRules = workcalRuleManager.getWorkcalRules(STATUS_WEEK, year, true);
        Set<Integer> weeks = new HashSet<Integer>();

        for (WorkcalRule workcalRule : workcalRules) {
            weeks.add(Integer.valueOf(workcalRule.getWeek() - 1));
        }

        try {
            // model.addAttribute("weeks", JsonUtil.getJsonString(weeks));
            weekStr = jsonMapper.toJson(weeks);
        } catch (IOException ex) {
            // logger.error(ex.getMessage(), ex);
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        // ��������
        List<WorkcalRule> extraWorkcalRules = workcalRuleManager.getWorkcalRules(STATUS_WEEK, year, false);

        List<Map<String, String>> holidays = new ArrayList<Map<String, String>>();
        List<Map<String, String>> workdays = new ArrayList<Map<String, String>>();
        List<Map<String, String>> extrdays = new ArrayList<Map<String, String>>();

        for (WorkcalRule workcalRule : extraWorkcalRules) {
            Map<String, String> day = new HashMap<String, String>();
            day.put("date", dateFormat.format(workcalRule.getWorkDate()));
            day.put("name", workcalRule.getName());

            if (workcalRule.getStatus() == STATUS_HOLIDAY) {
                holidays.add(day);
            } else if (workcalRule.getStatus() == STATUS_HOLIDAY_TO_WORKDAY) {
                workdays.add(day);
            } else {
                extrdays.add(day);
            }
        }

        String holidayStr = "";
        String workdayStr = "";
        String extrdayStr = "";
        try {
            // model.addAttribute("holidays", jsonMapper.toJson(holidays));
            holidayStr = jsonMapper.toJson(holidays);
        } catch (IOException ex) {
            // logger.error(ex.getMessage(), ex);
        }

        try {
            workdayStr = jsonMapper.toJson(workdays);
            // model.addAttribute("workdays", jsonMapper.toJson(workdays));
        } catch (IOException ex) {
            // logger.error(ex.getMessage(), ex);
        }

        try {
            extrdayStr = jsonMapper.toJson(extrdays);
            // model.addAttribute("extrdays", jsonMapper.toJson(extrdays));
        } catch (IOException ex) {
            // logger.error(ex.getMessage(), ex);
        }

        Element data = new Element("Data");
        Element record = new Element("Record");

        XmlDocPkgUtil.setChildText(record, "weeks", weekStr);
        XmlDocPkgUtil.setChildText(record, "holidays", holidayStr);
        XmlDocPkgUtil.setChildText(record, "workdays", workdayStr);
        XmlDocPkgUtil.setChildText(record, "extrdays", extrdayStr);
        data.addContent(record);
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }
}
