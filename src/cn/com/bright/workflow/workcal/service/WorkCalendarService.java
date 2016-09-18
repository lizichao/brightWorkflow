package cn.com.bright.workflow.workcal.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.datatype.Duration;

import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.workcal.domain.WorkcalPart;
import cn.com.bright.workflow.workcal.domain.WorkcalRule;
import cn.com.bright.workflow.workcal.manager.WorkcalPartManager;
import cn.com.bright.workflow.workcal.manager.WorkcalRuleManager;
import cn.com.bright.workflow.workcal.support.DayPart;
import cn.com.bright.workflow.workcal.support.Holiday;
import cn.com.bright.workflow.workcal.support.WorkCalendar;
import cn.com.bright.workflow.workcal.support.WorkDay;

public class WorkCalendarService {
    // private static Logger logger = LoggerFactory
    // .getLogger(WorkCalendarService.class);
    public static final int STATUS_WEEK = 0;
    public static final int STATUS_HOLIDAY = 1;
    public static final int STATUS_HOLIDAY_TO_WORKDAY = 2;
    public static final int STATUS_WORKDAY_TO_HOLIDAY = 3;
    private WorkCalendar workCalendar;
    // private WorkcalRuleManager workcalRuleManager;
    // private WorkcalPartManager workcalPartManager;
    private String hourFormatText = "HH:mm";
    private boolean enabled = true;

    public Date processDate(Date date) {
        return workCalendar.findWorkDate(date);
    }

    public Date add(Date date, Duration duration) {
        return workCalendar.add(date, duration);
    }

    public void processWeek() throws Exception {
        List<WorkDay> days = new ArrayList<WorkDay>(8);
        days.add(new Holiday(workCalendar));
        days.add(new Holiday(workCalendar));
        days.add(new Holiday(workCalendar));
        days.add(new Holiday(workCalendar));
        days.add(new Holiday(workCalendar));
        days.add(new Holiday(workCalendar));
        days.add(new Holiday(workCalendar));
        days.add(new Holiday(workCalendar));
        WorkcalRuleManager workcalRuleManager = ApplicationContextHelper.getWorkcalRuleManager();
        WorkcalPartManager workcalPartManager = ApplicationContextHelper.getWorkcalPartManager();
        // 每周的工作规则
        List<WorkcalRule> workcalRules = workcalRuleManager.getWorkcalRules(STATUS_WEEK, null, true);

        for (WorkcalRule workcalRule : workcalRules) {
            WorkDay day = new WorkDay(workCalendar);
            int dayPartIndex = 0;
            List<DayPart> dayParts = day.getDayParts();

            for (WorkcalPart workcalPart : workcalPartManager.getWorkcalParts(String.valueOf(workcalRule
                .getId()))) {
                DayPart dayPart = new DayPart();
                dayPart.setWorkDay(day);
                dayPart.setIndex(dayPartIndex);

                Date startDate = new SimpleDateFormat(hourFormatText).parse(workcalPart.getStartTime());
                Date endDate = new SimpleDateFormat(hourFormatText).parse(workcalPart.getEndTime());
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(startDate);

                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(endDate);
                dayPart.setFromHour(startCalendar.get(Calendar.HOUR));
                dayPart.setFromMinute(startCalendar.get(Calendar.MINUTE));
                dayPart.setToHour(endCalendar.get(Calendar.HOUR));
                dayPart.setToMinute(endCalendar.get(Calendar.MINUTE));
                dayParts.add(dayPart);
            }

            days.set(workcalRule.getWeek(), day);
        }
    }

    public void processHoliday(WorkcalRule workcalRule) throws Exception {
        Date date = workcalRule.getWorkDate();
        Holiday holiday = new Holiday(workCalendar);
        holiday.setDate(date);
        workCalendar.addHoliday(holiday);
    }

    public void processWorkDay(WorkcalRule workcalRule) throws Exception {
        Date date = workcalRule.getWorkDate();
        WorkDay workDay = new WorkDay(workCalendar);
        workDay.setDate(date);

        int dayPartIndex = 0;
        List<DayPart> dayParts = workDay.getDayParts();
        List<WorkcalPart> workcalParts = ApplicationContextHelper.getWorkcalPartManager().getWorkcalParts(
            String.valueOf(workcalRule.getId()));
        for (WorkcalPart workcalPart : workcalParts) {
            DayPart dayPart = new DayPart();
            dayPart.setWorkDay(workDay);
            dayPart.setIndex(dayPartIndex);

            Date startDate = new SimpleDateFormat(hourFormatText).parse(workcalPart.getStartTime());
            Date endDate = new SimpleDateFormat(hourFormatText).parse(workcalPart.getEndTime());
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            dayPart.setFromHour(startCalendar.get(Calendar.HOUR));
            dayPart.setFromMinute(startCalendar.get(Calendar.MINUTE));
            dayPart.setToHour(endCalendar.get(Calendar.HOUR));
            dayPart.setToMinute(endCalendar.get(Calendar.MINUTE));
            dayParts.add(dayPart);
        }

        workCalendar.addWorkDay(workDay);
    }

    @PostConstruct
    public void init() throws Exception {
        if (!enabled) {
            // logger.info("skip work calendar");
            return;
        }

        workCalendar = new WorkCalendar();
        this.processWeek();

        // 特殊日期
        List<WorkcalRule> extraWorkcalRules = ApplicationContextHelper.getWorkcalRuleManager()
            .getWorkcalRules(STATUS_WEEK, null, false);
        for (WorkcalRule workcalRule : extraWorkcalRules) {
            if (workcalRule.getStatus() == STATUS_HOLIDAY) {
                this.processHoliday(workcalRule);
            } else if (workcalRule.getStatus() == STATUS_HOLIDAY_TO_WORKDAY) {
                this.processWorkDay(workcalRule);
            } else {
                this.processHoliday(workcalRule);
            }
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
