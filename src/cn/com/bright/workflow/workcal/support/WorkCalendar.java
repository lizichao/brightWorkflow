package cn.com.bright.workflow.workcal.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

public class WorkCalendar {
    /** 1����60��. */
    public static final long MILLIS_OF_MINUTE = 1000L * 60;

    /** 1Сʱ60����. */
    public static final long MILLIS_OF_HOUR = 60 * MILLIS_OF_MINUTE;

    /** 1��8Сʱ. */
    public static final long HOUR_OF_DAY = 8;
    private List<WorkDay> days = new ArrayList<WorkDay>();
    private List<Holiday> holidays = new ArrayList<Holiday>();
    private List<WorkDay> workDays = new ArrayList<WorkDay>();
    private DatatypeFactory datatypeFactory;
    private boolean accurateToDay;

    /**
     * construtor.
     */
    public WorkCalendar() throws Exception {
        datatypeFactory = DatatypeFactory.newInstance();
    }

    /**
     * �������ʱ��.
     */
    public Date add(Date date, String period) throws Exception {
        return add(date, this.parsePeriod(period));
    }

    /**
     * �������ʱ��.
     */
    public Date add(Date startDate, Duration duration) {
        // �õ���Ӧ��ʱ��
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        // ��������������������շ�������µĸ����ı�
        calendar.add(Calendar.YEAR, duration.getYears());
        calendar.add(Calendar.MONTH, duration.getMonths());

        // ������Сʱ�����ӿ�����Ϊ�������и���������⴦��
        int day = duration.getDays();
        int hour = duration.getHours();
        int minute = duration.getMinutes();

        if (accurateToDay) {
            // ��ʱ��Ҫ�Զ���һ�컻���8��Сʱ����ʵ�ʼ��㹤ʱ
            hour += (day * HOUR_OF_DAY);
            day = 0;
        } else {
            Date workDate = this.findWorkDate(calendar.getTime());
            calendar.setTime(workDate);

            // Ŀǰ��û�и��õ��㷨�����Զ������ۼӣ����ж��Ƿ�����
            for (int i = 0; i < day; i++) {
                calendar.add(Calendar.DATE, 1);

                int originHour = calendar.get(Calendar.HOUR_OF_DAY);
                int originMinute = calendar.get(Calendar.MINUTE);
                // �����ǰ���ǹ����գ��ͷ��ص�ǰʱ��
                // �����ǰ��ʱ���Ѿ����ǹ������˾ͷ�������Ĺ�����
                workDate = this.findWorkDate(calendar.getTime());
                calendar.setTime(workDate);
                calendar.set(Calendar.HOUR_OF_DAY, originHour);
                calendar.set(Calendar.MINUTE, originMinute);
            }
        }

        Date targetDate = calendar.getTime();
        long millis = (hour * MILLIS_OF_HOUR) + (minute * MILLIS_OF_MINUTE);
        DayPart dayPart = this.findDayPart(targetDate);
        boolean isInbusinessHours = (dayPart != null);

        if (!isInbusinessHours) {
            DayPartResult dayPartResult = this.findTargetWorkDay(targetDate).findNextDayPartStart(0,
                targetDate);
            targetDate = dayPartResult.getDate();
            dayPart = dayPartResult.getDayPart();
        }

        Date end = dayPart.add(targetDate, millis);

        return end;
    }

    /**
     * �ѿ�ʼʱ��ת���ɹ���ʱ�䣬���統ǰʱ���Ǽ��ڣ���Ҫ������Ĺ����տ�ʼ����.
     */
    public Date findWorkDate(Date date) {
        // ���ҵ�ʱ������ʱ��Σ�����ҵ�������ֱ�ӷ��ص�ǰʱ����
        DayPart dayPart = this.findDayPart(date);

        if (dayPart != null) {
            return date;
        }

        // ����Ҳ������ӵ���ĵ�һ��ʱ��ο�ʼ����
        DayPartResult dayPartResult = this.findTargetWorkDay(date).findNextDayPartStart(0, date);

        // Object[] result = new Object[2];
        // this.findDay(date).findNextDayPartStart(0, date, result);
        // date = (Date) result[0];
        // return date;
        return dayPartResult.getDate();
    }

    /**
     * ���صڶ���.
     */
    public Date findStartOfNextDay(Date date) {
        Calendar calendar = this.cleanTime(date);

        return calendar.getTime();
    }

    /**
     * �ҵ������ʱ�������.
     */
    public WorkDay findDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int weekDayIndex = calendar.get(Calendar.DAY_OF_WEEK);

        return this.days.get(weekDayIndex);
    }

    /**
     * ��������һ�죬���ʱ������.
     */
    public Calendar cleanTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    /**
     * ��ȡʱ�������.
     */
    public DayPart findDayPart(Date date) {
        if (this.isWorkDay(date)) {
            DayPart dayPart = this.findWorkDayPart(date);

            if (dayPart != null) {
                return dayPart;
            }
        }

        if (this.isHoliday(date)) {
            return null;
        }

        WorkDay day = this.findDay(date);
        List<DayPart> dayParts = day.getDayParts();

        if (dayParts == null) {
            return null;
        }

        for (int i = 0; i < dayParts.size(); i++) {
            DayPart dayPart = dayParts.get(i);

            if (dayPart.includes(date)) {
                return dayPart;
            }
        }

        return null;
    }

    // ~ ==================================================
    public boolean isHoliday(Calendar calendar) {
        return this.isHoliday(calendar.getTime());
    }

    public boolean isHoliday(Date date) {
        if (holidays != null) {
            for (WorkDay holiday : holidays) {
                if (holiday.isSameDay(date)) {
                    return true;
                }
            }
        }

        return false;
    }

    // ~ ==================================================
    public boolean isWorkDay(Calendar calendar) {
        return this.isWorkDay(calendar.getTime());
    }

    public boolean isWorkDay(Date date) {
        return findWorkDayPart(date) != null;
    }

    public WorkDay findWorkDay(Date date) {
        for (WorkDay workDay : workDays) {
            if (workDay.isSameDay(date)) {
                return workDay;
            }
        }

        return null;
    }

    public DayPart findWorkDayPart(Date date) {
        WorkDay workDay = findWorkDay(date);

        if (workDay == null) {
            return null;
        }

        for (DayPart dayPart : workDay.getDayParts()) {
            if (dayPart.includes(date)) {
                return dayPart;
            }
        }

        return null;
    }

    public WorkDay findTargetWorkDay(Date date) {
        WorkDay workDay = this.findWorkDay(date);

        if (workDay != null) {
            return workDay;
        }

        if (this.isHoliday(date)) {
            Holiday holiday = new Holiday(this);
            holiday.setDate(date);

            return holiday;
        }

        return this.findDay(date);
    }

    // ~ ==================================================
    private Duration parsePeriod(String period) throws Exception {
        return datatypeFactory.newDuration(period);
    }

    // ~ ==================================================
    public void setDays(List<WorkDay> days) {
        this.days = days;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public void setWorkDays(List<WorkDay> workDays) {
        this.workDays = workDays;
    }

    public void addHoliday(Holiday holiday) {
        this.holidays.add(holiday);
    }

    public void addWorkDay(WorkDay workDay) {
        this.workDays.add(workDay);
    }
}
