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
 * 公众平台通用接口工具类
 * 
 * @author lhbo
 * @date 2014-01-09
 */
public class WeixinUtil {
	private static String appId = "wx88d367e78b555d8c"; // 第三方用户唯一凭证	
	private static String appSecret = "3a9b3b20e5c7602ba574425c5e4c6226"; // 第三方用户唯一凭证密钥
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
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
	
	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * 获取access_token
	 * 
	 * @param appid 凭证
	 * @param appsecret 密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
				System.out.println("获取token失败 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}
	
	/**
	 * 获取access_token
	 * @return
	 */
	public static AccessToken getAccessToken() {
		AccessToken accessToken = null;
		String requestUrl = access_token_url.replace("APPID", appId).replace("APPSECRET", appSecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
				System.out.println("获取token失败 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}
	
	// 菜单创建（POST） 限100（次/天）
	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * 创建菜单
	 * 
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;
		
		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				System.out.println("创建菜单失败 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	// 删除菜单（GET） 限100（次/天）
	public static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	/**
	 * 删除菜单
	 * 
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int deleteMenu(String accessToken) {
		int result = 0;
		
		// 拼装删除菜单的url
		String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);
		// 调用接口删除菜单
		JSONObject jsonObject = httpRequest(url, "GET", null);
		
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				System.out.println("删除菜单失败 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	// 发送客服消息（POST）
	public static String send_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	/**
	 * 发送客服消息
	 * 
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int sendMessage(String accessToken) {
		int result = 0;
		
		// 拼装发送客服消息的url
		String url = send_url.replace("ACCESS_TOKEN", accessToken);
		// 调用接口发送客服消息
		JSONObject jsonObject = httpRequest(url, "POST", null);
		
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				System.out.println("发送客服消息 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	// 获取所有分组（GET）
	public static String groups_url = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";

	/**
	 * 获取所有分组
	 * 
	 * @param accessToken 有效的access_token
	 * @return Groups[]
	 */
	public static Group[] getAllGroups(String accessToken) {
		// 拼装发送客服消息的url
		String url = groups_url.replace("ACCESS_TOKEN", accessToken);
		// 调用接口发送客服消息
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
	
	// 查询用户所在分组（POST）
	public static String user_group_url = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";

	/**
	 * 查询用户所在分组
	 * 
	 * @param accessToken 有效的access_token
	 * @param openid 用户微信ID
	 * @return -1表示失败，其他值表示用户组编号
	 */
	public static int getUserGroup(String accessToken, String openid) {
		int result = -1;
		
		// 拼装发送客服消息的url
		String url = user_group_url.replace("ACCESS_TOKEN", accessToken);
		// 将Map集合转换成json字符串
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("openid", openid);
		String jsonUser = JSONObject.fromObject(requestMap).toString();
		
		// 调用接口发送客服消息
		JSONObject jsonObject = httpRequest(url, "POST", jsonUser);
		
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				System.out.println("查询用户所在分组 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			} else {
				result = jsonObject.getInt("groupid");
			}
		}
		return result;
	}
	
	// 移动用户分组（POST）
	public static String move_user_url = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";

	/**
	 * 移动用户分组
	 * 
	 * @param openid 用户微信ID
	 * @param to_groupid 要移动到的用户组编号
	 * @return 0表示成功，其他值表示失败
	 */
	public static int moveUserGroup(String openid, int to_groupid) {
		int result = 0;
		
		AccessToken at = WeixinUtil.getAccessToken();
		if (null == at) {
			return -1;
		}
		
		// 拼装发送客服消息的url
		String url = move_user_url.replace("ACCESS_TOKEN", at.getToken());
		// 将Map集合转换成json字符串
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("openid", openid);
		requestMap.put("to_groupid", to_groupid+"");
		String jsonUser = JSONObject.fromObject(requestMap).toString();
		
		// 调用接口移动用户分组
		JSONObject jsonObject = httpRequest(url, "POST", jsonUser);
		
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				Log log4j = new Log(WeixinUtil.class.toString());
				log4j.logError("[移动用户分组]openid=" + openid + ";" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	//下载多媒体文件
	public final static String video_url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	
	/**
	 * 下载多媒体文件
	 * @param mediaId
	 * 
	 * @return
	 */
	public static String getMediaURL(String mediaId) {
		AccessToken at = WeixinUtil.getAccessToken();
		if (null == at) {
			return null;
		}
	    // 拼接请求地址
		String requestUrl = video_url.replace("ACCESS_TOKEN", at.getToken()).replace("MEDIA_ID", mediaId);
	    return requestUrl;
	}
	
	// 获取OAuth2.0鉴权的 openid（GET）
	public static String oauth_base_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	/**
	 * 获取OAuth2.0鉴权的 JSONObject
	 * 
	 * @param code 授权页面同意授权，获取code
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
	 * 获取OAuth2.0鉴权的 openid
	 * 
	 * @param code 授权页面同意授权，获取code
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
	
	// 获取OAuth2.0鉴权的 userinfo（GET）
	public static String oauth_userinfo_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	/**
	 * 获取OAuth2.0鉴权的 JSONObject
	 * 
	 * @param code 授权页面同意授权，获取code
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
	
	// JS-SDK使用权限签名算法 jsapi_ticket （GET）
	public static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	/**
	 * JS-SDK使用权限签名算法 jsapi_ticket
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
	 * JS-SDK 签名 signature
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
	
	
	// 发送模板消息
	public static String message_template_send_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	/**
	 * 发送模板消息
	 * 
	 * @param code 授权页面同意授权，获取code
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


