package cn.com.bright.workflow.web.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.api.vo.ProcessOperateVO;
import cn.com.bright.workflow.api.vo.ProcessStartVO;
import cn.com.bright.workflow.api.vo.TaskCompleteVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.service.ProcessOperateService;
import cn.com.bright.workflow.util.WorkflowConstant;

public abstract class BaseWorkflowAction {
    protected XmlDocPkgUtil xmlDocUtil = null;
    private static final String PROCESS_TYPE = "1";
    private static final String TASK_TYPE = "2";

    protected ProcessOperateVO createProcessOperateVO() {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        HttpServletRequest request = ApplicationContext.getRequest();
        String submitType = request.getParameter("submitType");
        String processDefKey = request.getParameter("processDefKey");
        String processDefName = request.getParameter("processDefName");
        String processDefId = request.getParameter("processDefId");
        String processParam = request.getParameter("processParam");
        String sequenceFlow = request.getParameter("sequenceFlow");
        String nextHandlers = (String) request.getParameter(WorkflowConstant.NEXT_HANDLERS);
        String nextDepartments = (String) request.getParameter(WorkflowConstant.NEXT_DEPARTMENTS);
        String nextPrincipalHandlers = (String) request.getParameter(WorkflowConstant.NEXT_PRINCIPAL_HANDLERS);
        String nextPrincipalDepartments = (String) request.getParameter(WorkflowConstant.NEXT_PRINCIPAL_DEPARTMENTS);
        // String multiCompleteMark =
        // (String)request.getParameter(WorkflowConstant.MULTI_COMPLETE_MARK);

        ProcessOperateVO processOperateVO = null;
        if (submitType.equals(PROCESS_TYPE)) {
            ProcessStartVO processStartVO = new ProcessStartVO(processDefId, processDefKey, processDefName,userid);
            processOperateVO = processStartVO;
        } else {
            TaskCompleteVO taskCompleteVO = new TaskCompleteVO(processDefId, processDefKey, processDefName,userid);
            String processInstanceId = request.getParameter("processInstanceId");
            String taskId = request.getParameter("taskId");
            String taskDefKey = request.getParameter("taskDefKey");
            taskCompleteVO.setTaskId(taskId);
            taskCompleteVO.setProcessInstanceId(processInstanceId);
            taskCompleteVO.setTaskDefKey(taskDefKey);
            processOperateVO = taskCompleteVO;
        }
        Map<String, Object> processParamMap = getProcessParamMap(processParam);
        processParamMap.put(WorkflowConstant.PROCESS_SEQUENCEFLOW, sequenceFlow);
        // if(!StringUtil.isEmpty(nextHandlers)){
        processParamMap.put(WorkflowConstant.NEXT_HANDLERS,StringUtil.isEmpty(nextHandlers) ? Collections.EMPTY_LIST : Arrays.asList(nextHandlers.split(",")));
        // }
        if (!StringUtil.isEmpty(nextDepartments)) {
            processParamMap.put(WorkflowConstant.NEXT_DEPARTMENTS,StringUtil.isEmpty(nextDepartments) ? Collections.EMPTY_LIST : Arrays.asList(nextDepartments.split(",")));
        }
        if (!StringUtil.isEmpty(nextPrincipalHandlers)) {
            processParamMap.put(WorkflowConstant.NEXT_PRINCIPAL_HANDLERS,StringUtil.isEmpty(nextPrincipalHandlers) ? Collections.EMPTY_LIST : Arrays.asList(nextPrincipalHandlers.split(",")));
        }
        if (!StringUtil.isEmpty(nextPrincipalDepartments)) {
            processParamMap.put(WorkflowConstant.NEXT_PRINCIPAL_DEPARTMENTS,StringUtil.isEmpty(nextPrincipalDepartments) ? Collections.EMPTY_LIST : Arrays.asList(nextPrincipalDepartments.split(",")));
        }
        if (null == processParamMap.get(WorkflowConstant.MULTI_COMPLETE_MARK)) {
            processParamMap.put(WorkflowConstant.MULTI_COMPLETE_MARK, null);
        }
        processOperateVO.setVariables(processParamMap);
        return processOperateVO;
    }

    private Map<String, Object> getProcessParamMap(String processParam) {
        Map<String, Object> processParamMap = new HashMap<String, Object>();
        if (StringUtil.isEmpty(processParam)) {
            return processParamMap;
        }
        // JSONArray jSONArray = new JSONArray(processParam);
        JSONObject jSONObject = new JSONObject(processParam);
        // for (int i = 0; i < jSONArray.length(); i++) {
        // JSONObject jSONObject = jSONArray.getJSONObject(i);
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (StringUtils.isNotEmpty(jSONObject.getString(key))) {
                processParamMap.put(key, jSONObject.getString(key));
            }
        }
        // }
        return processParamMap;
    }

    protected ProcessStartVO createProcessStartVO() {
        ProcessStartVO processStartVO = (ProcessStartVO) this.createProcessOperateVO();
        return processStartVO;
    }

    protected TaskCompleteVO createTaskCompleteVO() {
        TaskCompleteVO taskCompleteVO = (TaskCompleteVO) this.createProcessOperateVO();
        return taskCompleteVO;
    }

    public void startProcessInstance(String businessKey) {
        ProcessOperateVO processOperateVO = this.createProcessOperateVO();
        ProcessOperateService processOperateService = ApplicationContextHelper.getProcessOperateService();
        processOperateService.startProcessInstance((ProcessStartVO) processOperateVO, businessKey);
    }

    //public void completeTask(T t) {
     //   ProcessOperateVO processOperateVO = this.createProcessOperateVO();
      //  TaskOperateService taskOperateService = ApplicationContextHelper.getTaskOperateService();
        // try {
        // completeBusinessTask((TaskCompleteVO)processOperateVO,t);
        // } catch (TaskDelegateException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // try {
        // taskOperateService.completeTask((TaskCompleteVO)processOperateVO);

        // } catch (TaskDelegateException e) {
        // //dealCompleteException(e);
        // xmlDocUtil.setResult("-1");
        // xmlDocUtil.writeErrorMsg("10607", "当前任务是主办任务，还有协办任务未完成！");
        // e.printStackTrace();
        // return false;
        // }
        // return true;
   // }

    // protected abstract void completeBusinessTask(TaskCompleteVO
    // taskCompleteVO,T businessVO)throws TaskDelegateException;
}
