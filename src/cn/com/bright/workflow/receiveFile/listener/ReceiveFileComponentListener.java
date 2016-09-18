package cn.com.bright.workflow.receiveFile.listener;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.com.bright.workflow.api.component.IFunctionNotifyListener;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.receiveFile.cmd.ReceiveFileTransferCmd;
import cn.com.bright.workflow.util.WorkflowConstant;

@Component
public class ReceiveFileComponentListener implements IFunctionNotifyListener {

    public void notifyBeforeEvent(String componentName, Map<String, String> componentParamMap)
        throws Exception {
    }

    public void notifyAfterEvent(String componentName, Map<String, String> componentParamMap)
        throws Exception {
        if (componentName.equals("transfer")) {
            String processInstanceId = componentParamMap.get("processInstanceId");
            String processDefKey = componentParamMap.get("processDefKey");
            String currentUserId = componentParamMap.get(WorkflowConstant.CURRENT_USERID);
            String transferUserId = componentParamMap.get(WorkflowConstant.TRANSFER_USERID);// 原转审人
            String taskDefKey = componentParamMap.get("taskDefKey");// 原转审人

            Map<String, String> delegate = ApplicationContextHelper.getWorkflowDefExtService()
                .findDelegatetaskConfig(transferUserId, processDefKey);
            if (!StringUtils.isEmpty(delegate.get(transferUserId))) {
                transferUserId = delegate.get(transferUserId);
            }
            if (taskDefKey.equals("usertask7")) {
                ApplicationContextHelper.getManagementService()
                    .executeCommand(new ReceiveFileTransferCmd(processInstanceId, currentUserId, transferUserId,taskDefKey));
            }
        }
    }
}
