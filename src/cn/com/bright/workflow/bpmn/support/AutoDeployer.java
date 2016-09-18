package cn.com.bright.workflow.bpmn.support;

import org.activiti.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * 
 */
public class AutoDeployer {
    private Logger logger = LoggerFactory.getLogger(AutoDeployer.class);
    private ProcessEngine processEngine;
    private Resource[] deploymentResources = new Resource[0];
    private boolean enable = true;

}
