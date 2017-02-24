package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 课题情况
 * @ClassName: SubjectVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2015年12月18日 下午4:48:45
 */
public class SubjectVO {
    private String id;
    private String businessKey;
    private String subjectName;
    
    private String subjectCompany;
    
    private String subjectLevel;
    private String subjectLevelDesc;
    private String subjectRresponsibility;
    
    private String finishResult;
    
    private Date projectTime;//立项时间
    private Date finishTime;
    
    private String isfinishSubject;
    private String isfinishSubjectDesc;
    private String subjectAttachmentId;
    
    private String approve_result;
    
    private AttachMentVO subjectAttachVO = new AttachMentVO();

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCompany() {
        return subjectCompany;
    }

    public void setSubjectCompany(String subjectCompany) {
        this.subjectCompany = subjectCompany;
    }

    public String getSubjectLevel() {
        return subjectLevel;
    }

    public void setSubjectLevel(String subjectLevel) {
        this.subjectLevel = subjectLevel;
    }
    
    public String getSubjectLevelDesc() {
		return subjectLevelDesc;
	}

	public void setSubjectLevelDesc(String subjectLevelDesc) {
		this.subjectLevelDesc = subjectLevelDesc;
	}

	public String getSubjectRresponsibility() {
        return subjectRresponsibility;
    }

    public void setSubjectRresponsibility(String subjectRresponsibility) {
        this.subjectRresponsibility = subjectRresponsibility;
    }

    public String getFinishResult() {
        return finishResult;
    }

    public void setFinishResult(String finishResult) {
        this.finishResult = finishResult;
    }
    
    public Date getProjectTime() {
		return projectTime;
	}

	public void setProjectTime(Date projectTime) {
		this.projectTime = projectTime;
	}

	public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getIsfinishSubject() {
        return isfinishSubject;
    }
    
    public String getIsfinishSubjectDesc() {
		return isfinishSubjectDesc;
	}

	public void setIsfinishSubjectDesc(String isfinishSubjectDesc) {
		this.isfinishSubjectDesc = isfinishSubjectDesc;
	}

	public void setIsfinishSubject(String isfinishSubject) {
        this.isfinishSubject = isfinishSubject;
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

    public String getSubjectAttachmentId() {
        return subjectAttachmentId;
    }

    public void setSubjectAttachmentId(String subjectAttachmentId) {
        this.subjectAttachmentId = subjectAttachmentId;
    }

    public AttachMentVO getSubjectAttachVO() {
        return subjectAttachVO;
    }

    public void setSubjectAttachVO(AttachMentVO subjectAttachVO) {
        this.subjectAttachVO = subjectAttachVO;
    }

    public String getApprove_result() {
        return approve_result;
    }

    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }
    
}
