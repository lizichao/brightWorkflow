<%@ page contentType="text/html; charset=utf8" %><%@ page import="java.util.*,org.jdom.*,cn.brightcom.jraf.util.*,cn.brightcom.jraf.web.*,cn.brightcom.jraf.report.*,net.sf.jasperreports.engine.*" %><%
	
	response.setCharacterEncoding("utf-8");
	
	String rname       = JReportUtil.getCurrentRepName();
	List jps           = JReportUtil.getCurrentPrint();
	String type = request.getParameter("rptype");
	
	byte[] bts = JReportUtil.genReport(jps,type);
	if(null == bts)
	{
		out.print("Error");
		return;
	}
	if(StringUtil.isNotEmpty(type))
	{
		String conTp = null;
		if("pdf".equals(type)) conTp = "application/pdf";
		if("xls".equals(type)) conTp = "application/vnd.ms-excel";
		if("doc".equals(type)) conTp = "application/vnd.ms-word";
		if("jrpxml".equals(type)) conTp = "text/xml";
		
		SmartUpload su = new SmartUpload();
        su.initialize(pageContext);
		su.setContentDisposition(null);
		su.downloadBytes(bts,"inline", conTp, rname+"."+type);
	}
	else
	{
		ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(bts, 0, bts.length);
        ouputStream.flush();
        ouputStream.close();
	}
%>