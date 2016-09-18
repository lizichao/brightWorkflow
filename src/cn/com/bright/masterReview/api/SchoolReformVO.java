package cn.com.bright.masterReview.api;

import java.util.Date;

/**
 * 加分学校特色和改革
 * @ClassName: SchoolReformVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2016年6月8日 下午4:43:39
 */
 
public class SchoolReformVO {
    private String id;
    private String businessKey;
    
    //学校特色创建及改革项目名称
    private String project_name;
    //项目级别
    private String project_level;
    //时间
    private Date implement_time;
    //项目主管部门
    private String charge_department;
    //项目完成情况
    private String performance;
    
    private String approve_result;
    
    
    
    
    public String getProject_name() {
        return project_name;
    }
    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
    public String getProject_level() {
        return project_level;
    }
    public void setProject_level(String project_level) {
        this.project_level = project_level;
    }
    public Date getImplement_time() {
        return implement_time;
    }
    public void setImplement_time(Date implement_time) {
        this.implement_time = implement_time;
    }
    public String getCharge_department() {
        return charge_department;
    }
    public void setCharge_department(String charge_department) {
        this.charge_department = charge_department;
    }
    public String getPerformance() {
        return performance;
    }
    public void setPerformance(String performance) {
        this.performance = performance;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBusinessKey() {
        return businessKey;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    public String getApprove_result() {
        return approve_result;
    }
    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }
   
 
}
