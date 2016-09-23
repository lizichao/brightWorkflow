package cn.com.bright.masterReview.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import cn.com.bright.workflow.core.spring.ApplicationContextHelper;
import cn.com.bright.workflow.exception.NoApproveUsersException;

/**
 *  ����������������ʼ�����¸ɲ������������
 * @ClassName: PersonnelInitListener 
 * @Description: TODO
 * @author: lzc
 * @date: 2016��5��16�� ����2:28:00
 */
@Component
public class PersonnelCrossInitListener implements ExecutionListener {
    
    /**
     * @fieldName: serialVersionUID
     * @fieldType: long
     * @Description: TODO
     */
    private static final long serialVersionUID = -1010821953329382311L;

    public void notify(DelegateExecution execution) throws Exception {
        
        String businessKey = execution.getProcessBusinessKey();
        
        //��ѯһ�����뵱��У�����ڵ�������code
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT  b.deptcode");
        sql.append(" FROM headmaster t ");
        sql.append(" INNER JOIN pcmc_user_dept a ON t.headerMasterId = a.userid");
        sql.append("  LEFT JOIN pcmc_dept b ON a.deptid = b.deptid");
        sql.append(" WHERE t.id = ?");

        String masterDeptcode = ApplicationContextHelper.getJdbcTemplate().queryForObject(sql.toString(),String.class, businessKey);
       masterDeptcode= masterDeptcode.substring(0,8);
        
        //��ѯ�����������¸ɲ������Ұ����¸ɲ�����ֵ�У�������������򣬵�һ���Ǵ�����ٵ����¸ɲ�(������û����ֵ����¸ɲ�)
        sql.setLength(0);
        sql.append(" SELECT t.userid,");
        sql.append("  CASE WHEN a.approvenum IS NOT NULL THEN a.approvenum ELSE '0' END AS approvenums");
        sql.append(" FROM headmaster_personalleader_info t");
        sql.append(" LEFT JOIN (SELECT personnel_leader, count(1) AS approvenum");
        sql.append("    FROM headmaster_personnel_leader_grade");
        sql.append("  GROUP BY personnel_leader) a");
        sql.append("   ON t.userid = a.personnel_leader");
        sql.append(" INNER JOIN pcmc_user_dept b ON t.userid = b.userid");
        sql.append(" INNER JOIN pcmc_dept c ON b.deptid = c.deptid");
        sql.append(" WHERE c.deptcode != ?");
        sql.append(" ORDER BY approvenums");
 

        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(),masterDeptcode);
        if(CollectionUtils.isEmpty(list)){
            throw new NoApproveUsersException("�¼������δ�ҵ�������ϵ����Ա���ã�");
        }
      //  list.get(0).get("userid");
        
        
         List<String> allHandlers = new ArrayList<String>();
         allHandlers.add("4028814d5499edd2015499efc4670004");
       //  execution.setVariable("personalUsers", allHandlers);
         execution.setVariable("personalCrossUser", list.get(0).get("userid"));
    }
}
