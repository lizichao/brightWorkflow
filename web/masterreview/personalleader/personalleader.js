Ext.namespace("jsp.masterreview.personalleader");jsp.masterreview.personalleader.personalleader=function(){var __caches__=[];this.__caches__=__caches__;
       var _editInfoWin; 
       function openEditInfoWin(_rec){ 
         _editInfoWin=Ext.getCmp('editInfoWin'); 
         if(!_editInfoWin){
          _editInfoWin=new Ext.Window(
            {title:'数据录入',
             id:'editInfoWin',
             layout:'fit',
             width:600,
             height:300,
             closeAction:'hide',
             plain:true,
             modal: true,
             items:EditInfoForm
             });
          } 
          _editInfoWin.show();
          EditInfoForm.getForm().reset();
          EditInfoForm.getForm().loadRecord(_rec);
          if(_rec.data.id){
            EditInfoForm.getForm().findField('deptid').setComboVal(_rec.get('deptid'),_rec.get('deptname'));
          }
          EditInfoForm.record=_rec;
        }
    var PersonalleaderRec=Ext.data.Record.create([  {
    xtype : "Field",
    name : "id",
    width : 150,
    fieldLabel : "主键id",
    allowBlank : false,
    type : "string"
},   {
    xtype : "Field",
    name : "userid",
    width : 150,
    fieldLabel : "校长主键id",
    allowBlank : false,
    type : "string"
}, {
    xtype : "Field",
    name : "username",
    width : 150,
    fieldLabel : "姓名",
    allowBlank : false,
    type : "string"
}, {
    xtype : "Field",
    name : "usersex",
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
}, {
    xtype : "Field",
    name : "deptid",
    width : 150,
    fieldLabel : "部门id",
    allowBlank : false,
    type : "string"
}, {
    xtype : "Field",
    name : "deptname",
    width : 150,
    fieldLabel : "部门",
    allowBlank : false,
    type : "string"
},   {
    xtype : "Field",
    name : "address",
    width : 150,
    fieldLabel : "地址",
    allowBlank : true,
    type : "string"
}, {
    xtype : "Field",
    name : "email",
    fieldLabel : "邮箱",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "mobile",
    fieldLabel : "移动电话",
    type : "string",
    allowBlank : true
}]);
this.PersonalleaderRec=PersonalleaderRec;this.__caches__.push(PersonalleaderRec);var PersonalleaderStore=Ext.create({
  xtype : "Store",
  classname : "PersonalleaderStore",
  type : "JrafXmlStore",
  recordType : PersonalleaderRec,
  idProperty : "id",
  api : {
    read :     {
      sysName : "headmaster",
      oprID : "personalLeaderAction",
      actions : "findPersonalLeader"
  },
    create :     {
      sysName : "headmaster",
      oprID : "personalLeaderAction",
      actions : "addPersonalLeader"
  },
    update :     {
      sysName : "headmaster",
      oprID : "personalLeaderAction",
      actions : "updatePersonalLeader"
  },
    destroy :     {
      sysName : "headmaster",
      oprID : "personalLeaderAction",
      actions : "deletePersonalLeader"
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
this.PersonalleaderStore=PersonalleaderStore;this.__caches__.push(PersonalleaderStore);var EditInfoForm=Ext.create({
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
              name : "id",
              fieldLabel : "主键id",
              width : 150,
              allowBlank : false,
              xtype : "hidden"
          }]
      },      {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "userid",
              fieldLabel : "用户id",
              width : 150,
              allowBlank : false,
              xtype : "hidden"
          }]
      },  {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "username",
              fieldLabel : "名称",
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
              name : "usercode",
              fieldLabel : "账号",
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
              name : "idnumber",
              fieldLabel : "身份证号",
              width : 150,
              allowBlank : false,
              xtype : "textfield"
          }]
      },       {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "mobile",
              fieldLabel : "手机",
              width : 150,
              allowBlank : true,
              xtype : "textfield"
          }]
      },   {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "email",
              fieldLabel : "邮箱",
              width : 150,
              allowBlank : true,
              xtype : "textfield"
          }]
      },   {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "address",
              fieldLabel : "地址",
              width : 150,
              allowBlank : true,
              xtype : "textfield"
          }]
      },{
                layout : "form",
                columnWidth :  1,
                bodyBorder : false,
                  labelAlign : "right",
                items : [                  {
                    xtype : "depttreecombo",
				      fieldLabel : "所属机构",
				      id : "udeptid",
				      name : "deptid",
				      hiddenName : "deptid",
				      valueField : "deptid",
				      displayField : "deptname",
				      triggerAction : "all",
				      width : 180,
				      lazyRender : true,
				      editable : false,
				      forceSelection : true,
				      baseParams:{PageSize:'-1'},
				      allowBlank : false,
				      rootid : '8a21b3ab4d23c0a7014d2c5f4910001a',
	                 roottx  : '深圳市',
                }]
            }]
  }],
  id : "EditInfoForm",
  buttonAlign : "center",
  buttons : [    {
      text : "保存",
      width : 100,
      height : 25,
      iconCls : "disk",
      handler : function() {
                var _form=this.ownerCt.ownerCt;
	            if(!_form.getForm().isValid()) return;
				var rec=_form.record;
				var newFlag = Ext.isEmpty(rec.data['id']);
				var jr;
			    if(newFlag){
		            jr=new JrafRequest('headmaster','personalLeaderAction','addPersonalLeader',{recordType:PersonalleaderRec,idProperty:'id'});
		        }else{
		            jr=new JrafRequest('headmaster','personalLeaderAction','updatePersonalLeader',{recordType:PersonalleaderRec,idProperty:'id'});
		        }
				jr.setForm(_form);
		        jr.setSuccFn(function(a,_resp,xr){
		            _form.getForm().updateRecord(_form.record);
		            if(newFlag){
		               xr.realize(rec,a.records);
		            }
				    _form.ownerCt.hide();
				    PersonalleaderStore.load();
		        });
		        jr.postData();
      }
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
    items : [      {
        xtype : "form",
        region : "north",
        autoHeight : true,
        title : "查询条件",
        frame : true,
        items : [          {
            layout : "column",
            bodyBorder : false,
            items : [              {
                layout : "form",
                bodyBorder : false,
                columnWidth : 0.33,
                items : [                  {
                    name : "username",
                    fieldLabel : "姓名",
                    width : 150,
                    allowBlank : true,
                    xtype : "textfield"
                }]
            },              {
                layout : "form",
                bodyBorder : false,
                columnWidth : 0.33,
                items : [                  {
                    name : "usercode",
                    fieldLabel : "帐号",
                    width : 150,
                    allowBlank : true,
                    xtype : "textfield"
                }]
            },              {
                layout : "form",
                bodyBorder : false,
                columnWidth : 0.33,
                items : [                  {
                    name : "idnumber",
                    fieldLabel : "身份证号码",
                    width : 150,
                    allowBlank : true,
                    xtype : "textfield"
                }]
            }],
            anchor : "100%"
        }],
        buttonAlign : "center",
        id : "PersonalleaderQryForm",
        buttons : [        {
          text : "查询",
          handler : function(){
            var qStore=Ext.getCmp('PersonalleaderGrid').getStore();
            qStore.setFormParam(Ext.getCmp('PersonalleaderQryForm'));
            qStore.setPageInfo(JrafSession.get('PageSize'),'1');
            qStore.load();
         }
      }]
    },      {
        xtype : "grid",
        id : "PersonalleaderGrid",
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
        store : PersonalleaderStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:PersonalleaderStore,displayInfo: true}),
        tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
	          var _grid=this.ownerCt.ownerCt;
	          var recordType=_grid.getStore().recordType;
	          var nr=new recordType();
	          _grid.getStore().add(nr);
	          _grid.getSelectionModel().selectLastRow();
	          openEditInfoWin(nr);
          }
        },{xtype: 'tbseparator'},
        {text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
	          var _grid=this.ownerCt.ownerCt;
	          var ckrs=_grid.getSelectionModel().getSelections();
               	var param={};
				for(var i=0;i<ckrs.length;i++){
				    var id = ckrs[i].get('userid');
				 	param['id']=!param['id'] ? id:[].concat(param['id']).concat(id);
				 	_grid.getStore().remove(ckrs[i]);
				}
				var jr=new JrafRequest('headmaster','personalLeaderAction','deletePersonalLeader');
		 		jr.setExtraPs(param);
				jr.setSuccFn(function(data,status){
			    
			    });
				jr.postData();
          }
        },{xtype: 'tbseparator'},
        {text:'保存',iconCls:'disk',ref: '../saveBtn',handler:function(){
	         var _grid=this.ownerCt.ownerCt;
	         Ext.Msg.confirm('保存数据','确认保存已修改数据?',
	           function(btn){ 
	             if(btn == 'yes'){
	               _grid.getStore().save();
	             }
	            }
	          );
          }
        }],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        {width:100,sortable:true,header:'名称',dataIndex:'username',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'账号',dataIndex:'usercode',renderer:Ext.util.Format.paramRenderer('undefined','')},
             {width:100,sortable:true,header:'部门id',dataIndex:'deptid',renderer:Ext.util.Format.paramRenderer('undefined',''),hidden : true},
        {width:100,sortable:true,header:'部门',dataIndex:'deptname',renderer:Ext.util.Format.paramRenderer('undefined','')},
           {width:80,sortable:true,header:'移动电话',dataIndex:'mobile'},
        {width:100,sortable:true,header:'身份证号',dataIndex:'idnumber',renderer:Ext.util.Format.paramRenderer('undefined','')},
          {width:100,sortable:true,header:'邮箱',dataIndex:'email',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'地址',dataIndex:'address',renderer:Ext.util.Format.paramRenderer('undefined','')}]),
        sm : new Ext.grid.CheckboxSelectionModel({ 
          listeners:{
	        selectionchange:function(sm){
	           var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}
	        }
          }
        }),
        listeners : {rowdblclick : function(g,rowIndex,e){var _rec=g.getSelectionModel().getSelected();openEditInfoWin(_rec);}}
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};