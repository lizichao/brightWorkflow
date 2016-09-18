package cn.com.bright.yuexue.task;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.db.SqlTypes;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.TimerTask;

/**
 * <p>Title:�����Ѷ�,���ֶ�ͳ������</p>
 * <p>Description: �����Ѷ�,���ֶ�ͳ������</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2015/04/27       1.0          build this moudle </p>    
 */
public class DifficultyStatTask extends TimerTask{
	private Log log4j = new Log(this.getClass().toString());
	private String preStatTime;
	private String currTime;
	
	public void run() {	
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" DifficultyStatTask run ----------");
		this.preStatTime = getPreStatTime();
		this.currTime = DatetimeUtil.getNow("");
		
		String prePraxesID ="";
		List praxesList = getPraxesList();
		for (int i=0;i<praxesList.size();i++){
			Element praxesRec = (Element)praxesList.get(i);			
			updatePaperPraxesDifficulty(praxesRec);
			String praxes_id = praxesRec.getChildText("praxes_id");
			
            if (!prePraxesID.equals(praxes_id)){
				if (i>0){
					if (StringUtil.isNotEmpty(prePraxesID)){
					   int resultCount = Integer.parseInt(praxesRec.getChildText("result_count"));
					   if (resultCount<3000){//�����������Ѷȡ����ֶ���������С��3K,�����¼���
					       updatePraxesDifficulty(praxesRec);
					   }
					}
				}
				prePraxesID = praxes_id;
			}
		}
		ArrayList<Object> paramList = new ArrayList<Object>();
		StringBuffer upSQL = new StringBuffer();
		upSQL.append("update learn_examination_result set");
		upSQL.append(" difficulty_stat_flag=1");
		upSQL.append(" where difficulty_stat_flag=0 and modify_date<=?");
		paramList.add(SqlTypes.getConvertor("Timestamp").convert(this.currTime, "yyyy-MM-dd HH:mm:ss"));
		
		PlatformDao pdao = new PlatformDao();		
	    try{
	    	pdao.setSql(upSQL.toString());
	    	pdao.setBindValues(paramList);
	    	pdao.executeTransactionSql();	    	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[�����Ѷ�/���ֶ�ͳ��-�޸�ͳ�Ʊ�ʶ]"+e.getMessage());	
		} finally {
			pdao.releaseConnection();
		}		
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" DifficultyStatTask complete ----------");
	}
	/**
	 * �޸���������Ѷ�/���ֶ�
	 * @param praxesRec
	 */
	private void updatePraxesDifficulty(Element praxesRec){
		String praxes_id = praxesRec.getChildText("praxes_id");
		float dbScore = Float.valueOf(praxesRec.getChildText("db_score"));
		Element statRec = this.getPraxesStatInfo(praxes_id);
		int stuCount = Integer.parseInt(statRec.getChildText("stu_count"));
		float avgScore = Float.valueOf(statRec.getChildText("avg_score"));
		//�Ѷ�ϵ��=����ƽ���÷�/�������
		float difficultyLevel = (float)Math.round(avgScore*100/dbScore)/100;
		//�߷���ƽ����
		float highGroupAvgScore = Float.valueOf(this.getPraxesAvgScore(praxes_id, "high", stuCount));
		//�ͷ���ƽ�ַ�
		float lowGroupAvgScore = Float.valueOf(this.getPraxesAvgScore(praxes_id, "low", stuCount));
		//�������ֶ�=(�߷���ƽ����-�ͷ���ƽ����)/�������
		float discrimination = (float)Math.round((highGroupAvgScore-lowGroupAvgScore)*100/dbScore)/100;
		
		//System.out.println("praxes_id="+praxes_id+";difficultyLevel="+String.valueOf(difficultyLevel)+";discrimination="+String.valueOf(discrimination));
		
		StringBuffer upSQL = new StringBuffer();
		upSQL.append("update learn_praxes set");
		upSQL.append(" difficulty_level=?,discrimination=?,modify_by='DifficultyStatTask',modify_date=now()");
		upSQL.append(" where praxes_id=?");
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(String.valueOf(difficultyLevel));
		paramList.add(String.valueOf(discrimination));
		paramList.add(praxes_id);
		
		PlatformDao pdao = new PlatformDao();		
	    try{
	    	pdao.setSql(upSQL.toString());
	    	pdao.setBindValues(paramList);
	    	pdao.executeTransactionSql();	    	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[�����Ѷ�/���ֶ�ͳ��-�޸���������Ѷȼ����ֶ�]"+e.getMessage());	
		} finally {
			pdao.releaseConnection();
		}		
		
	}
	
	/**
	 * �޸��Ծ���Ŀ�Ѷ�/���ֶ�
	 * @param praxesRec
	 */
	private void updatePaperPraxesDifficulty(Element praxesRec){
		String paper_praxes_id = praxesRec.getChildText("paper_praxes_id");
		float praxesScore = Float.valueOf(praxesRec.getChildText("score"));
		Element statRec = this.getPaperPraxesStatInfo(paper_praxes_id);
		
		int stuCount = Integer.parseInt(statRec.getChildText("stu_count"));
		
		float avgScore = Float.valueOf(statRec.getChildText("avg_score"));
		//�Ѷ�ϵ��=����ƽ���÷�/�������
		float difficultyLevel = (float)Math.round(avgScore*100/praxesScore)/100;
		//�߷���ƽ����
		float highGroupAvgScore = Float.valueOf(this.getPaperPraxesAvgScore(paper_praxes_id, "high", stuCount));
		//�ͷ���ƽ�ַ�
		float lowGroupAvgScore = Float.valueOf(this.getPaperPraxesAvgScore(paper_praxes_id, "low", stuCount));
		//�������ֶ�=(�߷���ƽ����-�ͷ���ƽ����)/�������
		float discrimination = (float)Math.round((highGroupAvgScore-lowGroupAvgScore)*100/praxesScore)/100;
		
		//System.out.println("paper_praxes_id="+paper_praxes_id+";difficultyLevel="+String.valueOf(difficultyLevel)+";discrimination="+String.valueOf(discrimination));
		
		StringBuffer upSQL = new StringBuffer();
		upSQL.append("update learn_paper_praxes set");
		upSQL.append(" difficulty_level=?,discrimination=?,modify_by='DifficultyStatTask',modify_date=now()");
		upSQL.append(" where paper_praxes_id=?");
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(String.valueOf(difficultyLevel));
		paramList.add(String.valueOf(discrimination));
		paramList.add(paper_praxes_id);
		
		PlatformDao pdao = new PlatformDao();		
	    try{
	    	pdao.setSql(upSQL.toString());
	    	pdao.setBindValues(paramList);
	    	pdao.executeTransactionSql();	    	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[�����Ѷ�/���ֶ�ͳ��-�޸��Ծ������Ѷȼ����ֶ�]"+e.getMessage());	
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ȡ�߷����ͷ���ƽ����
	 * @param paperPraxesId
	 * @param groupType high or low
	 * @return
	 */
	private String getPaperPraxesAvgScore(String paperPraxesId,String groupType,int stuCount){
		Float groupStuCount = (float)Math.round(stuCount*0.27);
		int recCount = groupStuCount.intValue();
		if (recCount<1){
			recCount = 1;
		}
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select avg(score) as avg_score from (");
		strSQL.append(" select score from learn_examination_result");
		strSQL.append(" where paper_praxes_id=?");
		strSQL.append(" order by score ");
		if ("high".equals(groupType)){
		   strSQL.append(" desc");
		}
		strSQL.append(" limit "+String.valueOf(recCount)+" ) stt");
		
		paramList.add(paperPraxesId);
		
		PlatformDao pdao = new PlatformDao();		
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);	    	
	    	String avg_score = result.getChild("Record").getChildText("avg_score");
	    	if (StringUtil.isEmpty(avg_score)){
	    		return "0";
	    	}
	    	else{
	    		return avg_score;
	    	}
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[�����Ѷ�/���ֶ�ͳ��-ȡ�Ծ���Ŀƽ����]"+e.getMessage());	
			return "0";
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ȡ����������ƽ����
	 * @param paperPraxesId
	 * @param groupType
	 * @param stuCount
	 * @return
	 */
	private String getPraxesAvgScore(String praxesId,String groupType,int stuCount){
		Float groupStuCount = (float)Math.round(stuCount*0.27);
		int recCount = groupStuCount.intValue();
		if (recCount<1){
			recCount = 1;
		}
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select avg(convert_score) as avg_score from (");
		strSQL.append(" select t1.score*t3.score/t2.score as convert_score");
		strSQL.append(" from learn_examination_result t1,learn_paper_praxes t2,learn_praxes t3");
		strSQL.append(" where t1.score is not null and t1.paper_praxes_id=t2.paper_praxes_id ");
		strSQL.append(" and t2.praxes_id=t3.praxes_id and t3.praxes_id=?");
		strSQL.append(" order by convert_score ");
		if ("high".equals(groupType)){
		   strSQL.append(" desc");
		}
		strSQL.append(" limit "+String.valueOf(recCount)+" ) stt");
		
		paramList.add(praxesId);
		
		PlatformDao pdao = new PlatformDao();		
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);	    	
	    	String avg_score = result.getChild("Record").getChildText("avg_score");
	    	if (StringUtil.isEmpty(avg_score)){
	    		return "0";
	    	}
	    	else{
	    		return avg_score;
	    	}
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[�����Ѷ�/���ֶ�ͳ��-ȡ�����Ŀƽ����]"+e.getMessage());	
			return "0";
		} finally {
			pdao.releaseConnection();
		}		
	}	
	/**
	 * ȡ�Ծ���Ŀƽ����
	 * @param paperPraxesId
	 * @return
	 */
	private Element getPaperPraxesStatInfo(String paperPraxesId){
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select avg(score) as avg_score,count(result_id) as stu_count from learn_examination_result ");
		strSQL.append(" where paper_praxes_id=?");
		paramList.add(paperPraxesId);
		
		PlatformDao pdao = new PlatformDao();		
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(1,1);	    	
	    	return result.getChild("Record");	    	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[�����Ѷ�/���ֶ�ͳ��-ȡ�Ծ���Ŀƽ����]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ȡ�������ͳ����Ϣ
	 * @param praxesId
	 * @return
	 */
	private Element getPraxesStatInfo(String praxesId){
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select avg(t1.score*t3.score/t2.score) as avg_score,count(t1.result_id) as stu_count ");
		strSQL.append(" from learn_examination_result t1,learn_paper_praxes t2,learn_praxes t3");
		strSQL.append(" where t1.score is not null  and t1.paper_praxes_id=t2.paper_praxes_id ");
		strSQL.append(" and t2.praxes_id=t3.praxes_id and t3.praxes_id=?");
		paramList.add(praxesId);
		
		PlatformDao pdao = new PlatformDao();		
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(1,1);	    	
	    	return result.getChild("Record");	    	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[�����Ѷ�/���ֶ�ͳ��-ȡ�����Ŀƽ����]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();
		}		
	}	
	/**
	 * ȡ������Ϣ
	 * @param preTime
	 * @return
	 */
	private List getPraxesList(){
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("select t1.paper_praxes_id,t1.praxes_id,t1.difficulty_level,t1.discrimination,t1.score,t3.score as db_score");
		strSQL.append(",(select count(t10.result_id) from learn_examination_result t10,learn_paper_praxes t11 ");
		strSQL.append(" where t10.paper_praxes_id=t11.paper_praxes_id and t11.praxes_id=t3.praxes_id and t10.score is not null) as result_count");
		strSQL.append(" from  learn_paper_praxes t1,learn_praxes t3");
		strSQL.append(" where t1.praxes_id=t3.praxes_id ");
		strSQL.append(" and exists (");
		strSQL.append(" select null from learn_examination_result t2 where t2.paper_praxes_id=t1.paper_praxes_id");
		strSQL.append(" and t2.difficulty_stat_flag=0 and t2.score is not null");
		if (StringUtil.isNotEmpty(this.preStatTime)){
			strSQL.append(" and t2.modify_date > ?");
			paramList.add(SqlTypes.getConvertor("Timestamp").convert(this.preStatTime, "yyyy-MM-dd HH:mm:ss"));
		}
		strSQL.append(" and t2.modify_date <= ? )");
		paramList.add(SqlTypes.getConvertor("Timestamp").convert(this.currTime, "yyyy-MM-dd HH:mm:ss"));		
		strSQL.append(" order by t1.praxes_id");
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());	
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);	    	
	    	return result.getChildren("Record");  	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[�����Ѷ�/���ֶ�ͳ��-ȡ�ϴ�ͳ��ʱ��]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ȡ�ϴ�ͳ�Ƶ�ʱ��
	 * @return
	 */
	private String getPreStatTime(){
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select max(modify_date) as modify_date from learn_examination_result ");
		strSQL.append(" where difficulty_stat_flag=1");
		
		PlatformDao pdao = new PlatformDao();		
	    try{
	    	pdao.setSql(strSQL.toString());	    	
	    	Element result = pdao.executeQuerySql(1,1);	    	
	    	return result.getChild("Record").getChildText("modify_date");	    	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[�����Ѷ�/���ֶ�ͳ��-ȡ�ϴ�ͳ��ʱ��]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();
		}		
	}
}
