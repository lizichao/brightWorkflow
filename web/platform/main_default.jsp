<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.system.pcmc.util.MenuUtil"%>
<%@ page import="cn.brightcom.system.pcmc.pm.PmInformations" %>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="sl" %>
<%
	request.setCharacterEncoding("GBK");
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
    
	Document xmlDoc = (Document)request.getAttribute("xmlDoc");
	Document reqXml = HttpProcesser.createRequestPackage("pcmc","userrole","getSubSysByUser",request);
	xmlDoc = SwitchCenter.doPost(reqXml);
    request.setAttribute("xmlDoc",xmlDoc);

	List recList = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
	String  firstSubSysId = "2";
	if (recList.size()>0){
	    Element el = (Element)recList.get(0);
        firstSubSysId =  el.getChildTextTrim("subsysid");
	}
%>
<html>
<head>
	<title>系统管理</title>
	<link href="/images/style.css" rel="stylesheet" type="text/css">
<%@ include file="/platform/public/includejs.jsp"%>
  <script language="javascript" type="text/javascript">
    var _htmlPath = '<iframe src="/platform/welcome.jsp" scrolling="auto" frameborder="0" width="100%" height="100%"></iframe>'; 
  	Ext.onReady(function() {
		Ext.QuickTips.init();
		// turn on validation errors beside the field globally
		Ext.form.Field.prototype.msgTarget = 'side';

	    var hd = new Ext.BoxComponent({ // raw
                    region:'north',
                    el: 'header',
                    height:40
                });
	    var ft = new Ext.BoxComponent({ // raw
                    region:'south',
                    el: 'footer',
                    height:24
                });
	    var mtree = new Ext.tree.TreePanel({
	        id:'menu-tree',
	        title:'系统菜单',
	        region:'west',
	        split:true,
			frame:true,
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
	            nparams: {nid:'menuid',pid:'pmenuid',ntext:'{menuname}',loadAll:true,expanded:true,chk:false,icon:'imgurl'},
	            action: {sysName:'pcmc',oprID:'menu',actions:'getUserMenuBySubsys'},
	            baseParams: {subsysid:'<%=firstSubSysId%>'}
	        }),
	        collapseFirst:false
	    });
	    var mainPanel=new Ext.TabPanel({
            region:'center',
			id:'basePanel',
            activeTab:0,
            items:[{
            	id: 'welcome',
                contentEl:'center1',
                title: '欢迎光临',
                autoScroll:true,
				html: _htmlPath
            }],
            loadMenu:function(href,nm,mid){
            	href += (href.indexOf("?")>0)?"&":"?"+"aj_randnum="+Math.random();
            	var id = 'docpanel'+mid;
            	var fid= 'dociframe-'+mid;
		        var tab = this.getComponent(id);
		        if(tab){		        	
			        this.setActiveTab(tab);
			        if(mid!=tab.mid)
			        {
				        tab.setTitle(nm);
				        tab.show();
				        //tab.setSrc(href);
				        window.frames[fid].location.href=href;
				    }
		        }else{
		        	var _html='<iframe id="'+fid+'" name="'+fid+'" src="'+href+'" scrolling="auto" frameborder="0" width="100%" height="100%"></iframe>';
		            tab = this.add({
		            	//xtype : 'iframepanel',
		            	id: id,
		            	closable: true,
                		autoScroll:true,
		            	title: nm,
		            	loadMask : true,
					    frame : false,
					    mid: mid,
					    //frameConfig : {autoCreate:{id: 'docframe-' + mid,width:'100%',height:'100%'}},
		            	//defaultSrc: '_blank',
		            	//layout : 'fit'
				        listeners: {'beforedestroy' : function(){window.frames[fid].src="javascript:false";}},
		            	html:_html
		            });
		        	this.setActiveTab(tab);
		        	//tab.show();
		        	//tab.setSrc(href);
		        }
            }
        });
	    mtree.on('click', function(node, e){
	    	if(node.isLeaf()||!node.hasChildNodes()){
		        e.stopEvent();
		        var nrec=node.attributes.record;
		        var nw=nrec.data['newwin'];
		        if('1'==nw){
		        	JrafUTIL.openwin(node.id,nrec.data['linkurl']);
		        }else{
		        	mainPanel.loadMenu(nrec.data['linkurl'], node.text,node.id);
		        }
		    }
		});
	    var viewport = new Ext.Viewport({
	        layout:'border',
	        items:[hd, mtree, mainPanel,ft]
	    });
	    viewport.doLayout();
	});
	
	function changeSubSys(_subsysid){		
		var _menuTree = Ext.getCmp('menu-tree');        
		var  _loader = new JrafXmlTreeLoader({
	            nparams: {nid:'menuid',pid:'pmenuid',ntext:'{menuname}',loadAll:true,expanded:true,chk:false,icon:'imgurl'},
	            action: {sysName:'pcmc',oprID:'menu',actions:'getUserMenuBySubsys'},
	            baseParams: {subsysid:_subsysid}
	        });		
		_loader.load(_menuTree.root);        
	}	
	</script>
</head>
<body>
  <div id="header" style="background:#1E4176;">
	<!--
	<a href="/platform/login.jsp" style="float:right;margin-right:10px;"><img src="/images/exit.gif" style="width:95px;height:22px;margin-top:1px;"/></a>
    <div style="font:24px;color:white;margin:5px;">系统管理</div>
	-->
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td width="655"><img src="/images/topn2.jpg" width="655" height="62"></td>
		<td background="/images/topn3.jpg">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		   <tr>
			<td width="7"><a href="#"><img src="/images/arrow1.jpg" width="7" height="33" border="0"></a></td>
			<td valign="middle">
			   <table  border="0" cellspacing="0" cellpadding="0">
			       <tr>
				   <sl:with name="/DataPacket/Response/Data">
				       <sl:foreach name="Record">
				       <td width="12">&nbsp;</td>
				       <td  align="center">
					        <table  border="0" cellspacing="0" cellpadding="0">
							    <tr> 
								   <td  align="center">
									  <a href="#" onclick="changeSubSys('<sl:value name="subsysid"/>');">
										 <img border="0" src="<sl:value name="imgurl"/>">
									  </a>
								   </td>
                                </tr>
								<tr>
								    <td  align="center">
									    <a href="#" onclick="changeSubSys('<sl:value name="subsysid"/>');">
										      <sl:value name="cnname"/>
                                         </a>
                                    </td>
                                </td>
                            </table>								  
                       </td>
                       </sl:foreach>
                   </sl:with>					   
                   </tr>				   
			    </table>
			</td>
			<td width="7"><a href="#"><img src="/images/arrow2.jpg" width="7" height="33" border="0"></a></td>
		   </tr>
		</table>
		</td>
		<td width="20" class="toprbg">&nbsp;</td>
	  </tr>
	</table>
  </div>
  <div id="footer">
  	<div style="font-size:12px;margin:5px;">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
			<td align="left">&nbsp;当前登录用户：<%=session.getAttribute("username")%>&nbsp;&nbsp;&nbsp;&nbsp;所属单位：<%=session.getAttribute("deptname")%></td>
			<td align="right"><a  onclick = "javascript:return  Ext.Msg.confirm('提示','您是否真要退出！！',
			function (btn){if(btn=='yes'){			   	
				 window.top.location='/';
			}		
			});" href="#">
			<!--<img src="/images/exit.gif"/>-->
			退出系统
			</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		  </tr>
		</table>
	 </div>
  </div>
  <div id="center1">
  </div>
</body>
</html>

<script type="text/javascript" src="<%=request.getContextPath()%>/platform/public/message.js"></script>
<script language="javascript" type="text/javascript">
   showMyMessage();
</script>