Ext.namespace("jsp.platform.sm");jsp.platform.sm.oprdesigner=function(){var __caches__=[];this.__caches__=__caches__;
var actAttMeta= JrafUTIL.JrafDataMeta(2);
function deleteNode(node){
	var pn=node.previousSibling||node.parentNode;
	node.remove();
	pn.select();
	loadRec(pn);
};
function accCheck(chked){
	if(chked){
		JrafUTIL.findCmp(MainPanel,'accredit').setValue(true);
	}
	else{
		JrafUTIL.findCmp(MainPanel,'colacc').setValue(false);
		JrafUTIL.findCmp(MainPanel,'rowacc').setValue(false);
	}
};
function getOprAct(pid)
{
	var idx=pid.lastIndexOf('|');
	return pid.substring(idx+1);
};
function loadRec(node){
	var opr_form=JrafUTIL.findCmp(MainPanel,'oprForm');
	var act_form=JrafUTIL.findCmp(MainPanel,'actForm');

	var rec=node.attributes.record;
	var onode,anode;
	//(1:sys,2:opr,3:act,4:file,5:tbl)
	var stype =rec.get('stype');
	if("2"==stype){
		onode=node;
	}
	if("3"==stype){
		anode=node;
		onode=node.parentNode;
	}
	if(onode){
		var orec=onode.attributes.record;
		if(onode!=opr_form.node){
			opr_form.node=onode;
			if(''!=orec.get('sid')){
				var jr=new JrafRequest('pcmc','sys','getOprDetail',{recordType:oprRecord,idProperty:'__oprID'});
				jr.setExtraPs({__sysName:orec.get('__sysName'),__oprID:orec.get('enname')});
		        jr.setSuccFn(function(a,_resp,xr){
		        	opr_form.getForm().loadRecord(a.records[0]);
		        });
				jr.postData();
			}
			else{
				opr_form.getForm().reset();
			}
			opr_form.enable();
		}
	}
	else if(null!=opr_form.node){
		opr_form.getForm().reset();
		opr_form.node=null;
		opr_form.disable();
	}
	if(anode){
		var arec=anode.attributes.record;
		var tblsto=JrafUTIL.findCmp(MainPanel,'tablecombo').getStore();
		if(arec.get('__sysName')!=tblsto.baseParams['__sysName']){
			tblsto.setBaseParam('__sysName',arec.get('__sysName'));
			tblsto.reload();
		}
		if(anode!=act_form.node){
			act_form.node=anode;
			if(''!=arec.get('sid')){
				var jr=new JrafRequest('pcmc','sys','getActDetail',{meta:actAttMeta,recordType:actAttRecord,idProperty:'actname'});
				jr.setExtraPs({__sysName:arec.get('__sysName'),__oprID:orec.get('enname'),__actions:arec.get('enname')});
		        jr.setSuccFn(function(a,_resp,xr){
		        	act_form.getForm().loadRecord(a.records[0]);
		        	JrafUTIL.findCmp(MainPanel,'actFldGrid').getStore().loadData(_resp.responseXML);
		        	JrafUTIL.findCmp(MainPanel,'actMsgGrid').getStore().loadData(_resp.responseXML);
		        	JrafUTIL.findCmp(MainPanel,'actFlowGrid').getStore().loadData(_resp.responseXML);
		        });
				jr.postData();
			}
			else{
				act_form.getForm().reset();
	    		JrafUTIL.findCmp(MainPanel,'actFldGrid').getStore().loadData('');
	    		JrafUTIL.findCmp(MainPanel,'actMsgGrid').getStore().loadData('');
		        JrafUTIL.findCmp(MainPanel,'actFlowGrid').getStore().loadData('');
			}
			act_form.enable();
			JrafUTIL.findCmp(MainPanel,'actFldGrid').enable();
			JrafUTIL.findCmp(MainPanel,'actMsgGrid').enable();
			JrafUTIL.findCmp(MainPanel,'actFlowGrid').enable();
		}
	}
	else if(null!=act_form.node){
		act_form.getForm().reset();
	    JrafUTIL.findCmp(MainPanel,'actFldGrid').getStore().loadData('');
		act_form.node=null;
		act_form.disable();
		JrafUTIL.findCmp(MainPanel,'actFldGrid').disable();
		JrafUTIL.findCmp(MainPanel,'actMsgGrid').disable();
		JrafUTIL.findCmp(MainPanel,'actFlowGrid').disable();
	}
};var oprRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "__sysName",
    fieldLabel : "sysName",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "__oprID",
    fieldLabel : "oprID",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "bean",
    fieldLabel : "bean",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "jndi",
    fieldLabel : "jndi",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "desc",
    fieldLabel : "desc",
    type : "string",
    allowBlank : true
}]);
this.oprRecord=oprRecord;this.__caches__.push(oprRecord);var actAttRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "actname",
    fieldLabel : "name",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "old",
    fieldLabel : "old",
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
    name : "accredit",
    fieldLabel : "accredit",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "colacc",
    fieldLabel : "colacc",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "rowacc",
    fieldLabel : "rowacc",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "log",
    fieldLabel : "log",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "nologin",
    fieldLabel : "nologin",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "webservice",
    fieldLabel : "webservice",
    type : "string",
    allowBlank : true
},{
    xtype : "Field",
    name : "oauth",
    fieldLabel : "oauth",
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
},  {
    xtype : "Field",
    name : "orderby",
    fieldLabel : "orderby",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "groupby",
    fieldLabel : "groupby",
    type : "string",
    allowBlank : true
}]);
this.actAttRecord=actAttRecord;this.__caches__.push(actAttRecord);var actFldRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "name",
    fieldLabel : "name",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "title",
    fieldLabel : "title",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "type",
    fieldLabel : "type",
    type : "string",
    allowBlank : false
},  {
    xtype : "Field",
    name : "maxlen",
    fieldLabel : "maxlen",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "minlen",
    fieldLabel : "minlen",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "notnull",
    fieldLabel : "notnull",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "pk",
    fieldLabel : "pk",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "gen",
    fieldLabel : "gen",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "refname",
    fieldLabel : "refname",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "op",
    fieldLabel : "op",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "reqclass",
    fieldLabel : "reqclass",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "defaultval",
    fieldLabel : "defaultval",
    type : "string",
    allowBlank : true
}]);
this.actFldRecord=actFldRecord;this.__caches__.push(actFldRecord);var nodeRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "sid",
    fieldLabel : "sid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "pid",
    fieldLabel : "pid",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "__sysName",
    fieldLabel : "sysName",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "enname",
    fieldLabel : "enname",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "cnname",
    fieldLabel : "cnname",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "stype",
    fieldLabel : "stype",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "isleaf",
    fieldLabel : "isleaf",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "nodepath",
    fieldLabel : "nodepath",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "icon",
    fieldLabel : "icon",
    type : "string",
    allowBlank : true
}]);
this.nodeRecord=nodeRecord;this.__caches__.push(nodeRecord);var msgRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "msgcode",
    fieldLabel : "msgcode",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "msgdesc",
    fieldLabel : "msgdesc",
    type : "string",
    allowBlank : true
}]);
this.msgRecord=msgRecord;this.__caches__.push(msgRecord);var flowRecord=Ext.data.Record.create([  {
    xtype : "Field",
    name : "flowresult",
    fieldLabel : "flowresult",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "flowtype",
    fieldLabel : "flowtype",
    type : "string",
    allowBlank : true
},  {
    xtype : "Field",
    name : "flowpath",
    fieldLabel : "flowpath",
    type : "string",
    allowBlank : true
}]);
this.flowRecord=flowRecord;this.__caches__.push(flowRecord);var actStore=Ext.create({
  xtype : "Store",
  classname : "actStore",
  type : "JrafXmlStore",
  recordType : actFldRecord,
  idProperty : "name",
  api : {read:{sysName:"",oprID:"",actions:""},destroy:{sysName:"",oprID:"",actions:""},create:{sysName:"",oprID:"",actions:""},update:{sysName:"",oprID:"",actions:""}},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false
},'Store');
this.actStore=actStore;this.__caches__.push(actStore);var msgStore=Ext.create({
  xtype : "Store",
  classname : "msgStore",
  type : "JrafXmlStore",
  recordType : msgRecord,
  idProperty : "msgcode",
  api : {read:{sysName:"",oprID:"",actions:""},destroy:{sysName:"",oprID:"",actions:""},create:{sysName:"",oprID:"",actions:""},update:{sysName:"",oprID:"",actions:""}},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false,
  meta : JrafUTIL.JrafDataMeta(3)
},'Store');
this.msgStore=msgStore;this.__caches__.push(msgStore);var flowStore=Ext.create({
  xtype : "Store",
  classname : "flowStore",
  type : "JrafXmlStore",
  recordType : flowRecord,
  idProperty : "flowresult",
  api : {read:{sysName:"",oprID:"",actions:""},destroy:{sysName:"",oprID:"",actions:""},create:{sysName:"",oprID:"",actions:""},update:{sysName:"",oprID:"",actions:""}},
  autoLoad : false,
  autoSave : false,
  writeAllFields : true,
  paramsAsHash : true,
  remoteSort : false,
  meta : JrafUTIL.JrafDataMeta(4)
},'Store');
this.flowStore=flowStore;this.__caches__.push(flowStore);var typeCombo=Ext.create({
  xxtype : "Jpanel",
  xtype : "combo",
  classname : "typeCombo",
  items : [],
  valueField : "v",
  displayField : "t",
  store : new Ext.data.ArrayStore({id: 0,fields: ['v','t'], data: [['long','long'],['String', 'String'],['double','double'],['Timestamp','Timestamp'],['file','file'],['reqcustom','自定义']]}),
  mode : "local",
  editable : false,
  forceSelection : true,
  triggerAction : "all"
},'panel');
this.typeCombo=typeCombo;this.__caches__.push(typeCombo);var opCombo=Ext.create({
  xxtype : "Jpanel",
  xtype : "combo",
  classname : "opCombo",
  items : [],
  valueField : "v",
  displayField : "t",
  store : new Ext.data.ArrayStore({id: 0,fields: ['v','t'], data: [['=', '='],['<','<'],['<=','<='],['>','>'],['>=','>='],['<>','<>'],['%*','like %s'],['*%','like s%'],['%%','like %s%']]}),
  mode : "local",
  forceSelection : true,
  triggerAction : "all"
},'panel');
this.opCombo=opCombo;this.__caches__.push(opCombo);var genCombo=Ext.create({
  xxtype : "Jpanel",
  xtype : "combo",
  classname : "genCombo",
  valueField : "v",
  displayField : "t",
  store : new Ext.data.ArrayStore({id: 0,fields: ['v','t'], data: [['ip','ip'],['sysdate', 'sysdate'],['systime','systime'],['datetime','datetime'],['nextday','req.nextday'],['preday','req.preday'],['session.userid','session.userid'],['session.username','session.username'],['session.usercode','session.usercode'],['session.deptid','session.deptid'],['session.deptcode','session.deptcode']]}),
  mode : "local",
  forceSelection : false,
  editable : true,
  minChars : 0,
  customProperties : true
},'panel');
this.genCombo=genCombo;this.__caches__.push(genCombo);var msgCombo=Ext.create({
  xxtype : "Jpanel",
  xtype : "combo",
  classname : "msgCombo",
  valueField : "v",
  displayField : "t",
  store : new Ext.data.ArrayStore({id: 0,fields: ['v','t'], data: [['00101', 'Base-查询列表异常'],['00102','Base-查询明细异常'],['00103','Base-新增成功'],['00104','Base-新增失败'],['00105','Base-更新成功'],['00106','Base-更新失败'],['00107','Base-删除成功'],['00108','Base-删除失败']]}),
  mode : "local",
  forceSelection : false,
  editable : true,
  minChars : 0,
  customProperties : true
},'panel');
this.msgCombo=msgCombo;this.__caches__.push(msgCombo);var ftypeCombo=Ext.create({
  xxtype : "Jpanel",
  xtype : "combo",
  classname : "ftypeCombo",
  valueField : "v",
  displayField : "t",
  store : new Ext.data.ArrayStore({id: 0,fields: ['v','t'], data: [['0', '刷新当前窗口'],['1','打开新窗口']]}),
  mode : "local",
  forceSelection : false,
  editable : true,
  minChars : 0,
  customProperties : true
},'panel');
this.ftypeCombo=ftypeCombo;this.__caches__.push(ftypeCombo);var MainPanel=new Ext.Panel({layout:'fit',border:false,bodyBorder:false,items:[  {
    layout : "border",
    items : [      {
        region : "center",
        title : "操作定义",
        layout : "fit",
        xtype : "panel",
        items : [{
            xtype : "panel",
        	autoScroll:true,
            items : [{
                xtype : "form",
                title : "Operation",
                bodyBorder : false,
                titleCollapse: true,
	    		collapsible: true,
                anchor : "100%",
                itemId : "oprForm",
                labelAlign:'right',
                disabled : true,
                tbar : [{text:'保存Opr',iconCls:'disk',handler:function(){
                	var _form=this.ownerCt.ownerCt;
                	if(_form.node)
					{
						var noderec=_form.node.attributes.record;
						var jr=new JrafRequest('pcmc','sys','uptOpr');
						jr.setForm(_form);
						jr.setExtraPs({__sysName:noderec.get('__sysName'),oldOprID:noderec.get('enname'),pid:noderec.get('pid')});
	                    jr.setSuccFn(function(a,_resp,xr){
	                    	var fform=_form.getForm();
	                    	var oprID=fform.findField('__oprID').getValue();
	                    	var sid=noderec.get('pid')+'|'+oprID;
	                    	noderec.set('sid',sid);
	                    	noderec.set('enname',oprID);
	                    	noderec.set('cnname',fform.findField('desc').getValue());
	                    	var att=_form.node.getOwnerTree().getLoader().createAttrByRec(noderec);
	                    	_form.node.setText(att.text);
	                    	_form.node.id=sid;
	                    });
						jr.postData();
					}
                }}],
                items : [{
                    layout : "column",
                    items : [{
                        layout : "form",
                        columnWidth : 0.5,
                        items : [{
                            xtype : "textfield",
                            fieldLabel : "oprID",
                            name : "__oprID",
                            allowBlank : false
                        }],
                        bodyBorder : false
                    },{
                        layout : "form",
                        columnWidth : 0.5,
                        items : [{
                            name : "desc",
                            fieldLabel : "描述",
                            allowBlank : true,
                            xtype : "textfield"
                        }],
                        bodyBorder : false
                    }],
                    bodyBorder : false,
                    anchor : "100%"
                },{
                    layout : "column",
                    items : [{
                        items : [{
                            name : "bean",
                            fieldLabel : "处理类",
                            allowBlank : true,
                            xtype : "textfield",
                            width : 240
                        }],
                        layout : "form",
                        columnWidth : 0.5,
                        bodyBorder : false
                    },{
                        layout : "form",
                        items : [{
                            name : "jndi",
                            fieldLabel : "jndi",
                            allowBlank : true,
                            xtype : "textfield",
                            width : 240
                        }],
                        columnWidth : 0.5,
                        bodyBorder : false
                    }],
                    bodyBorder : false,
                    anchor : "100%"
                }]
            },{
                xtype : "panel",
                anchor : "99% 90%",
                items : [ {
                    xtype : "form",
                    labelAlign:'right',
                    title : "Action",
                    titleCollapse: true,
	    			collapsible: true,
                    anchor : "99%",
                    itemId : "actForm",
                    disabled : true,
                    tbar:[{text:'保存Act',iconCls:'disk',handler:function(){
                    	var _form=this.ownerCt.ownerCt;
                    	var _store=JrafUTIL.findCmp(MainPanel,'actFldGrid').getStore();
	                	if(_form.node)
						{
							var noderec=_form.node.attributes.record;
							var jr=new JrafRequest('pcmc','sys','uptAct');
							var ps=JrafUTIL.crDataAllRecNoEncode(_store,{__sysName:noderec.get('__sysName'),__oprID:getOprAct(noderec.get('pid')),__actions:noderec.get('enname')});
							var _store2=JrafUTIL.findCmp(MainPanel,'actMsgGrid').getStore();
							var ps2=JrafUTIL.crDataAllRecNoEncode(_store2);
							Ext.apply(ps,ps2);
							var _store3=JrafUTIL.findCmp(MainPanel,'actFlowGrid').getStore();
							var ps3=JrafUTIL.crDataAllRecNoEncode(_store3);
							Ext.apply(ps,ps3);
							jr.setForm(_form);
							jr.setExtraPs(ps);
		                    jr.setSuccFn(function(a,_resp,xr){
		                    	var fform=_form.getForm();
		                    	var actname=fform.findField('actname').getValue();
		                    	var sid=noderec.get('pid')+'|'+actname;
		                    	noderec.set('sid',sid);
		                    	noderec.set('enname',actname);
		                    	noderec.set('cnname',fform.findField('desc').getValue());
		                    	var att=_form.node.getOwnerTree().getLoader().createAttrByRec(noderec);
		                    	_form.node.setText(att.text);
		                    	_form.node.getUI().getIconEl().className='x-tree-node-icon '+(fform.findField('accredit').getValue()?'application-key':'');
		                    	_form.node.id=sid;
		                    });
							jr.postData();
						}
                    }}],
                    items : [{
                        xtype : "panel",
                        layout : "column",
                        items : [{
                            xtype : "panel",
                            columnWidth : 0.33,
                            bodyBorder : false,
                            items : [{
                                name : "actname",
                                fieldLabel : "action",
                                allowBlank : false,
                                xtype : "textfield"
                            }],
                            layout : "form"
                        },{
                            xtype : "panel",
                            columnWidth : 0.33,
                            bodyBorder : false,
                            items : [{
                                name : "old",
                                fieldLabel : "复用交易",
                                allowBlank : true,
                                xtype : "textfield"
                            }],
                            layout : "form"
                        },{
                            xtype : "panel",
                            columnWidth : 0.34,
                            bodyBorder : false,
                            items : [{
                                name : "desc",
                                fieldLabel : "描述",
                                allowBlank : true,
                                xtype : "textfield"
                            }],
                            layout : "form"
                        }],
                        bodyBorder : false,
                        anchor : "100%"
                    },{
                        xtype : "panel",
                        layout : "column",
                        items : [{
                            xtype : "panel",
                            layout : "form",
                            bodyBorder : false,
                            columnWidth : 0.33,
                            items : [{
                                name : "accredit",
                                itemId : "accredit",
                                hideLabel : false,
                                boxLabel : "交易授权",
                                xtype : "checkbox",
                                inputValue : true,
                                listeners : {  check : function(scope,checked){if(!checked){accCheck(false);}}}
                            }]
                        },{
                            xtype : "panel",
                            layout : "form",
                            bodyBorder : false,
                            columnWidth : 0.33,
                            items : [ {
                                name : "colacc",
                                itemId : "colacc",
                                hideLabel : false,
                                boxLabel : "列级授权",
                                xtype : "checkbox",
                                inputValue : true,
                                listeners : {  check : function(scope,checked){if(checked){accCheck(true);}}}
                            }]
                        },{
                            xtype : "panel",
                            layout : "form",
                            bodyBorder : false,
                            columnWidth : 0.34,
                            items : [{
                                name : "rowacc",
                                itemId : "rowacc",
                                hideLabel : false,
                                boxLabel : "行级授权",
                                xtype : "checkbox",
                                inputValue : true,
                                listeners : {  check : function(scope,checked){if(checked){accCheck(true);}}}
                            }]
                        }],
                        bodyBorder : false,
                        anchor : "100%"
                    },{
                        xtype : "panel",
                        layout : "column",
                        items : [{
                            xtype : "panel",
                            layout : "form",
                            bodyBorder : false,
                            columnWidth : 0.33,
                            items : [ {
                                name : "webservice",
                                hideLabel : false,
                                boxLabel : "WS接口",
                                xtype : "checkbox",
                                inputValue : true
                            }]
                        },{
                            xtype : "panel",
                            layout : "form",
                            bodyBorder : false,
                            columnWidth : 0.33,
                            items : [ {
                                name : "nologin",
                                hideLabel : false,
                                boxLabel : "无需登录",
                                xtype : "checkbox",
                                inputValue : true
                            }]
                        },{
                            xtype : "panel",
                            layout : "form",
                            bodyBorder : false,
                            columnWidth : 0.34,
                            items : [{
                                name : "log",
                                hideLabel : false,
                                boxLabel : "记日志",
                                xtype : "checkbox",
                                inputValue : true
                            }]
                        }],
                        bodyBorder : false,
                        anchor : "100%"
                    },{
                        xtype : "panel",
                        layout : "column",
                        items : [{
                            xtype : "panel",
                            layout : "form",
                            bodyBorder : false,
                            columnWidth : 1,
                            items : [ {
                                name : "oauth",
                                hideLabel : false,
                                boxLabel : "OAuth接口",
                                xtype : "checkbox",
                                inputValue : true
                            }]
                        }],
                        bodyBorder : false,
                        anchor : "100%"
                    },{
                            xtype : "panel",
                            layout : "form",
                            bodyBorder : false,
                            columnWidth : 0.25,
                            items : [{
                                xtype : "combo",
                                fieldLabel : "基本交易",
                                name : "base",
                                valueField : "v",
                                displayField : "t",
                                triggerAction : "all",
                                mode : "local",
                                store : new Ext.data.ArrayStore({id: 0,fields: ['v','t'],
    data: [['Insert', 'Insert'],['Update','Update'],['Delete','Delete'],['Detail','Detail'],['List','List'],['ListAll','ListAll']]}),
                                width : 80
                            }]
                    },{
                        xtype : "fieldset",
                        items : [{
                            xtype : "panel",
                            layout : "column",
                            items : [{
                                xtype : "panel",
                                bodyBorder : false,
                                columnWidth : 0.33,
                                layout : "form",
                                items : [{
                                    name : "tblname",
                                    itemId : "tablecombo",
                                    fieldLabel : "数据表",
                                    allowBlank : true,
                                    xtype : "tblcombo",
                                    editable : true,
                                    lazyRender : true
                                }]
                            },{
                                xtype : "panel",
                                columnWidth : 0.33,
                                bodyBorder : false,
                                layout : "form",
                                items : [{
                                    name : "orderby",
                                    fieldLabel : "排序",
                                    allowBlank : true,
                                    xtype : "textfield"
                                }]
                            },{
                                xtype : "panel",
                                layout : "form",
                                bodyBorder : false,
                                columnWidth : 0.34,
                                items : [{
                                    name : "groupby",
                                    fieldLabel : "分组",
                                    allowBlank : true,
                                    xtype : "textfield"
                                }]
                            }],
                            bodyBorder : false,
                            anchor : "100%"
                        },{
                            xtype : "textarea",
                            fieldLabel : "SQL查询",
                            name : "basesql",
                            height: 40,
                            anchor : "99%"
                        }],
                        layout : "form",
                        bodyBorder : false,
                        anchor : "100%",
                        id : "basePanel",
                        title : "Base Query",
                        titleCollapse : true,
                        collapsible : true
                    }]
                },
                {
                	xtype : "tabpanel",
                	activeTab : 0,
                	anchor : "99% 70%",
                	items:[
                		{
		                    xtype : "editorgrid",
		                    frame : true,
		                    title : "Request Fields",
		                    viewConfig : {forceFit:true},
		                    columnLines : true,
		                    autoHeight : false,
		                    height : 240,
		                    itemId : "actFldGrid",
		                    disabled : true,
		                    store : actStore,
		                    tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType();_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();}},{xtype: 'tbseparator'},{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}},{xtype: 'tbseparator'},{text:'加载',tooltip:'Load Fields from baseTable',iconCls:'arrow-refresh',handler:function(){
		                    	var tblco=JrafUTIL.findCmp(MainPanel,'tablecombo');
		                    	var _grid=this.ownerCt.ownerCt;
		                    	var tblval=tblco.getValue();
								if(""==tblval){
									Ext.Msg.show({
										title : '提示',
										msg :"请选择基本交易表",
										icon : Ext.Msg.INFO,
										buttons : Ext.Msg.OK
									});
									return;
								}else{
									Ext.Msg.show({
										title : '提示',
										msg :"加载数据表并刷新Fields列表",
										icon : Ext.Msg.WARNING,
										buttons : Ext.Msg.YESNO,
										fn:function(buttonId){
											if('yes'==buttonId){
												var jr=new JrafRequest('pcmc','sys','getTblDetail',{recordType:actFldRecord,idProperty:'name'});
												jr.setExtraPs({__sysName:tblco.getStore().baseParams['__sysName'],__tblName:tblval});
										        jr.setSuccFn(function(a,_resp,xr){
										        	_grid.getStore().loadData(_resp.responseXML);
										        });
												jr.postData();
											}
										}
									});
								}
		                    }},{xtype: 'tbseparator'}],
		                    colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
		{width:40,sortable:true,header:'名称',dataIndex:'name',editor: new Ext.form.TextField({allowBlank: false})},
		{width:40,sortable:true,header:'描述',dataIndex:'title',editor: new Ext.form.TextField({allowBlank: false})},
		{width:40,sortable:true,header:'类型',dataIndex:'type',editor:typeCombo},
		{width:40,sortable:true,header:'最长',dataIndex:'maxlen',editor: new Ext.form.NumberField({allowDecimals: false,allowNegative:false})},
		{width:40,sortable:true,header:'最短',dataIndex:'minlen',editor: new Ext.form.NumberField({allowDecimals: false,allowNegative:false})},
		{width:40,sortable:true,header:'非空',dataIndex:'notnull',editor:boolComboEditor()},
		{width:40,sortable:true,header:'主键',dataIndex:'pk',editor:boolComboEditor()},
		{width:40,sortable:true,header:'自定义类',dataIndex:'reqclass',editor: new Ext.form.TextField()},
		{width:40,sortable:true,header:'缺省值',dataIndex:'defaultval',editor: new Ext.form.TextField()},
		{width:40,sortable:true,header:'自生成',dataIndex:'gen',editor:genCombo,renderer:Ext.util.Format.storeRenderer(genCombo.store,"v","t")},
		{width:40,sortable:true,header:'查询.引用',dataIndex:'refname',editor: new Ext.form.TextField()},
		{width:40,sortable:true,header:'查询.操作符',dataIndex:'op',editor:opCombo}]),
		                    sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}})
		                    
		                },
		                {
		                    xtype : "editorgrid",
		                    frame : true,
		                    title : "Hint/Error Msg",
		                    viewConfig : {forceFit:true},
		                    columnLines : true,
		                    autoHeight : false,
		                    height : 240,
		                    itemId : "actMsgGrid",
		                    disabled : true,
		                    store : msgStore,
		                    tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType({msgcode:'',msgdesc:''});_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();}},{xtype: 'tbseparator'},{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}}],
		                    colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
		{width:40,sortable:true,header:'代码',dataIndex:'msgcode',editor: msgCombo,renderer:Ext.util.Format.storeRenderer(msgCombo.store,"v","t")},
		{width:40,sortable:true,header:'描述',dataIndex:'msgdesc',editor: new Ext.form.TextField({allowBlank: true})}]),
		                    sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}})
		                },
		                {
		                    xtype : "editorgrid",
		                    frame : true,
		                    title : "Forward",
		                    viewConfig : {forceFit:true},
		                    columnLines : true,
		                    autoHeight : false,
		                    height : 240,
		                    itemId : "actFlowGrid",
		                    disabled : true,
		                    store : flowStore,
		                    tbar : [{text:'新增',iconCls:'add',ref: '../addBtn',handler:function(){var _grid=this.ownerCt.ownerCt;var recordType=_grid.getStore().recordType;var nr=new recordType({flowresult:'',flowtype:'0',flowpath:''});_grid.getStore().add(nr);_grid.getSelectionModel().selectLastRow();var rec=_grid.getSelectionModel().getSelected();}},{xtype: 'tbseparator'},{text:'删除',iconCls:'delete',ref: '../removeBtn',disabled: true,handler:function(){var _grid=this.ownerCt.ownerCt;var ckrs=_grid.getSelectionModel().getSelections();for(var i=0;i<ckrs.length;i++){_grid.getStore().remove(ckrs[i]);}}}],
		                    colModel : new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(),
		{width:40,sortable:true,header:'Result',dataIndex:'flowresult',editor: new Ext.form.TextField({allowBlank: true})},
		{width:40,sortable:true,header:'Flow Type',dataIndex:'flowtype',editor: ftypeCombo,renderer:Ext.util.Format.storeRenderer(ftypeCombo.store,"v","t")},
		{width:40,sortable:true,header:'Path URI',dataIndex:'flowpath',editor: new Ext.form.TextField({allowBlank: true})}]),
		                    sm : new Ext.grid.CheckboxSelectionModel({listeners:{selectionchange:function(sm){var _grid=this.grid;if(sm.getCount()){_grid.removeBtn.enable();}else{_grid.removeBtn.disable();}}}})
		                }
                	]
                }
                ],
                layout : "anchor"
            }]
        }]
    },{
        region : "west",
        title : "操作列表",
        width : 280,
        split : true,
        collapsible : true,
        titleCollapse : false,
        items : [{
            xtype : "treepanel",
            height : 400,
            animate : true,
            autoScroll : true,
            containerScroll : true,
            rootVisible : false,
            root : new Ext.tree.AsyncTreeNode({id:'-1',text:'Tree Root',draggable : false}),
            loader : new JrafXmlTreeLoader({nparams: {nid:'sid',pid:'pid',ntext:'{enname}({cnname})',loadAll:false,expanded:true,sm:'all',isLeaf:'isleaf',icon:'icon'},action: {sysName:'pcmc',oprID:'sys',actions:'getOprList'},baseParams : {__qtype:'2',stype:'$NODEVALUE$',__sysName:'$NODEVALUE$'}}),
            listeners : {
  contextmenu : function(node,e){
  e.preventDefault();
  node.select();
  node.expand();
  var noderec=node.attributes.record;
  var tmenu=node.getOwnerTree().menu;
  if(!tmenu){
    tmenu =  new Ext.menu.Menu({
	items : [{
		id : 'addFile',
		text : '新增文件',
		iconCls:'add'
	},{
		id : 'addOpr',
		text : '新增Opr',
		iconCls:'add'
	},{
		id : 'delOpr',
		text : '删除Opr',
		iconCls:'delete'
	},{
		id : 'addAct',
		text : '新增Act',
		iconCls:'add'
	},{
		id : 'delAct',
		text : '删除Act',
		iconCls:'delete'
	}],
	listeners: {
		itemclick: function(item) {
			var node = item.parentMenu.treeNode;
			var noderec = node.attributes.record
			switch (item.id) {
			case 'addFile':
				Ext.Msg.prompt('提示', '文件名:', function(btn, text){
				    if (btn == 'ok' && ''!=text){
				        var jr=new JrafRequest('pcmc','sys','createOprFile');
						jr.setExtraPs({__sysName:noderec.get('__sysName'),pid:noderec.get('sid'),fname:text});
				        jr.setSuccFn(function(a,_resp,xr){
				        	node.getOwnerTree().getLoader().processResponse(_resp,node);
				        });
						jr.postData();
				    }
				});
				break;
			case 'addOpr':
				var newRec = new nodeRecord({sid:'',pid:noderec.get('sid'),__sysName:noderec.get('__sysName'),enname:'',cnname:'new Opr',stype:'2'});
				var newNode = node.getOwnerTree().getLoader().createNodeByRec(newRec);
				node.appendChild(newNode);
				newNode.select();
				loadRec(newNode);
				break;
			case 'delOpr':
				if(''==noderec.get('sid'))
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
							var jr=new JrafRequest('pcmc','sys','delOpr');
							jr.setExtraPs({__sysName:noderec.get('__sysName'),__oprID:noderec.get('enname')});
					        jr.setSuccFn(function(a,_resp,xr){
					        	deleteNode(node);
					        });
							jr.postData();
						}
					}
				});
				break;
			case 'addAct':
				var newRec = new actAttRecord({sid:'',pid:noderec.get('sid'),__sysName:noderec.get('__sysName'),enname:'',cnname:'new Act',stype:'3',isleaf:'true'});
				var newNode = node.getOwnerTree().getLoader().createNodeByRec(newRec);
				node.appendChild(newNode);
				newNode.select();
				loadRec(newNode);
				break;
			case 'delAct':
				if(''==noderec.get('sid'))
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
							var jr=new JrafRequest('pcmc','sys','delAct');
							jr.setExtraPs({__sysName:noderec.get('__sysName'),__oprID:getOprAct(noderec.get('pid')),__actions:noderec.get('enname')});
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
  tmenu.items.each(function(item){
  	item.disable();
  });
  //(1:sys,2:opr,3:act,4:file,5:tbl)
  var stype = noderec.get('stype');
  switch(stype){
  	case '1':
  	  tmenu.get('addFile').setDisabled(false);
  	  break;
  	case '2':
  	  tmenu.get('delOpr').setDisabled(false);
  	  tmenu.get('addAct').setDisabled(false);
  	  break;
  	case '3':
  	  tmenu.get('delAct').setDisabled(false);
  	  break;
  	case '4':
  	  tmenu.get('addOpr').setDisabled(false);
  	  break;
  }
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