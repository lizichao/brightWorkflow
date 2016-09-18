package cn.brightcom.system.pcmc.pm;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.web.HttpProcesser;
import cn.brightcom.jraf.web.SwitchCenter;

import javax.servlet.http.HttpServletRequest ;

/**
 * <p>Title:信息获取类 </p>
 * <p>Description:新闻，通知等信息获取 </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author kyle zhu
 * @version 1.0
 */

public class PmInformations
{
    public PmInformations()
    {
    }

    public static java.util.List getFrdlink()
    {
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","sm_query","getFrdlink");
        Document resXml = null;
        resXml = SwitchCenter.doPost(reqXml);
        Element data = resXml.getRootElement().getChild("Response").getChild("Data");
        if(null != data)
            return data.getChildren("Record");
        return new java.util.ArrayList();
    }
    /**
     * 获得新闻/公告信息
     * @param size String
     */
    public static Element getInformations(String size)
    {
        Element result = null;
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","pm","getInfomList");
        reqXml.getRootElement().getChild("Request").getChild("Data").addContent(new Element("size").setText(size));
        Document resXml = null;
        try
        {
            resXml = SwitchCenter.doPost(reqXml);
            int flag = Integer.parseInt(resXml.getRootElement().getChild("Response").getAttributeValue("result"));
            if(flag == 0)
            {
                result = resXml.getRootElement().getChild("Response").getChild("Data");
            }
        }
        catch(Exception e)
        {
            Log log4j = new Log("PmInformations.class");
            log4j.logError("[-FAIL->> 获取信息失败!"+e.getMessage());
            log4j.logInfo(JDomUtil.toXML(reqXml,"gb2312",true));
        }
        return result;
    }
    public static Element getSingleInfomations(long infoid)
    {
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","pm","getInfomSingle");
        reqXml.getRootElement().getChild("Request").getChild("Data").addContent(new Element("infoid").setText(Long.toString(infoid)));
        Document resXml = SwitchCenter.doPost(reqXml);
        Element result = resXml.getRootElement().getChild("Response").getChild("Data").getChild("Record");
        return result;
    }
    //获取是否存在未读的短消息
    public static boolean isHaveNewMes(String isread,HttpServletRequest request)
    {
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","pm","getMessageNum",request);
        reqXml.getRootElement().getChild("Request").getChild("Data").addContent(new Element("isread").setText(isread));
        //目前还没做登录部分，方便程序下面代码先做测试用
        //reqXml.getRootElement().addContent(new Element("Session"));
        //reqXml.getRootElement().getChild("Session").addContent(new Element("userid").setText(userid));
        Document resXml = null;
        try
        {
            resXml = SwitchCenter.doPost(reqXml);
            int flag = Integer.parseInt(resXml.getRootElement().getChild("Response").getAttributeValue("result"));
            if(flag ==0)
            {
                if(Integer.parseInt(resXml.getRootElement().getChild("Response").
                                    getChild("Data").getChild("Record").getChildTextTrim("num")) > 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch(Exception e)
        {
            //Log log4j = new Log("PmInformations.class");
            //log4j.logError("[-FAIL->> 获取信息失败!"+e.getMessage());
            //log4j.logInfo(JDomUtil.toXML(reqXml,"gb2312",true));
        }
        return false;
    }
    //根据用户获取未阅读的消息
    public static Element getMessList(String isread,String size,String pageno,HttpServletRequest request)
    {
        Element result = null;
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","pm","getNewMessageList",request);
        reqXml.getRootElement().getChild("Request").getChild("Data").addContent(new Element("isread").setText(isread));
        reqXml.getRootElement().getChild("Request").getChild("PageInfo").addContent(new Element("PageSize").setText(size));
        reqXml.getRootElement().getChild("Request").getChild("PageInfo").addContent(new Element("PageNo").setText(pageno));
        Document resXml = null;
        try
        {
            resXml = SwitchCenter.doPost(reqXml);
            int flag = Integer.parseInt(resXml.getRootElement().getChild("Response").getAttributeValue("result"));
            if(flag ==0)
            {
                if(Integer.parseInt(resXml.getRootElement().getChild("Response").
                                    getChild("Data").getChild("PageInfo").getChildTextTrim("RecordCount")) > 0)
                {
                    result = resXml.getRootElement().getChild("Response").getChild("Data");
                }
                else
                {
                    result = null;
                }
            }
        }
        catch(Exception e)
        {
            //Log log4j = new Log("PmInformations.class");
            //log4j.logError("[-FAIL->> 获取信息失败!"+e.getMessage());
            //log4j.logInfo(JDomUtil.toXML(reqXml,"gb2312",true));
        }
        return result;
    }
}
