package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * 职称情况
 * @ClassName: ProfessionalTitleVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2015年12月18日 下午3:09:26
 */
public class ProfessionalTitleVO {
    
    private String id;
    private String businessKey;
    //职称名称
    private String professionaltitle_name;
    
    private AttachMentVO attachMentVO;
    
    private String approve_result;
    
    private String professionalAttachId;

    private Date obtainTime;
    
    private String obtainSchool;
    
    
    public String getProfessionaltitle_name() {
        return professionaltitle_name;
    }

    public void setProfessionaltitle_name(String professionaltitle_name) {
        this.professionaltitle_name = professionaltitle_name;
    }

    public Date getObtainTime() {
        return obtainTime;
    }

    public void setObtainTime(Date obtainTime) {
        this.obtainTime = obtainTime;
    }

    public String getObtainSchool() {
        return obtainSchool;
    }

    public void setObtainSchool(String obtainSchool) {
        this.obtainSchool = obtainSchool;
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
    
    public AttachMentVO getAttachMentVO() {
        return attachMentVO;
    }

    public void setAttachMentVO(AttachMentVO attachMentVO) {
        this.attachMentVO = attachMentVO;
    }

    public String getApprove_result() {
        return approve_result;
    }

    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }

    public String getProfessionalAttachId() {
        return professionalAttachId;
    }

    public void setProfessionalAttachId(String professionalAttachId) {
        this.professionalAttachId = professionalAttachId;
    }
    
    
}
