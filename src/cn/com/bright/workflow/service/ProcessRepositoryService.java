package cn.com.bright.workflow.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.util.FileUtil;
import cn.com.bright.workflow.api.vo.UserTaskRemindVO;
import cn.com.bright.workflow.bpmn.cmd.FindGraphCmd;
import cn.com.bright.workflow.bpmn.graph.Graph;
import cn.com.bright.workflow.bpmn.graph.Node;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

@Transactional(rollbackFor = Exception.class)
@Service
public class ProcessRepositoryService {

    @Resource
    private RepositoryService repositoryService;

    public void importProcessDefinition(Element doc_file) throws IOException {
        String fileName = doc_file.getAttributeValue("name");
        String fileExt = doc_file.getAttributeValue("ext");
        String fileSize = doc_file.getAttributeValue("size");

        String upPath = doc_file.getText();
        String srcFile = FileUtil.getPhysicalPath(upPath);// 文档路径
        String desFileName = FileUtil.getFileName(srcFile);

        FileUtil.createDirs(FileUtil.getWebPath() + "upload/workflow/diagrams/", true);
        File targetFile = new File(FileUtil.getWebPath() + "upload/workflow/diagrams/" + desFileName);
        FileUtil.moveFile(new File(srcFile), targetFile);
        FileUtil.deleteFile(srcFile);

        InputStream fileInputStream = new FileInputStream(targetFile);
        Deployment deployment = null;

        // String extension = FilenameUtils.getExtension(fileName);
        if (fileExt.equals("zip") || fileExt.equals("bar")) {
            ZipInputStream zip = new ZipInputStream(fileInputStream);
            deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
        } else {
            deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
        }
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
            .deploymentId(deployment.getId()).list();
        // if(CollectionUtils.isEmpty(list)){
        // xmlDocUtil.writeErrorMsg("10610","流程定义部署失败");
        // }
        insertNodeRemindData(list);
    }

    public void insertNodeRemindData(List<ProcessDefinition> list) {
        List<UserTaskRemindVO> userTaskRemindVOs = new ArrayList<UserTaskRemindVO>();
        for (ProcessDefinition processDefinition : list) {
            ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) processDefinition;
            String processKey = processDefinition.getKey();
            ProcessEngine processEngine = ApplicationContextHelper.getProcessEngine();
            Graph graph = processEngine.getManagementService().executeCommand(
                new FindGraphCmd(processDefinition.getId()));

            for (Node node : graph.getNodes()) {
                String taskDefKey = node.getId();
                String taskDefName = node.getName();
                String node_type = node.getType();
                boolean isMulti = node.isMulti();
                // ActivityImpl activityImpl =
                // processDefinitionEntity.findActivity(taskDefKey);

                // boolean isMulti =
                // activityImpl.getProperty("type").equals(WorkflowConstant.USERTASKTYPE)
                // && (null != activityImpl.getProperty("multiInstance"));

                UserTaskRemindVO userTaskRemindVO = ApplicationContextHelper.getWorkflowDefExtService()
                    .findUserTaskRemindConfig(processKey, taskDefKey);
                if ("exclusiveGateway".equals(node.getType())) {
                    continue;
                } else if ("userTask".equals(node.getType())) {
                    if (StringUtils.isEmpty(userTaskRemindVO.getId())) {
                        userTaskRemindVO.setProcessDefKey(processKey);
                        userTaskRemindVO.setTaskDefkey(taskDefKey);
                        userTaskRemindVO.setTaskDefName(taskDefName);
                        userTaskRemindVO.setNode_type(node_type);
                        userTaskRemindVO.setMulti_type(node.getMultiType());
                        if (null != node.getMultiType() && node.getMultiType().equals("sequential")) {
                            userTaskRemindVO.setMulti_kind(UserTaskRemindVO.USER_MULTI);
                        }
                    }
                    userTaskRemindVO.setIsMulti(isMulti ? 1 : 0);
                    userTaskRemindVOs.add(userTaskRemindVO);
                } else if ("startEvent".equals(node.getType())) {
                    if (StringUtils.isEmpty(userTaskRemindVO.getId())) {
                        userTaskRemindVO.setProcessDefKey(processKey);
                        userTaskRemindVO.setTaskDefkey(taskDefKey);
                        userTaskRemindVO.setTaskDefName(taskDefName);
                        userTaskRemindVO.setNode_type(node_type);
                    }
                    userTaskRemindVOs.add(userTaskRemindVO);
                } else if ("endEvent".equals(node.getType())) {
                    // this.processEndEvent(node, bpmnModel, priority++,
                    // bpmConfBase);
                }
            }

            if (userTaskRemindVOs.size() > 0) {
                StringBuffer sql = new StringBuffer("");
                sql.append("delete from workflow_node_remind where processdefkey=?");
                ApplicationContextHelper.getJdbcTemplate().update(sql.toString(), processKey);

                sql.setLength(0);
                List<Object[]> batchArgs = new ArrayList<Object[]>();
                sql.append("insert into workflow_node_remind(id,processdefkey,taskdefkey,taskdefname,node_type,multi_kind,multi_type,ismulti,isRemind,duedate,remind_mode) values(?,?,?,?,?,?,?,?,?,?,?)");
                for (int i = 0; i < userTaskRemindVOs.size(); i++) {
                    Object[] param = new Object[11];
                    UserTaskRemindVO userTaskRemindVO = (UserTaskRemindVO) userTaskRemindVOs.get(i);
                    long seq = DBOprProxy.getNextSequenceNumber("workflow_node_remind");
                    if (StringUtils.isEmpty(userTaskRemindVO.getId())) {
                        param[0] = seq;
                    } else {
                        param[0] = userTaskRemindVO.getId();
                    }

                    param[1] = userTaskRemindVO.getProcessDefKey();
                    param[2] = userTaskRemindVO.getTaskDefkey();
                    param[3] = userTaskRemindVO.getTaskDefName();
                    param[4] = userTaskRemindVO.getNode_type();
                    param[5] = userTaskRemindVO.getMulti_kind();
                    param[6] = userTaskRemindVO.getMulti_type();
                    param[7] = userTaskRemindVO.getIsMulti();
                    param[8] = userTaskRemindVO.getIsRemind();
                    param[9] = userTaskRemindVO.getDueDate();
                    param[10] = userTaskRemindVO.getRemindMode();
                    batchArgs.add(param);
                }
                ApplicationContextHelper.getJdbcTemplate().batchUpdate(sql.toString(), batchArgs);
            }
        }
    }

}
