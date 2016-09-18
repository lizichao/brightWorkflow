package cn.com.bright.yuexue.converter;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jpedal.PdfDecoder;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.objects.PdfPageData;

import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.ImageUtils;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.yuexue.util.AttachmentUtil;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


/**
 * <p>Title:Pdf转Html</p>
 * <p>Description: Pdf转Html</p>
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
 * <p> zhangxq    2014/08/12       1.0          build this moudle </p>
 *     
 */
public class Pdf2Jpg implements AbstractConverter {
	
	private String origFileName;
	private String pageCount;
	private String uploadUserID;
	
	public void setFileName(String origFileName){
		this.origFileName=origFileName;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	public String getPageCount(){
		return this.pageCount;
	}
	public void setUploadUserID(String uploadUserID) {
		this.uploadUserID = uploadUserID;
	}

	/**
     * 启动转换
     * @param srcPath
     * @param destPath
     */	
	public void process(String srcPath,String destPath)throws Exception{
		String folderPath = "";
		String fileName = "";
		String fileNameNoExt = "";			
		if (StringUtil.isNotEmpty(this.pageCount) && !"0".equals(this.pageCount)){
			folderPath = destPath.substring(0,destPath.lastIndexOf("/"));
			fileNameNoExt = folderPath.substring(folderPath.lastIndexOf("/")+1);
			folderPath = folderPath.substring(0,folderPath.lastIndexOf("/")+1);
		}
		else{
			folderPath = srcPath.substring(0,srcPath.lastIndexOf("/")+1);
			fileName = FileUtil.getFileName(srcPath);
			fileNameNoExt = fileName.substring(0, fileName.lastIndexOf("."));		
			FileUtil.createDirs(folderPath+fileNameNoExt+"/", true);
		}
		File file = new File(srcPath);

		InputStream is = null; 
		FileOutputStream out = null;
		try{
			StringBuffer bookcotent = new StringBuffer(); 
			is = new FileInputStream(file); 
			
			PdfDecoder decode_pdf = new PdfDecoder(true);  
			PdfDecoder.setFontReplacements(decode_pdf);  
			decode_pdf.openPdfFileFromInputStream(is, false);			
			int dbPageCount=0;
			int pageSize = decode_pdf.getPageCount();
			if (StringUtil.isNotEmpty(this.pageCount) && !"0".equals(this.pageCount)){
			   dbPageCount = Integer.parseInt(this.pageCount);
				for (int j=1;j<=dbPageCount;j++){
					bookcotent.append("<div id='book_page_"+j+"' style='background-image:url("+j+".png)'></div>");
				}				
			}
			this.setPageCount(Integer.toString(pageSize+dbPageCount));
			
			PdfPageData pageData = decode_pdf.getPdfPageData();
			int pdfWidth=0;
			int pdfHeight=0;
			
			for (int i = 1; i <= pageSize; i++) {  
				pdfWidth  = (int)(pageData.getCropBoxWidth(i)*1.5);
				pdfHeight = (int)(pageData.getCropBoxHeight(i)*1.5);
				
				Rectangle rect = new Rectangle(0, 0, pdfWidth ,pdfHeight);
				//String desFileName = lpad(Integer.toString(i),Integer.toString(pageSize).length(),"0")+".png";
				String desFileName = Integer.toString(i+dbPageCount)+".png";
				bookcotent.append("<div id='book_page_"+Integer.toString(i+dbPageCount)+"' style='background-image:url("+desFileName+")'></div>");
				
				out = new FileOutputStream(folderPath+fileNameNoExt+"/"+desFileName); // 输出到文件流
				BufferedImage tag = new BufferedImage(rect.width, rect.height,BufferedImage.TYPE_INT_RGB);;

				
				Map<Object,Object> mapValues = new HashMap<Object,Object>();
				mapValues.put(JPedalSettings.EXTRACT_AT_BEST_QUALITY_MAXSCALING, 2);
				mapValues.put(JPedalSettings.EXTRACT_AT_PAGE_SIZE, new String[]{String.valueOf(pdfWidth),String.valueOf(pdfHeight)});
				mapValues.put(JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE, Boolean.TRUE);
				PdfDecoder.modifyJPedalParameters(mapValues); 
				
				Image img = decode_pdf.getPageAsHiRes(i,null,false);
			    tag.getGraphics().drawImage(img, 0,0, rect.width, rect.height,null);
			    
			    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
			    encoder.encode(tag); // JPEG编码	
			    out.close();
			    if (i==1 && dbPageCount==0){//封面生成缩略图，书架上用
			    	ImageUtils.createPreviewImage(folderPath+fileNameNoExt+"/"+desFileName);
			    }
			}
			decode_pdf.closePdfFile();
			
			String bookdisplay="double";
			
			int bookheight=(450*pdfHeight)/pdfWidth;
			if (bookheight>600){
				bookheight = 600;
			}
			else if (bookheight<400){
				bookheight = 600;
				//bookdisplay="single";
			}
			
			int htmlBodyHeight = bookheight+50;
			
			String sTemplateContent = AttachmentUtil.getFileContent(FileUtil.getWebPath()+"/reports/pdfConverterTemplate.html");
			sTemplateContent = sTemplateContent.replaceAll("<!--fileName-->", this.origFileName);
			sTemplateContent = sTemplateContent.replaceAll("<!--bookheight-->", ""+bookheight);
			sTemplateContent = sTemplateContent.replaceAll("<!--bookdisplay-->", bookdisplay);
			sTemplateContent = sTemplateContent.replaceAll("<!--upload_userid-->", this.uploadUserID);
			sTemplateContent = sTemplateContent.replaceAll("<!--bodyheight-->", ""+htmlBodyHeight);
			sTemplateContent = sTemplateContent.replaceAll("<!--bookcotent-->", bookcotent.toString());
			
			FileWriter fw = new FileWriter(folderPath+fileNameNoExt+"/"+"index.html");			
			fw.write(sTemplateContent);
			fw.flush();
			fw.close();			
		}
		catch(Exception ex){
			throw ex;			
		}
		finally{ 
			try{
			   if (is!=null){
			      is.close();
			   }
			   if (out!=null){
			      out.close();
			   }			     
			}
			catch(IOException e){  
				e.printStackTrace();
			}			 
		}
	}
	/**
	 * 左补充
	 * @param s
	 * @param length
	 * @param replace
	 * @return
	 */
	/**
	public String lpad(String s,int length,String replace){
		String temp = s;
		while (temp.length() < length) {  
			temp = replace+temp;  
        }  
        return temp; 		
	}
	*/
		
	public static void main(String argv[]) {  
        try {  
        	String destPath="/upload/book/2014/10/402882e54920fd5d01492136deae0016/index.html";
			String folderPath = destPath.substring(0,destPath.lastIndexOf("/"));
			System.out.println("folderPath1==="+folderPath);
			String fileNameNoExt = folderPath.substring(folderPath.lastIndexOf("/")+1);
			System.out.println("fileNameNoExt==="+fileNameNoExt);
			folderPath = folderPath.substring(0,folderPath.lastIndexOf("/")+1);  
			System.out.println("folderPath2==="+folderPath);
        	//Pdf2Jpg p2J = new Pdf2Jpg();
        	//p2J.process("E:/converter/ddddd.pdf", "E:/converter/ddddd.jpg");              
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }	
	
}
