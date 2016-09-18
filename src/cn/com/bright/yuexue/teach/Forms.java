package cn.com.bright.yuexue.teach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

public class Forms {

	private XmlDocPkgUtil xmlDocUtil = null;
	private String upFolder = "/upload/doc/praxes/";
	private Log log4j = new Log(this.getClass().toString());
	
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		
		if ("getTeachClass".equals(action)){
			getTeachClass();
		}
		else if ("getFormsInfo".equals(action)){
			getFormsInfo();
		}
		return xmlDoc;
	}
	
	
	/**
	 * ��ѯ�Ծ��͵��İ༶
	 */
	public void getTeachClass(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String paperid = reqElement.getChildText("paperid");
		Element session = xmlDocUtil.getSession();
        String userid = session.getChildText("userid");
        
		ArrayList<Object> praxesParam = new ArrayList<Object>();
		StringBuffer praxesSQL = new StringBuffer();
		praxesSQL.append("select s.receiver_names,s.receiver_ids from learn_paper_send s where s.userid = ? ");
		praxesParam.add(userid);
		
		if (StringUtil.isNotEmpty(paperid)){
			praxesSQL.append(" and s.paper_id = ?");
			praxesParam.add(paperid);	    			
		}
				
		PlatformDao pdao = new PlatformDao(true);
		try {
	    	pdao.setSql(praxesSQL.toString());
	    	pdao.setBindValues(praxesParam);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	String[] flds = {"classid","classNm"};
			Element data = XmlDocPkgUtil.createMetaData(flds);
			
	    	List list = result.getChildren("Record");
            for (int i=0;i<list.size();i++) {
            	Element elm = (Element)list.get(i);
        		String classNms = elm.getChildText("receiver_names");
        		String classids = elm.getChildText("receiver_ids");
        		if(classNms.indexOf(",")!=-1 && classids.indexOf(",")!=-1){
        			//���ݿ��в�ѯ��classid��ϣ�C_classid(�༶),U_classid(ѧ��)
        			if(classids.startsWith("C_")){
        				String[] classid = classids.split(",");
            			String[] classname = classNms.split(",");
            			for(int j=0;j<classname.length;j++){
            				String id = classid[j].substring(2, classid[j].length());
            				//����DATA
            				data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{id,classname[j]}));
            			}
        			}
        		}else{
        			String id = classids.substring(2, classids.length());
        			//����DATA
    				data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{id,classNms}));
        		}
            }
            xmlDocUtil.getResponse().addContent(data);
	    	//xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");			
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[�Ծ���-��ȡ�Ծ��Ͱ༶]"+e.getMessage());
			xmlDocUtil.writeHintMsg("�Ծ���-��ȡ�Ծ��Ͱ༶ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	
	/**
	 * ��ȡ�Ծ�֪ʶ��÷ֱ���
	 */
	public void getFormsInfo(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String classid = reqElement.getChildText("classid");
		String paperid = reqElement.getChildText("paper_id");
		Element session = xmlDocUtil.getSession();
        String userid = session.getChildText("userid");
		
		ArrayList<Object> praxesParam = new ArrayList<Object>();
		StringBuffer praxesSQL = new StringBuffer();
		/*
		praxesSQL.append("select bk.k_point_name,aa.paper_praxes_id,bb.score,aa.avgscore from");
		praxesSQL.append(" (select r.paper_praxes_id,AVG(r.score) avgscore from learn_examination_result r where r.paper_id = ?");
		praxesParam.add(paperid);
		
		if (StringUtil.isNotEmpty(classid) && !"0".equals(classid)){
			praxesSQL.append(" and r.classid = ?");
			praxesParam.add(classid);	    			
		}
		
		praxesSQL.append(" GROUP BY r.paper_praxes_id) aa");
		praxesSQL.append(" LEFT JOIN (select p.* from learn_paper_praxes p where p.paper_id = ? and p.userid = ?) bb ON aa.paper_praxes_id = bb.paper_praxes_id");
		praxesSQL.append(" LEFT JOIN base_knowledge_point bk ON bb.k_point_id = bk.k_point_id");
		praxesParam.add(paperid);
		praxesParam.add(userid);
		*/
		praxesSQL.append("select bk.k_point_name,a.* from (");
		praxesSQL.append("select sss.k_point_id,SUM(sss.score) score,SUM(sss.getscore) getscore from ");
		praxesSQL.append("(select ss.k_point_id,ss.paper_praxes_id,AVG(ss.score) score,AVG(s.score) getscore from ");
		//praxesSQL.append("(select p.* from learn_paper_praxes p where p.paper_id = ? and p.userid = ?) ss ");
		praxesSQL.append("(select p.* from learn_paper_praxes p where p.paper_id = ?) ss ");
		praxesParam.add(paperid);
		//praxesParam.add(userid);
		praxesSQL.append(" LEFT JOIN ");
		praxesSQL.append("(select r.* from learn_examination_result r where r.paper_id = ?");
		praxesParam.add(paperid);
		if (StringUtil.isNotEmpty(classid) && !"0".equals(classid)){
			praxesSQL.append(" and r.classid = ?");
			praxesParam.add(classid);	    			
		}
		praxesSQL.append(") s ON s.paper_praxes_id = ss.paper_praxes_id GROUP BY ss.paper_praxes_id,ss.k_point_id");
		praxesSQL.append(") sss GROUP BY sss.k_point_id");
		praxesSQL.append(") a LEFT JOIN base_knowledge_point bk ON a.k_point_id = bk.k_point_id");
		
		PlatformDao pdao = new PlatformDao(true);
		try {
	    	pdao.setSql(praxesSQL.toString());
	    	pdao.setBindValues(praxesParam);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	//֪ʶ�㡢������ƽ���÷֡��÷���
	    	String[] flds = {"k_point_name","score","avgscore","percent"};
			Element data = XmlDocPkgUtil.createMetaData(flds);
			//�������ݣ��ó��ٷ���
	    	List list = result.getChildren("Record");
            for (int i=0;i<list.size();i++) {
            	Element elm = (Element)list.get(i);
            	String name = elm.getChildText("k_point_name");
        		String score = elm.getChildText("score");
        		String avgscore = elm.getChildText("getscore");
        		String pct = "-";
        		//û�л�ȡ֪ʶ����
        		if("".equals(name)){
        			continue;
        		}
        		//֪ʶ��û�з���
        		if("".equals(score)){
        			score = "-";
        			avgscore = "-";
        		}else{
        			if(!"".equals(avgscore) && !"0".equals(avgscore)){
            			java.text.DecimalFormat df =new java.text.DecimalFormat("#.00");
            			//ƽ���ֱ���2λС��
            			double avg = Double.parseDouble(avgscore);
            			avgscore = df.format(avg);
            			//��װ�ٷֱ�
                		double percent = (avg*100)/Double.parseDouble(score);
                		pct = df.format(percent)+"%";
            		}else{
            			avgscore = "0.00";
            		}
        		}
				//����DATA
				data.addContent(XmlDocPkgUtil.createRecord(flds, new String[]{name,score,avgscore,pct}));
            }
            xmlDocUtil.getResponse().addContent(data);
	    	xmlDocUtil.setResult("0");		
		}
		catch (Exception e) {
			e.printStackTrace();			
			log4j.logError("[�Ծ���-��ȡ�Ծ�֪ʶ��÷ֱ���]"+e.getMessage());
			xmlDocUtil.writeHintMsg("�Ծ���-��ȡ�Ծ�֪ʶ��÷ֱ���ʧ��!");
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE); 
		} finally {
			pdao.releaseConnection();
		}		
	}
	
}
