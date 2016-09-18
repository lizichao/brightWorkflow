<%@ page contentType="text/xml; charset=GBK" %><%@ page import="org.jdom.*" %><%
    String xml = null;
    boolean notNull = false;
    Document xmlDoc = (Document)request.getAttribute("xmlDoc");
    
    if (xmlDoc!= null)
    {
        Element data = xmlDoc.getRootElement().getChild("Response").getChild("Data");
        xml = data.getChild("Record").getChildTextTrim("xmldoc");
        if(null != xml && xml.trim().length()>0)
        	notNull = true;
    }
    
    if(!notNull)
    {
%>
<?xml version="1.0" encoding="GBK"?>
<MESSAGE>
无XML数据！！
</MESSAGE>
<%
    }
    else
    {
    	out.print(xml);
    }
%>