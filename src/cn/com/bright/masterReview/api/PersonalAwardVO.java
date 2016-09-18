package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 个人获奖情况
 * @ClassName: PersonalAwardVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2015年12月18日 下午3:54:59
 */
public class PersonalAwardVO {
    private String id;
    private String businessKey;
    private String awardsName;
    private String awardsCompany;
    private String awardsLevel;
    private String awardsAttachmentId;
    private String awards_type;//奖项类别
    private Date awardsTime;
    private String approve_result;
    
    private AttachMentVO personalAttachVO = new AttachMentVO();
    public String getAwardsName() {
        return awardsName;
    }
    public void setAwardsName(String awardsName) {
        this.awardsName = awardsName;
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
    public Date getAwardsTime() {
        return awardsTime;
    }
    public void setAwardsTime(Date awardsTime) {
        this.awardsTime = awardsTime;
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
    public String getAwardsAttachmentId() {
        return awardsAttachmentId;
    }
    public void setAwardsAttachmentId(String awardsAttachmentId) {
        this.awardsAttachmentId = awardsAttachmentId;
    }
    public AttachMentVO getPersonalAttachVO() {
        return personalAttachVO;
    }
    public void setPersonalAttachVO(AttachMentVO personalAttachVO) {
        this.personalAttachVO = personalAttachVO;
    }
    public String getApprove_result() {
        return approve_result;
    }
    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }
    public String getAwards_type() {
        return awards_type;
    }
    public void setAwards_type(String awards_type) {
        this.awards_type = awards_type;
    }
}
