package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 进修学习VO
 * @ClassName: StudyTrainVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2016年6月8日 下午4:40:31
 */

public class StudyTrainVO {
    private String id;
    private String businessKey;
    
    private Date start_date;
    private Date end_date;

    private String title;
    private String content;
    private String class_hour;
    private String study_place;
    private String organizers;
    private String approve_result;

    private String proveAttachMentId;
    private AttachMentVO proveAttachMentVO;
    
  
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
    public Date getStart_date() {
        return start_date;
    }
    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }
    public Date getEnd_date() {
        return end_date;
    }
    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
  
	public String getClass_hour() {
		return class_hour;
	}
	public void setClass_hour(String class_hour) {
		this.class_hour = class_hour;
	}
	public String getStudy_place() {
        return study_place;
    }
    public void setStudy_place(String study_place) {
        this.study_place = study_place;
    }
    public String getOrganizers() {
        return organizers;
    }
    public void setOrganizers(String organizers) {
        this.organizers = organizers;
    }
    public String getApprove_result() {
        return approve_result;
    }
    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
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
    
}
