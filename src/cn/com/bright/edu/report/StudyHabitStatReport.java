package cn.com.bright.edu.report;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.util.BaseReport;

public class StudyHabitStatReport extends BaseReport{

	public StudyHabitStatReport(){}
	public Map<String , Object> fillData(HashMap<String , Object> paraHashMap){
		Map<String , Object> resultMap =  new HashMap<String , Object>();
		StringBuffer strSQL = new StringBuffer();
		String qry_deptcode = (String)paraHashMap.get("qry_deptcode");
		String qry_year = (String)paraHashMap.get("qry_years");
		String qry_username = (String)paraHashMap.get("qry_username");
		
		if (StringUtil.isNotEmpty(qry_username)) {
			try {
				qry_username = URLDecoder.decode(qry_username, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		strSQL.append("select t3.deptname,t1.username,t5.classnm,t1.userid,t3.deptcode, ");
		strSQL.append(" (select count(*) from learn_comment st where st.valid = 'Y' and st.userid = t1.userid ");
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(st.create_date, '%Y')='"+qry_year+"' ");
		}
		strSQL.append(" ) as comment_count,");
		
		strSQL.append(" (select count(*) from learn_my_examination me where me.userid = t1.userid and me.valid = 'Y' and me.status > '10' ");
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(me.create_date, '%Y')='"+qry_year+"' ");
		}
		strSQL.append(" ) as submit_count,");
		
		strSQL.append(" (SELECT count(*) FROM sell_read_log rl WHERE rl.userid = t1.userid and rl.valid = 'Y' ");
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(rl.create_date, '%Y')='"+qry_year+"' ");
		}
		strSQL.append(" ) AS read_count,");
		
		strSQL.append(" (SELECT count(*) FROM pcmc_uslogin_log log WHERE log.userid = t1.userid and log.state = '1' ");
		if(StringUtil.isNotEmpty(qry_year)){
			strSQL.append(" and DATE_FORMAT(log.logindt, '%Y')='"+qry_year+"' ");
		}
		strSQL.append(" ) AS login_count");
		
		strSQL.append(" from pcmc_user t1,pcmc_user_dept t2,pcmc_dept t3,base_studentinfo t4,base_class t5 ");
	    strSQL.append("	where t1.userid = t2.userid and t2.deptid = t3.deptid and t1.userid = t4.userid and t4.classid = t5.classid");
	    strSQL.append(" and t3.state = '1' ");
	    	
	    if(StringUtil.isNotEmpty(qry_username)){
	    	strSQL.append(" and t1.username like '%"+qry_username+"%'");
	    }
	    
        strSQL.append("	and t3.deptcode like '"+qry_deptcode+"%' ");
        
        strSQL.append(" order by t3.deptcode,t5.classnm,t1.username");
		resultMap.put("qryStr", strSQL.toString());
		return resultMap;
	}
}
