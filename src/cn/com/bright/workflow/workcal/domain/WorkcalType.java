package cn.com.bright.workflow.workcal.domain;

import java.util.HashSet;
import java.util.Set;

public class WorkcalType implements java.io.Serializable {
    private static final long serialVersionUID = 0L;

    /** null. */
    private Long id;

    /** null. */
    private String name;

    /** . */
    private Set<WorkcalRule> workcalRules = new HashSet<WorkcalRule>(0);

    public WorkcalType() {
    }

    public WorkcalType(String name, Set<WorkcalRule> workcalRules) {
        this.name = name;
        this.workcalRules = workcalRules;
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
    public String getName() {
        return this.name;
    }

    /**
     * @param name null.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** @return . */
    public Set<WorkcalRule> getWorkcalRules() {
        return this.workcalRules;
    }

    /**
     * @param workcalRules .
     */
    public void setWorkcalRules(Set<WorkcalRule> workcalRules) {
        this.workcalRules = workcalRules;
    }
}
