package cn.com.bright.yuexue.base;

import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * 
 * <p>Title: ��ʦ�ν̿γ̹���</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technology Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">LHBO</a>
 * @version 1.0x
 */
public class TeacherSubjectManage
{
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(CourseManage.class.getName());
    
    public Document doPost(Document xmlDoc)
	{
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		if("addTeacherSubject".equals(action))
		{
			addTeacherSubject();
		}
		else if("updateTeacherSubject".equals(action))
		{
			updateTeacherSubject();
		}
		else if("setSubjectAssistant".equals(action))
		{
			setSubjectAssistant();
		}
		return xmlDoc;
	}
    
    private void addTeacherSubject()
	{
		Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	if(!checkedUnique()){
        		xmlDocUtil.writeErrorMsg("10110", "��ʦ�ν̿γ��ظ�");
				return;
        	}
        	
        	Element rec = ConfigDocument.createRecordElement("yuexue","base_teacher_subject");
        	XmlDocPkgUtil.copyValues(reqData,rec,0,true);
        	
        	Object pk = pDao.insertOneRecordSeqPk(rec);        	
        	String[] flds = {"teasubjid"};
            Element rData = XmlDocPkgUtil.createMetaData(flds);
            Element pkRec = XmlDocPkgUtil.createRecord(flds,
            new String[]{""+pk});
            rData.addContent(pkRec);
        	
            xmlDocUtil.writeHintMsg("10100", "������ʦ�ν̿γ̳ɹ���");
        	xmlDocUtil.setResult("0");
        	xmlDocUtil.getResponse().addContent(rData);
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10120", "������ʦ�ν̿γ��쳣");
        }
        finally
        {
        	pDao.releaseConnection();
        }
	}
    
    private void updateTeacherSubject()
	{
		Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	if(!checkedUnique()){
        		xmlDocUtil.writeErrorMsg("10220", "��ʦ�ν̿γ��ظ�");
				return;
        	}
        	
        	Element rec = ConfigDocument.createRecordElement("yuexue","base_teacher_subject");
        	XmlDocPkgUtil.copyValues(reqData,rec,0,true);
        	//XmlDocPkgUtil.setChildText(rec, "assistant_id", reqData.getChildText("assistant_id"));
        	pDao.updateOneRecord(rec);
            
        	xmlDocUtil.writeHintMsg("10200", "�޸Ľ�ʦ�ν̿γ̳ɹ���");
        	xmlDocUtil.setResult("0");
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10220", "�޸Ľ�ʦ�ν̿γ��쳣");
        }
        finally
        {
        	pDao.releaseConnection();
        }
	}
    /**
     * ���ÿδ���
     *
     */
    private void setSubjectAssistant(){
    	Element reqData = xmlDocUtil.getRequestData();
    	PlatformDao pDao = new PlatformDao();
    	try{
    		StringBuffer upSQL = new StringBuffer();
    		upSQL.append(" update base_teacher_subject ");
    		upSQL.append(" set assistant_id=?");
    		upSQL.append(" where state='1' and classid=?");
    		upSQL.append(" and subjectid=?");
    		
    		ArrayList<String> bvals = new ArrayList<String>();
    		bvals.add(reqData.getChildText("assistant_id"));
    		bvals.add(reqData.getChildText("classid"));
    		bvals.add(reqData.getChildText("subjectid"));
    		
    		pDao.setSql(upSQL.toString());
    		pDao.setBindValues(bvals);
    		
    		pDao.executeTransactionSql();
    		String hintMsg = "���ÿδ���ɹ�!";
    		if (StringUtil.isEmpty(reqData.getChildText("assistant_id"))){
    			hintMsg ="ȡ���δ���ɹ�";
    		}
        	xmlDocUtil.writeHintMsg("10200", hintMsg);
        	xmlDocUtil.setResult("0");   		
    	}
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeErrorMsg("10221", "���ÿγ̿δ����쳣");
        }
        finally
        {
        	pDao.releaseConnection();
        }   	
    	
    }
    
    private boolean checkedUnique() throws Exception {
    	Element reqData = xmlDocUtil.getRequestData();
    	String teasubjid = reqData.getChildTextTrim("teasubjid");
    	String classid = reqData.getChildTextTrim("classid");
    	String userid = reqData.getChildTextTrim("userid");
    	String subjectid = reqData.getChildTextTrim("subjectid");
    	
    	ArrayList<String> bvals = new ArrayList<String>();
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select teasubjid from base_teacher_subject where 1=1 ")
		.append(" and classid=? and subjectid = ? and userid=? and state > '0'");
		bvals.add(classid);
		bvals.add(subjectid);
		bvals.add(userid);
		if(StringUtil.isNotEmpty(teasubjid))
		{
			sqlBuf.append(" and teasubjid <> ?");
			bvals.add(teasubjid);
		}
		Element rec = DaoUtil.getOneRecord(sqlBuf.toString(), bvals);
		return null == rec;
    }
}
