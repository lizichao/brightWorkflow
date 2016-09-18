Ext.namespace("jsp.platform.sm");jsp.platform.sm.subsys=function(){var __caches__=[];this.__caches__=__caches__;var sysRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "shortname",
    fieldLabel : "Ӣ�ļ�д",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "enname",
    fieldLabel : "Ӣ������",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "cnname",
    fieldLabel : "��������",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "imgurl",
    fieldLabel : "ͼƬ��ַ",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "linkurl",
    fieldLabel : "��ҳ��ַ",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "pubinfourl",
    fieldLabel : "������Ϣ",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "orderidx",
    fieldLabel : "������",
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
    title : "��ϵͳά��",
    viewConfig : {forceFit:true},
    columnLines : true,
    autoHeight : false,
    height : 320,
    store : sysStore,
    tbar : [{text:'����',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType();_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();}},
{xtype: 'tbseparator'},
{text:'ɾ��',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('ɾ������','ȷ����ϵͳ�����ɫ/�˵�?',function(btn){if(btn == 'yes'){var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}});}},
{xtype: 'tbseparator'},
{text:'����',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('��������','ȷ�ϱ������޸�����?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}},
{xtype: 'tbseparator'},
{text:'ȡ��',iconCls:'cancel',ref: '../cancelBtn',handler:function(){var _grid=this.ownerCt.ownerCt;_grid.getStore().rejectChanges();}}],
    bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:sysStore,displayInfo: true}),
    colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
{width:40,sortable:true,header:'Ӣ�ļ�д',dataIndex:'shortname',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'Ӣ������',dataIndex:'enname',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'��������',dataIndex:'cnname',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'ͼƬ��ַ',dataIndex:'imgurl',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'��ҳ��ַ',dataIndex:'linkurl',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'������Ϣ',dataIndex:'pubinfourl',editor: new Ext.form.TextField()},
{width:40,sortable:true,header:'������',dataIndex:'orderidx',editor: new Ext.form.NumberField({allowDecimals: false,allowNegative:false})}]),
    sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}})
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};