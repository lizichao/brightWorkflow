package cn.com.bright.workflow.api.component;

import java.util.Map;

/**
 * 功能组件事件监听
 * @author lzc
 *
 */
public interface IFunctionListener {

	void notifyEvent(Map<String, String> componentParamMap) throws Exception;
}
