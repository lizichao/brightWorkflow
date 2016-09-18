package cn.com.bright.workflow.bpmn.cmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;

import cn.brightcom.jraf.util.Log;
import cn.com.bright.workflow.api.vo.DelegateUserVO;

public class CounterSignCmd implements Command<Object> {

    private Log log4j = new Log(this.getClass().toString());
    private String operateType;
    private String activityId;
    private Set<String> assignees;
    private String processInstanceId;
    private String collectionVariableName;
    private String collectionElementVariableName;
    private CommandContext commandContext;
    private String taskId;
    private Map<String, DelegateUserVO> addDelegateMap;

    public CounterSignCmd(String operateType, Set<String> assignees, String taskId) {
        this.operateType = operateType;
        this.assignees = assignees;
        this.taskId = taskId;
    }

    /**
     * @param operateType �������� add or remove
     * @param activityId �ڵ�ID
     * @param assignee ��Ա����
     * @param processInstanceId ����ʵ��ID
     * @param collectionVariableName collection ���õı�����
     * @param collectionElementVariableName collection ��ÿ��Ԫ�ر�����
     */
    public CounterSignCmd(final String operateType, final String activityId, final Set<String> assignees,
        final String processInstanceId, final String collectionVariableName,
        final String collectionElementVariableName) {
        this.operateType = operateType;
        this.activityId = activityId;
        this.assignees = assignees;
        this.processInstanceId = processInstanceId;
        this.collectionVariableName = collectionVariableName;
        this.collectionElementVariableName = collectionElementVariableName;
        // this.addDelegateMap = addDelegateMap;
    }

    public Object execute(CommandContext commandContext) {
        this.commandContext = commandContext;

        if (this.taskId != null) {
            TaskEntity taskEntity = commandContext.getTaskEntityManager().findTaskById(taskId);
            activityId = taskEntity.getExecution().getActivityId();
            processInstanceId = taskEntity.getProcessInstanceId();
            this.collectionVariableName = "countersignUsers";
            this.collectionElementVariableName = "countersignUser";
        }

        if (operateType.equalsIgnoreCase("add")) {
            addInstance();
        } else if (operateType.equalsIgnoreCase("remove")) {
            removeInstance();
        }

        return null;
    }

    /**
     * <li>��ǩ
     */
    public void addInstance() {
        if (isParallel()) {
            addParallelInstance();
        } else {
            addSequentialInstance();
        }
    }

    /**
     * <li>��ǩ
     */
    public void removeInstance() {
        if (isParallel()) {
            removeParallelInstance();
        } else {
            removeSequentialInstance();
        }
    }

    /**
     * <li>���һ������ʵ��
     */
    private void addParallelInstance() {
        ExecutionEntity parentExecutionEntity = commandContext.getExecutionEntityManager()
            .findExecutionById(processInstanceId).findExecution(activityId);
        ActivityImpl activity = getActivity();
        for (String assignee : assignees) {
            ExecutionEntity execution = parentExecutionEntity.createExecution();
            execution.setActive(true);
            execution.setConcurrent(true);
            execution.setScope(false);

            if (getActivity().getProperty("type").equals("subProcess")) {
                ExecutionEntity extraScopedExecution = execution.createExecution();
                extraScopedExecution.setActive(true);
                extraScopedExecution.setConcurrent(false);
                extraScopedExecution.setScope(true);
                execution = extraScopedExecution;
            }

            setLoopVariable(parentExecutionEntity, "nrOfInstances",
                (Integer) parentExecutionEntity.getVariableLocal("nrOfInstances") + 1);
            setLoopVariable(parentExecutionEntity, "nrOfActiveInstances",
                (Integer) parentExecutionEntity.getVariableLocal("nrOfActiveInstances") + 1);
            setLoopVariable(execution, "loopCounter", parentExecutionEntity.getExecutions().size() + 1);
            setLoopVariable(execution, collectionElementVariableName, assignee);
            // setLoopVariable(execution, "addParallel_"+activity.getId(),
            // addDelegateMap);
            execution.executeActivity(activity);
        }
    }

    /**
     * <li>������ʵ�����������һ��������
     */
    private void addSequentialInstance() {
        ExecutionEntity execution = getActivieExecutions().get(0);

        if (getActivity().getProperty("type").equals("subProcess")) {
            if (!execution.isActive() && execution.isEnded()
                && ((execution.getExecutions() == null) || (execution.getExecutions().size() == 0))) {
                execution.setActive(true);
            }
        }

        Collection<String> col = (Collection<String>) execution.getVariable(collectionVariableName);
        for (String assignee : assignees) {
            col.add(assignee);
        }
        execution.setVariable(collectionVariableName, col);
        setLoopVariable(execution, "nrOfInstances", (Integer) execution.getVariableLocal("nrOfInstances")
            + assignees.size());
    }

    /**
     * <li>�Ƴ�һ������ʵ��
     */
    private void removeParallelInstance() {
        List<ExecutionEntity> executions = getActivieExecutions();
        int removeTimes = 0;
        for (ExecutionEntity executionEntity : executions) {
            String executionVariableAssignee = (String) executionEntity
                .getVariableLocal(collectionElementVariableName);

            if ((executionVariableAssignee != null) && assignees.contains(executionVariableAssignee)) {
                removeTimes++;
                executionEntity.remove();

                ExecutionEntity parentConcurrentExecution = executionEntity.getParent();

                if (getActivity().getProperty("type").equals("subProcess")) {
                    parentConcurrentExecution = parentConcurrentExecution.getParent();
                }

                setLoopVariable(parentConcurrentExecution, "nrOfInstances",
                    (Integer) parentConcurrentExecution.getVariableLocal("nrOfInstances") - 1);
                setLoopVariable(parentConcurrentExecution, "nrOfActiveInstances",
                    (Integer) parentConcurrentExecution.getVariableLocal("nrOfActiveInstances") - 1);
                if (assignees.size() == removeTimes) {
                    break;
                }
            }
        }
    }

    /**
     * <li>�崮���б����Ƴ�δ��ɵ��û�(��ǰִ�е��û��޷��Ƴ�)
     */
    private void removeSequentialInstance() {
        ExecutionEntity executionEntity = getActivieExecutions().get(0);
        Collection<String> col = (Collection<String>) executionEntity.getVariable(collectionVariableName);
        log4j.logInfo("�Ƴ�ǰ�����б� : " + col.toString());
        for (String assignee : assignees) {
            col.remove(assignee);
            // �������Ҫɾ�������ǵ�ǰactiveִ��,
            if (executionEntity.getVariableLocal(collectionElementVariableName).equals(assignee)) {
                throw new ActivitiException("��ǰ����ִ�е�ʵ��,�޷��Ƴ�!");
            }
        }

        executionEntity.setVariable(collectionVariableName, col);
        setLoopVariable(executionEntity, "nrOfInstances",
            (Integer) executionEntity.getVariableLocal("nrOfInstances") - assignees.size());

        log4j.logInfo("�Ƴ��������б� : " + col.toString());
    }

    /**
     * <li>��ȡ���ִ�� , �����̵Ļִ�����亢��ִ��(���ж�ʵ�������) <li>��������»�ȡ�Ľ������Ϊ1
     */
    protected List<ExecutionEntity> getActivieExecutions() {
        List<ExecutionEntity> activeExecutions = new ArrayList<ExecutionEntity>();
        ActivityImpl activity = getActivity();
        List<ExecutionEntity> executions = getChildExecutionByProcessInstanceId();

        for (ExecutionEntity execution : executions) {
            if (execution.isActive()
                && (execution.getActivityId().equals(activityId) || activity.contains(execution
                    .getActivity()))) {
                activeExecutions.add(execution);
            }
        }

        return activeExecutions;
    }

    /**
     * <li>��ȡ����ʵ������������ִ��
     */
    protected List<ExecutionEntity> getChildExecutionByProcessInstanceId() {
        return commandContext.getExecutionEntityManager().findChildExecutionsByProcessInstanceId(
            processInstanceId);
    }

    /**
     * <li>���ص�ǰ�ڵ����
     */
    protected ActivityImpl getActivity() {
        return this.getProcessDefinition().findActivity(activityId);
    }

    /**
     * <li>�жϽڵ��ʵ�������Ƿ��ǲ���
     */
    protected boolean isParallel() {
        return getActivity().getProperty("multiInstance").equals("parallel");
    }

    /**
     * <li>�������̶������
     */
    protected ProcessDefinitionImpl getProcessDefinition() {
        return this.getProcessInstanceEntity().getProcessDefinition();
    }

    /**
     * <li>��������ʵ���ĸ�ִ�ж���
     */
    protected ExecutionEntity getProcessInstanceEntity() {
        return commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
    }

    /**
     * <li>��ӱ��ر���
     */
    protected void setLoopVariable(ActivityExecution execution, String variableName, Object value) {
        execution.setVariableLocal(variableName, value);
    }

}
