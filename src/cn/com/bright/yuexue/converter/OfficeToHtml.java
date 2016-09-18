package cn.com.bright.yuexue.converter;

import cn.brightcom.jraf.util.FileUtil;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * <p>Title:office转Html</p>
 * <p>Description: office转Html</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能</p>
 * <p> 服务器必须安装office2007及以上</p>
 * <p> jacob-1.17-x86.dll,jacob-1.17-x64.dll需拷贝放到System32目录下</p>
 *     Put the appropriate DLL for your platform into your runtime library path.
 * 
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/08/12       1.0          build this moudle </p>
 *     
 */
public class OfficeToHtml implements AbstractConverter{

    /**
     * 启动转换
     * @param srcPath
     * @param destPath
     */	
	public void process(String srcPath,String destPath)throws Exception{
		String fileName = FileUtil.getFileName(srcPath);
		String fileType = FileUtil.getFileExt(fileName);
		if ("doc".equals(fileType) || "docx".equals(fileType)){
			WordtoHtml(srcPath,destPath);
		}
		else if ("xls".equals(fileType) || "xlsx".equals(fileType)){
			ExceltoHtml(srcPath,destPath);
		}
		else if ("ppt".equals(fileType) || "pptx".equals(fileType)){
			PPttoHtml(srcPath,destPath);
		}
	}
	/**
	 * word转html
	 * @param s
	 * @param s1
	 * @return
	 */
	public boolean WordtoHtml(String s, String s1) {
        ComThread.InitSTA();  
        ActiveXComponent activexcomponent = new ActiveXComponent("Word.Application");  
        String s2 = s;  
        String s3 = s1;  
        boolean flag = false;  
        try {  
            activexcomponent.setProperty("Visible", new Variant(false));  
            Dispatch dispatch = activexcomponent.getProperty("Documents").toDispatch();  
            Dispatch dispatch1 = Dispatch.invoke(dispatch, 
            		                             "Open", 
            		                             1,  
                                                 new Object[] { s2, new Variant(false), new Variant(true) },  
                                                new int[1]).toDispatch();  
            Dispatch.invoke(dispatch1, "SaveAs", 1, new Object[] { s3,new Variant(8) },new int[1]);  
            Variant variant = new Variant(false);  
            Dispatch.call(dispatch1, "Close", variant);  
            flag = true;  
        } catch (Exception exception) {  
            exception.printStackTrace();  
        } finally {  
            activexcomponent.invoke("Quit", new Variant[0]);  
            ComThread.Release();  
            ComThread.quitMainSTA();  
        }  
        return flag; 		
	}
	/**
	 * ppt转html
	 * @param s
	 * @param s1
	 * @return
	 */
	public boolean PPttoHtml(String s, String s1) {  
		ComThread.InitSTA();  
        ActiveXComponent activexcomponent = new ActiveXComponent("PowerPoint.Application");  
        String s2 = s;  
        String s3 = s1;  
        boolean flag = false;  
        try {  
            Dispatch dispatch = activexcomponent.getProperty("Presentations").toDispatch();  
            Dispatch dispatch1 = Dispatch.call(dispatch, 
            		                           "Open", 
            		                           s2,  
                                               new Variant(-1), 
                                               new Variant(-1), 
                                               new Variant(0)).toDispatch();  
            Dispatch.call(dispatch1, "SaveAs", s3, new Variant(12));  
            //Variant variant = new Variant(-1);  
            Dispatch.call(dispatch1, "Close");  
            flag = true;  
        } catch (Exception exception) {  
            System.out.println("|||" + exception.toString());  
        } finally {  
            activexcomponent.invoke("Quit", new Variant[0]);  
            ComThread.Release();  
            ComThread.quitMainSTA();  
        }  
        return flag; 		
	}
	/**
	 * excel转html
	 * @param s
	 * @param s1
	 * @return
	 */
	public boolean ExceltoHtml(String s, String s1) { 
		ComThread.InitSTA();  
        ActiveXComponent activexcomponent = new  ActiveXComponent("Excel.Application");  
        String s2 = s;  
        String s3 = s1;  
        boolean flag = false;  
        try  
        {  
	        activexcomponent.setProperty("Visible", new Variant(false));  
	        Dispatch dispatch = activexcomponent.getProperty("Workbooks").toDispatch();  
	        Dispatch dispatch1 = Dispatch.invoke(dispatch, 
	        		                             "Open", 
	        		                             1, 
	        		                             new Object[] {s2, new Variant(false), new Variant(true)}, 
	        		                             new int[1]).toDispatch();  
	        Dispatch.call(dispatch1, "SaveAs", s3, new Variant(44));  
	        Variant variant = new Variant(false);  
	        Dispatch.call(dispatch1, "Close", variant);  
	        flag = true;  
        }  
        catch(Exception exception)  
        {  
           System.out.println("|||" + exception.toString());  
        }  
        finally  
        {  
	        activexcomponent.invoke("Quit", new Variant[0]);  
	        ComThread.Release();  
	        ComThread.quitMainSTA();  
        }  
        return flag; 		
	}
	/**
	 * docx转doc
	 * @param docxPath
	 * @param docPath
	 * 0:Microsoft Word 97 - 2003 文档 (.doc）
	 * 1:Microsoft Word 97 - 2003 模板 (.dot）
	 * 2:文本文档 (.txt）
	 * 3:文本文档 (.txt）
	 * 4:文本文档 (.txt）
	 * 5:文本文档 (.txt）
	 * 6:RTF 格式 (.rtf）
	 * 7:文本文档 (.txt）
	 * 8:HTML 文档 (.htm)(带文件夹）
	 * 9:MHTML 文档 (.mht)(单文件）
	 * 10:MHTML 文档 (.mht)(单文件）
	 * 11:XML 文档 (.xml）
	 * 12:Microsoft Word 文档 (.docx）
	 * 13:Microsoft Word 启用宏的文档 (.docm）
	 * 14:Microsoft Word 模板 (.dotx）
	 * 15:Microsoft Word 启用宏的模板 (.dotm）
	 * 16:Microsoft Word 文档 (.docx）
	 * 17:PDF 文件 (.pdf）
	 * 18:XPS 文档 (.xps）
	 * 19:XML 文档 (.xml）
	 * 20:XML 文档 (.xml）
	 * 21:XML 文档 (.xml）
	 * 22:XML 文档 (.xml）
	 * 23:OpenDocument 文本 (.odt）
	 * 24:WTF 文件 (.wtf)
	 * @return
	 */
	public boolean docxTodoc(String docxPath, String docPath) {
		boolean flag = false;
		try {
			ComThread.InitSTA();
			ActiveXComponent app = new ActiveXComponent("Word.Application");
	        app.setProperty("Visible", new Variant(false));
	        Dispatch docs = app.getProperty("Documents").toDispatch();            
	        Dispatch doc = Dispatch.invoke(
	                docs,
	                "Open",
	                Dispatch.Method,
	                new Object[] { docxPath, new Variant(false),
	                        new Variant(true) }, new int[1]).toDispatch();
	        Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {docPath,new Variant(0)},new int[1]);
	        Dispatch.call(doc, "Close", new Variant(false));
	        
	        app.invoke("Quit", new Variant[] {});
	        ComThread.Release();
	        ComThread.quitMainSTA();
	
	        flag = true;
		} catch (Exception e) {  
            e.printStackTrace();  
        }
        return flag;
	}
	
	public static void main(String argv[]) {  
        try {  
        	OfficeToHtml p2h = new OfficeToHtml();
        	p2h.docxTodoc("D:/1/1.docx", "D:/1/1.doc");              
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 	
}
