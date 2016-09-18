package cn.com.bright.workflow.api.persistence;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;

public class HistoricProcessFormEntityManagerFactory implements SessionFactory {

	private HistoricProcessFormEntityManager historicProcessFormEntityManager;

	public void setHistoricProcessFormEntityManager(
			HistoricProcessFormEntityManager historicProcessFormEntityManager) {
		this.historicProcessFormEntityManager = historicProcessFormEntityManager;
	}

	public Class<?> getSessionType() {
		// TODO Auto-generated method stub
		return HistoricProcessFormEntityManager.class;
	}

	public Session openSession() {
		// TODO Auto-generated method stub
		return historicProcessFormEntityManager;
	}
}
