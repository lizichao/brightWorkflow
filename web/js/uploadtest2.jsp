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
		height : 600,
		items : [{
  xxtype : "Jpanel",
  xtype : "form",
  id:"formid",
  classname : "itemPanel",
  items : [    {
      name : "c_name",
      fieldLabel : "����",
      allowBlank : false,
      xtype : "textfield"
  },    {
      name : "c_desc",
      fieldLabel : "˵��",
      allowBlank : true,
      xtype : "textarea",
      width : 220
  },    
  	{
		xtype:'uploadpanel',
		border : false,
		width : 500,
		itemId : 'uploadPanel',
		height : 150,
		fieldLabel: 'ͼ��',
		fileSize : 1024*550,//�����ļ���С
		uploadUrl :'/p.ajaxutf',
		filePostName : 'file', //��̨���ղ���
		fileTypes : '*.*',//���ϴ��ļ�����
		file_upload_limit : '1',
		fileTypesDescription:'�����ļ�',
		upBtn:false,
		stopBtn:false,
		successFn:function(a){
			alert(a.errmsg);
		}
	},
  {
      name : "c_jsurl",
      fieldLabel : "�������",
      allowBlank : true,
      xtype : "textfield",
      width : 220
  }],
  buttonAlign:'center',
  buttons : [{
	text : '����',
	handler : function() {
		var _form=this.ownerCt.ownerCt;
		var up = _form.getComponent('uploadPanel');
		var ps = Ext.apply({'sysName':'tvote','oprID':'upload','actions':'file'},JrafUTIL.crForm(_form)||{});
		up.startUpload(this,null,ps);
	}
  }]
}
			
		]
	}).show();
});
</script>
  </body>
</html>
