package cn.com.bright.workflow.api.component;

import cn.com.bright.workflow.api.vo.CounterSignVO;


/**
 * ��ǩ��ǩ������û����
 * @author lzc
 *
 */

public interface ICounterFunctionNotifyListener {

	void notifyBeforeEvent(CounterSignVO counterSignVO) throws Exception;
	
	
	void notifyAfterEvent(CounterSignVO counterSignVO) throws Exception;
}
