package cn.com.bright.workflow.bpmn.parser;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.parse.BpmnParseHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bright.workflow.bpmn.listener.ProcessUpdateFormListener;
import cn.com.bright.workflow.el.ExpressionManagerExt;

/**
 * 后置用户任务解析器
 * @author lzc
 */
public class ProxyUserTaskBpmnParseHandler implements BpmnParseHandler {
    private static Logger logger = LoggerFactory.getLogger(ProxyUserTaskBpmnParseHandler.class);
    private String taskListenerId;
    private boolean useDefaultUserTaskParser;

    public void parse(BpmnParse bpmnParse, BaseElement baseElement) {
        if (!(baseElement instanceof UserTask)) {
            return;
        }

        if (useDefaultUserTaskParser) {
            new UserTaskParseHandler().parse(bpmnParse, baseElement);
        }

        UserTask userTask = (UserTask) baseElement;
        logger.info("bpmnParse : {}, userTask : {}", bpmnParse, userTask);

        TaskDefinition taskDefinition = (TaskDefinition) bpmnParse.getCurrentActivity().getProperty(
            UserTaskParseHandler.PROPERTY_TASK_DEFINITION);
        ExpressionManagerExt expressionManager = (ExpressionManagerExt) bpmnParse.getExpressionManager();

        if (StringUtils.isNotEmpty(userTask.getAssignee())) {
            taskDefinition.setAssigneeExpression(expressionManager.createUserExpression(userTask.getAssignee()));
        }
        if (StringUtils.isNotEmpty(userTask.getOwner())) {
            taskDefinition.setOwnerExpression(expressionManager.createUserExpression(userTask.getOwner()));
        }
        // for(Expression expression :
        // taskDefinition.getCandidateUserIdExpressions()){
        // expression = null;
        // }
        Field fieldUserId = ReflectUtil.getField("candidateUserIdExpressions", taskDefinition);
        ReflectUtil.setField(fieldUserId, taskDefinition, new HashSet<Expression>());

        Field fieldGroupId = ReflectUtil.getField("candidateGroupIdExpressions", taskDefinition);
        ReflectUtil.setField(fieldGroupId, taskDefinition, new HashSet<Expression>());
        for (String candidateUser : userTask.getCandidateUsers()) {
            taskDefinition.addCandidateUserIdExpression(expressionManager.createUserExpression(candidateUser));
        }
        for (String candidateGroup : userTask.getCandidateGroups()) {
            taskDefinition.addCandidateGroupIdExpression(expressionManager.createGroupExpression(candidateGroup));
        }

        this.configEvent(taskDefinition, bpmnParse, TaskListener.EVENTNAME_CREATE);
        this.configEvent(taskDefinition, bpmnParse, TaskListener.EVENTNAME_ASSIGNMENT);
        this.configEvent(taskDefinition, bpmnParse, TaskListener.EVENTNAME_COMPLETE);
        this.configEvent(taskDefinition, bpmnParse, TaskListener.EVENTNAME_DELETE);
        bpmnParse.getCurrentActivity().addExecutionListener(org.activiti.engine.impl.pvm.PvmEvent.EVENTNAME_END, new ProcessUpdateFormListener());
        bpmnParse.getCurrentActivity().setProperty(UserTaskParseHandler.PROPERTY_TASK_DEFINITION,taskDefinition);
    }

    public void configEvent(TaskDefinition taskDefinition, BpmnParse bpmnParse, String eventName) {
        ActivitiListener activitiListener = new ActivitiListener();
        activitiListener.setEvent(eventName);
        activitiListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        activitiListener.setImplementation("#{" + taskListenerId + "}");
        taskDefinition.addTaskListener(eventName, bpmnParse.getListenerFactory().createDelegateExpressionTaskListener(activitiListener));
    }

    public Collection<Class<? extends BaseElement>> getHandledTypes() {
        List types = Collections.singletonList(UserTask.class);
        return types;
    }

    public void setTaskListenerId(String taskListenerId) {
        this.taskListenerId = taskListenerId;
    }

    public void setUseDefaultUserTaskParser(boolean useDefaultUserTaskParser) {
        this.useDefaultUserTaskParser = useDefaultUserTaskParser;
    }
}
