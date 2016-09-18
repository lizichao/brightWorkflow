package cn.com.bright.workflow.el;

import org.activiti.engine.impl.el.JuelExpression;
import org.activiti.engine.impl.javax.el.ValueExpression;

public class GroupExpression extends JuelExpression {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3914411936088320364L;

    public GroupExpression(ValueExpression valueExpression, String expressionText) {
        super(valueExpression, expressionText);
    }
}
