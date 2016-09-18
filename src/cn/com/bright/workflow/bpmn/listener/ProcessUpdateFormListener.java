package cn.com.bright.workflow.bpmn.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.workflow.api.persistence.HistoricProcessFormEntity;
import cn.com.bright.workflow.api.persistence.HistoricProcessFormEntityManager;
import cn.com.bright.workflow.api.persistence.ProcessFormEntity;
import cn.com.bright.workflow.api.persistence.ProcessFormEntityManager;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.bpmn.support.DefaultTaskListener;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

public class ProcessUpdateFormListener extends DefaultTaskListener implements ExecutionListener {

    private static final long serialVersionUID = -4837418434904607691L;

    public void onCreate(DelegateTask delegateTask) throws Exception {
        updateProcessForm(delegateTask);
    }

    public void onComplete(DelegateTask delegateTask) throws Exception {
        updateProcessForm(delegateTask);
    }

    public void onDelete(DelegateTask delegateTask) throws Exception {
        updateProcessForm(delegateTask);
    }

    public void notify(DelegateExecution execution) throws Exception {
        String processTitle = (String) execution.getVariable(WorkflowConstant.PROCESS_TITLE);
        updateProcessForm(execution.getProcessInstanceId(), processTitle);
    }

    private void updateProcessForm(DelegateTask delegateTask) {
        String processTitle = (String) delegateTask.getVariable(WorkflowConstant.PROCESS_TITLE);
        updateProcessForm(delegateTask.getProcessInstanceId(), processTitle);
    }

    private void updateProcessForm(String processInstanceId, String processTitle) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        ProcessFormEntity processFormEntity = Context.getCommandContext()
            .getSession(ProcessFormEntityManager.class).findProcessFormById(processInstanceId);

        List<Task> tasks = getCurrentTasks(processInstanceId);
        Map<String, Set<String>> taskInfoMap = getTaskInfos(tasks);
        Set<UserVO> users = ApplicationContextHelper.getProcessQueryService().searchProcesshandlers(tasks);

        processFormEntity.setHandlers(getHandlerNames(users));
        processFormEntity.setCurrentTaskName(StringUtils.collectionToDelimitedString(taskInfoMap.get("taskName"), ","));
        processFormEntity.setState(StringUtils.collectionToDelimitedString(taskInfoMap.get("taskState"),","));
        processFormEntity.setUpdatePeople(userid);
        processFormEntity.setUpdateTime(Context.getProcessEngineConfiguration().getClock().getCurrentTime());

        if (!StringUtil.isEmpty(processTitle)) {
            processFormEntity.setTitle(processTitle);
            Context.getProcessEngineConfiguration().getRuntimeService()
                .setProcessInstanceName(processInstanceId, processTitle);
        }

        Context.getCommandContext().getSession(ProcessFormEntityManager.class)
            .updateProcessForm(processFormEntity);

        HistoricProcessFormEntity historicProcessFormEntity = Context.getCommandContext()
            .getSession(HistoricProcessFormEntityManager.class)
            .findHistoricProcessFormById(processInstanceId);

        BeanUtils.copyProperties(processFormEntity, historicProcessFormEntity);
        Context.getCommandContext().getSession(HistoricProcessFormEntityManager.class)
            .updateHistoricProcessForm(historicProcessFormEntity);
    }

    private String getHandlerNames(Set<UserVO> users) {
        Set<String> userNames = new HashSet<String>();
        for (UserVO userVO : users) {
            userNames.add(userVO.getUserName());
        }
        return StringUtils.collectionToDelimitedString(userNames, ",");
    }

    private Map<String, Set<String>> getTaskInfos(List<Task> tasks) {
        Map<String, Set<String>> taskInfoMap = new HashMap<String, Set<String>>();
        Set<String> taskState = new HashSet<String>();
        Set<String> taskName = new HashSet<String>();
        for (Task task : tasks) {
            taskState.add(task.getDescription());
            taskName.add(task.getName());
        }
        taskInfoMap.put("taskState", taskState);
        taskInfoMap.put("taskName", taskName);
        return taskInfoMap;
    }

    private List<Task> getCurrentTasks(String processInstanceId) {
        List<Task> resultTasks = new ArrayList<Task>();
        Map<String, Task> taskMap = new HashMap<String, Task>();
        List<TaskEntity> taskEntitys = Context.getCommandContext().getTaskEntityManager()
            .findTasksByProcessInstanceId(processInstanceId);
        for (TaskEntity taskEntity : taskEntitys) {
            if (!taskEntity.isDeleted()) {
                taskMap.put(taskEntity.getId(), taskEntity);
                resultTasks.add(taskEntity);
            }
        }
        List<TaskEntity> taskCacheEntitys = Context.getCommandContext().getDbSqlSession()
            .findInCache(TaskEntity.class);
        for (TaskEntity taskEntity : taskCacheEntitys) {
            if (taskEntity.getProcessInstanceId().equals(processInstanceId) && !taskEntity.isDeleted()) {
                if (!taskMap.containsKey(taskEntity.getId())) {
                    resultTasks.add(taskEntity);
                }
            }
        }
        return resultTasks;
    }

}
