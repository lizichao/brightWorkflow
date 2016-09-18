package cn.com.bright.workflow.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.util.CollectionUtils;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.workflow.bpmn.cmd.FindGraphCmd;
import cn.com.bright.workflow.bpmn.graph.Graph;
import cn.com.bright.workflow.bpmn.graph.Node;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;


/**
 * 流程管理操作类
 * @author lzc
 *
 */
public class ProcessManageBean
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

	public Document doPost(Document xmlDoc) {
		this.setRepositoryService((RepositoryService)ApplicationContextHelper.getBean("repositoryService"));
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		if ("queryProcessDefinition".equals(action)) {
			queryProcessDefinition();
		} else if ("importProcessDefinition".equals(action)) {
			importProcessDefinition();
		} else if ("downProcessResource".equals(action)) {
			downProcessResource();
		}else if ("processDefinitionOperate".equals(action)) {
			processDefinitionOperate();
		}else if ("getUserTaskNodes".equals(action)) {
			getUserTaskNodes();
		}
		return xmlDoc;
	}
    
	private void getUserTaskNodes() {
		Element reqElement = xmlDocUtil.getRequestData();
		String processDefId = reqElement.getChildTextTrim("query_processDefId");
		ProcessEngine processEngine = (ApplicationContextHelper.getBean("processEngine"));
		Graph graph = processEngine.getManagementService().executeCommand(new FindGraphCmd(processDefId));
       // Graph graph = new FindGraphCmd(processDefinitionId)
      //  .execute(commandContext);
    	//List<Node> nodes = new ArrayList<Node>();
      //  Element data = new Element("Data");
       // Element record = new Element("Record");

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
				//Element newrec = new Element("Record");
				//XmlDocPkgUtil.setChildText(newrec, "id", node.getId());
				//XmlDocPkgUtil.setChildText(newrec, "name", node.getName());
				//XmlDocPkgUtil.setChildText(newrec, "type", node.getType());
				//data.addContent(newrec);
				
				//String[] returnData = { "id"};
				//Element resData = XmlDocPkgUtil.createMetaData(returnData);	
				//resData.addContent(XmlDocPkgUtil.createRecord(returnData,new String[] {node.getId()}));
				//nodes.add(node);
				// this.processUserTask(node, bpmnModel, priority++,
				// bpmConfBase);
			} else if ("startEvent".equals(node.getType())) {
				// this.processStartEvent(node, bpmnModel, priority++,
				// bpmConfBase);
			} else if ("endEvent".equals(node.getType())) {
				// this.processEndEvent(node, bpmnModel, priority++,
				// bpmConfBase);
			}
		}
		  // data.addContent(record);
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

	private void downProcessResource() {
    //	xmlDocUtil.getResponse().setContentType("image/png");
        //IOUtils.copy(is, response.getOutputStream());
    	// xmlDocUtil.getResponse()
    	Element reqElement =  xmlDocUtil.getRequestData();
    	//Element doc_file = reqElement.getChild("doc_file");//文档文件	
    	String processDefId = reqElement.getChildTextTrim("query_processDefId");
    	String resourceType = reqElement.getChildTextTrim("resourceType");
    	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefId).singleResult();
    	
    	  InputStream resourceAsStream = null;
    	  String resourceName = "";
          if (resourceType.equals("image")) {
              resourceName = processDefinition.getDiagramResourceName();
          } else if (resourceType.equals("xml")) {
              resourceName = processDefinition.getResourceName();
          }
          resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
          
          BufferedImage image = null;
		try {
			image = ImageIO.read(resourceAsStream);
		    File targetFile =  new File(FileUtil.getWebPath()+upFolder+resourceName);
            if(!targetFile.exists())targetFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(targetFile);
            ImageIO.write(image, "png", fos);
            
            ApplicationContext.getRequest().getSession().setAttribute("filename",resourceName);
	        ApplicationContext.getRequest().getSession().setAttribute("filepath",upFolder+resourceName);
	        xmlDocUtil.setResult("10");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void importProcessDefinition() {
    	Element reqElement =  xmlDocUtil.getRequestData();
		Element session = xmlDocUtil.getSession();
		Element doc_file = reqElement.getChild("doc_file");//文档文件	
		
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
			
		} catch (IOException e) {
			e.printStackTrace();
			log4j.logError("[流程定义上传]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		}		    
	}

	private final void queryProcessDefinition() {
		Element dataElement = xmlDocUtil.getRequestData();
        String processDefKey = dataElement.getChildTextTrim("query_processDefKey").toUpperCase() ;
        String processDefName = dataElement.getChildTextTrim("query_processDefName");    
		PlatformDao dao = null;
		try {
			dao = new PlatformDao();
			ArrayList bvals = new ArrayList();
			StringBuffer sqlBuf = new StringBuffer(
					"select RES.ID_ as processDefId, RES.NAME_ as processDefName,")
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
					if (StringUtil.isNotEmpty(processDefKey)){
					    sqlBuf.append(" and upper(RES.KEY_)=? ") ;
		                bvals.add(processDefKey);
		            }
					if (StringUtil.isNotEmpty(processDefName)){
					    sqlBuf.append(" and RES.NAME_ like ? ") ;
						bvals.add("%"+processDefName+"%");
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
