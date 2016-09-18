Ext.namespace("jsp.platform.sm");jsp.platform.sm.userlist=function(){var __caches__=[];this.__caches__=__caches__;
var currUserId = JrafSession.get('userid');
dsRender=function(v){
    if (v=='1'){
	   return '是';
	}
    if (v=='0'){
	   return '否';
	}
    if (v=='true'){
	   return '是';
	}
    if (v=='false'){
	   return '否';
	}
	if (v){
	    return '是';
	}
	if (!v){
	   return '否';
	}
};
stateRender=function(v){
    if (v=='9'){
	   return '是';
	}
    else{
	   return '否';
	}
};
var userwin;
function openUserWin(UserForm,rec){    
    userwin=Ext.getCmp('userwin');
    if(!userwin){ 
	    userwin=new Ext.Window({
			title:'用户帐号信息',
			id:'userwin',
			layout:'fit',
			width:600,
			height:370,
			closeAction:'hide',
			plain: true, 
			modal: true,
			autoDestroy:false,
			items: UserForm
		}); 
	} 
	
	if (!rec.data['userid']){	     
	     userwin.show();
	     UserForm.getForm().reset();
	     UserForm.record=rec;
 	     UserForm.getForm().loadRecord(rec);		 
    }
	else{	    
		jr=new JrafRequest('pcmc','userrole','getUserDetail',{recordType:Jraf.UserDRecord,idProperty:'userid'});
		jr.setExtraPs({userid:rec.data['userid']});
		jr.setSuccFn(function(a,_resp,xr){
		    userwin.show();
			newRec=a.records[0];
			UserForm.getForm().reset();
            UserForm.record=rec;
			UserForm.getForm().loadRecord(newRec);
			UserForm.getForm().findField('udeptid').setComboVal(rec.get('deptid'),rec.get('deptname'));
		});
		jr.postData();
	}
	
};var RoleRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "subsysid",
    fieldLabel : "子系统流水号",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "rolename",
    fieldLabel : "角色名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "createuser",
    fieldLabel : "操作者流水号",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "remark",
    fieldLabel : "备注",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "mrole",
    fieldLabel : "管理角色",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "roleid",
    fieldLabel : "roleid",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "cnname",
    fieldLabel : "label",
    type : "string",
    allowBlank : true
}]);
this.RoleRecord=RoleRecord;this.__caches__.push(RoleRecord);var UserRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "userid",
    fieldLabel : "userid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "usercode",
    fieldLabel : "用户帐号",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "username",
    fieldLabel : "用户名称",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "userpwd",
    fieldLabel : "密码",
    type : "string",
    allowBlank : true
}, {
    xtype : "Field",
    name : "idnumber",
    fieldLabel : "身份证号",
    type : "string",
    allowBlank : true
}, {
    xtype : "Field",
    name : "email",
    fieldLabel : "邮箱",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "emailbind",
    fieldLabel : "emailbind",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "mobilebind",
    fieldLabel : "mobilebind",
    type : "int",
    allowBlank : true
},   {
    xtype : "Field",
    name : "mobile",
    fieldLabel : "手机",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "deptname",
    fieldLabel : "部门名称",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "phone",
    fieldLabel : "联系电话",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "state",
    fieldLabel : "是否激活",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "usertype",
    fieldLabel : "用户类型",
    type : "string",
    allowBlank : true
}]);
this.UserRecord=UserRecord;this.__caches__.push(UserRecord);var UserDRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "userid",
    fieldLabel : "userid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "usercode",
    fieldLabel : "用户帐号",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "username",
    fieldLabel : "用户名称",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "userpwd",
    fieldLabel : "密码",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "deptid",
    fieldLabel : "deptid",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "deptcode",
    fieldLabel : "部门代码",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "deptname",
    fieldLabel : "部门名称",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "phone",
    fieldLabel : "联系电话",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "disable",
    fieldLabel : "是否激活",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "urole",
    fieldLabel : "用户角色",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "subject",
    fieldLabel : "管理专业类型",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "work_dept",
    fieldLabel : "上班地点代码",
    type : "string",
    allowBlank : true
}, {
    xtype : "Field",
    name : "work_dept_name",
    fieldLabel : "上班地点",
    type : "string",
    allowBlank : true
}, {
    xtype : "Field",
    name : "remark",
    fieldLabel : "备注",
    type : "string",
    allowBlank : true
}]);
this.UserDRecord=UserDRecord;this.__caches__.push(UserDRecord);var MRoleStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "MRoleStore",
  recordType : RoleRecord,
  idProperty : "roleid",
  api : {read:{sysName:"pcmc",oprID:"userrole",actions:"getMRoleList"}},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.MRoleStore=MRoleStore;this.__caches__.push(MRoleStore);var UserStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "UserStore",
  recordType : UserRecord,
  idProperty : "userid",
  api : {
read:{sysName:"pcmc",oprID:"userrole",actions:"getUserList"},
destroy:{sysName:"pcmc",oprID:"userrole",actions:"deleteUser"}
},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.UserStore=UserStore;this.__caches__.push(UserStore);var UserForm=Ext.create({
  xtype : "form",
  classname : "UserForm",
  defaultType : "textfield",
  frame:true,
  id : "UserForm",
  items : [    {
      fieldLabel : "登录用户名",
      name : "usercode",
	  maxLength : 20,
	  width : 300,
	  minLength : 2,
      allowBlank : false
  },    {
      fieldLabel : "登录密码",
      id : "userpwda",
      name : "userpwd",
	  width : 300,
	  maxLength : 12,
	  minLength : 3,
      inputType : "password",
      allowBlank : true
  },    {
      fieldLabel : "重复登录密码",
      name : "userpwd1",
      inputType : "password",
      vtype : "valEqual",
	  width : 300,
	  maxLength : 12,
	  minLength : 3,
      valEqual : {field:"userpwda"},
      allowBlank : true
  },    {
      fieldLabel : "用户全称",
      name : "username",
	  width : 300,
      allowBlank : false
  },  {
      fieldLabel : "联系电话",
      id : "phone",
	  vtype : "phone",
	  width : 300,
      name : "phone"
  },  {
      fieldLabel : "手机号码",
      id : "mobile",
	  vtype : "mobile",
	  width : 300,
      name : "mobile"
  },   {
      fieldLabel : "电子邮箱",
      id : "email",
	  width : 300,
	  vtype : "email",
      name : "email"
  },     {
      xtype : "numberfield",
      name : "pagesize",
	  width : 300,
      fieldLabel : "查询显示记录数",
      allowDecimals : false,
      allowBlank : false,
      minValue : 5,
      value : 15
  },   {
      xtype : "depttreecombo",
      fieldLabel : "所属机构",
      id : "udeptid",
      name : "deptid",
      hiddenName : "deptid",
      valueField : "deptid",
      displayField : "deptname",
      triggerAction : "all",
      width : 300,
      lazyRender : true,
      editable : false,
      forceSelection : true,
      baseParams:{PageSize:'-1'},
      allowBlank : false
  },      {
	  xtype : "paracombo",
      fieldLabel : "是否冻结",
      name : "state",
      hiddenName : "state",
      baseParams : {paramname:'global_yes_or_no'},
      editable : false,
      allowBlank : false
  },{
      xtype : "multicombo",
      fieldLabel : "角色授权",
      id : "userrole",
      name : "urole",
      hiddenName : "urole",
      store : MRoleStore,
      mode : "local",
      displayField : "rolename",
      tplField : "{cnname}--{rolename}",
      valueField : "roleid",
      allowBlank : false,
      width : 300
  },   {
	  name : 'userid',
	  hidden : true,
	  hideLabel : true
  },     {
	  name : 'usertype',
	  hidden : true,
	  value:"9",
	  hideLabel : true
  }
  ],
  buttonAlign : "center",
  labelAlign:"right",
  labelWidth : 120,
  buttons : [{
		text : "保存",				
		handler : function() {		 
		    _forma = Ext.getCmp('UserForm');
			if(_forma.record)
			{
			    var newFlag = Ext.isEmpty(_forma.record.data['userid']);
				var jr;
				
				if (newFlag){
				   jr = new JrafRequest('pcmc','userrole','addUser',{recordType:UserDRecord,idProperty:'userid'});
				   var _grid=Ext.getCmp('user-grid');
                   _grid.getStore().add(_forma.record);
                   _grid.getSelectionModel().selectLastRow();
				}
				else{
				   jr = new JrafRequest('pcmc','userrole','updateUser',{recordType:UserDRecord,idProperty:'userid'});
				}
				jr.setForm('UserForm');
				jr.setSuccFn(function(a,_resp,xr){
					_forma.getForm().updateRecord(_forma.record);
					_forma.record.set('deptname',_forma.getForm().findField('udeptid').getRawValue());					
					_forma.record.set('work_dept_name',_forma.getForm().findField('workdeptid').getRawValue());
					if (newFlag){
					   xr.realize(_forma.record,a.records);
					   var _grid = Ext.getCmp('user-grid');
                       _grid.getSelectionModel().selectLastRow();
					}
					Ext.getCmp('userwin').hide();
				});
				jr.postData();
			}
		}
   }]
},'panel');
this.UserForm=UserForm;this.__caches__.push(UserForm);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    layout : "border",
    items : [      {
        xtype : "form",
        region : "north",
        autoHeight : true,
        items : [          {
            layout : "column",
            items : [              {
                layout : "form",
                columnWidth : 0.5,
                border : false,
                items : [                  {
                    fieldLabel : "用户帐号",
                    name : "qry_usercode",
                    width : 150,
                    xtype : "textfield"
                }]
            },              {
                layout : "form",
                columnWidth : 0.5,
                border : false,
                items : [                  {
                    fieldLabel : "用户名称",
                    name : "qry_username",
                    width : 150,
                    xtype : "textfield"
                }]
            },              {
                layout : "form",
                columnWidth : 0.8,
                border : false,
                items : [                  {
                    xtype : "depttreecombo",
                    fieldLabel : "所属机构",
                    name : "deptid",
                    hiddenName : "qry_deptcode",
                    valueField : "deptcode",
                    displayField : "deptname",
                    triggerAction : "all",
                    width : 350,
                    lazyRender : true,
                    editable : false,
                    forceSelection : true
                }]
            }]
        }],
        id : "user-qform",
        buttons : [{
	       text : '查询',
		   handler : function() {
			  var _form=Ext.getCmp('user-qform');
			  UserStore.setFormParam(_form);
			  UserStore.setPageInfo(JrafSession.get('PageSize'),'1');
		      UserStore.load();
		   }
        }],
        buttonAlign : "center",
        frame : true,
        labelWidth : 100,
        labelAlign : "left"
    },      {
        xtype : "grid",
        region : "center",
        frame : true,
        title : "用户列表",
        viewConfig : {forceFit:true},
        columnLines : true,
        autoHeight : false,
        height : 320,
        id : "user-grid",
        store : UserStore,
        tbar : [
		   {text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
		        var _grid = Ext.getCmp('user-grid');
		        var recordType=_grid.getStore().recordType;
				var nr=new recordType();
                openUserWin(UserForm,nr);
		   }},
           {xtype: 'tbseparator'},
           {xtype: 'tbfill'},{xtype: 'tbseparator'},	
		   {text:'冻结',iconCls:'lock',ref: '../lockBtn',disabled: true,handler:function(){
               var _grid=Ext.getCmp('user-grid');
               var _rec = _grid.getSelectionModel().getSelected();
				if (currUserId==_rec.get('userid') || 'admin'==_rec.get('usercode')){
					alert('不能删除自己和超级管理员');
					return;
				}
               var jr = new JrafRequest('pcmc','userrole','setUserDisable');			   jr.setExtraPs({'userid':_rec.get('userid'),'deptid':_rec.get('deptid'),'usercode':_rec.get('usercode'),'username':_rec.get('username'),'pagesize':'15','state':'9'});
               jr.setSuccFn(function(a,_resp,xr){
			       _rec.set('state','9');
				   _grid.unlockBtn.enable();
				   _grid.lockBtn.disable();
               });
               jr.postData();
		   }},{xtype: 'tbseparator'},
		   {text:'激活',iconCls:'lock-open',ref: '../unlockBtn',disabled: true,handler:function(){
               var _grid=Ext.getCmp('user-grid');
               var _rec = _grid.getSelectionModel().getSelected();
               var jr = new JrafRequest('pcmc','userrole','setUserDisable');               jr.setExtraPs({'userid':_rec.get('userid'),'deptid':_rec.get('deptid'),'usercode':_rec.get('usercode'),'username':_rec.get('username'),'pagesize':'15','state':'1'});
               jr.setSuccFn(function(a,_resp,xr){
			       _rec.set('state','1');
				   _grid.unlockBtn.disable();
				   _grid.lockBtn.enable();
               });
               jr.postData();                
		   }},{xtype: 'tbseparator'}		   
		],
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:UserStore,displayInfo: true}),
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
	        {width:60,sortable:true,header:'用户帐号',dataIndex:'usercode'},
			{width:80,sortable:true,header:'用户名称',dataIndex:'username'},
			{width:100,sortable:true,header:'所属机构',dataIndex:'deptname'},
			{width:60,sortable:true,header:'手机号码',dataIndex:'mobile'},
			{width:40,sortable:true,header:'绑定手机',dataIndex:'mobilebind',renderer:dsRender},
			{width:60,sortable:true,header:'电子邮箱',dataIndex:'email'},
			{width:40,sortable:true,header:'绑定邮箱',dataIndex:'emailbind',renderer:dsRender},
			{width:40,sortable:true,header:'是否冻结',dataIndex:'state',renderer:stateRender},
			{width:80,sortable:true,header:'用户类型',dataIndex:'usertype',renderer:Ext.util.Format.paramRenderer('usertype','')}
			]),
        sm : new Ext.grid.CheckboxSelectionModel({
		    singleSelect:true,
			listeners:{
		       selectionchange:function(sm){
			       var _grid=Ext.getCmp('user-grid');
				   if(sm.getCount()){
					  var _rec = sm.getSelected();
					  if (_rec.get('state')=='9'){
					     _grid.unlockBtn.enable();
						 _grid.lockBtn.disable();
					  }
					  else{
					     _grid.unlockBtn.disable();
						 _grid.lockBtn.enable();					     
					  }
				   }
				   else{
                      _grid.lockBtn.disable();
					  _grid.unlockBtn.disable();
				   }
			    }
			 }
		}),
        stripeRows : true,
		listeners : {
            rowdblclick : function(grid,rowIndex,e){
                var rec=grid.getSelectionModel().getSelected();	
                openUserWin(UserForm,rec);
           }
        }
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){UserStore.setFormParam(Ext.getCmp('user-qform'));
	  UserStore.setPageInfo(JrafSession.get('PageSize'),'1');
	  UserStore.load();};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};