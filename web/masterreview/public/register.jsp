<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.util.*"%>
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.jraf.auth.*"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.brightcom.jraf.conf.BrightComConfig"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>校长职级系统注册</title>
<link href="/js/jquery/plugin/toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
<link href="/js/jquery/plugin/selectpick/selectpick.css" rel="stylesheet" type="text/css"  />


<script src="/js/jquery/jquery-1.7.1.min.js?v=2.0.5" type="text/javascript"></script>
<script src="/js/jquery/plugin/toastr/toastr.min.js" type="text/javascript"></script>
<script src="/js/des.js" type="text/javascript"></script>
<script src="/platform/public/sysparam.js" type="text/javascript"></script>
<script src="/js/jquery/common.js" type="text/javascript"></script>
<script src="/js/jquery/plugin/selectpick/selectpick.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/workflow.js"></script>
<style type="text/css">
	*{
		padding:0;
		margin:0;
	}
	.load{
		width: 445px;
		height: 376px;
		background: #fff;
		border-bottom:1px solid #565656;
		margin-bottom: 30px;
	}
	.loadin{
		width: 100%;
		height: 100%;
		border:1px solid #dddddd;
	}
	.loadhd{
		padding-top:18px;
		line-height: 35px;
		padding-left: 44px;
		font-size: 16px;
		color:#44b549;
		position: relative;
		border-bottom: 1px solid #d4d4d4;
		margin-bottom: 30px;
	}
	.loadspan{
		display: inline-block;
		width: 161px;
		border-bottom: 4px solid #44b549;
		font-weight: bold;
	}
	.close{
		position: absolute;
		top:20px;
		right:15px;
	}
	.loadbd{
		padding:0 40px;
	}
	input,button{
		outline: none;
		border:0;
	}
	.loadbd input{
		width: 100%;
		font-size: 18px;
		margin-bottom: 15px;
		height: 45px;
		color:#cccccc;
		padding-left: 10px;
		line-height: 45px;
		background: #f1f1f1;
		border:1px solid #dfdfdf;
	}
	.loadbd input.passtxt{
		margin-bottom: 50px;
	}
	.loadbd input.radiotxt,.loadbd label{
		width: auto;
		color:#999999;
		height: auto;
	}
	.fl{
		float: left;
	}
	.fr{
		float: right;
	}
	.fotul li{
		float: left;
		margin-right: 5px;
		color:#999999;
	}
	a{
		text-decoration: none;
	}
	ul{
		list-style-type: none;
	}
	.fotul li a{
		font-size: 14px;
		color:#ef810c;
	}
	.btnregister{
		height: 45px;
		border-radius: 5px;
		background: #44b549;
		color:white;
		text-align: center;
		width: 100%;
		clear: both;
		margin-top: 10px;
	}
	.bd1{
		overflow: hidden;
		margin-bottom: 15px;
		height:62px;
	}
	.bd1 span{
		width: 90px;
		float: left;
		line-height: 45px;
		color:gray;
	}
	.bd1 input{
		width: 260px;
		float: left;
	}
	.load1,.loadbd{
		height: auto;
		
	}
	.loadbd{
			padding-bottom: 50px;
	}
	.top1{
	  top:359px;
	}
</style>
</head>
<body>
<form id="form1" name="form1">
<div class="load load1">
		<div class="loadin">
			<div class="loadhd">
			<span class="close" onclick="windowclose();"><img style="cursor:pointer;" src="/images/close.png" alt="" /></span>
				<span class="loadspan">校长注册</span>
			</div>
			<div class="loadbd">
				<div class="bd1" style="margin-bottom: 0px;">
				 	<select id="districtid" name="districtid" class="selectObj" emptytxt="区" cascade="deptid" >
				 	<option value="" selected>区</option>
				 	</select>
				 	<select id="deptid" name="deptid" class="selectObj" emptytxt="学校" cascade="gradecode" margin="80px;" addwidth="122px;" >
				      <option value="" selected>学校</option>
	  				</select>
			 	</div>
			    <div class="bd1" style="margin-bottom: 0px;height:31px;">
			     	<select id="ispositive" name="ispositive" class="selectObj" emptytxt="正副校长" cascade="classcode" lesstop="20px;">
		 			<option value="" selected>正副校长</option>
      				</select>
      				
			    	<select id="phasestudy" name="phasestudy" class="selectObj" emptytxt="学段" cascade="" margin="42px;" addwidth="122px;" lesstop="20px;">
		 			  <option value="" selected>学段</option>
    			 	</select>
			 	</div>
			 	<!--  
			 	<div class="bd1" style="margin-bottom: 0px;height:31px;">
			 		<select id="gradecode" name="gradecode" class="selectObj" emptytxt="年级" cascade="classcode" lesstop="20px;">
		 			<option value="" selected>年级</option>
    			 	</select>
    			 	<select id="classcode"name="classcode" class="selectObj" emptytxt="班级" cascade="" margin="67px;" addwidth="122px;" lesstop="20px;">
		 			<option value="" selected>班级</option>
      				</select>
			 	</div>-->
			 	<div class="bd1">
			 		<span>姓名</span>
			 		<input id="username" name="username" onblur="vode_name(this);" >
			 	</div>
			 	<div class="bd1">
			 		<span>身份证号</span>
			 		<input id="usercode" name="usercode"  >
			 		<input type="hidden" id="idnumber" name="idnumber"/>
			 	</div>
			 	<div class="bd1">
			 		<span>手机号</span>
			 		<input id="mobile" name="mobile" >
			 	<!--  <input type="hidden" id="tel" name="mobile"/>-->	
			 	</div>
			 	<div class="bd1">
			 		<span>密码</span>
			 		<input id="pwd" type="password"><input type="hidden" id="userpwd" name="userpwd"/>
			 	</div>
			 	<div class="bd1">
			 		<span>确认密码</span>
			 		<input id="repwd" type="password" >
			 	</div>
			
			 	<div class="bd1" id="phone_valid" style="display:none;">
			 		<span style="color:#44b549">短信验证码</span>
			 		<input id="phone_valid" name="phone_valid" >
			 	</div>
			 <!-- style="display:none;" -->	
				<button style="display:none;" id="btn_register" type="button" class="btnregister"  onclick="regStudent()" style="cursor:pointer;background:#44b549">注 册</button>
				<button id="btn_getPV" type="button" class="btnregister" onclick="getPhoneValid()" style="cursor:pointer;background:#44b549">获取验证码</button>
			</div>
		</div>
</div>
</form>	
</body>
<script type="text/javascript">
function windowclose () {
	top.$.fancybox.close();
}
</script>
<script>
function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");  
    var r = window.location.search.substr(1).match(reg);  
    if (r != null) return unescape(r[2]); return "";  
}

$(document).ready(function(){
	Brightcom.workflow.initSelectCombox('headmaster_phase_study','phasestudy');
	Brightcom.workflow.initSelectCombox('headmaster_ispositive','ispositive');
	debugger
	$(".selectObj").selectpick({width:100,height:28,
        onSelect:function(selid,value,text){
           $("#"+selid).val(value);
           var 	cascadeObjID = $("#"+selid).attr("cascade");
		   if (cascadeObjID){
		       var  cascadeEmptyTxt = $("#"+cascadeObjID).attr("emptytxt");
               setSelectOption(cascadeObjID,value,cascadeEmptyTxt);
		   }
		}
	  });
	$("#selectpick_districtid").addClass("top1");
	//设置默认城市
	setSelectOption("districtid","8a21b3ab4d23c0a7014d2c5f4910001a","区");	//	深圳市
});

//下拉
function setSelectOption(_cascadeid,_selidvalue,_emptyTxt){
    var bcReq;
	//if (_cascadeid=="gradecode"){//查询区下面的学校
	 //  bcReq = new BcRequest('yuexue','Register','getSchoolGrade');
      // bcReq.setExtraPs({"deptid":_selidvalue,"PageSize":"0"});
	//}
	/*
	else if  (_cascadeid=="classcode"){
	   bcReq = new BcRequest('yuexue','Register','getGradeClass');
	    bcReq.setExtraPs({"deptid":$("#deptid").val(),"gradecode":_selidvalue,"PageSize":"0"});
	}*/
	//else {
	   bcReq = new BcRequest('yuexue','Register','getChildrenDept');
	   bcReq.setExtraPs({"pdeptid":_selidvalue,"PageSize":"0"});
	//}
	bcReq.setSuccFn(function(data,status){
		$("#"+_cascadeid+" option").remove();//清空原信息
		$("#"+_cascadeid).append("<option value=''>"+_emptyTxt+"</option>");
		for(var i=0;i<data.Data.length;i++){
		    var _rec = data.Data[i];
			$("#"+_cascadeid).append("<option value='"+_rec.option_id+"'>"+_rec.option_name+"</option>");
		}
    });
	bcReq.postData();
}

//去特殊字符
function stripscript(s){
	var pattern = new RegExp("[`~!?@#$^&*()|{}';:',\\[\\]<>/~！？＠＃＄％@#＊＾＆％%￥……&*（）——|{}【】‘；：”“'，、]");
	var rs = ""; 
	for (var i = 0; i < s.length; i++) { 
		rs = rs+s.substr(i, 1).replace(pattern, ''); 
	}
	return rs;
}
//失去焦点时验证账号
function vode_code()  {
	var sMobile = $("#usercode").val();
	/*
    if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(sMobile)) || sMobile.length != 11){
    	top.toastr.clear();
		top.toastr.error("请输入正确手机号！");
		$("#usercode").focus();
        return false; 
    }*/
    if(!isCardNo(sMobile)){
    	top.toastr.clear();
		top.toastr.error("请输入正确身份证号码！");
		$("#usercode").focus();
        return false; 
    }
    $("#idnumber").val(sMobile);
    return true;
}

function isCardNo(card)  
{  
   // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
   var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
   if(reg.test(card) === false)  
   {  
       alert("身份证输入不合法");  
       return  false;  
   }  
   return true;
}

/*失去焦点时验证邮箱格式
function vode_email() {
	var code = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
	if(!code.test($("#iptemail").val())  && $("#iptemail").val() != ''){
		top.toastr.clear();
		top.toastr.error("邮箱格式不正确！");
		$("#iptemail").focus();
		return false;
	}	
	return true;
}*/

//失去焦点时验证用户名
function vode_name(eve) {
	$(eve).val(stripscript($(eve).val()));
}
function checkForm () {
	var controls = document.getElementsByTagName('input');
  	for  (var i=0; i<controls.length;i++) {
  		/*
    	if (controls[i].type=='text'|| controls[i].type=='password') {
      		if (controls[i].value == '') {
      			top.toastr.clear();
				top.toastr.error("请完善所有信息！");
				$(controls[i]).focus();
				return false;
      		}
    	}*/
  	}
	if($("#pwd").val().length<6 || $("#pwd").val().length>12){
		top.toastr.clear();
		top.toastr.error("密码长度必须在6~12位之间！");	
		$("#pwd").focus();		
		return false;
	}
	if ($("#repwd").val() != $("#pwd").val()) {
		top.toastr.clear();
		top.toastr.error("两次密码输入不同！");
		$("#repwd").focus();
		return false;
	}
	if($("#username").val().length>20) {
		top.toastr.clear();
		top.toastr.error("用户姓名过长！");
		$("#username").focus();
		return false;
	}
	if (!vode_code()) {
	  return false;
    }
	$("#userpwd").val(strEnc($("#pwd").val()));
	return true;
}

//获取验证码
function getPhoneValid() {
	if (!vode_code()) {
		return false;
	}
	var bcReq = new BcRequest('yuexue','Register','getPhoneValid');
	bcReq.setExtraPs({"mobile":$("#mobile").val()});
	bcReq.setSuccFn(function (data,status) {
		$("#btn_getPV").hide();
		$("#btn_register").show();
		$("#phone_valid").show();
	});
	bcReq.setFailFn(function(data,status){
		top.toastr.error("获取验证码失败!"+data.error_description);
	});
	bcReq.postData();
}

//注册
function regStudent() {
	if (checkForm()) {
		var bcReq = new BcRequest('headmaster','headMasterbase','registerMaster');
	    bcReq.setForm("form1");
	    bcReq.setSuccFn(function(data,status){
	         top.toastr.success("注册成功!");
	         top.$.fancybox.close();
		     top.login();
	    });
	    bcReq.setFailFn(function(data,status){
		top.toastr.error("注册失败!"+data.error_description);
		});
	    bcReq.postData();
	}
}
</script>
</html>