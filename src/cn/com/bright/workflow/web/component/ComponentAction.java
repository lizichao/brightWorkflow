package cn.com.bright.workflow.web.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.api.component.ICounterFunctionNotifyListener;
import cn.com.bright.workflow.api.vo.CounterSignDepartmentVO;
import cn.com.bright.workflow.api.vo.CounterSignUserVO;
import cn.com.bright.workflow.api.vo.CounterSignVO;
import cn.com.bright.workflow.api.vo.DelegateUserVO;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.api.vo.EditCounterSignDepartmentVO;
import cn.com.bright.workflow.api.vo.EditCounterSignUserVO;
import cn.com.bright.workflow.api.vo.EditCounterSignVO;
import cn.com.bright.workflow.api.vo.UserTaskConfigVO;
import cn.com.bright.workflow.api.vo.UserTaskRemindVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.bpmn.graph.ActivitiHistoryGraphBuilder;
import cn.com.bright.workflow.bpmn.graph.Edge;
import cn.com.bright.workflow.bpmn.graph.Graph;
import cn.com.bright.workflow.bpmn.graph.Node;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.TransferMySelfException;
import cn.com.bright.workflow.service.FunctionComponentService;
import cn.com.bright.workflow.util.JsonUtil;
import cn.com.bright.workflow.util.WorkflowConstant;

public class ComponentAction {
    private XmlDocPkgUtil xmlDocUtil = null;
    private Log log4j = new Log(this.getClass().toString());

    private String componentName;

    private String componentParam;

    private FunctionComponentService functionComponentService;

    public Document doPost(Document xmlDoc) throws Exception {
        xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
        String action = xmlDocUtil.getAction();
        this.setFunctionComponentService((FunctionComponentService) ApplicationContextHelper.getBean("functionComponentService"));

        if ("queryTransferUsers".equals(action)) {// 查询可以转审的人
            queryTransferUsers();
        } else if ("transferComponent".equals(action)) {// 转审触发
            try {
                commonDeal();
            } catch (TransferMySelfException e) {
                e.printStackTrace();
                xmlDocUtil.writeErrorMsg("10107", "不能转审给自己！");
                xmlDocUtil.setResult("-1");
                return xmlDoc;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("rollbackPrevious".equals(action)) {// 退回上一节点触发
            boolean isMeet = validRollback();
            if (!isMeet) {
                xmlDocUtil.writeErrorMsg("10107", "当前不满足驳回到上一节点的条件！");
                xmlDocUtil.setResult("-1");
                return xmlDoc;
            }
            commonDeal();
        } else if ("queryCounterNodes".equals(action)) {// 查询会签节点
            queryCounterNodes();
        } else if ("counterSignOperate".equals(action)) {// 加签减签触发
            counterSignOperate();
        } else if ("addSubTask".equals(action)) {// 添加子任务
            commonDeal();
        } else if ("closeCounterSign".equals(action)) {// 结束会签节点
            closeCounterSign();
        } else if ("addTaskHandler".equals(action)) {// 新增处理人
            commonDeal();
        } else if ("editTaskHandler".equals(action)) {// 编辑处理人
            commonDeal();
        } else if ("revokeTask".equals(action)) {// 收回任务
            revokeTask();
        } else if ("getUserList".equals(action)) {// 查询所有用户
            getUserList();
        } else if ("editCounterInfo".equals(action)) {
            editCounterInfo();
        } else if ("getEditCounterInfo".equals(action)) {
            getEditCounterInfo();
        } else if ("getLastDelegateUsers".equals(action)) {
            getLastDelegateUsers();
        }
        return xmlDoc;
    }

    private void commonDeal() throws Exception {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        HttpServletRequest request = ApplicationContext.getRequest();
        // Element requestElement = xmlDocUtil.getRequestData();
        String componentName = request.getParameter("componentName");
        String componentParam = request.getParameter("componentParam");
        Map<String, String> componentParamMap = JsonUtil.stringToObject(componentParam, Map.class);
        componentParamMap.put(WorkflowConstant.CURRENT_USERID, userid);

        FunctionComponentService functionComponentService = (FunctionComponentService) ApplicationContextHelper.getBean("functionComponentService");
        functionComponentService.notifyEvent(componentName, componentParamMap);
        xmlDocUtil.setResult("0");
        xmlDocUtil.writeHintMsg("10609", "触发功能组件成功");
    }

    private void revokeTask() {
        Element dataElement = xmlDocUtil.getRequestData();
        String sourceTaskId = dataElement.getChildTextTrim("sourceTaskId");
        String processInstanceId = dataElement.getChildTextTrim("processInstanceId");
        String targetTaskKey = dataElement.getChildTextTrim("targetTaskKey");
        String targetTaskAssignee = dataElement.getChildTextTrim("targetTaskAssignee");
        String targetTaskName = dataElement.getChildTextTrim("targetTaskName");
        functionComponentService.revokeTask(sourceTaskId, processInstanceId, targetTaskKey,targetTaskAssignee, targetTaskName);
        xmlDocUtil.setResult("0");

    }

    private void getEditCounterInfo() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processInstanceId = dataElement.getChildText("processInstanceId");
        String processDefKey = dataElement.getChildText("processDefKey");
        String taskDefKey = dataElement.getChildText("taskDefKey");
        String taskId = dataElement.getChildText("taskId");
        int multiKind = Integer.parseInt(dataElement.getChildText("multiKind")); // 部门还是人员会签

        Element data = null;
        Element data1 = null;
        Element data2 = null;
        if (multiKind == UserTaskRemindVO.DEPARTMENT_MULTI) {
            List[] deptList = functionComponentService.getEditDeptCounterInfo(processInstanceId, processDefKey, taskDefKey);
            List<DepartmentVO> nextSelectDepartments = deptList[0];
            List<DepartmentVO> optionalDepartments = deptList[1];
            List<String> majorDeptList = deptList[2];

            String[] deptColumn = { "deptid", "deptcode", "deptname" };
            data = XmlDocPkgUtil.createMetaData(deptColumn);
            for (DepartmentVO departmentVO : nextSelectDepartments) {
                Element record = new Element("Record");
                XmlDocPkgUtil.setChildText(record, "deptId", "" + departmentVO.getDeptId());
                XmlDocPkgUtil.setChildText(record, "deptCode", "" + departmentVO.getDeptCode());
                XmlDocPkgUtil.setChildText(record, "deptName", "" + departmentVO.getDeptName());
                if (!CollectionUtils.isEmpty(majorDeptList) && majorDeptList.contains(departmentVO.getDeptId())) {
                    XmlDocPkgUtil.setChildText(record, "isMajor", "1");
                } else {
                    XmlDocPkgUtil.setChildText(record, "isMajor", "0");
                }
                data.addContent(record);
            }

            data1 = XmlDocPkgUtil.createMetaData(deptColumn);
            for (DepartmentVO departmentVO : optionalDepartments) {
                Element record = new Element("Record");
                XmlDocPkgUtil.setChildText(record, "deptId", "" + departmentVO.getDeptId());
                XmlDocPkgUtil.setChildText(record, "deptCode", "" + departmentVO.getDeptCode());
                XmlDocPkgUtil.setChildText(record, "deptName", "" + departmentVO.getDeptName());
                data1.addContent(record);
            }
        } else {
            List[] userList = functionComponentService.getEditUserCounterInfo(processInstanceId, processDefKey, taskDefKey, taskId);
            List<DelegateUserVO> nextSelectUsers = userList[0];
            List<UserVO> optionalUsers = userList[1];
            List<String> majorUserList = userList[2];

            String[] delageteUserColumn = { "userId", "userCode", "userName", "delegateUserId",
                    "delegateUserCode", "delegateUserName" };
            data = XmlDocPkgUtil.createMetaData(delageteUserColumn);
            for (DelegateUserVO delegateUserVO : nextSelectUsers) {
                UserVO delegatedUser = delegateUserVO.getDelegatedUser();
                Element record = new Element("Record");
                XmlDocPkgUtil.setChildText(record, "userId", "" + delegateUserVO.getUserId());
                XmlDocPkgUtil.setChildText(record, "userCode", "" + delegateUserVO.getUserCode());
                XmlDocPkgUtil.setChildText(record, "userName", "" + delegateUserVO.getUserName());
                XmlDocPkgUtil.setChildText(record, "delegateUserId",(null != delegatedUser) ? delegatedUser.getUserId() : "");
                XmlDocPkgUtil.setChildText(record, "delegateUserCode",(null != delegatedUser) ? delegatedUser.getUserCode() : "");
                XmlDocPkgUtil.setChildText(record, "delegateUserName",(null != delegatedUser) ? delegatedUser.getUserName() : "");
                if (!CollectionUtils.isEmpty(majorUserList) && majorUserList.contains(delegateUserVO.getUserId())) {
                    XmlDocPkgUtil.setChildText(record, "isMajor", "1");
                } else {
                    XmlDocPkgUtil.setChildText(record, "isMajor", "0");
                }
                data.addContent(record);
            }

            String[] userColumn = { "userId", "userCode", "userName" };
            data1 = XmlDocPkgUtil.createMetaData(userColumn);
            for (UserVO userVO : optionalUsers) {
                Element record = new Element("Record");
                XmlDocPkgUtil.setChildText(record, "userId", "" + userVO.getUserId());
                XmlDocPkgUtil.setChildText(record, "userCode", "" + userVO.getUserCode());
                XmlDocPkgUtil.setChildText(record, "userName", "" + userVO.getUserName());
                data1.addContent(record);
            }
        }
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.getResponse().addContent(data1);
        xmlDocUtil.setResult("0");
    }

    private void editCounterInfo() throws Exception {
        Element dataElement = xmlDocUtil.getRequestData();
        int multiKind = Integer.parseInt(dataElement.getChildText("multiKind"));// 部门还是人员会签
        String processInstanceId = dataElement.getChildText("processInstanceId");
        String processDefKey = dataElement.getChildText("processDefKey");
        String processDefName = dataElement.getChildText("processDefName");
        String taskDefKey = dataElement.getChildText("taskDefKey");
        String taskId = dataElement.getChildText("taskId");
        String dealWay = dataElement.getChildText("dealWay");
        String businessKey = dataElement.getChildText("businessKey");

        EditCounterSignVO editCounterSignVO = new EditCounterSignVO();
        editCounterSignVO.setBusinessKey(businessKey);
        editCounterSignVO.setProcessInstanceId(processInstanceId);
        editCounterSignVO.setDealWay(dealWay);
        editCounterSignVO.setProcessDefKey(processDefKey);
        editCounterSignVO.setTaskDefKey(taskDefKey);
        editCounterSignVO.setTaskId(taskId);
        editCounterSignVO.setProcessDefName(processDefName);

        if (multiKind == UserTaskRemindVO.DEPARTMENT_MULTI) {
            String submitDepts = dataElement.getChildText("submitDepts");
            String majorDept = dataElement.getChildText("majorDept");
            String addDept = dataElement.getChildText("addDept");
            String removeDept = dataElement.getChildText("removeDept");

            List<DepartmentVO> addDepartmentVOs = getDepts(addDept);
            List<DepartmentVO> removeDepartmentVOs = getDepts(removeDept);

            EditCounterSignDepartmentVO editCounterSignDepartmentVO = new EditCounterSignDepartmentVO();
            BeanUtils.copyProperties(editCounterSignVO, editCounterSignDepartmentVO);
            editCounterSignDepartmentVO.setAddDepartmentVOs(addDepartmentVOs);
            editCounterSignDepartmentVO.setRemoveDepartmentVOs(removeDepartmentVOs);
            editCounterSignDepartmentVO.setMajorDept(majorDept);
            editCounterSignDepartmentVO.setSubmitDepts(submitDepts);

            functionComponentService.editCounterDeptInfo(editCounterSignDepartmentVO);
        } else {
            String submitUsers = dataElement.getChildText("submitUsers");
            String majorUser = dataElement.getChildText("majorUser");
            String addUser = dataElement.getChildText("addUser");
            String removeUser = dataElement.getChildText("removeUser");
            String unChangeUser = dataElement.getChildText("unChangeUser");

            EditCounterSignUserVO editCounterSignUserVO = new EditCounterSignUserVO();
            BeanUtils.copyProperties(editCounterSignVO, editCounterSignUserVO);
            editCounterSignUserVO.setMajorUser(majorUser);
            editCounterSignUserVO.setAddUserStrs(getUsers(addUser));
            editCounterSignUserVO.setRemoveUserStrs(getUsers(removeUser));
            editCounterSignUserVO.setSubmitUsers(submitUsers);
            editCounterSignUserVO.setUnChangeUsers(getUserVOs(unChangeUser));
            editCounterSignUserVO.setUnChangeUserStrs(getUsers(unChangeUser));

            functionComponentService.editCounterUserInfo(editCounterSignUserVO);
        }
        xmlDocUtil.setResult("0");
    }

    private List<DepartmentVO> getDepts(String depts) {
        if (StringUtils.isEmpty(depts)) {
            return Collections.EMPTY_LIST;
        }
        String[] counterDepartment = depts.split(",");
        List<DepartmentVO> departmentVOs = ApplicationContextHelper.getDepartmentQueryService().getMultiDepartmentVO(Arrays.asList(counterDepartment));
        return departmentVOs;
    }

    private List<String> getUsers(String users) {
        if (StringUtils.isEmpty(users)) {
            return Collections.EMPTY_LIST;
        }
        String[] counterUser = users.split(",");
        // List<UserVO> userVOs =
        // ApplicationContextHelper.getUserQueryService().getMultiUserVO(Arrays.asList(counterUser));
        return Arrays.asList(counterUser);
    }

    private List<UserVO> getUserVOs(String users) {
        if (StringUtils.isEmpty(users)) {
            return Collections.EMPTY_LIST;
        }
        String[] counterUser = users.split(",");
        List<UserVO> userVOs = ApplicationContextHelper.getUserQueryService().getMultiUserVO(Arrays.asList(counterUser));
        return userVOs;
    }

    private void closeCounterSign() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processInstanceId = dataElement.getChildTextTrim("query_processInstanceId");
        String processDefinitionId = dataElement.getChildTextTrim("query_processDefId");
        String multiNodeId = dataElement.getChildTextTrim("multiNodeId");
        String processParam = dataElement.getChildTextTrim("processParam");
        String taskId = dataElement.getChildTextTrim("taskId");

        CounterSignVO counterSignVO = new CounterSignVO();
        counterSignVO.setProcessInstanceId(processInstanceId);
        counterSignVO.setProcessDefinitionId(processDefinitionId);
        counterSignVO.setActivityId(multiNodeId);
        counterSignVO.setTaskId(taskId);
        functionComponentService.closeCounterSign(counterSignVO, processParam);
        xmlDocUtil.setResult("0");
        xmlDocUtil.writeHintMsg("10609", "结束会签成功");

        // ProcessDefinitionEntity processDefinition =
        // ApplicationContextHelper.getProcessQueryService().GetProcessDefinition(processDefinitionId);
        // ActivityImpl activityImpl =
        // processDefinition.findActivity(multiNodeId);
        //
        // List<Task> tasks =
        // ApplicationContextHelper.getTaskService().createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(multiNodeId).orderByTaskCreateTime()
        // .desc().list();
        // TaskServiceImplExt taskServiceImplExt =
        // (TaskServiceImplExt)ApplicationContextHelper.getBean("taskServiceImplExt");
        // Map<String, Object> variables = new HashMap<String, Object>();
        // variables.put(WorkflowConstant.MULTI_COMPLETE_MARK, true);
        //
        // if(!StringUtils.isEmpty(processParam)){
        // JSONObject jSONObject = new JSONObject(processParam);
        // Iterator keys = jSONObject.keys();
        // while (keys.hasNext()) {
        // String key = (String) keys.next();
        // if(StringUtils.isNotEmpty(jSONObject.getString(key))){
        // if(key.equals(WorkflowConstant.NEXT_HANDLERS)){
        // String nextHandlers = jSONObject.getString(key);
        // List nextHandlerList = StringUtil.isEmpty(nextHandlers)?
        // Collections.EMPTY_LIST : Arrays.asList(nextHandlers.split(","));
        // variables.put(key,nextHandlerList);
        // }else{
        // variables.put(key, jSONObject.getString(key));
        // }
        // }
        // }
        // }
        //
        // taskServiceImplExt.setVariableLocal(tasks.get(0).getId(),WorkflowConstant.IS_AUTO_LOG,"0");
        // //taskServiceImplExt.claim(tasks.get(0).getId(), currentUser);
        // taskServiceImplExt.completeNoListener(tasks.get(0).getId(),
        // variables,false);
        // //ApplicationContextHelper.getProcessFormService().updateProcessForm(processInstanceId);
        // logCloseCounterSign(processInstanceId,taskId,(String)activityImpl.getProperty("name"));
    }

    // private void logCloseCounterSign(String processInstanceId,String taskId,
    // String multiNodeName) {
    // StringBuffer logOperate = new StringBuffer();
    // logOperate.append("结束会签节点[");
    // logOperate.append(multiNodeName);
    // logOperate.append("].");
    // ApplicationContextHelper.getWorkflowLogService().logCloseCounterSign(processInstanceId,taskId,logOperate.toString());
    // }

    @SuppressWarnings("rawtypes")
    private void getUserList() {
        Element dataElement = xmlDocUtil.getRequestData();
        String deptid = dataElement.getChildTextTrim("deptid");
        String usercode = dataElement.getChildTextTrim("usercode");
        String username = dataElement.getChildTextTrim("username");
        String deptname = dataElement.getChildTextTrim("deptname");
        String userInfo = dataElement.getChildTextTrim("userInfo");// 查询用户账号或者名称

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            StringBuffer sql = new StringBuffer();
            ArrayList bvals = new ArrayList();
            sql.append("SELECT a.*, b.deptname, b.deptcode ");
            sql.append(" FROM pcmc_user a, pcmc_dept b, pcmc_user_dept t ");
            sql.append(" WHERE a.userid = t.userid ");
            sql.append(" and t.deptid = b.deptid ");
            if (StringUtil.isNotEmpty(deptid)) {
                sql.append("and a.deptid=? ");
                bvals.add(SqlTypes.getConvertor("long").convert(deptid, null));
            }
            if (StringUtil.isNotEmpty(usercode)) {
                sql.append("and a.usercode=? ");
                bvals.add(usercode);
            }
            if (StringUtil.isNotEmpty(username)) {
                sql.append(" and a.username like ?");
                bvals.add("%" + username + "%");
            }
            if (deptname != null) {
                sql.append(" and b.deptname like ?");
                bvals.add("%" + deptname + "%");
            }
            if (!StringUtils.isEmpty(userInfo)) {
                sql.append("   AND (upper(a.usercode) = ? or upper(a.username) = ?)");
                bvals.add(userInfo.toUpperCase());
                bvals.add(userInfo.toUpperCase());
            }

            sql.append(xmlDocUtil.getOrderBy(" order by a.usercode"));

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            xmlDocUtil.setResult("-1");
        } finally {
            dao.releaseConnection();
        }
    }

    private boolean validRollback() throws Exception {
        boolean isMeet = true;
        Element dataElement = xmlDocUtil.getRequestData();
        String taskDefKey = dataElement.getChildTextTrim("taskDefKey");
        String taskId = dataElement.getChildTextTrim("taskId");
        // String processInstanceId
        // =dataElement.getChildTextTrim("processInstanceId");
        String processDefinitionId = dataElement.getChildTextTrim("processDefinitionId");

        ProcessDefinitionEntity processDefinitionEntity = ApplicationContextHelper.getManagementService()
            .executeCommand(new GetDeploymentProcessDefinitionCmd(processDefinitionId));

        ActivityImpl currentActivityImpl = processDefinitionEntity.findActivity(taskDefKey);
        if (currentActivityImpl.getProperty("type").equals(WorkflowConstant.USERTASKTYPE) && (null != currentActivityImpl.getProperty("multiInstance"))) {
            isMeet = false;
        }

        // String sourceActivityKey = (String)
        // ApplicationContextHelper.getRuntimeService().getVariable(processInstanceId,"to->"
        // + taskDefKey);
        // sourceActivityKey =
        // sourceActivityKey.substring(6,sourceActivityKey.length());
        String sourceActivityKey = findNearestUserTask(taskId);
        if (StringUtils.isEmpty(sourceActivityKey)) {
            return false;
        }
        ActivityImpl sourceActivityImpl = processDefinitionEntity.findActivity(sourceActivityKey);

        if (!sourceActivityImpl.getProperty("type").equals(WorkflowConstant.USERTASKTYPE)) {
            isMeet = false;
        } else {
            if (null != sourceActivityImpl.getProperty("multiInstance")) {
                isMeet = false;
            }
        }
        return isMeet;
    }

    public String findNearestUserTask(final String taskId) {
        return ApplicationContextHelper.getManagementService().executeCommand(new Command<String>() {
            public String execute(CommandContext commandContext) {
                TaskEntity taskEntity = (TaskEntity) ApplicationContextHelper.getTaskService().createTaskQuery().taskId(taskId).singleResult();

                if (taskEntity == null) {
                    // log4j.debug("cannot find task : {}", taskId);
                    return null;
                }

                Graph graph = new ActivitiHistoryGraphBuilder(taskEntity.getProcessInstanceId()).build();
                JdbcTemplate jdbcTemplate = ApplicationContextHelper.getBean(JdbcTemplate.class);
                String historicActivityInstanceId = jdbcTemplate.queryForObject(
                    "select id_ from ACT_HI_ACTINST where task_id_=?", String.class, taskId);
                Node node = graph.findById(historicActivityInstanceId);

                String previousHistoricActivityInstanceId = findIncomingNode(graph, node);

                if (previousHistoricActivityInstanceId == null) {
                    // logger.debug(
                    // "cannot find previous historic activity instance : {}",
                    // taskEntity);
                    return null;
                }
                return jdbcTemplate.queryForObject("select act_id_ from ACT_HI_ACTINST where id_=?",
                    String.class, previousHistoricActivityInstanceId);
            }
        });
    }

    private String findIncomingNode(Graph graph, Node node) {
        for (Edge edge : graph.getEdges()) {
            Node src = edge.getSrc();
            Node dest = edge.getDest();
            String srcType = src.getType();

            if (!dest.getId().equals(node.getId())) {
                continue;
            }
            if ("userTask".equals(srcType)) {
                return src.getId();
            } else if (srcType.endsWith("Gateway")) {
                return this.findIncomingNode(graph, src);
            } else {
                return null;
            }
        }
        return null;
    }

    private void getLastDelegateUsers() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processInstanceId = dataElement.getChildText("processInstanceId");
        String taskId = dataElement.getChildText("taskId");
        String delegateUser = dataElement.getChildText("delegateUser");
        // String processDefKey = dataElement.getChildText("processDefKey");
        // String taskDefKey = dataElement.getChildText("taskDefKey");
        // String dealWay = dataElement.getChildText("dealWay");
        // String businessKey = dataElement.getChildText("businessKey");

        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT id,");
        sql.append("  processInstanceId,");
        sql.append(" taskId,");
        sql.append(" taskDefKey,");
        sql.append(" original_user,");
        sql.append(" original_user_name,");
        sql.append(" delegate_user,");
        sql.append("  delegate_user_name,");
        sql.append("   delegate_process_key,");
        sql.append("   delegate_process_name,");
        sql.append("  delegate_type,");
        sql.append("  isRunning,");
        sql.append("  create_people,");
        sql.append("  create_time");
        sql.append("  FROM workflow_hi_delegate");
        sql.append(" WHERE     processInstanceId = ?");
        sql.append("AND taskId = ?");
        sql.append("AND delegate_user in (" + convertUsers(delegateUser) + ")");
        // sql.append(" AND (delegate_type = ? OR delegate_type = ?)");
        sql.append(" AND isRunning = '1'");
        sql.append(" AND create_time = (SELECT max(create_time)");
        sql.append(" FROM workflow_hi_delegate t");
        sql.append("  WHERE t.processInstanceId = ? AND t.taskId = ?)");

        Map<String, List<DelegateUserVO>> delegateUserMap = new HashMap<String, List<DelegateUserVO>>();
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(
            sql.toString(), processInstanceId, taskId, processInstanceId, taskId);

        for (Map<String, Object> map : list) {
            String original_user = map.get("original_user").toString();
            String original_user_name = map.get("original_user_name").toString();
            String delegate_user = map.get("delegate_user").toString();
            String delegate_user_name = map.get("delegate_user_name").toString();

            DelegateUserVO delegateUserVO = new DelegateUserVO();
            delegateUserVO.setUserId(original_user);
            delegateUserVO.setUserName(original_user_name);

            UserVO userVO = new UserVO();
            userVO.setUserId(delegate_user);
            userVO.setUserName(delegate_user_name);
            delegateUserVO.setDelegatedUser(userVO);

            List<DelegateUserVO> delegateUserVOList = new ArrayList<DelegateUserVO>();
            if (!CollectionUtils.isEmpty(delegateUserMap.get(delegate_user))) {
                delegateUserVOList = delegateUserMap.get(delegate_user);
            }

            delegateUserVOList.add(delegateUserVO);
            delegateUserMap.put(delegate_user, delegateUserVOList);
        }

        List<UserVO> users = ApplicationContextHelper.getUserQueryService().getMultiUserVO(Arrays.asList(delegateUser.split(",")));
        Element data = new Element("Data");
        for (UserVO userVO : users) {
            Element record = new Element("Record");
            // 每个代理人的所有被代理人集合
            List<DelegateUserVO> delegatedUserList = delegateUserMap.get(userVO.getUserId());
            if (!CollectionUtils.isEmpty(delegatedUserList)) {
                Element handlers = new Element("originalUsers");
                Element dataHandlers = new Element("Data");
                for (DelegateUserVO delegateUserVO : delegatedUserList) {
                    Element recordHandler = new Element("Record");
                    XmlDocPkgUtil.setChildText(recordHandler, "userId", "" + delegateUserVO.getUserId());
                    XmlDocPkgUtil.setChildText(recordHandler, "userCode", "" + delegateUserVO.getUserCode());
                    XmlDocPkgUtil.setChildText(recordHandler, "userName", "" + delegateUserVO.getUserName());
                    dataHandlers.addContent(recordHandler);
                }
                handlers.addContent(dataHandlers);
                record.addContent(handlers);

                // XmlDocPkgUtil.setChildText(record, "userId",
                // delegatedUser.getUserId());
                // XmlDocPkgUtil.setChildText(record, "userCode",
                // delegatedUser.getUserCode());
                // XmlDocPkgUtil.setChildText(record, "userName",
                // delegatedUser.getUserName() );
                XmlDocPkgUtil.setChildText(record, "delegateUserId", userVO.getUserId());
                XmlDocPkgUtil.setChildText(record, "delegateUserCode", userVO.getUserCode());
                XmlDocPkgUtil.setChildText(record, "delegateUserName", userVO.getUserName());
            } else {
                // XmlDocPkgUtil.setChildText(record, "userId",
                // userVO.getUserId());
                // XmlDocPkgUtil.setChildText(record, "userCode",
                // userVO.getUserCode());
                // XmlDocPkgUtil.setChildText(record, "userName",
                // userVO.getUserName() );
                XmlDocPkgUtil.setChildText(record, "delegateUserId", userVO.getUserId());
                XmlDocPkgUtil.setChildText(record, "delegateUserCode", userVO.getUserCode());
                XmlDocPkgUtil.setChildText(record, "delegateUserName", userVO.getUserName());
            }
            data.addContent(record);
        }
        // PlatformDao dao = new PlatformDao();
        // Element resultElement = null;
        // try {

        // ArrayList<String> bvals = new ArrayList<String>();
        // bvals.add(processInstanceId);
        // bvals.add(taskId);
        // bvals.add(taskId);
        // bvals.add(processInstanceId);
        // bvals.add(taskId);
        // dao.setSql(sql.toString());
        // dao.setBindValues(bvals);
        // resultElement = dao.executeQuerySql(-1, 1);

        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
        // } catch (Exception e) {
        // e.printStackTrace();
        // } finally {
        // dao.releaseConnection();
        // }
    }

    private void queryTransferUsers() {
        Element dataElement = xmlDocUtil.getRequestData();
        String isPage = dataElement.getChildTextTrim("isPage");
        String userInfo = dataElement.getChildTextTrim("userInfo");// 查询用户账号或者名称
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        PlatformDao dao = new PlatformDao();

        Element resultElement = null;
        try {
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT a.*, ");
            sql.append("  t.deptid,");
            sql.append("  b.deptname,");
            sql.append("   b.deptcode");
            sql.append(" FROM pcmc_user_dept t, pcmc_user a , pcmc_dept b ");
            sql.append("WHERE     t.deptid IN (SELECT a.deptid ");
            sql.append("      FROM pcmc_user_dept a ");
            sql.append("   WHERE a.userid = ?)");
            sql.append("   AND t.userid = a.userid");
            sql.append("   AND t.deptid = b.deptid");
            sql.append("   AND t.userid != ?");
            bvals.add(userid);
            bvals.add(userid);
            if (!StringUtils.isEmpty(userInfo)) {
                sql.append("   AND a.usercode = ? or a.username = ?");
                bvals.add(userInfo);
                bvals.add(userInfo);
            }
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            if (!StringUtils.isEmpty(isPage)) {
                resultElement = dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
            } else {
                resultElement = dao.executeQuerySql(-1, 1);
            }

            xmlDocUtil.getResponse().addContent(resultElement);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            e.printStackTrace();
            log4j.logError("[查询用户发生异常.]" + e.getMessage());
        } finally {
            dao.releaseConnection();
        }
    }

    private String convertUsers(String delegateUser) {
        String[] users = delegateUser.split(",");
        Set<String> userVOs = new HashSet<String>();
        for (String user : users) {
            userVOs.add("'" + user + "'");
        }
        return org.springframework.util.StringUtils.collectionToDelimitedString(userVOs, ",");
    }

    /*
     * private void queryCurrentDeptUsers() { String userid = (String)
     * ApplicationContext.getRequest().getSession().getAttribute("userid");
     * PlatformDao dao = new PlatformDao(); Element resultElement = null; try {
     * ArrayList<String> bvals = new ArrayList<String>(); StringBuffer sql = new
     * StringBuffer(""); sql.append("SELECT a.* ");
     * sql.append(" FROM pcmc_user_dept t, pcmc_user a ");
     * sql.append("WHERE     t.deptid IN (SELECT a.deptid ");
     * sql.append("      FROM pcmc_user_dept a ");
     * sql.append("   WHERE a.userid = ?)");
     * sql.append("   AND t.userid = a.userid");
     * sql.append("   AND t.userid != ?"); bvals.add(userid); bvals.add(userid);
     * dao.setSql(sql.toString()); dao.setBindValues(bvals); resultElement =
     * dao.executeQuerySql(xmlDocUtil.getPageSize(), xmlDocUtil.getPageNo());
     * xmlDocUtil.getResponse().addContent(resultElement) ;
     * xmlDocUtil.setResult("0"); // if
     * (resultElement.getChildren("Record").size() == 0) { // return
     * Collections.EMPTY_SET; // } } catch (Exception e) { e.printStackTrace();
     * // log4j.logError("[更新用户发生异常.]"+e.getMessage()); //
     * log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(), // "GBK", true)); //
     * xmlDocUtil.writeErrorMsg("10614","修改密码失败"); } finally {
     * dao.releaseConnection(); } }
     */

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentParam() {
        return componentParam;
    }

    public void setComponentParam(String componentParam) {
        this.componentParam = componentParam;
    }

    public FunctionComponentService getFunctionComponentService() {
        return functionComponentService;
    }

    public void setFunctionComponentService(FunctionComponentService functionComponentService) {
        this.functionComponentService = functionComponentService;
    }

    /**
     * 查询流程实例所有的会签节点,没用了
     */
    private void queryCounterNodes() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processInstanceId = dataElement.getChildTextTrim("query_processInstanceId");
        String processDefinitionId = dataElement.getChildTextTrim("query_processDefId");

        ProcessDefinitionEntity processDefinitionEntity = ApplicationContextHelper.getManagementService()
            .executeCommand(new GetDeploymentProcessDefinitionCmd(processDefinitionId));
        List<String> activityIds = ApplicationContextHelper.getRuntimeService().getActiveActivityIds(processInstanceId);

        Element data = new Element("Data");
        Element record = new Element("Record");
        for (String activityId : activityIds) {
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
            if (activityImpl.getProperty("type").equals(WorkflowConstant.USERTASKTYPE)
                && (null != activityImpl.getProperty("multiInstance"))) {
                MultiInstanceActivityBehavior multiInstanceActivityBehavior = (MultiInstanceActivityBehavior) activityImpl
                    .getActivityBehavior();
                UserTaskConfigVO userTaskVO = new UserTaskConfigVO();
                userTaskVO.setId(activityImpl.getId());
                userTaskVO.setName((String) activityImpl.getProperty("name"));
                userTaskVO.setType("userTask");
                userTaskVO.setCollectionElementVariable(multiInstanceActivityBehavior
                    .getCollectionElementVariable());
                userTaskVO.setCollectionVariable(multiInstanceActivityBehavior.getCollectionVariable());

                XmlDocPkgUtil.setChildText(record, "id", activityImpl.getId());
                XmlDocPkgUtil.setChildText(record, "name", (String) activityImpl.getProperty("name"));
                XmlDocPkgUtil.setChildText(record, "type", "userTask");
                XmlDocPkgUtil.setChildText(record, "collectionElementVariable",
                    multiInstanceActivityBehavior.getCollectionElementVariable());
                XmlDocPkgUtil.setChildText(record, "collectionVariable",
                    multiInstanceActivityBehavior.getCollectionVariable());
            }
        }
        data.addContent(record);
        xmlDocUtil.getResponse().addContent(data);
        xmlDocUtil.setResult("0");
    }

    private void counterSignOperate() throws Exception {
        Element dataElement = xmlDocUtil.getRequestData();
        CounterSignVO counterSignVO = createCounterSignVO(dataElement);

        functionComponentService.counterSignOperate(counterSignVO);
        xmlDocUtil.setResult("0");
        xmlDocUtil.writeHintMsg("10609", "加签减签成功");

        /*
         * Set<String> counterUsers = getCounterUsers(counterSignVO); String
         * processInstanceId = counterSignVO.getProcessInstanceId();
         * ICounterFunctionNotifyListener counterFunctionNotifyListener =
         * getCounterFunctionNotifyListener(counterSignVO); if(null
         * !=counterFunctionNotifyListener){
         * counterFunctionNotifyListener.notifyBeforeEvent(counterSignVO); }
         * ApplicationContextHelper.getManagementService().executeCommand( new
         * CounterSignCmd(counterSignVO.getCounterSignOperate(),
         * counterSignVO.getActivityId(), counterUsers, processInstanceId,
         * counterSignVO.getCollectionVariable(),
         * counterSignVO.getCollectionElementVariable()));
         * ApplicationContextHelper
         * .getProcessFormService().updateProcessForm(processInstanceId);
         * if(null !=counterFunctionNotifyListener){
         * counterFunctionNotifyListener.notifyBeforeEvent(counterSignVO); }
         * if(counterSignVO.getMultiInstance().equals("sequential")){ String
         * activityId = counterSignVO.getActivityId(); RuntimeService
         * runtimeService = ApplicationContextHelper.getRuntimeService();
         * List<String> serialRemoveUsers=
         * (List<String>)runtimeService.getVariable(processInstanceId,
         * WorkflowConstant.TASK_SERIAL_REMOVE_USER+activityId); List<String>
         * serialAddUsers=
         * (List<String>)runtimeService.getVariable(processInstanceId,
         * WorkflowConstant.TASK_SERIAL_ADD_USER+activityId);
         * if(counterSignVO.getCounterSignOperate().equals("add")){
         * serialAddUsers.removeAll(counterUsers);
         * serialRemoveUsers.addAll(counterUsers); }else{
         * serialAddUsers.addAll(counterUsers);
         * serialRemoveUsers.removeAll(counterUsers); }
         * runtimeService.setVariable(processInstanceId,
         * WorkflowConstant.TASK_SERIAL_REMOVE_USER+activityId,
         * serialRemoveUsers); runtimeService.setVariable(processInstanceId,
         * WorkflowConstant.TASK_SERIAL_ADD_USER+activityId, serialAddUsers); }
         * logCounterSign(counterSignVO);
         */
    }

    private ICounterFunctionNotifyListener getCounterFunctionNotifyListener(CounterSignVO counterSignVO) {
        String processDefKey = counterSignVO.getProcessDefKey();
        String componentListenerBeanName = processDefKey + "counterSignListener";
        ICounterFunctionNotifyListener counterFunctionNotifyListener = null;
        try {
            counterFunctionNotifyListener = ApplicationContextHelper.getBean(componentListenerBeanName,
                ICounterFunctionNotifyListener.class);
        } catch (NoSuchBeanDefinitionException e) {
            // e.printStackTrace();
        }
        return counterFunctionNotifyListener;
    }

    private CounterSignVO createCounterSignVO(Element dataElement) {
        String counterSignOperate = dataElement.getChildTextTrim("counterSignOperate");
        String processInstanceId = dataElement.getChildTextTrim("processInstanceId");
        String processDefKey = dataElement.getChildTextTrim("processDefKey");
        String processDefName = dataElement.getChildTextTrim("processDefName");
        String componentParam = dataElement.getChildTextTrim("componentParam");
        int multiKind = Integer.parseInt(dataElement.getChildTextTrim("multiKind"));
        String multiInstance = dataElement.getChildTextTrim("multiInstance");

        JSONObject jSONObject = new JSONObject(componentParam);
        String collectionVariable = jSONObject.getString("collectionVariable");
        String collectionElementVariable = jSONObject.getString("collectionElementVariable");
        String activityId = jSONObject.getString("counterSignNodeId");

        CounterSignVO counterSignVO = new CounterSignVO();
        counterSignVO.setCollectionElementVariable(collectionElementVariable);
        counterSignVO.setCollectionVariable(collectionVariable);
        counterSignVO.setCounterSignOperate(counterSignOperate);
        counterSignVO.setProcessInstanceId(processInstanceId);
        counterSignVO.setActivityId(activityId);
        counterSignVO.setMultiKind(multiKind);
        counterSignVO.setProcessDefKey(processDefKey);
        counterSignVO.setProcessDefName(processDefName);
        counterSignVO.setMultiInstance(multiInstance);
        if (multiKind == UserTaskRemindVO.DEPARTMENT_MULTI) {
            String[] counterDepartment = jSONObject.getString("counterDepartmentId").split(",");
            CounterSignDepartmentVO counterSignDepartmentVO = new CounterSignDepartmentVO();
            BeanUtils.copyProperties(counterSignVO, counterSignDepartmentVO);
            List<DepartmentVO> departmentVOs = ApplicationContextHelper.getDepartmentQueryService()
                .getMultiDepartmentVO(Arrays.asList(counterDepartment));
            counterSignDepartmentVO.setDepartmentVOs(departmentVOs);
            return counterSignDepartmentVO;
        } else {
            String[] counterUsers = jSONObject.getString("counterUserId").split(",");
            CounterSignUserVO counterSignUserVO = new CounterSignUserVO();
            BeanUtils.copyProperties(counterSignVO, counterSignUserVO);
            counterSignUserVO.setUsers(Arrays.asList(counterUsers));
            return counterSignUserVO;
        }
    }

    private Set<String> getCounterUsers(CounterSignVO counterSignVO) {
        Set<String> users = null;
        if (counterSignVO.getMultiKind() == UserTaskRemindVO.DEPARTMENT_MULTI) {
            CounterSignDepartmentVO counterSignDepartmentVO = (CounterSignDepartmentVO) counterSignVO;
            users = ApplicationContextHelper.getWorkflowDefExtService().findWorkflowRoleUsers(
                counterSignVO.getProcessDefKey(), counterSignVO.getActivityId(),
                new HashSet<DepartmentVO>(counterSignDepartmentVO.getDepartmentVOs()));
        } else {
            CounterSignUserVO counterSignUserVO = (CounterSignUserVO) counterSignVO;
            users = new HashSet(counterSignUserVO.getUsers());
        }
        if (counterSignVO.getCounterSignOperate().equals("add")) {
            Map<String, DelegateUserVO> configMap = ApplicationContextHelper.getWorkflowDefExtService()
                .findDelegateTaskList(users, counterSignVO.getProcessDefKey());
            // ApplicationContextHelper.getWorkflowDefExtService().insertProcessDelegateHistory(configMap,counterSignVO.getProcessInstanceId(),counterSignVO.getProcessDefKey(),counterSignVO.getProcessDefName(),WorkflowConstant.TASK_DELEGATE_ADDMULTIHANDLER);
            // counterSignVO.setAddDelegateMap(configMap);
            List<String> userDelegates = ApplicationContextHelper.getWorkflowDefExtService()
                .getFinalDelegateUsers(new ArrayList<String>(users), configMap);
            return new HashSet<String>(userDelegates);
        }
        return users;
    }

    private void logCounterSign(CounterSignVO counterSignVO) {
        String counterLogOperate = "";
        String counterSignOperate = counterSignVO.getCounterSignOperate();
        if (counterSignVO.getMultiKind() == UserTaskRemindVO.DEPARTMENT_MULTI) {
            CounterSignDepartmentVO counterSignDepartmentVO = (CounterSignDepartmentVO) counterSignVO;
            counterLogOperate = getCounterSignOperate(counterSignOperate,
                counterSignDepartmentVO.getDepartmentVOs());
        } else {
            CounterSignUserVO counterSignUserVO = (CounterSignUserVO) counterSignVO;
            counterLogOperate = getUserCounterLogOperate(counterSignUserVO);
        }
        ApplicationContextHelper.getWorkflowLogService().recordCounterSignLog(
            counterSignVO.getProcessInstanceId(), counterLogOperate);
    }

    private String getUserCounterLogOperate(CounterSignUserVO counterSignUserVO) {
        List<String> users = counterSignUserVO.getUsers();
        String counterSignOperate = counterSignUserVO.getCounterSignOperate();
        StringBuffer logOperate = new StringBuffer();
        List<UserVO> userVOs = ApplicationContextHelper.getUserQueryService().getMultiUserVO(users);
        if (counterSignOperate.equalsIgnoreCase("add")) {
            logOperate.append("新增会签人[");
            int i = 0;
            for (UserVO userVO : userVOs) {
                Map<String, DelegateUserVO> configMap = ApplicationContextHelper.getWorkflowDefExtService()
                    .findDelegateTaskList(new HashSet<String>(users), counterSignUserVO.getProcessDefKey());
                DelegateUserVO delegateUserVO = configMap.get(userVO.getUserId());

                if (null != delegateUserVO) {
                    logOperate.append(delegateUserVO.getDelegatedUser().getUserName() + "代理("
                        + delegateUserVO.getUserName() + ")");
                } else {
                    logOperate.append(userVO.getUserName());
                }
                if (i < (userVOs.size() - 1)) {
                    logOperate.append(",");
                }
            }
            // addCounterUsers(userVOs, logOperate);
            // logOperate.append("].");

        } else if (counterSignOperate.equalsIgnoreCase("remove")) {
            logOperate.append("删除会签人[");
            addCounterUsers(userVOs, logOperate);
            logOperate.append("].");
        }
        return logOperate.toString();
    }

    private void addCounterUsers(List<UserVO> userVOs, StringBuffer logOperate) {
        int i = 0;
        for (UserVO userVO : userVOs) {
            logOperate.append(userVO.getUserName());
            if (i < (userVOs.size() - 1)) {
                logOperate.append(",");
            }
        }
    }

    private String getCounterSignOperate(String counterSignOperate, List<DepartmentVO> counterDepartments) {
        StringBuffer logOperate = new StringBuffer();
        if (counterSignOperate.equalsIgnoreCase("add")) {
            logOperate.append("新增会签部门[");
            addCounterDepartments(counterDepartments, logOperate);
            logOperate.append("].");

        } else if (counterSignOperate.equalsIgnoreCase("remove")) {
            logOperate.append("删除会签部门[");
            addCounterDepartments(counterDepartments, logOperate);
            logOperate.append("].");
        }
        return logOperate.toString();
    }

    private void addCounterDepartments(List<DepartmentVO> counterDepartments, StringBuffer logOperate) {
        int i = 0;
        for (DepartmentVO departmentVO : counterDepartments) {
            logOperate.append(departmentVO.getDeptName());
            if (i < (counterDepartments.size() - 1)) {
                logOperate.append(",");
            }
        }
    }
}
