Ext.namespace("jsp.workflow.workspace");jsp.workflow.workspace.component=function(){var __caches__=[];this.__caches__=__caches__;
    	var componentSelRecord= Ext.data.Record.create([
		   	{name: 'component_type', type: 'string'},
		    {name: 'component_name', type: 'string'}
  		]);
  		
  		function reloadStore(processKey,taskKey){
  		   componentStore.setBaseParam('query_processDefKey',processKey);
  		   componentStore.setBaseParam('query_taskDefKey',taskKey);
		   componentStore.load();
		}
		this.reloadStore = reloadStore;
     var componentRec=Ext.data.Record.create([  {
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
}, {
    xtype : "Field",
    name : "taskdefname",
    width : 150,
    fieldLabel : "任务定义名称",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "node_type",
    width : 150,
    fieldLabel : "节点类型",
    allowBlank : false,
    type : "string"
},{
    xtype : "Field",
    name : "component_id",
    width : 150,
    fieldLabel : "功能组件id",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "component_type",
    width : 150,
    fieldLabel : "功能组件类型",
    allowBlank : false,
    type : "string"
},   {
    xtype : "Field",
    name : "component_name",
    width : 150,
    fieldLabel : "功能组件名称",
    allowBlank : false,
    type : "string"
}, {
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
this.componentRec=componentRec;this.__caches__.push(componentRec);var componentSelectStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "componentSelectStore",
  recordType : componentSelRecord,
  idProperty : "component_type",
  api : {read:{sysName:"workflow",oprID:"processEditManage",actions:"getNodeComponent"}},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.componentSelectStore=componentSelectStore;this.__caches__.push(componentSelectStore);var componentStore=Ext.create({
  xtype : "Store",
  classname : "componentStore",
  type : "JrafXmlStore",
  recordType : componentRec,
  idProperty : "taskdefkey",
  api : {
    read :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "queryComponent"
  },
    create :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "addNodeComponent"
  },
    update :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "updateComponent"
  },
    destroy :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "deleteNodeComponent"
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
this.componentStore=componentStore;this.__caches__.push(componentStore);var ComponentEditForm=Ext.create({
  xtype : "form",
  classname : "ComponentEditForm",
  frame:true,
  items : [
     {  
       xtype : "multicombo",
       fieldLabel : "功能组件",
       id : "componentSelId",
       name : "componentSel",
       store : componentSelectStore,
           hiddenName : "component_type",
       mode : "local",
       displayField : "component_name",
       valueField : "component_type",
       triggerAction : "all",
       allowBlank : false,
       width : 150
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
			
			 var  _forma = Ext.getCmp('ComponentEditForm') || ComponentEditForm;
           	 var _grid = JrafUTIL.findCmp(MainPanel,"componentGrid");
             _grid.getStore().add(_forma.record);
             _grid.getSelectionModel().selectLastRow();
                 
                   
                   
			var componentType = _form.getForm().findField("componentSelId").getValue();
			var componentName = _form.getForm().findField("componentSelId").lastSelectionText;
			var jr=new JrafRequest('workflow','processEditManage','addNodeComponent');
            jr.setExtraPs({
	            'processdefkey':'vacation',
	            'taskdefkey':'usertask1',
	            'node_type':'usertask',
	            'component_type':componentType,
	            'component_name':componentName
            });
            jr.setSuccFn(function(a,_resp,xr){
                _forma.getForm().updateRecord(_forma.record);
                _forma.record.set('component_name',_forma.getForm().findField('componentSelId').getRawValue());	
                a.records[0]
               _forma.record.set('component_id',_forma.getForm().findField('componentSelId').getRawValue());	
				xr.realize(_forma.record,a.records);
                _grid.getSelectionModel().selectLastRow();
				Ext.getCmp('componentEditwin').hide();
            });
            jr.postData(); 	
			 
		}
   }]
},'panel');
this.ComponentEditForm=ComponentEditForm;this.__caches__.push(ComponentEditForm);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    layout : "border",
    items : [       {
        xtype : "grid",
        itemId : "componentGrid",
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
        store : componentStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:componentStore,displayInfo: true}),
        tbar : [
        {text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
                   //  JrafUTIL.getCmp("nodeEditGridd") ;
                // var rec= JrafUTIL.findCmp(nodePanel,"nodeEditGrid").getSelectionModel().getSelected()
                 var selectedData =  Ext.getCmp("nodeRemindGrid").getSelectionModel().getSelected().data;
                 var _grid=this.ownerCt.ownerCt;
	             var recordType=_grid.getStore().recordType;
	             var nr=new recordType();
	             nr.set("processdefkey",selectedData.processKey);
	             nr.set("taskdefkey",selectedData.nodeId);
	             nr.set("taskdefname",selectedData.nodeName);
	             nr.set("node_type",selectedData.nodeType);
	              
		         componentEditwin=Ext.getCmp('componentEditwin');
				    if(!componentEditwin){ 
					    componentEditwin=new Ext.Window({
							title:'新增功能组件',
							id:'componentEditwin',
							layout:'fit',
							width:300,
							height:180,
							closeAction:'hide',
							plain: true, 
							modal: true,
							autoDestroy:false,
							items: ComponentEditForm
						}); 
				  } 
				  componentEditwin.show();
			      ComponentEditForm.getForm().reset();
	              ComponentEditForm.record=nr;
 	              ComponentEditForm.getForm().loadRecord(nr);	
			      ComponentEditForm.getForm().reset(); 
        }},
        {xtype: 'tbseparator'},
        {text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
	            var _grid=this.ownerCt.ownerCt;
	            var ckrs=_grid.getSelectionModel().getSelections();
	            var deleteComponentArray = [];
	            for(var i=0;i<ckrs.length;i++){
	                _grid.getStore().remove(ckrs[i]);
	                var chkData = ckrs[i].data;
	                var deleteComponentObject = {
	                   processdefkey : chkData.processdefkey,
	                   component_type :chkData.component_type,
	                   taskdefkey : chkData.taskdefkey
	                };
	                deleteComponentArray.push(deleteComponentObject);
	            }
	            var jr=new JrafRequest('workflow','processEditManage','deleteNodeComponent');
	            jr.setExtraPs({"deleteComponentIds":JSON.stringify(deleteComponentArray)});
	           // jr.setExtraPs({"deleteComponentIds":deleteComponentArray});
	            jr.setSuccFn(function(a,_resp,xr){
	            });
	            jr.postData(); 
	            
	          //  _grid.getStore().save();
           }
         },
        {xtype: 'tbseparator'},
        {text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}}],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        {width:100,sortable:true,header:'流程定义key',dataIndex:'processdefkey'},
        {width:100,sortable:true,header:'任务定义key',dataIndex:'taskdefkey'},
        {width:100,sortable:true,header:'任务定义名称',dataIndex:'taskdefname'},
        {width:100,sortable:true,header:'节点类型',dataIndex:'node_type'},
        {width:100,sortable:true,header:'功能组件id',dataIndex:'component_id'},
        {width:100,sortable:true,header:'功能组件类型',dataIndex:'component_type'},
        {width:100,sortable:true,header:'功能组件名称',dataIndex:'component_name'},
        {width:100,sortable:true,header:'创建人',dataIndex:'create_people',hidden : true},
        {width:100,sortable:true,header:'创建时间',dataIndex:'create_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),hidden : true},
        {width:100,sortable:true,header:'更新人',dataIndex:'update_people',hidden : true},
        {width:100,sortable:true,header:'更新时间',dataIndex:'update_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s'),hidden : true}]),
        sm : new Ext.grid.CheckboxSelectionModel({ 
        listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
        listeners : {rowdblclick : function(g,rowIndex,e){var _rec=g.getSelectionModel().getSelected();openEditInfoWin(_rec);}}
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};