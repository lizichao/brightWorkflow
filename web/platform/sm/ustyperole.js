Ext.namespace("jsp.platform.sm");jsp.platform.sm.ustyperole=function(){var __caches__=[];this.__caches__=__caches__;var tpRoleRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "ustyperoleid",
    fieldLabel : "ustyperoleid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "usertype",
    fieldLabel : "用户类型",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "roleid",
    fieldLabel : "角色",
    type : "string",
    allowBlank : false
}]);
this.tpRoleRecord=tpRoleRecord;this.__caches__.push(tpRoleRecord);var roleRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "subsysid",
    fieldLabel : "子系统流水号",
    type : "int",
    allowBlank : false
},  {
    xtype : "Field",
    name : "rolename",
    fieldLabel : "角色名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "roleid",
    fieldLabel : "roleid",
    type : "int",
    allowBlank : false
}]);
this.roleRecord=roleRecord;this.__caches__.push(roleRecord);var roleStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "roleStore",
  recordType : roleRecord,
  idProperty : "roleid",
  api : {read:{sysName:"pcmc",oprID:"userrole",actions:"listAllRole"}},
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false,
  autoLoad : true
},'Store');
this.roleStore=roleStore;this.__caches__.push(roleStore);var tpRoleStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "tpRoleStore",
  recordType : tpRoleRecord,
  idProperty : "ustyperoleid",
  api : {read:{sysName:"pcmc",oprID:"tprole",actions:"list"},destroy:{sysName:"pcmc",oprID:"tprole",actions:"delrole"},create:{sysName:"pcmc",oprID:"tprole",actions:"addrole"},update:{sysName:"pcmc",oprID:"tprole",actions:"uptrole"}},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.tpRoleStore=tpRoleStore;this.__caches__.push(tpRoleStore);var usTypeCombo=Ext.create({
  xxtype : "Jpanel",
  xtype : "paracombo",
  classname : "usTypeCombo",
  items : [],
  baseParams : {paramname : 'usertype'}
},'panel');
this.usTypeCombo=usTypeCombo;this.__caches__.push(usTypeCombo);var roleCombo=Ext.create({
  xxtype : "Jpanel",
  xtype : "combo",
  classname : "roleCombo",
  items : [],
  valueField : "roleid",
  displayField : "rolename",
  store : roleStore,
  mode : "local",
  forceSelection : true,
  triggerAction : "all"
},'panel');
this.roleCombo=roleCombo;this.__caches__.push(roleCombo);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "editorgrid",
    frame : true,
    viewConfig : {forceFit:true},
    columnLines : true,
    autoHeight : false,
    height : 320,
    title : "用户类型角色",
    store : tpRoleStore,
    tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType();_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();}},
{xtype: 'tbseparator'},
{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}},
{xtype: 'tbseparator'},
{text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}}],
    bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:tpRoleStore,displayInfo: true}),
    colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
{width:40,sortable:true,header:'用户类型',dataIndex:'usertype',editor:usTypeCombo,renderer:Ext.util.Format.storeRenderer(usTypeCombo.store,"paramcode","parammeanings")},
{width:40,sortable:true,header:'角色',dataIndex:'roleid',editor:roleCombo,renderer:Ext.util.Format.storeRenderer(roleCombo.store,"roleid","rolename")}]),
    sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}})
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};