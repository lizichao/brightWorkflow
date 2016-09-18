package cn.com.bright.masterReview.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.NoApproveUsersException;

/**
 *  ��ʼ��ѧУ�����������������
 * @ClassName: PersonnelInitListener 
 * @Description: TODO
 * @author: lzc
 * @date: 2016��5��16�� ����2:28:00
 */
@Component
public class AreaInitListener implements ExecutionListener {
    
    /**
     * @fieldName: serialVersionUID
     * @fieldType: long
     * @Description: TODO
     */
    private static final long serialVersionUID = -1010821953329382311L;

    public void notify(DelegateExecution execution) throws Exception {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");
        String businessKey = execution.getProcessBusinessKey();
        
        //��ѯһ�����뵱��У�����ڵ�������code
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT a.deptcode");
        sql.append(" FROM pcmc_user_dept t INNER JOIN pcmc_dept a ON t.deptid = a.deptid");
        sql.append(" WHERE t.userid =?");

        String masterDeptcode = ApplicationContextHelper.getJdbcTemplate().queryForObject(sql.toString(),String.class, userid);
        masterDeptcode= masterDeptcode.substring(0,9);
        
        //���У���������Ķ�Ӧ����������Ա
        sql.setLength(0);
        sql.append("SELECT t.userid");
        sql.append(" FROM headmaster_arealeader_info t");
        sql.append(" INNER JOIN pcmc_user_dept a ON t.userid = a.userid");
        sql.append(" INNER JOIN pcmc_dept b ON a.deptid = b.deptid");
        sql.append(" WHERE b.deptcode = ?");
 

        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(),masterDeptcode);
        if(CollectionUtils.isEmpty(list)){
            throw new NoApproveUsersException("�¼������δ�ҵ�������ϵ����Ա���ã�");
        }
        list.get(0).get("userid");
        
        
         List<String> allHandlers = new ArrayList<String>();
         allHandlers.add("4028814d5499edd2015499efc4670004");
         execution.setVariable("personalUsers", allHandlers);
    }
}
