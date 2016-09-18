package cn.com.bright.edu.weixin.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.weixin.util.WeixinUtil;

/**
 * ΢����Ϣģ�������
 * 
 * @author lhbo
 * @date 2015-12-30
 */
public class WeiXinTemplate {
	//private Log log4j = new Log(this.getClass().toString());
	
	private static WeiXinTemplate weixin;	
	public static WeiXinTemplate getInstance(){
		if(weixin == null){
			weixin = new WeiXinTemplate();
		}
		return weixin;
	}
	
	
	private static Map<String, String> templateCode;	
	public static Map<String, String> gettemplateCode(){
		if(templateCode == null){
			templateCode = new HashMap<String, String>();
			PlatformDao pdao = new PlatformDao();
			pdao.setSql("select template_code,template_id_short from busi_winxin_message_template");
			try {
				List li = pdao.executeQuerySql(0, -1).getChildren("Record");
				for (Object object : li) {
					Element el = (Element)object;
					templateCode.put(el.getChildText("template_code"),el.getChildText("template_id_short"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				pdao.releaseConnection();
			}
		}
		return templateCode;
	}
	
	/**
	 * ����ģ����Ϣ
	 * @param touser ������OPENID
	 * @param template_id ģ��ID
	 * @param url ����url
	 * @param requestMap ��Ϣ���� 
	 * @return JSONObject ��ʽ��{"errcode":0,"errmsg":"ok","msgid":200228332}
	 */
	public JSONObject sendTemplate(String touser,String template_id,String url,Map<String, String> requestMap){
		JSONObject jsonObject = null;
		try{
			JSONObject postObject = new JSONObject();
			postObject.put("touser", touser);
			postObject.put("template_id", template_id);
			postObject.put("url", url);
			JSONObject data = new JSONObject();
			int index = 0,endNum = requestMap.size();
			for (Map.Entry<String, String> entry : requestMap.entrySet()) {
				index++;
				String value = entry.getKey();
				String color = entry.getValue();
				if(StringUtil.isEmpty(color)){
					color = "#173177"; //Ĭ����ɫ
				}
				if(index == 1){
					JSONObject first = new JSONObject();
					first.put("value", value);
					first.put("color", color);
					data.put("first", first);
				} else if(index == endNum){
					JSONObject remark = new JSONObject();
					remark.put("value", value);
					remark.put("color", color);
					data.put("remark", remark);
				} else {
					JSONObject keyword = new JSONObject();
					keyword.put("value", value);
					keyword.put("color", color);
					data.put("keyword"+(index-1), keyword);
				}
		    }
			postObject.put("data", data);
			jsonObject = WeixinUtil.sendMessageTemplate(postObject);
		} catch (Exception e) {
			jsonObject = null;
		}
		return jsonObject;
	}
	
	/**
	 * ����ģ����Ϣ
	 * @param requestMap
	 * @return
	 */
	public void receiveMessage(Map<String, String> requestMap){
		PlatformDao pdao = null;
		ArrayList<String> val = new ArrayList<String>();
		String msgid = requestMap.get("MsgID");
		String status = requestMap.get("Status");
		System.out.println("ToUserName:"+requestMap.get("ToUserName")
				+ "\nMsgID:"+requestMap.get("MsgID")
				+ "\nStatus:"+requestMap.get("Status")
				+ "\nFromUserName:"+requestMap.get("FromUserName"));
		try {
			pdao = new PlatformDao();
			val.add(status);
			val.add(msgid);
			pdao.setSql("UPDATE busi_winxin_message_record SET status=?,feedback_time=SYSDATE WHERE msgid=?");
			pdao.setBindValues(val);
			pdao.executeTransactionSql();
		} catch (Exception e) {
		} finally {
			pdao.releaseConnection();
		}
	}
	
	public static void main(String a[]){
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("�ı�֪ͨ��WO����", "#173177");
		requestMap.put("��������", "#173177");
		requestMap.put("��������", "#173177");
		requestMap.put("��Ƶ�μ�", "#173177");
		requestMap.put("�뼰ʱ����", "#173177");
		JSONObject postObject = WeiXinTemplate.getInstance().sendTemplate("touserID","UZY7eNFHR2mK9NFKMg1zr3oZLZDvewRx2pJD1rwT37U","http://wo.szbrightcom.com",requestMap);
	}
}
