package cn.com.bright.workflow.bpmn.listener.component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;

import cn.com.bright.workflow.api.component.IFunctionListener;
import cn.com.bright.workflow.api.vo.DelegateUserVO;
import cn.com.bright.workflow.service.ProcessFormService;
import cn.com.bright.workflow.service.UserQueryService;
import cn.com.bright.workflow.service.WorkflowDefExtService;
import cn.com.bright.workflow.service.WorkflowLogService;
import cn.com.bright.workflow.util.WorkflowConstant;

// @Component
public class AddTaskHandlerListener implements IFunctionListener {

    @Resource
    TaskService taskService;

    @Resource
    WorkflowLogService workflowLogService;
    
    @Resource
    UserQueryService userQueryService;

    @Resource
    ProcessFormService processFormService;

    @Resource
    WorkflowDefExtService workflowDefExtService;

    public void notifyEvent(Map<String, String> componentParamMap) throws Exception {
        String taskId = componentParamMap.get("taskId");
        String processInstanceId = componentParamMap.get("processInstanceId");
        String processDefKey = componentParamMap.get("processDefKey");
        String processDefName = componentParamMap.get("processDefName");
        List<String> addTaskHandlers = Arrays.asList(componentParamMap.get(
            WorkflowConstant.ADD_TASK_HANDLER).split(","));

        Map<String, DelegateUserVO> configMap = workflowDefExtService.findDelegateTaskList(new HashSet<String>(addTaskHandlers), processDefKey);
        List<String> resultUser = workflowDefExtService.getFinalDelegateUsers(addTaskHandlers, configMap);
        workflowDefExtService.insertDelegateHistory(configMap, taskId, processDefKey, processDefName,
            WorkflowConstant.TASK_DELEGATE_ADDHANDLER);

        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
        for (String addTaskHandler : resultUser) {
            if (!existsTask(addTaskHandler, identityLinks)) {
                taskService.addCandidateUser(taskId, addTaskHandler);
            }
        }

        processFormService.updateProcessForm(processInstanceId);
        logAddHandler(taskId, addTaskHandlers, configMap);
    }

    private void logAddHandler(String taskId, List<String> addTaskHandlers,
                               Map<String, DelegateUserVO> configMap) {
        StringBuffer logOperate = new StringBuffer();
        logOperate.append("新增处理人[");
        int i = 0;
        for (String addUser : addTaskHandlers) {
            DelegateUserVO delegateUserVO = configMap.get(addUser);
            i++;
            String editUserName = userQueryService.getUserVO(addUser).getUserName();
            if (null != delegateUserVO) {
                logOperate.append(delegateUserVO.getDelegatedUser().getUserName() + "代理("
                    + delegateUserVO.getUserName() + ")");
            } else {
                logOperate.append(editUserName);
            }
            if (i < addTaskHandlers.size()) {
                logOperate.append(",");
            }
        }
        logOperate.append("].");
        workflowLogService.recordFunctionComponentLog(taskId, logOperate.toString());
    }

    private boolean existsTask(String addTaskHandler, List<IdentityLink> identityLinks) {
        for (IdentityLink identityLink : identityLinks) {
            if (identityLink.getType().equals(IdentityLinkType.CANDIDATE)
                && identityLink.getUserId().equals(addTaskHandler)) {
                return true;
            }
        }
        return false;
    }
}
