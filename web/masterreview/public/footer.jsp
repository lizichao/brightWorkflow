  <%@ page contentType="text/html; charset=GBK" %>
  <div class="g-wrap m-foot f-pr" id="j-footer"> 
    <div class="g-flow f-cb"> 
     <div class="f1 f-fl"> 
      <div class="logo"></div> 
     </div>
     <div class="f2 f-fr f-pr" style="width:auto;">
      <h4 class="f-fcc">关于我们</h4>
      <div class="f-cb">
       <a class="f-fc9">电话：0755-25666343 | 邮箱：lh_jiaoyu@163.com</a><%if("2".equals((String)session.getAttribute("usertype"))){%>
       <a class="f-fc9" title="帮助文档-教师" href="/upload/help/teacher.doc"> | 帮助文档</a>
       <%}else if("9".equals((String)session.getAttribute("usertype"))){%>
       <a class="f-fc9" title="帮助文档-学校管理员" href="/upload/help/school-admin.doc"> | 帮助文档</a>
       <%}%>
      </div>
     </div> 
    </div> 
   </div> 
  </div>
  <div> 
   <div id="loadingMask" class="loading-mask" style="z-index: 10051; display: none;"></div> 
   <div id="loadingPb" class="u-loading f-cb" style="z-index: 10052; display: none;"></div> 
  </div>