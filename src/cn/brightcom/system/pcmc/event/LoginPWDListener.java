package cn.brightcom.system.pcmc.event;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.event.Event;
import cn.brightcom.jraf.event.Listener;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.system.pcmc.util.Des;


public class LoginPWDListener extends Listener
{
	public void fire(Event evt, Object param) {
		if(null == evt || null == param ) return;
		try
		{
			Des desObj = new Des();
			Document xmlDoc = (Document)param;
			XmlDocPkgUtil xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
			String action = xmlDocUtil.getAction();
            if("login".equals(action))
			{
            	String password = xmlDocUtil.getRequestData().getChildText("userpwd");
            	String userpwd = desObj.strDec(password) ;
            	Element root = xmlDoc.getRootElement();
            	root.getChild("Request").getChild("Data").getChild("userpwd").setText(userpwd);
			}
		}
		catch(Exception ex)
		{
			System.out.println("Ω‚√‹“Ï≥££∫"+ex.getMessage());
		}
	}
}
