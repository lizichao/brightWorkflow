<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="org.jdom.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<% request.setCharacterEncoding("GB2312"); %>
<link href="/css/style.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="/js/checkForm.js"></Script>
<table width="99%" border="0"  cellspacing="0" align="center">
<tr height="1" class="toolbar" valign="right" border=0>
<td valign="bottom" align="left">��ѯ�����
��<font color='#008000'><b> <bc:value name="/DataPacket/Response/Data/PageInfo/RecordCount"/> </b></font>����¼��
��<font color='#008000'><b> <bc:value name="/DataPacket/Response/Data/PageInfo/PageNo"/> </b></font>ҳ��
��<font color='#008000'><b> <bc:value name="/DataPacket/Response/Data/PageInfo/PageCount"/> </b></font>ҳ��
</td>
<input type="hidden" name="PageNo" value="<bc:value name="/DataPacket/Response/Data/PageInfo/PageNo"/>">
<input type="hidden" name="PageCount" value="<bc:value name="/DataPacket/Response/Data/PageInfo/PageCount"/>">
<td align="right">
��<input type="text" name="go" value="<bc:value name="/DataPacket/Response/Data/PageInfo/PageNo"/>" class="inputtext" size=1 maxlength="8" customtype="int" displayname="����ҳ��">ҳ&nbsp;&nbsp;<a href="#" onClick="goPage();"><b>GO</b></a>

<bc:with name="/DataPacket/Response/Data/PageInfo">
<bc:if name='PageCount[text()="0" or text()="1"]'>
<img src="/images/ICON_FIRST.gif" alt="��ҳ" align="absmiddle" border=0>
<img src="/images/ICON_PRIOR.gif" alt="��ҳ" align="absmiddle" border=0>
<img src="/images/ICON_NEXT.gif" alt="��ҳ" align="absmiddle" border=0>
<img src="/images/ICON_LAST.gif" alt="ĩҳ" align="absmiddle" border=0>
</bc:if><bc:else>
<bc:if name='PageNo[text()="1"]'>
<img src="/images/ICON_FIRST.gif" alt="��ҳ" align="absmiddle" border=0>
<img src="/images/ICON_PRIOR.gif" alt="��ҳ" align="absmiddle" border=0>
<a href="#" onClick="javascript:next();"><img src="/images/ICON_NEXT.gif" alt="��ҳ" align="absmiddle" border=0></a>
<a href="#" onClick="javascript:last();"><img src="/images/ICON_LAST.gif" alt="ĩҳ" align="absmiddle" border=0></a>
</bc:if><bc:else>
<bc:if name=".[PageNo=PageCount]">
<a href="#" onClick="javascript:first();"><img src="/images/ICON_FIRST.gif" alt="��ҳ" align="absmiddle" border=0></a>
<a href="#" onClick="javascript:prior();"><img src="/images/ICON_PRIOR.gif" alt="��ҳ" align="absmiddle" border=0></a>
<img src="/images/ICON_NEXT.gif" alt="��ҳ" align="absmiddle" border=0>
<img src="/images/ICON_LAST.gif" alt="ĩҳ" align="absmiddle" border=0>
</bc:if><bc:else>
<a href="#" onClick="javascript:first();"><img src="/images/ICON_FIRST.gif" alt="��ҳ" align="absmiddle" border=0></a>
<a href="#" onClick="javascript:prior();"><img src="/images/ICON_PRIOR.gif" alt="��ҳ" align="absmiddle" border=0></a>
<a href="#" onClick="javascript:next();"><img src="/images/ICON_NEXT.gif" alt="��ҳ" align="absmiddle" border=0></a>
<a href="#" onClick="javascript:last();"><img src="/images/ICON_LAST.gif" alt="ĩҳ" align="absmiddle" border=0></a>
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
function deleteSelected()
{
    if (!checkSelected(document.forms[0]))
    {
        alert("��ѡ��Ҫɾ�������ݣ�");
        return false;
    }
    if (confirm("��ȷ��Ҫɾ��ѡ�е����ݣ�"))
    {
        doDelete();
    }
}
//function update()
//{
//    document.getElementById('action').value='update';
//    document.forms[0].submit();
//}
function selectAll()
{
  var oForm = document.forms[0];
  if (!selected)
  {
      for (var i=0;i<oForm.all.tags("input").length;i++){
        var ele = oForm.all.tags("input")[i];
        var ct = ele.getAttribute("type");
        if ((ele.type=="checkbox") && (!ele.disabled))
          ele.checked = true;
      }
      selected = true;
      document.getElementById("selectall").src = '/images/ICON_SELECTNONE.gif';
  }
  else
  {
      for (var i=0;i<oForm.all.tags("input").length;i++){
        var ele = oForm.all.tags("input")[i];
        var ct = ele.getAttribute("type");
        if ((ele.type=="checkbox") && (!ele.disabled))
          ele.checked = false;
      }
      selected = false;
      document.getElementById("selectall").src = '/images/ICON_SELECTALL.gif';
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