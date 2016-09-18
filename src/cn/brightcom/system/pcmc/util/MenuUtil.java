package cn.brightcom.system.pcmc.util ;

import org.jdom.* ;
import java.util.List ;
import java.util.ArrayList ;
import java.util.Hashtable;

import cn.brightcom.jraf.util.Crypto;
import cn.brightcom.jraf.web.HttpProcesser;
import cn.brightcom.jraf.web.SwitchCenter;

import javax.servlet.http.HttpServletRequest;

/**
 * 修改日志：
 * 2004-09-14    qiumh 修改getMenuTree(List)方法，避免n*n次遍历
 *
 *
 */

/**
 *
 * <p>Title: 菜单工具类</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author 邱爱国
 * @version 1.0
 */
public class MenuUtil
{
    public MenuUtil()
    {
    }

    public static ArrayList getMenuJsArray(HttpServletRequest request,String shortname, String roleid)
    {
        ArrayList menuTree = getMenuTree(shortname, roleid) ;
        for (int i = 0 ; i < menuTree.size() ; i++)
        {
            Element currentNode = (Element) menuTree.get(i) ;
        }
        return getMenuJsArray(request,menuTree) ;
    }
    public static String getMenuJsArray(HttpServletRequest request,String shortname, String roleid,String menubar)
    {
        ArrayList menuTree = getMenuTree(shortname, roleid) ;

        return getNewMenuJsArray(request,menuTree,menubar) ;
    }

    public static List getMenuShortCuts(String subsysid,HttpServletRequest request)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "menu", "getMenuShortCuts",request) ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("subsysid")).setText(subsysid)) ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        List menuList = repXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record") ;
        return menuList ;
    }

    private static String getNewMenuJsArray(HttpServletRequest request,ArrayList menuTree,String menubar)
    {
        if (menuTree == null)
        {
            return null ;
        }
        StringBuffer menuJs = new StringBuffer();
        for (int i = 0 ; i < menuTree.size() ; i++)
        {
            Element moduleNode = (Element) menuTree.get(i);
            String menuna = moduleNode.getChildTextTrim("menuname");

            menuJs.append(getNewMenuJs(request,moduleNode,String.valueOf(i)));

            String menujsna = "m"+String.valueOf(i);
            //menubar.add('menuna',menujsna);
            menuJs.append(menubar).append(".add('").append(menuna)
                .append("',").append(menujsna).append(");\n");
        }
        return menuJs.toString();
    }

    private static String getNewMenuJs(HttpServletRequest request,Element menuEle,String idx)
    {
        String pmenu = "m"+idx;
        StringBuffer menuBuf = new StringBuffer("var ")
            .append(pmenu).append("=new alai_menu();\n");

        int menuLen = 10;
        List childList = menuEle.getChildren("Record");
        for(int i=0; i<childList.size(); i++)
        {
            Element childEle = (Element) childList.get(i);
            String menuna = childEle.getChildTextTrim("menuname");
            //加分隔线
            if("-".equals(menuna))
            {
                //pmenu.seperate();
                menuBuf.append(pmenu).append(".seperate();\n");
                continue;
            }
            int menunaLen = menuna.getBytes().length;
            if(menuLen < menunaLen)
            {
                menuLen = menunaLen;
            }

            if(0 < childEle.getChildren("Record").size())
            {
                String cidx = idx+String.valueOf(i);
                String cmenu = "m"+cidx;

                menuBuf.append(getNewMenuJs(request,childEle,cidx));
                //pmenu.add('menuna','sub',cmenu);
                menuBuf.append(pmenu).append(".add('").append(menuna)
                    .append("','sub',").append(cmenu).append(");\n");
            }
            else
            {
                String linkurl = childEle.getChildTextTrim("linkurl");
                //pmenu.add('menuna','js','popmenu("linkurl");');
                menuBuf.append(pmenu).append(".add('").append(menuna)
                    .append("','js','popmenu(\"").append(Crypto.encodeUrl(request,linkurl)).append("\");');\n");
            }
        }
        menuBuf.append(pmenu).append(".body.style.pixelWidth=").append(menuLen*6+25).append(";\n");

        return menuBuf.toString();
    }

    private static ArrayList getMenuJsArray(HttpServletRequest request,ArrayList menuTree)
    {
        ArrayList menuJsArray = new ArrayList() ;
        if (menuTree == null)
        {
            return null ;
        }
        StringBuffer menuJs = null ;
        for (int i = 0 ; i < menuTree.size() ; i++)
        {
            int moduleLen = 8 ;
            int groupLen = 10 ;
            int funcLen = 12 ;
            menuJs = new StringBuffer("") ;
            Element moduleNode = (Element) menuTree.get(i) ;
            moduleLen = moduleNode.getChildTextTrim("menuname").getBytes().length ;
            List groupList = moduleNode.getChildren("Record") ;
            menuJs.append("var MENU_ITEMS" + i + "=[") ;
            //模块菜单-begin
            menuJs.append("['" + moduleNode.getChildTextTrim("menuname")) ;
            if ( (groupList != null) && (groupList.size() > 0))
            {
                menuJs.append("<img src=\"/images/menu_jiantou.gif\" align=\"absmiddle\">") ;
            }
            menuJs.append("', null,") ;
            if ( (groupList != null) && (groupList.size() > 0))
            {
                for (int j = 0 ; j < groupList.size() ; j++)
                {
                    Element groupNode = (Element) groupList.get(j) ;
                    int igrouplen = groupNode.getChildTextTrim("menuname").getBytes().length;
                    if (igrouplen > groupLen)
                    {
                        groupLen = igrouplen ;
                    }
                    List funcList = groupNode.getChildren("Record") ;
                    //菜单组-begin
                    menuJs.append("['" + groupNode.getChildTextTrim("menuname")) ;
                    if ( (funcList != null) && (funcList.size() > 0))
                    {
                        menuJs.append("<img src=\"/images/menu_jiantou_4.gif\" align=\"absmiddle\">") ;
                        menuJs.append("',null,") ;
                    }
                    else
                    {
                        menuJs.append("','" + Crypto.encodeUrl(request,groupNode.getChildTextTrim("linkurl")) + "',") ;
                    }
                    if ( (funcList != null) && (funcList.size() > 0))
                    {
                        for (int k = 0 ; k < funcList.size() ; k++)
                        {
                            Element funcNode = (Element) funcList.get(k) ;
                            int ifunclen = funcNode.getChildTextTrim("menuname").getBytes().length;
                            if (ifunclen > funcLen)
                            {
                                funcLen = ifunclen;
                            }
                            //功能菜单-begin
                            menuJs.append("['" + funcNode.getChildTextTrim("menuname") + "','" + Crypto.encodeUrl(request,funcNode.getChildTextTrim("linkurl")) + "'],") ;
                            //功能菜单-end
                        }
                    }
                    menuJs.append("],") ;
                    //菜单组-end
                }
            }
            //模块菜单-end

            menuJs.append("],") ;
            menuJs.append("];") ;
            menuJs.append("\n") ;
            menuJs.append("var MENU_POS" + i + " = new Array();") ;
            // item sizes for different levels of menu
            menuJs.append("MENU_POS" + i + "['height'] = [22, 25, 25];") ;
            menuJs.append("MENU_POS" + i + "['width'] = [" + (moduleLen * 6 + 25) + "," + (groupLen * 6.5 + 25) + "," + (funcLen * 6 + 25) + "];") ;
            // menu block offset from the origin:
            //	for root level origin is upper left corner of the page
            //	for other levels origin is upper left corner of parent item
            menuJs.append("MENU_POS" + i + "['block_top'] = [-3, 24, 2];") ;
            menuJs.append("MENU_POS" + i + "['block_left'] = [0, 2, " + (groupLen * 6.5 + 25) + "];") ;
            // offsets between items of the same level
            menuJs.append("MENU_POS" + i + "['top'] = [0, 21, 21];") ;
            menuJs.append("MENU_POS" + i + "['left'] = [131, 0, 0];") ;
            // time in milliseconds before menu is hidden after cursor has gone out
            // of any items
            menuJs.append("MENU_POS" + i + "['hide_delay'] = [200, 200, 200];") ;
            menuJsArray.add(menuJs.toString()) ;
        }
        return menuJsArray ;
    }

    /**
     *根据传入子系统代码，返回此子系统下的菜单列表（返回为HTML代码）
     * @param shortname（子系统代码）
     * @return
     */
    public static String getHtmlTree(String shortname, String roleid)
    {
        ArrayList menuTree = getMenuTree(shortname, roleid) ;
        StringBuffer menuHtmlTree = getHtmlTree(menuTree) ;
        return "<ul>\n" + menuHtmlTree.toString() + "\n" + "</ul>\n" ;
    }

    public static String getHtmlTree(String shortname, String userid, String level)
    {
        ArrayList menuTree = getMenuTree(userid) ;
        StringBuffer menuHtmlTree = getHtmlTree(menuTree, level) ;
        return menuHtmlTree.toString();    	
    }
    
    public static String getRoleMenuHtmlTree(String userid,String roleid,String subsysid)
    {
        ArrayList menuTree = getRoleMenuTree(userid,roleid,subsysid) ;
        StringBuffer menuHtmlTree = getRoleMenuHtmlTree(menuTree) ;
        return "<ul>\n" + menuHtmlTree.toString() + "\n" + "</ul>\n" ;
    }
    public static String getShortCutMenuHtmlTree(String subsysid,String userid,String roleid)
    {
        ArrayList menuTree = getShortCutMenuTree(subsysid,userid,roleid) ;
        StringBuffer menuHtmlTree = getShortCutMenuHtmlTree(menuTree) ;
        return "<ul>\n" + menuHtmlTree.toString() + "\n" + "</ul>\n" ;
    }

    private static StringBuffer getRoleMenuHtmlTree(ArrayList menuTree)
    {
        StringBuffer result = new StringBuffer("") ;
        if ( (menuTree != null) && (menuTree.size() > 0))
        {
            for (int i = 0 ; i < menuTree.size() ; i++)
            {
                Element currentNode = (Element) menuTree.get(i) ;
                result.append(getRoleMenuHtmlTree(currentNode)) ;
            }
        }
        return result ;
    }
    private static StringBuffer getShortCutMenuHtmlTree(ArrayList menuTree)
    {
        StringBuffer result = new StringBuffer("") ;
        if ( (menuTree != null) && (menuTree.size() > 0))
        {
            for (int i = 0 ; i < menuTree.size() ; i++)
            {
                Element currentNode = (Element) menuTree.get(i) ;
                result.append(getShortCutMenuHtmlTree(currentNode)) ;
            }
        }
        return result ;
    }

    private static StringBuffer getHtmlTree(ArrayList menuTree)
    {
        StringBuffer result = new StringBuffer("") ;
        if ( (menuTree != null) && (menuTree.size() > 0))
        {
            for (int i = 0 ; i < menuTree.size() ; i++)
            {
                Element currentNode = (Element) menuTree.get(i) ;
                result.append(getHtmlTree(currentNode)) ;
            }
        }
        return result ;
    }

    //
    private static StringBuffer getHtmlTree(ArrayList menuTree, String level)
    {
        StringBuffer result = new StringBuffer("") ;
        if ( (menuTree != null) && (menuTree.size() > 0))
        {
            for (int i = 0 ; i < menuTree.size() ; i++)
            {
                Element currentNode = (Element) menuTree.get(i) ;
                result.append(getHtmlTree(currentNode, level)) ;
            }
        }
        return result ;
    }
    
    private static StringBuffer getRoleMenuHtmlTree(Element menuTree)
    {
        StringBuffer menuHtmlTree = new StringBuffer("") ;
        List childList = menuTree.getChildren("Record") ;
        if ( (childList != null) && (childList.size() > 0))
        {
            menuHtmlTree.append("<li class='clsItemShow' id='i" + menuTree.getChildTextTrim("menuid") + "' onClick='OnItemClick();'><input type='checkbox' name='menuid' pmenuid='"+ menuTree.getChildTextTrim("pmenuid") + "' value='"+menuTree.getChildTextTrim("menuid")+"' onclick='selectMenu();' "+menuTree.getChildTextTrim("selected")+">" + menuTree.getChildTextTrim("menuname") + "</li>\n") ;
            menuHtmlTree.append("<ul class='clsItemShow' id='i" + menuTree.getChildTextTrim("menuid") + "u'>\n") ;
            for (int i = 0 ; i < childList.size() ; i++)
            {
                Element currentChild = (Element) childList.get(i) ;
                menuHtmlTree.append(getRoleMenuHtmlTree(currentChild)) ;
            }
            menuHtmlTree.append("</ul>\n") ;
        }
        else
        {
            menuHtmlTree.append("<li class='clsItemsShow1' id='i" + menuTree.getChildTextTrim("menuid") + "'><input type='checkbox' name='menuid' pmenuid='"+ menuTree.getChildTextTrim("pmenuid") + "' value='"+menuTree.getChildTextTrim("menuid")+"' onclick='selectMenu();' "+menuTree.getChildTextTrim("selected")+">" + menuTree.getChildTextTrim("menuname") + "</li>\n") ;
        }
        return menuHtmlTree ;
    }
    private static StringBuffer getShortCutMenuHtmlTree(Element menuTree)
    {
        StringBuffer menuHtmlTree = new StringBuffer("") ;
        List childList = menuTree.getChildren("Record") ;
        if ( (childList != null) && (childList.size() > 0))
        {
            menuHtmlTree.append("<li class='clsItemShow' id='i" + menuTree.getChildTextTrim("menuid") + "' onClick='OnItemClick();'>" + menuTree.getChildTextTrim("menuname") + "</li>\n") ;
            menuHtmlTree.append("<ul class='clsItemShow' id='i" + menuTree.getChildTextTrim("menuid") + "u'>\n") ;
            for (int i = 0 ; i < childList.size() ; i++)
            {
                Element currentChild = (Element) childList.get(i) ;
                menuHtmlTree.append(getShortCutMenuHtmlTree(currentChild)) ;
            }
            menuHtmlTree.append("</ul>\n") ;
        }
        else
        {
            menuHtmlTree.append("<li class='clsItemsShow1' id='i" + menuTree.getChildTextTrim("menuid") + "'><input type='checkbox' name='menuid' pmenuid='"+ menuTree.getChildTextTrim("pmenuid") + "' value='"+menuTree.getChildTextTrim("menuid")+"' "+menuTree.getChildTextTrim("selected")+">" + menuTree.getChildTextTrim("menuname") + "</li>\n") ;
        }
        return menuHtmlTree ;
    }

    private static StringBuffer getHtmlTree(Element menuTree)
    {
        StringBuffer menuHtmlTree = new StringBuffer("") ;
        List childList = menuTree.getChildren("Record") ;
        if ( (childList != null) && (childList.size() > 0))
        {
            menuHtmlTree.append("<li class='clsItemShow' id='i" + menuTree.getChildTextTrim("menuid") + "' onClick='OnItemClick();'>" + menuTree.getChildTextTrim("menuname") + "</li>\n") ;
            menuHtmlTree.append("<ul class='clsItemShow' id='i" + menuTree.getChildTextTrim("menuid") + "u'>\n") ;
            for (int i = 0 ; i < childList.size() ; i++)
            {
                Element currentChild = (Element) childList.get(i) ;
                menuHtmlTree.append(getHtmlTree(currentChild)) ;
            }
            menuHtmlTree.append("</ul>\n") ;
        }
        else
        {
            menuHtmlTree.append("<li class='clsItemsShow1' id='i" + menuTree.getChildTextTrim("menuid") + "'>" + menuTree.getChildTextTrim("menuname") + "</li>\n") ;
        }
        return menuHtmlTree ;
    }

    /*
     * 
     * 
     */
    private static StringBuffer getHtmlTree(Element menuTree, String level)
    {
        StringBuffer menuHtmlTree = new StringBuffer("") ;
        List childList = menuTree.getChildren("Record") ;
        if ( (childList != null) && (childList.size() > 0))
        {
            menuHtmlTree.append("<LI   value=" + menuTree.getChildTextTrim("menuname") + ">\n") ;
            for (int i = 0 ; i < childList.size() ; i++)
            {
                Element currentChild = (Element) childList.get(i) ;
                menuHtmlTree.append(getHtmlTree(currentChild,"1")) ;
            }
            menuHtmlTree.append("</LI>\n") ;
        }
        else
        {       
        	if (level.equals("0"))
        		menuHtmlTree.append("<LI   value=" + menuTree.getChildTextTrim("menuname") + ">\n") ;	
        	else{
        		menuHtmlTree.append("<UL HREF=\"" + menuTree.getChildTextTrim("linkurl") + "\"   VALUE=\"" + menuTree.getChildTextTrim("menuname") + "\"   IMGSRC=\"/images/pcmc/func.gif\"></UL>\n") ;
        	}
        }
        return menuHtmlTree ;
    }    
    
    /**
     *根据传入子系统代码，返回此子系统下的菜单列表（返回XML对象Element的数组）
     * @param shortname（子系统代码）
     * @param userid 当前用户ID
     * @return
     */
    public static ArrayList getMenuTree(String userid)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "menu", "getMenuListByUserID") ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("userid")).setText(userid)) ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        List menuList = repXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record") ;
        return getMenuTree(menuList) ;
    }
    /**
     *根据传入子系统代码，返回此子系统下的菜单列表（返回XML对象Element的数组）
     * @param shortname（子系统代码）
     * @param roleid 当前用户角色ID
     * @return
     */
    public static ArrayList getMenuTree(String shortname, String roleid)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "menu", "getMenuList") ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("shortname")).setText(shortname)) ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("roleid")).setText(roleid)) ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        List menuList = repXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record") ;
        return getMenuTree(menuList) ;
    }

    /**
     *根据传入子系统代码及当前用户角色，返回此子系统下的菜单列表（返回XML对象Element的数组）
     * @param shortname（子系统代码）
     * @param roleid 当前用户角色ID
     * @return
     */
    private static ArrayList getRoleMenuTree(String userid,String roleid,String subsysid)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "sm_permission", "getPermissions") ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("userid")).setText(userid)) ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("roleid")).setText(roleid)) ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("subsysid")).setText(subsysid)) ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        List menuList = repXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record") ;
        return getMenuTree(menuList) ;
    }
    private static ArrayList getShortCutMenuTree(String subsysid,String userid,String roleid)
    {
        Document xmlDoc = HttpProcesser.createRequestPackage("pcmc", "menu", "getMenuShortCutTree") ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("subsysid")).setText(subsysid)) ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("userid")).setText(userid)) ;
        xmlDoc.getRootElement().getChild("Request").getChild("Data").addContent( (new Element("roleid")).setText(roleid)) ;
        Document repXml = SwitchCenter.doPost(xmlDoc) ;
        List menuList = repXml.getRootElement().getChild("Response").getChild("Data").getChildren("Record") ;
        return getMenuTree(menuList) ;
    }

    /**
     * 2004-09-14    qiumh 替换原有方法
     *
     * 组织menu为树形结构
     * @param menuList List
     * @return ArrayList
     */
    private static ArrayList getMenuTree(List menuList)
    {
        ArrayList pMenuArray = new ArrayList();
        Hashtable menuMap = new Hashtable();
        ArrayList keys = new ArrayList();

        if(null != menuList)
        {
            for(int i = 0; i < menuList.size(); i++)
            {
                Element currentMenu = (Element)menuList.get(i);
                String pmenuid = currentMenu.getChildTextTrim("pmenuid");
                String thismenuid = currentMenu.getChildTextTrim("menuid");

                Element cMenu = (Element)currentMenu.clone();
                menuMap.put(thismenuid,cMenu);
                if(null == pmenuid || "".equals(pmenuid))
                {
                    pMenuArray.add(cMenu);
                }
                else
                {
                    keys.add(thismenuid);
                }
            }
            for(int i=0; i<keys.size(); i++)
            {
                String menuid = (String)keys.get(i);
                Element cMenu = (Element)menuMap.get(menuid);
                String pmenuid = cMenu.getChildTextTrim("pmenuid");
                Element pMenu = (Element)menuMap.get(pmenuid);
                pMenu.addContent(cMenu);
            }
        }

        return pMenuArray;
    }
/*
    private static ArrayList getMenuTree(List menuList)
    {
        return getMenuTree(null,menuList);
    }

    private static ArrayList getMenuTree(String menuid, List menuList)
    {
        ArrayList menuRootList = new ArrayList() ;
        if (menuList != null)
        {
            if (menuid == null)
            {
                for (int i = 0 ; i < menuList.size() ; i++)
                {
                    Element currentMenu = (Element) menuList.get(i) ;
                    String pmenuid = currentMenu.getChildTextTrim("pmenuid") ;
                    String thismenuid = currentMenu.getChildTextTrim("menuid") ;
                    if ( (pmenuid == null) || (pmenuid.length() < 1) || (thismenuid.equals(pmenuid)))
                    {
                        Element pMenu = (Element) currentMenu.clone() ;
                        ArrayList childList = getMenuTree(thismenuid, menuList) ;
                        for (int j = 0 ; j < childList.size() ; j++)
                        {
                            pMenu.addContent( (Element) childList.get(j)) ;
                        }
                        menuRootList.add(pMenu) ;

                    }
                }
            }
            else
            {
                for (int i = 0 ; i < menuList.size() ; i++)
                {
                    Element currentMenu = (Element) menuList.get(i) ;
                    String pmenuid = currentMenu.getChildTextTrim("pmenuid") ;
                    String thismenuid = currentMenu.getChildTextTrim("menuid") ;
                    if ( (menuid != null) && (pmenuid.equals(menuid)) && (!thismenuid.equals(menuid)))
                    {
                        Element pMenu = (Element) currentMenu.clone() ;
                        ArrayList childList = getMenuTree(thismenuid, menuList) ;
                        for (int j = 0 ; j < childList.size() ; j++)
                        {
                            pMenu.addContent( (Element) childList.get(j)) ;
                        }
                        menuRootList.add(pMenu) ;
                    }
                }
            }
        }
        return menuRootList ;
    }*/

}
