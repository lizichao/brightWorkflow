<%@ page contentType="text/html; charset=UTF-8" %><%@ page import="cn.brightcom.jraf.cache.CacheFactory"%><%@ page import="org.jdom.*" %><%@ page import="cn.brightcom.jraf.web.SmartUpload" %>
2  <%@ page import="java.io.OutputStream" %>
<%
System.out.println("----------3333333333333333333333333333");
  Document xmlDoc = (Document) request.getAttribute("xmlDoc");
    String fileName=null;
    String filePath=null;
	if(null != xmlDoc)
    {
    	Element record = xmlDoc.getRootElement().getChild("Response").
                    getChild("Data").getChild("Record");
        fileName = record.getChildText("name");
        String sdoc = record.getChildText("downfile");
        byte[] doc = (byte[])CacheFactory.get(sdoc);
        SmartUpload su = new SmartUpload();
        su.initialize(pageContext);
		su.setContentDisposition(null);
        if(null != doc)
        {
        	su.downloadBytes(doc,null, null, fileName);
        }
    	else
    	{
    		su.downloadFile(sdoc,null,fileName);
    	}
    }
	else
	{
		fileName = (String)request.getSession().getAttribute("filename");
		filePath = (String)request.getSession().getAttribute("filepath");
		System.out.println("----------fileName"+fileName);
		System.out.println("----------filePath"+filePath);
		if(null != fileName)
		{
			SmartUpload su = new SmartUpload();
        	su.initialize(pageContext);
			su.setContentDisposition(null);
        	su.downloadFile(filePath,null,fileName);
		}
		else
		{
			String cacheFile = (String)request.getSession().getAttribute("cachefilekey");
			if (cacheFile!=null && !cacheFile.equals("")) {

				byte[] doc = (byte[])CacheFactory.get(cacheFile);
				SmartUpload su = new SmartUpload();
				su.initialize(pageContext);
				su.downloadBytes(doc,null, null, "");
			} else {
				fileName = request.getParameter("filename");
				filePath = request.getParameter("filepath");
				SmartUpload su = new SmartUpload();
				su.initialize(pageContext);
				su.setContentDisposition(null);
				su.downloadFile(filePath,null,fileName);
			}
		}
		 
		   // response.reset();
	        out.clear();
	        out=pageContext.pushBody();
		//out.clear();
           //　  out= pageContext.pushBody();
		//request.getSession().removeAttribute("filename");
		//request.getSession().removeAttribute("filepath");
	}
%>
<script>
alert("33")
</script>