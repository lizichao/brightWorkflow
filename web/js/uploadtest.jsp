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
				fileSize : 1024*550,//限制文件大小
				uploadUrl :'/p.ajaxutf',
				filePostName : 'file', //后台接收参数
				fileTypes : '*.*',//可上传文件类型
				file_upload_limit:"0",
				fileTypesDescription:'所有文件',
				postParams : {'sysName':'tvote','oprID':'upload','actions':'file','sss':'测试'} //
			}
		]
	}).show();
});
</script>
  </body>
</html>
