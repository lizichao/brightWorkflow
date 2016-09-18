package cn.brightcom.system.pcmc.util;

import cn.brightcom.jraf.conf.SystemConfig;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.web.HttpProcesser;
import cn.brightcom.jraf.web.SwitchCenter;

import org.jdom.*;
import java.util.*;


public class ITEMS
{
    private static Hashtable paramMap = new Hashtable();
    public ITEMS()
    {
    }
    /**
     * 获取用户列表
     * @return
     */
    public static String getUserOptions(String userid,Document respXml)
    {
        StringBuffer option = new StringBuffer();
        java.util.List recordList = null;
        try
        {
            recordList = respXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
            int maxLenDeptCode = 0;
            int maxLenUserCode = 0;
            for (int i=0;i<recordList.size();i++)
            {
                Element record = (Element)recordList.get(i);
                if (record.getChildTextTrim("deptcode").length() > maxLenDeptCode)
                {
                    maxLenDeptCode = record.getChildTextTrim("deptcode").length();
                }
            }
            for (int i=0;i<recordList.size();i++)
            {
                Element record = (Element)recordList.get(i);
                if (record.getChildTextTrim("usercode").length() > maxLenUserCode)
                {
                    maxLenUserCode = record.getChildTextTrim("usercode").length();
                }
            }
            for (int i=0;i<recordList.size();i++)
            {
                Element record = (Element)recordList.get(i);
                option.append("<option value='");
                option.append(record.getChildTextTrim("userid"));
                option.append("'");
                if (userid.equals(record.getChildTextTrim("userid")))
                {
                    option.append(" selected ");
                }
                option.append(">");
                option.append(appendBlock(maxLenDeptCode - record.getChildTextTrim("deptcode").length(),record.getChildTextTrim("deptcode")));
                option.append("|");
                option.append(appendBlock(maxLenUserCode - record.getChildTextTrim("usercode").length(),record.getChildTextTrim("usercode")));
                option.append("|");
                option.append(record.getChildTextTrim("username"));
                option.append("</option>");
            }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            System.out.println("[-FAIL->> 用户列表获取失败!\n" + e.getMessage());
        }
        return option.toString();
    }
    /**
     * 补空格
     * @return
     */
    private static String appendBlock(int count, String str)
    {
        StringBuffer sb = new StringBuffer(str);
        for(int i = 0; i <= count; i ++)
        {
            sb.append("_");
        }
        return sb.toString();
    }
    /**
     * 获取"年"选项(无参数)
     * @return
     */
    public static String getYearOption()
    {
        return getYearOption(1980,2020);
    }
    public static String getYearOption(String currentYear)
    {
        return getYearOption(1980,2020,currentYear);
    }
    /**
     * 获取"年"选项(根据起始年和结束年)
     * @param beginYear
     * @param endYear
     * @return
     */
    public static String getYearOption(int beginYear,int endYear)
    {
        return getYearOption(beginYear,endYear,"-1");
    }
    /**
     * 获取"年"选项(根据起始年和结束年)
     * @param beginYear
     * @param endYear
     * @return
     */
    public static String getYearOption(int beginYear,int endYear,String currentYear)
    {
        StringBuffer yearOption = new StringBuffer("");
        boolean isSelect = false;
        for (int i=beginYear;i<=endYear;i++)
        {
            yearOption.append("<option value='");
            yearOption.append(i);
            yearOption.append("'");
            if (Integer.toString(i).equals(currentYear))
            {
                yearOption.append(" selected ");
                isSelect = true;
            } else if (i==endYear && !isSelect) {
                yearOption.append(" selected ");
            }
            yearOption.append(">");
            yearOption.append(i);
            yearOption.append("</option>");
        }
        return yearOption.toString();
    }
    /**
     * 获取月份选项
     * @return
     */
    public static String getMonthOption()
    {
        return getMonthOption("-1");
    }
    /**
     * 获取月份选项
     * @return
     */
    public static String getMonthOption(String currentMonth)
    {
        StringBuffer monthOption = new StringBuffer("");
        for (int i=1;i<13;i++)
        {
            String temp = null;
            if (i<10)
            {
                temp = "0" + Integer.toString(i);
            }
            else
            {
                temp = Integer.toString(i);
            }
            monthOption.append("<option value='");
            monthOption.append(temp);
            monthOption.append("'");

            if (temp.equals(currentMonth))
            {
                monthOption.append(" selected ");
            }
            monthOption.append(">");
            monthOption.append(temp);
            monthOption.append("</option>");
        }
        return monthOption.toString();
    }

    /**
     * 根据paramname和paramvalue取得相应parammeaings
     * @param paramname
     * @param paramvalue
     * @return parammeanings
     */
    public static String getParamDesc(String paramname, String paramcode)
    {
        if (!paramMap.containsKey(paramname))
        {
            getParamDetailList(paramname);
        }
        List paramList = (List) paramMap.get(paramname);
        return getParamDesc(paramList, paramcode);
    }

    /**
     * 从查询列表中根据paramvalue获取相应parammeanings
     * @param paramList
     * @param paramvalue
     * @return parammeanings
     */
    public static String getParamDesc(List paramList, String paramcode)
    {
        if (paramList == null)
        {
            return "<font color=\"red\">未知参数名！！</font>";
        }
        if (paramcode == null || paramcode.equals(""))
        {
            return "";
        }
        Element paramElement = null;
        for (int i = 0; i < paramList.size(); i++)
        {
            //Record结点
            paramElement = (Element) paramList.get(i);
            if (paramcode.equals(paramElement.getChildText("paramcode")))
            {
                return paramElement.getChildText("parammeanings");
            }
        }
        return "";
    }

    /**
     * 根据paramname获取参数列表集，并根据paramvalue设定当前选中项（返回<option...></option> HTML代码）
     * @param paramname
     * @param paramcode
     * @return
     */
    public static String getParamOption(String paramname, String paramcode)
    {
        if (!paramMap.containsKey(paramname))
        {
            getParamDetailList(paramname);
        }
        List paramList = (List) paramMap.get(paramname);

        return getParamOption(paramList,paramcode,null,true);
    }
    /**
     * 根据paramname获取参数列表集，并根据paramvalue设定当前选中项（返回<input type="radio" name="componentName" value="paramcode"> HTML代码）
     * @param paramname
     * @param paramcode
     * @param componentName
     * @param isOption
     * @return
     */
    public static String getParamOption(String paramname, String paramcode,String componentName,boolean isOption)
    {
        if (!paramMap.containsKey(paramname))
        {
            getParamDetailList(paramname);
        }
        List paramList = (List) paramMap.get(paramname);

        return getParamOption(paramList,paramcode,componentName,isOption);
    }

    /**
     * 根据paramname获取参数列表集，（返回<option...></option> HTML代码）
     * @param paramname
     * @return <option value="paramcode">parammeanings</option>
     */
    public static String getParamOption(String paramname)
    {
        return getParamOption(paramname,null);
    }

    public static String getParamOption(String paramname,String componentName,boolean isOption)
    {
        return getParamOption(paramname,null,componentName,isOption);
    }
    /**
     * 根据参数列表及参数当前值组织选项，（返回<option...></option> HTML代码）
     * @param paramList
     * @param paramcode
     * @param componentName
     * @param isOption
     * @return <option value="paramcode">parammeanings</option>
     */
    public static String getParamOption(List paramList,String paramcode,String componentName,boolean isOption)
    {
        StringBuffer option = new StringBuffer("");
        if (isOption)
        {
            if ((paramList == null)||(paramList.size()==0))
            {
                return "<option value=\"-1\"><font color=\"red\">未知参数！！</font></option>";
            }
            String paramcode2 = null;
            String parammeanings = null;
            Element paramElement = null;
            for (int i = 0; i < paramList.size(); i++)
            {
                paramElement = (Element) paramList.get(i);
                paramcode2 = paramElement.getChildText("paramcode");
                parammeanings = paramElement.getChildText("parammeanings");
                if (paramcode2.equals(paramcode))
                {
                    option.append("<option value=\"" + paramcode2 + "\" selected>" + parammeanings + "</option>");
                }
                else
                {
                    option.append("<option value=\"" + paramcode2 + "\">" + parammeanings + "</option>");
                }
            }
        }
        else
        {
            if ((paramList == null)||(paramList.size()==0))
            {
                return "<font color=\"red\">未知参数！！</font>";
            }
            String paramcode2 = null;
            String parammeanings = null;
            Element paramElement = null;
            for (int i = 0; i < paramList.size(); i++)
            {
                paramElement = (Element) paramList.get(i);
                paramcode2 = paramElement.getChildText("paramcode");
                parammeanings = paramElement.getChildText("parammeanings");
                if (paramcode2.equals(paramcode))
                {
                    option.append("<input type=\"radio\" name=\""+ componentName +"\" value=\"" + paramcode2 + "\" checked>" + parammeanings);
                }
                else
                {
                    option.append("<input type=\"radio\" name=\""+ componentName +"\" value=\"" + paramcode2 + "\">" + parammeanings);
                }
            }

        }
        return option.toString();
    }
    /**
     * 根据paramname获取相应参数列表
     * @param paramname
     */
    private static void getParamDetailList(String paramname)
    {
        List paramList = null;
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","sm_query", "getParamDetailList");
        reqXml.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("paramname")).setText(paramname));
        try
        {
            Document resXml = SwitchCenter.doPost(reqXml);
            paramList = resXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("[-FAIL->> 参数:" + paramname + "列表获取失败!\n" + e.getMessage());
        }
        if (null != paramList)
            paramMap.put(paramname, paramList);
    }
    /**
     * 根据paramname获取相应参数列表
     * @param paramname
     */
    public static String getSubSysOption(String subsysid)
    {
        StringBuffer result = new StringBuffer("");
        List subSysList = null;
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","subsys", "listAllSubSys");
        try
        {
            Document resXml = SwitchCenter.doPost(reqXml);
            subSysList = resXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
            if (subSysList != null)
            {
                for (int i = 0 ; i < subSysList.size() ; i++)
                {
                    Element record = (Element) subSysList.get(i) ;
                    if (record.getChildTextTrim("subsysid").equals(subsysid))
                    {
                        result.append("<option value='" + record.getChildTextTrim("subsysid") + "' selected>" + record.getChildTextTrim("cnname") + "</option>") ;
                    }
                    else
                    {
                        result.append("<option value='" + record.getChildTextTrim("subsysid") + "'>" + record.getChildTextTrim("cnname") + "</option>") ;
                    }
                }
            }
        }
        catch (Exception e)
        {
        }
        return result.toString();
    }
    public static List getAllRoleList()
    {
        StringBuffer result = new StringBuffer("");
        List roleList = null;
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","userrole", "listAllRole");
        try
        {
            Document resXml = SwitchCenter.doPost(reqXml);
            roleList = resXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
        }
        catch (Exception e)
        {
        }
        return roleList;
    }
    public static String getSubSysOptions(String sysName)
    {
        StringBuffer options = new StringBuffer();
        if (ConfigDocument.getSysMap()!=null)
        {
            Object subKeyList[] = ConfigDocument.getSysMap().keySet().toArray();
            for (int i=0;i<subKeyList.length;i++)
            {
                String key = (String)subKeyList[i];
                String desc = ConfigDocument.getSystemDesc(key);
                if (key.equals(sysName))
                {
                    options.append("<option value='"+key + "' selected>"+ desc + "</option>");
                }
                else
                {
                    options.append("<option value='"+key + "'>"+ desc + "</option>");
                }
            }
        }
        return options.toString();
    }
    public static java.util.ArrayList getAllOprDesc()
    {
        java.util.ArrayList oprDescList = new java.util.ArrayList();
        if (ConfigDocument.getSysMap()!=null)
        {
            Object subKeyList[] = ConfigDocument.getSysMap().keySet().toArray();
            for (int i=0;i<subKeyList.length;i++)
            {
                String key = (String)subKeyList[i];
                SystemConfig doc = (SystemConfig)ConfigDocument.getSysMap().get(key);
                Object oprList[] = doc.getOprConfig().keySet().toArray();
                for (int j = 0;j<oprList.length;j++)
                {
                    String oprID = (String)oprList[i];
                    String oprDesc = doc.getOprElement(oprID).getAttributeValue("desc");
                    String opr[] = {key,oprID,oprDesc};
                    oprDescList.add(opr);
                }
            }
        }
        return oprDescList;
    }
    public static String getSubSysDesc(String subsysid)
    {
        StringBuffer result = new StringBuffer("");
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","subsys", "getSubSysDetail");
        reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("subsysid")).setText(subsysid));
        try
        {
            Document resXml = SwitchCenter.doPost(reqXml);
            result.append(resXml.getRootElement().getChild("Response").getChild("Data").getChild("Record").getChildTextTrim("cnname"));
        }
        catch (Exception e)
        {
        }
        return result.toString();
    }

    public static Document getRoleSubsysDetail(String subsysid)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "subsys", "getSubSysDetail") ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("subsysid")).setText(subsysid)) ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        Element ele = repXml.getRootElement().getChild("Response").getChild("Data").getChild("Record") ;
        //return (Document) ConfigDocument.oprConfig.get(ele.getChildTextTrim("shortname"));
        return null;
    }

    public static String getRoleActExits(String roleid,String oprid,String action)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "userrole", "getRoleActExits") ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("roleid")).setText(roleid)) ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("__oprid")).setText(oprid)) ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("action")).setText(action)) ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        Element ele = repXml.getRootElement().getChild("Response").getChild("Data").getChild("PageInfo") ;
        String vals = "";
        if (!"0".equals(ele.getChildTextTrim("RecordCount")))
        {
            vals = "checked";
        }
        return  vals;
    }

    public static String toChi(String input) {
        try {
            byte[] bytes = input.getBytes("ISO8859-1");
            return new String(bytes);
        }
        catch (Exception ex) {
        }
        return null;
    }

    /**
     * 获取当前用户所包含角色对应的系统模块
     * */

    public static String getRoleSubSysOption(String userid,String subsysid)
    {
        StringBuffer result = new StringBuffer("");
        List subSysList = null;
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","subsys", "getRoleSubSysList");
        reqXml.getRootElement().getChild("Request").getChild("Data").addContent(new Element("userid").setText(userid));
        try
        {
            Document resXml = SwitchCenter.doPost(reqXml);
            subSysList = resXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
            if (subSysList != null)
            {
                for (int i = 0 ; i < subSysList.size() ; i++)
                {
                    Element record = (Element) subSysList.get(i) ;
                    if (record.getChildTextTrim("subsysid").equals(subsysid))
                    {
                        result.append("<option value='" + record.getChildTextTrim("subsysid") + "' selected>" + record.getChildTextTrim("cnname") + "</option>") ;
                    }
                    else
                    {
                        result.append("<option value='" + record.getChildTextTrim("subsysid") + "'>" + record.getChildTextTrim("cnname") + "</option>") ;
                    }
                }
            }
        }
        catch (Exception e)
        {
        }
        return result.toString();
    }

    public static String getRoleSubSysOption(String userid)
    {
        return getRoleSubSysOption(userid,"");
    }

    /**
     * 获取当前用户所包含的角色
     * */
    public static Document getRoleList(String userid)
    {
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","userrole","getRoleList");
        reqXml.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("userid")).setText(userid));
        Document xmlDoc = SwitchCenter.doPost(reqXml);
        return xmlDoc;
    }

    public static Document getMRoleList(String userid)
    {
        Document reqXml = HttpProcesser.createRequestPackage("pcmc","userrole","getMRoleList");
        reqXml.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("userid")).setText(userid));
        Document xmlDoc = SwitchCenter.doPost(reqXml);
        return xmlDoc;
    }    
}
