  <%@ page contentType="text/html; charset=GBK" %>
  <style type="text/css">.m-navlinks a:hover{color:white;}</style>
   <div class="g-wrap m-header m-header1"> 
    <div class="g-flow f-pr f-cb"> 
     <h1 class="logo "><a class="f-hide" hidefocus="true" href="/" title="沃课堂">沃课堂</a><a class="spoc_icon" href="/"></a></h1> 
     <div class="nav f-cb"> 
       <!--  <a href="/mooc/student/main.jsp" hidefocus="true" class="first " id="stuResid"><div class="inner f-f0" style="width:80px;">资源</div><span class="ic f-pa"></span></a>-->
        <!--<a href="/mooc/school/school.jsp" hidefocus="true" class="first " id="schoolid"><div class="inner f-f0" style="width:80px;">学校</div><span class="ic f-pa"></span></a>-->
		<!-- <a href="/mooc/teacher/teacher.jsp" hidefocus="true" class="first " id="teacherid"><div class="inner f-f0"  style="width:80px;">名师</div><span class="ic f-pa"></span></a> -->
     	<%if("2".equals((String)session.getAttribute("usertype"))){%>
     	 <a href="/mooc/teacher/main.jsp" hidefocus="true" class="first " id="th_course"><div class="inner f-f0"  style="width:120px;">微课管理</div><span class="ic f-pa"></span></a>
     	 <a href="/mooc/teacher/exam_manager.jsp" hidefocus="true" class="first " id="th_paper"><div class="inner f-f0"  style="width:120px;">试卷管理</div><span class="ic f-pa"></span></a>  
     	<!--  <a href="/mooc/teacher/resource_manager.jsp" hidefocus="true" class="first" id="resources" ><div class="inner f-f0"  style="width:140px;">我的资源包</div><span class="ic f-pa"></span></a> --> 
     	  <a href="/mooc/teacher/tools.jsp" hidefocus="true" class="first" ><div class="inner f-f0"  style="width:160px;">微课制作工具</div><span class="ic f-pa"></span></a> 
    	<%}else if("1".equals((String)session.getAttribute("usertype"))){%>
     	 <!-- <a href="/mooc/student/student_myresource.jsp" hidefocus="true" class="first " id="myResource"><div class="inner f-f0"  style="width:140px;">我的资源包</div><span class="ic f-pa"></span></a> --> 
		<%}%>
		
		
     </div>
     <div class="m-navlinks" id="j-topnav">
     	<%if(cn.brightcom.jraf.util.StringUtil.isNotEmpty((String)session.getAttribute("username"))){
     	       String username1 =(String)session.getAttribute("username");
			   if(username1.length()>5){
			     username1=username1.substring(0, 5)+"...";
			   }
		 %>
     	  <span class="f-f0 f-fcc" style="font-size:16px;cursor:default;" title="欢迎您,<%= (String)session.getAttribute("username")%>">欢迎您,<%=username1%></span><span class="f-fcc">&nbsp;&nbsp;|&nbsp;&nbsp;</span><a id="price" href="/masterreview/alipay/my_tran_list.jsp" style="font-size:16px;" class="f-f0 f-fcc" title="查看交易记录">当前余额：￥0</a><span class="f-fcc">&nbsp;&nbsp;|&nbsp;&nbsp;</span><a href="/mooc/alipay/pay.jsp" style="font-size:16px;" class="f-f0 f-fcc" title="点击充值">充值</a><span class="f-fcc">&nbsp;&nbsp;|&nbsp;&nbsp;</span><a href="javascript:void();" onclick="updatepwd()" style="font-size:16px;" class="f-f0 f-fcc" title="修改密码">修改密码</a><span class="f-fcc">&nbsp;&nbsp;|&nbsp;&nbsp;</span><a href="/masterreview/public/logoff.jsp" style="font-size:16px;" class="f-f0 f-fcc" title="退出系统">注销</a>
    	<%}else{%>
     	<div class="unlogin">
     	  <a id="navLoginBtn" style="font-size:16px;" onclick="login()" class="f-f0 f-fcc">登录</a><span class="f-fcc">&nbsp;&nbsp;|&nbsp;&nbsp;</span><a style="font-size:16px;" onclick="register()" class="f-f0 f-fcc">注册</a>
     	</div>
			<%}%>
     </div>
    </div>
   </div>
   <script type="text/javascript">
		function login() {
			$.fancybox.open({href:"/masterreview/public/login.jsp",type:'iframe',width:447,height:378,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'no',preload:false}});
		}
		function register() {
			$.fancybox.open({href:"/masterreview/public/register.jsp",type:'iframe',width:449,height:722,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'auto',preload:false}});
		}
		function forgetpwd() {
			$.fancybox.open({href:"/masterreview/public/forgetpwd.jsp",type:'iframe',width:327,height:268,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'no',preload:false}});
		}
		function updatepwd() {
			$.fancybox.open({href:"/masterreview/public/updatepwd.jsp",type:'iframe',width:327,height:298,padding:0,margin:0,closeBtn:false,iframe:{scrolling:'no',preload:false}});
		}
		
	 </script>