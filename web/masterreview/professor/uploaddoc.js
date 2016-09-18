Ext.namespace("jsp.masterreview.professor");jsp.masterreview.professor.uploaddoc=function(){var __caches__=[];this.__caches__=__caches__;
   function impData(rec){
   var xlsWin = MainPanel['xlswin'];
   if(!xlsWin){
		xlsWin = new Ext.Window({
			title:'���ݵ���',
	        layout:'fit',
	        width:535,
	        height:320,
	        closeAction:'hide',
	        plain: true,
	        modal: true,
	        items:xlsForm
		});
		MainPanel['xlswin']=xlsWin;
		__caches__.push(xlsWin);
   }
     xlsWin.show();
 };
 function getUrlRender(value, meta, rec, rowIdx, colIdx, ds){
 	var returnStr = "<a href=\""+rec.get("doc_file_url")+"\");' title=\""+rec.get("doc_title")+"\" target=\"_blank\" >"+rec.get("doc_title")+"</a>";
	return returnStr;	
 }
 var docRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "doc_id",
    fieldLabel : "����",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "doc_title",
    fieldLabel : "�ļ���",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "doc_size",
    fieldLabel : "�ļ���С",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "doc_type",
    fieldLabel : "�ļ�����",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "upload_by",
    fieldLabel : "�ϴ��û�ID",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "upload_date",
    fieldLabel : "�ϴ�ʱ��",
    type : "date",
    allowBlank : true,
    dateFormat : "Y-m-d H:i:s"
},  {
    xtype : "Field",
    name : "doc_status",
    fieldLabel : "����״̬",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "class_id",
    fieldLabel : "�༶ID",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "doc_file_url",
    fieldLabel : "�ļ�·��",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "doc_remark",
    fieldLabel : "��ע",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "deptid",
    fieldLabel : "��λID",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "doc_flag",
    fieldLabel : "�ļ����",
    type : "string",
    allowBlank : true
}]);
this.docRecord=docRecord;this.__caches__.push(docRecord);var nodeRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "sid",
    fieldLabel : "sid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "pid",
    fieldLabel : "pid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "deptid",
    fieldLabel : "deptid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "scode",
    fieldLabel : "scode",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "gcode",
    fieldLabel : "gcode",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "sname",
    fieldLabel : "sname",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "stype",
    fieldLabel : "stype",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "isleaf",
    fieldLabel : "isleaf",
    type : "string",
    allowBlank : true
}]);
this.nodeRecord=nodeRecord;this.__caches__.push(nodeRecord);var docStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "docStore",
  recordType : docRecord,
  idProperty : "doc_id",
  api : {
  	read:{sysName:"yuexue",oprID:"doc",actions:"getListDoc"}
  },
  autoLoad : true,
  autoSave : false,
  baseParams : {paramname:'qry_docFlag'},
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.docStore=docStore;this.__caches__.push(docStore);var xlsForm=Ext.create({
	  xtype : "form",
	  classname : "xlsForm",
	  fileUpload : true,
	  items : [  {
		xtype:'uploadpanel',
		itemId : 'uploadPanel',
		border : false,
		fileSize : 5024*3500,//�����ļ���С
		uploadUrl :'/p.ajaxutf',
		filePostName : 'doc_file', //��̨���ղ���
		fileTypes : '*.xls',//���ϴ��ļ�����
		file_upload_limit:"5",
		height : 210,
		upBtn:false,
		stopBtn:false,
		fileTypesDescription:'�����ļ�',
		successFn:function(a){	
		   var xlsWin = MainPanel['xlswin'];
			xlsWin.hide();
		}		
		}],
	  frame : true,
	  buttonAlign:'center',
	  buttons : [{
		text : '�� ��',
		handler : function() {
			var _form=this.ownerCt.ownerCt;
			
			var up = _form.getComponent('uploadPanel');
			var ps = Ext.apply(
			   {'sysName':'yuexue','oprID':'doc','actions':'importDocFile'},
			   JrafUTIL.crParams([{doc_flag:"ImportProfessor"}])
			 );
			up.startUpload(this,null,ps);
		}
  }]
},'panel');
this.xlsForm=xlsForm;this.__caches__.push(xlsForm);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    layout : "border",
    frame : true,
    bodyBorder : false,
    items : [      {
        xtype : "form",
        region : "north",
        bodyBorder : false,
        height : 65,
        labelAlign : "right",
        items : [          {
            layout : "column",
            bodyBorder : false,
            anchor : "100%",
            items : [           /*    {
                layout : "form",
                columnWidth : 0.65,
                border : false,
                items : [                  {
                    xtype : "depttreecombo",
                    fieldLabel : "����ѧУ",
                    name : "qry_deptid",
                    hiddenName : "qry_deptid",
                    valueField : "deptid",
                    displayField : "deptname",
                    triggerAction : "all",
                    width : 350,
                    lazyRender : true,
                    editable : false,
                    forceSelection : true
                }]
            },  */                  {
                layout : "form",
                columnWidth : 0.35,
                bodyBorder : false,
                items : [                  {
                    name: "qry_docStatus",
                    fieldLabel: "����״̬",
                    allowBlank: true,
                    width: 160,
                    xtype: "paracombo",
                    hiddenName: "qry_docStatus",
                    baseParams: {
                        paramname: 'doc_status'
                    }
                }]
            }]
        }],
        buttonAlign : "center",
        buttons : [{
			  text : '��ѯ', handler : function() {
			  var  searchForm = this.ownerCt.ownerCt
				  if(searchForm.getForm().isValid()) {
					docStore.setFormParam(this.ownerCt.ownerCt);
					docStore.setPageInfo(JrafSession.get('PageSize'),'1');
					docStore.setBaseParam('qry_docFlag',"ImportProfessor");
					docStore.load();
				  }
			}
		}]
    },      {
        xtype : "grid",
        frame : true,
        viewConfig : {forceFit:false},
        columnLines : true,
        autoHeight : false,
        height : 320,
        title : "��Ϣ�����б�",
        store : docStore,
        tbar : [
           {text:'���ݵ���',iconCls:'page-excel',ref: '../impBtn',handler:function(){impData();}},
           {xtype: 'tbseparator'},
           {xtype: 'tbfill'},
           {xtype: 'tbseparator'},
           {text:'����ģ������',
             iconCls:'page-excel',
             ref: '../impBtn',
             handler:function(){
                 var url="/reports/model/professor_info.xls";window.open(url);
              }
           }],
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:docStore,displayInfo: true}),
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
		{width:200,sortable:true,header:'�ļ�����',dataIndex:'doc_title',renderer:getUrlRender},
		{width:130,sortable:true,header:'�ļ�����',dataIndex:'doc_type'},
		{width:80,sortable:true,header:'�ļ���С(KB)',dataIndex:'doc_size'},
		{width:80,sortable:true,header:'����״̬',dataIndex:'doc_status',renderer:Ext.util.Format.paramRenderer('doc_status')},
		{width:250,sortable:true,header:'��ע',dataIndex:'doc_remark'},
		{width:115,sortable:true,header:'�ϴ�ʱ��',dataIndex:'upload_date',renderer:Ext.util.Format.dateRenderer('Y-m-d')}]),
        region : "center"
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};