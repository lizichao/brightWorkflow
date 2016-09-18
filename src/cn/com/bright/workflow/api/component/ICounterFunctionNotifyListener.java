package cn.com.bright.workflow.api.component;

import cn.com.bright.workflow.api.vo.CounterSignVO;


/**
 * 加签减签监听，没用了
 * @author lzc
 *
 */

public interface ICounterFunctionNotifyListener {

	void notifyBeforeEvent(CounterSignVO counterSignVO) throws Exception;
	
	
	void notifyAfterEvent(CounterSignVO counterSignVO) throws Exception;
}
