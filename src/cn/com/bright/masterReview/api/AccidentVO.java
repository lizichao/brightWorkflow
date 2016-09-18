package cn.com.bright.masterReview.api;

import java.util.Date;

/**
 * 减分责任事故
 * @ClassName: AccidentVO 
 * @Description: TODO
 * @author: lzc
 * @date: 2016年6月8日 下午4:46:58
 */
 
public class AccidentVO {
    private String id;
    private String businessKey;
    //责任事故名称
    private String accident_name;
    //违纪描述
    private String description;
    //处理结果
    private String process_result;
    //时间
    private Date implement_time;
    
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

    public String getAccident_name() {
        return accident_name;
    }

    public void setAccident_name(String accident_name) {
        this.accident_name = accident_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProcess_result() {
        return process_result;
    }

    public void setProcess_result(String process_result) {
        this.process_result = process_result;
    }

    public Date getImplement_time() {
        return implement_time;
    }

    public void setImplement_time(Date implement_time) {
        this.implement_time = implement_time;
    }

    public String getApprove_result() {
        return approve_result;
    }

    public void setApprove_result(String approve_result) {
        this.approve_result = approve_result;
    }


    
}
