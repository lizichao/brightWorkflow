package cn.com.bright.workflow.bpmn.listener;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang.StringUtils;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.workflow.service.WorkflowLogService;
import cn.com.bright.workflow.util.WorkflowConstant;

public class ProcessStartLogListener extends BaseLogListener<DelegateExecution> implements ExecutionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3868363287274590392L;

    @Resource
    WorkflowLogService workflowLogService;

    public void notify(DelegateExecution execution) throws Exception {
        //workflowLogService =null;
        workflowLogService.recordProcessStartLog(execution.getProcessInstanceId(),
            getApproveOperate(execution), getApproveRemark(execution));
    }

    private String getApproveOperate(DelegateExecution execution) {
        String operate = (String) execution.getVariable(WorkflowConstant.INTERNALOPERATE);
        String copyToOperate = getCopyToOperate(execution);
        copyToOperate = (StringUtils.isEmpty(copyToOperate) ? "" : "," + copyToOperate);
        if (StringUtil.isNotEmpty(operate)) {
            return operate + copyToOperate;
        }
        return "发起流程";
    }
}
