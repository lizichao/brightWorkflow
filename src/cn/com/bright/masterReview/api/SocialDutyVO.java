package cn.com.bright.masterReview.api;

import java.util.Date;

import cn.com.bright.workflow.api.vo.AttachMentVO;

/**
 * �ӷ��������
 * @ClassName: SocialDutyVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2016��6��8�� ����4:44:55
 */

public class SocialDutyVO {
    private String id;
    private String businessKey;
    //�е��ϼ����Ű��ŵ�������ι���
    private String superior_task;
    //�������Ų���
    private String arrange_department;
    //ʱ��
    private Date implement_time;
    //������
    private String complete_state;
    
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
    public String getSuperior_task() {
        return superior_task;
    }
    public void setSuperior_task(String superior_task) {
        this.superior_task = superior_task;
    }
    public String getArrange_department() {
        return arrange_department;
    }
    public void setArrange_department(String arrange_department) {
        this.arrange_department = arrange_department;
    }
    public Date getImplement_time() {
        return implement_time;
    }
    public void setImplement_time(Date implement_time) {
        this.implement_time = implement_time;
    }
    public String getComplete_state() {
        return complete_state;
    }
    public void setComplete_state(String complete_state) {
        this.complete_state = complete_state;
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
