package cn.com.bright.workflow.api.component;

import java.util.Map;

/**
 * ��������¼�����
 * @author lzc
 *
 */
public interface IFunctionListener {

	void notifyEvent(Map<String, String> componentParamMap) throws Exception;
}
