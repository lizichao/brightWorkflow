package cn.com.bright.workflow.bpmn.parser;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.el.UelExpressionCondition;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.parse.BpmnParseHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bright.workflow.util.WorkflowConstant;

/**
 * 路由解析扩展处理器
 * @author lzc
 */
public class ProxySequenceFlowParseHandler implements BpmnParseHandler {
    private static Logger logger = LoggerFactory.getLogger(ProxySequenceFlowParseHandler.class);
    public static final String PROPERTYNAME_CONDITION = "condition";
    public static final String PROPERTYNAME_CONDITION_TEXT = "conditionText";

    private List<String> takeSequenceFlowListeners;

    public Collection<Class<? extends BaseElement>> getHandledTypes() {
        List types = Collections.singletonList(SequenceFlow.class);
        return types;
    }

    public void parse(BpmnParse bpmnParse, BaseElement baseElement) {
        if (!(baseElement instanceof SequenceFlow)) {
            return;
        }

        SequenceFlow sequenceFlow = (SequenceFlow) baseElement;
        logger.info("bpmnParse : {}, sequenceFlow : {}", bpmnParse, sequenceFlow);

        TransitionImpl transition = bpmnParse.getSequenceFlows().get(sequenceFlow.getId());
        ActivityImpl activityImpl = transition.getSource();
        FlowElement flowElement = bpmnParse.getBpmnModel().getFlowElement(activityImpl.getId());
        int size = 0;
        if (flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            size = userTask.getOutgoingFlows().size();
        }

        List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
        if (size > 1 && activityImpl.getProperty("type").equals(WorkflowConstant.USERTASKTYPE)
            || activityImpl.getProperty("type").equals(WorkflowConstant.STARTEVENT)) {
            
            if (StringUtils.isEmpty(sequenceFlow.getConditionExpression())) {
                String flowId = sequenceFlow.getId();
                Condition expressionCondition = new UelExpressionCondition(bpmnParse.getExpressionManager().createExpression("${'" + flowId + "' == sequenceFlow}"));
                transition.setProperty(PROPERTYNAME_CONDITION_TEXT, "${'" + flowId + "' == sequenceFlow}");
                transition.setProperty(PROPERTYNAME_CONDITION, expressionCondition);
            }
        }

        for (String sequenceFlowListener : takeSequenceFlowListeners) {
            createSequenceFlowListener(bpmnParse, transition, sequenceFlowListener);
        }
    }

    private void createSequenceFlowListener(BpmnParse bpmnParse, TransitionImpl transition,
                                            String sequenceFlowListener) {
        ActivitiListener activitiListener = new ActivitiListener();
        activitiListener.setEvent(ExecutionListener.EVENTNAME_TAKE);
        activitiListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        activitiListener.setImplementation("#{" + sequenceFlowListener + "}");
        transition.addExecutionListener(bpmnParse.getListenerFactory()
            .createDelegateExpressionExecutionListener(activitiListener));
    }

    public List<String> getTakeSequenceFlowListeners() {
        return takeSequenceFlowListeners;
    }

    public void setTakeSequenceFlowListeners(List<String> takeSequenceFlowListeners) {
        this.takeSequenceFlowListeners = takeSequenceFlowListeners;
    }
}
