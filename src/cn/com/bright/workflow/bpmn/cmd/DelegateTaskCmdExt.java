package cn.com.bright.workflow.bpmn.cmd;

import java.util.List;
import java.util.Set;

import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;

public class DelegateTaskCmdExt extends NeedsActiveTaskCmd<Object> {

    private static final long serialVersionUID = 1L;
    protected String originUserId;
    protected String transferUserId;

    public DelegateTaskCmdExt(String taskId, String originUserId, String transferUserId) {
        super(taskId);
        this.originUserId = originUserId;
        this.transferUserId = transferUserId;
    }

    protected Object execute(CommandContext commandContext, TaskEntity task) {
        if (task.getAssignee() != null) {
            task.delegate(transferUserId);
        } else {
            task.deleteCandidateUser(originUserId);

            boolean isHaveTransfer = false;// 当前任务是否含有转审人
            Set<IdentityLink> candidates = task.getCandidates();
            for (IdentityLink identityLink : candidates) {
                if (transferUserId.equals(identityLink.getUserId())) {
                    isHaveTransfer = true;
                    break;
                }
            }
            if (!isHaveTransfer) {
                task.addCandidateUser(transferUserId);
            }

            List<HistoricIdentityLink> historicIdentityLinks = (List) commandContext
                .getHistoricIdentityLinkEntityManager().findHistoricIdentityLinksByTaskId(taskId);

            if (!notExistInHistoricIdentity(originUserId, historicIdentityLinks)) {
                IdentityLinkEntity identityLinkEntity = new IdentityLinkEntity();
                identityLinkEntity.setTask(task);
                identityLinkEntity.setUserId(originUserId);
                identityLinkEntity.setGroupId(null);
                identityLinkEntity.setType(IdentityLinkType.CANDIDATE);
                identityLinkEntity.insert();
                Context.getCommandContext().getHistoryManager()
                    .recordIdentityLinkCreated(identityLinkEntity);
            }
            // List<IdentityLink> identityLinks
            // =task.getIdentityLinksForTask(task.getId());
            // for (IdentityLink identityLinkEntity : identityLinks) {
            // if
            // (IdentityLinkType.CANDIDATE.equals(identityLinkEntity.getType()))
            // {
            // handlers.add(getUserInfo(identityLinkEntity.getUserId()));
            // }
            // }
        }
        return null;
    }

    private boolean notExistInHistoricIdentity(String originUserId2,
                                               List<HistoricIdentityLink> historicIdentityLinks) {
        for (HistoricIdentityLink historicIdentityLink : historicIdentityLinks) {
            if (originUserId2.equals(historicIdentityLink.getUserId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getSuspendedTaskException() {
        return "Cannot delegate a suspended task";
    }

}
