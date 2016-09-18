package cn.com.bright.workflow.api.vo;

import java.util.Date;

public class DelegateConfigureVO {
	private String originalUser;
	private String delegateUser;
	private Date startTime;
	private Date endTime;
	private String processDefKey;
	private String processDefName;

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
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
}
