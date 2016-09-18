package cn.com.bright.masterReview.listener;

import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.com.bright.workflow.core.spring.ApplicationContextHelper;

public class ProfessorCompleteListener {
    
    public void notify(DelegateTask task) throws Exception {
        String userid = (String) ApplicationContext.getRequest().getSession().getAttribute("userid");

        String businessKey = task.getExecution().getProcessBusinessKey();

        //��ѯһ�����뵱��ĳ��У��������ר�Ҵ�ּ�¼
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT *");
        sql.append("  FROM headmaster_professor_grade where businessKey=?");
        sql.append("   ORDER BY sumGrade DESC");

        List<Map<String, Object>> list = ApplicationContextHelper.getJdbcTemplate().queryForList(sql.toString(),businessKey);
        String maxGrade = list.get(0).get("sumgrade").toString();
        String minGrade = list.get(list.size()-1).get("sumgrade").toString();
        
        //���ȥ����߷ֺ���ͷ�֮���ƽ����
        sql.setLength(0);
        sql.append(" SELECT avg(sumGrade)");
        sql.append("FROM headmaster_professor_grade where businessKey=?");
        sql.append("and sumGrade not in (?,?)");
        
        //У����ƽ��ר�ҵ÷�
        String avgGrade = ApplicationContextHelper.getJdbcTemplate().queryForObject(sql.toString(),
            String.class, businessKey);
        

    }
}
