package cn.com.bright.workflow.bpmn.listener.component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;

import cn.com.bright.workflow.api.component.IFunctionListener;
import cn.com.bright.workflow.api.vo.DelegateUserVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.exception.TransferMySelfException;
import cn.com.bright.workflow.service.ProcessFormService;
import cn.com.bright.workflow.service.TaskServiceImplExt;
import cn.com.bright.workflow.service.UserQueryService;
import cn.com.bright.workflow.service.WorkflowDefExtService;
import cn.com.bright.workflow.service.WorkflowLogService;
import cn.com.bright.workflow.util.WorkflowConstant;

// @Component
public class TransferFunctionEventListener implements IFunctionListener {
    // private static TransferFunctionEventListener instance = new
    // TransferFunctionEventListener();

    @Resource
    TaskServiceImplExt taskServiceImplExt;

    @Resource
    WorkflowLogService workflowLogService;

    @Resource
    UserQueryService userQueryService;

    @Resource
    ProcessFormService processFormService;

    @Resource
    WorkflowDefExtService workflowDefExtService;

    // private TransferFunctionEventListener() {
    // }

    // public static TransferFunctionEventListener getInstance() {
    // return instance;
    // }

    public void notifyEvent(Map<String, String> componentParamMap) throws Exception {
        String taskId = componentParamMap.get("taskId");
        String processInstanceId = componentParamMap.get("processInstanceId");
        String processDefKey = componentParamMap.get("processDefKey");
        String processDefName = componentParamMap.get("processDefName");

        String currentUserId = componentParamMap.get(WorkflowConstant.CURRENT_USERID);
        String transferUserId = componentParamMap.get(WorkflowConstant.TRANSFER_USERID);// 原转审人
        String transferUserName = componentParamMap.get(WorkflowConstant.TRANSFER_USERNAME);// 原转审人名称

        if (currentUserId.equals(transferUserId)) {
            throw new TransferMySelfException("不能转审给自己！");
        }
        String delegateTransferUserId = "";
        Set<String> transferUserSet = new HashSet<String>();
        transferUserSet.add(transferUserId);
        Map<String, DelegateUserVO> configMap = workflowDefExtService.findDelegateTaskList(transferUserSet,
            processDefKey);
        DelegateUserVO delegateUserVO = configMap.get(transferUserId);
        if (null != delegateUserVO) {
            delegateTransferUserId = delegateUserVO.getDelegatedUser().getUserId();
        } else {
            delegateTransferUserId = transferUserId;
        }

        // String delegateTransferUserId =
        // !StringUtils.isEmpty(configMap.get(transferUserId))?
        // delegateTaskMap.get(transferUserId) : transferUserId;

        workflowDefExtService.insertDelegateHistory(configMap, taskId, processDefKey, processDefName,
            WorkflowConstant.TASK_DELEGATE_TRANSFER);

        taskServiceImplExt.delegateTaskExt(taskId, currentUserId, delegateTransferUserId);

        processFormService.updateProcessForm(processInstanceId);

        String transferStr = "";
        if (!CollectionUtils.isEmpty(configMap)) {
            UserVO transferUserVO = userQueryService.getUserVO(delegateTransferUserId);
            transferStr = transferUserVO.getUserName() + "代理(" + transferUserName + ")";
        } else {
            transferStr = transferUserName;
        }

        StringBuffer logOperate = new StringBuffer();
        logOperate.append("转审任务给[");
        logOperate.append(transferStr);
        logOperate.append("].");
        workflowLogService.recordFunctionComponentLog(taskId, logOperate.toString());
    }
}
