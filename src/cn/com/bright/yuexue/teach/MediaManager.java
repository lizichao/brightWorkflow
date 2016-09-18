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
 * 微信多媒体文件管理
 * @author ZC
 *
 */
public class MediaManager {  
	private Log log4j = new Log(this.getClass().toString());
	static int filesize=0; //上传文件大小
	/**
	 * 获取多媒体下载路径
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
		      // 将mediaId作为文件名
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
		      System.out.println("文件大小："+filesize);
		      String info = String.format("下载媒体文件成功，filePath=" + FileUtil.getWebPath()+filePath);
		      System.out.println(info);
		    } catch (Exception e) {
		      filePath = null;
		      String error = String.format("下载媒体文件失败：%s", e);
		      System.out.println(error);
		    }
		    return "/"+filePath;
	}
	
	/**
	 * 添加一条视频记录
	 */
	public void addAttachment(String mediaId,String openId){
		//获取视频的路径
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
			paramList.add("");   //微信上传时默认一张图片
			paramList.add(DatetimeUtil.getNow(""));
			
			pdao.setSql(strSQL.toString());
			pdao.setBindValues(paramList);
			
			pdao.executeTransactionSql();
			pdao.commitTransaction();
			System.out.println("下载成功!");
		}
		catch (Exception e) {
			pdao.rollBack();
			e.printStackTrace();			
			log4j.logError("[视频管理-添加视频]"+e.getMessage());
		} finally {
			pdao.releaseConnection();
		}
	}
}
