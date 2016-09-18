Ext.namespace("jsp.masterreview.professor");jsp.masterreview.professor.headmasterselect=function(){var __caches__=[];this.__caches__=__caches__;var _group_id,_leavel
    function onload() {
        //DeviceStore.setBaseParam('deptId',_deptId);
	    HeadMasterStore.load();
	}var HeadMasterRec=Ext.data.Record.create([  {
    xtype : "Field",
    name : "id",
    width : 150,
    fieldLabel : "主键id",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "userid",
    width : 150,
    fieldLabel : "校长主键id",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "username",
    width : 150,
    fieldLabel : "姓名",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "user_sex",
    width : 150,
    fieldLabel : "性别",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "usercode",
    width : 150,
    fieldLabel : "账号",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "userpwd",
    width : 150,
    fieldLabel : "密码",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "idnumber",
    width : 150,
    fieldLabel : "身份证号",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "deptname",
    width : 150,
    fieldLabel : "部门",
    allowBlank : false,
    type : "string"
},  {
    xtype : "Field",
    name : "address",
    width : 150,
    fieldLabel : "地址",
    allowBlank : true,
    type : "string"
},  {
    xtype : "Field",
    name : "mobile",
    fieldLabel : "移动电话",
    type : "string",
    allowBlank : true
}]);
this.HeadMasterRec=HeadMasterRec;this.__caches__.push(HeadMasterRec);var HeadMasterStore=Ext.create({
  xtype : "Store",
  classname : "HeadMasterStore",
  type : "JrafXmlStore",
  recordType : HeadMasterRec,
  idProperty : "id",
  api : {
    read :     {
      sysName : "headmaster",
      oprID : "headMasterbase",
      actions : "findMaster"
  },
    create :     {
      sysName : "headmaster",
      oprID : "headMasterbase",
      actions : "addMaster"
  },
    update :     {
      sysName : "headmaster",
      oprID : "headMasterbase",
      actions : "updateMaster"
  },
    destroy :     {
      sysName : "headmaster",
      oprID : "headMasterbase",
      actions : "deleteMaster"
  }
},
  autoLoad : true,
  autoSave : false,
  paramsAsHash : true,
  remoteSort : true,
  listeners : {
    write : function (store,action,result,res,rs){if (res.success){if (_editInfoWin){_editInfoWin.hide();}}}
}
},'Store');
this.HeadMasterStore=HeadMasterStore;this.__caches__.push(HeadMasterStore);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    layout : "border",
    items : [      {
        xtype : "grid",
        id : "HeadMasterGrid",
        region : "center",
        autoWidth : true,
        frame : true,
        title : "查询结果",
        viewConfig : {
          forceFit : false
        },
        stripeRows : true,
        columnLines : true,
        autoHeight : false,
        height : 320,
        store : HeadMasterStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:HeadMasterStore,displayInfo: true}),
        tbar : [{text:'添加校长',iconCls:'add',ref: '../addBtn',handler:function(){
               	var _grid=Ext.getCmp('HeadMasterGrid');
	         	var addDeviceArray = [];
				var ckrs=_grid.getSelectionModel().getSelections();
				for(var i=0;i<ckrs.length;i++){
			         var eachSel = ckrs[i];
			         addDeviceArray.push(eachSel.data.userid);
		    	}
		    	if(ckrs.length==0){
		              Ext.MessageBox.alert("提示","请先勾选要添加的记录！！");
                      return false;
		        }
		    	var jr=new JrafRequest('headmaster','groupAction','addHeadMasterList');
				jr.setExtraPs({addHeadMasterIds:addDeviceArray,group_id:_group_id});
				jr.setSuccFn(function(a,_resp,xr){
				    JrafUTIL.getCmp("jsp.masterreview.professor.professorgroup").closeFolderWin();
				});
				jr.postData();
          }
        }],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        {width:100,sortable:true,header:'id',dataIndex:'userid',renderer:Ext.util.Format.paramRenderer('undefined',''),hidden : true},
        {width:100,sortable:true,header:'姓名',dataIndex:'username',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'账号',dataIndex:'usercode',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'部门id',dataIndex:'deptid',renderer:Ext.util.Format.paramRenderer('undefined',''),hidden : true},
        {width:100,sortable:true,header:'部门',dataIndex:'deptname',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:80,sortable:true,header:'移动电话',dataIndex:'mobile'},
        {width:100,sortable:true,header:'身份证号',dataIndex:'idnumber',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'地址',dataIndex:'address',renderer:Ext.util.Format.paramRenderer('undefined','')}]),
        sm : new Ext.grid.CheckboxSelectionModel({ 
          listeners:{
	        selectionchange:function(sm){
	          // var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}
	        }
          }
        }),
        listeners : {rowdblclick : function(g,rowIndex,e){var _rec=g.getSelectionModel().getSelected();openEditInfoWin(_rec);}}
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){_group_id = JrafUTIL.getParam(arguments[0],'_group_id');
	   onload();};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};