package cn.com.bright.masterReview.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import cn.com.bright.masterReview.base.UserManage;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.NoApproveUsersException;

/**
 * ���¸ɲ�������������ʼ��ר�Ҽ���
 * @ClassName: ProfessorInitListener 
 * @Description: TODO
 * @author: lzc
 * @date: 2016��5��16�� ����2:28:16
 */
@Component
public class ProfessorInitListener {
    
    public void notify(DelegateExecution execution) throws Exception {
        String businessKey  = execution.getProcessBusinessKey();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT *");
        sql.append("FROM headmaster_user_group b");
        sql.append(" WHERE     b.group_id =");
        sql.append("(SELECT a.group_id");
        sql.append(" FROM headmaster t");
        sql.append(" LEFT JOIN headmaster_user_group a");
        sql.append("  ON t.headerMasterId = a.user_id");
        sql.append("  WHERE t.id = ?)");
        sql.append("   AND b.user_type = ?");
        
        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(),businessKey,UserManage.PROFESSOR_TYPE);
      
        if(CollectionUtils.isEmpty(list)){
            throw new NoApproveUsersException("�¼������δ�ҵ�������ϵ����Ա���ã�");
        }
             
        for (Map<String, Object> map : list) {
            String user_id = map.get("user_id").toString();
        }
             
        List<String> allHandlers = new ArrayList<String>();
        allHandlers.add("4028814d5499edd2015499ef384f0002");//zhuan1
        execution.setVariable("professors", allHandlers);
    }
}
