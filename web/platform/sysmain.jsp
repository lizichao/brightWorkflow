<%@ page contentType="text/html; charset=GBK" %>
<%
	request.setCharacterEncoding("GBK");
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html>
<head>
	<title>系统管理</title>
<%@ include file="/platform/public/includejs.jsp"%>
  <script language="javascript" type="text/javascript">
  	Ext.onReady(function() {
		Ext.QuickTips.init();
		// turn on validation errors beside the field globally
		Ext.form.Field.prototype.msgTarget = 'side';

	    var hd = new Ext.BoxComponent({
                    region:'north',
                    el: 'header',
                    height:36
                });
	    var mtree = new Ext.tree.TreePanel({
	        id:'menu-tree',
	        title:'系统菜单',
	        region:'west',
	        split:true,
	        width: 200,
	        minSize: 175,
	        maxSize: 500,
	        collapsible: true,
	        margins:'0 0 0 5',
	        cmargins:'0 0 0 0',
	        lines:false,
	        autoScroll:true,
	        animCollapse:false,
	        animate: false,
	        collapseMode:'mini',
	        rootVisible:false,
	        root: new Ext.tree.AsyncTreeNode(),
	        loader: new JrafXmlTreeLoader({
	            nparams: {nid:'menuid',pid:'pmenuid',ntext:'{menuname}',loadAll:true,expanded:false,chk:false,icon:'imgurl'},
	            action: {sysName:'pcmc',oprID:'menu',actions:'getUserMenuBySubsys'},
	            baseParams: {subsysid:'-1',PageSize:'-1'},
	            listeners:{load:function(scope,node,resp){JrafUTIL.expandNode(node,2);}}
	        }),
	        collapseFirst:false
	    });
	    var mainPanel=new Ext.TabPanel({
            region:'center',
            enableTabScroll: true,
            activeTab:0,
            items:[{
            	id: 'welcome',
                contentEl:'center1',
                title: 'Welcome',
                autoScroll:true
            }]
        });
	    mtree.on('click', function(node, e){
	    	if(node.isLeaf()||!node.hasChildNodes()){
		        e.stopEvent();
		        var nrec=node.attributes.record;
		        var nw=nrec.data['newwin'];
		        var u=Jraf_ContextPath+nrec.data['linkurl'];
		        if('1'==nw){
		        	JrafUTIL.openwin(node.id,u);
		        }else{
		        	JrafUTIL.addTab(mainPanel,u,node.text,node.id,nw);
		        }
		    }
		});
		
		var loadsys=function(sysid,sysnm){
			mtree.getLoader().baseParams['subsysid']=sysid;
	        mtree.getLoader().load(mtree.getRootNode());
	        mtree.setTitle(sysnm);
		};
		
		var cpanel=new Ext.Panel({
			region:'center',
			layout:'border',
			items:[mtree,mainPanel],
			tbar:[],
			bbar:['<b>登录用户</b>：<%=session.getAttribute("username")%>','-','<b>所属机构</b>：<%=session.getAttribute("deptname")%>','->',
				{iconCls:'folder-page',text:'消息',handler:function(){JrafUTIL.addTab(mainPanel,'<%=request.getContextPath()%>/platform/sm/message.jsp','我的消息','n_msg_999','');}},
				{iconCls:'door-out',text:'退出',handler:function(){
					Ext.Msg.confirm('提示','您是否真要退出！！',
						function (btn){
							if(btn=='yes')
								window.top.location='<%=request.getContextPath().equals("")?"/":request.getContextPath()%>';
						}
					)
				}
			}]
		});
	    var viewport = new Ext.Viewport({
	        layout:'border',
	        items:[hd, cpanel]
	    });
	    viewport.doLayout();
	
	    var jr=new JrafRequest('pcmc','subsys','getUserPcmcSubsys');
        jr.setSuccFn(function(a,_resp,xr){
        	var rs=a.records;
        	var tbar=cpanel.getTopToolbar();
        	for(i=0;i<rs.length;i++){
        		var cnname=rs[i].get('cnname'),subsysid=rs[i].get('subsysid');
        		tbar.add({text:'<b>'+cnname+'</b>',handler:loadsys.createDelegate(this,[subsysid,cnname])},'-');
        	}
        	tbar.doLayout();
        });
		jr.postData();
		
		JrafUTIL.putCmp("MainPanel",mainPanel);
	});</script>
</head>
<body>
  <div id="header" style="border:0 none; background:#1E4176 url(<%=request.getContextPath()%>/images/hd-bg.gif) repeat-x 0 0; padding-top:3px; padding-left:3px;">
	<div style="font:24px;color:white;margin:5px;">深圳市亮信科技智能开发平台</div>
  </div>
  <div id="center1">
        <p><b>欢迎界面</b></p>
  </div>
</body>
</html>
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/public/message.js"></script>
<script language="javascript" type="text/javascript">
   showMyMessage();
</script>