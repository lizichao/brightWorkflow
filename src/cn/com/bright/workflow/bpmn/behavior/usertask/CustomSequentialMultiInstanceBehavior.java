package cn.com.bright.workflow.bpmn.behavior.usertask;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bright.workflow.util.WorkflowConstant;

public class CustomSequentialMultiInstanceBehavior extends SequentialMultiInstanceBehavior {
    private static Logger log = LoggerFactory.getLogger(CustomParallelMultiInstanceBehavior.class);

    public CustomSequentialMultiInstanceBehavior(ActivityImpl activity,
        AbstractBpmnActivityBehavior originalActivityBehavior) {
        super(activity, originalActivityBehavior);
    }

    protected void createInstances(ActivityExecution execution) throws Exception {
        log.info("创建多实例开始啦: {}  ", execution);
        super.createInstances(execution);
    }

    public void setCompletionConditionExpression(Expression completionConditionExpression) {
        log.info("你要表达式做什么用?: {}  ", completionConditionExpression.getExpressionText());
        super.setCompletionConditionExpression(completionConditionExpression);
    }

    protected boolean completionConditionSatisfied(ActivityExecution execution) {
        Object value = execution.getVariable(WorkflowConstant.MULTI_COMPLETE_MARK);
        if (value != null) {
            // execution.removeVariable(WorkflowConstant.MULTI_COMPLETE_MARK);
            // Boolean booleanValue = (Boolean) value;
            return true;
        }
        return super.completionConditionSatisfied(execution);
    }
}
