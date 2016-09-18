package cn.brightcom.system.pcmc.event;

import java.util.Hashtable;
import java.util.List;

import org.jdom.*;

import cn.brightcom.jraf.auth.OnlineUser;
import cn.brightcom.jraf.auth.OnlineUserManager;
import cn.brightcom.jraf.auth.SessionListener;
import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.conf.Configuration;
import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.event.Event;
import cn.brightcom.jraf.event.Listener;
import cn.brightcom.jraf.util.StringUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;


public class LoginListener extends Listener
{
	private final static Hashtable<String, String> TYPE_RESULT = new Hashtable<String, String>();
	static
	{
		Configuration cfg = BrightComConfig.getConfiguration();
		Document xmlCfg = (Document)cfg.getConfig();
		Element loginEle = xmlCfg.getRootElement().getChild("login");
		if(null != loginEle)
		{
			Element mainEle = loginEle.getChild("mainpage");
			if(null != mainEle)
			{
				List pglist = mainEle.getChildren("page");
				for(int i=0; i<pglist.size(); i++)
				{
					Element pgEle = (Element)pglist.get(i);
					String usertype = pgEle.getAttributeValue("usertype");
					String result = pgEle.getAttributeValue("result");
					TYPE_RESULT.put(usertype, result);
				}
			}
		}
	}
	public void fire(Event evt, Object param)
		throws Exception
	{
		if(null == evt || null == param ) return;
		try
		{
			Document xmlDoc = (Document)param;
			XmlDocPkgUtil xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
			String action = xmlDocUtil.getAction();
			OnlineUserManager userMgr = OnlineUserManager.getInstance();
			System.out.println("LoginListener:"+xmlDocUtil.getResult()+","+xmlDocUtil.getSysName()+","+xmlDocUtil.getOprID()+","+xmlDocUtil.getAction());
            if("0".equals(xmlDocUtil.getResult()))
			{
				if("login".equals(action))
				{
					ApplicationContext.getRequest().getSession().removeAttribute(SessionListener.SESSION_LISTENER);
					String userid = null;//xmlDocUtil.getRequestData().getChildText("usercode");
										
					Element record = xmlDocUtil.getResponse().getChild("Data").getChild("Record");
					List list = record.getChildren();
					for(int i=0; i<list.size(); i++)
					{
						Element f = (Element)list.get(i);
						if("pagesize".equals(f.getName()))
						{
							ApplicationContext.getRequest().getSession().setAttribute("PageSize", f.getText());
						}
						else
						{
							ApplicationContext.getRequest().getSession().setAttribute(f.getName(), f.getText());
							if("userid".equals(f.getName()))
							{
								userid = f.getText();
							}
						}
					}
					
					String sid = ApplicationContext.getRequest().getSession().getId();
					OnlineUser user = userMgr.getOnlineUser(userid);
					if(null == user)
					{
						user = new OnlineUser();
						user.setUserid(userid);
						userMgr.setOnlineUser(userid, user);
					}
					if(user.putSessionId(sid))
					{
						SessionListener sl = new SessionListener(userid,sid);
						ApplicationContext.getRequest().getSession().setAttribute(SessionListener.SESSION_LISTENER,sl);
					}
					
					//Ò³ÃæÌø×ª
					String usertype = record.getChildTextTrim("usertype");
					String tpResult = TYPE_RESULT.get(usertype);
					if(StringUtil.isNotEmpty(tpResult))
					{
						xmlDocUtil.setResult(tpResult);
					}
				}
				else if("logout".equals(action))
				{
					//String usercode = xmlDocUtil.getSession().getChildText("usercode");
					//if(StringUtil.isNotEmpty(usercode))
					//	userMgr.setOnlineUser(usercode, null);
					ApplicationContext.getRequest().getSession().invalidate();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
