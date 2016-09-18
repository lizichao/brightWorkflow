package cn.com.bright.workflow.api.vo;

import java.util.Map;

public class CounterSignVO {
	private String processInstanceId;

	private String counterSignOperate;

	private int multiKind;

	private String collectionVariable;
	private String collectionElementVariable;

	private String activityId;

	private String taskId;

	private String processDefKey;
	private String processDefName;

	private String multiInstance;

	private String processDefinitionId;

	private Map<String, DelegateUserVO> addDelegateMap;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getProcessDefKey() {
		return processDefKey;
	}

	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getCounterSignOperate() {
		return counterSignOperate;
	}

	public void setCounterSignOperate(String counterSignOperate) {
		this.counterSignOperate = counterSignOperate;
	}

	public String getCollectionVariable() {
		return collectionVariable;
	}

	public void setCollectionVariable(String collectionVariable) {
		this.collectionVariable = collectionVariable;
	}

	public String getCollectionElementVariable() {
		return collectionElementVariable;
	}

	public void setCollectionElementVariable(String collectionElementVariable) {
		this.collectionElementVariable = collectionElementVariable;
	}

	public int getMultiKind() {
		return multiKind;
	}

	public void setMultiKind(int multiKind) {
		this.multiKind = multiKind;
	}

	public String getMultiInstance() {
		return multiInstance;
	}

	public void setMultiInstance(String multiInstance) {
		this.multiInstance = multiInstance;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Map<String, DelegateUserVO> getAddDelegateMap() {
		return addDelegateMap;
	}

	public void setAddDelegateMap(Map<String, DelegateUserVO> addDelegateMap) {
		this.addDelegateMap = addDelegateMap;
	}

	public String getProcessDefName() {
		return processDefName;
	}

	public void setProcessDefName(String processDefName) {
		this.processDefName = processDefName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
