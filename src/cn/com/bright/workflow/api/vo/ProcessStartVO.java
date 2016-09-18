package cn.com.bright.workflow.api.vo;

public class ProcessStartVO extends ProcessOperateVO {

    private String processInstanceName;

    public ProcessStartVO() {
        // TODO Auto-generated constructor stub
    }

    public ProcessStartVO(String processDefinitionId, String processDefinitionKey,
        String processDefinitionName, String currentUserId) {
        super(processDefinitionId, processDefinitionKey, processDefinitionName, currentUserId);
        // TODO Auto-generated constructor stub
    }

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public void setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
    }
}
