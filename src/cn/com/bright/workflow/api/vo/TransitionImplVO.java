package cn.com.bright.workflow.api.vo;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;

public class TransitionImplVO extends TransitionImpl {
    private static final long serialVersionUID = 1L;

    public TransitionImplVO(String id, Expression skipExpression, ProcessDefinitionImpl processDefinition) {
        super(id, skipExpression, processDefinition);
        // TODO Auto-generated constructor stub
    }

    public void setSource(ActivityImpl source) {
        this.source = source;
    }
}
