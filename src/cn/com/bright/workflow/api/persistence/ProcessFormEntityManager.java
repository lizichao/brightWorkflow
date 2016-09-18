package cn.com.bright.workflow.api.persistence;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.springframework.beans.BeanUtils;

public class ProcessFormEntityManager extends AbstractManager {

	public void insertProcessForm(ProcessFormEntity processFormEntity) {
		HistoricProcessFormEntity historicProcessFormEntity = new HistoricProcessFormEntity();
		BeanUtils.copyProperties(processFormEntity, historicProcessFormEntity);

		getDbSqlSession().insert((PersistentObject) processFormEntity);
		getDbSqlSession().insert((PersistentObject) historicProcessFormEntity);

		if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
			getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(
					ActivitiEventBuilder
							.createEntityEvent(
									ActivitiEventType.ENTITY_CREATED,processFormEntity));
			getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(
					ActivitiEventBuilder.createEntityEvent(
							ActivitiEventType.ENTITY_INITIALIZED,processFormEntity));
		}
	}

	public void updateProcessForm(ProcessFormEntity processFormEntity) {
		CommandContext commandContext = Context.getCommandContext();
		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
		dbSqlSession.update((ProcessFormEntity) processFormEntity);

		if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
			getProcessEngineConfiguration().getEventDispatcher().dispatchEvent(
					ActivitiEventBuilder
							.createEntityEvent(
									ActivitiEventType.ENTITY_UPDATED,
									processFormEntity));
		}
	}

	public void deleteProcessForm(String processFormId) {
		ProcessFormEntity processFormEntity = getDbSqlSession().selectById(
				ProcessFormEntity.class, processFormId);

		if (processFormEntity != null) {

			getDbSqlSession().delete(processFormEntity);

			if (getProcessEngineConfiguration().getEventDispatcher().isEnabled()) {
				getProcessEngineConfiguration().getEventDispatcher()
						.dispatchEvent(ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_DELETED,processFormEntity));
			}
		}
	}

	public ProcessFormEntity findProcessFormById(String processInstanceId) {
		if (processInstanceId == null) {
			throw new ActivitiIllegalArgumentException("Invalid processFormId id : null");
		}
		if (getHistoryManager().isHistoryEnabled()) {
			ProcessFormEntity processFormEntity = (ProcessFormEntity) getDbSqlSession()
					.selectOne("selectProcessForm", processInstanceId);
			if (null == processFormEntity) {
				processFormEntity = Context
						.getCommandContext()
						.getDbSqlSession()
						.findInCache(ProcessFormEntity.class, processInstanceId);
			}
			return processFormEntity;
		}
		return null;
	}
}
