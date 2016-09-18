package cn.com.bright.workflow.api.persistence;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;

public class ProcessFormEntityManagerFactory implements SessionFactory {

	private ProcessFormEntityManager processFormEntityManager;

	public void setProcessFormEntityManager(ProcessFormEntityManager processFormEntityManager) {
		this.processFormEntityManager = processFormEntityManager;
	}

	public Class<?> getSessionType() {
		// TODO Auto-generated method stub
		return ProcessFormEntityManager.class;
	}

	public Session openSession() {
		// TODO Auto-generated method stub
		return processFormEntityManager;
	}

}
