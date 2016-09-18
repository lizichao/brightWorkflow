package cn.com.bright.edu.weixin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {   
	private static String s = null;
	private static MessageDigest md = null;
    public static String getMD5(String source) {   
	try {
			md = MessageDigest.getInstance("MD5");
	 } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 md.update(source.getBytes());
    	 byte[] encodedPassword = md.digest();

    	   StringBuilder sb = new StringBuilder();
    	   for (int i = 0; i < encodedPassword.length; i++) {
    	    if ((encodedPassword[i] & 0xff) < 0x10) {
    	     sb.append("0");
    	    }

    	    sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
    	   }
        return s=sb.toString();   
    }   
    public static void main(String args[]){
    	String ss= MD5.getMD5("GYQA");
    	System.out.println(ss);
    	String kk= MD5.JM(ss);
    	System.out.println(kk);
    	
    }
    
    
    public static String JM(String inStr) {    
    	  char[] a = inStr.toCharArray();    
    	  for (int i = 0; i < a.length; i++) {    
    	   a[i] = (char) (a[i] ^ 't');    
    	  }    
    	  String k = new String(a);    
    	  return k;    
    	 }    
}  

