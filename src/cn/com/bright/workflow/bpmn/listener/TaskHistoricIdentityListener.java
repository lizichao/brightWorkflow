package cn.com.bright.workflow.bpmn.listener;

import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricIdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;

import cn.com.bright.workflow.bpmn.support.DefaultTaskListener;

public class TaskHistoricIdentityListener extends DefaultTaskListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7608368098803293030L;

    public void onCreate(DelegateTask delegateTask) throws Exception {
        Set<IdentityLink> candidates = delegateTask.getCandidates();
        String processInstanceId = delegateTask.getProcessInstanceId();

        // Identity links from db
        List<HistoricIdentityLink> identityLinks = (List) Context.getCommandContext()
            .getHistoricIdentityLinkEntityManager()
            .findHistoricIdentityLinksByProcessInstanceId(processInstanceId);

        // Identity links from cache
        List<HistoricIdentityLinkEntity> identityLinksFromCache = Context.getCommandContext()
            .getDbSqlSession().findInCache(HistoricIdentityLinkEntity.class);
        identityLinks.addAll(identityLinksFromCache);

        ExecutionEntity executionEntity = Context.getCommandContext().getExecutionEntityManager()
            .findExecutionById(processInstanceId);

        for (IdentityLink identityLink : candidates) {
            if (!existsInHistoric(identityLink, identityLinks)) {
                IdentityLinkEntity identityLinkEntity = new IdentityLinkEntity();
                // identityLinkEntity.setTask(task);
                identityLinkEntity.setProcessInstance(executionEntity);
                identityLinkEntity.setUserId(identityLink.getUserId());
                identityLinkEntity.setGroupId(null);
                identityLinkEntity.setType(IdentityLinkType.PARTICIPANT);
                identityLinkEntity.insert();
                Context.getCommandContext().getHistoryManager()
                    .recordIdentityLinkCreated(identityLinkEntity);
            }
        }
    }

    private boolean existsInHistoric(IdentityLink identityLink,
                                     List<HistoricIdentityLink> historicIdentityLinks) {
        IdentityLinkEntity identityLinkEntity = (IdentityLinkEntity) identityLink;
        
        for (HistoricIdentityLink historicIdentityLink : historicIdentityLinks) {
            HistoricIdentityLinkEntity historicIdentityLinkEntity = (HistoricIdentityLinkEntity) historicIdentityLink;
            if (identityLinkEntity.getId().equals(historicIdentityLinkEntity.getId())) {
                return true;
            }
            if (identityLink.getUserId().equals(historicIdentityLink.getUserId())
                && identityLink.getType().equals(historicIdentityLink.getType())
                && (null != identityLink.getProcessInstanceId())
                && identityLink.getProcessInstanceId().equals(historicIdentityLink.getProcessInstanceId())) {
                return true;
            }
        }
        return false;
    }
}
