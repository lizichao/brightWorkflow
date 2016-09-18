package cn.com.bright.yuexue.teach;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.id.UUIDHexGenerator;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.com.bright.edu.weixin.util.WeixinUtil;
  
/**
 * ΢�Ŷ�ý���ļ�����
 * @author ZC
 *
 */
public class MediaManager {  
	private Log log4j = new Log(this.getClass().toString());
	static int filesize=0; //�ϴ��ļ���С
	/**
	 * ��ȡ��ý������·��
	 * @param mediaId
	 * @return
	 */
	public static String downLoadVideo(String mediaId) {
		String filePath="";
	    try {
	    	String requestUrl = WeixinUtil.getMediaURL(mediaId);
		      URL url = new URL(requestUrl);
		      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		      conn.setDoInput(true);
		      conn.setRequestMethod("GET");
		      filesize=conn.getContentLength();
		      
		      System.out.println();
		      // ��mediaId��Ϊ�ļ���
		      filePath = "upload/weixin/"+ mediaId+".mp4";
		      
		      BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		      File file= new File(FileUtil.getWebPath()+filePath);
		      FileOutputStream fos = new FileOutputStream(file);

		      byte[] buf = new byte[8096];
		      int size = 0;
		      while ((size = bis.read(buf)) != -1)
		        fos.write(buf, 0, size);
		      
		      fos.close();
		      bis.close();

		      conn.disconnect();
		      System.out.println("�ļ���С��"+filesize);
		      String info = String.format("����ý���ļ��ɹ���filePath=" + FileUtil.getWebPath()+filePath);
		      System.out.println(info);
		    } catch (Exception e) {
		      filePath = null;
		      String error = String.format("����ý���ļ�ʧ�ܣ�%s", e);
		      System.out.println(error);
		    }
		    return "/"+filePath;
	}
	
	/**
	 * ���һ����Ƶ��¼
	 */
	public void addAttachment(String mediaId,String openId){
		//��ȡ��Ƶ��·��
		String videoUrl=downLoadVideo(mediaId);
		PlatformDao pdao = new PlatformDao();
		try {		
			pdao.beginTransaction();
			ArrayList<Object> paramList = new ArrayList<Object>();
			
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("INSERT INTO learn_attachment (attachment_id,userid,file_name,file_type,file_size,file_path,create_by,cover_path,create_date,valid) VALUES (?,?,?,?,?,?,?,?,?,'Y')");
			
			paramList.add(new UUIDHexGenerator().generate(null));
			paramList.add(openId);	
			paramList.add(mediaId);
			paramList.add("mp4");
			paramList.add(filesize);
			paramList.add(videoUrl);
			paramList.add(openId);
			paramList.add("");   //΢���ϴ�ʱĬ��һ��ͼƬ
			paramList.add(DatetimeUtil.getNow(""));
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			
			pdao.executeTransactionSql();
			pdao.commitTransaction();
			System.out.println("���سɹ�!");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[��Ƶ����-�����Ƶ]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}
}
