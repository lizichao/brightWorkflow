Ext.namespace("jsp.workflow.workspace");jsp.workflow.workspace.role=function(){var __caches__=[];this.__caches__=__caches__;
      	var roleSelRecord= Ext.data.Record.create([
		   	{name: 'roleid', type: 'string'},
		    {name: 'rolename', type: 'string'}
  		]);
  		
  		function reloadStore(rec){
  		    alert()
		}
    var roleRec=Ext.data.Record.create([  {
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
}, {
    xtype : "Field",
    name : "roleid",
    width : 150,
    fieldLabel : "角色id",
    allowBlank : false,
    type : "string"
},{
    xtype : "Field",
    name : "rolename",
    width : 150,
    fieldLabel : "角色名称",
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
this.roleRec=roleRec;this.__caches__.push(roleRec);var roleSelectStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "roleSelectStore",
  recordType : roleSelRecord,
  idProperty : "componentType",
  api : {read:{sysName:"workflow",oprID:"processEditManage",actions:"getNodeRole"}},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.roleSelectStore=roleSelectStore;this.__caches__.push(roleSelectStore);var roleStore=Ext.create({
  xtype : "Store",
  classname : "roleStore",
  type : "JrafXmlStore",
  recordType : roleRec,
  idProperty : "roleId",
  api : {
    read :     {
      sysName : "workflow",
      oprID : "processEditManage",
      actions : "queryRole"
  },
    create :     {
      sysName : "workflow",
      oprID : "workflow_node_role",
      actions : "addrole"
  },
    update :     {
      sysName : "workflow",
      oprID : "workflow_node_role",
      actions : "updaterole"
  },
    destroy :     {
      sysName : "workflow",
      oprID : "workflow_node_role",
      actions : "deleterole"
  }
},
  autoLoad : false,
  autoSave : false,
  paramsAsHash : true,
  remoteSort : true,
  listeners : {
    write : function (store,action,result,res,rs){if (res.success){if (_editInfoWin){_editInfoWin.hide();}}}
}
},'Store');
this.roleStore=roleStore;this.__caches__.push(roleStore);var RoleEditForm=Ext.create({
  xtype : "form",
  classname : "RoleEditForm",
  frame:true,
  items : [
     {  
       xtype : "multicombo",
       fieldLabel : "角色",
       id: "roleSelId",
       name : "roleid",
       store : roleSelectStore,
       mode : "local",
        valueField : "roleid",
       displayField : "rolename",
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
			
			 var  _forma = Ext.getCmp('RoleEditForm') || RoleEditForm;
           	 var _grid = JrafUTIL.findCmp(MainPanel,"roleGrid");
             _grid.getStore().add(_forma.record);
             _grid.getSelectionModel().selectLastRow();
                 
                   
                   
			var roleId = _form.getForm().findField("roleSelId").getValue();
			var roleName = _form.getForm().findField("roleSelId").lastSelectionText;
			var jr=new JrafRequest('workflow','processEditManage','addNodeRole');
            jr.setExtraPs({
	            'processdefkey':'vacation',
	            'taskdefkey':'usertask1',
	            'roleId':roleId,
	            'roleName':roleName
            });
            jr.setSuccFn(function(a,_resp,xr){
                _forma.getForm().updateRecord(_forma.record);
                _forma.record.set('rolename',_forma.getForm().findField('roleSelId').getRawValue());	
				xr.realize(_forma.record,a.records);
				//var _grid = Ext.getCmp('user-grid');
                _grid.getSelectionModel().selectLastRow();
                      
				Ext.getCmp('roleEditwin').hide();
                   
                // _form.getForm().updateRecord(rec);
                // xr.realize(rec,a.records);
            });
            jr.postData(); 
		}
   }]
},'panel');
this.RoleEditForm=RoleEditForm;this.__caches__.push(RoleEditForm);var EditInfoForm=Ext.create({
  xtype : "form",
  classname : "EditInfoForm",
  frame : true,
  bodyBorder : false,
  items : [    {
      layout : "column",
      bodyBorder : false,
      items : [        {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "processdefkey",
              fieldLabel : "流程定义key",
              width : 150,
              allowBlank : false,
              xtype : "textfield"
          }]
      },        {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "taskdefkey",
              fieldLabel : "任务定义key",
              width : 150,
              allowBlank : false,
              xtype : "textfield"
          }]
      },        {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "roleid",
              fieldLabel : "角色id",
              width : 150,
              allowBlank : false,
              xtype : "textfield"
          }]
      },        {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "create_people",
              fieldLabel : "创建人",
              width : 150,
              allowBlank : true,
              xtype : "textfield"
          }]
      },        {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "create_time",
              fieldLabel : "创建时间",
              width : 150,
              allowBlank : true,
              xtype : "datefield",
              format : "Y-m-d H:i:s"
          }]
      },        {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "update_people",
              fieldLabel : "更新人",
              width : 150,
              allowBlank : true,
              xtype : "textfield"
          }]
      },        {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "update_time",
              fieldLabel : "更新时间",
              width : 150,
              allowBlank : true,
              xtype : "datefield",
              format : "Y-m-d H:i:s"
          }]
      }]
  }],
  buttonAlign : "center",
  buttons : [    {
      text : "保存",
      width : 100,
      height : 25,
      iconCls : "disk",
      handler : function() {var _forma = Ext.getCmp('EditInfoForm');if (_forma.getForm().isValid()){if(_forma.record){_forma.getForm().updateRecord(_forma.record);roleStore.save();}}}
  },    {
      text : "取消",
      width : 100,
      height : 25,
      iconCls : "arrow-undo",
      handler : function() {_editInfoWin.hide();}
  }]
},'panel');
this.EditInfoForm=EditInfoForm;this.__caches__.push(EditInfoForm);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    layout : "border",
    items : [         {
        xtype : "grid",
        itemId : "roleGrid",
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
        store : roleStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:roleStore,displayInfo: true}),
        tbar : [
        {text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
                  var _grid=this.ownerCt.ownerCt;
	              var recordType=_grid.getStore().recordType;
	              var nr=new recordType();
	              
		          roleEditwin=Ext.getCmp('roleEditwin');
				    if(!roleEditwin){ 
					    roleEditwin=new Ext.Window({
							title:'新增角色',
							id:'roleEditwin',
							layout:'fit',
							width:300,
							height:230,
							closeAction:'hide',
							plain: true, 
							modal: true,
							autoDestroy:false,
							items: RoleEditForm
						}); 
					} 
				  roleEditwin.show();
			      RoleEditForm.getForm().reset();
	              RoleEditForm.record=nr;
 	              RoleEditForm.getForm().loadRecord(nr);	
			      RoleEditForm.getForm().reset(); 
         }
        },{xtype: 'tbseparator'},
        {text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}},{xtype: 'tbseparator'},
        {text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){var _grid=this.ownerCt.ownerCt;Ext.Msg.confirm('保存数据','确认保存已修改数据?',function(btn){if(btn == 'yes'){_grid.getStore().save();}});}}],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        {width:100,sortable:true,header:'流程定义key',dataIndex:'processdefkey'},
        {width:100,sortable:true,header:'任务定义key',dataIndex:'taskdefkey'},
           {width:100,sortable:true,header:'任务定义名称',dataIndex:'taskdefname'},
        {width:100,sortable:true,header:'角色id',dataIndex:'roleid'},
        {width:100,sortable:true,header:'角色名称',dataIndex:'rolename'},
        {width:100,sortable:true,header:'创建人',dataIndex:'create_people'},
        {width:100,sortable:true,header:'创建时间',dataIndex:'create_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s')},
        {width:100,sortable:true,header:'更新人',dataIndex:'update_people'},
        {width:100,sortable:true,header:'更新时间',dataIndex:'update_time',renderer:Ext.util.Format.dateRenderer('Y-m-d H:i:s')}]),
        sm : new Ext.grid.CheckboxSelectionModel({ 
        listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}}),
        listeners : {rowdblclick : function(g,rowIndex,e){var _rec=g.getSelectionModel().getSelected();openEditInfoWin(_rec);}}
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};