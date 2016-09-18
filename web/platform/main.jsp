<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.system.pcmc.util.MenuUtil"%>
<%@ page import="cn.brightcom.system.pcmc.pm.PmInformations" %>
<%@ page import="cn.brightcom.jraf.conf.BrightComConfig"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.net.URLEncoder"%>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%@ include file="/masterreview/public/sessionoff.jsp"%>
<%
	request.setCharacterEncoding("GBK");
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	 String company =  "校长职级评定系统后台数据管理中心";
	
		String sessionUserId =  (String)request.getSession().getAttribute("userid");
		if (sessionUserId==null)
	       sessionUserId = "-1";
	    
		Document xmlDoc = (Document)request.getAttribute("xmlDoc");

		Document reqXml = HttpProcesser.createRequestPackage("pcmc","userrole","getSubSysByUser",request);
		reqXml.getRootElement().getChild("Request").getChild("Data").addContent((new Element("userid")).setText(sessionUserId));
		xmlDoc = SwitchCenter.doPost(reqXml);
	    request.setAttribute("xmlDoc",xmlDoc);

		List recList = xmlDoc.getRootElement().getChild("Response").getChild("Data").getChildren("Record");
		String  firstSubSysId = "2";
		int  subSysCount = recList.size();
		System.out.println("subSysCount===="+subSysCount);
		if (subSysCount>0){
		    Element el = (Element)recList.get(0);
	        firstSubSysId =  el.getChildTextTrim("subsysid");
		}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=company%></title>
	<link href="/images/style.css" rel="stylesheet" type="text/css">
<%@ include file="/platform/public/includejs.jsp"%>
  <script language="javascript" type="text/javascript">
    var _htmlPath = '<iframe src="/platform/welcome.jsp" scrolling="auto" frameborder="0" width="100%" height="100%"></iframe>'; 
	var maxTabCount = 5;
	var openTabCount = 0;
  	Ext.onReady(function() {
		Ext.QuickTips.init();
		// turn on validation errors beside the field globally
		Ext.form.Field.prototype.msgTarget = 'side';

	    var hd = new Ext.BoxComponent({ // raw
                    region:'north',
                    el: 'header',
                    height:84
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
	        width: 218,
	        minSize: 175,
	        maxSize: 500,	        
	        margins:'0 0 0 5',
	        cmargins:'0 0 0 0',
			frame:true,
	        lines:false,
			animate : true,
			autoScroll : true,
			containerScroll : true,
			collapsible : true,
			titleCollapse : false,
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
					if (openTabCount>=maxTabCount){
					   alert('只允许同时打开'+maxTabCount+'个菜单,请先关闭部分菜单!');
					   return;  
					}
					else{
					    openTabCount++;
					}
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
				        listeners: {'beforedestroy' : function(){openTabCount--;window.frames[fid].src="javascript:false";}},
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
				var u=Jraf_ContextPath+nrec.data['linkurl'];
		        if('1'==nw){
		        	//JrafUTIL.openwin(node.id,nrec.data['linkurl']);
					openFullScreenWin(node.id,nrec.data['linkurl']);
		        }else{
		        	//mainPanel.loadMenu(nrec.data['linkurl'], node.text,node.id);
					JrafUTIL.addTab(mainPanel,u,node.text,node.id,nw);
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
	
	function openWin(_url,_text,_id){
	    var _mainPanel = Ext.getCmp('basePanel'); 
		_mainPanel.loadMenu(_url,_text,_id);
	}

	function openStudentWin(_url,_text,_id){
		var _temp = (_url.indexOf("?")>0)?"&":"?"+"aj_randnum="+Math.random();
		var _href = _url+_temp;
		var id = 'docpanel'+_id;
		var fid= 'dociframe-'+_id;
	    var _mainPanel = Ext.getCmp('basePanel'); 
		var tab = _mainPanel.getComponent(id);
		if (tab){
		   if (window.frames[fid].location.href!=_href){
			   window.frames[fid].location.href=_href;
		   }
		}
		_mainPanel.loadMenu(_href,_text,_id);		
	}

	function openFullScreenWin(id,linkurl){ 
	    var _width = window.screen.width-20;
        var _height = window.screen.height+30;	
		//window.showModelessDialog(linkurl,id,"center:no;dialogLeft:10px;dialogTop:10px;scroll:0;status:0;help:0;resizable:0;dialogWidth:"+_width+"px;dialogHeight:"+_height+"px"); 
		var newWin = window.open(linkurl,id,'height='+_height+',width='+_width+',top=10px,left=10px,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');

		window.onfocus=function (){    
			if(newWin && !newWin.closed){  
				newWin.focus();  
			}  
		};   
		  
		window.document.onfocus=function (){    
			if(newWin && !newWin.closed){  
				newWin.focus();  
			}  
		};   
		  
		window.document.onclick=function (){    
			if(newWin && !newWin.closed){  
				newWin.focus();  
			}  
		};   
		  
		window.document.ondblclick=function (){    
			if(newWin && !newWin.closed){  
				newWin.focus();  
			}  
		}; 
	}	

	</script>
</head>
<body>
  <div id="header" style="background:#1E4176;height:84px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="background-image:url(/images/be/subimg01.jpg);background-repeat:repeat;">
		  <tr>
			<td width="490" align="center" background="/images/be/subtop1.jpg" height="84">
			    <div style="padding-left:40px;">
				    <font color="#FFFFFF" style="font-weight:bold;font-size:26px;"><%=company%></font>
				</div>
			</td>
			<td background="/images/be/img4.jpg" valign="middle" align="right" height="84">
			   <%if (subSysCount>1){//如果用户有多个子系统的操作权限,则显示子系统图标,否则不显示%>
			   <table  border="0" cellspacing="0" cellpadding="0" >
			       <tr>				   
				   <bc:with name="/DataPacket/Response/Data">
				       <bc:foreach name="Record">
				       <td width="12">&nbsp;</td>
				       <td  align="center">
					        <table  border="0" cellspacing="0" cellpadding="0">
							    <tr> 
								   <td  align="center">
									  <a href="#" onclick="changeSubSys('<bc:value name="subsysid"/>');">
										 <img border="0" src="<bc:value name="imgurl"/>">
									  </a>
								   </td>
                                </tr>
								<tr>
								    <td  align="center">
									    <a href="#" onclick="changeSubSys('<bc:value name="subsysid"/>');">
										      <div><font style="font-weight: bold; font-size:10px;color:#FFFFFF;">
											     <bc:value name="cnname"/></font>
											  </div>
                                         </a>
                                    </td>
                                </tr>
                            </table>								  
                       </td>
                       </bc:foreach>
                   </bc:with>					   
                   </tr>				   
			    </table>
				<%}
				 else{
				%>
                &nbsp;
				<%}
				%>
             </td>			
			<td width="455" height="84" align="right" valign="top" background="/images/be/img6.jpg">
			   <img src="/images/be/subimg03.jpg" width="455" height="84" />
			</td>
		  </tr>
		  <tr>
			<td height="2" colspan="4" bgcolor="#666666"> </td>
		  </tr>
		</table>
  </div>
  <div id="footer">
     <div id="toolbar"></div>
  </div>
  <div id="center1">
  </div>
</body>
</html>
<!-- 即时消息 -->
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/public/message.js"></script>
<script language="javascript" type="text/javascript">
   //showMyMessage();
</script>
<!-- //时钟
<script type="text/javascript" src="<%=request.getContextPath()%>/platform/public/clock.js"></script>
<script language="javascript" type="text/javascript">
	var clock = new Clock();
	clock.display(document.getElementById("clock"));
</script>
-->
<script language="javascript" type="text/javascript">
    var tb = new Ext.Toolbar();
    tb.render('toolbar');

	var extStyleMenu = new Ext.menu.Menu();
    extStyleMenu.add({id:'xtheme-vista',text:'vista风格',handler: onItemClick});
    extStyleMenu.add({id:'xtheme-brown02',text:'棕色风格',handler: onItemClick});
    extStyleMenu.add({id:'xtheme-brown',text:'褐色风格',handler: onItemClick});
    extStyleMenu.add({id:'xtheme-black',text:'黑色风格',handler: onItemClick});
    extStyleMenu.add({id:'xtheme-purple',text:'紫色风格',handler: onItemClick});
    extStyleMenu.add({id:'xtheme-green',text:'绿色风格',handler: onItemClick});
    extStyleMenu.add({id:'xtheme-red03',text:'红色风格',handler: onItemClick});
	extStyleMenu.add({id:'xtheme-pink',text:'粉红风格',handler: onItemClick});
	extStyleMenu.add({id:'xtheme-gray',text:'银白风格',handler: onItemClick});
	extStyleMenu.add({id:'xtheme-default',text:'默认风格',handler: onItemClick});	

	tb.add('-',{text:'当前登录用户：<%=session.getAttribute("username")%>',iconCls:'user'});
    tb.add('-',{text:'所属单位：<%=session.getAttribute("deptname")%>',iconCls:'book'});
	tb.add('-',{text:'修改密码',iconCls:'key',handler: onKeyItemClick});
    tb.add('-','->');
	tb.add('-',{text: '更换皮肤',iconCls:'ruby',menu: extStyleMenu});
    tb.add('-',{text: '退出系统',iconCls:'house-go',handler: onExitItemClick});
    tb.add('-');
    tb.doLayout();

	function onItemClick(item){
		var selectXtheme = item.id;
		var _themeValue = getCookie('sysTheme');
		if (selectXtheme!=_themeValue)
		{
		    saveCookie('sysTheme', selectXtheme);
			Ext.util.CSS.swapStyleSheet('xtheme','../../ext/resources/css/'+themeValue+'.css');
			document.location.href="/platform/main.jsp";
		}
	}

	function onExitItemClick()
    {
	    document.location.href="/index.jsp";
	}
	function onKeyItemClick()
    {
	   var _height = 226;
	   var _width = 330;
	   var _left = (window.screen.availWidth  - _width)/2; 
	   var _top  = (window.screen.availHeight - _height)/2;	   window.open("/platform/sm/changepwd.jsp",'修改密码','height='+_height+',width='+_width+',top='+_top+',left='+_left+',toolbar=no,menubar=yes,scrollbars=yes,resizable=no,location=no, status=no');	   
	}
</script>