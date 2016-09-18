package cn.brightcom.system.pcmc.pm;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.util.JDomUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.web.HttpProcesser;
import cn.brightcom.jraf.web.SwitchCenter;

import javax.servlet.http.HttpServletRequest ;

/**
 * <p>Title:��Ϣ��ȡ�� </p>
 * <p>Description:���ţ�֪ͨ����Ϣ��ȡ </p>
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
     * �������/������Ϣ
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
            log4j.logError("[-FAIL->> ��ȡ��Ϣʧ��!"+e.getMessage());
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
    //��ȡ�Ƿ����δ���Ķ���Ϣ
    public static boolean isHaveNewMes(String isread,HttpServletRequest request)
    {
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","pm","getMessageNum",request);
        reqXml.getRootElement().getChild("Request").getChild("Data").addContent(new Element("isread").setText(isread));
        //Ŀǰ��û����¼���֣�������������������������
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
            //log4j.logError("[-FAIL->> ��ȡ��Ϣʧ��!"+e.getMessage());
            //log4j.logInfo(JDomUtil.toXML(reqXml,"gb2312",true));
        }
        return false;
    }
    //�����û���ȡδ�Ķ�����Ϣ
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
            //log4j.logError("[-FAIL->> ��ȡ��Ϣʧ��!"+e.getMessage());
            //log4j.logInfo(JDomUtil.toXML(reqXml,"gb2312",true));
        }
        return result;
    }
}
