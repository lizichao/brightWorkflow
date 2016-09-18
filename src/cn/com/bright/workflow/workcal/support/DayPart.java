package cn.com.bright.workflow.workcal.support;

import java.util.Calendar;
import java.util.Date;

public class DayPart {
    private WorkDay workDay;
    private int index;
    private int fromHour;
    private int fromMinute;
    private int toHour;
    private int toMinute;

    public Date add(Date date, long millis) {
        Date end = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // ���㵱ǰʱ���Сʱ�ͷ���
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        long dateMilliseconds = ((hour * 60L) + minute) * 60 * 1000;
        long dayPartEndMilleseconds = ((toHour * 60L) + toMinute) * 60 * 1000;

        // ���㵱ǰdayPart�Ľ���ʱ���뵱ǰʱ��millis�Ĳ�
        long millisecondsInThisDayPart = dayPartEndMilleseconds - dateMilliseconds;

        if (millis <= millisecondsInThisDayPart) {
            // �������millis���ڵ�ǰdayPart�ֱ�ӷ���end
            end = new Date(date.getTime() + millis);
        } else {
            // ����dayPart��ʣ����millisû��
            long remainderMillis = millis - millisecondsInThisDayPart;
            Date dayPartEndDate = new Date((date.getTime() + millis) - remainderMillis);

            // �ҵ���һ������ʱ���
            DayPartResult dayPartResult = workDay.findNextDayPartStart(index + 1, dayPartEndDate);

            Date nextDayPartStart = dayPartResult.getDate();
            DayPart nextDayPart = dayPartResult.getDayPart();
            // ��������һ��ʱ��β���
            end = nextDayPart.add(nextDayPartStart, remainderMillis);
        }

        return end;
    }

    public boolean includes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return (((fromHour < hour) || ((fromHour == hour) && (fromMinute <= minute))) && ((hour < toHour) || ((hour == toHour) && (minute <= toMinute))));
    }

    public boolean isStartAfter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return ((hour < fromHour) || ((hour == fromHour) && (minute <= fromMinute)));
    }

    public Date getStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, fromHour);
        calendar.set(Calendar.MINUTE, fromMinute);

        return calendar.getTime();
    }

    public WorkDay getWorkDay() {
        return workDay;
    }

    public void setWorkDay(WorkDay workDay) {
        this.workDay = workDay;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public int getFromMinute() {
        return fromMinute;
    }

    public void setFromMinute(int fromMinute) {
        this.fromMinute = fromMinute;
    }

    public int getToHour() {
        return toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public int getToMinute() {
        return toMinute;
    }

    public void setToMinute(int toMinute) {
        this.toMinute = toMinute;
    }
}
