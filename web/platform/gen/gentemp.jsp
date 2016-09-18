<%@ page contentType="text/html; charset=GBK" %>
<%@ taglib uri="/WEB-INF/brightcom.tld" prefix="bc" %>
<%
	String tp=request.getParameter("___tp");
	String tabId=request.getParameter("___tabId");
	java.util.Enumeration enu=request.getParameterNames();
	StringBuffer opts=new StringBuffer("'{'");
	int c=0;
	while(enu.hasMoreElements())'{'
		String k=(String)enu.nextElement();
		String[] vs=request.getParameterValues(k);
		if(0<c++)opts.append(",");
		opts.append("''").append(k).append("'':[");
		for(int i=0;i<vs.length;i++)'{'
			if(i>0)opts.append(",");
			opts.append("''").append(vs[i]).append("''");
		}
		opts.append("]");
	}
	opts.append("}");
%>
<%if(!"2".equals(tp))'{'%>
<html>
<head>
<%@ include file="/platform/public/includejs.jsp"%>
<%}%>
<bc:vpage file="{0}">
<bc:vfuncs />
<bc:vrecords />
<bc:vstores />
<bc:vpanels />
<bc:vmain name="MainPanel" render="main_div" viewport="true"/>
<bc:vonload />
<%if(!"2".equals(tp))'{'%>
	Ext.onReady(function() '{'
		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = "side";
		var _mainPanel=new {1}();
		new Ext.Viewport('{'layout:"fit",items:_mainPanel.MainPanel});
		_mainPanel.__jrafonload(<%=opts%>);
		JrafUTIL.putCmp("{1}",_mainPanel);
	});
<%}else'{'%>
	var __mfunc=function()'{'
		var _mainPanel=new {1}();
		Ext.getCmp(''<%=tabId%>'').add(_mainPanel.MainPanel); 
		Ext.getCmp(''<%=tabId%>'').doLayout();
		_mainPanel.__jrafonload(<%=opts%>);
		JrafUTIL.putCmp("{1}",_mainPanel);
	}.createDelegate(this);
	<%if("true".equals(request.getAttribute("__gjfile__")))'{'%>JrafUTIL.scriptLoader("<%=request.getContextPath()%>{2}",true,__mfunc);<%}else'{'%>__mfunc();<%}%>
<%}%>
</bc:vpage>
<%if(!"2".equals(tp))'{'%>
</head>
<body>
</body>
</html>
<%}%>