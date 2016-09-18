Ext.namespace("jsp.masterreview.headmaster");jsp.masterreview.headmaster.headmaster=function(){var __caches__=[];this.__caches__=__caches__;
       var _editInfoWin; 
       function openEditInfoWin(_rec){ 
         _editInfoWin=Ext.getCmp('editInfoWin'); 
         if(!_editInfoWin){
          _editInfoWin=new Ext.Window(
            {title:'数据录入',
             id:'editInfoWin',
             layout:'fit',
             width:700,
             height:350,
             closeAction:'hide',
             plain:true,
             modal: true,
             items:EditInfoForm
             });
          } 
          _editInfoWin.show();
          EditInfoForm.getForm().reset();
      
          if(_rec.data.id){
            EditInfoForm.getForm().findField('deptid').setComboVal(_rec.get('deptid'),_rec.get('deptname'));
          }
         EditInfoForm.getForm().loadRecord(_rec);
          EditInfoForm.record=_rec;
        }
    var HeadMasterRec=Ext.data.Record.create([ {
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
},{
    xtype : "Field",
    name : "deptid",
    width : 150,
    fieldLabel : "部门id",
    allowBlank : false,
    type : "string"
},{
    xtype : "Field",
    name : "deptname",
    width : 150,
    fieldLabel : "部门名称",
    allowBlank : false,
    type : "string"
},   {
    xtype : "Field",
    name : "address",
    width : 150,
    fieldLabel : "地址",
    allowBlank : true,
    type : "string"
},  {
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
},{
    xtype : "Field",
    name : "phasestudy",
    fieldLabel : "学段",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "ispositive",
    fieldLabel : "正副校长",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "school_class",
    fieldLabel : "学校类型",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "present_occupation",
    fieldLabel : "现任职务",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "present_major_occupation",
    fieldLabel : "现任专业技术职务",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "join_work_time",
    fieldLabel : "参加工作时间",
    type : "date",
    dateFormat : "Y-m-d",
    allowBlank : true
},{
    xtype : "Field",
    name : "join_educate_work_time",
    fieldLabel : "参加教育工作时间",
     type : "date",
    dateFormat : "Y-m-d",
    allowBlank : true
},{
    xtype : "Field",
    name : "politics_status",
    fieldLabel : "政治面貌",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "teach_age",
    fieldLabel : "教龄",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "native_place",
    fieldLabel : "籍贯",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "census_register",
    fieldLabel : "户籍",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "nation",
    fieldLabel : "民族",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "person_img_attachId",
    fieldLabel : "个人头像附件id",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "person_img_filePath",
    fieldLabel : "个人头像附件路径",
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
this.HeadMasterStore=HeadMasterStore;this.__caches__.push(HeadMasterStore);var EditInfoForm=Ext.create({
  xtype : "form",
  classname : "EditInfoForm",
  frame : true,
  bodyBorder : false,
  items : [    {
      layout : "column",
      bodyBorder : false,
      items : [      {
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
      }, {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "username",
              fieldLabel : "姓名",
              width : 150,
              allowBlank : false,
              xtype : "textfield"
          }]
      },      {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "usercode",
              fieldLabel : "账号",
              width : 150,
              allowBlank : false,
              xtype : "hidden"
          }]
      }, {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "idnumber",
              fieldLabel : "身份证号",
              width : 150,
              vtype : "idcard",
              allowBlank : false,
              xtype : "textfield"
          }]
      },   {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "usersex",
              fieldLabel : "性别",
              width : 150,
              allowBlank : true,
              xtype : "paracombo",
              hiddenName : "usersex",
              baseParams : {paramname:'headmaster_user_sex'}
          }]
      },  {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "phasestudy",
              fieldLabel : "学段",
              width : 150,
              allowBlank : true,
              xtype : "paracombo",
              hiddenName : "phasestudy",
              baseParams : {paramname:'headmaster_phase_study'}
          }]
      },   {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "ispositive",
              fieldLabel : "正副校长",
              width : 150,
              allowBlank : true,
              xtype : "paracombo",
              hiddenName : "ispositive",
              baseParams : {paramname:'headmaster_ispositive'}
          }]
      }, {
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
      }, {
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
      },     {
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
      }, {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "present_occupation",
              fieldLabel : "现任职务",
              width : 150,
              allowBlank : true,
              xtype : "textfield"
          }]
      }, {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "school_class",
              fieldLabel : "学校类型",
              width : 150,
              allowBlank : true,
              xtype : "paracombo",
              hiddenName : "school_class",
              baseParams : {paramname:'headmaster_school_type'}
          }]
      }, {
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "present_major_occupation",
              fieldLabel : "现任专业技术职务",
              width : 150,
              allowBlank : true,
              xtype : "textfield"
          }]
      },{
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "join_work_time",
              fieldLabel : "参加工作时间",
              width : 150,
              allowBlank : true,
              xtype : "datefield",
              format : "Y-m-d"
          }]
      },{
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "join_educate_work_time",
              fieldLabel : "参加教育工作时间",
              width : 150,
              allowBlank : true,
               xtype : "datefield",
              format : "Y-m-d"
          }]
      },{
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "politics_status",
              fieldLabel : "政治面貌",
              width : 150,
              allowBlank : true,
              xtype : "paracombo",
              hiddenName : "politics_status",
              baseParams : {paramname:'headmaster_politics_status'}
          }]
      },{
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "teach_age",
              fieldLabel : "教龄",
              width : 150,
              allowBlank : true,
              xtype : "textfield"
          }]
      },{
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "native_place",
              fieldLabel : "籍贯",
              width : 150,
              allowBlank : true,
              xtype : "textfield"
          }]
      },{
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "census_register",
              fieldLabel : "户籍",
              width : 150,
              allowBlank : true,
              xtype : "textfield"
          }]
      },{
          layout : "form",
          bodyBorder : false,
          columnWidth : 0.5,
          labelAlign : "right",
          items : [            {
              name : "nation",
              fieldLabel : "民族",
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
		            jr=new JrafRequest('headmaster','headMasterbase','addMaster',{recordType:HeadMasterRec,idProperty:'id'});
		        }else{
		            jr=new JrafRequest('headmaster','headMasterbase','updateMaster',{recordType:HeadMasterRec,idProperty:'id'});
		        }
		        
		        /*
		         var command_net_type_val =  _forma.getForm().findField('command_net_type').getValue();
      			
      			var command_code_val =  _forma.getForm().findField('command_code').getValue();
      			if(command_net_type_val == '03' && !validateComandCode(command_code_val)){
      			    Ext.MessageBox.alert("提示","请重新输入指令代码！！");
                       return false;
      			}*/
	      			
	      			
	      	   // _form.record.data['usercode'] = _form.getForm().findField('idnumber').getValue();;
	      			 _form.getForm().findField('usercode').setValue(_form.getForm().findField('idnumber').getValue());
				jr.setForm(_form);
		        jr.setSuccFn(function(a,_resp,xr){
		            _form.getForm().updateRecord(_form.record);
		            if(newFlag){
		               xr.realize(rec,a.records);
		            }
				    _form.ownerCt.hide();
				    HeadMasterStore.load();
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
            },            /*   {
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
            },  */           {
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
        id : "HeadMasterQryForm",
        buttons : [        {
          text : "查询",
          handler : function(){
            var qStore=Ext.getCmp('HeadMasterGrid').getStore();
            qStore.setFormParam(Ext.getCmp('HeadMasterQryForm'));
            qStore.setPageInfo(JrafSession.get('PageSize'),'1');
            qStore.load();
         }
      }]
    },      {
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
					var jr=new JrafRequest('headmaster','headMasterbase','deleteMaster');
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
        {width:100,sortable:true,header:'姓名',dataIndex:'username',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'账号',dataIndex:'usercode',renderer:Ext.util.Format.paramRenderer('undefined',''),hidden : true},
        {width:100,sortable:true,header:'部门id',dataIndex:'deptid',renderer:Ext.util.Format.paramRenderer('undefined',''),hidden : true},
        {width:100,sortable:true,header:'部门',dataIndex:'deptname',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:80,sortable:true,header:'移动电话',dataIndex:'mobile'},
        {width:150,sortable:true,header:'身份证号码',dataIndex:'idnumber',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'性别',dataIndex:'usersex',renderer:Ext.util.Format.paramRenderer('headmaster_user_sex')},
        {width:100,sortable:true,header:'学段',dataIndex:'phasestudy',renderer:Ext.util.Format.paramRenderer('headmaster_phase_study')},
        {width:100,sortable:true,header:'正副校长',dataIndex:'ispositive',renderer:Ext.util.Format.paramRenderer('headmaster_ispositive')},
        {width:100,sortable:true,header:'学校类型',dataIndex:'school_class',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'现任职务',dataIndex:'present_occupation',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'现任专业技术职务',dataIndex:'present_major_occupation',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'参加工作时间',dataIndex:'join_work_time',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'参加教育工作时间',dataIndex:'join_educate_work_time',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'政治面貌',dataIndex:'politics_status',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'教龄',dataIndex:'teach_age',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'籍贯',dataIndex:'native_place',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'户籍',dataIndex:'census_register',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'民族',dataIndex:'nation',renderer:Ext.util.Format.paramRenderer('undefined','')},
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