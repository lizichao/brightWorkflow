package cn.com.bright.workflow.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.collections.CollectionUtils;
import org.jdom.Element;
import org.springframework.util.StringUtils;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.com.bright.workflow.api.vo.ComponentVO;
import cn.com.bright.workflow.api.vo.DelegateUserVO;
import cn.com.bright.workflow.api.vo.DepartmentVO;
import cn.com.bright.workflow.api.vo.RoleVO;
import cn.com.bright.workflow.api.vo.UserTaskRemindVO;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.api.vo.WorkflowDefExtVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.ComponentHelper;
import cn.com.bright.workflow.util.DateUtil;

public class WorkflowDefExtService {
    public static final String DEPARTMENT_LEVEL = "departmentlevel";// 平级
    public static final String DEPARTMENT_SUPERIOR = "departmentSuperior";// 上级
    public static final String DEPARTMENT_OPTIONAL = "departmentOptional";// 任选
    public static final String DEPARTMENT_CURRENT = "departmentCurrent";// 当前

    public WorkflowDefExtVO findWorkflowConfig(String processDefKey) {
        WorkflowDefExtVO workflowDefExtVO = new WorkflowDefExtVO();
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        Element resultElement = null;
        try {
            ArrayList<String> bvals = new ArrayList<String>();
            sql.append("select * from workflow_process_edit where processdefkey=?");
            bvals.add(processDefKey);
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            if (resultElement.getChildren("Record").size() > 0) {
                String processformkey = resultElement.getChild("Record").getChildTextTrim("processformkey");
                String reFillNodeId = resultElement.getChild("Record").getChildTextTrim("reFillNodeId");
                workflowDefExtVO.setProcessDefKey(processDefKey);
                workflowDefExtVO.setProcessformkey(processformkey);
                workflowDefExtVO.setReFillNodeId(reFillNodeId);
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
        return workflowDefExtVO;
    }

    /**
     * 查找节点配置的所有角色
     * @param processDefKey
     * @param taskDefKey
     * @return
     */
    public List<RoleVO> findWorkflowRoleConfig(String processDefKey, String taskDefKey) {
        PlatformDao dao = new PlatformDao();
        Element resultElement = null;
        List<RoleVO> roleList = new ArrayList<RoleVO>();
        try {
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sql = new StringBuffer("");
            sql.append("select * from workflow_node_role where processdefkey=? and taskdefkey = ?");
            bvals.add(processDefKey);
            bvals.add(taskDefKey);
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            if (resultElement.getChildren("Record").size() == 0) {
                return Collections.EMPTY_LIST;
            }
            List<Element> roles = resultElement.getChildren("Record");
            StringBuffer roleBuffer = new StringBuffer();
            int i = 0;
            for (Element roleElement : roles) {
                i++;
                roleBuffer.append("'" + roleElement.getChildTextTrim("roleid") + "'");
                if (i < roles.size()) {
                    roleBuffer.append(",");
                }
            }
            sql.setLength(0);
            bvals = new ArrayList<String>();
            sql.append("SELECT t.* ");
            sql.append("FROM pcmc_role t ");
            sql.append("WHERE t.roleid in (");
            sql.append(roleBuffer.toString());
            sql.append(")");
            // bvals.add(roleElement.getChildText("roleid"));

            dao.setSql(sql.toString());
            // dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            List<Element> roleElements = resultElement.getChildren("Record");
            for (Element roleElement : roleElements) {
                RoleVO roleVO = new RoleVO();
                roleVO.setRoleId(roleElement.getChildTextTrim("roleid"));
                roleVO.setRoleName(roleElement.getChildTextTrim("rolename"));
                roleVO.setSubsysId(roleElement.getChildTextTrim("subsysid"));
                roleList.add(roleVO);
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
        return roleList;
    }

    public List<String> findWorkflowRoleConfigStr(String processDefKey, String taskDefKey) {
        List<RoleVO> resultRoleVOs = this.findWorkflowRoleConfig(processDefKey, taskDefKey);
        List<String> roles = new ArrayList<String>();
        for (RoleVO roleVO : resultRoleVOs) {
            roles.add(roleVO.getRoleId());
        }
        return roles;
    }

    public Set<DepartmentVO> findWorkflowDepartmentConfig(String processDefKey, String taskDefKey) {
        PlatformDao dao = new PlatformDao();
        Element resultElement = null;
        List<DepartmentVO> departmentVOList = new ArrayList<DepartmentVO>();
        try {
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sql = new StringBuffer("");
            sql.append("select * from workflow_node_department where processdefkey=? and taskdefkey = ?");
            bvals.add(processDefKey);
            bvals.add(taskDefKey);
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            List<Element> departments = resultElement.getChildren("Record");
            if (departments.size() == 0) {
                return Collections.EMPTY_SET;
            }

            List<String> departmentIds = new ArrayList<String>();
            // StringBuffer departmentBuffer = new StringBuffer() ;
            for (Element departmentElement : departments) {
                String deptid = departmentElement.getChildTextTrim("deptid");
                if (deptid.equals(DEPARTMENT_LEVEL) || deptid.equals(DEPARTMENT_SUPERIOR)
                    || deptid.equals(DEPARTMENT_OPTIONAL) || deptid.equals(DEPARTMENT_CURRENT)) {
                    invariantDeal(deptid, departmentIds);
                } else {
                    departmentIds.add("'" + deptid + "'");
                }
                // departmentBuffer.append("'"+departmentElement.getChildTextTrim("deptid")+"'");
            }
            if (CollectionUtils.isEmpty(departmentIds)) {
                return Collections.EMPTY_SET;
            }

            // for (Element departmentElement : departments) {
            sql.setLength(0);
            bvals = new ArrayList<String>();
            // String deptid = departmentElement.getChildText("deptid");
            // if(deptid.equals(WorkflowDefExtService.DEPARTMENT_LEVEL)){

            // }
            sql.append("select * from pcmc_dept where deptid in ( ");
            sql.append(StringUtils.arrayToCommaDelimitedString(StringUtils.toStringArray(departmentIds)));
            sql.append(")");
            // bvals.add(deptid);
            dao.setSql(sql.toString());
            // dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);

            List<Element> departmentElements = resultElement.getChildren("Record");
            for (Element department : departmentElements) {
                DepartmentVO departmentVO = new DepartmentVO();
                departmentVO.setDeptId(department.getChildText("deptid"));
                departmentVO.setDeptCode(department.getChildText("deptcode"));
                departmentVO.setDeptName(department.getChildText("deptname"));
                departmentVOList.add(departmentVO);
            }

            // }
        } catch (Exception e) {
            e.printStackTrace();
            // log4j.logError("[更新用户发生异常.]"+e.getMessage());
            // log4j.logInfo(JDomUtil.toXML(xmlDocUtil.getXmlDoc(),
            // "GBK", true));
            // xmlDocUtil.writeErrorMsg("10614","修改密码失败");
        } finally {
            dao.releaseConnection();
        }
        return new HashSet(departmentVOList);
    }

    public List<String> findWorkflowDepartmentConfigStr(String processDefKey, String taskDefKey) {
        Set<DepartmentVO> departmentVOs = this.findWorkflowDepartmentConfig(processDefKey, taskDefKey);
        List<String> departments = new ArrayList<String>();
        for (DepartmentVO departmentVO : departmentVOs) {
            departments.add(departmentVO.getDeptId());
        }
        return departments;
    }

    private void invariantDeal(String deptid, List<String> departmentIds) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        String currentDeptid = (String) ApplicationContext.getRequest().getSession().getAttribute("deptid");
        // List<String> departmentIds = new ArrayList<String>();
        PlatformDao dao = new PlatformDao();
        ArrayList<String> bvals = new ArrayList<String>();
        StringBuffer sql = new StringBuffer("");
        if (deptid.equals(DEPARTMENT_LEVEL)) {
            sql.append("SELECT *");
            sql.append("  FROM pcmc_dept a");
            sql.append(" WHERE a.levels = (SELECT b.levels");
            sql.append("     FROM pcmc_dept b");
            sql.append("   WHERE b.state > 0 AND b.deptid = ?)");
            bvals.add(currentDeptid);
        } else if (deptid.equals(DEPARTMENT_SUPERIOR)) {
            sql.append("SELECT *");
            sql.append(" FROM pcmc_dept a");
            sql.append(" WHERE a.deptid = (SELECT b.pdeptid");
            sql.append(" FROM pcmc_dept b");
            sql.append(" WHERE b.state > 0 AND b.deptid = ?)");
            bvals.add(currentDeptid);
        } else if (deptid.equals(DEPARTMENT_OPTIONAL)) {
            sql.append("SELECT *");
            sql.append(" FROM pcmc_dept ");

        } else if (deptid.equals(DEPARTMENT_CURRENT)) {
            sql.append("SELECT *");
            sql.append(" FROM pcmc_dept a");
            sql.append(" WHERE a.deptid = ?");
            bvals.add(currentDeptid);
        }

        try {
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1, 1);
            List departments = resultElement.getChildren("Record");

            for (int i = 0; i < departments.size(); i++) {
                Element rRec = (Element) departments.get(i);
                departmentIds.add("'" + rRec.getChildText("deptid") + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }

    }

    public List<ComponentVO> findWorkflowComponentConfig(String processDefKey, String taskDefKey) {
        PlatformDao dao = new PlatformDao();

        Element resultElement = null;
        List<ComponentVO> componentList = new ArrayList<ComponentVO>();
        try {
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sql = new StringBuffer("");
            sql.append("select * from workflow_node_component where processdefkey=? and taskdefkey = ?");
            bvals.add(processDefKey);
            bvals.add(taskDefKey);
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            List components = resultElement.getChildren("Record");
            if (components.size() == 0) {
                return Collections.EMPTY_LIST;
            }

            ComponentHelper componentHelper = new ComponentHelper();
            for (int i = 0; i < components.size(); i++) {
                Element rRec = (Element) components.get(i);
                String componentId = rRec.getChildText("id");
                String componentType = rRec.getChildText("component_type");
                componentList.add(componentHelper.findComponent(componentId, componentType));
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
        return componentList;
    }

    /*
     * 查询用户节点根据配置的部门和角色查询出来的用户集合
     */
    public Set<String> getUserTaskHandlers(String processDefKey, String taskDefKey) {
        Set<DepartmentVO> departmentSet = this.findWorkflowDepartmentConfig(processDefKey, taskDefKey);
        Set<UserVO> resultHandlers = this.findWorkflowRoleHandlerVOs(processDefKey, taskDefKey,
            departmentSet);
        Set<String> userVOs = new HashSet<String>();
        for (UserVO handlerVO : resultHandlers) {
            userVOs.add(handlerVO.getUserId());
        }
        return userVOs;
    }

    public Set<UserVO> getUserTaskHandlerVOs(String processDefKey, String taskDefKey) {
        Set<DepartmentVO> departmentSet = this.findWorkflowDepartmentConfig(processDefKey, taskDefKey);
        Set<UserVO> resultHandlers = this.findWorkflowRoleHandlerVOs(processDefKey, taskDefKey,
            departmentSet);
        return resultHandlers;
    }

    public Set<String> findWorkflowRoleUsers(String processDefKey, String taskDefKey,
                                             Set<DepartmentVO> departmentSet) {
        Set<UserVO> resultHandlers = this.findWorkflowRoleHandlerVOs(processDefKey, taskDefKey,
            departmentSet);
        Set<String> userVOs = new HashSet<String>();
        for (UserVO handlerVO : resultHandlers) {
            userVOs.add(handlerVO.getUserId());
        }
        return userVOs;
    }

    public Set<UserVO> findWorkflowRoleHandlerVOs(String processDefKey, String taskDefKey,
                                                  Set<DepartmentVO> departmentSet) {
        List<RoleVO> roleVOList = this.findWorkflowRoleConfig(processDefKey, taskDefKey);
        if (CollectionUtils.isEmpty(departmentSet) && CollectionUtils.isEmpty(roleVOList)) {
            return Collections.EMPTY_SET;
        }

        List<UserVO> userVOList = new ArrayList<UserVO>();
        PlatformDao dao = new PlatformDao();
        Element resultElement = null;
        ArrayList<String> bvals = new ArrayList<String>();
        StringBuffer sql = new StringBuffer("");

        String roleStr = StringUtils.arrayToCommaDelimitedString(StringUtils
            .toStringArray(convertRoleVOs(roleVOList)));
        String departmentStr = StringUtils.arrayToCommaDelimitedString(StringUtils
            .toStringArray(convertDepartments(departmentSet)));
        // HashMap<String, HandlerVO> tempHandlerMap = new HashMap<String,
        // HandlerVO>();
        try {
            if (CollectionUtils.isEmpty(departmentSet)) {

                // for(RoleVO roleVO : roleVOList){
                sql.append("SELECT a.userid, a.usercode, a.username ");
                sql.append("FROM pcmc_user a, pcmc_user_role c ");
                sql.append("WHERE a.userid = c.userid ");
                sql.append("AND c.ROLEID in(");
                sql.append(roleStr);
                sql.append(")");

                // /bvals.add(roleVO.getRoleId());
                dao.setSql(sql.toString());
                // dao.setBindValues(bvals);
                resultElement = dao.executeQuerySql(-1, 1);
                List<Element> userIdElements = resultElement.getChildren("Record");
                for (Element userIdElement : userIdElements) {
                    UserVO handlerVO = new UserVO();
                    handlerVO.setUserId(userIdElement.getChildText("userid"));
                    handlerVO.setUserCode(userIdElement.getChildText("usercode"));
                    handlerVO.setUserName(userIdElement.getChildText("username"));
                    userVOList.add(handlerVO);
                }
                return new HashSet(userVOList);
            }
            sql.setLength(0);
            bvals = new ArrayList<String>();
            if (CollectionUtils.isEmpty(roleVOList)) {

                // for(RoleVO roleVO : roleVOList){
                sql.append("SELECT a.userid, a.usercode, a.username");
                sql.append("  FROM pcmc_user a, pcmc_user_dept b ");
                sql.append("WHERE a.userid = b.userid AND b.deptid in(");
                sql.append(departmentStr);
                sql.append(")");

                // /bvals.add(roleVO.getRoleId());
                dao.setSql(sql.toString());
                // dao.setBindValues(bvals);
                resultElement = dao.executeQuerySql(-1, 1);
                List<Element> userIdElements = resultElement.getChildren("Record");
                for (Element userIdElement : userIdElements) {
                    UserVO handlerVO = new UserVO();
                    handlerVO.setUserId(userIdElement.getChildText("userid"));
                    handlerVO.setUserCode(userIdElement.getChildText("usercode"));
                    handlerVO.setUserName(userIdElement.getChildText("username"));
                    userVOList.add(handlerVO);
                }
                return new HashSet(userVOList);
            }

            sql.setLength(0);
            bvals = new ArrayList<String>();
            // for(DepartmentVO departmentVO : departmentSet){

            // for(RoleVO roleVO : roleVOList){
            sql.append("SELECT a.userid,a.usercode,a.username ");
            sql.append("FROM pcmc_user a, pcmc_user_dept b,pcmc_user_role c ");
            sql.append("WHERE a.userid = c.userid AND a.userid=b.userid ");
            sql.append("AND b.deptid in(");
            sql.append(departmentStr);
            sql.append(") AND c.ROLEID in( ");
            sql.append(roleStr);
            sql.append(")");
            // bvals.add(departmentVO.getDeptId());
            // bvals.add(roleVO.getRoleId());

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            // if (resultElement.getChildren("Record").size() == 0) {
            // sql.setLength(0);
            // bvals = new ArrayList<String>();
            // continue;
            // }
            List<Element> userIdElements = resultElement.getChildren("Record");
            for (Element userIdElement : userIdElements) {
                UserVO handlerVO = new UserVO();
                handlerVO.setUserId(userIdElement.getChildText("userid"));
                handlerVO.setUserCode(userIdElement.getChildText("usercode"));
                handlerVO.setUserName(userIdElement.getChildText("username"));
                userVOList.add(handlerVO);
            }
            // sql.setLength(0);
            // bvals = new ArrayList<String>();
            // }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }
        return new HashSet(userVOList);
    }

    private List<String> convertRoleVOs(List<RoleVO> roleVOList) {
        List<String> roles = new ArrayList<String>();
        for (RoleVO roleVO : roleVOList) {
            roles.add("'" + roleVO.getRoleId() + "'");
        }
        return roles;
    }

    private Set<String> convertDepartments(Set<DepartmentVO> nextDepartments) {
        Set<String> nextDepartmentVOs = new HashSet<String>();
        for (DepartmentVO nextDepartment : nextDepartments) {
            nextDepartmentVOs.add("'" + nextDepartment.getDeptId() + "'");
        }
        return nextDepartmentVOs;
    }

    public Map<String, String> findDelegatetaskConfig(String user, String processDefKey) {
        Set<String> users = new HashSet<String>();
        users.add(user);
        return this.findDelegatetaskConfig(users, processDefKey);
    }

    public Map<String, String> findDelegatetaskConfig(Set<String> users, String processDefKey) {
        Map<String, String> configMap = new HashMap<String, String>();
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        Element resultElement = null;
        ArrayList<String> bvals = new ArrayList<String>();
        try {
            bvals = new ArrayList<String>();
            // sql.append("select * from workflow_delegate_task where ");
            // sql.append("original_user in (");
            // sql.append(" ? )");
            // sql.append(" and delegate_process_key = 'all' ") ;

            String sqll = "select * from workflow_delegate_task where delegate_process_key = 'all' and original_user in ("
                + convertUsers(users) + ")";

            // bvals.add(convertUsers(users));
            dao.setSql(sqll);
            // dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            List<Element> allDelegateConfigureElements = resultElement.getChildren("Record");
            for (Element allDelegateConfigureElement : allDelegateConfigureElements) {
                String starttimeStr = allDelegateConfigureElement.getChildText("starttime");
                String endtimeStr = allDelegateConfigureElement.getChildText("endtime");
                if (satisfyTime(starttimeStr, endtimeStr)) {
                    // DelegateConfigureVO delegateConfigureVO= new
                    // DelegateConfigureVO();
                    // delegateConfigureVO.setOriginalUser(delegateElement.getChildText("original_user"));
                    // delegateConfigureVO.setDelegateUser(delegateElement.getChildText("delegate_user"));
                    /*
                     * delegateConfigureVO.setStartTime(startTime);
                     * delegateConfigureVO.setEndTime(endTime);
                     * delegateConfigureVO
                     * .setProcessDefKey(delegateElement.getChildText
                     * ("delegate_process_key"));
                     * delegateConfigureVO.setProcessDefName
                     * (delegateElement.getChildText("delegate_process_name"));
                     * configs.add(delegateConfigureVO);
                     */
                    configMap.put(allDelegateConfigureElement.getChildText("original_user"),
                        allDelegateConfigureElement.getChildText("delegate_user"));
                }
            }

            sql.setLength(0);
            sql.append("select * from workflow_delegate_task where ");
            sql.append("original_user in (" + convertUsers(users) + ")");
            sql.append(" and delegate_process_key =? ");

            // bvals.add(convertUsers(users));
            bvals.add(processDefKey);
            // sql =
            // "select * from workflow_delegate_task where delegate_process_key = '"+processDefKey+"' and original_user in ("+convertUsers(users)+")";

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            List<Element> delegateConfigureElements = resultElement.getChildren("Record");
            for (Element delegateElement : delegateConfigureElements) {
                String starttimeStr = delegateElement.getChildText("starttime");
                String endtimeStr = delegateElement.getChildText("endtime");
                if (satisfyTime(starttimeStr, endtimeStr)) {
                    // DelegateConfigureVO delegateConfigureVO= new
                    // DelegateConfigureVO();
                    // delegateConfigureVO.setOriginalUser(delegateElement.getChildText("original_user"));
                    // delegateConfigureVO.setDelegateUser(delegateElement.getChildText("delegate_user"));
                    /*
                     * delegateConfigureVO.setStartTime(startTime);
                     * delegateConfigureVO.setEndTime(endTime);
                     * delegateConfigureVO
                     * .setProcessDefKey(delegateElement.getChildText
                     * ("delegate_process_key"));
                     * delegateConfigureVO.setProcessDefName
                     * (delegateElement.getChildText("delegate_process_name"));
                     * configs.add(delegateConfigureVO);
                     */
                    configMap.put(delegateElement.getChildText("original_user"),
                        delegateElement.getChildText("delegate_user"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }
        return configMap;
    }

    public Map<String, UserVO> findDelegateTaskConfig(Set<String> users, String processDefKey) {
        Map<String, UserVO> configMap = new HashMap<String, UserVO>();
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        Element resultElement = null;
        ArrayList<String> bvals = new ArrayList<String>();
        try {
            bvals = new ArrayList<String>();

            String sqll = "select * from workflow_delegate_task where delegate_process_key = 'all' and original_user in ("
                + convertUsers(users) + ")";
            dao.setSql(sqll);
            resultElement = dao.executeQuerySql(-1, 1);
            List<Element> allDelegateConfigureElements = resultElement.getChildren("Record");
            for (Element allDelegateConfigureElement : allDelegateConfigureElements) {
                String starttimeStr = allDelegateConfigureElement.getChildText("starttime");
                String endtimeStr = allDelegateConfigureElement.getChildText("endtime");
                if (satisfyTime(starttimeStr, endtimeStr)) {
                    String originalUser = allDelegateConfigureElement.getChildText("original_user");
                    String originalUserName = allDelegateConfigureElement
                        .getChildText("original_user_name");

                    UserVO originalUserVO = new UserVO();
                    originalUserVO.setUserId(originalUser);
                    originalUserVO.setUserName(originalUserName);

                    String delegateUser = allDelegateConfigureElement.getChildText("delegate_user");
                    String delegateUserName = allDelegateConfigureElement
                        .getChildText("delegate_user_name");
                    UserVO userVO = new UserVO();
                    userVO.setUserId(delegateUser);
                    userVO.setUserName(delegateUserName);

                    configMap.put(originalUserVO.getUserId(), userVO);
                }
            }

            sql.setLength(0);

            sql.append("select * from workflow_delegate_task where ");
            sql.append("original_user in (" + convertUsers(users) + ")");
            sql.append(" and delegate_process_key =? ");

            bvals.add(processDefKey);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            List<Element> delegateConfigureElements = resultElement.getChildren("Record");
            for (Element delegateElement : delegateConfigureElements) {
                String starttimeStr = delegateElement.getChildText("starttime");
                String endtimeStr = delegateElement.getChildText("endtime");
                if (satisfyTime(starttimeStr, endtimeStr)) {

                    String originalUser = delegateElement.getChildText("original_user");
                    String originalUserName = delegateElement.getChildText("original_user_name");

                    UserVO originalUserVO = new UserVO();
                    originalUserVO.setUserId(originalUser);
                    originalUserVO.setUserName(originalUserName);

                    String delegateUser = delegateElement.getChildText("delegate_user");
                    String delegateUserName = delegateElement.getChildText("delegate_user_name");
                    UserVO userVO = new UserVO();
                    userVO.setUserId(delegateUser);
                    userVO.setUserName(delegateUserName);

                    configMap.put(originalUserVO.getUserId(), userVO);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }
        return configMap;
    }

    public Map<String, DelegateUserVO> findDelegateTaskList(Set<String> users, String processDefKey) {
        Map<String, DelegateUserVO> configMap = new HashMap<String, DelegateUserVO>();
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        Element resultElement = null;
        ArrayList<String> bvals = new ArrayList<String>();
        try {
            bvals = new ArrayList<String>();

            String sqll = "select * from workflow_delegate_task where delegate_process_key = 'all' and original_user in ("
                + convertUsers(users) + ")";
            dao.setSql(sqll);
            resultElement = dao.executeQuerySql(-1, 1);
            List<Element> allDelegateConfigureElements = resultElement.getChildren("Record");
            for (Element allDelegateConfigureElement : allDelegateConfigureElements) {
                String starttimeStr = allDelegateConfigureElement.getChildText("starttime");
                String endtimeStr = allDelegateConfigureElement.getChildText("endtime");
                if (satisfyTime(starttimeStr, endtimeStr)) {
                    DelegateUserVO delegateUserVO = new DelegateUserVO();
                    String originalUser = allDelegateConfigureElement.getChildText("original_user");
                    String originalUserName = allDelegateConfigureElement
                        .getChildText("original_user_name");

                    UserVO originalUserVO = new UserVO();
                    originalUserVO.setUserId(originalUser);
                    originalUserVO.setUserName(originalUserName);

                    String delegateUser = allDelegateConfigureElement.getChildText("delegate_user");
                    String delegateUserName = allDelegateConfigureElement
                        .getChildText("delegate_user_name");
                    UserVO userVO = new UserVO();
                    userVO.setUserId(delegateUser);
                    userVO.setUserName(delegateUserName);

                    delegateUserVO.setUserId(originalUser);
                    delegateUserVO.setUserName(originalUserName);
                    delegateUserVO.setDelegatedUser(userVO);
                    configMap.put(originalUser, delegateUserVO);
                }
            }

            sql.setLength(0);

            sql.append("select * from workflow_delegate_task where ");
            sql.append("original_user in (" + convertUsers(users) + ")");
            sql.append(" and delegate_process_key =? ");

            bvals.add(processDefKey);

            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            List<Element> delegateConfigureElements = resultElement.getChildren("Record");
            for (Element delegateElement : delegateConfigureElements) {
                String starttimeStr = delegateElement.getChildText("starttime");
                String endtimeStr = delegateElement.getChildText("endtime");
                if (satisfyTime(starttimeStr, endtimeStr)) {
                    DelegateUserVO delegateUserVO = new DelegateUserVO();

                    String originalUser = delegateElement.getChildText("original_user");
                    String originalUserName = delegateElement.getChildText("original_user_name");

                    // UserVO originalUserVO = new UserVO();
                    // originalUserVO.setUserId(originalUser);
                    // originalUserVO.setUserName(originalUserName);

                    String delegateUser = delegateElement.getChildText("delegate_user");
                    String delegateUserName = delegateElement.getChildText("delegate_user_name");
                    UserVO userVO = new UserVO();
                    userVO.setUserId(delegateUser);
                    userVO.setUserName(delegateUserName);

                    delegateUserVO.setUserId(originalUser);
                    delegateUserVO.setUserName(originalUserName);
                    delegateUserVO.setDelegatedUser(userVO);
                    configMap.put(originalUser, delegateUserVO);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }
        return configMap;
    }

    // public List<String> findDelegateTaskUsers(Set<String> users,
    // String processDefKey) {
    // List<String> resultUser = new ArrayList<String>();
    // Map<String, DelegateUserVO> configMap =
    // findDelegateTaskList(users,processDefKey);
    //
    // for (Map.Entry<String, DelegateUserVO> entry : configMap.entrySet()) {
    // DelegateUserVO delegateUserVO = entry.getValue();
    // if(null!= delegateUserVO && null!=delegateUserVO.getDelegatedUser()){
    // resultUser.add(delegateUserVO.getDelegatedUser().getUserId());
    // }else{
    // resultUser.add(entry.getKey());
    // }
    // }
    // return resultUser;
    // }

    public List<String> getFinalDelegateUsers(List<String> users, Map<String, DelegateUserVO> configMap) {
        List<String> resultUser = new ArrayList<String>();
        for (String subTaskUser : users) {
            DelegateUserVO delegateUserVO = configMap.get(subTaskUser);
            if (null != delegateUserVO) {
                resultUser.add(delegateUserVO.getDelegatedUser().getUserId());
            } else {
                resultUser.add(subTaskUser);
            }
        }
        return resultUser;
    }

    private boolean satisfyTime(String startTimeStr, String endTimeStr) throws ParseException {
        Date startTime = DateUtil.stringToDate(startTimeStr);
        Date endTime = DateUtil.stringToDate(endTimeStr);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);
        endCalendar.add(Calendar.DATE, 1);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);
        endTime = endCalendar.getTime();

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);
        startCalendar.add(Calendar.DATE, 0);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, -1);
        startCalendar.set(Calendar.MILLISECOND, 0);
        startTime = startCalendar.getTime();

        Date nowDate = new Date();
        nowDate = DateUtil.stringToDate(DateUtil.dateToDateString(nowDate));

        if (startTime.before(nowDate) && endTime.after(nowDate)) {
            return true;
        }

        return false;
    }

    /*
     * 多实例节点创建的时候生成代理记录
     */
    public void insertProcessDelegateHistory(Map<String, DelegateUserVO> configMap,
                                             String processInstanceId, String processDefKey,
                                             String processDefName, String taskKey, String delegateType) {
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE workflow_hi_delegate");
        sql.append(" SET isRunning = '1'");
        sql.append(" WHERE taskDefKey = ? AND processInstanceId = ? AND delegate_type = ?");
        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(), taskKey, processInstanceId,
            delegateType);
        insertProcessDelegateSingle(configMap, "", taskKey, processInstanceId, processDefKey,
            processDefName, delegateType);
    }

    public void insertEditMultiDelegateHistory(Map<String, DelegateUserVO> configMap, String taskId,
                                               String processDefKey, String processDefName,
                                               String delegateType) {
        String taskDefinitionKey = "";
        String processInstanceId = "";
        if (!StringUtils.isEmpty(taskId)) {
            TaskEntity taskEntity = (TaskEntity) ApplicationContextHelper.getTaskService()
                .createTaskQuery().taskId(taskId).singleResult();
            processInstanceId = taskEntity.getProcessInstanceId();
            taskDefinitionKey = taskEntity.getTaskDefinitionKey();
        }
        StringBuffer sql = new StringBuffer("");
        sql.append("UPDATE workflow_hi_delegate");
        sql.append(" SET isRunning = '0'");
        sql.append(" WHERE taskDefKey = ? AND processInstanceId = ? AND delegate_type = ?");
        ApplicationContextHelper.getJdbcTemplate().update(sql.toString(), taskDefinitionKey,
            processInstanceId, delegateType);
        insertProcessDelegateSingle(configMap, taskId, taskDefinitionKey, processInstanceId, processDefKey,
            processDefName, delegateType);
    }

    public void insertDelegateHistory(Map<String, DelegateUserVO> configMap, String taskId,
                                      String processDefKey, String processDefName, String delegateType) {
        String taskDefinitionKey = "";
        String processInstanceId = "";
        if (!StringUtils.isEmpty(taskId)) {
            TaskEntity taskEntity = (TaskEntity) ApplicationContextHelper.getTaskService().createTaskQuery().taskId(taskId).singleResult();
            processInstanceId = taskEntity.getProcessInstanceId();
            taskDefinitionKey = taskEntity.getTaskDefinitionKey();
        }

        insertProcessDelegateSingle(configMap, taskId, taskDefinitionKey, processInstanceId, processDefKey,
            processDefName, delegateType);
    }

    public void insertDelegateHistory(Map<String, DelegateUserVO> configMap, DelegateTask delegateTask,
                                      String processDefKey, String processDefName, String delegateType) {
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String processInstanceId = delegateTask.getProcessInstanceId();

        insertProcessDelegateSingle(configMap, delegateTask.getId(), taskDefinitionKey, processInstanceId,
            processDefKey, processDefName, delegateType);
    }

    private void insertProcessDelegateSingle(Map<String, DelegateUserVO> configMap, String taskId,
                                             String taskKey, String processInstanceId,
                                             String processDefKey, String processDefName,
                                             String delegateType) {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        StringBuffer sql = new StringBuffer("");

        List<Object[]> batchArgs = new ArrayList<Object[]>();
        for (Map.Entry<String, DelegateUserVO> entry : configMap.entrySet()) {
            String seq = String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_hi_delegate"));
            DelegateUserVO delegateUserVO = entry.getValue();
            Object[] param = new Object[14];
            param[0] = seq;
            param[1] = processInstanceId;
            param[2] = taskId;
            param[3] = taskKey;
            param[4] = entry.getKey();
            param[5] = delegateUserVO.getUserName();
            param[6] = delegateUserVO.getDelegatedUser().getUserId();
            param[7] = delegateUserVO.getDelegatedUser().getUserName();
            param[8] = processDefKey;
            param[9] = processDefName;
            param[10] = delegateType;
            param[11] = "1";
            param[12] = "userid";
            param[13] = new Date();
            batchArgs.add(param);
        }
        sql.setLength(0);
        sql.append("INSERT INTO workflow_hi_delegate(");
        sql.append("   id");
        sql.append("  ,processInstanceId");
        sql.append(" ,taskId");
        sql.append(" ,taskDefKey");
        sql.append(" ,original_user");
        sql.append(" ,original_user_name");
        sql.append(" ,delegate_user");
        sql.append(" ,delegate_user_name");
        sql.append(" ,delegate_process_key");
        sql.append(" ,delegate_process_name");
        sql.append(" ,delegate_type");
        sql.append(" ,isRunning");
        sql.append(" ,create_people");
        sql.append(" ,create_time");
        sql.append(") VALUES (");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?,");
        sql.append("  ?");
        sql.append(")");
        ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
    }

    public Map<String, DelegateUserVO> findTaskHiDelegate(List<String> originalUsers,
                                                          String processInstanceId, String taskId,
                                                          String taskDefKey, String delegateType) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT id,");
        sql.append(" processInstanceId,");
        sql.append(" taskId,");
        sql.append("  original_user,");
        sql.append("  original_user_name,");
        sql.append(" delegate_user,");
        sql.append(" delegate_user_name");
        sql.append(" FROM workflow_hi_delegate");
        sql.append(" WHERE     processInstanceId = ?");
        sql.append(" AND original_user in (" + convertUsers(new HashSet<String>(originalUsers)) + ")");
        // sql.append("  AND taskId = ?");
        sql.append("  AND taskDefKey = ?");
        sql.append(" AND isRunning = '1'");
        // sql.append(" and delegate_type=?");
        sql.append(" and exists(select 1 from act_ru_task where act_ru_task.ID_ = workflow_hi_delegate.taskId)");

        sql.append(" order by create_time asc");

        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(), processInstanceId, taskDefKey);

        Map<String, DelegateUserVO> delegateMap = new HashMap<String, DelegateUserVO>();

        // List<DelegateUserVO> delegateUsers= new ArrayList<DelegateUserVO>();
        for (Map<String, Object> map : list) {
            String originalUser = map.get("original_user").toString();
            String originalUserName = map.get("original_user_name").toString();
            String delegateUser = map.get("delegate_user").toString();
            String delegateUserName = map.get("delegate_user_name").toString();
            DelegateUserVO delegateUserVO = new DelegateUserVO();
            delegateUserVO.setUserId(originalUser);
            delegateUserVO.setUserName(originalUserName);

            UserVO userVO = new UserVO();
            userVO.setUserId(delegateUser);
            userVO.setUserName(delegateUserName);
            delegateUserVO.setDelegatedUser(userVO);

            delegateMap.put(originalUser, delegateUserVO);
        }
        return delegateMap;
    }

    // public void insertDelegateHistory(Map<String, DelegateUserVO> configMap,
    // DelegateTask delegateTask,
    // String processDefKey,String processDefName) {
    //
    // }
    /*
     * public void insertDelegateHistory(Map<String, DelegateUserVO> configMap,
     * DelegateTask delegateTask, ProcessDefinitionEntity
     * processDefinitionEntity) { List<Object[]> batchArgs = new
     * ArrayList<Object[]>(); for (Map.Entry<String, DelegateUserVO> entry :
     * configMap.entrySet()) { String seq =
     * String.valueOf(DBOprProxy.getNextSequenceNumber("workflow_hi_delegate"));
     * DelegateUserVO delegateUserVO = entry.getValue(); Object[] param = new
     * Object[10]; param[0] = seq; param[1] =
     * delegateTask.getProcessInstanceId(); param[2] = delegateTask.getId();
     * param[3] = delegateTask.getTaskDefinitionKey(); param[4] =
     * entry.getKey(); param[5] = delegateUserVO.getUserName(); param[6] =
     * delegateUserVO.getDelegatedUser().getUserId(); param[7] =
     * delegateUserVO.getDelegatedUser().getUserName(); param[8] =
     * processDefinitionEntity.getKey(); param[9] =
     * processDefinitionEntity.getName(); param[10] =
     * WorkflowConstant.TASK_DELEGATE_ORIGINAL; batchArgs.add(param); }
     * StringBuffer sql = new StringBuffer("");
     * sql.append("INSERT INTO workflow_hi_delegate("); sql.append("   id");
     * sql.append("  ,processInstanceId"); sql.append(" ,taskId");
     * sql.append(" ,taskDefKey"); sql.append(" ,original_user");
     * sql.append(" ,original_user_name"); sql.append(" ,delegate_user");
     * sql.append(" ,delegate_user_name"); sql.append(" ,delegate_process_key");
     * sql.append(" ,delegate_process_name"); sql.append(" ,delegate_type");
     * sql.append(") VALUES ("); sql.append("  ?,"); sql.append("  ?,");
     * sql.append("  ?,"); sql.append("  ?,"); sql.append("  ?,");
     * sql.append("  ?,"); sql.append("  ?,"); sql.append("  ?,");
     * sql.append("  ?,"); sql.append("  ?"); sql.append("  ?");
     * sql.append(")");
     * ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql
     * .toString(),batchArgs); }
     */

    private String convertUsers(Set<String> users) {
        Set<String> userVOs = new HashSet<String>();
        for (String user : users) {
            userVOs.add("'" + user + "'");
        }
        return StringUtils.collectionToDelimitedString(userVOs, ",");
    }

    public UserTaskRemindVO findUserTaskRemindConfig(String processDefKey, String taskDefKey) {
        UserTaskRemindVO userTaskRemindVO = new UserTaskRemindVO();
        PlatformDao dao = new PlatformDao();
        StringBuffer sql = new StringBuffer("");
        Element resultElement = null;
        try {
            ArrayList<String> bvals = new ArrayList<String>();
            sql.append("select * from workflow_node_remind where processdefkey=? and taskdefkey=?");
            bvals.add(processDefKey);
            bvals.add(taskDefKey);
            dao.setSql(sql.toString());
            dao.setBindValues(bvals);
            resultElement = dao.executeQuerySql(-1, 1);
            if (resultElement.getChildren("Record").size() > 0) {
                String id = resultElement.getChild("Record").getChildTextTrim("id");
                String dueDate = resultElement.getChild("Record").getChildTextTrim("duedate");
                String remindMode = resultElement.getChild("Record").getChildTextTrim("remind_mode");
                String isRemind = resultElement.getChild("Record").getChildTextTrim("isremind");
                String taskDefName = resultElement.getChild("Record").getChildTextTrim("taskdefname");
                String node_type = resultElement.getChild("Record").getChildTextTrim("node_type");
                String multi_kind = resultElement.getChild("Record").getChildTextTrim("multi_kind");
                multi_kind = StringUtils.isEmpty(multi_kind) ? "0" : multi_kind;
                String ismulti = resultElement.getChild("Record").getChildTextTrim("ismulti");
                userTaskRemindVO.setId(id);
                userTaskRemindVO.setDueDate(Integer.parseInt(dueDate));
                userTaskRemindVO.setRemindMode(Integer.parseInt(remindMode));
                userTaskRemindVO.setIsRemind(Integer.parseInt(isRemind));
                userTaskRemindVO.setProcessDefKey(processDefKey);
                userTaskRemindVO.setTaskDefkey(taskDefKey);
                userTaskRemindVO.setTaskDefName(taskDefName);
                userTaskRemindVO.setNode_type(node_type);
                userTaskRemindVO.setIsMulti(Integer.parseInt(ismulti));
                userTaskRemindVO.setMulti_kind(Integer.parseInt(multi_kind));
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
        return userTaskRemindVO;
    }
}
