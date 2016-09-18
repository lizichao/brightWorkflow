package cn.com.bright.yuexue.task;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import cn.brightcom.jraf.db.PlatformDao;
import cn.brightcom.jraf.util.ConfigDocument;
import cn.brightcom.jraf.util.DatetimeUtil;
import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.Log;
import cn.brightcom.jraf.util.TimerTask;
import cn.brightcom.jraf.util.XmlDocPkgUtil;
import cn.com.bright.yuexue.util.AttachmentUtil;
/**
 * <p>Title:根据扫面图片存储文件夹，批量生成电子教材</p>
 * <p>Description: 电子教材格式化</p>
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
 * <p> zhangxq    2015/03/20       1.0          build this moudle </p>
 *     
 */
public class BatchImpBookTask extends TimerTask{
	private Log log4j = new Log(this.getClass().toString());
	
	public void run() {	
		//System.out.println("----------  "+DatetimeUtil.getNow("")+" BatchImpBookTask run ----------");
        try { 
        	Map<String,String> mp = new HashMap<String,String>();
        	mp.put("0", "全册");
        	mp.put("1", "上册");
        	mp.put("2", "下册");
        	mp.put("5", "必修1");
        	mp.put("6", "必修2");
        	mp.put("7", "必修3");
        	mp.put("8", "必修4");
        	mp.put("9", "必修5");
        	
        	List ls = getAllFile("G:\\Tomcat-6.0.20\\webapps\\ROOT\\upload\\book");
        	log4j.logInfo("foldercount==="+ls.size());
        	String sTemplateContent = AttachmentUtil.getFileContent(FileUtil.getWebPath()+"/reports/pdfConverterTemplate.html");
        	for (int i=0;i<ls.size();i++){
        		Map<String,String> fm = (Map<String,String>)ls.get(i);
        		String path = fm.get("path");
        		System.out.println("path==="+path);
        		path = path.substring(0, path.lastIndexOf("\\"));
        		String count = fm.get("count");
        		String temp = path.replace("\\", "/");        		
        		temp = temp.substring(path.indexOf("book")+5);        		
        		String[] pathbook = temp.split("/");
        		String subjectid = pathbook[0];
        		String gradecode = pathbook[1];
        		String rootfolder =  pathbook[2];        		
        		//System.out.println("subjectid="+subjectid+";gradecode="+gradecode+";rootfolder="+rootfolder);
        		Element folderRec = getBookFolder(subjectid,gradecode,mp.get(rootfolder));        		
        		if (folderRec!=null){
        			String bookname = folderRec.getChildText("subjname")+"-"+folderRec.getChildText("grade_desc")+"-"+mp.get(rootfolder);
        			genBookHtml(sTemplateContent,path,count,bookname);
        			//addBook2DB(folderRec,temp,count,bookname,rootfolder);        			
        		}
        		else{        			
        			log4j.logInfo("["+path+"]folder can not import,please check!");
        		}        		
        	}
        }catch (Exception e) {  
            e.printStackTrace();  
        } 		
	}
	/**
	 * 生成书本静态html
	 * @param path
	 * @param count
	 */
	private void genBookHtml(String bookTemplate,String path,String count,String origFileName){
		try{
			int pageSize = Integer.parseInt(count);
			StringBuffer bookcotent = new StringBuffer();
			for (int i = 1; i < 6; i++) {  
				bookcotent.append("<div id='book_page_"+i+"' style='background-image:url("+i+".jpg)'></div>");
			}
			bookTemplate = bookTemplate.replaceAll("<!--fileName-->",origFileName);
			bookTemplate = bookTemplate.replaceAll("<!--book_page_size-->", ""+pageSize);			
			bookTemplate = bookTemplate.replaceAll("<!--bookcotent-->", bookcotent.toString());
			
			FileWriter fw = new FileWriter(path+"/index.html");
			fw.write(bookTemplate);
			fw.flush();
			fw.close();				
		}
	    catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[电子书本加载-生成静态书本html出错]"+e.getMessage());	
		}		
	}
	/**
	 * 在数据库中增加电子书
	 * @param folderRec
	 * @param path
	 * @param count
	 */
	private void addBook2DB(Element folderRec,String path,String count,String bookname,String rootfolder){
		PlatformDao pdao = new PlatformDao();
	    try{
	    	Element attachRec = ConfigDocument.createRecordElement("yuexue", "learn_attachment");
	    	XmlDocPkgUtil.copyValues(folderRec, attachRec, 0, true);
	    	XmlDocPkgUtil.setChildText(attachRec, "userid", "1");
	    	XmlDocPkgUtil.setChildText(attachRec, "create_by", "admin");
	    	XmlDocPkgUtil.setChildText(attachRec, "file_name", bookname);	    	
	    	XmlDocPkgUtil.setChildText(attachRec, "file_type", "book");
	    	XmlDocPkgUtil.setChildText(attachRec, "file_size", rootfolder);
	    	XmlDocPkgUtil.setChildText(attachRec, "trans_status", "30");
	    	XmlDocPkgUtil.setChildText(attachRec, "page_count", count);
	    	XmlDocPkgUtil.setChildText(attachRec, "file_path", "/upload/book/"+path+"/index.html");	    	
	    	XmlDocPkgUtil.setChildText(attachRec, "access_path","/upload/book/"+path+"/index.html");
	    	XmlDocPkgUtil.setChildText(attachRec, "cover_path", "/upload/book/"+path+"/1.jpg");
	    	
	    	String attachid = pdao.insertOneRecordSeqPk(attachRec).toString();
	    	
	    	Element bookAttachRec = ConfigDocument.createRecordElement("yuexue", "base_book_attachment");
	    	XmlDocPkgUtil.copyValues(folderRec, bookAttachRec, 0, true);
	    	XmlDocPkgUtil.setChildText(bookAttachRec, "userid", "1");
	    	XmlDocPkgUtil.setChildText(bookAttachRec, "create_by", "admin");
	    	XmlDocPkgUtil.setChildText(bookAttachRec, "attachment_id",attachid);
	    	XmlDocPkgUtil.setChildText(bookAttachRec, "book_type","10");
	    	pdao.insertOneRecordSeqPk(bookAttachRec);
	    }
	    catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[电子书本加载-增加电子书记录]"+e.getMessage());	
		} finally {
			pdao.releaseConnection();
		}	    
	}
	/**
	 * 取学科目录节点
	 * @param subjectid
	 * @param gradecode
	 * @param foldername
	 * @return
	 */
	private Element getBookFolder(String subjectid,String gradecode,String foldername){
		PlatformDao pdao = new PlatformDao();
	    try{
	    	StringBuffer strSQL = new StringBuffer();
	    	strSQL.append("select t1.subjectid,t1.subjname,t2.grade_code,getParamDesc('C_GRADE',t2.grade_code) as grade_desc,");
	    	strSQL.append(" t2.subject_book_id,t3.folder_id,t3.folder_name");
	    	strSQL.append(" from base_subject t1,base_subject_book t2,base_book_folder t3");
	    	strSQL.append(" where t1.subjectid=t2.subjectid and  t3.parent_folder_id='-1'");
	    	strSQL.append(" and t2.subject_book_id=t3.subject_book_id and  t3.valid='Y' and t1.state>'0' and t2.valid='Y'");
	    	strSQL.append(" and t1.subjectid=? and t2.grade_code=? and t3.folder_name=?");
	    	
	    	ArrayList<Object> paramList = new ArrayList<Object>();
	    	paramList.add(subjectid);
	    	paramList.add(gradecode);
	    	paramList.add(foldername);
	    	
	    	pdao.setSql(strSQL.toString());	
	    	pdao.setBindValues(paramList);
	    	Element result = pdao.executeQuerySql(0,-1);	    	
	    	return result.getChild("Record");    	
	    }
	    catch (Exception e) {	    	
			e.printStackTrace();
			log4j.logError("[电子书本加载-取书本目录ID]"+e.getMessage());	
			return null;
		} finally {
			pdao.releaseConnection();
		}	    
		
	}
	/**
	 * 取所有的文件目录列表
	 * @param filePath
	 * @return
	 */
	private List<Map> getAllFile(String filePath){
		List<Map> list = new ArrayList<Map>() ;
		File file = new File(filePath) ;
		File[] docs = file.listFiles();
		int fileCount = docs.length;
		if(docs == null || docs.length <= 0){
			
		}else{
			for(int i = 0 ; i < fileCount ; i ++ ){
				File f = docs[i] ; 
				if(f.isFile()){
					String path = f.getAbsolutePath();
					if (path.indexOf("原始")<1){
					   Map<String,String> fm = new HashMap<String,String>();
					   fm.put("path",f.getAbsolutePath());
					   fm.put("count",""+(fileCount-2));
			 	       list.add(fm) ;
			 	       break;
					}				
				}else{
					list.addAll(getAllFile(f.getAbsolutePath())) ; 
				}
			}
		}		
		return list ;		
	}
}
