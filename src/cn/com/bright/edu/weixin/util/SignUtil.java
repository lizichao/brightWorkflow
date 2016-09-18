package cn.com.bright.edu.weixin.util;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * 签名认证类
 * 
 * @author lhbo
 * @date 2014-01-05
 */
public class SignUtil {
	final static String TOKEN = "Lxkj_wo_2015";
	
	public static boolean checkSignature(String signature, String timestamp, String nonce){
	    String token = TOKEN;
	    String[] tmpArr={token,timestamp,nonce};
	    Arrays.sort(tmpArr);
	    String tmpStr=ArrayToString(tmpArr);
	    tmpStr=SHA1Encode(tmpStr);
	    if(tmpStr.equalsIgnoreCase(signature)){
			return true;
		}else{
			return false;
		}
	}
	
	//数组转字符串
	public static String ArrayToString(String [] arr){
		StringBuffer bf = new StringBuffer();
		for(int i = 0; i < arr.length; i++){
		  bf.append(arr[i]);
		}
		return bf.toString();
	}
	
	//数组转字符串
	public static String ArrayToString(String [] arr,String splice){
		StringBuffer bf = new StringBuffer();
		for(int i = 0; i < arr.length; i++){
		  bf.append(arr[i]);
		  if(i < arr.length -1){
			  bf.append(splice);
		  }
		}
		return bf.toString();
	}
	
	//sha1加密
	public static String SHA1Encode(String sourceString) {
		String resultString = null;
		try {
		   resultString = new String(sourceString);
		   MessageDigest md = MessageDigest.getInstance("SHA-1");
		   resultString = byte2hexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}
	
	public final static String byte2hexString(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
		    	buf.append("0");
		   	}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}
}
