package cn.com.bright.workflow.workcal.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class WorkcalRule implements java.io.Serializable {
    private static final long serialVersionUID = 0L;

    /** null. */
    private Long id;

    /** null. */
    private WorkcalType workcalType;

    /** null. */
    private Integer year;

    /** null. */
    private Integer week;

    /** null. */
    private String name;

    /** null. */
    private Date workDate;

    /** null. */
    private Integer status;

    /** . */
    private Set<WorkcalPart> workcalParts = new HashSet<WorkcalPart>(0);

    public WorkcalRule() {
    }

    public WorkcalRule(WorkcalType workcalType, Integer year, Integer week, String name, Date workDate,
        Integer status, Set<WorkcalPart> workcalParts) {
        this.workcalType = workcalType;
        this.year = year;
        this.week = week;
        this.name = name;
        this.workDate = workDate;
        this.status = status;
        this.workcalParts = workcalParts;
    }

    /** @return null. */
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
    public WorkcalType getWorkcalType() {
        return this.workcalType;
    }

    /**
     * @param workcalType null.
     */
    public void setWorkcalType(WorkcalType workcalType) {
        this.workcalType = workcalType;
    }

    /** @return null. */
    public Integer getYear() {
        return this.year;
    }

    /**
     * @param year null.
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /** @return null. */
    public Integer getWeek() {
        return this.week;
    }

    /**
     * @param week null.
     */
    public void setWeek(Integer week) {
        this.week = week;
    }

    /** @return null. */
    public String getName() {
        return this.name;
    }

    /**
     * @param name null.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** @return null. */
    public Date getWorkDate() {
        return this.workDate;
    }

    /**
     * @param workDate null.
     */
    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    /** @return null. */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * @param status null.
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /** @return . */
    public Set<WorkcalPart> getWorkcalParts() {
        return this.workcalParts;
    }

    /**
     * @param workcalParts .
     */
    public void setWorkcalParts(Set<WorkcalPart> workcalParts) {
        this.workcalParts = workcalParts;
    }
}
