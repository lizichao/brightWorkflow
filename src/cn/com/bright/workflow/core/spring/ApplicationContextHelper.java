package cn.com.bright.workflow.core.spring;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.com.bright.workflow.service.DepartmentQueryService;
import cn.com.bright.workflow.service.ProcessFormService;
import cn.com.bright.workflow.service.ProcessOperateService;
import cn.com.bright.workflow.service.ProcessQueryService;
import cn.com.bright.workflow.service.TaskOperateService;
import cn.com.bright.workflow.service.TaskQueryService;
import cn.com.bright.workflow.service.UserQueryService;
import cn.com.bright.workflow.service.WorkflowDefExtService;
import cn.com.bright.workflow.service.WorkflowLogService;
import cn.com.bright.workflow.workcal.manager.WorkcalPartManager;
import cn.com.bright.workflow.workcal.manager.WorkcalRuleManager;
import cn.com.bright.workflow.workcal.manager.WorkcalTypeManager;

/**
 * ApplicationContextHelper
 */
public class ApplicationContextHelper implements ApplicationContextAware {
    /**
     * 向ApplicationContextHolder里设置ApplicationContext.
     * 
     * @param applicationContext
     *            applicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.getInstance().setApplicationContext(applicationContext);
    }

    /**
     * 获得ApplicationContext.
     * 
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return ApplicationContextHolder.getInstance().getApplicationContext();
    }

    /**
     * 根据class获得bean.
     * 
     * @param clz
     *            Class
     * @return T
     */
    public static <T> T getBean(Class<T> clz) {
        return ApplicationContextHolder.getInstance().getApplicationContext().getBean(clz);
    }

    /**
     * 根据id获得bean.
     * 
     * @param id
     *            String
     * @return T
     */
    public static <T> T getBean(String id) {
        return (T) ApplicationContextHolder.getInstance().getApplicationContext().getBean(id);
    }

    /**
     * 根据id和class获得bean.
     * 
     * @param id
     *            String
     * @return T
     */
    public static <T> T getBean(String id, Class<T> arg1) {
        return (T) ApplicationContextHolder.getInstance().getApplicationContext().getBean(id, arg1);
    }

    public static ProcessEngineConfiguration getProcessEngineConfiguration() {
        return (ProcessEngineConfiguration) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("processEngineConfiguration");
    }

    public static ProcessEngine getProcessEngine() {
        return (ProcessEngine) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("processEngine");
    }

    public static RepositoryService getRepositoryService() {
        return (RepositoryService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("repositoryService");
    }

    public static RuntimeService getRuntimeService() {
        return (RuntimeService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("runtimeService");
    }

    public static HistoryService getHistoryService() {
        return (HistoryService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("historyService");
    }

    public static IdentityService getIdentityService() {
        return (IdentityService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("identityService");
    }

    public static TaskService getTaskService() {
        return (TaskService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("taskService");
    }

    public static FormService getFormService() {
        return (FormService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("formService");
    }

    public static ManagementService getManagementService() {
        return (ManagementService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("managementService");
    }

    public static ProcessOperateService getProcessOperateService() {
        return (ProcessOperateService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("processOperateService");
    }

    public static TaskOperateService getTaskOperateService() {
        return (TaskOperateService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("taskOperateService");
    }

    public static TaskQueryService getTaskQueryService() {
        return (TaskQueryService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("taskQueryService");
    }

    public static ProcessQueryService getProcessQueryService() {
        return (ProcessQueryService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("processQueryService");
    }

    public static WorkflowLogService getWorkflowLogService() {
        return (WorkflowLogService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("workflowLogService");
    }

    public static DepartmentQueryService getDepartmentQueryService() {
        return (DepartmentQueryService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("departmentQueryService");
    }

    public static UserQueryService getUserQueryService() {
        return (UserQueryService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("userQueryService");
    }

    public static WorkflowDefExtService getWorkflowDefExtService() {
        return (WorkflowDefExtService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("workflowDefExtService");
    }

    public static WorkcalRuleManager getWorkcalRuleManager() {
        return (WorkcalRuleManager) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("workcalRuleManager");
    }

    public static WorkcalPartManager getWorkcalPartManager() {
        return (WorkcalPartManager) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("workcalPartManager");
    }

    public static WorkcalTypeManager getWorkcalTypeManager() {
        return (WorkcalTypeManager) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("workcalTypeManager");
    }

    public static ProcessFormService getProcessFormService() {
        return (ProcessFormService) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("processFormService");
    }

    public static JdbcTemplate getJdbcTemplate() {
        return (JdbcTemplate) ApplicationContextHolder.getInstance().getApplicationContext()
            .getBean("jdbcTemplate");
    }
}
