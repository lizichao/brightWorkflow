package cn.com.bright.workflow.web.action.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.task.Task;
import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.api.vo.FlowVO;
import cn.com.bright.workflow.api.vo.ProcessStartFormVO;
import cn.com.bright.workflow.api.vo.ProcessViewFormVO;
import cn.com.bright.workflow.api.vo.TaskViewFormVO;
import cn.com.bright.workflow.api.vo.UserTaskVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.bpmn.listener.TaskDelegateListener;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.ProcessDefinitionSuspendException;
import cn.com.bright.workflow.service.FormResourceService;
import cn.com.bright.workflow.service.ProcessPermissionService;
import cn.com.bright.workflow.service.TaskQueryService;
import cn.com.bright.workflow.util.DateUtil;
import cn.com.bright.workflow.util.JsonUtil;
import cn.com.bright.workflow.util.WorkflowConstant;

public class FormServiceAction {
    private XmlDocPkgUtil xmlDocUtil = null;

    private Log log4j = new Log(this.getClass().toString());

    private FormResourceService formResourceService;

    private ProcessPermissionService processPermissionService;


    public Document doPost(Document xmlDoc) throws IOException {
        this.setFormResourceService((FormResourceService) ApplicationContextHelper.getBean("formResourceService"));
        this.setProcessPermissionService((ProcessPermissionService) ApplicationContextHelper.getBean("processPermissionService"));
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        if ("getRenderedStartForm".equals(action)) {
            getRenderedStartForm();
        } else if ("getRenderedTaskForm".equals(action)) {
            getRenderedTaskForm();
        } else if ("getRenderedViewForm".equals(action)) {
            getRenderedViewForm();
        } else if ("getApproveLog".equals(action)) {
            getApproveLog();
        } else if ("getTaskEditData".equals(action)) {
            getTaskEditData();
        } else if ("getMultiNodes".equals(action)) {
            getMultiNodes();
        } else if ("getTaskConfigHandlers".equals(action)) {
            getTaskConfigHandlers();
        }
        return xmlDoc;
    }

    private void getRenderedViewForm() throws IOException {
        Element reqElement = xmlDocUtil.getRequestData();
        String processInstanceId = reqElement.getChildTextTrim("query_processInstanceId");
        ProcessViewFormVO processViewFormVO = (ProcessViewFormVO) formResourceService.getRenderedViewForm(processInstanceId, "processViewFormEngine");
        Element data = new Element("Data");
        Element record = new Element("Record");

        XmlDocPkgUtil.setChildText(record, "processTitle", processViewFormVO.getProcessTitle());
        XmlDocPkgUtil.setChildText(record, "processViewFormKey", processViewFormVO.getFormKey());
        XmlDocPkgUtil.setChildText(record, "processBusinessKey", processViewFormVO.getProcessBusinessKey());
        XmlDocPkgUtil.setChildText(record, "processDefKey", processViewFormVO.getProcessDefinitionKey());
        XmlDocPkgUtil.setChildText(record, "processInstanceId", processViewFormVO.getProcessInstanceId());
        XmlDocPkgUtil.setChildText(record, "processCreatorId", processViewFormVO.getProcessCreator().getUserId());
        XmlDocPkgUtil.setChildText(record, "processCreatorCode", processViewFormVO.getProcessCreator().getUserCode());
        XmlDocPkgUtil.setChildText(record, "processCreatorName", processViewFormVO.getProcessCreator().getUserName());
        XmlDocPkgUtil.setChildText(record, "processCreateTime",DateUtil.date24ToString(processViewFormVO.getProcessCreateTime()));
        XmlDocPkgUtil.setChildText(record, "processViewFormVO", JsonUtil.objectToJson(processViewFormVO));

        Element handlers = new Element("Handlers");
        Element dataHandlers = new Element("Data");
        for (UserVO handlerVO : processViewFormVO.getProcessHandlers()) {
            Element recordHandler = new Element("Record");
            XmlDocPkgUtil.setChildText(recordHandler, "userId", "" + handlerVO.getUserId());
            XmlDocPkgUtil.setChildText(recordHandler, "userCode", "" + handlerVO.getUserCode());
            XmlDocPkgUtil.setChildText(recordHandler, "userName", "" + handlerVO.getUserName());
            dataHandlers.addContent(recordHandler);
        }
        handlers.addContent(dataHandlers);
        record.addContent(handlers);

        data.addContent(record);
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }

    private void getRenderedTaskForm() throws IOException {
        Element reqElement = xmlDocUtil.getRequestData();
        String taskId = reqElement.getChildTextTrim("query_taskId");
        Task task = ApplicationContextHelper.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        if (null == task) {
            xmlDocUtil.setResult("-1");
            xmlDocUtil.writeErrorMsg("10107", "该任务不存在！");
            return;
        }
        if (!processPermissionService.checkTaskPermission(taskId)) {
            xmlDocUtil.setResult("-1");
            xmlDocUtil.writeErrorMsg("10607", "当前用户没有任务审批权限!");
            return;
        }
        TaskViewFormVO taskViewFormVO = (TaskViewFormVO) formResourceService.getRenderedTaskForm(taskId,
            "taskFormEngine");

        Element data = new Element("Data");
        Element record = new Element("Record");

        XmlDocPkgUtil.setChildText(record, "processTitle", taskViewFormVO.getProcessTitle());
        XmlDocPkgUtil.setChildText(record, "taskId", taskViewFormVO.getTaskId());
        XmlDocPkgUtil.setChildText(record, "taskKey", taskViewFormVO.getTaskKey());
        XmlDocPkgUtil.setChildText(record, "taskName", taskViewFormVO.getTaskName());
        XmlDocPkgUtil.setChildText(record, "taskFormKey", taskViewFormVO.getFormKey());
        XmlDocPkgUtil.setChildText(record, "processBusinessKey", taskViewFormVO.getProcessBusinessKey());
        XmlDocPkgUtil.setChildText(record, "processDefKey", taskViewFormVO.getProcessDefinitionKey());
        XmlDocPkgUtil.setChildText(record, "processInstanceId", taskViewFormVO.getProcessInstanceId());
        XmlDocPkgUtil.setChildText(record, "processCreatorId", taskViewFormVO.getProcessCreator().getUserId());
        XmlDocPkgUtil.setChildText(record, "processCreatorCode", taskViewFormVO.getProcessCreator().getUserCode());
        XmlDocPkgUtil.setChildText(record, "processCreatorName", taskViewFormVO.getProcessCreator() .getUserName());
        XmlDocPkgUtil.setChildText(record, "processCreateTime",
            DateUtil.date24ToString(taskViewFormVO.getProcessCreateTime()));
        XmlDocPkgUtil.setChildText(record, "taskViewFormVO", JsonUtil.objectToJson(taskViewFormVO));

        if (!taskViewFormVO.isSubTask()) {
            Element flow = new Element("Flow");
            Element dataFlow = new Element("Data");
            for (FlowVO flowVO : taskViewFormVO.getFlows()) {
                Element recordFlow = new Element("Record");
                XmlDocPkgUtil.setChildText(recordFlow, "flowId", "" + flowVO.getId());
                XmlDocPkgUtil.setChildText(recordFlow, "flowName", "" + flowVO.getName());
                dataFlow.addContent(recordFlow);
            }
            flow.addContent(dataFlow);
            record.addContent(flow);

        }

        Element handlers = new Element("Handlers");
        Element dataHandlers = new Element("Data");
        for (UserVO handlerVO : taskViewFormVO.getProcessHandlers()) {
            Element recordHandler = new Element("Record");
            XmlDocPkgUtil.setChildText(recordHandler, "userId", "" + handlerVO.getUserId());
            XmlDocPkgUtil.setChildText(recordHandler, "userCode", "" + handlerVO.getUserCode());
            XmlDocPkgUtil.setChildText(recordHandler, "userName", "" + handlerVO.getUserName());
            dataHandlers.addContent(recordHandler);
        }
        handlers.addContent(dataHandlers);
        record.addContent(handlers);

        data.addContent(record);
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }

    private void getRenderedStartForm() throws IOException {
        Element session = xmlDocUtil.getSession();
        String userid = session.getChildTextTrim("userid");

        Element reqElement = xmlDocUtil.getRequestData();
        String processKey = reqElement.getChildTextTrim("query_processKey");
        PlatformDao dao = null;

//        try {
//            dao = new PlatformDao();
//            StringBuffer sql = new StringBuffer("");
//            sql.append("SELECT a.USERID");
//            sql.append(" FROM pcmc_user_role a");
//            sql.append(" WHERE     a.USERID = ?");
//            sql.append(" AND a.ROLEID IN (SELECT t.roleid");
//            sql.append("  FROM workflow_node_role t");
//            sql.append("  WHERE t.processdefkey = ? AND t.node_type = 'startEvent')");
//            ArrayList uList = new ArrayList();
//            uList.add(userid);
//            uList.add(processKey);
//            dao.setSql(sql.toString());
//            dao.setBindValues(uList);
//            Element tmp = dao.executeQuerySql(-1, 1);
//            if (tmp.getChildren("Record").size() == 0) {
//                xmlDocUtil.writeErrorMsg("10607", "您没权限发起流程");
//                xmlDocUtil.setResult("-1");
//                return;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            dao.rollBack();
//        } finally {
//            dao.releaseConnection();
//        }

        try {
            ProcessStartFormVO processStartFormVO = (ProcessStartFormVO) formResourceService.getRenderedStartFormByProcessKey(processKey);

            Element data = new Element("Data");
            Element record = new Element("Record");
            data.addContent(record);
            XmlDocPkgUtil.setChildText(record, "processDefKey","" + processStartFormVO.getProcessDefinitionKey());
            XmlDocPkgUtil.setChildText(record, "processDefName","" + processStartFormVO.getProcessDefinitionName());
            XmlDocPkgUtil.setChildText(record, "processDefId","" + processStartFormVO.getProcessDefinitionId());
            XmlDocPkgUtil.setChildText(record, "processStartFormKey", "" + processStartFormVO.getFormKey());
            XmlDocPkgUtil.setChildText(record, "processStartFormVO",JsonUtil.objectToJson(processStartFormVO));
            xmlDocUtil.getResponse().addContent(data);
            xmlDocUtil.setResult("0");
        } catch (ProcessDefinitionSuspendException e) {

            e.printStackTrace();
            xmlDocUtil.writeErrorMsg("10201", "流程定义已经挂起，不能发起流程");
            // xmlDocUtil.writeHintMsg("10201","修改密码成功");
            xmlDocUtil.setResult("-1");
        } catch (Exception e) {
            e.printStackTrace();
            // xmlDocUtil.writeErrorMsg("10602",e.getMessage());
        }
    }

    private void getApproveLog() {
        Element reqElement = xmlDocUtil.getRequestData();
        String processInstanceId = reqElement.getChildTextTrim("processInstanceId");
        PlatformDao dao = new PlatformDao();
        ArrayList bvals = new ArrayList();
        StringBuffer sql = new StringBuffer("");
        try {
            sql.append(" select t.* from workflow_approve_log t where t.processInstanceId=? order by createTime desc ");
            bvals.add(processInstanceId);
            // bvals.add("33");
            // bvals.add("33");
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception ex) {
            ex.printStackTrace();
            log4j.logError(ex);
            // xmlDocUtil.writeErrorMsg("10607", "查询当前用户信息错误");
        }
    }

    private void getTaskEditData() throws IOException {
        Element reqElement = xmlDocUtil.getRequestData();
        String processInstanceId = reqElement.getChildTextTrim("processInstanceId");
        TaskQueryService taskQueryService = ApplicationContextHelper.getTaskQueryService();
        List<Task> tasks = taskQueryService.findTaskByProcessInstanceId(processInstanceId);

        Element data = new Element("Data");
        Element record = new Element("Record");
        JSONArray jSONArray = new JSONArray();
        List<UserTaskVO> userTasks = new ArrayList<UserTaskVO>();
        for (Task task : tasks) {
            /*
             * XmlDocPkgUtil.setChildText(record, "taskId", task.getId());
             * XmlDocPkgUtil.setChildText(record, "taskName", task.getName());
             * Element handlers = new Element("Handlers"); Element dataHandlers
             * = new Element("Data"); for(HandlerVO handlerVO : handlerVOSet){
             * Element recordHandler = new Element("Record");
             * XmlDocPkgUtil.setChildText(recordHandler, "userId", "" +
             * handlerVO.getUserId()); XmlDocPkgUtil.setChildText(recordHandler,
             * "userCode", "" + handlerVO.getUserCode());
             * XmlDocPkgUtil.setChildText(recordHandler, "userName", "" +
             * handlerVO.getUserName()); dataHandlers.addContent(recordHandler);
             * } handlers.addContent(dataHandlers); record.addContent(handlers);
             */

            // Element record = new Element("Record");
            Set<UserVO> handlerVOSet = taskQueryService.searchTaskhandler(task);
            UserTaskVO userTaskVO = new UserTaskVO();
            userTaskVO.setProcessInstanceId(processInstanceId);
            userTaskVO.setTaskId(task.getId());
            userTaskVO.setTaskName(task.getName());
            userTaskVO.setHandlers(handlerVOSet);
            // XmlDocPkgUtil.setChildText(record,
            // "userTaskVO",JsonUtil.objectToJson(userTaskVO));
            JSONObject jSONObject = new JSONObject(JsonUtil.objectToJson(userTaskVO));
            jSONArray.put(jSONObject);
            // userTasks.add(userTaskVO);
            // data.addContent(record);
        }

        XmlDocPkgUtil.setChildText(record, "userTaskVO", jSONArray.toString());
        data.addContent(record);
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }

    private void getMultiNodes() {
        Element reqElement = xmlDocUtil.getRequestData();
        String processInstanceId = reqElement.getChildTextTrim("processInstanceId");
        String multiTaskKey = reqElement.getChildTextTrim("multiTaskKey");
        TaskService taskService = ApplicationContextHelper.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId)
            .taskDefinitionKey(multiTaskKey).list();
        List<String> departments = new ArrayList<String>();
        boolean isHaveAssist = false;
        for (Task task : tasks) {
            // TaskEntity taskEntity = (TaskEntity)task;
            String taskIsPrincipal = (String) taskService.getVariableLocal(task.getId(),
                WorkflowConstant.TASK_WHETHER_PRINCIPAL);
            if (!TaskDelegateListener.MAJOR_TASK.equals(taskIsPrincipal)) {
                isHaveAssist = true;
                String taskMultiDepartment = (String) taskService.getVariableLocal(task.getId(),
                    WorkflowConstant.TASK_MULTI_DEPARTMENT);
                departments.add(taskMultiDepartment);
            }
        }

        List<DepartmentVO> assistDepartmentVOs = ApplicationContextHelper.getDepartmentQueryService().getMultiDepartmentVO(departments);
        Element data = new Element("Data");
        Element record = new Element("Record");
        XmlDocPkgUtil.setChildText(record, "isHaveAssist", isHaveAssist ? "1" : "0");

        Element assistDepartments = new Element("AssistDepartment");
        Element dataAssists = new Element("Data");
        for (DepartmentVO departmentVO : assistDepartmentVOs) {
            Element recordHandler = new Element("Record");
            XmlDocPkgUtil.setChildText(recordHandler, "deptId", "" + departmentVO.getDeptId());
            XmlDocPkgUtil.setChildText(recordHandler, "deptCode", "" + departmentVO.getDeptCode());
            XmlDocPkgUtil.setChildText(recordHandler, "deptName", "" + departmentVO.getDeptName());
            dataAssists.addContent(recordHandler);
        }
        assistDepartments.addContent(dataAssists);
        record.addContent(assistDepartments);

        data.addContent(record);
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }

    private void getTaskConfigHandlers() {
        Element reqElement = xmlDocUtil.getRequestData();
        String taskId = reqElement.getChildTextTrim("taskId");
        Task taskEntity = ApplicationContextHelper.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinitionEntity processDefinitionEntity = ApplicationContextHelper.getManagementService().executeCommand(new GetDeploymentProcessDefinitionCmd(taskEntity.getProcessDefinitionId()));
        Set<UserVO> users = ApplicationContextHelper.getWorkflowDefExtService().getUserTaskHandlerVOs(
            processDefinitionEntity.getKey(), taskEntity.getTaskDefinitionKey());
        int pageSize = xmlDocUtil.getPageSize();
        int pageNo = xmlDocUtil.getPageNo();

        int startPageNum = (pageNo - 1) * pageSize;
        int endPageNum = (pageNo - 1) * pageSize + pageSize;

        String[] act_flds = { "userid", "usercode", "username" };
        Element data = XmlDocPkgUtil.createMetaData(act_flds);

        // data.addContent(pageInfo);
        int i = 0;
        for (UserVO handlerVO : users) {
            if (i >= startPageNum && i < endPageNum) {
                // Element recordHandler = new Element("Record");
                // XmlDocPkgUtil.setChildText(recordHandler, "userId", "" +
                // handlerVO.getUserId());
                // XmlDocPkgUtil.setChildText(recordHandler, "userCode", "" +
                // handlerVO.getUserCode());
                // XmlDocPkgUtil.setChildText(recordHandler, "userName", "" +
                // handlerVO.getUserName());
                String[] vals = new String[act_flds.length];
                vals[0] = handlerVO.getUserId();
                vals[1] = handlerVO.getUserCode();
                vals[2] = handlerVO.getUserName();
                data.addContent(XmlDocPkgUtil.createRecord(act_flds, vals));
            }
            i++;
        }
        // xmlDocUtil.setPageInfo(data, 44, 55, pageSize, pageNo);
        // xmlDocUtil.getResponse().addContent(pageInfo);

        // Element pageInfo = new Element("CustomPageInfo");
        Element data1 = new Element("Data");
        Element record = new Element("Record");
        XmlDocPkgUtil.setChildText(record, "RecordCount", String.valueOf(users.size()));
        XmlDocPkgUtil.setChildText(record, "PageCount", String.valueOf(users.size() / pageSize + 1));
        XmlDocPkgUtil.setChildText(record, "PageSize", String.valueOf(pageSize));
        XmlDocPkgUtil.setChildText(record, "PageNo", String.valueOf(pageNo));
        data1.addContent(record);
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.getResponse().addContent(data1);
        xmlDocUtil.setResult("0");
    }

    public FormResourceService getFormResourceService() {
        return formResourceService;
    }

    public void setFormResourceService(FormResourceService formResourceService) {
        this.formResourceService = formResourceService;
    }

    public ProcessPermissionService getProcessPermissionService() {
        return processPermissionService;
    }

    public void setProcessPermissionService(ProcessPermissionService processPermissionService) {
        this.processPermissionService = processPermissionService;
    }
}
