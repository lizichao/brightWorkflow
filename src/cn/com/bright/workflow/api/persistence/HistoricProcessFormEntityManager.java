package cn.com.bright.workflow.api.persistence;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;

public class HistoricProcessFormEntityManager extends AbstractManager {

	public void insertHistoricProcessForm(
			HistoricProcessFormEntity historicProcessFormEntity) {

		getDbSqlSession().insert((PersistentObject) historicProcessFormEntity);

		if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
			getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(
					ActivitiEventBuilder.createEntityEvent(
							ActivitiEventType.ENTITY_CREATED,
							historicProcessFormEntity));
			getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(
					ActivitiEventBuilder.createEntityEvent(
							ActivitiEventType.ENTITY_INITIALIZED,
							historicProcessFormEntity));
		}
	}

	public void updateHistoricProcessForm(
			HistoricProcessFormEntity historicProcessFormEntity) {
		CommandContext commandContext = Context.getCommandContext();
		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
		dbSqlSession.update((HistoricProcessFormEntity) historicProcessFormEntity);

		if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
			getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(
					ActivitiEventBuilder.createEntityEvent(
							ActivitiEventType.ENTITY_UPDATED,
							historicProcessFormEntity));
		}
	}

	public void deleteHistoricProcessForm(String processFormId) {
		HistoricProcessFormEntity historicProcessFormEntity = getDbSqlSession().selectById(HistoricProcessFormEntity.class, processFormId);

		if (historicProcessFormEntity != null) {
			getDbSqlSession().delete(historicProcessFormEntity);

			if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
				getProcessEngineConfiguration().getEventDispatcher()
						.dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_DELETED,historicProcessFormEntity));
			}
		}
	}

	public HistoricProcessFormEntity findHistoricProcessFormById(
			String processInstanceId) {
		if (processInstanceId == null) {
			throw new ActivitiIllegalArgumentException("Invalid processFormId id : null");
		}
		if (getHistoryManager().isHistoryEnabled()) {
			HistoricProcessFormEntity historicProcessFormEntity = (HistoricProcessFormEntity) getDbSqlSession()
					.selectOne("selectHistoricProcessForm", processInstanceId);
			if (null == historicProcessFormEntity) {
				historicProcessFormEntity = Context
						.getCommandContext()
						.getDbSqlSession()
						.findInCache(HistoricProcessFormEntity.class,processInstanceId);
			}
			return historicProcessFormEntity;
		}
		return null;
	}
}
