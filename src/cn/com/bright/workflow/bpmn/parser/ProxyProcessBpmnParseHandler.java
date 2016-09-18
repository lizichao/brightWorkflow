package cn.com.bright.workflow.bpmn.parser;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ProcessParseHandler;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.parse.BpmnParseHandler;

import cn.com.bright.workflow.el.ExpressionManagerExt;
import cn.com.bright.workflow.util.WorkflowConstant;

/**
 * 后置流程解析器
 * @author lzc
 */
public class ProxyProcessBpmnParseHandler implements BpmnParseHandler {

    private List<String> startProcessListeners;
    private List<String> endProcessListeners;

    private boolean useDefaultExecutionParser;

    public Collection<Class<? extends BaseElement>> getHandledTypes() {
        List types = Collections.singletonList(Process.class);
        return types;
    }

    public void parse(BpmnParse bpmnParse, BaseElement element) {
        if (!(element instanceof Process)) {
            return;
        }

        if (useDefaultExecutionParser) {
            new ProcessParseHandler().parse(bpmnParse, element);
        }

        Process process = (Process) element;
        ProcessDefinitionEntity processDefinitionEntity = bpmnParse.getCurrentProcessDefinition();
        processDefinitionEntity.setProperty(BpmnParse.PROPERTYNAME_INITIATOR_VARIABLE_NAME,
            WorkflowConstant.PROCESS_CREATER);

        ExpressionManagerExt expressionManager = (ExpressionManagerExt) bpmnParse.getExpressionManager();
        for (String candidateUser : process.getCandidateStarterUsers()) {
            processDefinitionEntity.addCandidateStarterUserIdExpression(expressionManager
                .createUserExpression(candidateUser));
        }

        for (String candidateGroup : process.getCandidateStarterGroups()) {
            processDefinitionEntity.addCandidateStarterGroupIdExpression(expressionManager
                .createGroupExpression(candidateGroup));
        }
        for (String startProcessListener : startProcessListeners) {
            createProcessListener(processDefinitionEntity, bpmnParse, ExecutionListener.EVENTNAME_START,
                startProcessListener);
        }
        for (String endProcessListener : endProcessListeners) {
            createProcessListener(processDefinitionEntity, bpmnParse, ExecutionListener.EVENTNAME_END,
                endProcessListener);
        }
    }

    private void createProcessListener(ProcessDefinitionEntity processDefinitionEntity,
                                       BpmnParse bpmnParse, String eventnameStart,
                                       String startProcessListener) {

        ActivitiListener activitiListener = new ActivitiListener();
        activitiListener.setEvent(eventnameStart);
        activitiListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        activitiListener.setImplementation("#{" + startProcessListener + "}");
        bpmnParse.getListenerFactory().createDelegateExpressionExecutionListener(activitiListener);
        processDefinitionEntity.addExecutionListener(eventnameStart, bpmnParse.getListenerFactory()
            .createDelegateExpressionExecutionListener(activitiListener));
    }

    public List<String> getStartProcessListeners() {
        return startProcessListeners;
    }

    public void setStartProcessListeners(List<String> startProcessListeners) {
        this.startProcessListeners = startProcessListeners;
    }

    public List<String> getEndProcessListeners() {
        return endProcessListeners;
    }

    public void setEndProcessListeners(List<String> endProcessListeners) {
        this.endProcessListeners = endProcessListeners;
    }

    public boolean isUseDefaultExecutionParser() {
        return useDefaultExecutionParser;
    }

    public void setUseDefaultExecutionParser(boolean useDefaultExecutionParser) {
        this.useDefaultExecutionParser = useDefaultExecutionParser;
    }
}
