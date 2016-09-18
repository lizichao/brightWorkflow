package cn.com.bright.edu.report;

import java.util.HashMap;
import java.util.Map;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.util.BaseReport;

public class UserGrowthStatReport extends BaseReport{

	public UserGrowthStatReport(){}
	public Map<String , Object> fillData(HashMap<String , Object> paraHashMap){
		Map<String , Object> resultMap =  new HashMap<String , Object>();
		StringBuffer strSQL = new StringBuffer();
		
		String qry_deptcode = (String)paraHashMap.get("qry_deptcode");
		String qry_years = (String)paraHashMap.get("qry_years");
		
		String qry_stat_type = (String)paraHashMap.get("qry_stat_type");

		strSQL.append(" select *,stu_total_count-stu_primary_school_count as stu_middle_school_count,th_total_count-th_primary_school_count as th_middle_school_count from ( ");
		strSQL.append("	SELECT t1.deptid, t1.deptname, t1.deptcode,");
		//学生总数
		strSQL.append(" (select count(distinct tt2.userid) from base_studentinfo tt2,pcmc_dept tt3,pcmc_user tt5 where  tt2.deptid = tt3.deptid ");
		strSQL.append(" AND tt2.userid = tt5.userid AND tt3.state='1' and tt5.usertype='1' and tt5.state='1' and tt3.deptcode like CONCAT(t1.deptcode,'%')");

		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') = '"+qry_years+"'");
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') >=2015");
			}
	    }
		strSQL.append(" ) AS stu_total_count,");
		//教师总数
		strSQL.append("	(select count(distinct tt2.userid) from base_teacher_info tt2,pcmc_dept tt3,pcmc_user_dept tt4,pcmc_user tt5 where tt2.userid = tt4.userid and tt4.deptid = tt3.deptid AND tt2.userid=tt5.userid and tt5.usertype='2' and tt5.state='1' AND tt3.state='1' and tt3.deptcode like CONCAT(t1.deptcode,'%') ");
		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') = '"+qry_years+"'");
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') >=2015");
			}
	    }
		strSQL.append(" ) AS th_total_count, ");
		//小学生总数
		strSQL.append("	(select count(DISTINCT tt2.userid) from base_class tt1,base_studentinfo tt2,pcmc_dept tt3,pcmc_user tt5 where tt1.classid=tt2.classid and tt2.deptid = tt3.deptid AND tt2.userid = tt5.userid  AND tt3.state='1' and tt5.usertype='1' and tt5.state='1' and tt3.deptcode like CONCAT(t1.deptcode,'%') and tt1.gradecode<=6  ");
		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') = '"+qry_years+"'");
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') >=2015");
			}
	    }
		strSQL.append(" ) AS stu_primary_school_count,");
		// 小学教师总数
		strSQL.append("	(select count(distinct tt1.userid) from base_teacher_info tt1,base_teacher_subject tt2,pcmc_dept tt3,pcmc_user_dept tt4,pcmc_user tt5 where tt1.userid = tt4.userid and tt4.deptid = tt3.deptid and tt1.userid = tt5.userid AND tt2.userid = tt5.userid AND tt3.state='1' and tt5.usertype='2' and tt5.state='1' and tt3.deptcode like CONCAT(t1.deptcode,'%')  ");
		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') = '"+qry_years+"'");
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt5.create_date, '%Y') >=2015");
			}
	    }
		strSQL.append(" and exists(select null from base_class tt1 where tt1.classid=tt2.classid and tt1.gradecode<=6)) AS th_primary_school_count,");
		//小学班级总数
		strSQL.append("(select count(*) from base_class tt1,pcmc_dept tt2 where tt1.deptid=tt2.deptid and tt2.state='1' and tt2.deptcode like CONCAT(t1.deptcode,'%') and tt1.gradecode<=6 ");
		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt1.createdt, '%Y') = '"+qry_years+"'");
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt1.createdt, '%Y') >=2015");
			}
	    }
		strSQL.append(" ) as primary_school_class_count,");
		//初中班级总数 
		strSQL.append("	(select count(*) from base_class tt1,pcmc_dept tt2 where tt1.deptid=tt2.deptid and tt2.state='1' and tt2.deptcode like CONCAT(t1.deptcode,'%') and tt1.gradecode>6 ");
		if("1".equals(qry_stat_type)){
	    	if(StringUtil.isNotEmpty(qry_years)){
	    		strSQL.append(" and DATE_FORMAT(tt1.createdt, '%Y') = '"+qry_years+"'");
	    	}else {
	    		strSQL.append(" and DATE_FORMAT(tt1.createdt, '%Y') >=2015");
			}
	    }
		strSQL.append(" ) as middle_school_class_count");
		strSQL.append("	FROM pcmc_dept t1 WHERE t1.state='1'");

		if(qry_deptcode.length()>=8){
			strSQL.append(" and length(t1.deptcode)>8  AND t1.deptcode like '"+qry_deptcode+"%' ");
		}
		else if(qry_deptcode.length()<8){
			strSQL.append("	and length(t1.deptcode)=8 and t1.deptcode like '"+qry_deptcode+"%' ");
		}

		strSQL.append("	order by t1.deptcode) user");
		resultMap.put("qryStr", strSQL.toString());
		return resultMap;
	}
}
