package cn.com.bright.yuexue.base;

import org.jdom.Document;

import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * <p>Title:��ͥ���������</p>
 * <p>Description: ѧ������ʦ��ͥ���������</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author E40
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 *      
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time          version      desc</p>
 *     
 */
public class FamilyManager {
	private XmlDocPkgUtil xmlDocUtil = null;
	private Log log4j = new Log(this.getClass().toString());
	
    /**
     * ��̬ί�����
     * @param request xmlDoc 
     * @return response xmlDoc
     */
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		return xmlDoc;
	}
}
