package cn.com.bright.workflow.bpmn.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
/**
 * 
 * 
act_ru_identitylink
配置的是直接处理人，则在这个表里面存的是参与人,这样task.setAssignee编辑后这个人没被删掉,但是我的代办查询没根据
这个查询,所有没影响
配置的是候选人，则在这个表里面存的是候选人和参与人

act_hi_identitylink
配置的是直接处理人，则在这个表里面存的是参与人
配置的是候选人，则在这个表里面存的是候选人和参与人,因为新增参与人时不区分类型,所以如果流程实例的申请人也是下个某个
节点的处理人时候,这样这个表里不会新增一条参与类型的这个用户,导致在用participant查询参与的流程时可能不对

参与人只有流程实例id，没有taskid
候选人只有有taskid，没有流程实例id

根据以上规则,在线处理人表只对候选人删除,参与人因为挂钩流程实例id,删了的话可能别的任务的处理人也是这个也就是流程的参与人
历史处理人表记录都不删除,只是对人进行合并

 * @author lzc
 *
 */
public class EditTaskHandlerCmd implements Command<Void> {

    private String taskId;
    private String editAssigneeHandler;
    private Set<String> editCandidateHandlers;

    public EditTaskHandlerCmd(String taskId, String editAssigneeHandler, Set<String> editCandidateHandlers) {
        this.taskId = taskId;
        this.editAssigneeHandler = editAssigneeHandler;
        this.editCandidateHandlers = editCandidateHandlers;
    }

    public Void execute(CommandContext commandContext) {

        TaskEntity task = commandContext.getTaskEntityManager().findTaskById(taskId);
        if (null != task.getAssignee()) {
            task.setAssignee(editAssigneeHandler);
        } else {
            // List<String> editCandidateHandlerList =
            // Arrays.asList(editCandidateHandlers);

            List<String> sourceNotCandidates = new ArrayList<String>();

            Set<IdentityLink> candidates = task.getCandidates();
            for (IdentityLink identityLink : candidates) {
                if (!editCandidateHandlers.contains(identityLink.getUserId())) {
                    sourceNotCandidates.add(identityLink.getUserId());
                }
            }

            for (IdentityLink identityLink : candidates) {
                task.deleteCandidateUser(identityLink.getUserId());
            }

            for (String eachEditCandidateHandler : editCandidateHandlers) {
                task.addCandidateUser(eachEditCandidateHandler);
            }
            List<HistoricIdentityLink> historicIdentityLinks = (List) commandContext
                .getHistoricIdentityLinkEntityManager().findHistoricIdentityLinksByTaskId(taskId);

            /*
             * sourceNotCandidates表示编辑后的处理人在任务原处理人中不存在
             */
            for (String sourceNotCandidate : sourceNotCandidates) {
                if (!notExistInHistoricIdentity(sourceNotCandidate, historicIdentityLinks)) {
                    IdentityLinkEntity identityLinkEntity = new IdentityLinkEntity();
                    identityLinkEntity.setTask(task);
                    identityLinkEntity.setUserId(sourceNotCandidate);
                    identityLinkEntity.setGroupId(null);
                    identityLinkEntity.setType(IdentityLinkType.CANDIDATE);
                    identityLinkEntity.insert();
                    Context.getCommandContext().getHistoryManager()
                        .recordIdentityLinkCreated(identityLinkEntity);
                }
            }
        }
        return null;
    }

    private boolean notExistInHistoricIdentity(String sourceNotCandidate,
                                               List<HistoricIdentityLink> historicIdentityLinks) {
        for (HistoricIdentityLink historicIdentityLink : historicIdentityLinks) {
            if (sourceNotCandidate.equals(historicIdentityLink.getUserId())) {
                return true;
            }
        }
        return false;
    }
}
