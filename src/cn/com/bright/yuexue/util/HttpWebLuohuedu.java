package cn.com.bright.yuexue.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Document;

public class HttpWebLuohuedu {
	public static String[] getUserInfo(String id,String userid,String passwd_md5_1){
		String [] strArray = new String[4];
    	try {
    		String xmlStr = HttpWebLuohuedu.getPostResponse("http://www.luohuedu.net/user_WebService.asmx/checkuser",id,userid,passwd_md5_1);
    		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		Document doc = builder.parse(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));    		
    		strArray[0] = doc.getElementsByTagName("userid").item(0).getFirstChild().getNodeValue();
    		strArray[1] = doc.getElementsByTagName("loginid").item(0).getFirstChild().getNodeValue();
    		strArray[2] = doc.getElementsByTagName("sex").item(0).getFirstChild().getNodeValue();
    		strArray[3] = doc.getElementsByTagName("schoolcode").item(0).getFirstChild().getNodeValue();
		} catch (Exception e) {
			return null;
		}
		return strArray;
	}
	
    public static String getPostResponse(String url,String id,String userid,String passwd_md5_1) throws HttpException, IOException {
        String html = "";
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        // 填入各个表单域的值
        NameValuePair[] data = { new NameValuePair("id", id), 
        		new NameValuePair("userid", userid),
        		new NameValuePair("passwd_md5_1", passwd_md5_1)};
        // 将表单的值放入postMethod中
        postMethod.setRequestBody(data);
        // 执行postMethod
        int statusCode = httpClient.executeMethod(postMethod);
        if (statusCode == HttpStatus.SC_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), postMethod
                    .getResponseCharSet()));
            StringBuffer sb = new StringBuffer();
            int chari;
            while ((chari = in.read()) != -1) {
                sb.append((char) chari);
            }
            html = sb.toString();
            in.close();
            postMethod.releaseConnection();
        }
        return html;
    }
}
