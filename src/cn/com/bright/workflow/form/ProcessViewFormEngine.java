package cn.com.bright.workflow.form;

import java.util.List;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.workflow.api.vo.ProcessViewFormVO;
import cn.com.bright.workflow.api.vo.RevokeTaskVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

public class ProcessViewFormEngine extends BaseFormEngine<ProcessViewFormVO> {

    public String getName() {
        return "processViewFormEngine";
    }

    public Object renderProcessForm(String processInstanceId) {
        // StartFormData startForm
        // =startFormHandler.createStartFormData(processDefinition);
        ProcessViewFormVO processViewFormVO = new ProcessViewFormVO();
        processViewFormVO.setProcessInstanceId(processInstanceId);
        processViewFormVO = super.getBaseForm(processViewFormVO);
        processViewFormVO.setFormKey(getProcessViewFormKey(processViewFormVO));

        if (null == processViewFormVO.getProcessEndTime()) {
            RevokeTaskVO revokeTask = getRevokeTask(processViewFormVO);
            processViewFormVO.setRevokeTask(revokeTask);
            if (StringUtil.isNotEmpty(revokeTask.getSourceTaskId())) {
                processViewFormVO.setHaveRevokeTask(true);
            }
        }
        return processViewFormVO;
    }

    private RevokeTaskVO getRevokeTask(ProcessViewFormVO processViewFormVO) {
        RevokeTaskVO revokeTask = new RevokeTaskVO();
        ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration()
            .getDeploymentManager().findDeployedProcessDefinitionById(processViewFormVO.getProcessDefinitionId());
        String processInstanceId = processViewFormVO.getProcessInstanceId();
        List<Task> tasks = Context.getProcessEngineConfiguration().getTaskService().createTaskQuery().processInstanceId(processInstanceId).list();
        if (tasks.size() > 1 || tasks.size() == 0) {
            return revokeTask;
        }
        Task currentTask = tasks.get(0);
        Object isRollbackTask = ((TaskEntity) currentTask).getVariable("isRollbackTask");
        if (null != isRollbackTask) {
            boolean isRollbackTaskBoolean = (Boolean) isRollbackTask;
            if (isRollbackTaskBoolean) {
                return revokeTask;
            }
        }
        String currentTaskKey = currentTask.getTaskDefinitionKey();
        ActivityImpl sourceActivityImpl = processDefinitionEntity.findActivity(currentTaskKey);
        String sourceActivityType = (String) sourceActivityImpl.getProperty("type");
        if (!sourceActivityType.equals(WorkflowConstant.USERTASKTYPE)) {
            return revokeTask;
        }
        if (null != sourceActivityImpl.getProperty("multiInstance")) {
            return revokeTask;
        }

        String targetActivityKey = (String) ApplicationContextHelper.getRuntimeService().getVariable(processInstanceId, "to->" + currentTask.getTaskDefinitionKey());
        targetActivityKey = targetActivityKey.substring(6, targetActivityKey.length());
        ActivityImpl targetActivityImpl = processDefinitionEntity.findActivity(targetActivityKey);
        String targetActivityType = (String) targetActivityImpl.getProperty("type");
        if (!targetActivityType.equals(WorkflowConstant.USERTASKTYPE)) {
            return revokeTask;
        }
        if (null != targetActivityImpl.getProperty("multiInstance")) {
            return revokeTask;
        }

        List<HistoricTaskInstance> historicTaskInstances = ApplicationContextHelper.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
            .taskDefinitionKey(targetActivityKey).orderByHistoricTaskInstanceEndTime().desc().list();
        String userId = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        if (historicTaskInstances.get(0).getAssignee().equals(userId)) {
            revokeTask.setSourceTaskId(currentTask.getId());
            revokeTask.setTargetTaskKey(targetActivityKey);
            revokeTask.setTargetTaskName((String) targetActivityImpl.getProperty("name"));
            revokeTask.setTargetTaskAssignee(userId);
            revokeTask.setProcessInstanceId(processInstanceId);
        }
        return revokeTask;
    }

    private String getProcessViewFormKey(ProcessViewFormVO processViewFormVO) {
        String processViewFormKey = ApplicationContextHelper.getJdbcTemplate().queryForObject(
            "select processformkey from workflow_process_edit where processdefkey=?", String.class,processViewFormVO.getProcessDefinitionKey());
        return processViewFormKey;
        /*
         * String processViewFormKey = ""; PlatformDao dao = new PlatformDao();
         * StringBuffer sql = new StringBuffer(""); Element resultElement =
         * null; try { ArrayList<String> bvals = new ArrayList<String>();
         * sql.append
         * ("select * from workflow_process_edit where processdefkey=?");
         * bvals.add
         * (String.valueOf(processViewFormVO.getProcessDefinitionKey()));
         * dao.setSql(sql.toString()); dao.setBindValues(bvals); resultElement =
         * dao.executeQuerySql(-1, 1); if
         * (resultElement.getChildren("Record").size() > 0) { processViewFormKey
         * =
         * resultElement.getChild("Record").getChildTextTrim("processformkey");
         * } } catch (Exception e) { e.printStackTrace(); //
         * log4j.logError("[更新用户发生异常.]"+e.getMessage()); //
         * log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), // "GBK",
         * true)); // xmlDocUtil.writeErrorMsg("10614","修改密码失败"); } finally {
         * dao.releaseConnection(); }
         */
    }
}
