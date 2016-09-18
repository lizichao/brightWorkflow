package cn.com.bright.workflow.receiveFile.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Component;

import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

@Component
public class PartDealCompleteListener {

    public void notify(DelegateTask task) throws Exception {
        String taskMultiDepartment = (String) task.getVariableLocal(WorkflowConstant.TASK_MULTI_DEPARTMENT);
        String businessKey = task.getExecution().getProcessBusinessKey();

        boolean isLastDept = true;
        String processInstanceId = task.getProcessInstanceId();
        List<Task> tasks = getCurrentTasks(processInstanceId, task.getId());
        for (Task taskEach : tasks) {
            TaskEntity taskEntity = (TaskEntity) taskEach;
            String taskMultiDepartmentEach = (String) taskEntity
                .getVariableLocal(WorkflowConstant.TASK_MULTI_DEPARTMENT);
            if (taskMultiDepartment.equals(taskMultiDepartmentEach)) {
                isLastDept = false;
                break;
            }
        }
        if (isLastDept) {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE workflow_receivefile_deal");
            sql.append("  SET return_time = ?,");
            sql.append("   isOnLine = '0'");
            sql.append("WHERE businessKey = ? AND deptid = ?");
            ApplicationContextHelper.getJdbcTemplate().update(sql.toString(), new Date(), businessKey,
                taskMultiDepartment);
        }
    }

    private List<Task> getCurrentTasks(String processInstanceId, String taskId) {
        List<Task> resultTasks = new ArrayList<Task>();
        Map<String, Task> taskMap = new HashMap<String, Task>();
        List<TaskEntity> taskEntitys = Context.getCommandContext().getTaskEntityManager()
            .findTasksByProcessInstanceId(processInstanceId);
        for (TaskEntity taskEntity : taskEntitys) {
            if (!taskEntity.isDeleted() && !taskEntity.getId().equals(taskId)) {
                taskMap.put(taskEntity.getId(), taskEntity);
                resultTasks.add(taskEntity);
            }
        }
        List<TaskEntity> taskCacheEntitys = Context.getCommandContext().getDbSqlSession().findInCache(TaskEntity.class);
        for (TaskEntity taskEntity : taskCacheEntitys) {
            if (taskEntity.getProcessInstanceId().equals(processInstanceId) && !taskEntity.isDeleted()
                && !taskEntity.getId().equals(taskId)) {
                if (!taskMap.containsKey(taskEntity.getId())) {
                    resultTasks.add(taskEntity);
                }
            }
        }
        return resultTasks;
    }
}
