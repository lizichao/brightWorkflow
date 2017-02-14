package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 学校获得荣誉
 * @ClassName: SchoolAwardVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2015年12月18日 下午3:55:55
 */
public class SchoolAwardVO {
    private String id;
    private String businessKey;
    private String awardsName;
    private String workSchool;
    private String awardsCompany;
    private String awardsLevel;
    private String awardsLevelDesc;
    private String awardsAttachmentId;
    private Date awardsTime;
    
    private String approve_result;
    
    private AttachMentVO schoolAttachVO = new AttachMentVO();
    public String getAwardsName() {
        return awardsName;
    }
    public void setAwardsName(String awardsName) {
        this.awardsName = awardsName;
    }
    public String getWorkSchool() {
        return workSchool;
    }
    public void setWorkSchool(String workSchool) {
        this.workSchool = workSchool;
    }
    public String getAwardsCompany() {
        return awardsCompany;
    }
    public void setAwardsCompany(String awardsCompany) {
        this.awardsCompany = awardsCompany;
    }
    public String getAwardsLevel() {
        return awardsLevel;
    }
    public void setAwardsLevel(String awardsLevel) {
        this.awardsLevel = awardsLevel;
    }
    public String getAwardsLevelDesc() {
		return awardsLevelDesc;
	}
	public void setAwardsLevelDesc(String awardsLevelDesc) {
		this.awardsLevelDesc = awardsLevelDesc;
	}
	public Date getAwardsTime() {
        return awardsTime;
    }
    public void setAwardsTime(Date awardsTime) {
        this.awardsTime = awardsTime;
    }
    public String getBusinessKey() {
        return businessKey;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAwardsAttachmentId() {
        return awardsAttachmentId;
    }
    public void setAwardsAttachmentId(String awardsAttachmentId) {
        this.awardsAttachmentId = awardsAttachmentId;
    }
    public AttachMentVO getSchoolAttachVO() {
        return schoolAttachVO;
    }
    public void setSchoolAttachVO(AttachMentVO schoolAttachVO) {
        this.schoolAttachVO = schoolAttachVO;
    }
    public String getApprove_result() {
        return approve_result;
    }
    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }
    
    
}
