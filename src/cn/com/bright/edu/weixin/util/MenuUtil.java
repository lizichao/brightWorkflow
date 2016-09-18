package cn.com.bright.edu.weixin.util;

import java.io.BufferedReader;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;

import java.net.URL;

public class MenuUtil {
    public static void createMenu(String params,String accessToken) {
        StringBuffer bufferRes = new StringBuffer();
        try {
            URL realUrl = new URL("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+ accessToken);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            
            // ���ӳ�ʱ
            conn.setConnectTimeout(25000);            
            // ��ȡ��ʱ --��������Ӧ�Ƚ���,����ʱ��
            conn.setReadTimeout(25000);            
            HttpURLConnection.setFollowRedirects(true);
            
            // ����ʽ
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0");
            conn.setRequestProperty("Referer", "https://api.weixin.qq.com/");
            conn.connect();
            
            // ��ȡURLConnection�����Ӧ�������
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            
            // �����������
            //out.write(URLEncoder.encode(params,"UTF-8"));
            out.write(params);
            out.flush();
            out.close();
            
            InputStream in = conn.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String valueString = null;
            while ((valueString=read.readLine())!=null){
            	bufferRes.append(valueString);
            }
            System.out.println(bufferRes.toString());
            in.close();	
            if (conn != null) {
            	// �ر�����
            	conn.disconnect();
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String s = "{\"button\":[{\"type\":\"click\",\"name\":\"���ո���\",\"key\":\"V1001_TODAY_MUSIC\"},{\"type\":\"click\",\"name\":\"���ּ��\",\"key\":\"V1001_TODAY_SINGER\"},{\"name\":\"�˵�\",\"sub_button\":[{\"type\":\"view\",\"name\":\"����\",\"url\":\"http://www.soso.com/\"},{\"type\":\"view\",\"name\":\"��Ƶ\",\"url\":\"http://v.qq.com/\"},{\"type\":\"click\",\"name\":\"��һ������\",\"key\":\"V1001_GOOD\"}]}]}";
        // ���Լ���token
        String accessToken = "I-R3IKTk9X5Y6VJc6QjNICCmondmn30IJUGVk-D9XCnEw83X0R88v76PaJi_irOeNnuPYdyOVWvHtOHksNA9_UqmiBZTQwk8lCFOcXNiFYrkXM3GpaPb9FjIy1GL7vNrewB0M9HOk96nYFPfZlXl9w";
        createMenu(s,accessToken);
    }
}