package cn.com.bright.yuexue.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpWebWeiXinMessage {
	/**
	 * 发送微信消息（POST）
	 * 
	 * @param token 接收消息的服务号（此参数固定，由校园号提供）
	 * @param mobiles 要发送的手机号码列表
	 * @param msg_name 模板消息名称
	 * @param msg_title 模板消息标题
	 * @param msg_content 模板消息内容
	 * @param url 模板消息点击跳转的地址
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值，{"errcode":0,"errmsg":"发送完成！"})
	 */
    public static JSONObject sendWebWeiXinMessage(String token,String mobiles,String msg_name,String msg_title,String msg_content,String url) throws HttpException, IOException {
    	JSONObject jsonObject = null;
    	String html = "";
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod("http://www.jxhl.com/weixin/api/sendwxmessage");
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        
        // 填入各个表单域的值
        NameValuePair[] data = { new NameValuePair("token", token), 
        		new NameValuePair("mobiles", mobiles),
        		new NameValuePair("msg_name", msg_name),
        		new NameValuePair("msg_title", msg_title),
        		new NameValuePair("msg_content", msg_content),
        		new NameValuePair("url", url)};
        // 将表单的值放入postMethod中
        postMethod.setRequestBody(data);
        // 执行postMethod
        int statusCode = httpClient.executeMethod(postMethod);
        if (statusCode == HttpStatus.SC_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), postMethod.getResponseCharSet()));
            StringBuffer sb = new StringBuffer();
            int chari;
            while ((chari = in.read()) != -1) {
                sb.append((char) chari);
            }
            html = sb.toString();
            in.close();
            postMethod.releaseConnection();
        }
        jsonObject = JSONObject.fromObject(html);
        return jsonObject;
    }
    
    public static JSONObject sendWebWeiXinMessage2(String token,String mobiles,String msg_name,String msg_title,String msg_content,String url) throws HttpException, IOException {
    	JSONObject jsonObject = null;
        jsonObject = JSONObject.fromObject("{\"errcode\":0,\"errmsg\":\"发送完成！\"}");
        return jsonObject;
    }
    
    public static void main(String a[]){
    	try {
    		String token = "55";
    		String mobiles = "15012631828";//多条记录用,拼接
    		String msg_name = "作业业务提醒";
    		String msg_title = "语文第一课";//年级加学科
    		String msg_content = "完成语文第一课";//课件名
    		String url = "http://wo.szbrightcom.cn/mooc/school/school.jsp";
    		JSONObject jsonObject = HttpWebWeiXinMessage.sendWebWeiXinMessage(token,mobiles,msg_name,msg_title,msg_content,url);
    		System.out.println("jsonObject:"+jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
