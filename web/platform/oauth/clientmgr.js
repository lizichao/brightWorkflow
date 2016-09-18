Ext.namespace("jsp.platform.oauth");jsp.platform.oauth.clientmgr=function(){var __caches__=[];this.__caches__=__caches__;function showClient(rec){
    var win=MainPanel['clientwin'];
	if(!win){
		win=new Ext.Window({
			title:'OAuth客户端',
	        layout:'fit',
	        width:700,
	        height:500,
	        closeAction:'hide',
	        plain: true,
	        modal: true,
	        items:clientForm
	    });
	    MainPanel['clientwin']=win;
	    __caches__.push(win);
	}
	win.show();
	clientForm.getForm().reset();
	clientForm.record=rec;
	clientForm.getForm().loadRecord(rec);
};var clientRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "client_id",
    fieldLabel : "ClientID",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "client_secret",
    fieldLabel : "SecretKey",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "client_type",
    fieldLabel : "类型",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "redirect_uris",
    fieldLabel : "重定向URI",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "default_scopes",
    fieldLabel : "缺省权限",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "app_name",
    fieldLabel : "名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "app_desc",
    fieldLabel : "描述",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "app_uri",
    fieldLabel : "应用URI",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "app_type",
    fieldLabel : "应用分类",
    type : "string",
    allowBlank : true
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
this.clientRecord=clientRecord;this.__caches__.push(clientRecord);var clientStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "clientStore",
  recordType : clientRecord,
  idProperty : "client_id",
  api : {
  	read:{method:'POST',sysName:'pcmc',oprID:'oauth2_client',actions:'listClient'},
  	destroy:{method:'POST',sysName:'pcmc',oprID:'oauth2_client',actions:'delClient'}
  },
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false,
  autoLoad : true
},'Store');
this.clientStore=clientStore;this.__caches__.push(clientStore);var clientForm=Ext.create({
  xxtype : "Jpanel",
  xtype : "form",
  classname : "clientForm",
  items : [    {
      name : "client_id",
      fieldLabel : "ClientID",
      xtype : "textfield",
      allowBlank : true,
      width : 300,
      readOnly : true
  },    {
      name : "client_secret",
      fieldLabel : "SecretKey",
      xtype : "textfield",
      allowBlank : true,
      width : 300,
      readOnly : true
  },    {
      xtype : "combo",
      fieldLabel : "类型",
      name : "client_type",
      hiddenName : "client_type",
      mode : "local",
      store : new Ext.data.ArrayStore({
				        id: 0,
				        fields: ['v','t'],
				        data: [[1, 'confidential'],[0, 'public']]
				    }),
      valueField : "v",
      displayField : "t",
      editable : false,
      triggerAction : "all",
      forceSelection : true,
      allowBlank : false,
      value : 1
  },    {
      name : "redirect_uris",
      fieldLabel : "重定向URI",
      allowBlank : false,
      xtype : "textfield",
      width : 300
  },    {
      name : "default_scopes",
      fieldLabel : "缺省权限",
      allowBlank : true,
      xtype : "textfield",
      width : 300
  },    {
      name : "app_name",
      fieldLabel : "名称",
      allowBlank : false,
      xtype : "textfield",
      width : 300
  },    {
      name : "app_desc",
      fieldLabel : "描述",
      allowBlank : true,
      xtype : "textarea",
      width : 300,
      height : 70
  },    {
      name : "app_uri",
      fieldLabel : "应用URI",
      allowBlank : true,
      xtype : "textfield",
      width : 300
  },    {
      xtype : "combo",
      fieldLabel : "状态",
      name : "state",
      hiddenName : "state",
      mode : "local",
      store : new Ext.data.ArrayStore({
				        id: 0,
				        fields: ['v','t'],
				        data: [[1, '有效'],[0, '无效']]
				    }),
      valueField : "v",
      displayField : "t",
      editable : false,
      triggerAction : "all",
      forceSelection : true,
      allowBlank : false,
      value : 1
  }],
  buttonAlign : "center",
  buttons : [{
    	text : '保存',
    	ref: '../saveBtn',
    	handler : function() {
    	    var _form=this.ownerCt.ownerCt;
    	    var rec=_form.record;
    	    var jr;
    	    if(rec.phantom){
                jr=new JrafRequest('pcmc','oauth2_client','addClient',{recordType:clientRecord,idProperty:'client_id'});
            }else{
                jr=new JrafRequest('pcmc','oauth2_client','uptClient',{recordType:clientRecord,idProperty:'client_id'});
            }
            jr.setForm(_form);
            jr.setSuccFn(function(a,_resp,xr){
                _form.getForm().updateRecord(rec);
                xr.realize(rec,a.records);
                _form.getForm().loadRecord(rec);
            });
            jr.postData();
    	}
    },   {
      text : '关闭',
      handler : function() {
        this.ownerCt.ownerCt.ownerCt.hide();
      }
  }]
},'panel');
this.clientForm=clientForm;this.__caches__.push(clientForm);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "grid",
    frame : true,
    viewConfig : {forceFit:true},
    columnLines : true,
    autoHeight : false,
    height : 320,
    title : "OAuth客户端管理",
    store : clientStore,
    tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType();_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();showClient(rec);}},
{xtype: 'tbseparator'},
{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
    var _grid=this.ownerCt.ownerCt;
    Ext.Msg.confirm('删除数据','确认删除选择的客户端?',function(btn){
					if(btn == 'yes'){var ckrs=_grid.getSelectionModel().getSelections();
					for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}_grid.getStore().save();}})}}],
    bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:clientStore,displayInfo: true}),
    colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
{width:40,sortable:true,header:'ClientID',dataIndex:'client_id'},
{width:40,sortable:true,header:'ClientSecret',dataIndex:'client_secret'},
{width:40,sortable:true,header:'类型',dataIndex:'client_type',renderer:function(v){
		return '0'==v?'public':'confidential';
	}},
{width:40,sortable:true,header:'重定向URI',dataIndex:'redirect_uris'},
{width:40,sortable:true,header:'缺省权限',dataIndex:'default_scopes'},
{width:40,sortable:true,header:'名称',dataIndex:'app_name'},
{width:40,sortable:true,header:'描述',dataIndex:'app_desc'},
{width:40,sortable:true,header:'创建时间',dataIndex:'create_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s')},
{width:40,sortable:true,header:'状态',dataIndex:'state',renderer:function(v){
		return '0'==v?'<font color=red>无效</font>':'有效';
	}}]),
    sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
    listeners : {
  rowdblclick : function(g,rowIndex,e){
	var rec=g.getSelectionModel().getSelected();
	showClient(rec);
}
}
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};