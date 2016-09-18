package cn.com.bright.workflow.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.jdom.Element;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.com.bright.workflow.api.vo.ProcessStartVO;
import cn.com.bright.workflow.exception.ProcessInstanceStartException;

@Transactional(rollbackFor = Exception.class)
public class ProcessOperateService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskServiceImplExt taskServiceImplExt;

    @Resource
    private IdentityService identityService;

    @Resource
    WorkflowLogService workflowLogService;

    @Resource
    JdbcTemplate jdbcTemplate;

    public String startProcessInstance(ProcessStartVO processStartVO, String businessKey) {
       // permissionCheck(processStartVO);
        String processInstanceId = "";
        try {
            identityService.setAuthenticatedUserId(processStartVO.getCurrentUserId());
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(processStartVO.getProcessDefinitionId(), businessKey, processStartVO.getVariables());
            processInstanceId= processInstance.getId();
        } finally {
            // identityService =null;
            identityService.setAuthenticatedUserId(null);
        }
        return processInstanceId;
    }

    private void permissionCheck(ProcessStartVO processStartVO) throws ProcessInstanceStartException {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT a.ROLEID");
        sql.append(" FROM pcmc_user_role a");
        sql.append(" WHERE     a.USERID = ?");
        sql.append(" AND a.ROLEID IN (SELECT t.roleid");
        sql.append("  FROM workflow_node_role t");
        sql.append("  WHERE t.processdefkey = ? AND t.node_type = 'startEvent')");
        // map = jdbcTemplate.queryForMap(sql.toString(),
        // taskId, userVO.getUserId(),WorkflowConstant.TASK_DELEGATE_ORIGINAL);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), userid,
            processStartVO.getProcessDefinitionKey());
        if (CollectionUtils.isEmpty(list)) {
            throw new ProcessInstanceStartException("您没权限发起该流程");
        }

    }

    public void suspendProcess(List<String> processInstanceIds) {
        for (String processInstanceId : processInstanceIds) {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            workflowLogService.logCommonProcess(processInstanceId, "挂起流程实例");
        }
    }

    public void activateProcess(List<String> processInstanceIds) {
        for (String processInstanceId : processInstanceIds) {
            runtimeService.activateProcessInstanceById(processInstanceId);
            workflowLogService.logCommonProcess(processInstanceId, "激活流程实例");
        }
    }

    public void deleteProcess(List<String> processInstanceIds) {
        for (String processInstanceId : processInstanceIds) {
            runtimeService.deleteProcessInstance(processInstanceId, "删除");
            workflowLogService.logCommonProcess(processInstanceId, "删除流程实例");
        }
    }

    public void revokeProcess(List<String> processInstanceIds) {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceIds(new HashSet<String>(processInstanceIds)).list();
        for (ProcessInstance processInstance : processInstances) {
            if (processInstance.isSuspended()) {
                throw new ActivitiException("process definition for key '"
                    + processInstance.getProcessDefinitionKey() + "' is suspended;");
            }
            revokeSingleProces(processInstance);
        }
    }

    private void revokeSingleProces(ProcessInstance processInstance) {
        List<Task> tasks = taskServiceImplExt.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
        if (tasks.size() > 1) {
            throw new ActivitiException("process definition for key '"
                + processInstance.getProcessDefinitionKey() + "' is suspended;");
        }
        Task currentTask = tasks.get(0);
        String targetActivityId = getTargetActivityId(processInstance.getProcessDefinitionKey());
        // Map<String, Object> variables = new HashMap<String, Object>();
        // variables.put(WorkflowConstant.IS_AUTO_LOG, "0");
        taskServiceImplExt.freeCompleteNoLog(currentTask.getId(), targetActivityId, null);

    }

    private String getTargetActivityId(String processDefinitionKey) {
        String targetActivityId = "";
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        Element resultElement = null;
        try {
            ArrayList<String> bvals = new ArrayList<String>();
            sql.append("select * from workflow_process_edit where processdefkey=?");
            bvals.add(processDefinitionKey);
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            if (resultElement.getChildren("Record").size() > 0) {
                targetActivityId = resultElement.getChild("Record").getChildTextTrim("refillnodeid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // log4j.logError("[更新用户发生异常.]"+e.getMessage());
            // log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(),
            // "GBK", true));
            // xmlDocUtil.writeErrorMsg("10614","修改密码失败");
        } finally {
            dao.releaseConnection();
        }
        return targetActivityId;
    }

    // @SuppressWarnings("unchecked")
    // public String startProcessInstance(String businessKey, Element
    // processParam,String userid) {
    // //Element processParam = dataElement.getChild("processParam");
    // String processDefinitionKey =
    // processParam.getChildText("processDefinitionKey");
    // String processDefinitionId =
    // processParam.getChildText("processDefinitionId");
    // String processName = processParam.getChildText("processName");
    // List<Element> processVariables =
    // processParam.getChildren("processVariable");
    //
    // try {
    // // Element session = xmlDocUtil.getSession();
    // // String userid = session.getChildTextTrim("userid");
    // identityService.setAuthenticatedUserId(userid);
    // ProcessInstance processInstance = runtimeService
    // .startProcessInstanceById(processDefinitionId, businessKey,
    // null);
    // runtimeService.setProcessInstanceName(processInstance.getId(),
    // processName);
    // } finally {
    // identityService.setAuthenticatedUserId(null);
    // }
    // return null;
    // }
}
