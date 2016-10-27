package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 任职年限VO，曾经担任过什么校长
 * @ClassName: WorkExperienceVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2015年12月18日 下午3:02:41
 */
public class WorkExperienceVO {
    
    private String id;
    private String businessKey;
    
    private Date startTime;
    private Date endTime;
    private String workSchool;
    private String workProfession;
    private String workYear;
    private String proveAttachMentId;
    private String manage_level;
    private AttachMentVO proveAttachMentVO;
    
    private String approve_result;
    
 
    /**
     * 任职年限
     */
    private String workTime;
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
  
   
    public String getWorkTime() {
        return workTime;
    }
    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }
    public String getWorkSchool() {
        return workSchool;
    }
    public void setWorkSchool(String workSchool) {
        this.workSchool = workSchool;
    }
    public String getWorkProfession() {
        return workProfession;
    }
    public void setWorkProfession(String workProfession) {
        this.workProfession = workProfession;
    }
    public String getWorkYear() {
        return workYear;
    }
    public void setWorkYear(String workYear) {
        this.workYear = workYear;
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
    public String getProveAttachMentId() {
        return proveAttachMentId;
    }
    public void setProveAttachMentId(String proveAttachMentId) {
        this.proveAttachMentId = proveAttachMentId;
    }
    public AttachMentVO getProveAttachMentVO() {
        return proveAttachMentVO;
    }
    public void setProveAttachMentVO(AttachMentVO proveAttachMentVO) {
        this.proveAttachMentVO = proveAttachMentVO;
    }
    public String getApprove_result() {
        return approve_result;
    }
    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }
	public String getManage_level() {
		return manage_level;
	}
	public void setManage_level(String manage_level) {
		this.manage_level = manage_level;
	}

    
}
