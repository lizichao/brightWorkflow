package cn.com.bright.workflow.workcal.support;

public class Holiday extends WorkDay {
    public Holiday(WorkCalendar workCalendar) {
        super(workCalendar);
    }

    public boolean isHoliday() {
        return true;
    }
}
