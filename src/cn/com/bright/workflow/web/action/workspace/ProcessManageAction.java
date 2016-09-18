package cn.com.bright.workflow.web.action.workspace;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBOprProxy;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.api.vo.UserTaskRemindVO;
import cn.com.bright.workflow.bpmn.cmd.FindGraphCmd;
import cn.com.bright.workflow.bpmn.graph.Graph;
import cn.com.bright.workflow.bpmn.graph.Node;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.service.ProcessRepositoryService;


/**
 * 流程管理操作类
 * @author lzc
 *
 */
public class ProcessManageAction
{
	private XmlDocPkgUtil xmlDocUtil = null;
	
	private Log log4j = new Log(this.getClass().toString());
	
	private static String upFolder = (String)BrightComConfig.getSysConfiguration().getProperty("processFile");

    protected RepositoryService repositoryService;
    
    public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public Document doPost(Document xmlDoc) throws IOException {
		this.setRepositoryService(ApplicationContextHelper.getRepositoryService());
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		if ("queryProcessDefinition".equals(action)) {
			queryProcessDefinition();
		} else if ("importProcessDefinition".equals(action)) {
			importProcessDefinition();
		} else if ("downProcessResource".equals(action)) {
			downProcessResource();
		} else if ("processDefinitionOperate".equals(action)) {
			processDefinitionOperate();
		} else if ("getUserTaskNodes".equals(action)) {
			getUserTaskNodes();
		}
		return xmlDoc;
	}
    


	private void getUserTaskNodes() {
		Element reqElement = xmlDocUtil.getRequestData();
		String processDefId = reqElement.getChildTextTrim("query_processDefId");
		Graph graph = ApplicationContextHelper.getManagementService().executeCommand(new FindGraphCmd(processDefId));

        String[] act_flds = {"id","name","type"};
    	Element nodeDatas = XmlDocPkgUtil.createMetaData(act_flds);
		 for (Node node : graph.getNodes()) {
			if ("exclusiveGateway".equals(node.getType())) {
				continue;
			} else if ("userTask".equals(node.getType())) {
				String[] vals = new String[act_flds.length];
				vals[0] = node.getId();
				vals[1] = node.getName();
				vals[2] = node.getType();
				nodeDatas.addContent(XmlDocPkgUtil.createRecord(act_flds, vals));
			} else if ("startEvent".equals(node.getType())) {
				// this.processStartEvent(node, bpmnModel, priority++,
				// bpmConfBase);
			} else if ("endEvent".equals(node.getType())) {
				// this.processEndEvent(node, bpmnModel, priority++,
				// bpmConfBase);
			}
		}
		 xmlDocUtil.getResponse().addContent(nodeDatas);
		 xmlDocUtil.setResult("0");
	}

	
	/**
	 * 
	 */
    private void processDefinitionOperate() {
    	Element reqElement =  xmlDocUtil.getRequestData();
    	String processDefId = reqElement.getChildTextTrim("query_processDefId");
    	String suspensionState = reqElement.getChildTextTrim("query_suspensionState");
	    if (suspensionState.equals("active")) {
           repositoryService.activateProcessDefinitionById(processDefId, true, null);
        } else if (suspensionState.equals("suspend")) {
           repositoryService.suspendProcessDefinitionById(processDefId, true, null);
        }
	    xmlDocUtil.setResult("0");
	}

    private Element downProcessResource() {
        // xmlDocUtil.getResponse().setContentType("image/png");
        // IOUtils.copy(is, response.getOutputStream());
        // xmlDocUtil.getResponse()
        Element reqElement = xmlDocUtil.getRequestData();
        // Element doc_file = reqElement.getChild("doc_file");//文档文件
        String processDefId = reqElement.getChildTextTrim("query_processDefId");
        String resourceType = reqElement.getChildTextTrim("resourceType");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefId).singleResult();

        InputStream resourceAsStream = null;
        String resourceName = "";
        if (resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml") || resourceType.equals("bpmn")) {
            resourceName = processDefinition.getResourceName();
        }
        resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);

        BufferedImage image = null;
        try {
            if (resourceType.equals("image")) {
                image = ImageIO.read(resourceAsStream);
                resourceAsStream.close();
                File targetFile = new File(FileUtil.getWebPath() + upFolder + resourceName);
                if (!targetFile.exists())
                    targetFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(targetFile);
                ImageIO.write(image, "png", fos);
                fos.close();
            } else {
                try {
                    // 指定输出流的目的地
                    FileOutputStream fwe = new FileOutputStream(FileUtil.getWebPath() + upFolder
                        + resourceName);
                    int count = resourceAsStream.available();
                    resourceAsStream.close();
                    byte[] contents = new byte[count];
                    resourceAsStream.read(contents);

                    // fwe.write(new byte[]{(byte)0xEF, (byte)0xBB,
                    // (byte)0xBF});//utf-8 bom
                    fwe.write(contents);
                    // fwe.write(contents);

                    fwe.flush();
                    fwe.close();
                    // IOUtils.copy(resourceAsStream, fw);
                    /*
                     * //定义xml文件的格式 OutputFormat format
                     * =OutputFormat.createPrettyPrint();
                     * format.setEncoding("GB2312"); //准备输出xml文件 XMLWriter
                     * writer =new XMLWriter(fw,format);
                     * writer.write(resourceAsStream); writer.close();
                     */
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ApplicationContext.getRequest().getSession().setAttribute("filename", resourceName);
            ApplicationContext.getRequest().getSession().setAttribute("filepath", upFolder + resourceName);
            xmlDocUtil.setResult("10");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void importProcessDefinition() throws IOException {
        Element reqElement = xmlDocUtil.getRequestData();
        Element doc_file = reqElement.getChild("doc_file");// 文档文件

        ProcessRepositoryService processRepositoryService = (ProcessRepositoryService) ApplicationContextHelper.getBean("processRepositoryService");
        try {
            processRepositoryService.importProcessDefinition(doc_file);
            xmlDocUtil.setResult("0");
        } catch (Exception e) {
            xmlDocUtil.setResult("-1");
            e.printStackTrace();
            log4j.logError("[流程定义上传]" + e.getMessage());
            xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
        }
		
		/*
		String fileName = doc_file.getAttributeValue("name");
    	String fileExt = doc_file.getAttributeValue("ext");
    	String fileSize = doc_file.getAttributeValue("size");
    	
    	String upPath = doc_file.getText();
	    String srcFile =  FileUtil.getPhysicalPath(upPath);//文档路径
	    String desFileName = FileUtil.getFileName(srcFile);

	    try {
			FileUtil.createDirs(FileUtil.getWebPath()+"upload/workflow/diagrams/", true);
			File targetFile =  new File(FileUtil.getWebPath()+"upload/workflow/diagrams/"+desFileName);
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
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
            if(CollectionUtils.isEmpty(list)){
                xmlDocUtil.writeErrorMsg("10610","流程定义部署失败");
            }
            insertNodeRemindData(list);
     
            xmlDocUtil.setResult("0");
		} catch (IOException e) {
			e.printStackTrace();
			log4j.logError("[流程定义上传]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		}	*/	    
	}

    private void insertNodeRemindData(List<ProcessDefinition> list) {
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

            PlatformDao dao = null;
            try {
                dao = new PlatformDao();

                if (userTaskRemindVOs.size() > 0) {
                    StringBuffer sql = new StringBuffer("");
                    sql.append("delete from workflow_node_remind where processdefkey=?");
                    ArrayList bvals = new ArrayList();
                    bvals.add(processKey);
                    dao.setSql(sql.toString());
                    dao.setBindValues(bvals);
                    dao.executeTransactionSql();

                    sql.setLength(0);
                    bvals = new ArrayList();
                    dao.setSql("insert into workflow_node_remind(id,processdefkey,taskdefkey,taskdefname,node_type,multi_kind,multi_type,ismulti,isRemind,duedate,remind_mode) values(?,?,?,?,?,?,?,?,?,?,?)");
                    for (int i = 0; i < userTaskRemindVOs.size(); i++) {
                        UserTaskRemindVO userTaskRemindVO = (UserTaskRemindVO) userTaskRemindVOs.get(i);
                        long seq = DBOprProxy.getNextSequenceNumber("workflow_node_remind");
                        bvals.clear();
                        if (StringUtils.isEmpty(userTaskRemindVO.getId())) {
                            bvals.add(new Long(seq));
                        } else {
                            bvals.add(userTaskRemindVO.getId());
                        }
                        bvals.add(userTaskRemindVO.getProcessDefKey());
                        bvals.add(userTaskRemindVO.getTaskDefkey());
                        bvals.add(userTaskRemindVO.getTaskDefName());
                        bvals.add(userTaskRemindVO.getNode_type());
                        bvals.add(userTaskRemindVO.getMulti_kind());
                        bvals.add(userTaskRemindVO.getMulti_type());
                        bvals.add(userTaskRemindVO.getIsMulti());
                        bvals.add(userTaskRemindVO.getIsRemind());
                        bvals.add(userTaskRemindVO.getDueDate());
                        bvals.add(userTaskRemindVO.getRemindMode());
                        dao.addBatch(bvals);
                    }
                    dao.executeBatch();
                }
                dao.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                dao.rollBack();
                log4j.logError("[更新用户发生异常.]" + e.getMessage());
                // log4j.logInfo(JDomUtil.toXML(xmlDoc, "GBK", true));
                xmlDocUtil.writeErrorMsg("10610", "修改用户信息失败");
            } finally {
                dao.releaseConnection();
            }
        }
    }

    private final void queryProcessDefinition() {
        Element dataElement = xmlDocUtil.getRequestData();
        String processDefKey = dataElement.getChildTextTrim("query_processDefKey");
        String processDefName = dataElement.getChildTextTrim("query_processDefName");
        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("select RES.ID_ as processDefId, RES.NAME_ as processDefName,")
                .append("RES.KEY_ as processDefKey,RES.VERSION_ as processDefVer,RES.SUSPENSION_STATE_ as suspension, ")
                .append("RES.RESOURCE_NAME_ as processdefxml,RES.DGRM_RESOURCE_NAME_ as processdefpicture,")
                .append("RED.NAME_ as DEPLOY_NAME,RED.CATEGORY_ as DEPLOY_CATEGORY,RED.TENANT_ID_ as DEPLOY_TENANT_ID, RED.DEPLOY_TIME_ as processDefCreateTime ")
                .append("  from ACT_RE_PROCDEF RES")
                .append("          left join ACT_RE_DEPLOYMENT RED")
                .append("             on RES.DEPLOYMENT_ID_ = RED.ID_")
                .append("               WHERE RES.VERSION_ =")
                .append("      (select max(VERSION_)")
                .append("        from ACT_RE_PROCDEF")
                .append("      where KEY_ = RES.KEY_)");
            if (StringUtil.isNotEmpty(processDefKey)) {
                sqlBuf.append(" and upper(RES.KEY_)=? ");
                bvals.add(processDefKey.toUpperCase());
            }
            if (StringUtil.isNotEmpty(processDefName)) {
                sqlBuf.append(" and RES.NAME_ like ? ");
                bvals.add("%" + processDefName + "%");
            }
            sqlBuf.append(" order by RED.DEPLOY_TIME_ desc ");
            dao.setSql(sqlBuf.toString());
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
}
