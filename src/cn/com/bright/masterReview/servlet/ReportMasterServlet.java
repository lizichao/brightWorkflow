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
 * <p>Title:�������</p>
 * <p>Description: �������</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: BrightCom Technologies.</p>
 * @author zxqing
 * @version 1.0
 * 
 * <p>Function List // ��Ҫ�������书��</p>
 * <p>     ǰ̨���б���ͨ����servlet�������,���ñ�servlet��url����</p>
 * <p>     ReportService?reportName=������&className=����(��ѡ)&��������</p>
 * <p>    ����reportName��������,�����Ĳ�������ҵ�������Լ�����</p>
 *      
 * <p>History: // ��ʷ�޸ļ�¼:</p>
 * <p> author    time          version      desc</p>
 * <p> zxqing    10/8/31       1.0          build this moudle </p>
 *     
 */
public class ReportMasterServlet  extends javax.servlet.http.HttpServlet{

	private static final long serialVersionUID = 8082401760848997575L;

	/**
	 * ���ɱ���
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
	      // setContentType����MIME���� PDF�ļ�Ϊ"application/pdf"��WORD�ļ�Ϊ��"application/msword"��EXCEL�ļ�Ϊ��"application/vnd.ms-excel"��  	   	  
          response.setContentType("application/vnd.ms-excel");
          response.setContentLength(in.available());           
          //setHeader���ô򿪷�ʽ������Ϊ��inlineΪ��������д򿪣�attachment�����򿪡�           
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
