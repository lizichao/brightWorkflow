package cn.com.bright.workflow.bpmn.listener;

import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.workflow.api.vo.IdentityLinkTypeExt;
import cn.com.bright.workflow.util.WorkflowConstant;

public abstract class BaseCopyToListener<T extends VariableScope> {

    public void notifyCopyToUser(VariableScope variableScope) {
        String copyToVariable = (String) variableScope.getVariable(WorkflowConstant.COPYTO_VARIABLE);
        if (StringUtil.isEmpty(copyToVariable)) {
            return;
        }
        variableScope.removeVariable(WorkflowConstant.COPYTO_VARIABLE);
        String processInstanceId = getProcessInstanceId((T) variableScope);
        ExecutionEntity executionEntity = Context.getCommandContext().getExecutionEntityManager()
            .findExecutionById(processInstanceId);

        String[] copyToArray = copyToVariable.split(",");
        for (String copyToStr : copyToArray) {
            IdentityLinkEntity identityLinkEntity = new IdentityLinkEntity();
            identityLinkEntity.setProcessInstance(executionEntity);
            identityLinkEntity.setUserId(copyToStr);
            identityLinkEntity.setGroupId(null);
            identityLinkEntity.setType(IdentityLinkTypeExt.COPYTO);
            identityLinkEntity.insert();
            Context
                .getCommandContext()
                .getHistoryManager()
                .createProcessInstanceIdentityLinkComment(processInstanceId, copyToStr, null,
                    IdentityLinkTypeExt.COPYTO, true);
        }

    }

    protected abstract String getProcessInstanceId(T variableScope);
}
