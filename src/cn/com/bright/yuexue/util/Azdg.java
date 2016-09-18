package cn.com.bright.yuexue.util;

import java.security.MessageDigest;

public class Azdg {

	public static void main(String[] args) {
		Azdg azdg = new Azdg();
		azdg.setjiami("08661");
	}
	
	public void setjiami(String clearText) {
		Azdg azdg = new Azdg();

		String cipherText = "";// 密文

		String key = "chinagdn";// 密钥，可自定义

		cipherText = azdg.encrypt(clearText, key);

		System.out.println(clearText + "	" + cipherText);
	}
	
	public static String getJiami(String clearText) {
		Azdg azdg = new Azdg();

		String cipherText = "";// 密文

		String key = "chinagdn";// 密钥，可自定义

		cipherText = azdg.encrypt(clearText, key);
		return cipherText;
	}
	
	/**
	 * 
	 * 加密算法
	 * 
	 * */

	public String encrypt(String txt, String key) {

		String encrypt_key = "0f9cfb7a9acced8a4167ea8006ccd098";

		int ctr = 0;

		String tmp = "";

		int i;

		for (i = 0; i < txt.length(); i++) {

			ctr = (ctr == encrypt_key.length()) ? 0 : ctr;

			tmp = tmp + encrypt_key.charAt(ctr)

			+ (char) (txt.charAt(i) ^ encrypt_key.charAt(ctr));

			ctr++;

		}

		return base64_encode(key(tmp, key));

	}

	public String key(String txt, String encrypt_key) {

		encrypt_key = strMD5(encrypt_key);

		int ctr = 0;

		String tmp = "";

		for (int i = 0; i < txt.length(); i++) {

			ctr = (ctr == encrypt_key.length()) ? 0 : ctr;

			int c = txt.charAt(i) ^ encrypt_key.charAt(ctr);

			String x = "" + (char) c;

			tmp = tmp + x;

			ctr++;

		}

		return tmp;

	}

	public String base64_encode(String str) {

		return new sun.misc.BASE64Encoder().encode(str.getBytes());

	}

	public static final String strMD5(String s) {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

		'a', 'b', 'c', 'd', 'e', 'f' };

		try {

			byte[] strTemp = s.getBytes();

			MessageDigest mdTemp = MessageDigest.getInstance("MD5");

			mdTemp.update(strTemp);

			byte[] md = mdTemp.digest();

			int j = md.length;

			char str[] = new char[j * 2];

			int k = 0;

			for (int i = 0; i < j; i++) {

				byte byte0 = md[i];

				str[k++] = hexDigits[byte0 >>> 4 & 0xf];

				str[k++] = hexDigits[byte0 & 0xf];

			}

			return new String(str);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

}
