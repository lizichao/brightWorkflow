Ext.namespace("jsp.platform.sm");jsp.platform.sm.paramlist=function(){var __caches__=[];this.__caches__=__caches__;var paraMRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "paramname",
    fieldLabel : "参数名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "paramdesc",
    fieldLabel : "参数描述",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "paramid",
    fieldLabel : "paramid",
    type : "int",
    allowBlank : true
}]);
this.paraMRecord=paraMRecord;this.__caches__.push(paraMRecord);var paraDRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "paramdetailid",
    fieldLabel : "paramdetailid",
    type : "float",
    allowBlank : true
},  {
    xtype : "Field",
    name : "paramcode",
    fieldLabel : "paramcode",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "parammeanings",
    fieldLabel : "parammeanings",
    type : "string",
    allowBlank : false
}]);
this.paraDRecord=paraDRecord;this.__caches__.push(paraDRecord);var paraMStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "paraMStore",
  recordType : paraMRecord,
  idProperty : "paramid",
  api : {
	read:{method:'POST',sysName:'pcmc',oprID:'sm_query',actions:'getParamMasterList'},
	create:{method:'POST',sysName:'pcmc',oprID:'sm_maintenance',actions:'addParamMaster'},
	update:{method:'POST',sysName:'pcmc',oprID:'sm_maintenance',actions:'updateParamMaster'},
	destroy:{method:'POST',sysName:'pcmc',oprID:'sm_maintenance',actions:'deleteParamMaster'}
},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.paraMStore=paraMStore;this.__caches__.push(paraMStore);var paraDStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "paraDStore",
  recordType : paraDRecord,
  idProperty : "paramdetailid",
  api : {
  	read:{method:'POST',sysName:'pcmc',oprID:'sm_query',actions:'getParamDetailList'},
	create:{method:'POST',sysName:'pcmc',oprID:'sm_maintenance',actions:'addParamDetail'},
	update:{method:'POST',sysName:'pcmc',oprID:'sm_maintenance',actions:'updateParamDetail'},
	destroy:{method:'POST',sysName:'pcmc',oprID:'sm_maintenance',actions:'deleteParamDetail'}
},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.paraDStore=paraDStore;this.__caches__.push(paraDStore);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    layout : "anchor",
    items : [      {
        xtype : "editorgrid",
        frame : true,
        title : "系统代码",
        viewConfig : {forceFit:true},
        columnLines : true,
        autoHeight : false,
        height : 320,
        store : paraMStore,
        tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType();_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();}},
{xtype: 'tbseparator'},
{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}},
{xtype: 'tbseparator'},
{text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}},
{xtype: 'tbseparator'},
{text:'取消',iconCls:'cancel',ref: '../cancelBtn',handler:function(){var _grid=this.ownerCt.ownerCt;_grid.getStore().rejectChanges();}},
{xtype: 'tbseparator'},
{text:'生成JS',iconCls:'disk',ref: '../uptjsBtn',handler:function(){var jr=new JrafRequest('pcmc','param','uptjs');jr.postData();}}],
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:paraMStore,displayInfo: true}),
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
{width:40,sortable:true,header:'代码英文名称',dataIndex:'paramname',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'代码中文名称',dataIndex:'paramdesc',editor: new Ext.form.TextField({allowBlank: false})}
]),
        sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}, rowselect: function(sm, row, rec) {
	        	var _pdgrid=this.grid.ownerCt.get(1);
	        	if(rec.phantom === true)
	        	{
	        		_pdgrid.setDisabled(true);
	             }
	             else
	             {
	             	_pdgrid.setDisabled(false);
		        	_pdgrid.setTitle('代码明细----'+rec.get('paramdesc'));
	                _pdgrid.getStore().setBaseParam('paramid',rec.id);
	                _pdgrid.getStore().load();
	             }
            }}}),
        anchor : "100% 50%"
    },      {
        xtype : "editorgrid",
        frame : true,
        title : "代码明细",
        viewConfig : {forceFit:true},
        columnLines : true,
        autoHeight : false,
        height : 320,
        store : paraDStore,
        tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType();_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();}},
{xtype: 'tbseparator'},
{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}},
{xtype: 'tbseparator'},
{text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}},
{xtype: 'tbseparator'},
{text:'取消',iconCls:'cancel',ref: '../cancelBtn',handler:function(){var _grid=this.ownerCt.ownerCt;_grid.getStore().rejectChanges();}}],
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:paraDStore,displayInfo: true}),
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
{width:40,sortable:true,header:'代码值',dataIndex:'paramcode',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'代码描述',dataIndex:'parammeanings',editor: new Ext.form.TextField({allowBlank: false})}]),
        sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
        anchor : "100% 50%"
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};