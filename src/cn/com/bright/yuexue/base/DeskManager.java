package cn.com.bright.yuexue.base;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Constants;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * 个人桌页面业务处理
 * @author E40
 *
 */
public class DeskManager {
private XmlDocPkgUtil xmlDocUtil = null;
    
    private static Log log4j = new Log(DeskManager.class.getName());
    
    public Document doPost(Document xmlDoc)
	{
    	xmlDocUtil = new XmlDocPkgUtil(xmlDoc);
    	String action = xmlDocUtil.getAction();
    	if("getGroupDeskList".equals(action)){
    		getGroupDeskList();
    	}
    	if("getSubSysMenu".equals(action)){
    		getSubSysMenu();
    	}
    	if("delDeskInfo".equals(action)){
    		delDeskInfo();
    	}
    	return xmlDoc;
	}
    
    /**
	 * 获取个人桌面信息(组菜单)
	 *
	 */
	public void getGroupDeskList(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String currUserid = xmlDocUtil.getSession().getChildText("userid");
		
		StringBuffer strSQL = new StringBuffer();		
		strSQL.append("SELECT * FROM pcmc_user_desktop WHERE userid=? and p_desktop_id IS null  ORDER BY show_idx asc ");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(currUserid);
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0, -1);
	    	
	    	List list = result.getChildren("Record");
	    	StringBuffer menuSQL = new StringBuffer();
	    	menuSQL.append("SELECT t.desktop_id,t.p_desktop_id,t.menu_id,t.userid,t.menu_name,t.menu_ico,t.show_idx,t2.newwin,t2.LINKURL ");
	    	menuSQL.append(" FROM pcmc_user_desktop t,pcmc_menu t2 ");
	    	menuSQL.append(" WHERE t.menu_id = t2.MENUID and t.p_desktop_id=? ");
	    	menuSQL.append(" order by t.show_idx ");
	    	for (int i = 0; i < list.size() ;i++){
	    		Element groupRec = (Element)list.get(i);
	    		String desktop_id = groupRec.getChildText("desktop_id");
	    		ArrayList<Object> menuParam = new ArrayList<Object>();
	    		menuParam.add(desktop_id);
	    		
	    		pdao.setSql(menuSQL.toString());
	    		pdao.setBindValues(menuParam);
	    		Element menuResult = pdao.executeQuerySql(0,-1);
	    		Element menuEl = new Element("MenuDesk");
	    		menuEl.addContent(menuResult);
	    		groupRec.addContent(menuEl);  		
	    	}
	    	xmlDocUtil.getResponse().addContent(result);
	    	xmlDocUtil.setResult("0");
	    	
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[个人桌面设置管理-获取分组菜单列表]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}		
	}
	
	/**
	 * 获得待分组的菜单项
	 */
	public void getSubSysMenu(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String subsysid = reqElement.getChildText("subsysid");
		String currUserid = xmlDocUtil.getSession().getChildText("userid");
		
		StringBuffer menuSQL = new StringBuffer();
		menuSQL.append("SELECT DISTINCT c.*,'' selected,(select count(*) from pcmc_menu where pmenuid=c.menuid) as isleaf FROM pcmc_menu c ");
		menuSQL.append("WHERE c.subsysid=? AND NOT EXISTS ");
		menuSQL.append("(SELECT NULL FROM pcmc_user_desktop WHERE menu_id = c.menuid AND userid=?) ");
		menuSQL.append("ORDER BY c.levels,c.pmenuid,c.orderidx,c.menuid ");
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(subsysid);
		paramList.add(currUserid);
		
	    PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(menuSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element deskResult = pdao.executeQuerySql(0, -1);
	    	xmlDocUtil.getResponse().addContent(deskResult);
	    	xmlDocUtil.setResult("0");
	    }catch (Exception e) {
			e.printStackTrace();
			log4j.logError("[个人桌面设置管理-获取待分组信息]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}	
	}
	/**
	 * 删除个人桌面设置
	 */
	public void delDeskInfo(){
		Element reqElement =  xmlDocUtil.getRequestData();
		String deskIds = reqElement.getChildText("desktop_id");//个人桌面ID
		PlatformDao pdao = new PlatformDao();
		StringBuffer strSQL = new StringBuffer();
		ArrayList<Object> paramList = new ArrayList<Object>();
		strSQL.append("SELECT t.*,(SELECT COUNT(*) FROM pcmc_user_desktop WHERE p_desktop_id=t.desktop_id) AS isleaf ");
		strSQL.append("FROM pcmc_user_desktop t WHERE t.desktop_id = ? ");
		paramList.add(deskIds);
		
		try{
			
	    	pdao.setSql(strSQL.toString());
	    	pdao.setBindValues(paramList);
	    	Element deskEl = pdao.executeQuerySql(0, -1);
	    	List list = deskEl.getChildren("Record");
	    	Element deskRecord =(Element)list.get(0);
	    	String isLeaf = deskRecord.getChildText("isleaf");
	    	String desktop_id = deskRecord.getChildText("desktop_id");
	    	StringBuffer deskSQL = new StringBuffer();
	    	deskSQL.append("delete from pcmc_user_desktop where desktop_id ='"+desktop_id+"' ");
	    	if(!"0".equals(isLeaf)){
	    		deskSQL.append(" or p_desktop_id='"+desktop_id+"' ");
	    	}
    		pdao.setSql(deskSQL.toString());
	    	pdao.executeTransactionSql();
	    	xmlDocUtil.setResult("0");
	    }catch (Exception e) {
	    	pdao.rollBack();
			e.printStackTrace();
			log4j.logError("[个人桌面设置管理-删除分组信息]"+e.getMessage());
			xmlDocUtil.setResult(Constants.SYSTEM_ERROR_CODE);
		} finally {
			pdao.releaseConnection();
		}	
	}
}
