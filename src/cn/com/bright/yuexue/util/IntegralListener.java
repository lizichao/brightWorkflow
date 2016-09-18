package cn.com.bright.yuexue.util;

import java.util.ArrayList;
import java.util.List;
import org.jdom.*;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.event.Event;
import cn.brightcom.jraf.event.Listener;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
/**
 * <p>Title:沃课堂积分规则</p>
 * <p>Description: 教师上传课件,试卷。学生提交试卷</p>
 * @author LZY
 *
 */
public class IntegralListener extends Listener{


	public void fire(Event evt, Object param) throws Exception {
		if(null == evt || null == param ) return;
		try {
			Document xmlDoc = (Document)param;
			XmlDocPkgUtil xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		    String userid=xmlDocUtil.getSession().getChildTextTrim("userid");
			String action = xmlDocUtil.getAction();
			PlatformDao pdao = new PlatformDao();
			pdao.beginTransaction();
			 if("0".equals(xmlDocUtil.getResult()) || "2".equals(xmlDocUtil.getResult())){
				List list = xmlDocUtil.getResponse().getChild("Data").getChild("Record").getChildren();
			  
					 Element record = (Element)list.get(0);
					 String paperid=record.getText();
					
					 //增加积分信息sell_integral
					 Element sellIntegralLogRec = ConfigDocument.createRecordElement("yuexue", "sell_integral");
					 XmlDocPkgUtil.copyValues(record, sellIntegralLogRec, 0, true);
					 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "userid", userid);
					 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "source_id", paperid);
					 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "create_by", "管理员");
					 XmlDocPkgUtil.setChildText(sellIntegralLogRec, "valid", "Y");
					 
					 StringBuffer strSQL = new StringBuffer();
				     ArrayList<Object> paramList = new ArrayList<Object>();
					 if("uploadVideoList".equals(action)){
					    strSQL.append("SELECT * FROM learn_attachment tt WHERE file_md5 = (SELECT file_md5 FROM learn_paper_attachment t1, learn_attachment t2");
					    strSQL.append("  WHERE t1.attachment_id = t2.attachment_id  AND t1.paper_id = ?");
					    paramList.add(paperid);
					    strSQL.append(" )  AND userid = ?");
					    paramList.add(userid);
					    strSQL.append(" order by tt.create_date desc");
					    
					    pdao.setSql(strSQL.toString());
					    pdao.setBindValues(paramList);
					    Element result = pdao.executeQuerySql(0, -1);
					    List queryList = result.getChildren("Record");
					
					    if(queryList.size()>1){
						   XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_num", "0");
						   XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", "重复上传课件");
						   XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_type", "3");
				    	}else{
						   XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_num", "10");
						   XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", "上传课件");
						   XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_type", "3");
					    }
					  }else if("savePaper".equals(action)){
						 
						  if(StringUtil.isNotEmpty(paperid)){
							  strSQL.append("select (extcut/cut) as num from (");
							  strSQL.append(" SELECT count(*) AS cut,(SELECT count(*) FROM learn_paper_praxes t WHERE t.paper_id = ? AND EXISTS (SELECT NULL FROM learn_paper_praxes tt1 ");
							  strSQL.append(" WHERE tt1.praxes_id = t.praxes_id AND tt1.paper_id <> t.paper_id AND tt1.userid= ? ))  AS extcut FROM learn_paper_praxes t WHERE t.paper_id =  ?");
							  paramList.add(paperid);
							  paramList.add(userid);
							  paramList.add(paperid);
							  strSQL.append(" ) tt");
							  pdao.setSql(strSQL.toString());
							  pdao.setBindValues(paramList);
							  Element result = pdao.executeQuerySql(0, -1);
							  List queryList = result.getChildren("Record");

							  Double cut= Double.parseDouble(((Element)queryList.get(0)).getChildText("num")); 
							  if(cut>0.5){
								  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_num", "0");
								  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", "重复上传试卷");
								  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_type", "4");  
							  }else{
								  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_num", "5");
								  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", "上传试卷");
								  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_type", "4"); 						  
							  }
						  }else{
							  return;
						  }
					  }else if("submitExamResult".equals(action)){
						  strSQL.append("select * from sell_integral si where si.integral_type='7' and si.source_id=? and si.userid=? ");
						  paramList.add(paperid);
						  paramList.add(userid);
						  pdao.setSql(strSQL.toString());
						  pdao.setBindValues(paramList);
						  Element result = pdao.executeQuerySql(0, -1);
						  List queryList = result.getChildren("Record");
						  if(queryList.size()>0){
							  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_num", "0");
							  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", "重复完成试卷");
							  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_type", "7");
						  }else{
							  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_num", "1");
							  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "remark", "完成试卷");
							  XmlDocPkgUtil.setChildText(sellIntegralLogRec, "integral_type", "7");
						  }
						 
						  
					  }
					 pdao.insertOneRecordSeqPk(sellIntegralLogRec);
				 }
			
			 pdao.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
