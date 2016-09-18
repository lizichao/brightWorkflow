package cn.com.bright.workflow.bpmn.listener.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.workflow.api.component.IFunctionListener;
import cn.com.bright.workflow.api.vo.DelegateUserVO;
import cn.com.bright.workflow.bpmn.cmd.EditTaskHandlerCmd;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.service.ProcessFormService;
import cn.com.bright.workflow.service.UserQueryService;
import cn.com.bright.workflow.service.WorkflowDefExtService;
import cn.com.bright.workflow.service.WorkflowLogService;
import cn.com.bright.workflow.util.WorkflowConstant;

// @Component
public class EditTaskHandlerListener implements IFunctionListener {

    @Resource
    TaskService taskService;

    @Resource
    RuntimeService runtimeService;

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
        String editAssigneeHandler = componentParamMap.get(WorkflowConstant.EDIT_ASSIGNEE_HANDLER);
        String[] editCandidateHandler = componentParamMap.get(WorkflowConstant.EDIT_CANDIDATE_HANDLER).split(",");
        // Task task =
        // taskService.createTaskQuery().taskId(taskId).singleResult();

        Set<String> editAssigneeSet = new HashSet<String>();
        editAssigneeSet.add(editAssigneeHandler);
        Map<String, DelegateUserVO> assigneeConfigMap = workflowDefExtService.findDelegateTaskList(
            new HashSet<String>(editAssigneeSet), processDefKey);
        
        DelegateUserVO assigneeDelegateUserVO = assigneeConfigMap.get(editAssigneeHandler);
        editAssigneeHandler = (null != assigneeDelegateUserVO) ? assigneeDelegateUserVO.getDelegatedUser().getUserId() : editAssigneeHandler;

        List<String> editCandidateHandlers = Arrays.asList(editCandidateHandler);
        Map<String, DelegateUserVO> configMap = workflowDefExtService.findDelegateTaskList(
            new HashSet<String>(editCandidateHandlers), processDefKey);
        List<String> resultUser = workflowDefExtService.getFinalDelegateUsers(editCandidateHandlers,configMap);

        workflowDefExtService.insertDelegateHistory(configMap, taskId, processDefKey, processDefName,WorkflowConstant.TASK_DELEGATE_EDITHANDLER);
        logEditHandler(taskId, editAssigneeHandler, assigneeConfigMap, editCandidateHandlers, configMap);
        ApplicationContextHelper.getManagementService().executeCommand(
            new EditTaskHandlerCmd(taskId, editAssigneeHandler, new HashSet<String>(resultUser)));

        processFormService.updateProcessForm(processInstanceId);
        /*
         * if(null !=task.getAssignee()){ String editAssigneeHandler =
         * componentParamMap.get(WorkflowConstant.EDIT_ASSIGNEE_HANDLER);
         * editAssigneeHandler(task,editAssigneeHandler) }else{ String[]
         * editCandidateHandler =
         * componentParamMap.get(WorkflowConstant.EDIT_CANDIDATE_HANDLER
         * ).split(","); editCandidateHandler(task,editCandidateHandler); }
         */
    }

    private void logEditHandler(String taskId, String editAssigneeHandler,Map<String, DelegateUserVO> assigneeConfigMap,
                                List<String> editCandidateHandlers, Map<String, DelegateUserVO> configMap) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        StringBuffer logOperate = new StringBuffer();

        if (StringUtil.isNotEmpty(editAssigneeHandler)) {
            String assigneeName = userQueryService.getUserVO(task.getAssignee()).getUserName();
            logOperate.append("替换处理人,把[");
            logOperate.append(assigneeName);
            logOperate.append("]替换成[");
            DelegateUserVO assigneeDelegateUserVO = assigneeConfigMap.get(assigneeName);
            String str = (null != assigneeDelegateUserVO) ? assigneeDelegateUserVO.getDelegatedUser()
                .getUserName() + "代理(" + assigneeDelegateUserVO.getUserName() + ")" : assigneeName;
            logOperate.append(str);
            logOperate.append("].");
        } else {
            List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(taskId);
            logOperate.append("替换处理人,把[");
            List<String> candidateNameList = new ArrayList<String>();
            for (IdentityLink identityLink : identityLinks) {
                if (identityLink.getType().equals(IdentityLinkType.CANDIDATE)) {
                    String candidateName = userQueryService.getUserVO(identityLink.getUserId()).getUserName();
                    candidateNameList.add(candidateName);
                    // logOperate.append(candidateName);
                    // logOperate.append(",");
                }
            }
            logOperate.append(StringUtils.collectionToDelimitedString(candidateNameList, ","));
            logOperate.append("]替换成[");
            int i = 0;
            for (String editUser : editCandidateHandlers) {
                DelegateUserVO delegateUserVO = configMap.get(editUser);
                i++;
                String editUserName = userQueryService.getUserVO(editUser).getUserName();
                if (null != delegateUserVO) {
                    logOperate.append(delegateUserVO.getDelegatedUser().getUserName() + "代理("
                        + delegateUserVO.getUserName() + ")");
                } else {
                    logOperate.append(editUserName);
                }
                if (i < editCandidateHandlers.size()) {
                    logOperate.append(",");
                }
            }
            logOperate.append("].");
        }
        workflowLogService.recordFunctionComponentLog(taskId, logOperate.toString());
    }

    // private List<String> getDelegateUsers(List<String>
    // editCandidateHandlers,Map<String, DelegateUserVO> configMap) {
    // List<String> resultUser = new ArrayList<String>();
    // for(String subTaskUser :editCandidateHandlers){
    // DelegateUserVO delegateUserVO = configMap.get(subTaskUser);
    // if(null!= delegateUserVO){
    // resultUser.add(delegateUserVO.getDelegatedUser().getUserId());
    // }else{
    // resultUser.add(subTaskUser);
    // }
    // }
    // return resultUser;
    // }

    // private void logEditHandler(String taskId, String editAssigneeHandler,
    // String[] editCandidateHandler) {
    // Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    // StringBuffer logOperate = new StringBuffer();
    //
    // if(StringUtil.isNotEmpty(editAssigneeHandler)){
    // String assigneeName =
    // userQueryService.getUserVO(task.getAssignee()).getUserName();
    // logOperate.append("替换处理人，把[");
    // logOperate.append(assigneeName);
    // logOperate.append("]替换成[");
    // logOperate.append(userQueryService.getUserVO(editAssigneeHandler).getUserName());
    // logOperate.append("].");
    // }else{
    // List<IdentityLink> identityLinks
    // =taskService.getIdentityLinksForTask(taskId);
    // logOperate.append("替换处理人，把[");
    // List<String> candidateNameList = new ArrayList<String>();
    // for(IdentityLink identityLink : identityLinks){
    // if(identityLink.getType().equals(IdentityLinkType.CANDIDATE)){
    // String candidateName =
    // userQueryService.getUserVO(identityLink.getUserId()).getUserName();
    // candidateNameList.add(candidateName);
    // //logOperate.append(candidateName);
    // //logOperate.append(",");
    // }
    // }
    // logOperate.append(StringUtils.collectionToDelimitedString(candidateNameList,","));
    // logOperate.append("]替换成[");
    // int i=0;
    // for(String editUser : editCandidateHandler){
    // i++;
    // String editUserName = userQueryService.getUserVO(editUser).getUserName();
    // logOperate.append(editUserName);
    // if(i<editCandidateHandler.length){
    // logOperate.append(",");
    // }
    // }
    // logOperate.append("].");
    // }
    // workflowLogService.recordFunctionComponentLog(taskId,logOperate.toString());
    // }
}
