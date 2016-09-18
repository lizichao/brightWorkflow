package cn.com.bright.workflow.api.vo;

/*
 * Œﬁ”√
 */
public class DelegateTaskVO {
	private String originalUser;
	private String delegateUser;
	private String delegateUserName;
	private String taskId;
	private String processInstanceId;
	private String taskDefKey;
	private String processDefKey;
	private String processDefName;
	private String delegateType;

	public String getOriginalUser() {
		return originalUser;
	}
	public void setOriginalUser(String originalUser) {
		this.originalUser = originalUser;
	}
	public String getDelegateUser() {
		return delegateUser;
	}
	public void setDelegateUser(String delegateUser) {
		this.delegateUser = delegateUser;
	}
	public String getDelegateUserName() {
		return delegateUserName;
	}
	public void setDelegateUserName(String delegateUserName) {
		this.delegateUserName = delegateUserName;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getTaskDefKey() {
		return taskDefKey;
	}
	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}
	public String getProcessDefKey() {
		return processDefKey;
	}
	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}
	public String getProcessDefName() {
		return processDefName;
	}
	public void setProcessDefName(String processDefName) {
		this.processDefName = processDefName;
	}
	public String getDelegateType() {
		return delegateType;
	}
	public void setDelegateType(String delegateType) {
		this.delegateType = delegateType;
	}
}
