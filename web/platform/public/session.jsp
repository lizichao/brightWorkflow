<%@ page contentType="application/x-javascript; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
_jraf_session={PageSize:10};
<%
	String pg = (String)session.getAttribute("PageSize");
	String classid = (String)session.getAttribute("Classid");
	if(null!=pg) out.print("_jraf_session['PageSize']="+("".equals(pg)?"15":pg)+";");
	Document reqXml = HttpProcesser.createRequestPackage("pcmc","session","get",request);
	Document xmlDoc = SwitchCenter.doPost(reqXml);
	Element data = xmlDoc.getRootElement().getChild("Response").getChild("Data");
	Element record = null==data?null:data.getChild("Record");
	if(null != record)
	{
		List f = record.getChildren();
    	for(int i=0; i<f.size(); i++)
    	{
    		Element ele = (Element)f.get(i);
    		String name = ele.getName();
    		String val = ele.getText();
    		if("pagesize".equals(name))
    		{
    		  val = ("".equals(val) || val==null)?"10":val;
    			out.print("_jraf_session['PageSize']="+val+";");
    		}
    		else
    		{
    			out.print("_jraf_session['"+name+"']='"+val+"';");
    		}
    	}
        out.print("_jraf_session['classid']='"+classid+"';");
	}
%>
Jraf_ContextPath='<%=request.getContextPath()%>';
JrafSession={};
JrafSession.get=function(nm){
	return _jraf_session[nm];
}
JrafSession.load=function(){
	var jr = new BcRequest('pcmc','session','get');
	jr.setValidFn(function(){return true});
	jr.setSuccFn(function(r){_jraf_session=Ext.apply({PageSize:10},r.records[0].data);});
	jr.setAsync(false);
	jr.postData();
};