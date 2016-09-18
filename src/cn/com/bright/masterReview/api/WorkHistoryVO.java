package cn.com.bright.masterReview.api;

import java.util.Date;

/**
 * 工作经历
 * @ClassName: WorkHistoryVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2016年6月17日 下午2:20:10
 */
public class WorkHistoryVO {
    
    private String id;
    private String businessKey;
    
    private Date start_date;
    private Date end_date;
    //证明人
    private String prove_people;
    //工作单位
    private String work_company;
    
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

    public String getProve_people() {
        return prove_people;
    }

    public void setProve_people(String prove_people) {
        this.prove_people = prove_people;
    }

    public String getApprove_result() {
        return approve_result;
    }

    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }

    public String getWork_company() {
        return work_company;
    }

    public void setWork_company(String work_company) {
        this.work_company = work_company;
    }
    
    
}
