Ext.namespace("jsp.platform.sm");jsp.platform.sm.subsys=function(){var __caches__=[];this.__caches__=__caches__;var sysRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "shortname",
    fieldLabel : "英文简写",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "enname",
    fieldLabel : "英文名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "cnname",
    fieldLabel : "中文名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "imgurl",
    fieldLabel : "图片地址",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "linkurl",
    fieldLabel : "主页地址",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "pubinfourl",
    fieldLabel : "公共信息",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "orderidx",
    fieldLabel : "排序编号",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "subsysid",
    fieldLabel : "subsysid",
    type : "int"
}]);
this.sysRecord=sysRecord;this.__caches__.push(sysRecord);var sysStore=Ext.create({
  xtype : "Store",
  classname : "sysStore",
  type : "JrafXmlStore",
  recordType : sysRecord,
  idProperty : "subsysid",
  api : {read:{sysName:"pcmc",oprID:"subsys",actions:"getSubSysList"},destroy:{sysName:"pcmc",oprID:"subsys",actions:"deleteSubSys"},create:{sysName:"pcmc",oprID:"subsys",actions:"addSubSys"},update:{sysName:"pcmc",oprID:"subsys",actions:"updateSubSys"}},
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false,
  autoLoad : true
},'Store');
this.sysStore=sysStore;this.__caches__.push(sysStore);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "editorgrid",
    frame : true,
    title : "子系统维护",
    viewConfig : {forceFit:true},
    columnLines : true,
    autoHeight : false,
    height : 320,
    store : sysStore,
    tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType();_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();}},
{xtype: 'tbseparator'},
{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('删除数据','确认子系统及其角色/菜单?',function(btn){if(btn == 'yes'){var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}});}},
{xtype: 'tbseparator'},
{text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}},
{xtype: 'tbseparator'},
{text:'取消',iconCls:'cancel',ref: '../cancelBtn',handler:function(){var _grid=this.ownerCt.ownerCt;_grid.getStore().rejectChanges();}}],
    bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:sysStore,displayInfo: true}),
    colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
{width:40,sortable:true,header:'英文简写',dataIndex:'shortname',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'英文名称',dataIndex:'enname',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'中文名称',dataIndex:'cnname',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'图片地址',dataIndex:'imgurl',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'主页地址',dataIndex:'linkurl',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'公共信息',dataIndex:'pubinfourl',editor: new Ext.form.TextField()},
{width:40,sortable:true,header:'排序编号',dataIndex:'orderidx',editor: new Ext.form.NumberField({allowDecimals: false,allowNegative:false})}]),
    sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}})
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};