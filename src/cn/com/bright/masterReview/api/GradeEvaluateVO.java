package cn.com.bright.masterReview.api;


/**
 * ѧУ�ȼ�����
 * @ClassName: GradeEvaluateVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2016��6��17�� ����2:26:20
 */
public class GradeEvaluateVO {
    
    private String id;
    private String businessKey;
    
    //�������
    private String compulsory_education;
    //����
    private String high_school;
    //��ְ
    private String secondary_school;
    
    private String approve_result;

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

    public String getApprove_result() {
        return approve_result;
    }

    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }
}
