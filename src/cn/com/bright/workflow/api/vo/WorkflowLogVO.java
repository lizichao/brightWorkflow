package cn.com.bright.workflow.api.vo;

import java.util.Date;

import org.springframework.util.StringUtils;

public class WorkflowLogVO {

    private String processInstanceId;
    private String taskId;
    private String taskDefKey;
    private String taskName;
    private String operation;
    private String remark;
    private String handlerId;
    private String handlerName;
    private String handlerOrder;
    private String handlerDepartmentId;
    private String handlerDepartmentName;
    private String handlerDepartmentOrder;
    private Date createTime;
    private String createPeople;
    private Date updateTime;
    private String updatePeople;
    private int logType;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getHandlerOrder() {
        return handlerOrder;
    }

    public void setHandlerOrder(String handlerOrder) {
        this.handlerOrder = handlerOrder;
    }

    public String getHandlerDepartmentId() {
        return handlerDepartmentId;
    }

    public void setHandlerDepartmentId(String handlerDepartmentId) {
        this.handlerDepartmentId = handlerDepartmentId;
    }

    public String getHandlerDepartmentName() {
        return handlerDepartmentName;
    }

    public void setHandlerDepartmentName(String handlerDepartmentName) {
        this.handlerDepartmentName = handlerDepartmentName;
    }

    public String getHandlerDepartmentOrder() {
        return handlerDepartmentOrder;
    }

    public void setHandlerDepartmentOrder(String handlerDepartmentOrder) {
        this.handlerDepartmentOrder = handlerDepartmentOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePeople() {
        return createPeople;
    }

    public void setCreatePeople(String createPeople) {
        this.createPeople = createPeople;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatePeople() {
        return updatePeople;
    }

    public void setUpdatePeople(String updatePeople) {
        this.updatePeople = updatePeople;
    }

    public String getTaskDefKey() {
        return (StringUtils.isEmpty(taskDefKey)) ? "" : taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

}
