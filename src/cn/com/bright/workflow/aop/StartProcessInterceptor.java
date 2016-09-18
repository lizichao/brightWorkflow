package cn.com.bright.workflow.aop;

import org.aopalliance.intercept.MethodInterceptor ;
import org.aopalliance.intercept.MethodInvocation;

public class StartProcessInterceptor implements MethodInterceptor{

	public Object invoke(MethodInvocation invo) throws Throwable {
		  Object[] object = invo.getArguments();		
		  System.out.print("ddddddddddd");
		  return null;
	}

}
