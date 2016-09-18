Ext.namespace("jsp.platform.cas");jsp.platform.cas.services=function(){var __caches__=[];this.__caches__=__caches__;function openServWin(rec){
	var win=MainPanel['servwin'];
	if(!win){
		win=new Ext.Window({
			title:'服务定义',
	        layout:'fit',
	        width:640,
	        height:480,
	        closeAction:'hide',
	        plain: true,
	        modal: true,
	        items:servForm
	    });
	    MainPanel['servwin']=win;
	    __caches__.push(win);
    }
	win.show();
	servForm.getForm().reset();
	servForm.record=rec;
	if(rec.phantom === false && rec.modified==null)
	{
		var jr=new JrafRequest('pcmc','cas','detService',{recordType:servRecord,idProperty:'id'});
		jr.setExtraPs({id:rec.get('id')});
		jr.setSuccFn(function(a,_resp,xr){
			servForm.getForm().loadRecord(a.records[0]);
	    });
		jr.postData();
	}
	else
	{
		servForm.getForm().loadRecord(rec);
	}
};var servRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "id",
    fieldLabel : "主键",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "name",
    fieldLabel : "名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "serviceid",
    fieldLabel : "服务URL",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "description",
    fieldLabel : "描述",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "theme",
    fieldLabel : "主题名",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "enabled",
    fieldLabel : "启用",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "ssoenabled",
    fieldLabel : "参与SSO",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "allowedtoproxy",
    fieldLabel : "允许作为代理",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "anonymousaccess",
    fieldLabel : "匿名访问",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "evaluation_order",
    fieldLabel : "排序号",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "ignoreattributes",
    fieldLabel : "忽略属性",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "a_name",
    fieldLabel : "属性",
    type : "string",
    allowBlank : true
}]);
this.servRecord=servRecord;this.__caches__.push(servRecord);var attRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "a_name",
    fieldLabel : "a_name",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "a_desc",
    fieldLabel : "a_desc",
    type : "string",
    allowBlank : true
}]);
this.attRecord=attRecord;this.__caches__.push(attRecord);var servStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "servStore",
  recordType : servRecord,
  idProperty : "id",
  api : {
    read:{method:'POST',sysName:'pcmc',oprID:'cas',actions:'listService'},
	create:{method:'POST',sysName:'pcmc',oprID:'cas',actions:'addService'},
	update:{method:'POST',sysName:'pcmc',oprID:'cas',actions:'uptService'},
	destroy:{method:'POST',sysName:'pcmc',oprID:'cas',actions:'delService'}
},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.servStore=servStore;this.__caches__.push(servStore);var attStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "attStore",
  recordType : attRecord,
  idProperty : "a_name",
  api : {read:{sysName:"pcmc",oprID:"cas",actions:"attMapping"}},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.attStore=attStore;this.__caches__.push(attStore);var servForm=Ext.create({
  xxtype : "Jpanel",
  xtype : "form",
  classname : "servForm",
  items : [    {
      name : "name",
      fieldLabel : "名称",
      allowBlank : false,
      xtype : "textfield"
  },    {
      name : "serviceid",
      fieldLabel : "服务URL",
      allowBlank : false,
      xtype : "textfield",
      width : 300
  },    {
      name : "description",
      fieldLabel : "描述",
      allowBlank : false,
      xtype : "textarea",
      width : 300
  },    {
      name : "theme",
      fieldLabel : "主题名",
      allowBlank : false,
      xtype : "textfield",
      value : "default"
  },    {
      xtype : "checkboxgroup",
      fieldLabel : "状态",
      items : [    {
      boxLabel : "启用",
      name : "enabled",
      inputValue : 1
  },    {
      boxLabel : "参与SSO",
      name : "ssoenabled",
      inputValue : 1
  },    {
      boxLabel : "允许作为代理",
      name : "allowedtoproxy",
      inputValue : 1
  },    {
      name : "anonymousaccess",
      inputValue : 1,
      boxLabel : "匿名访问"
  }]
  },    {
      name : "evaluation_order",
      fieldLabel : "排序号",
      allowBlank : true,
      xtype : "numberfield",
      allowDecimals : false
  },    {
      name : "ignoreattributes",
      allowBlank : true,
      xtype : "checkbox",
      boxLabel : "忽略属性",
      inputValue : "1"
  },    {
      name : "a_name",
      hiddenName : "a_name",
      fieldLabel : "属性",
      allowBlank : true,
      xtype : "multicombo",
      store : attStore,
      mode : "local",
      displayField : "a_desc",
      valueField : "a_name",
      width : 420
  }],
  buttons : [{
	text:"确定",handler:function(){
	if(servForm.getForm().isValid()){
		var win=MainPanel['servwin'];
		servForm.getForm().updateRecord(servForm.record);
		win.hide();
	}
}
}],
  buttonAlign : "center"
},'panel');
this.servForm=servForm;this.__caches__.push(servForm);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "grid",
    frame : true,
    title : "服务管理",
    viewConfig : {forceFit:true},
    columnLines : true,
    autoHeight : false,
    height : 320,
    store : servStore,
    tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType();_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();openServWin(rec);}},
{xtype: 'tbseparator'},
{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}},
{xtype: 'tbseparator'},
{text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}},
{xtype: 'tbseparator'},
{text:'取消',iconCls:'cancel',ref: '../cancelBtn',handler:function(){var _grid=this.ownerCt.ownerCt;_grid.getStore().rejectChanges();}}],
    bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:servStore,displayInfo: true}),
    colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        	{width:40,sortable:true,header:'名称',dataIndex:'name'},
        	{width:40,sortable:true,header:'服务URL',dataIndex:'serviceid'},
        	{width:40,sortable:true,header:'描述',dataIndex:'description'},
        	{width:40,sortable:true,header:'启用',dataIndex:'enabled',renderer:Ext.util.Format.checkboxRenderer()},
        	{width:40,sortable:true,header:'参与SSO',dataIndex:'ssoenabled',renderer:Ext.util.Format.checkboxRenderer()},
        	{width:40,sortable:true,header:'允许作为代理',dataIndex:'allowedtoproxy',renderer:Ext.util.Format.checkboxRenderer()},
        	{width:40,sortable:true,header:'匿名访问',dataIndex:'anonymousaccess',renderer:Ext.util.Format.checkboxRenderer()}]),
    sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
    listeners : {
  rowdblclick : function(g,rowIndex,e){
	var rec=g.getSelectionModel().getSelected();
	openServWin(rec);
}
}
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};