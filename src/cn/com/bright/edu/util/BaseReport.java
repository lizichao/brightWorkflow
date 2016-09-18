package cn.com.bright.edu.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.report.ReportManager;
import net.sf.jxls.report.ReportManagerImpl;
import net.sf.jxls.transformer.XLSTransformer;
import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBConnection;

/**
 * <p>Title:基本报表类</p>
 * <p>Description: 所有报表实体都可继承本类</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能
 *      
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time          version      desc</p>
 * <p> zxqing    10/8/31       1.0          build this moudle </p>
 *     
 */
public class BaseReport implements ReportInterface {
	
	public BaseReport() {};
	
	public Map<String,Object> fillData(HashMap<String,Object> paraHashMap){
		Map<String , Object> resultMap = new  HashMap<String , Object>();
		resultMap.putAll(paraHashMap);		
		return resultMap;		 		
	};
	
	public String makeReport(HashMap<String,Object> paraHashMap)throws IOException,ParsePropertyException{
		String srcFileName = "";
		String reportName = (String)paraHashMap.get("reportName");	
		String basePath   = (String)paraHashMap.get("basePath");
		String usercode   = (String)paraHashMap.get("usercode");
		
		srcFileName = basePath+"reports/model/"+reportName+".xls";
		
		DateFormat df = new SimpleDateFormat("yyyy");
		
		String currentYear = df.format(new Date());
		
		DateFormat dfTime = new SimpleDateFormat("yyyyMMddHHmmss");
		
		String currentdate = dfTime.format(new Date());
		String docName = reportName+"_"+usercode+"_"+currentdate+".xls";         
		
		String docPath = basePath+"reports/out/"+currentYear+"_2/";
		
		String resultFileName = docPath+docName;
		String webPath = "/reports/out/"+currentYear+"_2/"+docName;
		
		try {
		    File dirFile = new File(docPath);		    
		    if (!dirFile.exists() && !dirFile.isDirectory()){
		    	if (dirFile.mkdirs()){
		    		System.out.println(docPath+"文件路径已经创建"); 
		    	}
		    	else{
		    		System.out.println(docPath+"文件路径已存在"); 
		    	}
		    }
			
		}catch (Exception e)  {
            e.printStackTrace();
		}
		
		File  docFile = new File(docPath,docName);		
	    if (!docFile.exists()){
	    	docFile.createNewFile();
	    	ApplicationContext.regSubSys("pcmc");
	    	Connection conn = null;
			try{
			    conn = DBConnection.getConnection();	
			    
			    Map<String,Object> beans = fillData(paraHashMap);
			    
			    ReportManager reportManager = new ReportManagerImpl( conn, beans );
			    beans.put("rm", reportManager);
			      
			    XLSTransformer transformer = new XLSTransformer();			
			    transformer.transformXLS(srcFileName, beans, resultFileName);

			    beans.clear();
			    beans       = null;		    
			    transformer = null;
			} catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if (conn!=null){
					try{
						conn.close();
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}		    
 		}  		
		return webPath;
	}
}

