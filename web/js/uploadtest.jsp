<%@ page contentType="text/html; charset=GBK" %>
<html>
  <head>    
    <title>upload test</title>
	<%@ include file="/platform/public/includejs.jsp"%>
  </head>  
  <body>
<script language="JavaScript">
Ext.onReady(function(){
	Ext.QuickTips.init();
	new Ext.Window({
		width : 650,
		title : 'swfUpload test',
		height : 300,
		layout : 'fit',
		items : [
			{
				xtype:'uploadpanel',
				border : false,
				fileSize : 1024*550,//�����ļ���С
				uploadUrl :'/p.ajaxutf',
				filePostName : 'file', //��̨���ղ���
				fileTypes : '*.*',//���ϴ��ļ�����
				file_upload_limit:"0",
				fileTypesDescription:'�����ļ�',
				postParams : {'sysName':'tvote','oprID':'upload','actions':'file','sss':'����'} //
			}
		]
	}).show();
});
</script>
  </body>
</html>
