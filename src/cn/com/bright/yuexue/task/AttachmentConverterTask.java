package cn.com.bright.yuexue.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import cn.brightcom.jraf.conf.BrightComConfig;
import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.ImageUtils;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.TimerTask;
import cn.com.bright.yuexue.converter.AbstractConverter;
import cn.com.bright.yuexue.converter.Pdf2Jpg;
import cn.com.bright.yuexue.converter.Video2MP4;
import cn.com.bright.yuexue.sell.preview.DocConverter;

/**
 * <p>Title:����ת������</p>
 * <p>Description: ����ת������</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��
 * 
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/08/12       1.0          build this moudle </p>
 *     
 */
public class AttachmentConverterTask extends TimerTask{
	private Log log4j = new Log(this.getClass().toString());
	private static String transType = (String) BrightComConfig.getConfiguration().getProperty("mencoder.trans_type");
	private static String openofficeport = (String) BrightComConfig.getConfiguration().getProperty("sell.openofficeport");
	public void run() {	
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" AttachmentConverterTask run ----------");
		List attachmentList = getUnconvertedAttachment();
	    for (int i=0;i<attachmentList.size();i++){	
	    	Element attachmentRec = (Element)attachmentList.get(i);
	    	String attachment_id = attachmentRec.getChildText("attachment_id");
	    	String file_type = attachmentRec.getChildText("file_type");
	    	String file_name = attachmentRec.getChildText("file_name");
	    	String file_path = attachmentRec.getChildText("file_path");	    	
	    	File docFile = new File(FileUtil.getWebPath()+file_path);
	    	
	    	if (!docFile.exists()){
	    		setAttachmentInfo(attachment_id,"80","","�ļ�������");
	    	}
	    	else{
	    		AbstractConverter converter = null;
	    		String targetType ="html";
	    		String srcPath  = FileUtil.getWebPath()+file_path;
    			String folderPath = file_path.substring(0,file_path.lastIndexOf("/")+1);
    			String fileName = FileUtil.getFileName(srcPath);
    			String fileNameNoExt = fileName.substring(0, fileName.lastIndexOf("."));
    			
    			String destPath = folderPath+fileNameNoExt+"."+targetType;
    			
    			try {    			
	                /**  office�ĵ�ͳһͨ��open office ת��pdf ��ת�ɵ�����
		    		if ("doc,docx".indexOf(file_type.toLowerCase())>-1){
		    			converter = new Word2Html();	    			
		    		}
		    		else if  ("xls,xlsx".indexOf(file_type.toLowerCase())>-1){
		    			converter = new Excel2Html();
		    		}
		    		else if  ("ppt,pptx".indexOf(file_type.toLowerCase())>-1){
		    			converter = new OfficeToHtml();
		    		}
		    		*/
	    			if ("doc,docx,xls,xlsx,ppt,pptx,txt,pdf".indexOf(file_type.toLowerCase()) > -1) {
	    				/**
	    				String pdfPath = folderPath+fileNameNoExt+".pdf";
	    				File pdfFile = new File(pdfPath);
	    				doc2pdf(docFile,pdfFile);
	    				
	    				srcPath = pdfPath;
	    				destPath = folderPath+fileNameNoExt+"/index.html";
		    			converter = new Pdf2Jpg();
		    			((Pdf2Jpg)converter).setFileName(file_name);	    				
		    			((Pdf2Jpg)converter).setUploadUserID(attachmentRec.getChildText("userid"));
		    			*/
	    				DocConverter dc= new DocConverter(srcPath);
	    				dc.conver(false);//false ȫ��ת�� true ֻת��ָ��������ҳ��
	    				destPath = "/yuexue/expand/preview.jsp?attachid="+attachment_id;
	    				setAttachmentInfo(attachment_id,"30",destPath,"");
	    			}
	    			/**
		    		else if ("pdf".equalsIgnoreCase(file_type)){
		    			converter = new Pdf2Jpg();
		    			((Pdf2Jpg)converter).setFileName(file_name);
		    			((Pdf2Jpg)converter).setPageCount(attachmentRec.getChildText("page_count"));
		    			((Pdf2Jpg)converter).setUploadUserID(attachmentRec.getChildText("userid"));
		    			String access_path = attachmentRec.getChildText("access_path");
		    			if (StringUtil.isEmpty(access_path)){
		    			    destPath = folderPath+fileNameNoExt+"/index.html";
		    			}
		    			else{//����������½�,��Ŀ¼Ϊ��һ�ε�Ŀ¼	    	    		
		        			destPath = access_path;
		    			}
		    		}
	    			*/
	    			
		    		else if ("mpg,mpeg,mp4,avi,mkv,rmvb,rm,flv,mov,wmv,mod".indexOf(file_type.toLowerCase())>-1){
		    			converter = new Video2MP4();
		    			targetType ="mp4";
		    			destPath = folderPath+fileNameNoExt+"_s."+targetType;
		    		}
		    		else if ("txt,rar,zip,js,apk".indexOf(file_type.toLowerCase())>-1){
		    			setAttachmentInfo(attachment_id,"30",file_path,"����ת��");
		    		}
		    		else if (ImageUtils.isImage(file_type)){
		    			targetType ="jpg";	
		    			destPath = folderPath+fileName+"_s."+targetType;
		    			try{
		    			   ImageUtils.createPreviewImage(srcPath);
		    			   setAttachmentInfo(attachment_id,"30","/"+destPath,"");
		    			}
		    			catch(Exception ex){
		    			   log4j.logError("[����ת��-ͼƬת��ʧ��]"+ex.getMessage());
			    		   String strMsg = ex.getMessage();
		    			   if (strMsg.length()>500){
		    				   strMsg =  strMsg.substring(0, 500);
		    			   }		    		   
			    		   setAttachmentInfo(attachment_id,"90","",strMsg);
		    			}	    			
		    		}
		    		else {
		    			setAttachmentInfo(attachment_id,"70","","�ݲ�֧��ת�����ļ�����");
		    		}
		    		if (converter != null){	    			
		    			try{
		    			   setAttachmentInfo(attachment_id,"20","","");
		    			   converter.process(srcPath, FileUtil.getWebPath()+destPath);    			       
		    			   if ("doc,docx,xls,xlsx,ppt,pptx,txt,pdf".indexOf(file_type.toLowerCase()) > -1) {
	    			    	   Pdf2Jpg pdf2jpg = (Pdf2Jpg)converter;
	    			    	   String coverPath =  "1.png_s.jpg";
	    			    	   setAttachmentInfo(attachment_id,"30",destPath,destPath.replace("index.html",coverPath),pdf2jpg.getPageCount(),""); 
	    			       }
	    			       else{
	    			    	   setAttachmentInfo(attachment_id,"30",destPath,"");
	    			       }
		    			}
		    			catch(Exception ex){
		    			   ex.printStackTrace();
		    			   log4j.logError("[����ת��-����ת��ʧ��]"+ex.getMessage());
		    			   String strMsg = ex.getMessage();
		    			   if (strMsg.length()>500){
		    				   strMsg =  strMsg.substring(0, 500);
		    			   }
		    			   setAttachmentInfo(attachment_id,"90","",strMsg);
		    			}
		    		}
    			}
    			catch (Exception e) {
    				e.printStackTrace();
    				log4j.logInfo("["+srcPath+"]ת���쳣 " + e.getMessage());
    			    String strMsg = e.getMessage();
    			    if (strMsg.length()>500){
    				   strMsg =  strMsg.substring(0, 500);
    			    }
    			    setAttachmentInfo(attachment_id,"90","",strMsg);    				
    				// return false;
    			}
	    	}
	    }
	}
	/**
	 * ���ø�����Ϣ
	 * @param attachmentId
	 * @param transStatus
	 * @param accessPath
	 * @param coverPath
	 * @param pageCount
	 * @param transError
	 */
	public void setAttachmentInfo(String attachmentId,String transStatus,String accessPath,String coverPath,String pageCount,String transError){
    	StringBuffer updateSQL = new StringBuffer();
    	updateSQL.append(" update learn_attachment set file_type='png',trans_status=?,access_path=?,cover_path=?,page_count=?,trans_error=?");
    	updateSQL.append(" where attachment_id=?");
    	
    	ArrayList<Object> updateParam = new ArrayList<Object>();
    	updateParam.add(transStatus);
    	updateParam.add(accessPath);
    	updateParam.add(coverPath);
    	updateParam.add(pageCount);
    	updateParam.add(transError);
    	updateParam.add(attachmentId);
		
		PlatformDao pdao = new PlatformDao();
		try{
		    pdao.setSql(updateSQL.toString());
		    pdao.setBindValues(updateParam);
		    pdao.executeTransactionSql();			
		}
		catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[����ת��-�޸ĸ�����Ϣ]"+e.getMessage());			
		} finally {
			pdao.releaseConnection();
		}		
	}
	/**
	 * ���ø�����Ϣ
	 * @param attachmentId
	 * @param transStatus
	 * @param accessPath
	 */
	public void setAttachmentInfo(String attachmentId,String transStatus,String accessPath,String transError){
		
    	StringBuffer updateSQL = new StringBuffer();
    	updateSQL.append(" update learn_attachment set trans_status=?,access_path=?,trans_error=?");
    	updateSQL.append(" where attachment_id=?");
    	
    	ArrayList<Object> updateParam = new ArrayList<Object>();
    	updateParam.add(transStatus);
    	updateParam.add(accessPath);
    	updateParam.add(transError);
    	updateParam.add(attachmentId);
		
		PlatformDao pdao = new PlatformDao();
		try{
		    pdao.setSql(updateSQL.toString());
		    pdao.setBindValues(updateParam);
		    pdao.executeTransactionSql();			
		}
		catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[����ת��-�޸ĸ�����Ϣ]"+e.getMessage());			
		} finally {
			pdao.releaseConnection();
		}
	}
	/**
	 * ��ȡδת������
	 * @return δת���ĸ����б�
	 */
	public List getUnconvertedAttachment(){
		
		StringBuffer strSQL = new StringBuffer();
		strSQL.append(" select * ");
		strSQL.append(" from learn_attachment t1");
		strSQL.append(" where t1.trans_status='10'");
		strSQL.append(" and '"+transType+"' like concat('%',t1.file_type,'%')");
		
		PlatformDao pdao = new PlatformDao();
	    try{
	    	pdao.setSql(strSQL.toString());	    	
	    	Element result = pdao.executeQuerySql(10,1);
	    	List ls = result.getChildren("Record");
	    	for (int i=0;i<ls.size();i++){
	    		Element rec = (Element)ls.get(i);
	    		setAttachmentInfo(rec.getChildText("attachment_id"),"15","","");
	    	}
	    	return ls;	    	
	    }catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[����ת��-ȡ��ת���ĸ����б�]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();
		}		
	}	
}
