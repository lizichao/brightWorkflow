package cn.com.bright.edu.report;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.util.BaseReport;

public class TeacherResourceStatReport extends BaseReport{

	public TeacherResourceStatReport(){}
	public Map<String , Object> fillData(HashMap<String , Object> paraHashMap){
		Map<String , Object> resultMap =  new HashMap<String , Object>();
		
		StringBuffer strSQL = new StringBuffer();
		String qry_deptcode = (String)paraHashMap.get("qry_deptcode");
		
		String qry_years = (String)paraHashMap.get("qry_years");
		String qry_username = (String)paraHashMap.get("qry_username");
		
		if (StringUtil.isNotEmpty(qry_username)) {
			try {
				qry_username = URLDecoder.decode(qry_username, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		strSQL.append(" SELECT tt4.deptcode,tt4.deptname,tt2.username,tt1.*,");
		strSQL.append(" (SELECT pd.PARAMMEANINGS FROM param_detail pd WHERE pd.PARAMID='1552' AND pd.PARAMCODE = tt1.resource_type ) AS resource_type_text,");
		strSQL.append("	CONCAT((SELECT g.gradename FROM base_grade g WHERE g.gradecode = tt1.grade_code AND g.deptid = tt4.deptid),(SELECT s.subjname FROM base_subject s WHERE s.subjectid = tt1.subject_id)) AS grade_subject,");
		strSQL.append(" (SELECT COUNT(comment_id) FROM learn_comment st WHERE st.valid = 'Y' AND st.paper_id = tt1.paper_id) AS comment_count,");
		strSQL.append(" (SELECT COUNT(*) FROM learn_my_examination me WHERE me.paper_id = tt1.paper_id AND me.valid = 'Y' AND me.status > '10') AS submit_count,");
		strSQL.append(" (SELECT COUNT(send_id) FROM learn_paper_send st  WHERE st.valid = 'Y' AND st.paper_id = tt1.paper_id) AS send_count,");
		strSQL.append(" (SELECT COUNT(*) FROM sell_read_log t2 WHERE t2.paper_info_id = tt1.paper_id) AS readcount ");
	    strSQL.append("	FROM learn_examination_paper tt1,pcmc_user tt2,pcmc_user_dept tt3,pcmc_dept tt4");
	    strSQL.append("	WHERE tt1.user_id = tt2.userid AND tt2.userid = tt3.userid AND tt3.deptid = tt4.deptid AND tt1.valid = 'Y' AND tt4.state = '1'");
	    
	    if(StringUtil.isNotEmpty(qry_years)){
	    	strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
	    }
        
	    if(StringUtil.isNotEmpty(qry_username)){
	    	strSQL.append(" and tt2.username like '%"+qry_username+"%'");
	    }
	    
	    strSQL.append("	and tt4.deptcode like '"+qry_deptcode+"%' ");
		resultMap.put("qryStr", strSQL.toString());
		return resultMap;
	}
}
