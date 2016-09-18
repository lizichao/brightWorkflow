package cn.com.bright.workflow.api.component;

import java.util.Map;

/**
 * �������ǰ���Զ������
 * 
 * @author lzc
 * 
 */
public interface IFunctionNotifyListener {

	void notifyBeforeEvent(String componentName,Map<String, String> componentParamMap) throws Exception;

	void notifyAfterEvent(String componentName,Map<String, String> componentParamMap) throws Exception;
}
