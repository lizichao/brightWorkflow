package cn.brightcom.system.pcmc.event;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.event.Event;
import cn.brightcom.jraf.event.Listener;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.brightcom.jraf.web.CaptchaService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2009 Brightcom</p>
 * <p>Company: ShenZhen BrightCom Technological Co.,Ltd.</p>
 * @author <a href="http://www.szbrightcom.cn" target="_blank">QIUMH</a>
 * @version 1.0a
 */
public class CaptchaValidateListener extends Listener
{
	public void fire(Event evt, Object param) throws Exception
	{
		if(null == evt || null == param ) return;
		try
		{
			Document xmlDoc = (Document)param;
			XmlDocPkgUtil xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
			
			HttpServletRequest req = ApplicationContext.getRequest();
			String anwser = req.getParameter("captchaCode");
			Object err = req.getSession().getAttribute("validateCaptchaErr");
			boolean v = CaptchaService.getInstance().validateCaptchaResponse(anwser, ApplicationContext.getRequest());
			if(v || null != err)
			{
				req.getSession().removeAttribute("validateCaptchaErr");
			}
			else
			{
				xmlDocUtil.writeErrorMsg("验证码错误");
				throw new Exception("验证码错误");
			}			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}

}
