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
<title>У��ְ��ϵͳע��</title>
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
				<span class="loadspan">У��ע��</span>
			</div>
			<div class="loadbd">
				<div class="bd1" style="margin-bottom: 0px;">
				 	<select id="districtid" name="districtid" class="selectObj" emptytxt="��" cascade="deptid" >
				 	<option value="" selected>��</option>
				 	</select>
				 	<select id="deptid" name="deptid" class="selectObj" emptytxt="ѧУ" cascade="gradecode" margin="80px;" addwidth="122px;" >
				      <option value="" selected>ѧУ</option>
	  				</select>
			 	</div>
			    <div class="bd1" style="margin-bottom: 0px;height:31px;">
			     	<select id="ispositive" name="ispositive" class="selectObj" emptytxt="����У��" cascade="classcode" lesstop="20px;">
		 			<option value="" selected>����У��</option>
      				</select>
      				
			    	<select id="phasestudy" name="phasestudy" class="selectObj" emptytxt="ѧ��" cascade="" margin="42px;" addwidth="122px;" lesstop="20px;">
		 			  <option value="" selected>ѧ��</option>
    			 	</select>
			 	</div>
			 	<!--  
			 	<div class="bd1" style="margin-bottom: 0px;height:31px;">
			 		<select id="gradecode" name="gradecode" class="selectObj" emptytxt="�꼶" cascade="classcode" lesstop="20px;">
		 			<option value="" selected>�꼶</option>
    			 	</select>
    			 	<select id="classcode"name="classcode" class="selectObj" emptytxt="�༶" cascade="" margin="67px;" addwidth="122px;" lesstop="20px;">
		 			<option value="" selected>�༶</option>
      				</select>
			 	</div>-->
			 	<div class="bd1">
			 		<span>����</span>
			 		<input id="username" name="username" onblur="vode_name(this);" >
			 	</div>
			 	<div class="bd1">
			 		<span>���֤��</span>
			 		<input id="usercode" name="usercode"  >
			 		<input type="hidden" id="idnumber" name="idnumber"/>
			 	</div>
			 	<div class="bd1">
			 		<span>�ֻ���</span>
			 		<input id="mobile" name="mobile" >
			 	<!--  <input type="hidden" id="tel" name="mobile"/>-->	
			 	</div>
			 	<div class="bd1">
			 		<span>����</span>
			 		<input id="pwd" type="password"><input type="hidden" id="userpwd" name="userpwd"/>
			 	</div>
			 	<div class="bd1">
			 		<span>ȷ������</span>
			 		<input id="repwd" type="password" >
			 	</div>
			
			 	<div class="bd1" id="phone_valid" style="display:none;">
			 		<span style="color:#44b549">������֤��</span>
			 		<input id="phone_valid" name="phone_valid" >
			 	</div>
			 <!-- style="display:none;" -->	
				<button style="display:none;" id="btn_register" type="button" class="btnregister"  onclick="regStudent()" style="cursor:pointer;background:#44b549">ע ��</button>
				<button id="btn_getPV" type="button" class="btnregister" onclick="getPhoneValid()" style="cursor:pointer;background:#44b549">��ȡ��֤��</button>
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
	//����Ĭ�ϳ���
	setSelectOption("districtid","8a21b3ab4d23c0a7014d2c5f4910001a","��");	//	������
});

//����
function setSelectOption(_cascadeid,_selidvalue,_emptyTxt){
    var bcReq;
	//if (_cascadeid=="gradecode"){//��ѯ�������ѧУ
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
		$("#"+_cascadeid+" option").remove();//���ԭ��Ϣ
		$("#"+_cascadeid).append("<option value=''>"+_emptyTxt+"</option>");
		for(var i=0;i<data.Data.length;i++){
		    var _rec = data.Data[i];
			$("#"+_cascadeid).append("<option value='"+_rec.option_id+"'>"+_rec.option_name+"</option>");
		}
    });
	bcReq.postData();
}

//ȥ�����ַ�
function stripscript(s){
	var pattern = new RegExp("[`~!?@#$^&*()|{}';:',\\[\\]<>/~���������磥@#���ޣ���%������&*��������|{}��������������'����]");
	var rs = ""; 
	for (var i = 0; i < s.length; i++) { 
		rs = rs+s.substr(i, 1).replace(pattern, ''); 
	}
	return rs;
}
//ʧȥ����ʱ��֤�˺�
function vode_code()  {
	var sMobile = $("#usercode").val();
	/*
    if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(sMobile)) || sMobile.length != 11){
    	top.toastr.clear();
		top.toastr.error("��������ȷ�ֻ��ţ�");
		$("#usercode").focus();
        return false; 
    }*/
    if(!isCardNo(sMobile)){
    	top.toastr.clear();
		top.toastr.error("��������ȷ���֤���룡");
		$("#usercode").focus();
        return false; 
    }
    $("#idnumber").val(sMobile);
    return true;
}

function isCardNo(card)  
{  
   // ���֤����Ϊ15λ����18λ��15λʱȫΪ���֣�18λǰ17λΪ���֣����һλ��У��λ������Ϊ���ֻ��ַ�X  
   var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
   if(reg.test(card) === false)  
   {  
       alert("���֤���벻�Ϸ�");  
       return  false;  
   }  
   return true;
}

/*ʧȥ����ʱ��֤�����ʽ
function vode_email() {
	var code = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
	if(!code.test($("#iptemail").val())  && $("#iptemail").val() != ''){
		top.toastr.clear();
		top.toastr.error("�����ʽ����ȷ��");
		$("#iptemail").focus();
		return false;
	}	
	return true;
}*/

//ʧȥ����ʱ��֤�û���
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
				top.toastr.error("������������Ϣ��");
				$(controls[i]).focus();
				return false;
      		}
    	}*/
  	}
	if($("#pwd").val().length<6 || $("#pwd").val().length>12){
		top.toastr.clear();
		top.toastr.error("���볤�ȱ�����6~12λ֮�䣡");	
		$("#pwd").focus();		
		return false;
	}
	if ($("#repwd").val() != $("#pwd").val()) {
		top.toastr.clear();
		top.toastr.error("�����������벻ͬ��");
		$("#repwd").focus();
		return false;
	}
	if($("#username").val().length>20) {
		top.toastr.clear();
		top.toastr.error("�û�����������");
		$("#username").focus();
		return false;
	}
	if (!vode_code()) {
	  return false;
    }
	$("#userpwd").val(strEnc($("#pwd").val()));
	return true;
}

//��ȡ��֤��
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
		top.toastr.error("��ȡ��֤��ʧ��!"+data.error_description);
	});
	bcReq.postData();
}

//ע��
function regStudent() {
	if (checkForm()) {
		var bcReq = new BcRequest('headmaster','headMasterbase','registerMaster');
	    bcReq.setForm("form1");
	    bcReq.setSuccFn(function(data,status){
	         top.toastr.success("ע��ɹ�!");
	         top.$.fancybox.close();
		     top.login();
	    });
	    bcReq.setFailFn(function(data,status){
		top.toastr.error("ע��ʧ��!"+data.error_description);
		});
	    bcReq.postData();
	}
}
</script>
</html>