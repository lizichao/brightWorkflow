package cn.com.bright.workflow.api.component;

import cn.com.bright.workflow.api.vo.EditCounterSignVO;

/**
 * ÐÞ¸ÄÁ÷³Ì¼àÌý
 * @author lzc
 *
 */
public interface IEditCounterFunctionListener {

	void notifyBeforeEvent(EditCounterSignVO editCounterSignVO)throws Exception;

	void notifyAfterEvent(EditCounterSignVO editCounterSignVO) throws Exception;
}
