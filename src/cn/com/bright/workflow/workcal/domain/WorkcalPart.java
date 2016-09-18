package cn.com.bright.workflow.workcal.domain;

public class WorkcalPart implements java.io.Serializable {
    private static final long serialVersionUID = 0L;

    /** null. */
    private Long id;

    /** null. */
    private WorkcalRule workcalRule;

    /** null. */
    private Integer shift;

    /** null. */
    private String startTime;

    /** null. */
    private String endTime;

    public WorkcalPart() {
    }

    public WorkcalPart(WorkcalRule workcalRule, Integer shift, String startTime, String endTime) {
        this.workcalRule = workcalRule;
        this.shift = shift;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return this.id;
    }

    /**
     * @param id null.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return null. */
    public WorkcalRule getWorkcalRule() {
        return this.workcalRule;
    }

    /**
     * @param workcalRule null.
     */
    public void setWorkcalRule(WorkcalRule workcalRule) {
        this.workcalRule = workcalRule;
    }

    /** @return null. */
    public Integer getShift() {
        return this.shift;
    }

    /**
     * @param shift null.
     */
    public void setShift(Integer shift) {
        this.shift = shift;
    }

    /** @return null. */
    public String getStartTime() {
        return this.startTime;
    }

    /**
     * @param startTime null.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /** @return null. */
    public String getEndTime() {
        return this.endTime;
    }

    /**
     * @param endTime null.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
