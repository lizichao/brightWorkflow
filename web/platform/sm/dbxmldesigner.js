Ext.namespace("jsp.platform.sm");jsp.platform.sm.dbxmldesigner=function(){var __caches__=[];this.__caches__=__caches__;var boolRender=function(v){return true==v||'true'==v?'√':'';};
var boolRender2=function(v){return true==v||'true'==v?'':'√';};
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
    name : "name",
    mapping : "column_name",
    fieldLabel : "名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "title",
    mapping : "column_desc",
    fieldLabel : "描述",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "type",
    mapping : "data_type",
    fieldLabel : "类型",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name: "maxlen",
    mapping : "data_width",
    fieldLabel : "长度",
    type : "int",
    allowBlank : true
},  
{
    xtype : "Field",
    name : "notnull",
    mapping : "allownull",
    fieldLabel : "允许为空",
    type : "string",
    allowBlank : true,
    convert:function(v){
    	return "true"==v?false:true;
    }
},  {
    xtype : "Field",
    name : "pk",
    mapping : "ispk",
    fieldLabel : "是否主键",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "column_id",
    fieldLabel : "column_id",
    type : "string",
    allowBlank : true
},
 {
    xtype : "Field",
    name : "gen",
    fieldLabel : "gen",
    type : "string",
    allowBlank : true
},
 {
    xtype : "Field",
    name : "minlen",
    fieldLabel : "minlen",
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
  api : {read:{sysName:"pcmc",oprID:"tbldesigner",actions:"dbXMLcolumns"},destroy:{sysName:"pcmc",oprID:"tbldesigner",actions:"dropcol"},create:{sysName:"pcmc",oprID:"tbldesigner",actions:"addcol"},update:{sysName:"pcmc",oprID:"tbldesigner",actions:"uptcol"}},
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
this.dtStore=dtStore;this.__caches__.push(dtStore);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
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
        store : tblsStore,
        sm : new Ext.grid.RowSelectionModel({singleSelect:true,listeners:{rowselect: function(sm, row, rec){
    var _grid=JrafUTIL.findCmp(MainPanel,'colGrid');
    _grid.getStore().setBaseParam('__sysname',rec.store.baseParams['__sysname']);
    _grid.getStore().setBaseParam('__owner',rec.get('owner'));
    _grid.getStore().setBaseParam('__table_name',rec.get('table_name'));
    _grid.getStore().setBaseParam('__table_desc',rec.get('table_desc'));
    _grid.tblrec=rec;

    _grid.saveBtn.enable();
    _grid.cancelBtn.enable();
    _grid.getStore().load();
}}}),
        tbar : [{xtype:'syscombo',itemId:'syscombo',valueField:'shortname',displayField:'shortname',width:160},
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
	        		var sysnm=this.ownerCt.getComponent('syscombo').getRawValue();
	        		var fsto=JrafUTIL.findCmp(MainPanel,'colGrid').getTopToolbar().getComponent('tblfilecombo').getStore();
	        		fsto.setBaseParam('__sysName',sysnm);
	        		fsto.load();
	        	}
        	}}],
        colModel : new Ext.grid.ColumnModel([{width:40,sortable:true,header:'表空间',dataIndex:'owner'},{width:40,sortable:true,header:'表名',dataIndex:'table_name'}
,{width:40,sortable:false,header:'描述',dataIndex:'table_desc'}]),
        collapsible : true,
        split : true
    },      {
        xtype : "editorgrid",
        title : "字段列表",
        viewConfig : {forceFit:true},
        columnLines : true,
        autoHeight : false,
        height : 320,
        itemId : "colGrid",
        store : colsStore,
        tbar : [

{text:'删除',iconCls:'delete',ref:'../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}},
{xtype: 'tbseparator'},
{xtype:'tblfilecombo',itemId:'tblfilecombo',valueField:'cnname',displayField:'cnname',width:160,editable : true,
  minChars : 0,
  customProperties : true},

{text:'保存',iconCls:'disk',ref:'../saveBtn',disabled: true,handler:function(){                    	
            var _store=this.ownerCt.ownerCt.getStore(); 											
							var jr=new JrafRequest('pcmc','sys','uptTbl');
							
							var ps=JrafUTIL.crDataAllRecNoEncode(_store,{__sysname:_store.baseParams['__sysname'],fname:this.ownerCt.getComponent('tblfilecombo').getValue(),__table_name:_store.baseParams['__table_name'],__table_desc:_store.baseParams['__table_desc']});
							jr.setExtraPs(ps);
							jr.postData();
						
						}},
{xtype: 'tbseparator'},
{text:'取消',iconCls:'cancel',ref:'../cancelBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;_grid.getStore().rejectChanges();}}],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
{width:40,sortable:true,header:'名称',dataIndex:'name'},
{width:40,sortable:true,header:'类型',dataIndex:'type',editor:arrayComboEditor([['long','long'],['String', 'String'],['double','double'],['Timestamp','Timestamp'],['file','file']])},
{width:40,sortable:true,header:'长度',dataIndex:'maxlen'},

{width:40,sortable:true,header:'允许为空',dataIndex:'notnull',renderer:boolRender2},
{width:40,sortable:true,header:'是否主键',dataIndex:'pk',renderer:boolRender},
{width:40,sortable:true,header:'描述',dataIndex:'title',editor: new Ext.form.TextField({allowBlank: false})},
{width:40,sortable:true,header:'最小长度',dataIndex:'minlen',editor: new Ext.form.NumberField({allowDecimals: false,allowNegative:false})},
{width:40,sortable:true,header:'自动生成',dataIndex:'gen',editor:arrayComboEditor([['sysdate', 'sysdate'],['systime','systime'],['datetime','datetime'],['uuid','PK uuid'],['table','PK seq_block'],['session.userid','session.userid'],['session.username','session.username'],['session.deptid','session.deptid'],['session.deptcode','session.deptcode'],['session.deptname','session.deptname']],{editable : true,
  minChars : 0,
  customProperties : true})}]),
        sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
        region : "center"
    }],
    xtype : "panel"
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};