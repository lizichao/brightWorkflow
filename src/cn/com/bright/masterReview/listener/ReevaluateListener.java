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
 * 本区和其他区分别打完分后,来计算这两个审核人之间的差值来决定是否要由教育局人事处复评
 * @ClassName: PersonnelCompleteListener 
 * @Description: TODO
 * @author: lzc
 * @date: 2016年6月24日 下午2:06:36
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
