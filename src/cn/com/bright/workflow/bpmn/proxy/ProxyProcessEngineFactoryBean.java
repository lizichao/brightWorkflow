package cn.com.bright.workflow.bpmn.proxy;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cfg.SpringBeanFactoryProxyMap;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.com.bright.workflow.el.ExpressionManagerExt;

public class ProxyProcessEngineFactoryBean implements InitializingBean, DisposableBean,
    FactoryBean<ProcessEngine>, ApplicationContextAware {
    private ProxyProcessEngine proxyProcessEngine = new ProxyProcessEngine();
    private ProcessEngineConfigurationImpl processEngineConfiguration;
    private ApplicationContext applicationContext;
    private boolean enabled = true;

    public void afterPropertiesSet() throws Exception {
        if (!enabled) {
            return;
        }
        configureExpressionManager();

        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(processEngineConfiguration);
        processEngineFactoryBean.setApplicationContext(applicationContext);
        if (processEngineConfiguration.getBeans() == null) {
            processEngineConfiguration.setBeans(new SpringBeanFactoryProxyMap(applicationContext));
        }

        ProcessEngine processEngine = processEngineFactoryBean.getObject();
        proxyProcessEngine.setProcessEngine(processEngine);
    }

    public void destroy() throws Exception {
        if (proxyProcessEngine != null) {
            proxyProcessEngine.close();
        }
    }

    public ProcessEngine getObject() {
        return proxyProcessEngine;
    }

    private void configureExpressionManager() {
        if (processEngineConfiguration.getExpressionManager() == null && applicationContext != null) {
            processEngineConfiguration.setExpressionManager(new ExpressionManagerExt(applicationContext,
                processEngineConfiguration.getBeans()));
        }
    }

    public Class<ProcessEngine> getObjectType() {
        return ProcessEngine.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
