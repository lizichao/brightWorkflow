<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<title>沃课堂</title>
<%@ include file="/masterreview/public/header.jsp"%>
<link rel="stylesheet" type="text/css" href="/images/mooc/Global.css" />
<style type="text/css">
<!--
body{background:#EEE;}
-->
</style>
</head>
<body>
<%@ include file="/masterreview/public/top.jsp"%>
<!-- Banner s -->
<div class="jf-banner"></div>
<!-- Banner e -->
<!-- 积分规则 s -->
<div class="jfgz">
	<div class="content">
		<h3>沃课堂平台积分兑换规则</h3>
		<table border="0" cellpadding="0" cellspacing="0" class="jf-tab">
			<tr><th style="width:120px;">用户角色</th><th style="width:120px;">积分类别</th><th style="width:80px;">积分</th><th class="txtAlignLeft" style="text-align:center;">积分计算规则</th></tr>
			<tr><td rowspan="6">教师</td>
			    <td>上传课件</td>
			    <td>10</td>
			    <td class="txtAlignLeft"><span style="font-weight: bold; font-size: 14px;">积分规则：</span>
			                            1、教师上传课件<span style="color: red;">10积分/课</span>等于<span style="color: red;">1元/课</span>。课件内容不得重复、课件需为上传者自行创建微课5-8分钟课件内容，按照先积分后核算的管理办法。
                                    <br/><span style="font-weight: bold; font-size: 14px;">积分测算：</span> 按照教师每天上传5个课件，每月30天，课件上传获得积分1500积分<span style="color: red;">（150元/月）</span>。
                </td>
			</tr>
			<tr>
			   <td>用户注册</td>
			   <td>5</td>
			   <td class="txtAlignLeft">注册教师用户获得5个积分<span style="color: red;">（0.5元）</span>，上传5个课件以上后，该部分积分可纳入积分兑换规则。</td>
            </tr>
            <tr>
			   <td>连续登录</td>
			   <td>1</td>
			   <td class="txtAlignLeft">每天登录教师用户每天获得1个积分<span style="color: red;">（0.1元）</span>，上传5个课件以上后，该部分积分可纳入积分兑换规则。</td>
            </tr>
            <tr>
			   <td>课件点击量</td>
			   <td>1</td>
			   <td class="txtAlignLeft">上传课件点击量符合课件20%时长在线时间，记录1次点击量对应课件上传教师获得1个积分</td>
            </tr>
            <tr>
			   <td>上传习题</td>
			   <td>5</td>
			   <td class="txtAlignLeft">积分规则：1、教师上传课件配套习题5积分/课等于0.5元/课。课件配套习题内容不得重复按照先积分后核算的管理办法。
                                                                                       积分测算：按照教师每天上传5套课件习题，每月30天，课件上传获得积分750积分（75元/月）。</td>
            </tr>
              <tr>
			   <td>转发</td>
			   <td>5</td>
			   <td class="txtAlignLeft">转发课件、平台到社交媒体，获得5积分，上线不超过20积分。</td>
            </tr>
			<tr><td rowspan="4">学生</td>
			    <td>用户注册</td>
			    <td>100</td>
			    <td class="txtAlignLeft">学生注册获得100积分，不进行金额兑换，可用作课程购买消费。</td>
			 </tr>
			  <tr>
			   <td>连续登录</td>
			   <td>1</td>
			   <td class="txtAlignLeft">每天登录获得1个积分，可积累进行课件购买。</td>
             </tr>
              <tr>
			   <td>转发</td>
			   <td>5</td>
			   <td class="txtAlignLeft">转发课件、平台到社交媒体，获得5积分，上线不超过20积分。</td>
            </tr>
               <tr>
			   <td>完成习题</td>
			   <td>1</td>
			   <td class="txtAlignLeft">完成全部配套习题课程获得1个积分，同套课题不重复提供积分</td>
            </tr>
		</table> 
	</div>
</div>
<!-- 积分规则 e -->
<%@ include file="/masterreview/public/footer.jsp"%>
</body>

</html>
