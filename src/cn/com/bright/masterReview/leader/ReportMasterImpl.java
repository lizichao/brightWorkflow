package cn.com.bright.masterReview.leader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;
import org.jdom.Element;

import cn.brightcom.jraf.context.ApplicationContext;
import cn.brightcom.jraf.db.DBConnection;
import cn.brightcom.jraf.db.PlatformDao;

public class ReportMasterImpl {

	public ReportMasterImpl(){}
	
	public Map<String , Object> fillData(HashMap<String , Object> paraHashMap){
		Map<String , Object> resultMap =  new HashMap<String , Object>();

        PlatformDao dao = null;
        try {
            dao = new PlatformDao();
            ArrayList<String> bvals = new ArrayList<String>();
            StringBuffer sqlBuf = new StringBuffer("");

            sqlBuf.append("SELECT t.headerMasterName,");
            sqlBuf.append(" ziligrade,");
            sqlBuf.append(" managementgrade,");
            sqlBuf.append(" zhuanyegrade,");
            sqlBuf.append("professorgrade,");
            sqlBuf.append("sum(ziligrade + managementgrade + zhuanyegrade + professorgrade) as sumgrade,");
            sqlBuf.append("   CASE");
            sqlBuf.append(" WHEN apply_level = '0' THEN '特级'");
            sqlBuf.append(" WHEN apply_level = '1' THEN '一级'");
            sqlBuf.append(" WHEN apply_level = '2' THEN '二级'");
            sqlBuf.append(" WHEN apply_level = '3' THEN '三级'");
            sqlBuf.append(" ELSE ''");
            sqlBuf.append("  END");
            sqlBuf.append("  AS apply_level,");
            
            //sqlBuf.append("apply_level,");
            sqlBuf.append("t.processInstanceId,");
            sqlBuf.append("a.usercode,");
            sqlBuf.append(" a.idnumber");
            sqlBuf.append(" FROM headmaster t");
            sqlBuf.append(" LEFT JOIN");
            sqlBuf.append("(SELECT avg(baseinfo_grade),");
            sqlBuf.append(" avg(work_experience_grade),");
            sqlBuf.append(" avg(education_grade),");
            sqlBuf.append("  avg(work_experience_grade)");
            sqlBuf.append(" + avg(education_grade)");
            sqlBuf.append(" + avg(professional_title_grade)");
            sqlBuf.append("   AS ziligrade,");
            sqlBuf.append("avg(professional_title_grade),");
            sqlBuf.append("avg(management_difficulty_grade),");
            sqlBuf.append(" avg(management_difficulty_grade_ago),");
            sqlBuf.append("   avg(management_difficulty_grade)");
            sqlBuf.append(" + avg(management_difficulty_grade_ago)");
            sqlBuf.append("    AS managementgrade,");
            sqlBuf.append(" avg(paper_grade),");
            sqlBuf.append("avg(work_publish_grade),");
            sqlBuf.append("avg(subject_grade),");
            sqlBuf.append("avg(personal_award_grade),");
            sqlBuf.append(" avg(school_award_grade),");
            sqlBuf.append(" avg(paper_grade)");
            sqlBuf.append(" + avg(work_publish_grade)");
            sqlBuf.append(" + avg(subject_grade)");
            sqlBuf.append(" + avg(personal_award_grade)");
            sqlBuf.append(" + avg(school_award_grade)");
            sqlBuf.append("  AS zhuanyegrade,");
            sqlBuf.append("apply_headmaster,");
            sqlBuf.append("processInstanceId");
            sqlBuf.append(" FROM headmaster_personnel_leader_grade");
            sqlBuf.append("  GROUP BY apply_headmaster,processInstanceId) p");
            sqlBuf.append(" ON t.headerMasterId = p.apply_headmaster");
            sqlBuf.append(" AND t.processInstanceId = p.processInstanceId");
            sqlBuf.append(" LEFT JOIN");
            sqlBuf.append("  (SELECT avg(report_grade) AS professorgrade, apply_headmaster,processInstanceId");
            sqlBuf.append("  FROM headmaster_professor_grade");
            sqlBuf.append(" GROUP BY apply_headmaster,processInstanceId) u");
            sqlBuf.append("  ON t.headerMasterId = u.apply_headmaster");
            sqlBuf.append("  AND t.processInstanceId = u.processInstanceId");
            sqlBuf.append(" inner join pcmc_user a");
            sqlBuf.append(" on t.headerMasterId = a.userid");
            sqlBuf.append(" where 1=1");
            sqlBuf.append("  AND t.create_date = (SELECT max(create_date)");
            sqlBuf.append("  FROM headmaster a");
            sqlBuf.append("  WHERE t.headerMasterId = a.headerMasterId)");
            sqlBuf.append("  GROUP BY t.headerMasterId");
         

            dao.setSql(sqlBuf.toString());
            dao.setBindValues(bvals);
            Element resultElement = dao.executeQuerySql(-1,1);
            
            List<Element> roleElements = resultElement.getChildren("Record");
            int  resultSize= roleElements.size();
            int shangshu = resultSize/10;
            int yushu = resultSize%10;
            
            int lowRange = shangshu*10+1;
            int highRange = shangshu*10+yushu;
            
            int i=0;
            int j=1;
            ArrayList<List> objects = new ArrayList<List>();  
            List<String> listSheetNames = new ArrayList<String>();  
            Map<String, Object> tempMap = new HashMap<String, Object>(); 
            
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
            
            for (Element roleElement : roleElements) {
               tempMap =new HashMap<String, Object>(); 
               String headerMasterName =   roleElement.getChildTextTrim("headermastername");
               String ziligrade =   roleElement.getChildTextTrim("ziligrade");
               String managementgrade =   roleElement.getChildTextTrim("managementgrade");
               String zhuanyegrade =   roleElement.getChildTextTrim("zhuanyegrade");
               String professorgrade =   roleElement.getChildTextTrim("professorgrade");
               String sumgrade =   roleElement.getChildTextTrim("sumgrade");
               String apply_level =   roleElement.getChildTextTrim("apply_level");
               
               
               tempMap.put("headerMasterName", headerMasterName);
               tempMap.put("ziligrade", ziligrade);
               tempMap.put("managementgrade", managementgrade);
               tempMap.put("zhuanyegrade", zhuanyegrade);
               tempMap.put("professorgrade", professorgrade);
               tempMap.put("sumgrade", sumgrade);
               tempMap.put("apply_level", apply_level);
               list.add(tempMap);  
               i++;
               if(i%10 ==0){
                   objects.add(list);  
                   listSheetNames.add(String.valueOf("sheet"+j));
                   j++;
                   list =  new ArrayList<Map<String, Object>>();  
               }
//               if(lowRange<=i && i<=highRange){
//                   
//               }
            }
            
            objects.add(list);  
            listSheetNames.add(String.valueOf("sheet"+j));
            resultMap.put("listSheetNames", listSheetNames);
            resultMap.put("objects", objects);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.releaseConnection();
        }
        
		return resultMap;
	}
	
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
            FileInputStream is = new FileInputStream(srcFileName);  
            docFile.createNewFile();
            ApplicationContext.regSubSys("pcmc");
            Connection conn = null;
            try{
                conn = DBConnection.getConnection();    
                
               Map<String,Object> beans = fillData(paraHashMap);
                
              //  ReportManager reportManager = new ReportManagerImpl( conn, beans );
               // beans.put("rm", reportManager);
                ArrayList<List> objects =( ArrayList<List>)beans.get("objects");
                List<String> listSheetNames = ( List<String>)beans.get("listSheetNames");
                  
                
                XLSTransformer transformer = new XLSTransformer();      
                
                Workbook workbook = transformer.transformMultipleSheetsList(is, objects, listSheetNames, "list", new HashMap(), 0);  

                workbook.write(new FileOutputStream(resultFileName));  
                
              //  transformer.transformXLS(srcFileName, beans, resultFileName);

             //   beans.clear();
             //   beans       = null;         
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
