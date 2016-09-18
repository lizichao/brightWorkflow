package cn.com.bright.yuexue.base;

import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DaoUtil;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.util.PcmcUtil;

/**
 * 
 * <p>Title: �༶����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technology Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class ClassManage
{
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(ClassManage.class.getName());
    
    public Document doPost(Document xmlDoc)
	{
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	
    	if("init".equals(action))
    	{
    		initClass();
    	}
		return xmlDoc;
	}
    
    /**
     * �������ɰ༶
     */
    private void initClass()
    {
    	Element reqData = xmlDocUtil.getRequestData();
		Element session = xmlDocUtil.getSession();
        String curUsercode = session.getChildText("usercode");
        String curDeptid = session.getChildText("deptid");
        
        PlatformDao pDao = null;
        try
        {
        	pDao = new PlatformDao();
        	pDao.beginTransaction();
        	
        	String gradecode = reqData.getChildText("gradecode");
        	String deptid;
        	if(PcmcUtil.isSysManager(curUsercode))
        	{
        		deptid = reqData.getChildText("deptid");
        	}
        	else
        	{
        		deptid = curDeptid;
        	}
        	//��ѯ�꼶����
        	StringBuffer sqlBuf = new StringBuffer("select * from base_grade where deptid=? and gradecode=?");
        	ArrayList bvals = new ArrayList();
        	bvals.add(deptid);
        	bvals.add(gradecode);
        	
        	Element gradeRec = DaoUtil.getOneRecord(sqlBuf.toString(), bvals);
        	if(null == gradeRec || "false".equals(gradeRec.getChildText("enabled")))
        	{
        		xmlDocUtil.writeErrorMsg("10645", "��Ӧ�꼶δ����/��Ч");
        		return;
        	}
        	int classnum = new Integer(gradeRec.getChildText("classnum")).intValue();
        	String gradename = gradeRec.getChildText("gradename");
        	if(0>=classnum)
        	{
        		xmlDocUtil.writeErrorMsg("10646", "��Ӧ�༶��δ����");
        		return;
        	}
        	//��ѯ���а༶��¼
        	sqlBuf.setLength(0);
        	sqlBuf.append("select max(classcode) as maxcls from base_class where deptid=? and gradecode=? and state<>?");
        	bvals.add("3");//�ѱ�ҵ
        	Element maxRec = DaoUtil.getOneRecord(sqlBuf.toString(), bvals);
        	int max = 0;
        	if(null != maxRec && StringUtil.isNotEmpty(maxRec.getChildText("maxcls")))
        	{
        		max = new Integer(maxRec.getChildText("maxcls")).intValue();
        	}
        	if(max>=classnum)
        	{
        		xmlDocUtil.writeErrorMsg("10647", "���а༶���ڰ༶��������");
        		return;
        	}
        	Element record = ConfigDocument.createRecordElement("yuexue","base_class");
        	StringBuffer hintmsg = new StringBuffer("���ɰ༶[");
    		for(int i=max+1; i<=classnum; i++)
    		{
    			hintmsg.append(i).append(",");
    			StringBuffer clsnm = new StringBuffer(gradename).append("(").append(i).append(")").append("��");
    			record.removeContent();
    			XmlDocPkgUtil.setChildText(record, "deptid", deptid);
    			XmlDocPkgUtil.setChildText(record, "gradecode", gradecode);
    			XmlDocPkgUtil.setChildText(record, "classcode", String.valueOf(i));
    			XmlDocPkgUtil.setChildText(record, "classnm", clsnm.toString());
    			XmlDocPkgUtil.setChildText(record, "createdt", DatetimeUtil.getNow(null));
    			XmlDocPkgUtil.setChildText(record, "state", "1");
    			pDao.insertOneRecord(record);
    		}
    		hintmsg.deleteCharAt(hintmsg.length()-1);
    		hintmsg.append("],�����ð༶��ѧ��");
    		
        	pDao.commitTransaction();
        	xmlDocUtil.setResult("0");
        	xmlDocUtil.writeHintMsg("10648", hintmsg.toString());
        }
        catch(Exception ex)
        {
        	pDao.rollBack();
        	ex.printStackTrace();
        	log4j.logError(ex);
        	xmlDocUtil.writeHintMsg("10649", "�������ɰ༶����");
        }
        finally
        {
        	pDao.releaseConnection();
        }
    }
}
