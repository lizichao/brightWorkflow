package cn.com.bright.workflow.bpmn.listener;

import org.activiti.engine.delegate.VariableScope;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

public abstract class BaseLogListener<T extends VariableScope> {
    protected String getCopyToOperate(T variableScope) {
        String copyToVariable = (String) variableScope.getVariable(WorkflowConstant.COPYTO_VARIABLE);
        if (StringUtil.isEmpty(copyToVariable)) {
            return "";
        }

        StringBuffer logOperate = new StringBuffer();
        logOperate.append("³­ËÍ¸ø[");
        String[] copyToArray = copyToVariable.split(",");
        int i = 0;
        for (String copyTo : copyToArray) {
            i++;
            String copyToName = ApplicationContextHelper.getUserQueryService().getUserVO(copyTo).getUserName();
            logOperate.append(copyToName);
            if (i < copyToArray.length) {
                logOperate.append(",");
            }
        }
        logOperate.append("].");
        return logOperate.toString();
    }

    protected String getApproveRemark(T variableScope) {
        String remark = (String) variableScope.getVariable(WorkflowConstant.INTERNALREMARK);
        variableScope.removeVariable(WorkflowConstant.INTERNALREMARK);
        if (StringUtil.isNotEmpty(remark)) {
            return remark;
        }
        return "";
    }

}
