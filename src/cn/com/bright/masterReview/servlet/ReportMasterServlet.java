package cn.com.bright.masterReview.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.brightcom.jraf.util.FileUtil;
import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.masterReview.leader.ReportMasterImpl;

/**
 * <p>Title:报表输出</p>
 * <p>Description: 报表输出</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // 主要函数及其功能</p>
 * <p>     前台所有报表都通过本servlet进行输出,调用本servlet的url如下</p>
 * <p>     ReportService?reportName=报表名&className=类名(可选)&其它参数</p>
 * <p>    其中reportName必须输入,其它的参数根据业务需求自己定义</p>
 *      
 * <p>History: // 历史修改记录:</p>
 * <p> author    time          version      desc</p>
 * <p> zxqing    10/8/31       1.0          build this moudle </p>
 *     
 */
public class ReportMasterServlet  extends javax.servlet.http.HttpServlet{

	private static final long serialVersionUID = 8082401760848997575L;

	/**
	 * 生成报表
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
	   String basePath = FileUtil.getWebPath();	   
	   String reportName = request.getParameter("reportName");
	   String className  = request.getParameter("className");
	   String usercode = (String)request.getSession().getAttribute("usercode");
	   
	   if (StringUtil.isEmpty(usercode)){
		   return;
	   }
	   
	   String destFileName="";
	   
	   HashMap<String,Object> parasMap = new HashMap<String,Object>();	   
	   parasMap.put("basePath",basePath);
	   parasMap.put("usercode", usercode);
	   try{   
		   Enumeration er = request.getParameterNames();
		   while (er.hasMoreElements()){
		   	  String paraName = (String)er.nextElement();
		   	  String paraVal  = request.getParameter(paraName);		   	
		      parasMap.put(paraName,paraVal);
		   }
	   }catch(Exception e){
       	  e.printStackTrace();
       }
	   
	   
       try{
           ReportMasterImpl reportMasterImpl= new ReportMasterImpl();
          destFileName =   reportMasterImpl.makeReport(parasMap); 

       }catch(Exception e){
       	  e.printStackTrace();
       }
    // response.sendRedirect("/reports/out/ReportOutput.jsp?docPath="+destFileName); 
       
	   try{	   	 
	   	  FileInputStream in = new FileInputStream(basePath+destFileName);	   	     
	   	  javax.servlet.ServletOutputStream out = response.getOutputStream();
	      // setContentType设置MIME类型 PDF文件为"application/pdf"，WORD文件为："application/msword"，EXCEL文件为："application/vnd.ms-excel"。  	   	  
          response.setContentType("application/vnd.ms-excel");
          response.setContentLength(in.available());           
          //setHeader设置打开方式，具体为：inline为在浏览器中打开，attachment单独打开。           
          response.setHeader("Content-Disposition", "inline;filename=\""+reportName+".xls\";");
          
          byte[] P_Buf = new byte[8192];
		  int i;
		  while ((i = in.read(P_Buf)) != -1) {
		  	out.write(P_Buf, 0, i);
		  }
		  in.close();
		  out.flush(); 
		  out.close();
	   }catch (IOException e){
          e.printStackTrace();
       }
	   
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request,response);
	}

}
