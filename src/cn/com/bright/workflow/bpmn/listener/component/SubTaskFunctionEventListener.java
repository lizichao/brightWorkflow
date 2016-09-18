package cn.com.bright.workflow.bpmn.listener.component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.com.bright.workflow.api.component.IFunctionListener;
import cn.com.bright.workflow.api.vo.DelegateUserVO;
import cn.com.bright.workflow.bpmn.cmd.AddSubTaskCmd;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.service.ProcessFormService;
import cn.com.bright.workflow.service.UserQueryService;
import cn.com.bright.workflow.service.WorkflowDefExtService;
import cn.com.bright.workflow.service.WorkflowLogService;
import cn.com.bright.workflow.util.WorkflowConstant;

// @Component
public class SubTaskFunctionEventListener implements IFunctionListener {
    @Resource
    WorkflowLogService workflowLogService;
    
    @Resource
    ProcessFormService processFormService;
    
    @Resource
    WorkflowDefExtService workflowDefExtService;

    @Resource
    UserQueryService userQueryService;

    public void notifyEvent(Map<String, String> componentParamMap) throws Exception {
        String taskId = componentParamMap.get("taskId");
        String processInstanceId = componentParamMap.get("processInstanceId");
        String processDefKey = componentParamMap.get("processDefKey");
        String processDefName = componentParamMap.get("processDefName");
        String subTaskUserId = componentParamMap.get(WorkflowConstant.SUB_TASK_USER_ID);
        // String subTaskUserName =
        // componentParamMap.get(WorkflowConstant.SUB_TASK_USER_NAME);
        String subTaskName = componentParamMap.get(WorkflowConstant.SUB_TASK_NAME);

        List<String> subTaskUsers = Arrays.asList(subTaskUserId.split(","));
        Map<String, DelegateUserVO> configMap = workflowDefExtService.findDelegateTaskList(
            new HashSet<String>(subTaskUsers), processDefKey);

        List<String> resultUser = workflowDefExtService.getFinalDelegateUsers(subTaskUsers, configMap);

        workflowDefExtService.insertDelegateHistory(configMap, taskId, processDefKey, processDefName,
            WorkflowConstant.TASK_DELEGATE_ADDSUBTASK);

        ApplicationContextHelper.getManagementService().executeCommand(
            new AddSubTaskCmd(taskId, resultUser, subTaskName));

        processFormService.updateProcessForm(processInstanceId);
        StringBuffer logOperate = new StringBuffer();
        logOperate.append("添加子任务,子任务名称是:[");
        logOperate.append(subTaskName);
        logOperate.append("].子任务处理人是:[");
        logOperate.append(getDelegateUserNames(subTaskUsers, configMap));
        logOperate.append("].");
        workflowLogService.recordFunctionComponentLog(taskId, logOperate.toString());
    }

    private String getDelegateUserNames(List<String> subTaskUsers, Map<String, DelegateUserVO> configMap) {
        StringBuffer resultStr = new StringBuffer("");
        int i = 0;
        for (String subTaskUser : subTaskUsers) {
            i++;
            DelegateUserVO delegateUserVO = configMap.get(subTaskUser);
            if (null != delegateUserVO) {
                resultStr.append(delegateUserVO.getDelegatedUser().getUserName() + "代理("
                    + delegateUserVO.getUserName() + ")");
            } else {
                resultStr.append(userQueryService.getUserVO(subTaskUser).getUserName());
            }
            if (i < subTaskUsers.size()) {
                resultStr.append(",");
            }
        }

        // for (Map.Entry<String, DelegateUserVO> entry : configMap.entrySet())
        // {
        // DelegateUserVO delegateUserVO = entry.getValue();//被代理人
        // UserVO userVO = delegateUserVO.getDelegatedUser();//代理人
        // if( null!=userVO){
        // resultStr.append(userVO.getUserName()+"代理("+delegateUserVO.getUserName()+")");
        // }else{
        // resultStr.append(delegateUserVO.getUserName());
        // }
        // }
        return resultStr.toString();
    }
    /*
     * private List<String> getDelegateUsers(List<String> subTaskUsers,
     * Map<String, DelegateUserVO> configMap) { List<String> resultUser = new
     * ArrayList<String>(); for(String subTaskUser :subTaskUsers){
     * DelegateUserVO delegateUserVO = configMap.get(subTaskUser); if(null!=
     * delegateUserVO){
     * resultUser.add(delegateUserVO.getDelegatedUser().getUserId()); }else{
     * resultUser.add(userQueryService.getUserVO(subTaskUser).getUserName()); }
     * } for (Map.Entry<String, DelegateUserVO> entry : configMap.entrySet()) {
     * DelegateUserVO delegateUserVO = entry.getValue();//被代理人 UserVO userVO =
     * delegateUserVO.getDelegatedUser();//代理人 if( null!=userVO){
     * resultUser.add(userVO.getUserId()); }else{
     * resultUser.add(entry.getKey()); } } return resultUser; }
     */
}
