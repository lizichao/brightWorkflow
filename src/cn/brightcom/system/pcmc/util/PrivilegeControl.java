package cn.brightcom.system.pcmc.util;

import org.jdom.*;

import cn.brightcom.jraf.web.HttpProcesser;
import cn.brightcom.jraf.web.SwitchCenter;

public class PrivilegeControl
{
    public PrivilegeControl()
    {
    }
    public boolean checkIsInternet(String menuid,String ipAddress)
    {
        if ((menuid==null)||(menuid.length()<1))
        {
            return false;
        }
        try
        {
            Long.parseLong(menuid) ;
        }
        catch (java.lang.NumberFormatException e)
        {
            return false;
        }
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","menu","getMenuById");
        reqXml.getRootElement().getChild("Request").addContent((new Element("menuid")).setText(menuid));
        Document repXml = SwitchCenter.doPost(reqXml);
        Element record = repXml.getRootElement().getChild("Response").getChild("Data").getChild("Record");
        if (record==null)
        {
            return false;
        }
        String isInternet = record.getChildTextTrim("isinternet");
        if ("1".equals(isInternet))
        {
            return true;
        }
        return checkInsideIP(ipAddress);
    }
    private boolean checkInsideIP(String ipAddresss)
    {
        if (ipAddresss.startsWith("10"))
        {
            return true;
        }
        if (ipAddresss.startsWith("172"))
        {
            return true;
        }
        if (ipAddresss.startsWith("192"))
        {
            return true;
        }
        return false;
    }
}
