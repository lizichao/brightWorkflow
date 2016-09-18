package cn.com.bright.edu.weixin.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.weixin.pojo.AccessToken;
import cn.com.bright.edu.weixin.pojo.Group;
import cn.com.bright.edu.weixin.pojo.Menu;


/**
 * ����ƽ̨ͨ�ýӿڹ�����
 * 
 * @author lhbo
 * @date 2014-01-09
 */
public class WeixinUtil {
	private static String appId = "wx88d367e78b555d8c"; // �������û�Ψһƾ֤	
	private static String appSecret = "3a9b3b20e5c7602ba574425c5e4c6226"; // �������û�Ψһƾ֤��Կ
	
	/**
	 * ����https���󲢻�ȡ���
	 * 
	 * @param requestUrl �����ַ
	 * @param requestMethod ����ʽ��GET��POST��
	 * @param outputStr �ύ������
	 * @return JSONObject(ͨ��JSONObject.get(key)�ķ�ʽ��ȡjson���������ֵ)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// ������SSLContext�����еõ�SSLSocketFactory����
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// ��������ʽ��GET/POST��
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// ����������Ҫ�ύʱ
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// ע������ʽ����ֹ��������
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// �����ص�������ת�����ַ���
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// �ͷ���Դ
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			System.out.println("Weixin server connection timed out." + ce.getMessage());
		} catch (Exception e) {
			System.out.println("https request error:{}" + e.getMessage());
		}
		return jsonObject;
	}
	
	// ��ȡaccess_token�Ľӿڵ�ַ��GET�� ��200����/�죩
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * ��ȡaccess_token
	 * 
	 * @param appid ƾ֤
	 * @param appsecret ��Կ
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// �������ɹ�
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// ��ȡtokenʧ��
				System.out.println("��ȡtokenʧ�� errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}
	
	/**
	 * ��ȡaccess_token
	 * @return
	 */
	public static AccessToken getAccessToken() {
		AccessToken accessToken = null;
		String requestUrl = access_token_url.replace("APPID", appId).replace("APPSECRET", appSecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// �������ɹ�
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// ��ȡtokenʧ��
				System.out.println("��ȡtokenʧ�� errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}
	
	// �˵�������POST�� ��100����/�죩
	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * �����˵�
	 * 
	 * @param menu �˵�ʵ��
	 * @param accessToken ��Ч��access_token
	 * @return 0��ʾ�ɹ�������ֵ��ʾʧ��
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;
		
		// ƴװ�����˵���url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// ���˵�����ת����json�ַ���
		String jsonMenu = JSONObject.fromObject(menu).toString();
		
		// ���ýӿڴ����˵�
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				System.out.println("�����˵�ʧ�� errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	// ɾ���˵���GET�� ��100����/�죩
	public static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	/**
	 * ɾ���˵�
	 * 
	 * @param accessToken ��Ч��access_token
	 * @return 0��ʾ�ɹ�������ֵ��ʾʧ��
	 */
	public static int deleteMenu(String accessToken) {
		int result = 0;
		
		// ƴװɾ���˵���url
		String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);
		// ���ýӿ�ɾ���˵�
		JSONObject jsonObject = httpRequest(url, "GET", null);
		
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				System.out.println("ɾ���˵�ʧ�� errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	// ���Ϳͷ���Ϣ��POST��
	public static String send_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	/**
	 * ���Ϳͷ���Ϣ
	 * 
	 * @param accessToken ��Ч��access_token
	 * @return 0��ʾ�ɹ�������ֵ��ʾʧ��
	 */
	public static int sendMessage(String accessToken) {
		int result = 0;
		
		// ƴװ���Ϳͷ���Ϣ��url
		String url = send_url.replace("ACCESS_TOKEN", accessToken);
		// ���ýӿڷ��Ϳͷ���Ϣ
		JSONObject jsonObject = httpRequest(url, "POST", null);
		
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				System.out.println("���Ϳͷ���Ϣ errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	// ��ȡ���з��飨GET��
	public static String groups_url = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";

	/**
	 * ��ȡ���з���
	 * 
	 * @param accessToken ��Ч��access_token
	 * @return Groups[]
	 */
	public static Group[] getAllGroups(String accessToken) {
		// ƴװ���Ϳͷ���Ϣ��url
		String url = groups_url.replace("ACCESS_TOKEN", accessToken);
		// ���ýӿڷ��Ϳͷ���Ϣ
		JSONObject jsonObject = httpRequest(url, "GET", null);
		
		if (null != jsonObject) {
			ArrayList<Group> groups = new ArrayList<Group>();
			JSONArray array = jsonObject.getJSONArray("groups");
			for(int i=0;i<array.size();i++){  
				JSONObject jsonObj = array.getJSONObject(i);
				Group group = new Group();
				group.setName(jsonObj.getString("name"));
				group.setId(jsonObj.getInt("id"));
				group.setCount(jsonObj.getInt("count"));
				groups.add(group);
			}
			return groups.toArray(new Group[1]);
		}
		return null;
	}
	
	// ��ѯ�û����ڷ��飨POST��
	public static String user_group_url = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";

	/**
	 * ��ѯ�û����ڷ���
	 * 
	 * @param accessToken ��Ч��access_token
	 * @param openid �û�΢��ID
	 * @return -1��ʾʧ�ܣ�����ֵ��ʾ�û�����
	 */
	public static int getUserGroup(String accessToken, String openid) {
		int result = -1;
		
		// ƴװ���Ϳͷ���Ϣ��url
		String url = user_group_url.replace("ACCESS_TOKEN", accessToken);
		// ��Map����ת����json�ַ���
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("openid", openid);
		String jsonUser = JSONObject.fromObject(requestMap).toString();
		
		// ���ýӿڷ��Ϳͷ���Ϣ
		JSONObject jsonObject = httpRequest(url, "POST", jsonUser);
		
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				System.out.println("��ѯ�û����ڷ��� errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			} else {
				result = jsonObject.getInt("groupid");
			}
		}
		return result;
	}
	
	// �ƶ��û����飨POST��
	public static String move_user_url = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";

	/**
	 * �ƶ��û�����
	 * 
	 * @param openid �û�΢��ID
	 * @param to_groupid Ҫ�ƶ������û�����
	 * @return 0��ʾ�ɹ�������ֵ��ʾʧ��
	 */
	public static int moveUserGroup(String openid, int to_groupid) {
		int result = 0;
		
		AccessToken at = WeixinUtil.getAccessToken();
		if (null == at) {
			return -1;
		}
		
		// ƴװ���Ϳͷ���Ϣ��url
		String url = move_user_url.replace("ACCESS_TOKEN", at.getToken());
		// ��Map����ת����json�ַ���
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("openid", openid);
		requestMap.put("to_groupid", to_groupid+"");
		String jsonUser = JSONObject.fromObject(requestMap).toString();
		
		// ���ýӿ��ƶ��û�����
		JSONObject jsonObject = httpRequest(url, "POST", jsonUser);
		
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				Log log4j = new Log(WeixinUtil.class.toString());
				log4j.logError("[�ƶ��û�����]openid=" + openid + ";" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	//���ض�ý���ļ�
	public final static String video_url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	
	/**
	 * ���ض�ý���ļ�
	 * @param mediaId
	 * 
	 * @return
	 */
	public static String getMediaURL(String mediaId) {
		AccessToken at = WeixinUtil.getAccessToken();
		if (null == at) {
			return null;
		}
	    // ƴ�������ַ
		String requestUrl = video_url.replace("ACCESS_TOKEN", at.getToken()).replace("MEDIA_ID", mediaId);
	    return requestUrl;
	}
	
	// ��ȡOAuth2.0��Ȩ�� openid��GET��
	public static String oauth_base_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	/**
	 * ��ȡOAuth2.0��Ȩ�� JSONObject
	 * 
	 * @param code ��Ȩҳ��ͬ����Ȩ����ȡcode
	 * @return
	 */
	public static JSONObject getOAuthBaseInfoByCode(String code) {
		if(StringUtil.isEmpty(code)){
			return null;
		}
		JSONObject jsonObject = null;
		String url = oauth_base_url.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
		try {
			jsonObject = httpRequest(url, "GET", null);
		} catch (Exception e) {
		}
		return jsonObject;
	}
	
	/**
	 * ��ȡOAuth2.0��Ȩ�� openid
	 * 
	 * @param code ��Ȩҳ��ͬ����Ȩ����ȡcode
	 * @return
	 */
	public static String getOpenidByOAuth(String code) {
		if(StringUtil.isEmpty(code)){
			return null;
		}
		String openid = null;
		try {
			JSONObject jsonObject = getOAuthBaseInfoByCode(code);
			if (null != jsonObject) {
			   openid = jsonObject.getString("openid");
			}
		} catch (Exception e) {
			openid = null;
		}
		return openid;
	}
	
	// ��ȡOAuth2.0��Ȩ�� userinfo��GET��
	public static String oauth_userinfo_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	/**
	 * ��ȡOAuth2.0��Ȩ�� JSONObject
	 * 
	 * @param code ��Ȩҳ��ͬ����Ȩ����ȡcode
	 * @return
	 */
	public static JSONObject getOAuthUserInfoByCode(String code) {
		if(StringUtil.isEmpty(code)){
			return null;
		}
		String access_token = "",openid = "";
		JSONObject jsonObject = getOAuthBaseInfoByCode(code);
		if (null != jsonObject) {
			access_token = jsonObject.getString("access_token");
			openid = jsonObject.getString("openid");
			String url = oauth_userinfo_url.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
			try {
				jsonObject = httpRequest(url, "GET", null);
			} catch (Exception e) {
				jsonObject = null;
			}
		}
		return jsonObject;
	}
	
	// JS-SDKʹ��Ȩ��ǩ���㷨 jsapi_ticket ��GET��
	public static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	/**
	 * JS-SDKʹ��Ȩ��ǩ���㷨 jsapi_ticket
	 * 
	 * @return
	 */
	public static String getJsapiTicket(String accessToken) {
		String jsapi_ticket = "";
		String url = jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken);
		try {
			JSONObject jsonObject = httpRequest(url, "GET", null);
			if(jsonObject != null){
				jsapi_ticket = jsonObject.getString("ticket");
			}
		} catch (Exception e) {
			return null;
		}
		return jsapi_ticket;
	}
	
	/**
	 * JS-SDK ǩ�� signature
	 * 
	 * @return
	 */
	public static JSONObject getSignatureByUrl(String accessToken, String url){
		JSONObject jsonObject = new JSONObject();
		long timestamp = System.currentTimeMillis() / 1000;
        String noncestr = UUID.randomUUID().toString();
        String jsapi_ticket = getJsapiTicket(accessToken);
        
	    String[] tmpArr={"noncestr="+noncestr,"jsapi_ticket="+jsapi_ticket,"timestamp="+timestamp,"url="+url};
	    Arrays.sort(tmpArr);
	    String tmpStr = SignUtil.ArrayToString(tmpArr,"&");
	    String signature = SignUtil.SHA1Encode(tmpStr);
        
	    jsonObject.put("appId", appId);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("noncestr", noncestr);
        jsonObject.put("jsapi_ticket", jsapi_ticket);
        jsonObject.put("signature", signature.toLowerCase());
        return jsonObject;
	}
	
	
	// ����ģ����Ϣ
	public static String message_template_send_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	/**
	 * ����ģ����Ϣ
	 * 
	 * @param code ��Ȩҳ��ͬ����Ȩ����ȡcode
	 * @return
	 */
	public static JSONObject sendMessageTemplate(JSONObject postObject) {
		if(postObject == null || postObject.isEmpty()){
			return null;
		}
		AccessToken at = WeixinUtil.getAccessToken();
		String access_token = at.getToken();
		JSONObject jsonObject;
		String url = message_template_send_url.replace("ACCESS_TOKEN", access_token);
		try {
			jsonObject = httpRequest(url, "POST", postObject.toString());
		} catch (Exception e) {
			jsonObject = null;
		}
		return jsonObject;
	}
}


