package cn.com.bright.workflow.bpmn.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.spring.SpringProcessEngineConfiguration;

import cn.com.bright.workflow.service.FormResourceService;
import cn.com.bright.workflow.service.ProcessFormService;
import cn.com.bright.workflow.service.TaskServiceImplExt;

public class ProxyProcessEngineConfigurationImpl extends SpringProcessEngineConfiguration {

    @Resource
    protected TaskServiceImplExt taskServiceImplExt;

    @Resource
    protected FormResourceService formResourceService;

    @Resource
    protected ProcessFormService processFormService;

    @Override
    public ProcessEngine buildProcessEngine() {
        super.init();
        buildTaskServiceExt();
        buildFormServiceExt();
        buildProcessFormService();
        // formEngines.put(formEngine.getName(), formEngine);
        ProcessEngine processEngine = new ProcessEngineImpl(this);
        ProcessEngines.setInitialized(true);
        autoDeployResources(processEngine);
        return processEngine;
    }

    private void buildProcessFormService() {
        ((ServiceImpl) processFormService).setCommandExecutor(commandExecutor);
    }

    private void buildFormServiceExt() {
        ((ServiceImpl) formResourceService).setCommandExecutor(commandExecutor);
        this.setFormService(formResourceService);
    }

    private void buildTaskServiceExt() {
        ((ServiceImpl) taskServiceImplExt).setCommandExecutor(commandExecutor);
        this.setTaskService(taskServiceImplExt);
    }

    protected InputStream getMyBatisXmlConfigurationSteam() {
        String basePath = System.getProperty("brightcom.root");
        // String
        // gg="D:/developProject/workflow/web/WEB-INF/config/workflow/db/mapping/mappings.xml";
        // gg="WEB-INF/config/workflow/db/mapping/mappings.xml";
        // gg=FileUtil.getWebPath()+"/workflow/db/mapping/mappings.xml"
        // return getResourceAsStream(gg);
        File file = new File(basePath + "WEB-INF/config/workflow/db/mapping/mappings.xml");
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return in;
    }
}
