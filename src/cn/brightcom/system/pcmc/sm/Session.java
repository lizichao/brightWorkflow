package cn.brightcom.system.pcmc.sm;

import java.util.List;

import cn.brightcom.jraf.util.DES;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

import org.jdom.Document;
import org.jdom.Element;


public class Session
{
	private XmlDocPkgUtil xmlDocUtil = null;
	
	private static String v=null;
    
    public Document doPost(Document xmlDoc)
    {
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	if("get".equals(action))
    	{
    		get();
    	}
    	
    	return xmlDoc;
    }
    
    private final void get()
    {
    	Element s=xmlDocUtil.getSession();
    	Element data = XmlDocPkgUtil.createResponseData();
    	Element fields = data.getChild("Metadata").getChild("Fields");
    	Element record = new Element("Record");
    	data.addContent(record);
    	
    	List l = s.getChildren();
    	for(int i=0; i<l.size(); i++)
    	{
    		Element ele = (Element)l.get(i);
    		String name = ele.getName();
    		String val = ele.getText();
    		fields.addContent(XmlDocPkgUtil.createFieldEle(name));
    		record.addContent(new Element(name).setText(val));
    	}
    	
    	Element ver = xmlDocUtil.getVer();
    	StringBuffer buf = new StringBuffer();
    	l = ver.getChildren();
    	for(int i=0; i<l.size(); i++)
    	{
    		Element ele = (Element)l.get(i);
    		String val = ele.getText();
    		buf.append(val).append(" ");
    	}
    	record.addContent(new Element("ver1").setText(buf.toString().trim()));
    	if(null == v)
    	{
    		try
    		{
	    		buf.append(new char[]{'B','r','i','g','h','t','C','o','m'});
	    		v = StringUtil.toHex(DES.md5(buf.toString().getBytes()));	    		
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
    		}
    	}
    	record.addContent(new Element("ver2").setText(v));
    	
    	xmlDocUtil.getResponse().addContent(data);
    	xmlDocUtil.setResult("0");
    }
}
