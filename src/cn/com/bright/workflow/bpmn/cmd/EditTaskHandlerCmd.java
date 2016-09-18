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
���õ���ֱ�Ӵ����ˣ�����������������ǲ�����,����task.setAssignee�༭�������û��ɾ��,�����ҵĴ����ѯû����
�����ѯ,����ûӰ��
���õ��Ǻ�ѡ�ˣ�����������������Ǻ�ѡ�˺Ͳ�����

act_hi_identitylink
���õ���ֱ�Ӵ����ˣ�����������������ǲ�����
���õ��Ǻ�ѡ�ˣ�����������������Ǻ�ѡ�˺Ͳ�����,��Ϊ����������ʱ����������,�����������ʵ����������Ҳ���¸�ĳ��
�ڵ�Ĵ�����ʱ��,����������ﲻ������һ���������͵�����û�,��������participant��ѯ���������ʱ���ܲ���

������ֻ������ʵ��id��û��taskid
��ѡ��ֻ����taskid��û������ʵ��id

�������Ϲ���,���ߴ����˱�ֻ�Ժ�ѡ��ɾ��,��������Ϊ�ҹ�����ʵ��id,ɾ�˵Ļ����ܱ������Ĵ�����Ҳ�����Ҳ�������̵Ĳ�����
��ʷ�����˱��¼����ɾ��,ֻ�Ƕ��˽��кϲ�

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
             * sourceNotCandidates��ʾ�༭��Ĵ�����������ԭ�������в�����
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
