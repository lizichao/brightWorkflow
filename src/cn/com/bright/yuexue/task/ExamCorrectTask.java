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
 * <p>Title:�Ծ���������</p>
 * <p>Description: �Ծ���������</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 *   ֻ�����ύ�˵Ŀ͹���,�������ɽ�ʦ�˹�����
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/08/16       1.0          build this moudle </p>
 * <p> zhangxq    2014/12/30       1.1          �����������ж�,���������ķֱ����� </p>    
 */
public class ExamCorrectTask extends TimerTask{
	private Log log4j = new Log(this.getClass().toString());
	
	public void run() {	
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" ExamCorrectTask run ----------");
		List paperList = getSumitExamPaper();
	    for (int i=0;i<paperList.size();i++){	    	
	    	String paperid = ((Element)paperList.get(i)).getChildText("paper_id");	    	
	    	String choose_type = ((Element)paperList.get(i)).getChildText("choose_type");
	    	if("30".equals(choose_type)){
	    		List randomList =getRandomPaper(paperid);
	    		for (int j = 0; j < randomList.size(); j++) {
	    			String random_paperid = ((Element)randomList.get(j)).getChildText("random_paper_id");
	    			 correctPaper(paperid,random_paperid,"30");
				}
	    	}else{
	    	  correctPaper(paperid,"","");
	    	}
	    }
	    //System.out.println("----------  "+DatetimeUtil.getNow("")+" ExamCorrectTask complete ----------");
	}
	/**
	 * ����һ���Ծ�
	 * @param paperid
	 */
	public void correctPaper(String paperid,String random_paperid,String choose_type){
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("select t1.paper_praxes_id,praxes_type,score,right_result");
		strSQL.append(" from learn_paper_praxes t1");
		strSQL.append(" where valid='Y' and t1.paper_id=?");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		if("30".equals(choose_type)){
			paramList.add(random_paperid);
		}else{
			paramList.add(paperid);
		}
		//�Ƚ�ѧ����ҵ����״̬�޸�Ϊ������,����ֻ���������е���ҵ
    	StringBuffer preUpdateSQL = new StringBuffer();
    	ArrayList<Object> preUpdateParam = new ArrayList<Object>();
    	
    	preUpdateSQL.append("update learn_my_examination set status='55'");
    	preUpdateSQL.append(" where valid='Y' and status='50'");
    	if("30".equals(choose_type)){
    		preUpdateSQL.append(" and random_paper_id=?");
    		preUpdateParam.add(random_paperid);
    	}else{
    		preUpdateSQL.append(" and paper_id=?");
    		preUpdateParam.add(paperid);
    	}	

		StringBuffer correctSQL = new StringBuffer();
		correctSQL.append("update learn_examination_result set modify_date=?,correct_result='1',");
		correctSQL.append(" is_right=(case when praxes_result=? then 1 else 0 end),");
		correctSQL.append(" score=(case when praxes_result=? then ? else 0 end)");
		correctSQL.append(" where is_right is null and score is null");
		correctSQL.append(" and paper_id=? and paper_praxes_id=?");	
		correctSQL.append(" and exists");
		correctSQL.append(" (select null from learn_my_examination t2");
		correctSQL.append(" where t2.valid='Y' and t2.status='55'");
		correctSQL.append(" and t2.my_examination_id=learn_examination_result.my_examination_id)");
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(preUpdateSQL.toString());
	    	pdao.setBindValues(preUpdateParam);	    	
	    	pdao.executeTransactionSql();
	    	
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);
	    	
	    	String taskResult = "70";
	    	List praxesList = result.getChildren("Record");
	    	for (int i=0;i<praxesList.size();i++){
	    		 Element praxesRec = (Element)praxesList.get(i);
	    		 String praxes_type = praxesRec.getChildText("praxes_type");
	    		 String paper_praxes_id = praxesRec.getChildText("paper_praxes_id");
	    		 String score = praxesRec.getChildText("score"); 
	    		 if (StringUtil.isEmpty(score)){
	    			 score = "0";
	    		 }
	    		 String right_result = praxesRec.getChildText("right_result");
	    		 if (StringUtil.isEmpty(right_result)){
	    			 right_result = "1+1=3";
	    		 }	    		 
	    		 
				 ArrayList<Object> correctParam = new ArrayList<Object>();				 
				 correctParam.add(SqlTypes.getConvertor("Timestamp").convert(DatetimeUtil.getNow(""), "yyyy-MM-dd HH:mm:ss"));
				 
	    		 if ("10".equals(praxes_type) || "40".equals(praxes_type)){//�ж���������
	    			 if (right_result.indexOf(",")>0 || right_result.indexOf("|")>0){	    				 
	        			 correctParam.add(paperid);
	        			 correctParam.add(paper_praxes_id);	 	    				 
	    				 String[] praxesRightResult = right_result.split(",");
	    				 for (int j=0;j<praxesRightResult.length;j++){
	    					 String _rightResult = praxesRightResult[j];
	    					 StringBuffer corrPraxesSQL = new StringBuffer();
		    				 //corrPraxesSQL.append("update learn_examination_result set correct_result=concat(ifnull(correct_result,''),if(substring_index(substring_index(praxes_result,',',"+(j+1)+"),',',-1)=?,'1','0'))");    				
		    				 corrPraxesSQL.append(" where is_right is null and score is null");
		    				 corrPraxesSQL.append(" and paper_id=? and paper_praxes_id=?");	    				 
		    				 corrPraxesSQL.append(" and exists");
		    				 corrPraxesSQL.append(" (select null from learn_my_examination t2");
		    				 corrPraxesSQL.append(" where t2.valid='Y' and t2.status='55'");
		    				 corrPraxesSQL.append(" and t2.my_examination_id=learn_examination_result.my_examination_id)");
	    					 
	    					 
	    					 if (_rightResult.indexOf("|")>0){
		    					 String[] _spRightResult = _rightResult.split("\\|");
		    					 for (int k=0;k<_spRightResult.length;k++){
		    						 StringBuffer upSQL = new StringBuffer();		    						 
		    						 upSQL.append("update learn_examination_result set modify_date=?,correct_result=");
		    						 upSQL.append("concat(ifnull(correct_result,''),if(substring_index(substring_index(praxes_result,',',"+(j+1)+"),',',-1)='"+_spRightResult[k]+"','1','0'))");
		    						 
		    						 pdao.setSql(upSQL.toString()+corrPraxesSQL.toString());
		    						 pdao.setBindValues(correctParam);
		    						 pdao.executeTransactionSql();
		    					 }	    						 
	    					 }
	    					 else{
	    						 StringBuffer upSQL = new StringBuffer();		    						 
	    						 upSQL.append("update learn_examination_result set modify_date=?,correct_result=");
	    						 upSQL.append("concat(ifnull(correct_result,''),if(substring_index(substring_index(praxes_result,',',"+(j+1)+"),',',-1)='"+_rightResult+"','1','0'))");
	    						 
	    						 pdao.setSql(upSQL.toString()+corrPraxesSQL.toString());
	    						 pdao.setBindValues(correctParam);
	    						 pdao.executeTransactionSql();	    						 
	    					 }
	    				 }
	    				 //���ô�����ȷ��񼰷���
	    				 StringBuffer corrPraxesResult = new StringBuffer();
	    				 corrPraxesResult.append("update learn_examination_result set modify_date=?,is_right=if(locate('1',correct_result)>0,1,0),");
	    				 corrPraxesResult.append(" score=round((length(correct_result)-length(replace(correct_result,'1','')))/length(correct_result)*"+score+")");
	    				 corrPraxesResult.append(" where is_right is null and score is null");
	    				 corrPraxesResult.append(" and paper_id=? and paper_praxes_id=?");	    				 
	    				 corrPraxesResult.append(" and correct_result is not null ");
	    				 corrPraxesResult.append(" and exists");
	    				 corrPraxesResult.append(" (select null from learn_my_examination t2");
	    				 corrPraxesResult.append(" where t2.valid='Y' and t2.status='55'");
	    				 corrPraxesResult.append(" and t2.my_examination_id=learn_examination_result.my_examination_id)");
	    				 
	    				 pdao.setSql(corrPraxesResult.toString());
	    				 pdao.setBindValues(correctParam);
						 pdao.executeTransactionSql();
						 //���ö���������ȷ����
	    				 StringBuffer corrResult = new StringBuffer();
	    				 corrResult.append("update learn_examination_result set modify_date=?,is_right=2");	    				 
	    				 corrResult.append(" where is_right =1 and correct_result like '%0%'");
	    				 corrResult.append(" and paper_id=? and paper_praxes_id=?");	    				 
	    				 corrResult.append(" and exists");
	    				 corrResult.append(" (select null from learn_my_examination t2");
	    				 corrResult.append(" where t2.valid='Y' and t2.status='55'");
	    				 corrResult.append(" and t2.my_examination_id=learn_examination_result.my_examination_id)");
	    				 
	    				 pdao.setSql(corrResult.toString());
	    				 pdao.setBindValues(correctParam);
						 pdao.executeTransactionSql();						 
	    			 }
	    			 else{
	    				 
		    			 correctParam.add(right_result);
		    			 correctParam.add(right_result);
		    			 correctParam.add(score);
		    			 correctParam.add(paperid);
		    			 correctParam.add(paper_praxes_id);
		    			 
		    		     pdao.setSql(correctSQL.toString());
		    		     pdao.setBindValues(correctParam);
		    		     pdao.executeTransactionSql();
	    			 }
	    		 }
	    		 else if ("20".equals(praxes_type) || "30".equals(praxes_type)){//��ѡ���ѡ��
	    			 String optionSQL = "select option_id from learn_paper_options where valid='Y' and is_right=1 and paper_praxes_id=? order by display_order";
	    			 ArrayList<Object> optionParam = new ArrayList<Object>();
	    			 optionParam.add(paper_praxes_id);
	    			 
	    		     pdao.setSql(optionSQL);
	    		     pdao.setBindValues(optionParam);
	    		     Element optionResult = pdao.executeQuerySql(0,-1);
	    		     
	    		     String rightOption = "";
	    		     List optioinList = optionResult.getChildren("Record");
	    		     for (int j=0;j<optioinList.size();j++){
	    		    	 rightOption = rightOption+","+((Element)optioinList.get(j)).getChildText("option_id");
	    		     }
	    		     if (StringUtil.isNotEmpty(rightOption)){
	    		        rightOption = rightOption.substring(1);
	    		     }
	    			 correctParam.add(rightOption);
	    			 correctParam.add(rightOption);
	    			 correctParam.add(score);
	    			 correctParam.add(paperid);
	    			 correctParam.add(paper_praxes_id);
	    			 
	    		     pdao.setSql(correctSQL.toString());
	    		     pdao.setBindValues(correctParam);
	    		     pdao.executeTransactionSql();	    		     
	    		 }
	    		 else if ("90".equals(praxes_type) && "0".equals(score)){
    				 StringBuffer upSQL = new StringBuffer();
    				 upSQL.append("update learn_examination_result set modify_date=?,score=0");    				
    				 upSQL.append(" where is_right is null and score is null");
    				 upSQL.append(" and paper_id=? and paper_praxes_id=?");		    			 
    				 upSQL.append(" and exists");
    				 upSQL.append(" (select null from learn_my_examination t2");
    				 upSQL.append(" where t2.valid='Y' and t2.status='55'");
    				 upSQL.append(" and t2.my_examination_id=learn_examination_result.my_examination_id)");
    				    				 
	    			 correctParam.add(paperid);
	    			 correctParam.add(paper_praxes_id);
	    			 
	    		     pdao.setSql(upSQL.toString());
	    		     pdao.setBindValues(correctParam);
	    		     pdao.executeTransactionSql();	    			 
	    		 }
	    		 else if ("90".equals(praxes_type) && !"0".equals(score)){
	    			 taskResult = "60"; 
	    		 }
	    	}
	    	//�������е���ҵ�޸�Ϊ��ɻ������
	    	StringBuffer updateSQL = new StringBuffer();
	    	ArrayList<Object> updateParam = new ArrayList<Object>();
	    	updateSQL.append(" update learn_my_examination set status='"+taskResult+"',");
	    	updateSQL.append(" paper_score=(select sum(score) from learn_examination_result st");
	    	updateSQL.append("              where st.score is not null and st.my_examination_id=learn_my_examination.my_examination_id)");
	    	updateSQL.append(" where status='55' ");
	    	
	    	if("30".equals(choose_type)){
	    		updateSQL.append(" and random_paper_id=?");
	    		updateParam.add(random_paperid);
	    	}else{
	    		updateSQL.append(" and paper_id=?");
	    		updateParam.add(paperid);
	    	}   	
            
		    pdao.setSql(updateSQL.toString());
		    pdao.setBindValues(updateParam);
		    pdao.executeTransactionSql();
		    
		    //��ҵ״̬�޸�
		    //pdao.commitTransaction();
	    	
	    }catch (Exception e) {	
	    	//pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[��ҵ�Զ�����-����IDΪ"+paperid+"���Ծ�]"+e.getMessage());			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ȡ���ύ��¼���Ծ�ID
	 * @return �Ծ�ID�б�
	 */
	public List getSumitExamPaper(){

		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" select t1.paper_id,t1.choose_type");
		strSQL.append(" from learn_examination_paper t1");
		strSQL.append(" where t1.valid='Y' and exists");
		strSQL.append(" (select null from learn_my_examination t2");
		strSQL.append(" where t2.valid='Y' and t2.status='50'");
		strSQL.append(" and   t2.paper_id=t1.paper_id)");
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());	    	
	    	Element result = pdao.executeQuerySql(20,1);	    	
	    	return result.getChildren("Record");	    	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[��ҵ�Զ�����-ȡ���ύ��ҵ���Ծ�ID]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();
		}		
	}
	
	public List getRandomPaper(String paperid){
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" select t1.random_paper_id");
		strSQL.append(" from learn_my_examination t1");
		strSQL.append(" where t1.valid='Y' and t1.status = '50' ");
		strSQL.append(" and t1.paper_id= ? ");
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(paperid); 
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());	 
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);	    	
	    	return result.getChildren("Record");	    	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[��ҵ�Զ�����-ȡ����ʱ������]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();
		}		
	}
}
