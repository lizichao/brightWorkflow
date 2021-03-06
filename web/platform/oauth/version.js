Ext.namespace("jsp.platform.oauth");jsp.platform.oauth.version=function(){var __caches__=[];this.__caches__=__caches__;function showVer(rec){
	var win=MainPanel['verwin'];
	if(!win){
		win=new Ext.Window({
			title:'版本信息',
	        layout:'fit',
	        width:700,
	        height:500,
	        closeAction:'hide',
	        plain: true,
	        modal: true,
	        items:verPanel
	    });
	    MainPanel['verwin']=win;
	    __caches__.push(win);
	}
	win.show();
	verPanel.record=rec;
	verPanel.getForm().reset();
	verPanel.getForm().loadRecord(rec);
	verPanel.getForm().findField("uploadfile").reset();
};var verRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "version_id",
    fieldLabel : "VersionID",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "client_id",
    fieldLabel : "ClientID",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "version_code",
    fieldLabel : "版本号",
    type : "int",
    allowBlank : false
},  {
    xtype : "Field",
    name : "version_name",
    fieldLabel : "版本名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "file_name",
    fieldLabel : "文件名",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "version_type",
    fieldLabel : "版本类型",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "description",
    fieldLabel : "更新说明",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "url",
    fieldLabel : "下载地址",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "create_time",
    fieldLabel : "创建时间",
    type : "date",
    allowBlank : true,
    dateFormat : "Y-m-d H:i:s"
},  {
    xtype : "Field",
    name : "state",
    fieldLabel : "状态",
    type : "string",
    allowBlank : true
}]);
this.verRecord=verRecord;this.__caches__.push(verRecord);var clientRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "client_id",
    fieldLabel : "ClientID",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "app_name",
    fieldLabel : "应用名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "app_type",
    fieldLabel : "应用类型",
    type : "string",
    allowBlank : true
}]);
this.clientRecord=clientRecord;this.__caches__.push(clientRecord);var verStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "verStore",
  recordType : verRecord,
  idProperty : "version_id",
  api : {read:{sysName:"pcmc",oprID:"oauth2_client",actions:"listVer"},destroy:{sysName:"pcmc",oprID:"oauth2_client",actions:"delVer"}},
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false,
  autoLoad : true
},'Store');
this.verStore=verStore;this.__caches__.push(verStore);var clientStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "clientStore",
  recordType : clientRecord,
  idProperty : "client_id",
  api : {read:{sysName:"pcmc",oprID:"oauth2_client",actions:"listClient"}},
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false,
  autoLoad : true,
  baseParams : {PageSize:-1,state:"1"}
},'Store');
this.clientStore=clientStore;this.__caches__.push(clientStore);var verPanel=Ext.create({
  xxtype : "Jpanel",
  xtype : "form",
  classname : "verPanel",
  fileUpload : true,
  items : [    {
      name : "version_id",
      fieldLabel : "VersionID",
      allowBlank : true,
      xtype : "hidden"
  },    {
      name : "client_id",
      fieldLabel : "客户端",
      allowBlank : false,
      editable : false,
      xtype : "combo",
      width : 150,
      store : clientStore,
      triggerAction : "all",
      displayField : "app_name",
      valueField : "client_id",
      hiddenName : "client_id",
      mode : "local"
  },    {
      name : "version_code",
      fieldLabel : "版本号",
      allowBlank : false,
      allowDecimals:false,
      allowNegative:false,
      xtype : "numberfield"
  },    {
      name : "version_name",
      fieldLabel : "版本名称",
      allowBlank : false,
      xtype : "textfield"
  },    {
      name : "file_name",
      fieldLabel : "文件名",
      allowBlank : false,
      xtype : "textfield"
  },    {
      name : "version_type",
      fieldLabel : "版本类型",
      allowBlank : true,
      xtype : "textfield"
  },    {
      name : "description",
      fieldLabel : "更新说明",
      allowBlank : true,
      xtype : "textarea",
      width : 327,
      height : 123
  },    {
      name : "url",
      fieldLabel : "下载地址",
      xtype : "hidden",
      allowBlank : true
  },    {
      xtype : "fileuploadfield",
      emptyText : "选择上传文件",
      fieldLabel : "版本文件",
      name : "uploadfile",
      buttonText : "选择文件",
      width : 260
  },    {
      xtype : "combo",
      fieldLabel : "状态",
      allowBlank : false,
      name : "state",
      hiddenName : "state",
      valueField : "v",
      displayField : "t",
      triggerAction : "all",
      mode : "local",
      store : new Ext.data.ArrayStore({id: 0,fields: ['v','t'],
      data: [['1', '有效'],['0','无效']]}),
      width : 80
  }],
  buttonAlign : "center",
  buttons : [{
		text : '保存', handler : function() {
			var _form=this.ownerCt.ownerCt;
			var rec=_form.record;
			var jr;
    	    if(rec.phantom){
                jr=new JrafRequest('pcmc','oauth2_client','addVer',{recordType:verRecord,idProperty:'version_id'});
            }else{
                jr=new JrafRequest('pcmc','oauth2_client','uptVer',{recordType:verRecord,idProperty:'version_id'});
            }
			jr.setForm(_form);
            jr.setSuccFn(function(a,_resp,xr){
                _form.getForm().updateRecord(rec);
                xr.realize(rec,a.records);
            });
            jr.postData();
		}
	},{
		text : '关闭', handler : function() {
			this.ownerCt.ownerCt.ownerCt.hide();
		}
	}]
},'panel');
this.verPanel=verPanel;this.__caches__.push(verPanel);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    bodyBorder : false,
    layout : "border",
    items : [      {
        xtype : "form",
        bodyBorder : false,
        region : "north",
        height : 65,
        labelAlign : "right",
        items : [          {
            name : "client_id",
            fieldLabel : "客户端",
            editable : false,
            xtype : "combo",
            width : 150,
            store : clientStore,
            triggerAction : "all",
            displayField : "app_name",
            valueField : "client_id",
            hiddenName : "client_id",
            mode : "local"
        }],
        buttonAlign : "center",
		frame:true,
        buttons : [{
			text : '查询', handler : function() {
				verStore.setFormParam(this.ownerCt.ownerCt);
				verStore.setPageInfo(JrafSession.get('PageSize'),'1');
				verStore.load();
			}
		}]
    },      {
        xtype : "grid",
        frame : true,
        viewConfig : {forceFit:true},
        columnLines : true,
        autoHeight : false,
        height : 320,
        title : "版本信息",
        store : verStore,
        tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
		        var _grid=this.ownerCt.ownerCt;
		        var recordType=_grid.getStore().recordType;
		        var nr=new recordType();
		        _grid.getStore().add(nr);
		        _grid.getSelectionModel().selectLastRow();
		        var rec=_grid.getSelectionModel().getSelected();
		        showVer(rec);
        }},
{xtype: 'tbseparator'},
{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
          var _grid=this.ownerCt.ownerCt;
          var ckrs=_grid.getSelectionModel().getSelections();
          for(var i=0;i<ckrs.length;i++){
            _grid.getStore().remove(ckrs[i]);}
            Ext.Msg.confirm('删除版本','确认删除选择版本?',function(btn){
            if(btn == 'yes'){
		       _grid.getStore().save();
	       }});
	}}],
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:verStore,displayInfo: true}),
        colModel : new Ext.grid.ColumnModel([new JrafRowNumberer(),
{width:40,sortable:true,header:'客户端',dataIndex:'client_id',renderer:Ext.util.Format.storeRenderer(clientStore,"client_id","app_name")},
{width:40,sortable:true,header:'版本号',dataIndex:'version_code'},
{width:40,sortable:true,header:'版本名称',dataIndex:'version_name'},
{width:40,sortable:true,header:'版本类型',dataIndex:'version_type'},
{width:40,sortable:true,header:'下载地址',dataIndex:'url'},
{width:40,sortable:true,header:'创建时间',dataIndex:'create_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s')},
{width:40,sortable:true,header:'状态',dataIndex:'state'}]),
        sm: new Ext.grid.RowSelectionModel({singleSelect:true,listeners:{
            selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
        listeners : {
	    	rowdblclick:function(g,rowIndex){
				var rec=g.getSelectionModel().getSelected();
				showVer(rec);
			}
	    },
        region : "center"
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};