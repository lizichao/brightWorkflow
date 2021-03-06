Ext.namespace("jsp.platform.sm");jsp.platform.sm.rolelist=function(){var __caches__=[];this.__caches__=__caches__;sysRender=function(v){
		var sto=JrafUTIL.findCmp(MainPanel,'role-grid').getTopToolbar().getComponent('syscombo').getStore();
		var index = sto.find('subsysid', v);
        if(index > -1){
            return sto.getAt(index).get('cnname');
        }
        return '';
	};
	var actAttMeta= JrafUTIL.JrafDataMeta(2);
function loadAccRec(node){
    var rec=node.attributes.record;
    var rid=node.getOwnerTree().getLoader().baseParams['roleid'];
    var sys=rec.get('sysname');
    var opr=rec.get('oprid');
    var act=rec.get('action');
    var c_acc=rec.get('colacc');
    var r_acc=rec.get('rowacc');
    
    if(""==act){
    	JrafUTIL.findCmp(aMenuTree,'actForm').getForm().reset();
    	return;
    }
    
    var jr=new JrafRequest('pcmc','sys','getActDetail',{meta:actAttMeta,recordType:actAttRecord,idProperty:'actname'});
	jr.setExtraPs({__sysName:sys,__oprID:opr,__actions:act});
    jr.setSuccFn(function(a,_resp,xr){
    	JrafUTIL.findCmp(aMenuTree,'actForm').getForm().loadRecord(a.records[0]);
    });
	jr.postData();
	
	var c_grid=JrafUTIL.findCmp(aMenuTree,'colGrid');
	if("true"==c_acc){
		c_grid.setVisible(true);
		var c_sto=c_grid.getStore();
		c_sto.setBaseParam("roleid",rid);
		c_sto.setBaseParam("sys",sys);
		c_sto.setBaseParam("opr",opr);
		c_sto.setBaseParam("act",act);
		c_sto.load();
	}
	else{
		c_grid.setVisible(false);
	}
	
	var r_panel=JrafUTIL.findCmp(aMenuTree,'rowpanel');
	if('true'==r_acc){
		r_panel.setVisible(true);
		var r_grid=JrafUTIL.findCmp(aMenuTree,'rowGrid');
		var r_sto=r_grid.getStore();
		
		var rvals=[{id:'roleid',value:rid},{id:'__sysname',value:sys},{id:'oprname',value:opr},{id:'actname',value:act}];
		var jr=new JrafRequest('pcmc','sm_permission','getRowPerms',{recordType:rowRecord,idProperty:'rolerowid'});
		jr.setExtraPs({sys:sys,opr:opr,act:act});
        jr.setSuccFn(function(a,_resp,xr){
        	r_sto.loadData(_resp.responseXML);
        	Ext.each(a.records,function(r){
        		if(rid==r.get('roleid')){
        			rvals.push({id:'sqlwhere',value:r.get('sqlwhere')});
        			return;
        		}
        	});
        	JrafUTIL.findCmp(aMenuTree,'rowForm').getForm().setValues(rvals);
        });
		jr.postData();
	}
	else{
		r_panel.setVisible(false);
	}
	
};var roleRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "subsysid",
    fieldLabel : "子系统流水号",
    type : "int",
    allowBlank : false
},  {
    xtype : "Field",
    name : "rolename",
    fieldLabel : "角色名称",
    type : "string",
    allowBlank : false
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
}]);
this.roleRecord=roleRecord;this.__caches__.push(roleRecord);var colRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "name",
    fieldLabel : "字段名",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "title",
    fieldLabel : "描述",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "colacc",
    fieldLabel : "授权可见",
    type : "boolean",
    allowBlank : true
}]);
this.colRecord=colRecord;this.__caches__.push(colRecord);var actAttRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "actname",
    fieldLabel : "name",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "desc",
    fieldLabel : "desc",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "base",
    fieldLabel : "base",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "tblname",
    fieldLabel : "tblname",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "basesql",
    fieldLabel : "basesql",
    type : "string",
    allowBlank : true
}]);
this.actAttRecord=actAttRecord;this.__caches__.push(actAttRecord);var rowRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "rolerowid",
    fieldLabel : "rolerowid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "roleid",
    fieldLabel : "roleid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "rolename",
    fieldLabel : "rolename",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "sysname",
    fieldLabel : "sysname",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "oprname",
    fieldLabel : "oprname",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "actname",
    fieldLabel : "actname",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "sqlwhere",
    fieldLabel : "sqlwhere",
    type : "string",
    allowBlank : true
}]);
this.rowRecord=rowRecord;this.__caches__.push(rowRecord);var roleStore=Ext.create({
  type : "JrafXmlStore",
  xtype : "Store",
  classname : "roleStore",
  recordType : roleRecord,
  idProperty : "roleid",
  api : {
  	read:{method:'POST',sysName:'pcmc',oprID:'userrole',actions:'getRoleBySubsys'},
	create:{method:'POST',sysName:'pcmc',oprID:'userrole',actions:'addRole'},
	update:{method:'POST',sysName:'pcmc',oprID:'userrole',actions:'updateRole'},
	destroy:{method:'POST',sysName:'pcmc',oprID:'userrole',actions:'deleteRole'}
},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.roleStore=roleStore;this.__caches__.push(roleStore);var colStore=Ext.create({
  type : "JrafXmlStore",
  xtype : "Store",
  classname : "colStore",
  recordType : colRecord,
  idProperty : "name",
  api : {read:{sysName:"pcmc",oprID:"sm_permission",actions:"getColPerms"}},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.colStore=colStore;this.__caches__.push(colStore);var rowStore=Ext.create({
  type : "JrafXmlStore",
  xtype : "Store",
  classname : "rowStore",
  recordType : rowRecord,
  idProperty : "rolerowid",
  api : {read:{sysName:"pcmc",oprID:"sm_permission",actions:"getRowPerms"}},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.rowStore=rowStore;this.__caches__.push(rowStore);var rMenuTree=Ext.create({
  xxtype : "Jpanel",
  xtype : "panel",
  classname : "rMenuTree",
  layout:'fit',
  items:[
  	{
  		xtype:'treepanel',
  		itemId : "menu-tree",
  margins : "0 0 0 5",
  cmargins : "0 0 0 0",
  lines : false,
  autoScroll : true,
  animCollapse : false,
  animate : false,
  collapseMode : "mini",
  rootVisible : false,
  root : new Ext.tree.AsyncTreeNode({id:'0'}),
  loader : new JrafXmlTreeLoader({
	        nparams: {nid:'menuid',pid:'pmenuid',ntext:'{menuname}',loadAll:true,expanded:true,chk:true,chkfld:'selected',chkval:'checked'},
	        action: {sysName:'pcmc',oprID:'sm_permission',actions:'getRoleMenuPerms'},
	        baseParams: {roleid:'-1',subsysid:'2'}
	    }),
  collapseFirst : false,
  listeners : {
			checkchange: function(node, e) {
		    	if (!node.leaf) {
		        	node.expand(true, false, function() {
		                node.eachChild(function(child) {
		                    child.ui.toggleCheck(node.attributes.checked);
		                    child.attributes.checked = node.attributes.checked;
		                });
		            });
		        }
		    },
			scope: this
		},
  buttonAlign : "center",
  frame:true,
  buttons : [{
			text : '保存',
			handler : function() {
				var mtree=this.ownerCt.ownerCt;
				var rid=mtree.getLoader().baseParams['roleid'];
				var rs=mtree.getChecked();
				var param={roleid:rid};
				for (var i=0,len=rs.length;i<len;i++) {
			    	var rec=rs[i].attributes.record;
			    	var val=rec.get('menuid');
			        param['menuid']=!param['menuid'] ? val:[].concat(param['menuid']).concat(val);
		        }
				var jr=new JrafRequest('pcmc','sm_permission','uptRoleMenuPerms');
				jr.setExtraPs(param);
				jr.postData();
			}
		}]
  	}
  ]
},'panel');
this.rMenuTree=rMenuTree;this.__caches__.push(rMenuTree);var aMenuTree=Ext.create({
  xxtype : "Jpanel",
  xtype : "panel",
  classname : "aMenuTree",
  layout : "fit",
  items : [
  	{
          layout : "border",
          items : [            {
              region : "center",
              title : "交易属性",
              layout : "border",
              items : [                {
                  xtype : "form",
                  itemId : "actForm",
                  labelWidth : 75,
                  region : "north",
                  autoHeight : true,
				  frame:true,
                  items : [                    {
                      xtype : "panel",
                      layout : "column",
                      items : [                        {
                          xtype : "panel",
                          layout : "form",
                          columnWidth : 0.35,
                          items : [                            {
                              xtype : "displayfield",
                              fieldLabel : "交易名",
                              name : "desc"
                          }],
                          border : false
                      },                        {
                          xtype : "panel",
                          layout : "form",
                          columnWidth : 0.3,
                          items : [                            {
                              xtype : "displayfield",
                              fieldLabel : "交易类型",
                              name : "base",
                              width : 61
                          }],
                          border : false
                      },                        {
                          xtype : "panel",
                          layout : "form",
                          columnWidth : 0.35,
                          items : [                            {
                              xtype : "displayfield",
                              fieldLabel : "数据表",
                              name : "tblname",
                              width : 100
                          }],
                          border : false
                      }],
                      border : false
                  },                    {
                      xtype : "textarea",
                      fieldLabel : "SQL查询",
                      name : "basesql",
                      anchor : "99%",
                      readOnly : true,
                      height : 40
                  }],
                  labelAlign : "right"
              },                {
                  xtype : "tabpanel",
                  region : "center",
                  activeTab : 0,
                  items : [                    {
                      title : "列级权限",
                      autoHeight : true,
                      items : [                        {
                          xtype : "editorgrid",
                          region : "center",
                          frame : true,
                          viewConfig : {forceFit:true},
                          columnLines : true,
                          autoWidth : true,
                          autoHeight : false,
                          height : 420,
                          itemId : "colGrid",
                          store : colStore,
                          colModel : new Ext.grid.ColumnModel([new JrafRowNumberer(),
				{width:40,sortable:true,header:'字段名',dataIndex:'name'},
				{width:40,sortable:true,header:'描述',dataIndex:'title'},
				{width:40,sortable:true,header:'授权可见',dataIndex:'colacc',editor:new Ext.form.Checkbox(),renderer:new Ext.util.Format.checkboxRenderer()}]),
                          buttonAlign : "center",
                          buttons : [{
								text : '列级授权',
								handler : function() {
									var c_grid=this.ownerCt.ownerCt;
									var c_sto=c_grid.getStore();
						
									var jr=new JrafRequest('pcmc','sm_permission','saveColPerm');
									var ps=JrafUTIL.crDataAllRecNoEncode(colStore,{roleid:c_sto.baseParams['roleid'],
										sys:c_sto.baseParams['sys'],opr:c_sto.baseParams['opr'],act:c_sto.baseParams['act']});
									jr.setExtraPs(ps);
									jr.postData();
								}
							}],
                          hidden : true
                      }]
                  },                    {
                      title : "行级权限",
                      items : [                        {
                          xtype : "panel",
                          title : "行级权限",
                          hidden : true,
                          itemId : "rowpanel",
                          items : [                            {
                              xtype : "grid",
                              frame : true,
                              viewConfig : {forceFit:true},
                              columnLines : true,
                              autoWidth : true,
                              autoHeight : false,
                              height : 280,
                              itemId : "rowGrid",
                              store : rowStore,
                              tbar : [],
                              colModel : new Ext.grid.ColumnModel([new JrafRowNumberer(),
				{width:40,sortable:true,header:'角色',dataIndex:'rolename'},
				{width:40,sortable:true,header:'限定条件',dataIndex:'sqlwhere'}])
                          },                            {
                              xtype : "form",
                              itemId : "rowForm",
                              items : [                                {
                                  xtype : "textarea",
                                  fieldLabel : "当前角色行级权限",
                                  name : "sqlwhere",
                                  anchor : "99%"
                              },                                {
                                  xtype : "hidden",
                                  fieldLabel : "Hidden Field",
                                  name : "roleid"
                              },                                {
                                  xtype : "hidden",
                                  fieldLabel : "Hidden Field",
                                  name : "__sysname"
                              },                                {
                                  xtype : "hidden",
                                  fieldLabel : "Hidden Field",
                                  name : "oprname"
                              },                                {
                                  xtype : "hidden",
                                  fieldLabel : "Hidden Field",
                                  name : "actname"
                              }],
                              buttonAlign : "center",
							  frame:true,
                              buttons : [{
					text:'保存',handler:function(){
						var jr=new JrafRequest('pcmc','sm_permission','saveRowPerm');
						jr.setForm(this.ownerCt.ownerCt);
						jr.postData();
				}
				}]
                          }]
                      }]
                  }]
              }],
              autoScroll : true
          },            {
              region : "west",
              title : "交易权限",
              width : 240,
              collapsible : true,
              split : true,
              layout : "fit",
              items : [                {
                  xtype : "treepanel",
                  height : 400,
                  itemId : "act-tree",
                  animate : true,
                  autoScroll : true,
                  containerScroll : true,
                  rootVisible : false,
                  root : new Ext.tree.AsyncTreeNode({id:'-1'}),
                  loader : new JrafXmlTreeLoader({
				nparams: {nid:'id',pid:'pid',ntext:'{oprid}.{action}({acctext})({desc})',loadAll:true,expanded:true,chk:true,chkfld:'selected',chkval:'checked'},
				action: {sysName:'pcmc',oprID:'sm_permission',actions:'getActPermissions'},
				baseParams: {roleid:'-1',subsysid:'2'}
			}),
                  listeners : {
				click : function(node,e){loadAccRec(node);},
				checkchange: function(node, e) {
			    	if (!node.leaf) {
			        	node.expand(true, false, function() {
			                node.eachChild(function(child) {
			                    child.ui.toggleCheck(node.attributes.checked);
			                    child.attributes.checked = node.attributes.checked;
			                });
			            });
			        }
			    }
			},
                  buttonAlign : "center",
				  frame:true,
                  buttons : [{
				text : '交易授权',
				handler : function() {
					var atree=this.ownerCt.ownerCt;
					var ns=atree.getChecked();
					var rid=atree.getLoader().baseParams['roleid'];
					var param={roleid:rid};
					for (var i=0,len=ns.length;i<len;i++) {
				    	var rec=ns[i].attributes.record;
				    	var actnm=rec.get('action');
				    	if(actnm && ''!=actnm){
					    	var val=rec.get('oprid')+','+rec.get('action');
					        param['chkOpr']=!param['chkOpr'] ? val:[].concat(param['chkOpr']).concat(val);
					    }
			        }
					jr=new JrafRequest('pcmc','sm_permission','saveActPermissions');
					jr.setExtraPs(param);
					jr.postData();
				}
			}]
              }]
          }]
      }
  ]
},'panel');
this.aMenuTree=aMenuTree;this.__caches__.push(aMenuTree);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    layout : "border",
    items : [      {
    	xtype : "grid",
        region : "center",
        frame : true,
        title : "角色管理",
        viewConfig : {forceFit:true,cellTpl:JrafUTIL.selCellTpl()},
        columnLines : true,
        autoHeight : false,
        height : 320,
        itemId : "role-grid",
        store : roleStore,
        tbar : [{xtype:'syscombo',itemId:'syscombo',width:160},
    	{
    	tooltip:'刷新',
    	iconCls:'arrow-refresh',
    	handler:function(){
    	    var sv=this.ownerCt.getComponent('syscombo').getValue();
    	    if(''!=sv)
    	    {
        		roleStore.baseParams['subsysid']=sv;
        		roleStore.load();
        		var _form=JrafUTIL.findCmp(MainPanel,'role-form');
    			_form.getForm().reset();
    			_form.saveBtn.disable();
    			_form.menuBtn.disable();
    			_form.actBtn.disable();
        	}
    	}},{xtype: 'tbseparator'},{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
	        	var sv=this.ownerCt.getComponent('syscombo').getValue();
	        	if(''!=sv){
		        	var _grid=this.ownerCt.ownerCt;
		        	var recordType=_grid.getStore().recordType;
					var nr=new recordType({subsysid:sv});
	                _grid.getStore().add(nr);
	                _grid.getSelectionModel().selectLastRow();
	            }
			}},
{xtype: 'tbseparator'},
{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
				var _grid=this.ownerCt.ownerCt;
	        	Ext.Msg.confirm('删除数据','确认删除选择的角色?',function(btn){
					if(btn == 'yes'){
						var ckrs=_grid.getSelectionModel().getSelections();
						for(i=0;i<ckrs.length;i++){
							_grid.getStore().remove(ckrs[i]);
						}
						_grid.getStore().save();
						var _form=JrafUTIL.findCmp(MainPanel,'role-form');
	        			_form.getForm().reset();
	        			_form.saveBtn.disable();
	        			_form.menuBtn.disable();
	        			_form.actBtn.disable();
					}
				});
			}}],
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:roleStore,displayInfo: true}),
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
		{header: "角色名称", width: 40, sortable: true, dataIndex: 'rolename'},
        {header: "所属系统", width: 80, sortable: true, dataIndex: 'subsysid',renderer:sysRender},
        {header: "管理角色", width: 80, sortable: false, dataIndex: 'mrole'},
        {header: "备注", width: 80, sortable: false, dataIndex: 'remark'}]),
        sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}},
	        rowselect: function(sm, row, rec) {
	        	var _form=JrafUTIL.findCmp(MainPanel,'role-form');
	        	_form.getForm().loadRecord(rec);
				_form.record=rec;
	        	_form.saveBtn.enable();
	        	var _id=Ext.isEmpty(rec.data['roleid']);
	        	if(_id){
	        		_form.menuBtn.disable();
	        		_form.actBtn.disable();
	        	}else{
	        		_form.menuBtn.enable();
	        		_form.actBtn.enable();
	        	}
	        		
            },
	        rowdeselect: function(sm, row, rec) {
	        	var _form=JrafUTIL.findCmp(MainPanel,'role-form');
	        	_form.getForm().reset();
	        	_form.saveBtn.disable();
	        	_form.menuBtn.disable();
	        	_form.actBtn.disable();
            }}})
    },      {
        xtype : "form",
        title : "角色信息",
        region : "south",
        autoHeight : true,
        labelWidth : 75,
        itemId : "role-form",
        buttonAlign : "center",
        defaultType : "textfield",
        labelAlign : "right",
        items : [          {
            name : "roleid",
            hidden : true,
            hideLabel : true
        },          {
            fieldLabel : "角色名称",
            name : "rolename",
            allowBlank : false
        },          {
            xtype : "hidden",
            fieldLabel : "所属系统",
            name : "subsysid"
        },          {
            xtype : "multicombo",
            fieldLabel : "管理角色",
            id : "mrcombo",
            name : "mrole",
            hiddenName : "mrole",
            store : roleStore,
            mode : "local",
            displayField : "rolename",
            valueField : "roleid",
            width : 300
        },          {
            xtype : "textarea",
            fieldLabel : "备注",
            name : "remark",
            width : 300
        }],
		frame:true,
        buttons : [{
			text : '保存',
			ref: '../saveBtn',
			disabled:true,
			handler : function() {
				var _form=this.ownerCt.ownerCt;
				if(_form.record)
				{
					var newFlag=Ext.isEmpty(_form.record.data['roleid']);
					var jr;
					if(newFlag)
					{
						jr=new JrafRequest('pcmc','userrole','addRole',{recordType:Jraf.RoleRecord,idProperty:'roleid'});
					}
					else
					{
						jr=new JrafRequest('pcmc','userrole','updateRole',{recordType:Jraf.RoleRecord,idProperty:'roleid'});
					}
					jr.setForm(_form);
                    jr.setSuccFn(function(a,_resp,xr){
                    	_form.getForm().updateRecord(_form.record);
						if(newFlag)
                    	{
                    		xr.realize(_form.record,a.records);
                    		_form.menuBtn.enable();
	        				_form.actBtn.enable();
                    		_form.get('mrcombo').reload();
                    	}
                    	else
                    	{
                    		xr.update(_form.record,a.records);
                    	}
                    	_form.getForm().loadRecord(_form.record);
                    });
					jr.postData();
				}
			}
		},{
			text : '菜单授权',
			ref: '../menuBtn',
			disabled:true,
			handler : function() {
				var _form=this.ownerCt.ownerCt;
				var rec=_form.record;
		    	var mt=JrafUTIL.findCmp(rMenuTree,'menu-tree');
        		mt.getLoader().baseParams['subsysid']=rec.get('subsysid');
        		mt.getLoader().baseParams['roleid']=rec.get('roleid');
				var win=MainPanel['menuwin'];
				if(!win){
					win=new Ext.Window({
						title:'角色菜单授权',
				        layout:'fit',
				        width:640,
				        height:480,
				        closeAction:'hide',
				        plain: true,
				        modal: true,
				        items:rMenuTree
				    });
				    MainPanel['menuwin']=win;
				    __caches__.push(win);
			    }
			    else
			    {
			    	mt.getLoader().load(mt.getRootNode());
			    }
		    	win.show();
			}
		},{
			text : '交易授权',
			ref: '../actBtn',
			disabled:true,
			handler : function() {
				var _form=this.ownerCt.ownerCt;
				var rec=_form.record;
		    	var mt=JrafUTIL.findCmp(aMenuTree,'act-tree');
        		mt.getLoader().baseParams['subsysid']=rec.get('subsysid');
        		mt.getLoader().baseParams['roleid']=rec.get('roleid');
				var win=MainPanel['actwin'];
				if(!win){
					win=new Ext.Window({
						title:'角色操作授权',
				        layout:'fit',
				        width:800,
				        height:600,
				        closeAction:'hide',
				        plain: true,
				        modal: true,
				        maximizable : true,
				        items:aMenuTree
				    });
				    MainPanel['actwin']=win;
				    __caches__.push(win);
			    }
			    else{
	        		mt.getLoader().load(mt.getRootNode());
		    	};
		    	win.show();
			}
		}]
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};