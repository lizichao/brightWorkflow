Ext.namespace("jsp.workflow.workspace");jsp.workflow.workspace.department=function(){var __caches__=[];this.__caches__=__caches__;
        function reloadStore(processKey,taskKey){
  		   departmentStore.setBaseParam('query_processDefKey',processKey);
  		   departmentStore.setBaseParam('query_taskDefKey',taskKey);
		   departmentStore.load();
		}
		this.reloadStore = reloadStore;
    
    
    var departmentRec=Ext.data.Record.create([  {
    xtype : "Field",
    name : "processdefkey",
    width : 150,
    fieldLabel : "流程定义key",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "taskdefkey",
    width : 150,
    fieldLabel : "任务定义key",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "taskdefname",
    width : 150,
    fieldLabel : "任务定义名称",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "deptid",
    width : 150,
    fieldLabel : "部门编号",
    allowBlank : false,
    type : "string"
}, {
    xtype : "Field",
    name : "deptname",
    width : 150,
    fieldLabel : "部门名称",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "create_people",
    width : 150,
    fieldLabel : "创建人",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "create_time",
    width : 150,
    fieldLabel : "创建时间",
    allowBlank : true,
    type : "date",
    dateFormat : "Y-m-d H:i:s"
},  {
    xtype : "Field",
    name : "update_people",
    width : 150,
    fieldLabel : "更新人",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "update_time",
    width : 150,
    fieldLabel : "更新时间",
    allowBlank : true,
    type : "date",
    dateFormat : "Y-m-d H:i:s"
}]);
this.departmentRec=departmentRec;this.__caches__.push(departmentRec);var departmentStore=Ext.create({
  xtype : "Store",
  classname : "departmentStore",
  type : "JrafXmlStore",
  recordType : departmentRec,
  idProperty : "processdefkey",
  api : {
    read :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "queryDepartment"
  },
    create :     {
      sysName : "workflow",
      oprID : "workflow_node_department",
      actions : "addNodeDepartment"
  },
    update :     {
      sysName : "workflow",
      oprID : "workflow_node_department",
      actions : "updatedepartment"
  },
    destroy :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "deleteNodeDepartment"
  }
},
  autoLoad : false,
  autoSave : false,
  paramsAsHash : true,
  remoteSort : true,
  listeners : {
    write : function (store,action,result,res,rs){
    if (res.success){
    if (_editInfoWin){_editInfoWin.hide();}}}
}
},'Store');
this.departmentStore=departmentStore;this.__caches__.push(departmentStore);var DepartmentEditForm=Ext.create({
  xtype : "form",
  classname : "DepartmentEditForm",
  frame:true,
  items : [
            {
                    xtype : "depttreecombo",
                    fieldLabel : "部门名称",
                    id : "udeptid",
			        name : "deptid",
			        hiddenName : "deptid",
			        valueField : "deptid",
			        displayField : "deptname",
                    triggerAction : "all",
                    width : 150,
                    lazyRender : true,
                    editable : false,
                    forceSelection : true,
                    baseParams : {PageSize:'-1'},
                    allowBlank : false
                }
  ],
  buttonAlign : "center",
  labelAlign:"right",
  labelWidth : 90,
  buttons : [{
		text : "保存",				
		handler : function() {		 
		    var _form=this.ownerCt.ownerCt;
			var rec=_form.record;
			
			 var  _forma = Ext.getCmp('DepartmentEditForm') || DepartmentEditForm;
           	 var _grid = JrafUTIL.findCmp(MainPanel,"departmentGrid");
             _grid.getStore().add(_forma.record);
             _grid.getSelectionModel().selectLastRow();
                 
                   
                   
			var deptId = _form.getForm().findField("deptid").getValue();
			var deptName = _form.getForm().findField("deptid").lastSelectionText;
			var jr=new JrafRequest('workflow','processEditManage','addNodeDepartment');
            jr.setExtraPs({
	            'processdefkey':'vacation',
	            'taskdefkey':'usertask1',
	            'deptId':deptId,
	            'deptName':deptName
            });
            jr.setSuccFn(function(a,_resp,xr){
                _forma.getForm().updateRecord(_forma.record);
                _forma.record.set('deptname',_forma.getForm().findField('udeptid').getRawValue());	
				xr.realize(_forma.record,a.records);
				//var _grid = Ext.getCmp('user-grid');
                _grid.getSelectionModel().selectLastRow();
                      
				Ext.getCmp('departmentEditwin').hide();
                   
                // _form.getForm().updateRecord(rec);
                // xr.realize(rec,a.records);
            });
            jr.postData();
		}
   }]
},'panel');
this.DepartmentEditForm=DepartmentEditForm;this.__caches__.push(DepartmentEditForm);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    layout : "border",
    items : [{
        xtype : "grid",
        itemId : "departmentGrid",
        region : "center",
        autoWidth : true,
        frame : true,
        viewConfig : {
        forceFit : false
       },
        stripeRows : true,
        columnLines : true,
        autoHeight : false,
        height : 320,
        store : departmentStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:departmentStore,displayInfo: true}),
        tbar : [
        {text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
	              var _grid=this.ownerCt.ownerCt;
	              var recordType=_grid.getStore().recordType;
	              var nr=new recordType();
	              
	             // _grid.getStore().add(nr);
	            //  _grid.getSelectionModel().selectLastRow();
	            //  var rec=_grid.getSelectionModel().getSelected();
	              
	              departmentEditwin=Ext.getCmp('departmentEditwin');
				    if(!departmentEditwin){ 
					    departmentEditwin=new Ext.Window({
							title:'新增部门',
							id:'departmentEditwin',
							layout:'fit',
							width:300,
							height:180,
							closeAction:'hide',
							plain: true, 
							modal: true,
							autoDestroy:false,
							items: DepartmentEditForm
						}); 
					} 
				  departmentEditwin.show();
				  DepartmentEditForm.getForm().reset();
	              DepartmentEditForm.record=nr;
 	              DepartmentEditForm.getForm().loadRecord(nr);	
			      DepartmentEditForm.getForm().reset();     
         }},
        {xtype: 'tbseparator'},
        {text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
	           var _grid=this.ownerCt.ownerCt;
	           var ckrs=_grid.getSelectionModel().getSelections();
	           for(var i=0;i<ckrs.length;i++){
	              _grid.getStore().remove(ckrs[i]);
	           }
	           _grid.getStore().save();
        }},
        {xtype: 'tbseparator'},
        {text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}}],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        {width:100,sortable:true,header:'流程定义key',dataIndex:'processdefkey'},
        {width:100,sortable:true,header:'任务定义key',dataIndex:'taskdefkey'},
        {width:100,sortable:true,header:'任务定义名称',dataIndex:'taskdefname'},
        {width:100,sortable:true,header:'部门编号',dataIndex:'deptid'},
        {width:100,sortable:true,header:'部门名称',dataIndex:'deptname'},
        {width:100,sortable:true,header:'创建人',dataIndex:'create_people',hidden : true},
        {width:100,sortable:true,header:'创建时间',dataIndex:'create_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),hidden : true},
        {width:100,sortable:true,header:'更新人',dataIndex:'update_people',hidden : true},
        {width:100,sortable:true,header:'更新时间',dataIndex:'update_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),hidden : true}]),
        sm : new Ext.grid.CheckboxSelectionModel({ listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
        listeners : {rowdblclick : function(g,rowIndex,e){var _rec=g.getSelectionModel().getSelected();openEditInfoWin(_rec);}}
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};