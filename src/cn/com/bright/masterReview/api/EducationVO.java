package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 学历情况,教育程度经历
 * @ClassName: EducationVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2015年12月18日 下午2:55:46
 */
public class EducationVO {
    private String id;
    private String businessKey;
    private Date startTime;
    private Date endTime;
    
    private String studySchool;
    private String studyProfession;
    
    //学历
    private String education;
    private String educationDesc;
    //学位
    private String degree;
    private String degreeDesc;
    //学习形式
    private String study_form;
    
    //教育类别
    private String education_type;
    
    private String educationAttachmentId;
    private String degreeAttachmentId;
    
    private String approve_result;
    
    private AttachMentVO educationAttachMentVO = new AttachMentVO();
    
    private AttachMentVO degreeAttachMentVO = new AttachMentVO();

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

    public String getStudySchool() {
        return studySchool;
    }

    public void setStudySchool(String studySchool) {
        this.studySchool = studySchool;
    }

    public String getStudyProfession() {
        return studyProfession;
    }

    public void setStudyProfession(String studyProfession) {
        this.studyProfession = studyProfession;
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

    public String getEducationAttachmentId() {
        return educationAttachmentId;
    }

    public void setEducationAttachmentId(String educationAttachmentId) {
        this.educationAttachmentId = educationAttachmentId;
    }

    public String getDegreeAttachmentId() {
        return degreeAttachmentId;
    }

    public void setDegreeAttachmentId(String degreeAttachmentId) {
        this.degreeAttachmentId = degreeAttachmentId;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
    
    public String getEducationDesc() {
		return educationDesc;
	}

	public void setEducationDesc(String educationDesc) {
		this.educationDesc = educationDesc;
	}

	public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
    
    public String getDegreeDesc() {
		return degreeDesc;
	}

	public void setDegreeDesc(String degreeDesc) {
		this.degreeDesc = degreeDesc;
	}

	public AttachMentVO getEducationAttachMentVO() {
        return educationAttachMentVO;
    }

    public void setEducationAttachMentVO(AttachMentVO educationAttachMentVO) {
        this.educationAttachMentVO = educationAttachMentVO;
    }

    public AttachMentVO getDegreeAttachMentVO() {
        return degreeAttachMentVO;
    }

    public void setDegreeAttachMentVO(AttachMentVO degreeAttachMentVO) {
        this.degreeAttachMentVO = degreeAttachMentVO;
    }

    public String getApprove_result() {
        return approve_result;
    }

    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }

    public String getStudy_form() {
        return study_form;
    }

    public void setStudy_form(String study_form) {
        this.study_form = study_form;
    }

    public String getEducation_type() {
        return education_type;
    }

    public void setEducation_type(String education_type) {
        this.education_type = education_type;
    }
    
    
}
