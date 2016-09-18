package cn.brightcom.system.pcmc.sm;

import org.jdom.Document;

import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

public class UserTypeRole
{
	private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(UserTypeRole.class.getName());
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	
    	return xmlDoc;
    }
}
