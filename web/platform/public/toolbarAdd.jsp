<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="org.jdom.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<% request.setCharacterEncoding("GB2312"); %>
<link href="/css/style.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="/js/checkForm.js"></Script>
<table width="100%" border=0  cellspacing="0">
<tr height="1" class="toolbar" valign="right" border=0>
<td align="left">查询结果：
共<font color='#FF0000'><b> <bc:value name="/DataPacket/Response/Data/PageInfo/RecordCount"/> </b></font>条记录，
第<font color='#FF0000'><b> <bc:value name="/DataPacket/Response/Data/PageInfo/PageNo"/> </b></font>页，
共<font color='#FF0000'><b> <bc:value name="/DataPacket/Response/Data/PageInfo/PageCount"/> </b></font>页。
</td>
<input type="hidden" name="PageNo" value="<bc:value name="/DataPacket/Response/Data/PageInfo/PageNo"/>">
<input type="hidden" name="PageCount" value="<bc:value name="/DataPacket/Response/Data/PageInfo/PageCount"/>">
<td align="right">
到<input type="text" name="go" value="" class="inputtext" size=1 maxlength="8" customtype="int" displayname="输入页码">页&nbsp;&nbsp;<a href="#" onClick="goPage();"><b>GO</b></a>
<a href="#" onClick="javascript:add();"><img src="/images/ICON_NEW.gif" alt="新增" align="absmiddle" border=0></a>&nbsp;&nbsp;
<a href="#" onClick="javascript:del();"><img src="/images/ICON_DELETE.gif" alt="删除" align="absmiddle" border=0></a>
<bc:with name="/DataPacket/Response/Data/PageInfo">
<bc:if name='PageCount[text()="0" or text()="1"]'>
<img src="/images/ICON_FIRST.gif" alt="首页" align="absmiddle" border=0>
<img src="/images/ICON_PRIOR.gif" alt="上页" align="absmiddle" border=0>
<img src="/images/ICON_NEXT.gif" alt="下页" align="absmiddle" border=0>
<img src="/images/ICON_LAST.gif" alt="末页" align="absmiddle" border=0>
</bc:if><bc:else>
<bc:if name='PageNo[text()="1"]'>
<img src="/images/ICON_FIRST.gif" alt="首页" align="absmiddle" border=0>
<img src="/images/ICON_PRIOR.gif" alt="上页" align="absmiddle" border=0>
<a href="#" onClick="javascript:next();"><img src="/images/ICON_NEXT.gif" alt="下页" align="absmiddle" border=0></a>
<a href="#" onClick="javascript:last();"><img src="/images/ICON_LAST.gif" alt="末页" align="absmiddle" border=0></a>
</bc:if><bc:else>
<bc:if name=".[PageNo=PageCount]">
<a href="#" onClick="javascript:first();"><img src="/images/ICON_FIRST.gif" alt="首页" align="absmiddle" border=0></a>
<a href="#" onClick="javascript:prior();"><img src="/images/ICON_PRIOR.gif" alt="上页" align="absmiddle" border=0></a>
<img src="/images/ICON_NEXT.gif" alt="下页" align="absmiddle" border=0>
<img src="/images/ICON_LAST.gif" alt="末页" align="absmiddle" border=0>
</bc:if><bc:else>
<a href="#" onClick="javascript:first();"><img src="/images/ICON_FIRST.gif" alt="首页" align="absmiddle" border=0></a>
<a href="#" onClick="javascript:prior();"><img src="/images/ICON_PRIOR.gif" alt="上页" align="absmiddle" border=0></a>
<a href="#" onClick="javascript:next();"><img src="/images/ICON_NEXT.gif" alt="下页" align="absmiddle" border=0></a>
<a href="#" onClick="javascript:last();"><img src="/images/ICON_LAST.gif" alt="末页" align="absmiddle" border=0></a>
</bc:else>
</bc:else>
</bc:else>
</bc:with>
</td>
</tr>
</table>
<Script LANGUAGE="JavaScript">
var selected = false;
function first()
{
    document.getElementById('PageNo').value=1;
    document.forms[0].submit();
}
function last()
{
    document.getElementById('PageNo').value=document.getElementById('PageCount').value;
    document.forms[0].submit();
}
function prior()
{
    document.getElementById('PageNo').value=parseInt(document.getElementById('PageNo').value)-1;
    document.forms[0].submit();
}
function next()
{
    document.getElementById('PageNo').value=parseInt(document.getElementById('PageNo').value)+1;
    document.forms[0].submit();
}
function goPage()
{
    if (checkForm(document.forms[0]))
    {
        document.getElementById('PageNo').value=document.getElementById('go').value;
        document.forms[0].submit();
    }
}

function mOvr(src,clrOver){
	if (!src.contains(event.fromElement)) {
		src.style.cursor = 'hand';
		src.bgColor = clrOver;
	}
}
function mOut(src,clrIn)  {
	if (!src.contains(event.toElement)) {
		src.style.cursor = 'default';
		src.bgColor = clrIn;
	}
}
function checkSelected(oForm){
  window.event.returnValue = false;

  for (var i=0;i<oForm.all.tags("input").length;i++){
    var ele = oForm.all.tags("input")[i];
    var ct = ele.getAttribute("type");
    if ((ele.type=="checkbox")&&(ele.checked==true))
      return true;
  }
  return false;
}
</Script>