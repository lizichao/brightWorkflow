package cn.com.bright.workflow.el;

import java.util.Map;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.spring.SpringExpressionManager;
import org.springframework.context.ApplicationContext;

public class ExpressionManagerExt extends SpringExpressionManager {

    public ExpressionManagerExt(ApplicationContext applicationContext, Map<Object, Object> beans) {
        super(applicationContext, beans);
    }

    public Expression createUserExpression(String expression) {
        ValueExpression valueExpression = expressionFactory.createValueExpression(parsingElContext,
            expression.trim(), Object.class);
        return new UserExpression(valueExpression, expression);
    }

    public Expression createGroupExpression(String expression) {
        ValueExpression valueExpression = expressionFactory.createValueExpression(parsingElContext,
            expression.trim(), Object.class);
        return new GroupExpression(valueExpression, expression);
    }
}
