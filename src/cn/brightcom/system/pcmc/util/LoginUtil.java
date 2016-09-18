package cn.brightcom.system.pcmc.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.conf.Configuration;
import cn.brightcom.system.pcmc.sm.l.Validator;

public class LoginUtil
{
	private final static String DEFAULT_PKG = "cn.brightcom.system.pcmc.sm.l.";
	private static List<Validator> fldList = new ArrayList<Validator>();
	
	static
	{
		try
		{
			Configuration cfg = BrightComConfig.getConfiguration();
			Document xmlCfg = (Document)cfg.getConfig();
			Element msgsEle = xmlCfg.getRootElement().getChild("login");
			if(null != msgsEle)
			{
				List list = msgsEle.getChildren("field");
				for(int i=0; i<list.size(); i++)
				{
					Element msg = (Element)list.get(i);
					String code = msg.getAttributeValue("name");
					String cls = msg.getAttributeValue("class");
					String text = msg.getTextTrim();
					try
					{
						if(cls.indexOf(".")==-1)
							cls = DEFAULT_PKG+cls;
						Class clazz = Class.forName(cls);
						Validator vv = (Validator)clazz.newInstance();
						vv.setName(code);
						vv.setRegex(text);
						fldList.add(vv);
					}
					catch(Exception ve)
					{
						ve.printStackTrace();
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static List<Validator> getLoginField()
	{
		return fldList;
	}	
}
