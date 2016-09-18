package cn.com.bright.masterReview.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.util.WorkflowConstant;

/**
 * �������������ֱ����ֺ�,�����������������֮��Ĳ�ֵ�������Ƿ�Ҫ�ɽ��������´�����
 * @ClassName: PersonnelCompleteListener 
 * @Description: TODO
 * @author: lzc
 * @date: 2016��6��24�� ����2:06:36
 */
@Component
public class ReevaluateListener implements ExecutionListener  {
    
    /**
     * @fieldName: serialVersionUID
     * @fieldType: long
     * @Description: TODO
     */
    private static final long serialVersionUID = -1010821953329382311L;

    public void notify(DelegateExecution execution) throws Exception {
        
        String businessKey = execution.getProcessBusinessKey();
        
        execution.setVariable("isReevaluate", "0");
    }
}
