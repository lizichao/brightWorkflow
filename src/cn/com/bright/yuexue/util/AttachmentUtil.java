package cn.com.bright.yuexue.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.XmlDocPkgUtil;

/**
 * <p>Title:附件操作</p>
 * <p>Description: 附件操作</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 * 
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/09/10       1.0          build this moudle </p>
 *     
 */
public class AttachmentUtil {
    /**
     * 移动文件
     * @param pdao
     * @param fileElement
     * @param destFile
     * @return attachment_id
     */
	public static String moveFile(PlatformDao pdao,Element fileElement,String destFile)throws Exception{
		if (fileElement==null){
			return null;
		}
		else{
			Element attachmentRec = ConfigDocument.createRecordElement("yuexue","learn_attachment");
			XmlDocPkgUtil.copyValues(fileElement, attachmentRec, 0 , true);				
			
		    String srcFile =  FileUtil.getPhysicalPath(fileElement.getText());	
		    String desFileName = FileUtil.getFileName(srcFile);
		    //每年每月创建一个文件夹
		    String currDate = DatetimeUtil.getCurrentDate();			    
		    String desPath = "upload/"+destFile+"/"+currDate.substring(0, 4)+"/"+currDate.substring(5, 7)+"/";
		    

		    FileUtil.createDirs(FileUtil.getWebPath()+desPath, true);			    
			FileUtil.moveFile(new File(srcFile), new File(FileUtil.getWebPath()+desPath+desFileName));
			FileUtil.deleteFile(srcFile);
			
		    XmlDocPkgUtil.setChildText(attachmentRec, "file_path", "/"+desPath+desFileName);
			XmlDocPkgUtil.setChildText(attachmentRec, "file_name", fileElement.getAttributeValue("name"));
			XmlDocPkgUtil.setChildText(attachmentRec, "file_size", fileElement.getAttributeValue("size"));
			XmlDocPkgUtil.setChildText(attachmentRec, "file_type", fileElement.getAttributeValue("ext").toLowerCase());
			
			return pdao.insertOneRecordSeqPk(attachmentRec).toString();
		}
	}
	/**
	 * 
	 * @param fileElement
	 * @param destFile
	 * @return filePath
	 * @throws Exception
	 */
	public static String moveFile(Element fileElement,String destFile)throws Exception{
		if (fileElement==null){
			return null;
		}
		else{
		    String srcFile =  FileUtil.getPhysicalPath(fileElement.getText());	
		    String desFileName = FileUtil.getFileName(srcFile);
		    //每年每月创建一个文件夹
		    String currDate = DatetimeUtil.getCurrentDate();			    
		    String desPath = "upload/"+destFile+"/"+currDate.substring(0, 4)+"/"+currDate.substring(5, 7)+"/";
		    

		    FileUtil.createDirs(FileUtil.getWebPath()+desPath, true);			    
			FileUtil.moveFile(new File(srcFile), new File(FileUtil.getWebPath()+desPath+desFileName));
			FileUtil.deleteFile(srcFile);
			return "/"+desPath+desFileName;
		}
	}
	/**
	 * 替换文件
	 * @param pdao
	 * @param fileElement
	 * @param destFile
	 * @param attachment_id
	 * @throws Exception
	 */
	public static void repalceFile(PlatformDao pdao,Element fileElement,String destFile,String attachment_id,String username)throws Exception{
		if (fileElement==null){
			return;
		}
		else{
			Element attachmentRec = ConfigDocument.createRecordElement("yuexue","learn_attachment");
			XmlDocPkgUtil.copyValues(fileElement, attachmentRec, 0 , true);				
			
		    String srcFile =  FileUtil.getPhysicalPath(fileElement.getText());	
		    String desFileName = FileUtil.getFileName(srcFile);
		    //每年每月创建一个文件夹
		    String currDate = DatetimeUtil.getCurrentDate();			    
		    String desPath = "upload/"+destFile+"/"+currDate.substring(0, 4)+"/"+currDate.substring(5, 7)+"/";
		    

		    FileUtil.createDirs(FileUtil.getWebPath()+desPath, true);			    
			FileUtil.moveFile(new File(srcFile), new File(FileUtil.getWebPath()+desPath+desFileName));
			FileUtil.deleteFile(srcFile);
			
			XmlDocPkgUtil.setChildText(attachmentRec, "attachment_id", attachment_id);
			XmlDocPkgUtil.setChildText(attachmentRec, "trans_status", "10");
		    XmlDocPkgUtil.setChildText(attachmentRec, "file_path", "/"+desPath+desFileName);
			XmlDocPkgUtil.setChildText(attachmentRec, "file_name", fileElement.getAttributeValue("name"));
			XmlDocPkgUtil.setChildText(attachmentRec, "file_size", fileElement.getAttributeValue("size"));
			XmlDocPkgUtil.setChildText(attachmentRec, "file_type", fileElement.getAttributeValue("ext").toLowerCase());
			XmlDocPkgUtil.setChildText(attachmentRec, "file_type", fileElement.getAttributeValue("ext").toLowerCase());
			XmlDocPkgUtil.setChildText(attachmentRec, "modify_by", username);
			XmlDocPkgUtil.setChildText(attachmentRec, "modify_date", DatetimeUtil.getNow(""));
			
			pdao.updateOneRecord(attachmentRec);
		}		
	}
	/**
	 * 取文件内容
	 * @param fileName
	 * @return
	 */
	public static String getFileContent(String filePath) throws Exception{
		StringBuffer result = new StringBuffer();
		 try {
			 
			 String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
               	 result.append(lineTxt);
                }
                read.close();
		     }else{
		         System.out.println("找不到指定的文件");
		     }
	     } catch (Exception e) {
	         throw e;
	    	 //System.out.println("读取文件内容出错");
	         //e.printStackTrace();
	     }		 
		return result.toString();		
	}
	
}
