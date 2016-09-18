package cn.com.bright.edu.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.util.BaseReport;

public class ResourceGrowthStatReport extends BaseReport{

	public ResourceGrowthStatReport(){}
	public Map<String , Object> fillData(HashMap<String , Object> paraHashMap){
		Map<String , Object> resultMap =  new HashMap<String , Object>();
		StringBuffer strSQL = new StringBuffer();
		
		String qry_deptcode = (String)paraHashMap.get("qry_deptcode");
		String qry_years = (String)paraHashMap.get("qry_years");
		
		String qry_stat_type = (String)paraHashMap.get("qry_stat_type");
		if(qry_deptcode.length()!=10){
    		//全市、全区资源增长统计SQL
	    	strSQL.append("	SELECT t1.deptname,t1.deptcode,'' as userid,");
	    	strSQL.append("	ifnull(sum(t2.resource_total_count),0) as resource_total_count,");
	    	strSQL.append("	ifnull(sum(t2.paper_total_count),0) as paper_total_count,");
	    	strSQL.append("	ifnull(sum(t2.video_total_count),0) as video_total_count,");
	    	strSQL.append("	ifnull(sum(t2.flash_total_count),0) as flash_total_count,");
	    	strSQL.append("	ifnull(sum(t2.ppt_total_count),0) as ppt_total_count,");
	    	strSQL.append("	ifnull(sum(t2.word_total_count),0) as word_total_count,");
	    	strSQL.append("	ifnull(sum(t2.image_total_count),0) as image_total_count");
	    	strSQL.append(" FROM pcmc_dept t1 left join (");
	    	strSQL.append(" select tt4.deptcode,tt2.username,");
	    	strSQL.append(" COUNT(case when (tt1.resource_type like '40%' or tt1.resource_type like '20%') ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as resource_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type like '40%' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as paper_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2010' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as video_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2020' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as flash_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2030' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as ppt_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2040' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as word_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2050' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as image_total_count");
	    	strSQL.append(" from learn_examination_paper tt1,pcmc_user tt2,pcmc_user_dept tt3,pcmc_dept tt4");
	    	strSQL.append(" where tt1.user_id = tt2.userid and tt2.userid = tt3.userid and tt3.deptid = tt4.deptid and tt1.valid = 'Y' and tt4.state = '1'");
	    	strSQL.append(" group by tt4.deptcode,tt2.username ) t2 on t2.deptcode like CONCAT(t1.deptcode,'%')");
	    	if(qry_deptcode.length()>=8){
	    		strSQL.append(" WHERE length(t1.deptcode)>8  AND t1.deptcode like '"+qry_deptcode+"%' ");
	    	}
	    	else if(qry_deptcode.length()<8){
	    		strSQL.append("	WHERE length(t1.deptcode)=8 and t1.deptcode like '"+qry_deptcode+"%' ");
	    	}  
	    	strSQL.append(" AND t1.state='1' GROUP BY t1.deptname, t1.deptcode ORDER BY t1.deptcode");
    	}else {
    		//具体学校所有老师上传资源SQL
    		strSQL.append(" select * from ( SELECT t1.username as deptname,'' as deptcode,t1.userid,");
        	strSQL.append(" ifnull(sum(t2.resource_total_count),0) as resource_total_count,");
        	strSQL.append(" ifnull(sum(t2.paper_total_count),0) as paper_total_count,");
        	strSQL.append(" ifnull(sum(t2.video_total_count),0) as video_total_count,");
        	strSQL.append(" ifnull(sum(t2.flash_total_count),0) as flash_total_count,");
        	strSQL.append(" ifnull(sum(t2.ppt_total_count),0) as ppt_total_count,");
        	strSQL.append(" ifnull(sum(t2.word_total_count),0) as word_total_count,");
        	strSQL.append(" ifnull(sum(t2.image_total_count),0) as image_total_count");
        	strSQL.append(" FROM pcmc_user t1 left join ( select tt2.username,tt2.userid,");
        	strSQL.append(" COUNT(case when (tt1.resource_type like '40%' or tt1.resource_type like '20%') ");
        	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as resource_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type like '40%' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as paper_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2010' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as video_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2020' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as flash_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2030' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as ppt_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2040' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as word_total_count,");
	    	strSQL.append(" COUNT(case when tt1.resource_type = '2050' ");
	    	if("1".equals(qry_stat_type)){
		    	if(StringUtil.isNotEmpty(qry_years)){
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') = '"+qry_years+"' ");
		    	}else {
		    		strSQL.append(" and DATE_FORMAT(tt1.create_date, '%Y') >=2015");
				}
		    }
	    	strSQL.append(" then 1 else null end) as image_total_count");
        	strSQL.append(" from learn_examination_paper tt1,pcmc_user tt2,pcmc_user_dept tt3,pcmc_dept tt4");
        	strSQL.append(" where tt1.user_id = tt2.userid and tt2.userid = tt3.userid and tt3.deptid = tt4.deptid ");
        	strSQL.append(" and tt4.deptcode like '"+qry_deptcode+"%' and tt1.valid = 'Y'");
        	strSQL.append(" and tt4.state = '1' group by tt2.username ) t2 on t2.userid = t1.userid GROUP BY t1.username");
        	strSQL.append(" ) as u order by u.resource_total_count desc,u.deptname");
		}
		resultMap.put("qryStr", strSQL.toString());
		return resultMap;
	}
}
