package cn.com.bright.workflow.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;

import cn.com.bright.workflow.api.vo.UserVO;

public class TaskQueryService {

    @Resource
    private TaskService taskService;

    @Resource
    private UserQueryService userQueryService;

    // @Resource
    // private IdentityService identityService;

    public List<Task> findTaskByProcessInstanceId(String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        return tasks;
    }

    public Task findTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return task;
    }

    public Set<UserVO> searchTaskhandler(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return this.searchTaskhandler(task);
    }

    public Set<UserVO> searchTaskhandler(Task task) {
        HashSet<UserVO> handlers = new HashSet<UserVO>();
        if (task.getAssignee() != null) {
            handlers.add(userQueryService.getUserVO(task.getAssignee()));
        } else {
            List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
            List<String> userIds = new ArrayList<String>();
            for (IdentityLink identityLinkEntity : identityLinks) {
                if (IdentityLinkType.CANDIDATE.equals(identityLinkEntity.getType())) {
                    userIds.add(identityLinkEntity.getUserId());
                    // handlers.add(getUserInfo(identityLinkEntity.getUserId()));
                }
            }
            handlers = new HashSet(userQueryService.getMultiUserVO(userIds));
        }
        return handlers;
    }

    public HashSet<String> searchTaskhandlerStr(Task task) {
        HashSet<String> handlers = new HashSet<String>();
        if (task.getAssignee() != null) {
            handlers.add(task.getAssignee());
        } else {
            List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
            List<String> userIds = new ArrayList<String>();
            for (IdentityLink identityLinkEntity : identityLinks) {
                if (IdentityLinkType.CANDIDATE.equals(identityLinkEntity.getType())) {
                    handlers.add(identityLinkEntity.getUserId());
                    // userIds.add(identityLinkEntity.getUserId());
                    // handlers.add(getUserInfo(identityLinkEntity.getUserId()));
                }
            }
            // handlers = new HashSet(userQueryService.getMultiUserVO(userIds));
        }
        return handlers;
    }
}
