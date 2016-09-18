package cn.com.bright.edu.weixin.core;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.weixin.main.GroupManager;
import cn.com.bright.edu.weixin.util.WeixinUtil;

/**
 * 微信账户管理核心类
 * 
 * @author lhbo
 * @date 2014-04-19
 */
public class WeiXinAccount {
	private Log log4j = new Log(this.getClass().toString());
	
	private static WeiXinAccount weixin;	
	public static WeiXinAccount getInstance(){
		if(weixin == null){
			weixin = new WeiXinAccount();
		}
		return weixin;
	}
	
	/**
	 * 关注微信
	 * @param userName
	 * @return
	 */
	public void subscribe(String userName){
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		try {
			pdao = new PlatformDao();
			val.add(userName);
			pdao.setSql("INSERT INTO busi_winxin_subscribe(winxincode, subscribedate) VALUES (?,SYSDATE)");
			pdao.setBindValues(val);
			pdao.executeTransactionSql();
		} catch (Exception e) {
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * 取消关注微信
	 * @param userName
	 * @return
	 */
	public void unsubscribe(String userName){
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		try {
			pdao = new PlatformDao();
			val.add(userName);
			pdao.setSql("UPDATE busi_winxin_subscribe SET unsubscribedate=SYSDATE WHERE unsubscribedate IS NULL AND winxincode=?");
			pdao.setBindValues(val);
			pdao.executeTransactionSql();
		} catch (Exception e) {
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * 定期调整用户所在组
	 * @param userName
	 * @return
	 */
	public void adjustmentUserGroup(){
		String winxincode = "",usertype = "",currgroup = "";
		int groupid = -1 ,result = -1;
		
		PlatformDao pdao = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t1.winxincode,t1.currgroup,decode(t2.usertype,'00','儿童','01','民办','02','教师') AS usertype ");
		sql.append(" FROM busi_winxin_subscribe t1,(SELECT * FROM (SELECT t.winxincode,t.usertype,ROW_NUMBER() OVER(PARTITION BY winxincode ORDER BY createdate DESC) RN FROM busi_winxin_user t WHERE t.status = 'Y') WHERE RN =1) t2");
		sql.append(" WHERE t1.winxincode = t2.winxincode AND t1.unsubscribedate IS NULL");
		try {
			pdao = new PlatformDao();
			pdao.setSql(sql.toString());
			Element el = pdao.executeQuerySql(0, -1);
			List ls = el.getChildren("Record");
			for(int i=0;i<ls.size();i++){
				Element info = (Element)ls.get(i);
				winxincode = info.getChildTextTrim("winxincode");
				usertype = info.getChildTextTrim("usertype");
				currgroup = info.getChildTextTrim("currgroup");
				groupid = GroupManager.getInstance().getGroupIdByName(usertype);
				if(groupid == -1){
					continue;
				}
				if(StringUtil.isNotEmpty(currgroup) && currgroup.equals(groupid+"")){
					continue;
				}
				result = WeixinUtil.moveUserGroup(winxincode,groupid);
				if(result == 0){
					updateUserGroup(winxincode,groupid);
				}
			}
		} catch (Exception e) {
			log4j.logError("[定期调整用户所在组]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}
	
	/**
	 * 修改数据库中的用户所在组
	 * @param userName 用户微信编号
	 * @param currgroup 当前用户组
	 * @return
	 */
	public void updateUserGroup(String userName,int currgroup){
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		try {
			pdao = new PlatformDao();
			val.add(currgroup+"");
			val.add(userName);
			pdao.setSql("UPDATE busi_winxin_subscribe SET currgroup=? WHERE unsubscribedate IS NULL AND winxincode=?");
			pdao.setBindValues(val);
			pdao.executeTransactionSql();
		} catch (Exception e) {
			log4j.logError("[修改数据库中的用户所在组]" + e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}
}
