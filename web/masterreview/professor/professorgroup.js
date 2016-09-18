Ext.namespace("jsp.masterreview.professor");jsp.masterreview.professor.professorgroup=function(){var __caches__=[];this.__caches__=__caches__;
function loadRec(n){
	var node = n||MainPanel.get(0).getComponent('group-tree').getSelectionModel().getSelectedNode();
	var _form=JrafUTIL.findCmp(MainPanel,'group-form');
	
	var rec=node.attributes.record;
    if(rec){
		//if(n!=_form.node){
			_form.node=n;
			_form.getForm().reset();
			_form.getForm().loadRecord(rec);
			//_form.record=node.attributes.record;
			//_form.enable();
			// Ext.getCmp("classRoomInfoTab").setDisabled(false);
		//}
	}else if(null!=_form.node){
		_form.getForm().reset();
		_form.node=null;
		//_form.disable();
		//Ext.getCmp("classRoomInfoTab").setDisabled(true);
	}
		/*
	_form.getForm().reset();
	_form.getForm().loadRecord(node.attributes.record);
	_form.record=node.attributes.record;
	_form.treeNode=node;*/
	
	if(rec){
		loadHeadMaster(rec.data['group_id']);
		loadProfessor(rec.data['group_id']);
	}
};
function deleteNode(node){
	var pn=node.previousSibling||node.parentNode;
	node.remove();
	pn.select();
	loadRec(pn);
};
function updateNode(node,rec){
	var att=node.getOwnerTree().getLoader().createAttrByRec(rec);
	node.setText(att.text);
};

function loadHeadMaster(group_id){
	//var headMasterGrid=JrafUTIL.findCmp(MainPanel,'HeadMasterGrid');
	var headMasterGrid=Ext.getCmp("HeadMasterGrid");
	if(Ext.isEmpty(group_id)){
		headMasterGrid.setDisabled(true);
	}
	else{
		headMasterGrid.setDisabled(false);
		headMasterGrid.getStore().setBaseParam('group_id',group_id);
	    headMasterGrid.getStore().load();
	}
};


function loadProfessor(group_id){
	//var professorGrid=JrafUTIL.findCmp(MainPanel,'ProfessorGrid');
	var professorGrid=Ext.getCmp("ProfessorGrid");
	if(Ext.isEmpty(group_id)){
		professorGrid.setDisabled(true);
	}
	else{
		professorGrid.setDisabled(false);
		professorGrid.getStore().setBaseParam('group_id',group_id);
	    professorGrid.getStore().load();
	}
};


var folderWin_
function openFolderWindow(group_id,_leavel){
			var jsurl,jsobjnm,title
			var wid,hei;
			if (_leavel == '1') {
				title="添加校长";
			    jsurl="/masterreview/professor/headmasterselect.js";
				jsobjnm="jsp.masterreview.professor.headmasterselect";
				wid = parseInt(690);
				hei = parseInt(390);
			} else if (_leavel == '2'){
				title="添加专家";
			    jsurl="/masterreview/professor/professorselect.js";
				jsobjnm="jsp.masterreview.professor.professorselect";
				wid = parseInt(690);
				hei = parseInt(390);
			}
			var __mfunc=function(){
					var crtobj='var _mainPanel=new '+jsobjnm+'();';
					if (window.execScript) {
					   window.execScript(crtobj);
					} else {
					   window.eval(crtobj);
					}
					//创建window
					folderWin_=new Ext.Window({
						title:[title],
						layout:'fit',
						width:wid,
						height:hei,
						//closable:true,
						closeAction:'hide',
						maximizable:true,
						plain: true,
						modal: true,
						items:_mainPanel.MainPanel
					});
					
					 folderWin_.on("hide",function(){
				        if (_leavel == '1') {
				           groupHeadmasterStore.setBaseParam('group_id',group_id);
				           groupHeadmasterStore.load();
						} else if (_leavel == '2'){
							groupProfessorStore.setBaseParam('group_id',group_id);
						   groupProfessorStore.load();
						} 
				     });
				     
					folderWin_.show();
				    _mainPanel.__jrafonload({
				      _group_id:[group_id],
				      _leavel:[_leavel]
				    });
					JrafUTIL.putCmp(jsobjnm,_mainPanel);
			}.createDelegate(this);
			JrafUTIL.scriptLoader(Jraf_ContextPath+jsurl,true,__mfunc);
};

this.closeFolderWin =function(){
   folderWin_.hide();
}

    var groupRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "group_id",
    fieldLabel : "group_id",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "group_pid",
    fieldLabel : "group_pid",
    type : "string",
    allowBlank : true
},    {
    xtype : "Field",
    name : "group_name",
    fieldLabel : "组名称",
    type : "string",
    allowBlank : false
}, {
    xtype : "Field",
    name : "childnum",
    fieldLabel : "childnum",
    type : "string",
    allowBlank : true
}]);
this.groupRecord=groupRecord;this.__caches__.push(groupRecord);var headmasterRecord=Ext.data.Record.create([ {
    xtype : "Field",
    name : "user_group_id",
    width : 150,
    fieldLabel : "主键id",
    allowBlank : false,
    type : "string"
},{
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
},{
    xtype : "Field",
    name : "mobile",
    fieldLabel : "移动电话",
    type : "string",
    allowBlank : true
}]);
this.headmasterRecord=headmasterRecord;this.__caches__.push(headmasterRecord);var ProfessorRec=Ext.data.Record.create([ {
    xtype : "Field",
    name : "user_group_id",
    width : 150,
    fieldLabel : "主键id",
    allowBlank : false,
    type : "string"
},{
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
    fieldLabel : "专家主键id",
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
},{
    xtype : "Field",
    name : "mobile",
    fieldLabel : "移动电话",
    type : "string",
    allowBlank : true
}]);
this.ProfessorRec=ProfessorRec;this.__caches__.push(ProfessorRec);var groupHeadmasterStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "groupHeadmasterStore",
  recordType : headmasterRecord,
  idProperty : "userid",
  api : {    read :     {
	      sysName : "headmaster",
	      oprID : "groupAction",
	      actions : "findHeadMasters"
	  },
	    create :     {
	      sysName : "headmaster",
	      oprID : "groupAction",
	      actions : "addGroupMaster"
	  },
	    update :     {
	      sysName : "headmaster",
	      oprID : "groupAction",
	      actions : "updateGroupMaster"
	  },
	    destroy :     {
	      sysName : "headmaster",
	      oprID : "groupAction",
	      actions : "deleteGroupMaster"
	  }
  },
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.groupHeadmasterStore=groupHeadmasterStore;this.__caches__.push(groupHeadmasterStore);var groupProfessorStore=Ext.create({
  xxtype : "Store",
  xtype : "Store",
  classname : "groupProfessorStore",
  recordType : ProfessorRec,
  idProperty : "userid",
  api : { 
     read :     {
      sysName : "headmaster",
      oprID : "groupAction",
      actions : "findProfessors"
  },
    create :     {
      sysName : "headmaster",
      oprID : "groupAction",
      actions : "addGroupProfessor"
  },
    update :     {
      sysName : "headmaster",
      oprID : "groupAction",
      actions : "updateGroupProfessor"
  },
    destroy :     {
      sysName : "headmaster",
      oprID : "groupAction",
      actions : "deleteGroupProfessor"
  }
 },
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.groupProfessorStore=groupProfessorStore;this.__caches__.push(groupProfessorStore);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    xtype : "panel",
    layout : "border",
    items : [     {
    xtype : "panel",
    layout : "anchor",
    region : "center",
    items : [    {
            xtype : "form",
            title : "组信息",
            id:"groupId",
            itemId : "group-form",
            region : "north",
            titleCollapse : true,
            collapsible : true,
            height : 90,
            disabled : false,
            frame : true,
            labelAlign : "right",
            items : [           {
	            name : "group_id",
	            fieldLabel : "group_id",
	            allowBlank : true,
	            xtype : "hidden"
	        },          {
	            name : "group_pid",
	            fieldLabel : "group_pid",
	            allowBlank : true,
	            xtype : "hidden"
	        },      {
                layout : "column",
                bodyBorder : false,
                items : [       
                      {
                    layout : "form",
                    columnWidth : 0.5,
                    bodyBorder : false,
                    items : [                      {
                        name : "group_name",
                        fieldLabel : "组名称",
                        allowBlank : false,
                        xtype : "textfield"
                    }]
                }
                ]
            }],
            buttonAlign : "center",
            buttons : [{
				text : '保存',
				handler : function() {
					var _form=this.ownerCt.ownerCt;
					//if(_form.record && _form.treeNode)
					if(_form.node)
					{
				     	var rec=_form.node.attributes.record;
						var newFlag=Ext.isEmpty(rec.data['group_id']);
						var jr;
						if(newFlag)
						{
							jr=new JrafRequest('headmaster','groupAction','addGroup',{idProperty:'group_id'});
						}
						else
						{
							jr=new JrafRequest('headmaster','groupAction','updateGroup',{idProperty:'group_id'});
						}
						jr.setForm(_form);
	                    jr.setSuccFn(function(a,_resp,xr){
	                    	_form.getForm().updateRecord(rec);
							if(newFlag)
	                    	{
	                    		xr.realize(rec,a.records);
	                    	}
	                    	else
	                    	{
	                    		xr.update(rec,a.records);
	                    	}
	                    	_form.getForm().loadRecord(rec);
	                    	var att=_form.node.getOwnerTree().getLoader().createAttrByRec(rec);
	                    	_form.node.setText(att.text);
	                    //	_form.node.id=a.records[0].room_id;
	                    	_form.node.id=att.id;
	                    	//updateNode(_form.node,rec);
	                    });
						jr.postData();
					}
				}
			}]
        },     {
        xtype : "grid",
        id : "ProfessorGrid",
        region : "center",
        autoWidth : true,
        frame : true,
        title : "专家列表",
        viewConfig : {
        forceFit : false
        },
        stripeRows : true,
        columnLines : true,
        autoHeight : false,
        height : 270,
        store : groupProfessorStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:groupProfessorStore,displayInfo: true}),
        tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
	               var selectNode = MainPanel.get(0).getComponent('group-tree').getSelectionModel().getSelectedNode();
		           if(selectNode){
		              var group_id = selectNode.attributes.record.data['group_id'];
		              openFolderWindow(group_id,'2');
		           }else{
		              Ext.MessageBox.alert("提示","请先选择专家组！！");
                      return false;
		           }
          }
        },{xtype: 'tbseparator'},
        {text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
	    	        var _grid=this.ownerCt.ownerCt;
		        	Ext.Msg.confirm('删除数据','确认删除选中数据?',function(btn){
		        		if(btn == 'yes'){
				        	var ckrs=_grid.getSelectionModel().getSelections();
				        	for(var i=0;i<ckrs.length;i++){
				        		_grid.getStore().remove(ckrs[i]);
				        	}
				        	_grid.getStore().save();
				        	setTimeout(function() { 
	                        	var selectNode = MainPanel.get(0).getComponent('group-tree').getSelectionModel().getSelectedNode();
				                if(selectNode){
				                  var group_id = selectNode.attributes.record.data['group_id'];
					        	  loadProfessor(group_id);
					        	}
	                        },500);
		        		}
		        	});
          }
        }],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
        {width:100,sortable:true,header:'主键id',dataIndex:'user_group_id',renderer:Ext.util.Format.paramRenderer('undefined',''),hidden : true},
        {width:100,sortable:true,header:'用户id',dataIndex:'user_id',renderer:Ext.util.Format.paramRenderer('undefined',''),hidden : true},
        {width:100,sortable:true,header:'姓名',dataIndex:'username',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'账号',dataIndex:'usercode',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:80,sortable:true,header:'移动电话',dataIndex:'mobile'},
        {width:100,sortable:true,header:'身份证号',dataIndex:'idnumber',renderer:Ext.util.Format.paramRenderer('undefined','')},
        {width:100,sortable:true,header:'地址',dataIndex:'address',renderer:Ext.util.Format.paramRenderer('undefined','')}]),
        sm : new Ext.grid.CheckboxSelectionModel({ 
          listeners:{
	        selectionchange:function(sm){
	           var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}
	        }
          }
        }),
        listeners : {rowdblclick : function(g,rowIndex,e){var _rec=g.getSelectionModel().getSelected();openEditInfoWin(_rec);}}
    },    {
        xtype : "grid",
        id : "HeadMasterGrid",
        region : "center",
        autoWidth : true,
        frame : true,
        title : "校长列表",
        viewConfig : {
        forceFit : false
    },
        stripeRows : true,
        columnLines : true,
        autoHeight : false,
        height : 270,
        store : groupHeadmasterStore,
        bbar : new Ext.PagingToolbar({pageSize: JrafSession.get('PageSize'),store:groupHeadmasterStore,displayInfo: true}),
        tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){
		           var selectNode = MainPanel.get(0).getComponent('group-tree').getSelectionModel().getSelectedNode();
		           if(selectNode){
		              var group_id = selectNode.attributes.record.data['group_id'];
		              openFolderWindow(group_id,'1');
		           }else{
		              Ext.MessageBox.alert("提示","请先选择专家组！！");
                      return false;
		           }
          }
        },{xtype: 'tbseparator'},
        {text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){
	        	    var _grid=this.ownerCt.ownerCt;
		        	Ext.Msg.confirm('删除数据','确认删除选中数据?',function(btn){
		        		if(btn == 'yes'){
				        	var ckrs=_grid.getSelectionModel().getSelections();
				        	for(var i=0;i<ckrs.length;i++){
				        		_grid.getStore().remove(ckrs[i]);
				        	}
				        	_grid.getStore().save();
				        	setTimeout(function() { 
	                        	var selectNode = MainPanel.get(0).getComponent('group-tree').getSelectionModel().getSelectedNode();
				                if(selectNode){
				                  var group_id = selectNode.attributes.record.data['group_id'];
					        	  loadHeadMaster(group_id);
					        	}
	                        },500);
		        		}
		        	});
          }
        }],
        colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
         {width:100,sortable:true,header:'主键id',dataIndex:'user_group_id',renderer:Ext.util.Format.paramRenderer('undefined',''),hidden : true},
        {width:100,sortable:true,header:'用户id',dataIndex:'user_id',renderer:Ext.util.Format.paramRenderer('undefined',''),hidden : true},
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
	           var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}
	        }
          }
        }),
        listeners : {rowdblclick : function(g,rowIndex,e){var _rec=g.getSelectionModel().getSelected();openEditInfoWin(_rec);}}
    }]
},  {
        xtype : "treepanel",
        region : "west",
        title : "专家组列表",
        itemId : "group-tree",
        height : 400,
        animate : true,
        autoScroll : true,
        containerScroll : true,
        split : true,
        collapsible : true,
        titleCollapse : false,
        rootVisible : true,
        root : new Ext.tree.AsyncTreeNode({id:"-1", text:'全部组',draggable : false,expanded:true}),
        loader : new JrafXmlTreeLoader({
            nparams: {nid:'group_id',pid:'group_pid',ntext:'{group_name}',isLeaf:'childnum',expanded:true},
            action: {sysName:'headmaster',oprID:'groupAction',actions:'getGroupList'},
            baseParams:{PageSize:'-1'}
        }),
        width : 320,
        listeners : {
        /*
        render : function(p) {
	        	var jr = new JrafRequest('headmaster','groupAction','getGroupList',{idProperty:'group_id'});
	        	jr.setSuccFn(function(a,_resp){
	        		var rec = a.records[0];
	        		var r = new Ext.tree.AsyncTreeNode({
						text :rec.data['group_name'],
						draggable : false,
						id : rec.group_id,
						record: rec
					});
					p.setRootNode(r);
					r.expand();
	        	});
	        	jr.setExtraPs({deptid:JrafSession.get('deptid'),group_pid:'-1'});
                jr.postData();
	        },*/
  contextmenu : function(node,e){
	  e.preventDefault();
	  node.select();
	  node.expand();
	  var noderec=node.attributes.record;
	  var tmenu=node.getOwnerTree().menu;
	  if(!tmenu){
	    tmenu =  new Ext.menu.Menu({
		items : [{
			id : 'addNode',
			text : '新增',
			iconCls:'add'
		},{
			id : 'delNode',
			text : '删除',
			iconCls:'delete'
		}],
		listeners: {
			itemclick: function(item) {
				var node = item.parentMenu.treeNode;
				var noderec = node.attributes.record
				switch (item.id) {
				case 'addNode':
					if (node.isLeaf()){
						node.leaf = false;
					}
					//var lv = noderec.data['levels'];
					//lv = Ext.isEmpty(lv)?'0':lv;
					//var nlevels = new Number(lv)+1;
					
					var finalPid= (node.id == -1)? JrafSession.get('deptid') : node.id ;
					var newRec = new groupRecord({group_id:'',group_pid:finalPid,group_name:'新组',childnum:0});
					var newNode = node.getOwnerTree().getLoader().createNodeByRec(newRec);
					node.appendChild(newNode);
					newNode.select();
					loadRec(newNode);
					break;
				case 'delNode':
					if(''==noderec.get('group_id'))
					{
						deleteNode(node);
						break;
					}
					Ext.Msg.show({
						title : '提示',
						msg :"确认删除?",
						icon : Ext.Msg.WARNING,
						buttons : Ext.Msg.YESNO,
						fn:function(buttonId){
							if('yes'==buttonId){
								var jr=new JrafRequest('headmaster','groupAction','deleteGroup',{recordType:groupRecord,idProperty:'group_id'});
								jr.setExtraPs({group_id:noderec.data['group_id']});
								jr.setSuccFn(function(a,_resp,xr){
									deleteNode(node);
			                    });
								jr.postData();
							}
						}
					});
					break;
				}
			}
		}
	    });
	    node.getOwnerTree().menu=tmenu;
	  }
	  tmenu.showAt(e.getXY());
	  tmenu.treeNode=node;
	  loadRec(node);
  	
  },
  click : function(node,e){
  	loadRec(node);
  }
}
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};