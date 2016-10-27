package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 减分处分
 * @ClassName: PunishmentVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2016年6月8日 下午4:48:23
 */


public class PunishmentVO {
    private String id;
    private String businessKey;
    private String description;
    
    private String people;
    
    private String department;
    private String process_result;
    
    
    private Date implement_time;
    private String approve_result;
  
    private String prove_attachment_id;
    
    
    private AttachMentVO proveAttachMentVO = new AttachMentVO();

   

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProcess_result() {
        return process_result;
    }

    public void setProcess_result(String process_result) {
        this.process_result = process_result;
    }

  

    public Date getImplement_time() {
        return implement_time;
    }

    public void setImplement_time(Date implement_time) {
        this.implement_time = implement_time;
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
