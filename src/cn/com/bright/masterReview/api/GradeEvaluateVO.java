package cn.com.bright.masterReview.api;

import cn.com.bright.workflow.api.vo.AttachMentVO;


/**
 * 学校等级评估
 * @ClassName: GradeEvaluateVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2016年6月17日 下午2:26:20
 */
public class GradeEvaluateVO {
    
    private String id;
    private String businessKey;
    
    //义务教育
    private String compulsory_education;
    //高中
    private String high_school;
    //中职
    private String secondary_school;
    //特殊教育
    private String special_education;
    
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

    public String getCompulsory_education() {
        return compulsory_education;
    }

    public void setCompulsory_education(String compulsory_education) {
        this.compulsory_education = compulsory_education;
    }

    public String getHigh_school() {
        return high_school;
    }

    public void setHigh_school(String high_school) {
        this.high_school = high_school;
    }

    public String getSecondary_school() {
        return secondary_school;
    }

    public void setSecondary_school(String secondary_school) {
        this.secondary_school = secondary_school;
    }
    
    public String getSpecial_education() {
		return special_education;
	}

	public void setSpecial_education(String special_education) {
		this.special_education = special_education;
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
