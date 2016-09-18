package cn.brightcom.system.pcmc.sm;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.jraf.web.action.HTMLAction;


public class LoginHtmlAction implements HTMLAction
{
	public void doEnd(HttpServletRequest request, Document xmlDoc,
			HashMap response)
	{
		XmlDocPkgUtil xmlUtil = new XmlDocPkgUtil(xmlDoc);
		if("0".equals(xmlUtil.getResult()) && "login".equals(xmlUtil.getAction()))
		{
			try
			{
				Element record = xmlUtil.getResponse().getChild("Data").getChild("Record");
				List list = record.getChildren();
				for(int i=0; i<list.size(); i++)
				{
					Element f = (Element)list.get(i);
					if("pagesize".equals(f.getName()))
					{
						request.getSession().setAttribute("PageSize", f.getText());
					}
					else
					{
						request.getSession().setAttribute(f.getName(), f.getText());
					}					
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public HashMap doStart(HttpServletRequest request, Document xmlDoc)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
