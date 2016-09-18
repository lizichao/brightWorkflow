package cn.com.bright.workflow.receiveFile.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import cn.com.bright.workflow.util.WorkflowConstant;

public class ReceiveFileTransferCmd implements Command<Object> {
    private static final long serialVersionUID = 1L;
    
    protected String processInstanceId;
    protected String currentUserId;
    protected String transferUserId;
    protected String currentTaskKey;

    public ReceiveFileTransferCmd(String processInstanceId, String currentUserId, String transferUserId,
        String currentTaskKey) {
        this.processInstanceId = processInstanceId;
        this.currentUserId = currentUserId;
        this.transferUserId = transferUserId;
        this.currentTaskKey = currentTaskKey;
    }

    public Object execute(CommandContext commandContext) {
        ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
        String monitorName = WorkflowConstant.NEXT_MONITORS_PREFIX + currentTaskKey;
        String monitor = (String) executionEntity.getVariable(monitorName);

        if (currentUserId.equals(monitor)) {
            executionEntity.setVariable(monitorName, transferUserId);
        }
        return null;
    }

}
