Ext.namespace("jsp.platform.sm");jsp.platform.sm.sysmenu=function(){var __caches__=[];this.__caches__=__caches__;function deleteNode(node){
	var pn=node.previousSibling||node.parentNode;
	node.remove();
	pn.select();
	loadRec(pn);
};
function loadRec(node){
	var menu_form=JrafUTIL.findCmp(MainPanel,'menuForm');

	var rec=node.attributes.record;
	if(rec){
		if(node!=menu_form.node){
			menu_form.node=node;
			menu_form.getForm().reset();
			menu_form.getForm().loadRecord(rec);
			menu_form.enable();
		}
	}
	else if(null!=menu_form.node){
		menu_form.getForm().reset();
		menu_form.node=null;
		menu_form.disable();
	}
};var menuRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "subsysid",
    fieldLabel : "子系统流水号",
    type : "int"
},  {
    xtype : "Field",
    name : "pmenuid",
    fieldLabel : "父菜单流水号",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "levels",
    fieldLabel : "层次号",
    type : "int",
    allowBlank : false
},  {
    xtype : "Field",
    name : "menuname",
    fieldLabel : "菜单名称",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "imgurl",
    fieldLabel : "图片地址",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "linkurl",
    fieldLabel : "超链接地址",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "isinternet",
    fieldLabel : "是否公网发布",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "remark",
    fieldLabel : "备注",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "orderidx",
    fieldLabel : "排序编号",
    type : "int",
    allowBlank : true
},  {
    xtype : "Field",
    name : "newwin",
    fieldLabel : "新窗口",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "menuid",
    fieldLabel : "menuid",
    type : "int",
    allowBlank : true
}]);
this.menuRecord=menuRecord;this.__caches__.push(menuRecord);var iconRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "icon",
    fieldLabel : "icon",
    type : "string"
},  {
    xtype : "Field",
    name : "icondesc",
    fieldLabel : "icondesc",
    type : "string",
    allowBlank : true
}]);
this.iconRecord=iconRecord;this.__caches__.push(iconRecord);var iconStore=Ext.create({
  xtype : "Store",
  classname : "iconStore",
  type : "JrafXmlStore",
  recordType : iconRecord,
  idProperty : "icon",
  api : {read:{sysName:"pcmc",oprID:"menu",actions:"getMenuIcons"}},
  autoLoad : true,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.iconStore=iconStore;this.__caches__.push(iconStore);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    layout : "border",
    items : [               {
            xtype : "form",
            itemId : "menuForm",
			region : "center",
            disabled : true,
			title : "菜单定义",
            labelAlign : "right",
            items : [              {
                name : "subsysid",
                fieldLabel : "子系统流水号",
                xtype : "hidden"
            },              {
                name : "menuid",
                fieldLabel : "menuid",
                allowBlank : true,
                xtype : "hidden"
            },              {
                name : "pmenuid",
                fieldLabel : "父菜单流水号",
                allowBlank : true,
                xtype : "hidden"
            },              {
                name : "menuname",
                fieldLabel : "菜单名称",
                allowBlank : false,
                xtype : "textfield"
            },              {
                name : "imgurl",
                fieldLabel : "菜单图标",
                allowBlank : true,
                xtype : "iconcombo",
                valueField : "icon",
				displayField : "icondesc",
				iconField : "icon",
				store : iconStore,
				mode : "local",
				editable:true,
				forceSelection:false,
				triggerAction : "all",
				customProperties : true,
                hiddenName:'imgurl',
                width : 300
            },              {
                name : "linkurl",
                fieldLabel : "超链接地址",
                allowBlank : true,
                xtype : "textfield",
                width : 300
            },              {
                name : "isinternet",
                fieldLabel : "是否公网发布",
                allowBlank : true,
                xtype : "hidden"
            },              {
                name : "levels",
                fieldLabel : "层次号",
                allowBlank : false,
                xtype : "numberfield",
                allowDecimals : false,
                width : 40
            },              {
                name : "orderidx",
                fieldLabel : "序号",
                allowBlank : true,
                xtype : "numberfield",
                allowDecimals : false,
                width : 40
            },              {
                name : "newwin",
                fieldLabel : "窗口类型",
                allowBlank : true,
                xtype : 'combo',
                width : 100,
                hiddenName:'newwin',
                valueField : "v",
				displayField : "t",
				store : new Ext.data.ArrayStore({id: 0,fields: ['v','t'], data: [['1', 'Window'],['0','Iframe'],['2','Script']]}),
				mode : "local",
				editable:true,
				forceSelection:true,
				triggerAction : "all"
            },              {
                name : "remark",
                fieldLabel : "备注",
                allowBlank : true,
                xtype : "textarea",
                width : 300
            }],
            buttonAlign:'center',
			frame:true,
			buttons : [{
				text : '保存',
				handler : function() {
					var _form=this.ownerCt.ownerCt;
					if(_form.node)
					{
						var rec=_form.node.attributes.record;
						var newFlag=Ext.isEmpty(rec.data['menuid']);
						if(newFlag)
						{
							jr=new JrafRequest('pcmc','menu','addMenu',{idProperty:'menuid'});
						}
						else
						{
							jr=new JrafRequest('pcmc','menu','updateMenu',{idProperty:'menuid'});
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
	                    	_form.node.id=att.id;
	                    });
						jr.postData();
					}
				}
			}]
    },      {
        region : "west",
        title : "菜单列表",
        width : 280,
        split : true,
        collapsible : true,
        titleCollapse : false,
		frame:true,
        items : [          {
            xtype : "treepanel",
            height : 400,
            itemId : "menutree",
            animate : true,
            autoScroll : true,
            containerScroll : true,
            rootVisible : true,
            root : new Ext.tree.AsyncTreeNode({id:'0',text:'系统菜单',draggable : false}),
            loader : new JrafXmlTreeLoader({nparams: {nid:'menuid',pid:'pmenuid',ntext:'{menuname}',loadAll:false,expanded:true,sm:'all',isLeaf:'isleaf'},action: {sysName:'pcmc',oprID:'menu',actions:'getMenuListByPid'},baseParams: {PageSize:'-1'}}),
            tbar : [{xtype:'syscombo',itemId:'syscombo',width:160},
        	{
        	tooltip:'刷新',
        	iconCls:'arrow-refresh',
        	handler:function(){
        	    var sv=this.ownerCt.getComponent('syscombo').getValue();
        	    if(''!=sv)
        	    {
	        		var mt=this.ownerCt.ownerCt;
	        		mt.getLoader().baseParams['subsysid']=sv;
	        		mt.getLoader().load(mt.getRootNode());
	        	}
        	}}],
            listeners : {
  contextmenu : function(node,e){
  e.preventDefault();
  node.select();
  node.expand();
  var tmenu=node.getOwnerTree().menu;
  if(!tmenu){
    tmenu =  new Ext.menu.Menu({
	items : [{
		id : 'addMenu',
		text : '新增菜单',
		iconCls:'add'
	},{
		id : 'delMenu',
		text : '删除菜单',
		iconCls:'delete'
	}],
	listeners: {
		itemclick: function(item) {
			var node = item.parentMenu.treeNode;
			var noderec = node.attributes.record
			switch (item.id) {
			case 'addMenu':
				var newRec = new menuRecord({menuid:'',pmenuid:node.id,menuname:'new Menu',isleaf:'true',subsysid:node.getOwnerTree().getLoader().baseParams['subsysid']});
				var newNode = node.getOwnerTree().getLoader().createNodeByRec(newRec);
				node.appendChild(newNode);
				newNode.select();
				loadRec(newNode);
				break;
			case 'delMenu':
				if(''==noderec.get('menuid'))
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
							var jr=new JrafRequest('pcmc','menu','deleteMenu');
							jr.setExtraPs({menuid:node.id});
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
  tmenu.get('delMenu').setDisabled(node.parentNode==null);
  tmenu.showAt(e.getXY());
  tmenu.treeNode=node;
  loadRec(node);
},
  click : function(node,e){loadRec(node);}
}
        }],
        layout : "fit"
    }]
}]});
this.MainPanel=MainPanel;var __jrafonload=function(_params_){};
this.__jrafonload=__jrafonload;this.__caches__.push(__jrafonload);};