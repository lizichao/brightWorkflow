package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

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
    private String project_level_desc;
    //时间
    private Date implement_time;
    //项目主管部门
    private String charge_department;
    //项目完成情况
    private String performance;
    
    private String approve_result;
    
    private String prove_attachment_id;
    
    
    private AttachMentVO proveAttachMentVO = new AttachMentVO();
    
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
    public String getProject_level_desc() {
		return project_level_desc;
	}
	public void setProject_level_desc(String project_level_desc) {
		this.project_level_desc = project_level_desc;
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
	public String getProve_attachment_id() {
		return prove_attachment_id;
	}
	public void setProve_attachment_id(String prove_attachment_id) {
		this.prove_attachment_id = prove_attachment_id;
	}
	public AttachMentVO getProveAttachMentVO() {
		return proveAttachMentVO;
	}
	public void setProveAttachMentVO(AttachMentVO proveAttachMentVO) {
		this.proveAttachMentVO = proveAttachMentVO;
	}
   
 
}
