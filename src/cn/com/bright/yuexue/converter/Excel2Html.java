package cn.com.bright.yuexue.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;


/**
 * <p>Title:Excel转Html</p>
 * <p>Description: Excel转Html</p>
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
public class Excel2Html implements AbstractConverter{
	
    /**
     * 启动转换
     * @param srcPath
     * @param destPath
     */
	public void process(String srcPath,String destPath) throws Exception{
		File excelFile = new File(srcPath);  
		File htmlFile = new File(destPath);  
		File htmlFileParent = new File(htmlFile.getParent()); 
		InputStream is = null;  
		OutputStream out = null;  
		StringWriter writer = null;  
		try{
			 if(excelFile.exists()){  
				 if(!htmlFileParent.exists()){  
					 htmlFileParent.mkdirs();  
				 }
				 is = new FileInputStream(excelFile);  
				 HSSFWorkbook workBook = new HSSFWorkbook(is);
				 
				 ExcelToHtmlConverter converter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
				 converter.setOutputColumnHeaders(false);//不输出列头 A,B,C,D,...
				 converter.setOutputRowNumbers(false);//不输出行号 1,2,3,4,...
				 converter.setOutputLeadingSpacesAsNonBreaking(false);
				 converter.setUseDivsToSpan(false);
				 converter.processWorkbook(workBook); 
				 Document excelDoc = converter.getDocument();
				 writer = new StringWriter();  
				 Transformer serializer = TransformerFactory.newInstance().newTransformer();  
				 serializer.setOutputProperty(OutputKeys.ENCODING, "GB2312");  
				 serializer.setOutputProperty(OutputKeys.INDENT, "no");				  
				 serializer.setOutputProperty(OutputKeys.METHOD, "html");  
				 serializer.transform(new DOMSource(excelDoc), 
						              new StreamResult(writer)
						             );
				 String content = writer.toString();				 
				 String left  = content.substring(0, content.indexOf("<h2>"));
				 String right = content.substring(content.indexOf("</h2>")+5);
				 content = left+right;
				 content = content.replaceAll("border-bottom:thin solid black;", "");
				 content = content.replaceAll("border-top:thin solid black;", "");
				 content = content.replaceAll("border-left:thin solid black;", "");
				 content = content.replaceAll("border-right:thin solid black;", "border:1px solid black;");
				 
				 out = new FileOutputStream(htmlFile);  
				 out.write(content.getBytes("GB2312")); 
				 out.flush(); 
				 out.close();  
				 writer.close();				 
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
			   if (out!=null){
			      out.close(); 
			   }
			   if (writer!=null){
			      writer.close(); 
			   }
			}
			catch(IOException e){  
				e.printStackTrace();
			}			 
		}		
	}
	
	public static void main(String argv[]) {  
        try {  
        	Excel2Html e2h = new Excel2Html();
        	e2h.process("E:/converter/aaaaa.xls", "E:/converter/aaaaa.html");              
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 	
}
