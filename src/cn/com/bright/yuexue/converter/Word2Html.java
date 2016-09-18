package cn.com.bright.yuexue.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

import cn.brightcom.jraf.util.FileUtil;

/**
 * <p>Title:Word转Html</p>
 * <p>Description: Word转Html</p>
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
public class Word2Html implements AbstractConverter{
	private String fileNameNoExt;
	private String folderPath;
	private String accessPath;
    /**
     * 启动转换
     * @param srcPath
     * @param destPath
     */	
	public void process(String srcPath,String destPath)throws Exception{
		this.folderPath = srcPath.substring(0,srcPath.lastIndexOf("/")+1);
		String fileName = FileUtil.getFileName(srcPath);
		this.fileNameNoExt = fileName.substring(0, fileName.lastIndexOf("."));
		if (folderPath.indexOf("upload")>0){
			this.accessPath="/"+folderPath.substring(folderPath.indexOf("upload"));
		}
		else{
			this.accessPath=folderPath;
		}
		
		File wordlFile = new File(srcPath);  
		File htmlFile = new File(destPath);  
		File htmlFileParent = new File(htmlFile.getParent()); 
		InputStream is = null;  
		//OutputStream out = null;  
		//StringWriter writer = null;  
		try{
			 if(wordlFile.exists()){  
				 if(!htmlFileParent.exists()){  
					 htmlFileParent.mkdirs();  
				 }
				 is = new FileInputStream(wordlFile); 
				 
				 HWPFDocument wordDocument = new HWPFDocument(is);

				 WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
				 wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
			            	try {
			            		FileUtil.createDirs(folderPath+fileNameNoExt+"/", true);
			            		File file = new File(folderPath+fileNameNoExt+"/" + suggestedName);
								OutputStream os = new FileOutputStream(file);
								os.write(content);
								os.close();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            	return accessPath+fileNameNoExt+"/"+suggestedName;							
			            }
			        });
		         wordToHtmlConverter.processDocument(wordDocument);
		         
		         Document htmlDocument = wordToHtmlConverter.getDocument();
		        
		         OutputStream outStream = new FileOutputStream(htmlFile);
		         DOMSource domSource = new DOMSource(htmlDocument);
		         StreamResult streamResult = new StreamResult(outStream);
		 
		         TransformerFactory tf = TransformerFactory.newInstance();
		         Transformer serializer = tf.newTransformer();
		         serializer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
		         serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		         serializer.setOutputProperty(OutputKeys.METHOD, "html");
		         serializer.transform(domSource, streamResult);
		         outStream.close();
		         
		         //writeFile(new String(outStream.toByteArray()), outPutFile);  	 
			 }
		}
	    catch(Exception ex){
			throw ex;			
		}
		finally{ 
			try{
			   if (is!=null){
			      is.close();
			   }
			   //out.close(); 
			   //writer.close();  
			}
			catch(IOException e){  
				e.printStackTrace();
			}			 
		}
	}
	
	public static void main(String argv[]) {  
        try {        	
        	Word2Html w2h = new Word2Html();
        	w2h.process("E:/converter/bbbbb.doc", "E:/converter/bbbbb.html");              
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 	
}
