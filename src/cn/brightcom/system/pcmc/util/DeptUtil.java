package cn.brightcom.system.pcmc.util ;

import org.jdom.* ;
import java.util.List ;
import java.util.ArrayList ;
import java.util.HashMap;

import cn.brightcom.jraf.web.HttpProcesser;
import cn.brightcom.jraf.web.SwitchCenter;

/**
 * 修改日志：
 * 2004-09-15    qiumh    修改getDeptTree(String deptid, List deptList)
 *                        不使用递归，避免n次遍历
 *
 */

/**
 *
 * <p>Title: 部门工具类</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author 邱爱国
 * @version 1.0
 */
public class DeptUtil
{
    public DeptUtil()
    {
    }

    /**
     * 获取当前部门及其子部门的ID,并组织成 1,2,3...,n 的形式
     * @param deptid
     * @return
     */
    public static String getDeptIds(String deptid)
    {
        ArrayList deptList = getDeptTree(deptid) ;
        return getDeptIds(deptList).toString() + deptid ;
    }

    public static String listDeptAndUser()
    {
        ArrayList deptTree = getDeptTree(null) ;
        List userList = getAllUser();
        StringBuffer deptHtmlTree = listDeptAndUser(deptTree,userList) ;
        return "<ul>\n" + deptHtmlTree.toString() + "\n" + "</ul>\n" ;
    }

    private static StringBuffer listDeptAndUser(ArrayList deptTree,List userList)
    {
        StringBuffer result = new StringBuffer("") ;
        if ( (deptTree != null) && (deptTree.size() > 0))
        {
            for (int i = 0 ; i < deptTree.size() ; i++)
            {
                Element currentNode = (Element) deptTree.get(i) ;
                result.append(listDeptAndUser(currentNode,userList)) ;
            }
        }
        return result ;
    }

    /*用于短消息发送，树状，选择部门其下的用户*/
    private static StringBuffer listDeptAndUser(Element deptTree,List userList)
    {
        StringBuffer deptHtmlTree = new StringBuffer("") ;
        List childList = deptTree.getChildren("Record") ;
        String deptid = deptTree.getChildTextTrim("deptid") ;
        String pdeptid = deptTree.getChildTextTrim("pdeptid") ;
        boolean hasUser = false;
        for (int i = 0 ; i < userList.size() ; i++)
        {
            Element userElement = (Element) userList.get(i) ;
            if (deptid.equals(userElement.getChildTextTrim("deptid")))
            {
                hasUser = true;
                break;
            }
        }
        if (( (childList != null) && (childList.size() > 0))||hasUser)
        {
            deptHtmlTree.append("<li class='clsItemShow' id='i" + deptTree.getChildTextTrim("deptid") + "' onClick='OnItemClick();'>" + deptTree.getChildTextTrim("deptname") + "</li>\n") ;
            deptHtmlTree.append("<ul class='clsItemShow' id='i" + deptTree.getChildTextTrim("deptid") + "u'>\n") ;
            for (int i = 0 ; i < childList.size() ; i++)
            {
                Element currentChild = (Element) childList.get(i) ;
                deptHtmlTree.append(listDeptAndUser(currentChild,userList)) ;
            }
            for (int i = 0 ; i < userList.size() ; i++)
            {
                Element userElement = (Element) userList.get(i) ;
                if (deptid.equals(userElement.getChildTextTrim("deptid")))
                {
                    deptHtmlTree.append("<li class='clsItemsShow1' id='i" + userElement.getChildTextTrim("deptid") + userElement.getChildTextTrim("userid") + "'><input type='checkbox' name='targetuser' value='" + userElement.getChildTextTrim("userid") + "'>" + userElement.getChildTextTrim("username") + "</li>\n") ;
                }
            }
            deptHtmlTree.append("</ul>\n") ;
        }
        else
        {
            deptHtmlTree.append("<li class='clsItemsShow1' id='i" + deptTree.getChildTextTrim("deptid") + "'>" +deptTree.getChildTextTrim("deptname") + "</li>\n") ;
        }
        return deptHtmlTree ;
    }
    public static List getAllUser()
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "userrole", "getAllUser") ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        List userList = repXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record") ;
        return userList ;
    }

    private static StringBuffer getDeptIds(List deptList)
    {
        StringBuffer result = new StringBuffer("") ;
        if (deptList != null)
        {
            for (int i = 0 ; i < deptList.size() ; i++)
            {
                Element currentDept = (Element) deptList.get(i) ;
                result.append(currentDept.getChildTextTrim("deptid")) ;
                result.append(",") ;
                result.append(getDeptIds(currentDept.getChildren("Record"))) ;
            }
        }
        return result ;
    }

    /**
     * 获取所有的部门列表（返回为HTML代码）
     * @return String
     */
    public static String getHtmlTree()
    {
        return getHtmlTree( (String)null) ;
    }

    /**
     *根据传入部门ID，返回此部门下的部门列表（返回为HTML代码）
     * @param deptid（当前登陆用户所属部门ID）
     * @return
     */
    public static String getHtmlTree(String deptid)
    {
        ArrayList deptTree = getDeptTree(deptid) ;
        StringBuffer deptHtmlTree = getHtmlTree(deptTree, null) ;
        return "<ul>\n" + deptHtmlTree.toString() + "\n" + "</ul>\n" ;
    }

    public static String getHtmlTreeSelected(String selectedDeptid)
    {
        ArrayList deptTree = getDeptTree(null) ;
        StringBuffer deptHtmlTree = getHtmlTree(deptTree, selectedDeptid) ;
        return "<ul>\n" + deptHtmlTree.toString() + "\n" + "</ul>\n" ;
    }

    private static StringBuffer getHtmlTree(ArrayList deptTree, String selectedDeptid)
    {
        StringBuffer result = new StringBuffer("") ;
        if ( (deptTree != null) && (deptTree.size() > 0))
        {
            for (int i = 0 ; i < deptTree.size() ; i++)
            {
                Element currentNode = (Element) deptTree.get(i) ;
                result.append(getHtmlTree(currentNode, selectedDeptid)) ;
            }
        }
        return result ;
    }

    private static StringBuffer getHtmlTree(Element deptTree, String selectedDeptid)
    {
        StringBuffer deptHtmlTree = new StringBuffer("") ;
        List childList = deptTree.getChildren("Record") ;
        if ( (childList != null) && (childList.size() > 0))
        {
            String deptid = deptTree.getChildTextTrim("deptid") ;
            if (deptid.equals(selectedDeptid))
            {
                deptHtmlTree.append("<li class='clsItemShow' id='i" + deptTree.getChildTextTrim("deptid") + "' onClick='OnItemClick();'><a href='#' onclick=\"setDeptValue('" + deptTree.getChildTextTrim("deptid") + "','" + deptTree.getChildTextTrim("deptcode") + "','" +
                                    deptTree.getChildTextTrim("pdeptid") + "','" + deptTree.getChildTextTrim("level") + "','" + deptTree.getChildTextTrim("deptname") + "','" + deptTree.getChildTextTrim("linkman") + "','" + deptTree.getChildTextTrim("phone") + "','" +
                                    deptTree.getChildTextTrim("remark") + "');\"><font color='red'><b>" + deptTree.getChildTextTrim("deptname") + "</b></font></a></li>\n") ;
            }
            else
            {
                deptHtmlTree.append("<li class='clsItemShow' id='i" + deptTree.getChildTextTrim("deptid") + "' onClick='OnItemClick();'><a href='#' onclick=\"setDeptValue('" + deptTree.getChildTextTrim("deptid") + "','" + deptTree.getChildTextTrim("deptcode") + "','" +
                                    deptTree.getChildTextTrim("pdeptid") + "','" + deptTree.getChildTextTrim("level") + "','" + deptTree.getChildTextTrim("deptname") + "','" + deptTree.getChildTextTrim("linkman") + "','" + deptTree.getChildTextTrim("phone") + "','" +
                                    deptTree.getChildTextTrim("remark") + "');\">" + deptTree.getChildTextTrim("deptname") + "</a></li>\n") ;
            }
            deptHtmlTree.append("<ul class='clsItemShow' id='i" + deptTree.getChildTextTrim("deptid") + "u'>\n") ;
            for (int i = 0 ; i < childList.size() ; i++)
            {
                Element currentChild = (Element) childList.get(i) ;
                deptHtmlTree.append(getHtmlTree(currentChild, selectedDeptid)) ;
            }
            deptHtmlTree.append("</ul>\n") ;
        }
        else
        {
            String deptid = deptTree.getChildTextTrim("deptid") ;
            if (deptid.equals(selectedDeptid))
            {
                deptHtmlTree.append("<li class='clsItemsShow1' id='i" + deptTree.getChildTextTrim("deptid") + "'><a href='#' onclick=\"setDeptValue('" + deptTree.getChildTextTrim("deptid") + "','" + deptTree.getChildTextTrim("deptcode") + "','" + deptTree.getChildTextTrim("pdeptid") + "','" +
                                    deptTree.getChildTextTrim("level") + "','" + deptTree.getChildTextTrim("deptname") + "','" + deptTree.getChildTextTrim("linkman") + "','" + deptTree.getChildTextTrim("phone") + "','" + deptTree.getChildTextTrim("remark") + "');\"><font color='red'><b>" +
                                    deptTree.getChildTextTrim("deptname") + "</b></font></a></li>\n") ;
            }
            else
            {
                deptHtmlTree.append("<li class='clsItemsShow1' id='i" + deptTree.getChildTextTrim("deptid") + "'><a href='#' onclick=\"setDeptValue('" + deptTree.getChildTextTrim("deptid") + "','" + deptTree.getChildTextTrim("deptcode") + "','" + deptTree.getChildTextTrim("pdeptid") + "','" +
                                    deptTree.getChildTextTrim("level") + "','" + deptTree.getChildTextTrim("deptname") + "','" + deptTree.getChildTextTrim("linkman") + "','" + deptTree.getChildTextTrim("phone") + "','" + deptTree.getChildTextTrim("remark") + "');\">" +
                                    deptTree.getChildTextTrim("deptname") + "</a></li>\n") ;
            }
        }
        return deptHtmlTree ;
    }

    /**
     * 获取所有的部门列表（返回XML对象Element的数组）
     * @return String
     */
    public static ArrayList getDeptTree()
    {
        return getDeptTree( (String)null) ;
    }

    public static String getDeptOptions(String deptid)
    {
        StringBuffer options = new StringBuffer("") ;
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "dept", "getDeptList") ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        List deptList = repXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record") ;
        for (int i = 0 ; i < deptList.size() ; i++)
        {
            String selected = null ;
            Element deptRecord = (Element) deptList.get(i) ;
            if (deptRecord.getChildTextTrim("deptid").equals(deptid))
            {
                selected = "selected" ;
            }
            else
            {
                selected = "" ;
            }
            options.append("<option value='" + deptRecord.getChildTextTrim("deptid") + "' " + selected + ">" + deptRecord.getChildTextTrim("deptname") + "</option>") ;
        }
        return options.toString() ;
    }

    /**
     *根据传入部门ID，返回此部门下的部门列表（返回XML对象Element的数组）
     * @param deptid（当前登陆用户所属部门ID）
     * @return
     */
    public static ArrayList getDeptTree(String deptid)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "dept", "getDeptList") ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        List deptList = repXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record") ;
        return getDeptTree(deptid, deptList) ;
    }

    /**
     * 修改日志：
     * 2004-09-15    qiumh    修改原方法，避免n次遍历
     *
     */
    /**
     * 根据传入部门ID，返回此部门下的部门列表
     * @param deptid String 部门ID
     * @param deptList List 所有部门列表
     * @return ArrayList
     */
    private static ArrayList getDeptTree(String deptid, List deptList)
    {
        ArrayList rootList = new ArrayList();
        HashMap deptMap = new HashMap();
        ArrayList keys = new ArrayList();

        if(null != deptList)
        {
            for(int i=0; i<deptList.size(); i++)
            {
                Element currentDept = (Element)deptList.get(i);
                String pdeptid = currentDept.getChildTextTrim("pdeptid");
                String thisdeptid = currentDept.getChildTextTrim("deptid");

                Element cdept = (Element)currentDept.clone();
                deptMap.put(thisdeptid,cdept);
                if(null == pdeptid || "".equals(pdeptid) ||
                   pdeptid.equals(thisdeptid))
                {
                    rootList.add(cdept);
                }
                else
                {
                    keys.add(thisdeptid);
                }
            }
            for(int i=0; i<keys.size();i++)
            {
                String cdeptid = (String)keys.get(i);
                Element cdept = (Element)deptMap.get(cdeptid);
                String pdeptid = cdept.getChildText("pdeptid");
                Element pdept = (Element)deptMap.get(pdeptid);
                pdept.addContent(cdept);
            }
        }

        return rootList;
    }

    public static Document getDeptInfo(String deptid)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "dept", "getDeptDetail") ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent(new Element("deptid").setText(deptid));
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        return repXml;

    }

    public static Document getRootDeptDetail()
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "dept", "getRootDeptDetail") ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        return repXml;

    }

    private static ArrayList getCurrentDeptList(String deptid, List deptList)
    {
        ArrayList rootList = new ArrayList();
        HashMap deptMap = new HashMap();
        ArrayList keys = new ArrayList();
        if(null != deptList)
        {
            for(int i=0; i<deptList.size(); i++)
            {
                Element currentDept = (Element)deptList.get(i);
                String thisdeptid = currentDept.getChildTextTrim("deptid");
                Element cdept = (Element)currentDept.clone();
                deptMap.put(thisdeptid,cdept);
                if(deptid.equals(thisdeptid))
                {
                    rootList.add(cdept);
                }
                else
                {
                    keys.add(thisdeptid);
                }
            }
            for(int i=0; i<keys.size();i++)
            {
                String cdeptid = (String)keys.get(i);
                Element cdept = (Element)deptMap.get(cdeptid);
                String pdeptid = cdept.getChildText("pdeptid");
                Element pdept = (Element)deptMap.get(pdeptid);
                if(!cdeptid.equals(pdeptid))
                     pdept.addContent(cdept);
            }
        }
        return rootList;
    }

    private static List getCurrentDeptList(String deptid) {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "dept", "getDeptList") ;
        Document repXml = SwitchCenter.doPost(xmlDoc);
        List deptList = repXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
        return getCurrentDeptList(deptid, deptList);
    }

    public static String getCurrentDeptListOptions(String deptid)
    {
        List currentDeptList = getCurrentDeptList(deptid);
        return getCurrentDeptListOptions(currentDeptList,deptid).toString();
    }

    private static StringBuffer getCurrentDeptListOptions(List deptList,String deptid)
    {
        StringBuffer result = new StringBuffer("") ;
        if (deptList != null)
        {
            for (int i = 0 ; i < deptList.size() ; i++)
            {
                Element currentDept = (Element) deptList.get(i) ;
                result.append("<option value=\""+currentDept.getChildTextTrim("deptid")+"\"");
                if (currentDept.getChildText("deptid").equals(deptid)) {
                    result.append(" selected");
                }
                result.append(">");
                result.append("["+currentDept.getChildTextTrim("deptcode")+"]."+currentDept.getChildTextTrim("deptname")) ;
                result.append("</option>") ;
                result.append(getCurrentDeptListOptions(currentDept.getChildren("Record"),deptid)) ;
            }
        }
        return result ;
    }

    public static Element getDeptSchool(String deptid)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("samc", "dept", "getDeptSchool") ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent(new Element("deptid").setText(deptid));
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        Element result = repXml.getRootElement().getChild("Response").getChild("Data").getChild("Record") ;
        return result ;
    }

    public static Element getDeptService(String deptid)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("samc", "dept", "getDeptService") ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent(new Element("deptid").setText(deptid));
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        Element result = repXml.getRootElement().getChild("Response").getChild("Data").getChild("Record") ;
        return result ;
    }

    private static StringBuffer getCurrentDeptIds(List deptList)
    {
        StringBuffer result = new StringBuffer("") ;
        if (deptList != null)
        {
            for (int i = 0 ; i < deptList.size() ; i++)
            {
                Element currentDept = (Element) deptList.get(i) ;
                result.append(currentDept.getChildTextTrim("deptid")) ;
                result.append(",") ;
                result.append(getCurrentDeptIds(currentDept.getChildren("Record"))) ;
            }
        }
        return result ;
    }

    public static String getCurrentDeptIds(String deptid)
    {
        List currentDeptList = getCurrentDeptList(deptid);
        return getCurrentDeptIds(currentDeptList).toString()+deptid;
    }

    private static StringBuffer getSchoolIds(List deptList)
    {
        StringBuffer result = new StringBuffer("") ;
        if (deptList != null)
        {
            for (int i = 0 ; i < deptList.size() ; i++)
            {
                Element currentDept = (Element) deptList.get(i) ;
                if (!currentDept.getChildTextTrim("depttype").equals("07"))
                {
                     result.append(currentDept.getChildTextTrim("deptid"));
                     result.append(",");
                }
                result.append(getSchoolIds(currentDept.getChildren("Record"))) ;
            }
        }
        return result ;
    }

    public static String getCurrentSchoolIds(String deptid)
    {
        List currentDeptList = getCurrentDeptList(deptid);
        return getCurrentDeptIds(currentDeptList).toString()+deptid;
    }

}
