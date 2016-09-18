
<%@page import="java.net.URLDecoder"%><%@ page language="java" contentType="application/vnd.ms-excel; charset=utf-8" pageEncoding="utf-8" %>


<%!
	private String toUtf8(String s){
	 	StringBuffer sb = new StringBuffer();
       	for (int i=0;i<s.length();i++){
          char c = s.charAt(i);
          if (c >= 0 && c <= 255){sb.append(c);}
          else{
              byte[] b;
              try { b = Character.toString(c).getBytes("utf-8");}
              catch (Exception ex) {
                  System.out.println(ex);
                  b = new byte[0];
              }
              for (int j = 0; j < b.length; j++) {
                  int k = b[j];
                  if (k < 0) k += 256;
                  sb.append("%" + Integer.toHexString(k).toUpperCase());
              }
          }
      	}
       	return sb.toString();
	}
%>
<%
	request.setCharacterEncoding("utf-8");
	String reqString = request.getParameter("content");
	String filename = request.getParameter("filename");
try{
	response.setContentType("application/vnd.ms-excel");
	response.addHeader("Content-Disposition", "attachment; filename=\"" + toUtf8(filename)  +   ".xls\"");
	out.print ( reqString );	 
}catch(Exception e){
//IF the image data is mal-formatted.
out.print("");
out.close();
}
%>