package cn.com.bright.workflow.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.util.FileUtil;
import cn.com.bright.workflow.api.vo.UserVO;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

public class ProcessQueryService {

    @Resource
    private TaskQueryService taskQueryService;

    @Resource
    private IdentityService identityService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private UserQueryService userQueryService;

    @Resource
    private WorkflowDefExtService workflowDefExtService;

    public Set<UserVO> searchProcesshandlers(String processInstanceId) {
        HashSet<String> handlers = new HashSet<String>();
        List<Task> tasks = taskQueryService.findTaskByProcessInstanceId(processInstanceId);
        for (Task task : tasks) {
            handlers.addAll(taskQueryService.searchTaskhandlerStr(task));
        }
        List<UserVO> results = userQueryService.getMultiUserVO(new ArrayList<String>(handlers));
        return new HashSet<UserVO>(results);
    }

    /**
     * 无用
     * @param processInstanceId
     * @param processDefKey
     * @return
     */
    /*
     * public Map<UserVO,UserVO> searchProcesshandlers(String
     * processInstanceId,String processDefKey) { HashSet<String> handlers = new
     * HashSet<String>(); List<Task> tasks =
     * taskQueryService.findTaskByProcessInstanceId(processInstanceId); for(Task
     * task : tasks){
     * handlers.addAll(taskQueryService.searchTaskhandlerStr(task)); }
     * List<UserVO> results = userQueryService.getMultiUserVO(new
     * ArrayList<String>(handlers)); Map<UserVO,UserVO> resultMap= new
     * HashMap<UserVO,UserVO>(); Map<String,UserVO> delegateTaskMap
     * =workflowDefExtService.findDelegateTaskConfig(handlers, processDefKey);
     * for (UserVO userVO : results) { UserVO delegateUserVO=
     * delegateTaskMap.get(userVO.getUserId()); //String delegateStr =
     * !StringUtils.isEmpty(delegateUserVO.getUserId()) ?
     * delegateUserVO.getUserName()+"(代理"+userVO.getUserName()+")" :
     * userVO.getUserName(); resultMap.put(userVO, delegateUserVO); } return
     * resultMap; }
     */

    public Set<UserVO> searchProcesshandlers(List<Task> tasks) {
        HashSet<String> handlers = new HashSet<String>();
        for (Task task : tasks) {
            handlers.addAll(taskQueryService.searchTaskhandlerStr(task));
        }
        List<UserVO> results = userQueryService.getMultiUserVO(new ArrayList<String>(handlers));
        return new HashSet<UserVO>(results);
    }

    /*
     * public List<String> searchProcessDelegateHandlers(List<Task> tasks,String
     * processDefKey) { HashSet<String> handlers = new HashSet<String>(); for
     * (Task task : tasks) {
     * handlers.addAll(taskQueryService.searchTaskhandlerStr(task)); }
     * Map<String,UserVO> delegateTaskMap
     * =workflowDefExtService.findDelegateTaskConfig(handlers, processDefKey);
     * List<UserVO> results = userQueryService.getMultiUserVO(new
     * ArrayList<String>(handlers)); List<String> resultStr = new
     * ArrayList<String>(); for (UserVO userVO : results) { UserVO
     * delegateUserVO= delegateTaskMap.get(userVO.getUserId()); String
     * delegateStr = (null!= delegateUserVO
     * &&!StringUtils.isEmpty(delegateUserVO.getUserId())) ?
     * delegateUserVO.getUserName()+"(代理"+userVO.getUserName()+")" :
     * userVO.getUserName(); resultStr.add(delegateStr); } return resultStr; }
     */

    /*
     * public List<DelegateUserVO> searchProcessDelegateVOs(String
     * processInstanceId,String processDefKey) { HashSet<String> handlers = new
     * HashSet<String>(); List<Task> tasks =
     * taskQueryService.findTaskByProcessInstanceId(processInstanceId); for
     * (Task task : tasks) {
     * handlers.addAll(taskQueryService.searchTaskhandlerStr(task)); }
     * List<UserVO> results = userQueryService.getMultiUserVO(new
     * ArrayList<String>(handlers)); Map<UserVO,UserVO> delegateTaskMap
     * =workflowDefExtService.findDelegateTaskConfig(handlers, processDefKey);
     * List<DelegateUserVO> delegateUserVOs= new ArrayList<DelegateUserVO>();
     * for (UserVO userVO : results) { UserVO delegateUserVO=
     * delegateTaskMap.get(userVO.getUserId()); DelegateUserVO delegateResultVO
     * = new DelegateUserVO(); delegateResultVO.setUserId(userVO.getUserId());
     * delegateResultVO.setUserName(userVO.getUserName());
     * delegateResultVO.setDelegatedUser(delegateUserVO);
     * delegateUserVOs.add(delegateResultVO); } return delegateUserVOs; }
     */

    public String generateDiagram(String processInstanceId) throws IOException {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstanceId).singleResult();
        ProcessDefinition processDefinition = ApplicationContextHelper.getRepositoryService()
            .getProcessDefinition(processInstance.getProcessDefinitionId());
        BpmnModel bpmnModel = ApplicationContextHelper.getRepositoryService().getBpmnModel(
            processInstance.getProcessDefinitionId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);

        // ProcessEngineConfiguration processEngineConfiguration =
        // (ApplicationContextHelper.getBean("processEngineConfiguration"));.setActivityFontName("宋体").setLabelFontName("宋体")

        ProcessDiagramGenerator diagramGenerator = ApplicationContextHelper.getProcessEngineConfiguration()
            .getProcessDiagramGenerator();
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds,
            Collections.<String> emptyList(), "宋体", "宋体", null, 1.0);

        BufferedImage image = null;
        image = ImageIO.read(imageStream);
        String relativePath = BrightComConfig.getSysConfiguration().getProperty("processFile")
            + processDefinition.getDiagramResourceName();
        File targetFile = new File(FileUtil.getWebPath() + relativePath);
        if (!targetFile.exists())
            targetFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(targetFile);
        ImageIO.write(image, "png", fos);
        return relativePath;

        // 输出资源内容到相应对象
        /*
         * byte[] b = new byte[1024]; int len; while ((len = imageStream.read(b,
         * 0, 1024)) != -1) { response.getOutputStream().write(b, 0, len); }
         */
    }

    public ProcessDefinitionEntity GetProcessDefinition(String processDefinitionId) {
        ProcessDefinitionEntity processDefinitionEntity = ApplicationContextHelper.getManagementService()
            .executeCommand(new GetDeploymentProcessDefinitionCmd(processDefinitionId));
        return processDefinitionEntity;

    }

    public List<ProcessDefinition> searchProcessDefinitions() {
        List<ProcessDefinition> processDefinitions = ApplicationContextHelper.getRepositoryService()
            .createProcessDefinitionQuery().latestVersion().list();
        return processDefinitions;
    }
}
