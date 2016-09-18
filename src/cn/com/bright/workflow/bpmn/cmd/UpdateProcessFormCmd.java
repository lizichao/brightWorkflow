package cn.com.bright.workflow.bpmn.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.api.persistence.HistoricProcessFormEntity;
import cn.com.bright.workflow.api.persistence.HistoricProcessFormEntityManager;
import cn.com.bright.workflow.api.persistence.ProcessFormEntity;
import cn.com.bright.workflow.api.persistence.ProcessFormEntityManager;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

public class UpdateProcessFormCmd implements Command<Void> {

    protected String processInstanceId;

    public UpdateProcessFormCmd(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Void execute(CommandContext commandContext) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        ProcessFormEntity processFormEntity = Context.getCommandContext()
            .getSession(ProcessFormEntityManager.class).findProcessFormById(processInstanceId);

        List<Task> tasks = getCurrentTasks(processInstanceId);
        Map<String, Set<String>> taskInfoMap = getTaskInfos(tasks);
        Set<UserVO> users = ApplicationContextHelper.getProcessQueryService().searchProcesshandlers(tasks);
        // List<String> users =
        // ApplicationContextHelper.getProcessQueryService().searchProcessDelegateHandlers(tasks,processFormEntity.getProcessDefKey());

        // processFormEntity.setHandlers(StringUtils.collectionToDelimitedString(users,
        // ","));
        processFormEntity.setHandlers(getHandlerNames(users));
        processFormEntity.setCurrentTaskName(StringUtils.collectionToDelimitedString( taskInfoMap.get("taskName"), ","));
        processFormEntity.setState(StringUtils.collectionToDelimitedString(taskInfoMap.get("taskState"), ","));
        processFormEntity.setUpdatePeople(userid);
        processFormEntity.setUpdateTime(Context.getProcessEngineConfiguration().getClock().getCurrentTime());

        Context.getCommandContext().getSession(ProcessFormEntityManager.class).updateProcessForm(processFormEntity);

        HistoricProcessFormEntity historicProcessFormEntity = Context.getCommandContext()
            .getSession(HistoricProcessFormEntityManager.class)
            .findHistoricProcessFormById(processInstanceId);

        BeanUtils.copyProperties(processFormEntity, historicProcessFormEntity);
        Context.getCommandContext().getSession(HistoricProcessFormEntityManager.class)
            .updateHistoricProcessForm(historicProcessFormEntity);
        return null;
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
        resultTasks.addAll(Context.getCommandContext().getTaskEntityManager()
            .findTasksByProcessInstanceId(processInstanceId));
        resultTasks.addAll(Context.getCommandContext().getDbSqlSession().findInCache(TaskEntity.class));

        return resultTasks;
    }
}
