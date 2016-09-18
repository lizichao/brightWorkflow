package cn.com.bright.yuexue.base;

import org.jdom.Document;

import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * <p>Title:家庭情况处理类</p>
 * <p>Description: 学生、教师家庭情况处理类</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author E40
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 *      
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time          version      desc</p>
 *     
 */
public class FamilyManager {
	private XmlDocPkgUtil xmlDocUtil = null;
	private Log log4j = new Log(this.getClass().toString());
	
    /**
     * 动态委派入口
     * @param request xmlDoc 
     * @return response xmlDoc
     */
	public Document doPost(Document xmlDoc) {
		xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
		String action = xmlDocUtil.getAction();
		return xmlDoc;
	}
}
