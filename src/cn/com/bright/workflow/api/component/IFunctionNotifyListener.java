package cn.com.bright.workflow.api.component;

import java.util.Map;

/**
 * 功能组件前后自定义监听
 * 
 * @author lzc
 * 
 */
public interface IFunctionNotifyListener {

	void notifyBeforeEvent(String componentName,Map<String, String> componentParamMap) throws Exception;

	void notifyAfterEvent(String componentName,Map<String, String> componentParamMap) throws Exception;
}
