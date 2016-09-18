package cn.com.bright.workflow.api.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseFormVO {

	private String processDefinitionId;
	private String processDefinitionKey;
	private String processDefinitionName;
	private String formKey;

	private String processInstanceId;
	private String processBusinessKey;
	private String processTitle;
	private Set<UserVO> processHandlers;
	private UserVO processCreator;
	private Date processCreateTime;
	private Date processEndTime;
	private List<WorkflowLogVO> approveLogs;

	private List<ComponentVO> components;

	private List<FlowVO> flows;

	// 当前用户是否是会签节点的发起人
	private boolean isMultiCreator;
	// 当前用户有权限操作的会签节点集合，就是他发起的会签节点
	private List<UserTaskConfigVO> multiUserTasks;

	// 当前节点是否是会签节点
	private boolean isMultiUserTask;

	// 当前用户是否是会签节点的监控人
	private boolean isMonitor;

	private Map<UserVO, UserVO> processDelegateHandlers;

	private List<DelegateUserVO> delegateUserVOs;

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessBusinessKey() {
		return processBusinessKey;
	}

	public void setProcessBusinessKey(String processBusinessKey) {
		this.processBusinessKey = processBusinessKey;
	}

	public String getProcessTitle() {
		return processTitle;
	}

	public void setProcessTitle(String processTitle) {
		this.processTitle = processTitle;
	}

	public Set<UserVO> getProcessHandlers() {
		return processHandlers;
	}

	public void setProcessHandlers(Set<UserVO> processHandlers) {
		this.processHandlers = processHandlers;
	}

	public UserVO getProcessCreator() {
		return processCreator;
	}

	public void setProcessCreator(UserVO processCreator) {
		this.processCreator = processCreator;
	}

	public Date getProcessCreateTime() {
		return processCreateTime;
	}

	public void setProcessCreateTime(Date processCreateTime) {
		this.processCreateTime = processCreateTime;
	}

	public List<WorkflowLogVO> getApproveLogs() {
		return approveLogs;
	}

	public void setApproveLogs(List<WorkflowLogVO> approveLogs) {
		this.approveLogs = approveLogs;
	}

	public boolean isMultiCreator() {
		return isMultiCreator;
	}

	public void setMultiCreator(boolean isMultiCreator) {
		this.isMultiCreator = isMultiCreator;
	}

	public List<UserTaskConfigVO> getMultiUserTasks() {
		return multiUserTasks;
	}

	public void setMultiUserTasks(List<UserTaskConfigVO> multiUserTasks) {
		this.multiUserTasks = multiUserTasks;
	}

	public List<ComponentVO> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentVO> components) {
		this.components = components;
	}

	public boolean isMultiUserTask() {
		return isMultiUserTask;
	}

	public void setMultiUserTask(boolean isMultiUserTask) {
		this.isMultiUserTask = isMultiUserTask;
	}

	public Date getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTime(Date processEndTime) {
		this.processEndTime = processEndTime;
	}

	public List<FlowVO> getFlows() {
		return flows;
	}

	public void setFlows(List<FlowVO> flows) {
		this.flows = flows;
	}

	public boolean isMonitor() {
		return isMonitor;
	}

	public void setMonitor(boolean isMonitor) {
		this.isMonitor = isMonitor;
	}

	public Map<UserVO, UserVO> getProcessDelegateHandlers() {
		return processDelegateHandlers;
	}

	public void setProcessDelegateHandlers(
			Map<UserVO, UserVO> processDelegateHandlers) {
		this.processDelegateHandlers = processDelegateHandlers;
	}

	public List<DelegateUserVO> getDelegateUserVOs() {
		return delegateUserVOs;
	}

	public void setDelegateUserVOs(List<DelegateUserVO> delegateUserVOs) {
		this.delegateUserVOs = delegateUserVOs;
	}
}
