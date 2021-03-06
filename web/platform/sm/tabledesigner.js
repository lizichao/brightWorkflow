Ext.namespace("jsp.platform.sm");jsp.platform.sm.tabledesigner=function(){var __caches__=[];this.__caches__=__caches__;var boolRender=function(v){return true==v||'true'==v?'√':'';};
function coloumsWin(colForm,rec)
{
	var colwin=MainPanel['colwin'];
	if(!colwin){
		colwin=new Ext.Window({
		title:'字段定义',
		layout:'fit',
		width:500,
		height:320,
		closeAction:'hide',
		plain: true,
		modal: true,
		autoDestroy:false,
		items: colForm
		});
		MainPanel['colwin']=colwin;
		__caches__.push(colwin);
	}	
	colwin.show();
	colForm.getForm().reset();
	colForm.record=rec;
	if(rec)	colForm.getForm().loadRecord(rec);
};
    var tblsRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "owner",
    fieldLabel : "表空间",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "table_name",
    fieldLabel : "表名",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "table_desc",
    fieldLabel : "描述",
    type : "string",
    allowBlank : true
}]);
this.tblsRecord=tblsRecord;this.__caches__.push(tblsRecord);var colsRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "column_name",
    fieldLabel : "名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "column_desc",
    fieldLabel : "描述",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "data_type",
    fieldLabel : "类型",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "data_width",
    fieldLabel : "长度",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "data_scale",
    fieldLabel : "精度",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "allownull",
    fieldLabel : "允许为空",
    type : "string",
    defaultValue : "true",
    allowBlank : true
},  {
    xtype : "Field",
    name : "ispk",
    fieldLabel : "是否主键",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "data_default",
    fieldLabel : "缺省值",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "column_id",
    fieldLabel : "column_id",
    type : "string",
    allowBlank : true
}]);
this.colsRecord=colsRecord;this.__caches__.push(colsRecord);var dtRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "data_type",
    fieldLabel : "数据类型",
    type : "string",
    allowBlank : true
}]);
this.dtRecord=dtRecord;this.__caches__.push(dtRecord);var tblsStore=Ext.create({
  xtype : "Store",
  classname : "tblsStore",
  type : "JrafXmlStore",
  recordType : tblsRecord,
  api : {read:{sysName:"pcmc",oprID:"tbldesigner",actions:"alltables"},destroy:{sysName:"pcmc",oprID:"tbldesigner",actions:"droptable"}},
  autoLoad : false,
  autoSave : false,
  idProperty : "table_name",
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.tblsStore=tblsStore;this.__caches__.push(tblsStore);var colsStore=Ext.create({
  xtype : "Store",
  classname : "colsStore",
  type : "JrafXmlStore",
  recordType : colsRecord,
  idProperty : "column_id",
  api : {read:{sysName:"pcmc",oprID:"tbldesigner",actions:"tablecolumns"},destroy:{sysName:"pcmc",oprID:"tbldesigner",actions:"dropcol"},create:{sysName:"pcmc",oprID:"tbldesigner",actions:"addcol"},update:{sysName:"pcmc",oprID:"tbldesigner",actions:"uptcol"}},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false,
  listeners : {
  write : function(store,action,result,res,rs){
if("create"===action){
    var _grid=JrafUTIL.findCmp(MainPanel,'colGrid');
    _grid.tblrec.phantom = false;
    _grid.tblrec._phid = rs.id;
    _grid.tblrec.id = _grid.tblrec.get('table_name');
    _grid.tblrec.commit();
}
}
}
},'Store');
this.colsStore=colsStore;this.__caches__.push(colsStore);var dtStore=Ext.create({
  xtype : "Store",
  classname : "dtStore",
  type : "JrafXmlStore",
  recordType : dtRecord,
  idProperty : "data_type",
  api : {read:{sysName:"pcmc",oprID:"tbldesigner",actions:"datatypes"}},
  autoLoad : false,
  autoSave : false,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.dtStore=dtStore;this.__caches__.push(dtStore);var tblForm=Ext.create({
  xxtype : "Jpanel",
  xtype : "form",
  classname : "tblForm",
  items : [    {
      name : "owner",
      fieldLabel : "表空间",
      allowBlank : false,
      xtype : "textfield"
  },    {
      name : "table_name",
      fieldLabel : "表名",
      allowBlank : false,
      vtype : "upperCase",
      xtype : "textfield"
  },    {
      name : "table_desc",
      fieldLabel : "描述",
      allowBlank : false,
      xtype : "textfield",
      width : 200
  }],
  buttons : [{
    text:'保存',
    handler:function(){
	this.ownerCt.ownerCt.doSave();
}
}],
  buttonAlign : "center"
},'panel');
this.tblForm=tblForm;this.__caches__.push(tblForm);var colForm=Ext.create({
  xxtype : "Jpanel",
  xtype : "form",
  classname : "colForm",
  items : [    {
      name : "column_name",
      fieldLabel : "名称",
      allowBlank : false,
      vtype : "upperCase",
      xtype : "textfield"
  },    {
      xtype : "combo",
      fieldLabel : "类型",
      allowBlank : false,
      triggerAction : "all",
      editable : false,
      lazyRender : true,
      store : dtStore,
      valueField : "data_type",
      displayField : "data_type",
      width : 125,
      name : "data_type"
  },    {
      name : "data_width",
      fieldLabel : "长度",
      allowBlank : true,
      xtype : "numberfield",
      allowDecimals : false,
      width : 80,
      allowNegative : false
  },    {
      name : "data_scale",
      fieldLabel : "精度",
      allowBlank : true,
      xtype : "numberfield",
      allowDecimals : false,
      width : 80,
      allowNegative : false
  },    {
      name : "allownull",
      xtype : "checkbox",
      allowBlank : false,
      checked : true,
      fieldLabel : "允许为空"
  },    {
      name : "ispk",
      xtype : "checkbox",
      fieldLabel : "主键",
      listeners : {
  check : function(scope,checked){
if(checked){
    var _form=scope.ownerCt;
    _form.getForm().findField('allownull').setValue('false');
}
}
}
  },    {
      name : "data_default",
      fieldLabel : "缺省值",
      allowBlank : true,
      xtype : "textfield",
      width : 280
  },    {
      name : "column_desc",
      fieldLabel : "描述",
      allowBlank : true,
      xtype : "textfield",
      width : 280
  }],
  buttonAlign : "center",
  buttons : [{
    text:'确认',
    handler:function(){
    var _form=this.ownerCt.ownerCt;
    if(!_form.getForm().isValid()) return;
    if(!_form.record)
    {
    	var _grid=JrafUTIL.findCmp(MainPanel,'colGrid');
    	var recordType=_grid.getStore().recordType;
    	var nr=new recordType();
    	_grid.getStore().add(nr);
    	_grid.getSelectionModel().selectLastRow();
    	_form.record=nr;
    }
    _form.getForm().updateRecord(_form.record);
    MainPanel['colwin'].hide();
}
}]
},'panel');
this.colForm=colForm;this.__caches__.push(colForm);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    layout : "border",
    items : [      {
        xtype : "grid",
        region : "west",
        width : 280,
        titleCollapse : true,
        title : "数据库表",
        viewConfig : {forceFit:true},
        columnLines : true,
        autoHeight : false,
        itemId : "tblsGrid",
        store : tblsStore,
        sm : new Ext.grid.RowSelectionModel({singleSelect:true,listeners:{rowselect: function(sm, row, rec){
    var _grid=JrafUTIL.findCmp(MainPanel,'colGrid');
    _grid.getStore().setBaseParam('__sysname',rec.store.baseParams['__sysname']);
    _grid.getStore().setBaseParam('__owner',rec.get('owner'));
    _grid.getStore().setBaseParam('__table_name',rec.get('table_name'));
    _grid.getStore().setBaseParam('__table_desc',rec.get('table_desc'));
    _grid.tblrec=rec;
    _grid.addBtn.enable();
    _grid.saveBtn.enable();
    _grid.cancelBtn.enable();
    _grid.getStore().load();
}}}),
        tbar : [{xtype:'syscombo',itemId:'syscombo',valueField:'shortname',width:160},
        	{
        	tooltip:'刷新数据库',
        	iconCls:'arrow-refresh',
        	handler:function(){
        	    var sv=this.ownerCt.getComponent('syscombo').getValue();
        	    if(''!=sv)
        	    {
	        		tblsStore.setBaseParam('__sysname',sv);
	        		tblsStore.load();
	        		dtStore.setBaseParam('__sysname',sv);
	        		dtStore.load();
	        	}
        	}},{xtype: 'tbseparator'},
        	{
	        tooltip:'新建表',
	        iconCls:'add',
	        handler:function(){
var tblwin=MainPanel['tblwin'];
if(!tblwin){
	tblwin=new Ext.Window({
		title:'新建数据表',
	    layout:'fit',
	    width:400,
	    height:240,
	    closeAction:'hide',
	    plain: true,
	    modal: true,
	    autoDestroy:false,
	    items: tblForm
	});
	MainPanel['tblwin']=tblwin;
	__caches__.push(tblwin);
	tblForm.doSave=function(){
		if(tblForm.getForm().isValid())
		{
			var _grid=JrafUTIL.findCmp(MainPanel,'tblsGrid');
			var recordType=_grid.getStore().recordType;
			var nr=new recordType();
			tblForm.getForm().updateRecord(nr);
			_grid.getStore().add(nr);
			_grid.getSelectionModel().selectLastRow();
			tblwin.hide();
		}
	}
}
tblwin.show();
tblForm.getForm().reset();
			}
	    	},{xtype: 'tbseparator'},{
	        tooltip:'删除表',
	        iconCls:'delete',
	        handler:function(){
	        	var _grid=this.ownerCt.ownerCt;
	        	var ckrec=_grid.getSelectionModel().getSelected();
	        	if(ckrec && !ckrec.phantom)
	        	{
	        		Ext.Msg.confirm('删除数据表','确认删除表?数据不可恢复!',function(btn){if(btn == 'yes'){
	        			_grid.getStore().remove(ckrec);
	        			_grid.getStore().save();
	        			//_grid.getSelectionModel().selectPrevious.defer(200);
	        		}});
	        	}
	        	else
	        	{
	        		_grid.getStore().remove(ckrec);
	        		//_grid.getSelectionModel().selectPrevious.defer(200);
	        	}
	        }}],
        colModel : new Ext.grid.ColumnModel([{width:40,sortable:true,header:'表空间',dataIndex:'owner'},{width:40,sortable:true,header:'表名',dataIndex:'table_name'}
,{width:40,sortable:false,header:'描述',dataIndex:'table_desc'}]),
        collapsible : true,
        split : true
    },      {
        xtype : "grid",
        title : "字段列表",
        viewConfig : {forceFit:true},
        columnLines : true,
        autoHeight : false,
        height : 320,
        itemId : "colGrid",
        store : colsStore,
        tbar : [{text:'新增',iconCls:'add',ref:'../addBtn',disabled: true,handler:function(){
coloumsWin(colForm,null);
var cn=colForm.getForm().findField('column_name');
cn.enable();
var dd=colForm.getForm().findField('data_default');
dd.enable();
}},
{xtype: 'tbseparator'},
{text:'删除',iconCls:'delete',ref:'../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}},
{xtype: 'tbseparator'},
{text:'保存',iconCls:'disk',ref:'../saveBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){
    var tblrec=_grid.tblrec||{};
    if(tblrec.phantom)
    {
    	_grid.getStore().setBaseParam('__newtblflag','true');
    }
    else
    {
    	_grid.getStore().setBaseParam('__newtblflag','');
    }
    _grid.getStore().save();
}});}},
{xtype: 'tbseparator'},
{text:'取消',iconCls:'cancel',ref:'../cancelBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;_grid.getStore().rejectChanges();}}],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
{width:40,sortable:true,header:'名称',dataIndex:'column_name'},
{width:40,sortable:true,header:'类型',dataIndex:'data_type'},
{width:40,sortable:true,header:'长度',dataIndex:'data_width'},
{width:40,sortable:true,header:'精度',dataIndex:'data_scale'},
{width:40,sortable:true,header:'允许为空',dataIndex:'allownull',renderer:boolRender},
{width:40,sortable:true,header:'是否主键',dataIndex:'ispk',renderer:boolRender},
{width:40,sortable:true,header:'缺省值',dataIndex:'data_default'},
{width:40,sortable:true,header:'描述',dataIndex:'column_desc'}]),
        sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
        region : "center",
        listeners : {
  rowdblclick : function(grid,rowIndex,e){
var rec=grid.getSelectionModel().getSelected();
coloumsWin(colForm,rec)
var cn=colForm.getForm().findField('column_name');
rec.phantom?cn.enable():cn.disable();
var dd=colForm.getForm().findField('data_default');
rec.phantom?dd.enable():dd.disable();
}
}
    }],
    xtype : "panel"
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};