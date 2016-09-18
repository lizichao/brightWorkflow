package cn.com.bright.yuexue.converter;

import cn.brightcom.jraf.util.FileUtil;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * <p>Title:officeתHtml</p>
 * <p>Description: officeתHtml</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zhangxq
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��</p>
 * <p> ���������밲װoffice2007������</p>
 * <p> jacob-1.17-x86.dll,jacob-1.17-x64.dll�追���ŵ�System32Ŀ¼��</p>
 *     Put the appropriate DLL for your platform into your runtime library path.
 * 
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time             version      desc</p>
 * <p> zhangxq    2014/08/12       1.0          build this moudle </p>
 *     
 */
public class OfficeToHtml implements AbstractConverter{

    /**
     * ����ת��
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
	 * wordתhtml
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
	 * pptתhtml
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
	 * excelתhtml
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
	 * docxתdoc
	 * @param docxPath
	 * @param docPath
	 * 0:Microsoft Word 97 - 2003 �ĵ� (.doc��
	 * 1:Microsoft Word 97 - 2003 ģ�� (.dot��
	 * 2:�ı��ĵ� (.txt��
	 * 3:�ı��ĵ� (.txt��
	 * 4:�ı��ĵ� (.txt��
	 * 5:�ı��ĵ� (.txt��
	 * 6:RTF ��ʽ (.rtf��
	 * 7:�ı��ĵ� (.txt��
	 * 8:HTML �ĵ� (.htm)(���ļ��У�
	 * 9:MHTML �ĵ� (.mht)(���ļ���
	 * 10:MHTML �ĵ� (.mht)(���ļ���
	 * 11:XML �ĵ� (.xml��
	 * 12:Microsoft Word �ĵ� (.docx��
	 * 13:Microsoft Word ���ú���ĵ� (.docm��
	 * 14:Microsoft Word ģ�� (.dotx��
	 * 15:Microsoft Word ���ú��ģ�� (.dotm��
	 * 16:Microsoft Word �ĵ� (.docx��
	 * 17:PDF �ļ� (.pdf��
	 * 18:XPS �ĵ� (.xps��
	 * 19:XML �ĵ� (.xml��
	 * 20:XML �ĵ� (.xml��
	 * 21:XML �ĵ� (.xml��
	 * 22:XML �ĵ� (.xml��
	 * 23:OpenDocument �ı� (.odt��
	 * 24:WTF �ļ� (.wtf)
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
