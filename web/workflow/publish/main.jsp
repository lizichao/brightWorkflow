<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.brightcom.jraf.util.*"%> 
<%@ page import="cn.brightcom.jraf.web.*"%>
<%@ page import="cn.brightcom.system.pcmc.util.MenuUtil"%>
<%@ page import="cn.brightcom.system.pcmc.pm.PmInformations" %>
<%@ page import="cn.brightcom.jraf.conf.BrightComConfig"%>
<%@ page import="org.jdom.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.net.URLEncoder"%>
<%@ include file="/mooc/public/islogin.jsp"%>
<%@ include file="/mooc/public/session.jsp"%>
<%
   String username =(String)session.getAttribute("username");
   String deptname =(String)session.getAttribute("deptname");
   String portrait =(String)session.getAttribute("portrait"); 
   String usertype =(String)session.getAttribute("usertype");
   String mobile =(String)session.getAttribute("mobile");
   if(StringUtil.isEmpty(portrait)){
  	   portrait = "/images/yuexue2/photo.png";
   }
	String subjectid = (String)request.getParameter("subjectid");
	subjectid = subjectid==null?"":subjectid;
	String gradecode = request.getParameter("gradecode");
	gradecode = gradecode==null?"":gradecode;
	String paperid = request.getParameter("paperid");
	paperid = paperid==null?"":paperid;
	String foldercode = request.getParameter("foldercode");
	foldercode = foldercode==null?"":foldercode;
	if(StringUtil.isEmpty(subjectid)){
		subjectid = subjectid_session==null?"":subjectid_session;
		gradecode = gradecode_session==null?"":gradecode_session;
		foldercode = foldercode_session==null?"":foldercode_session;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>沃课堂</title>
<%@ include file="/mooc/public/header.jsp"%>
<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
<link rel="stylesheet" href="/images/yuexue2/message/Font.css" />

<script type="text/javascript" src="/js/jquery/image-file-visible.js"></script>
<link rel="stylesheet" type="text/css" href="/images/mooc/question/base.css" /> 
<link rel="stylesheet" type="text/css" href="/images/mooc/question/common.css" /> 
<link rel="stylesheet" type="text/css" href="/images/mooc/question/teacher.css" /> 
<link href="/images/mooc/question/jquery-ui-1.css" rel="stylesheet" type="text/css" />
<style type="text/css">.indexsprite, .m-slide .lbtn, .m-slide .rbtn, .m-recommend .change .icon, .m-teacherSay .teach .icon, .m-platAdvantages .ats .item .icon, .m-userSay .users .icon {background: url(http://mc.stu.126.net/res/images/pages/index/icons.png?3b6719d215ad166ee19c571b161dc993) 9999px 9999px no-repeat;}body {background-color: #fff;}.u-title {padding: 90px 0 30px 0;border-bottom: 1px solid #d4d4d4;}.u-title .txt {margin: 0 auto;background: url(http://mc.stu.126.net/res/images/pages/index/text.png?452afd0c96ade1324567fc81bef94493) no-repeat 4449px 4449px;height: 36px;position: relative;}.u-title .txt .divider {position: absolute;width: 116px;height: 0;top: 64px;left: 14px;border: 2px solid #8bb721;}.u-indexDes {text-align: center;font-size: 22px;padding: 35px 0 40px 0;}.u-link {font-size: 22px;margin-left: 10px;}.m-slide {background-color: #000;height: 460px;width: 100%;}.m-slide .u-slide {width: 100%;height: 100%;}.m-slide .lbtn, .m-slide .rbtn {position: absolute;top: 200px;height: 100px;width: 56px;background-color: #000;filter: alpha(opacity=20);opacity: 0.2;}.m-slide .lbtn {left: 0px;background-position: -300px -420px;}.m-slide .rbtn {right: 0px;background-position: -298px -333px;}.m-slide .lbtn:hover {background-position: -364px -420px;background-color: #000;filter: alpha(opacity=40);opacity: 0.4;}.m-slide .rbtn:hover {background-position: -358px -333px;background-color: #000;filter: alpha(opacity=40);opacity: 0.4;}.m-slide .array {width: 100%;height: 30px;left: 0;background: url(http://mc.stu.126.net/res/images/pages/index/bot.png?1dcb151f502582d1f90acc5b26ed94ea) center bottom no-repeat;}.m-slide .slide {display: block;height: 100%;width: 960px;margin: 0 auto;position: relative;}.m-slide .slide img {width: 1024px;height: 100%;margin-left: -32px;}.m-slide .slide .text {left: 30px;top: 135px;font-family: 'Microsoft YAHEI';z-index: 996;}.m-slide .slide .text .title {margin-bottom: 15px;font-size: 32px;}.m-slide .slide .text .des {font-size: 16px;font-weight: normal;}.m-slide .pager {width: 500px;height: 5px;line-height: 5px;margin: 0 auto;position: relative;z-index: 10;text-align: center;*padding-top: 5px;}.m-slide .pager li {display: inline-block;*display: inline;zoom: 1;width: 18px;height: 2px;background: #aaa59f;margin: 0 0 0 10px;*margin: 0 10px 0 0;text-indent: 20px;overflow: hidden;cursor: pointer;}.m-slide .pager li.js-selected {cursor: default;background: #8bb721;}.m-slide .side {width: 260px;height: 50px;padding: 0 20px;}.m-slide .side a {float: left;}.m-platDes {border-bottom: 1px solid #e5e5e5;padding-top: 100px;}.m-platDes .txt {background-position: -0px -0px;width: 618px;}.m-platDes .u-link {padding-top: 4px;}.m-platDes .box {width: 720px;margin: 0 auto;}.m-school .link {font-size: 14px;margin: 25px 0;}.m-school .box .schoolItm {margin-right: 20px;}.m-school .box .schoolItm.last {margin-right: 0px;}.m-school .box .schoolItm img {width: 225px;height: 83px;}.m-recommend {margin-top: 60px;height: 180px;overflow: hidden;}.m-recommend .change {cursor: pointer;position: absolute;top: 0;width: 60px;height: 100%;}.m-recommend .change.disabled {display: none;}.m-recommend .change .icon {width: 50px;height: 80px;position: absolute;top: 60px;left: 5px;}.m-recommend .change.left {left: 0px;}.m-recommend .change.left .icon {background-position: -364px -78px;}.m-recommend .change.left:hover {background-color: rgba(46, 50, 62, 0);background-repeat: repeat-x;background-image: -khtml-gradient(linear, left top, right top, from(rgba(46, 50, 62, 0.4)), to(rgba(46, 50, 62, 0)));background-image: -moz-linear-gradient(left, rgba(46, 50, 62, 0.4), rgba(46, 50, 62, 0));background-image: -ms-linear-gradient(left, rgba(46, 50, 62, 0.4), rgba(46, 50, 62, 0));background-image: -webkit-gradient(linear, left top, right top, color-stop(0%, rgba(46, 50, 62, 0.4)), color-stop(100%, rgba(46, 50, 62, 0)));background-image: -webkit-linear-gradient(left, rgba(46, 50, 62, 0.4), rgba(46, 50, 62, 0));background-image: -o-linear-gradient(left, rgba(46, 50, 62, 0.4), rgba(46, 50, 62, 0));background-image: linear-gradient(left, rgba(46, 50, 62, 0.4), rgba(46, 50, 62, 0));filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff$startColor',endColorstr='#ff$endColor',GradientType=1);filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);}.m-recommend .change.left:hover .icon {background-position: -364px -154px;}.m-recommend .change.right {right: 0px;}.m-recommend .change.right .icon {background-position: -275px 12px;}.m-recommend .change.right:hover {background-color: rgba(46, 50, 62, 0);background-repeat: repeat-x;background-image: -khtml-gradient(linear, left top, right top, from(rgba(46, 50, 62, 0.4)), to(rgba(46, 50, 62, 0)));background-image: -moz-linear-gradient(right, rgba(46, 50, 62, 0.4), rgba(46, 50, 62, 0));background-image: -ms-linear-gradient(right, rgba(46, 50, 62, 0.4), rgba(46, 50, 62, 0));background-image: -webkit-gradient(linear, left top, right top, color-stop(0%, rgba(46, 50, 62, 0.4)), color-stop(100%, rgba(46, 50, 62, 0)));background-image: -webkit-linear-gradient(right, rgba(46, 50, 62, 0.4), rgba(46, 50, 62, 0));background-image: -o-linear-gradient(right, rgba(46, 50, 62, 0.4), rgba(46, 50, 62, 0));background-image: linear-gradient(right, rgba(46, 50, 62, 0.4), rgba(46, 50, 62, 0));filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff$startColor',endColorstr='#ff$endColor',GradientType=1);filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);}.m-recommend .change.right:hover .icon {background-position: -360px 12px;}.m-recommend .content {width: 3000px;}.m-recommend .content .courseItm {margin-right: 1px;}.m-recommend .content .courseItm .name {position: absolute;left: 0;bottom: 0;width: 100%;height: 50px;background-color: #000;color: #fff;font-size: 18px;text-align: center;line-height: 50px;filter: alpha(opacity=60);opacity: 0.6;}.m-hot {background-color: #f5f5f5;}.m-hot .u-title .txt {background-position: -0px -73px;width: 148px;}.m-hot .cardBox {margin-left: -20px;padding-bottom: 30px;}.m-hot .cardBox .courseCard {margin: 0 0 40px 20px;}.m-hot .cardBox .courseCard .img {width: 306px;height: 172px;}.m-hot .cardBox .courseCard .name {font-size: 18px;margin: 20px 0 14px 0;width: 300px;}.m-hot .cardBox .courseCard .school {font-size: 14px;width: 300px;}.m-teacher {background-color: #8cb822;}.m-teacher .u-title {border-bottom: 1px solid #70931b;}.m-teacher .u-title .txt {background-position: -0px -139px;width: 148px;}.m-teacher .u-title .txt .divider {border: 2px solid #000;}.m-teacher .tas {padding-bottom: 90px;}.m-teacher .tas .one {padding: 0 20px;display: block;width: 200px;}.m-teacher .tas .one img {display: block;margin: 0 auto;border: 5px solid #8cb822;border-radius: 65px;}.m-teacher .tas .one img:hover {border-color: #549552;}.m-teacher .tas .one p, .m-teacher .tas .one a {display: block;text-align: center;width: 100%;line-height: 1.1;}.m-teacher .tas .one .name {padding: 14px 0 10px 0;font-size: 20px;}.m-teacher .tas .one .title {font-size: 14px;padding: 0 0 10px 0;}.m-teacher .tas .one .course {font-size: 18px;color: #fff;}.m-teacher .tas .one .course:hover {text-decoration: underline;}.m-teacherSay .teach {width: 100%;padding-top: 50px;}.m-teacherSay .teach .icon {width: 50px;height: 50px;}.m-teacherSay .teach .icon-1 {background-position: -45px 2px;margin-right: -50px;}.m-teacherSay .teach .icon-2 {background-position: -110px 2px;margin-left: -50px;}.m-teacherSay .teach .say {width: 100%;font-size: 18px;line-height: 1.6;color: #666;}.m-teacherSay .teach-1 {border-bottom: 1px solid #eee;}.m-teacherSay .teach-1 .say .cnt {margin-top: 50px;margin-left: 50px;margin-right: 325px;}.m-teacherSay .teach-1 img {margin-left: -325px;}.m-teacherSay .teach-2 .say .cnt {margin-top: 50px;margin-left: 325px;margin-right: 50px;}.m-teacherSay .teach-2 img {margin-right: -325px;}.m-platAdvantages {background-color: #f5f5f5;}.m-platAdvantages .u-title .txt {background-position: -0px -209px;width: 719px;}.m-platAdvantages .u-title .txt .divider {left: 300px;}.m-platAdvantages .ats {padding: 60px 0;}.m-platAdvantages .ats .item {width: 30%;margin-left: 3.3%;}.m-platAdvantages .ats .item .icon {width: 150px;height: 150px;margin: 0 auto;}.m-platAdvantages .ats .item .icon-1 {background-position: 4px -582px;}.m-platAdvantages .ats .item .icon-2 {background-position: -147px -582px;}.m-platAdvantages .ats .item .icon-3 {background-position: 4px -731px;}.m-platAdvantages .ats .item .sub {text-align: center;padding: 40px 0 20px 0;font-size: 22px;color: #666;}.m-platAdvantages .ats .item .intro {line-height: 1.6;font-size: 14px;color: #666;}.m-userSay {padding-top: 80px;}.m-userSay .u-usersay .say {padding: 50px;border: 1px solid #eee;border-bottom: 4px solid #8cb822;font-size: 18px;color: #666;height: 100px;}.m-userSay .u-usersay .user {right: 60px;bottom: 50px;}.m-userSay .users {margin: 15px auto;width: 720px;}.m-userSay .users .item {width: 20%;}.m-userSay .users .item img {display: block;margin: 0 auto;}.m-userSay .users .icon {background-position: 0 3px;height: 15px;width: 15px;top: -18px;}.m-checkAllCourse {display: block;background-color: #8cb822;width: 720px;height: 80px;text-align: center;color: #fff;font-size: 22px;line-height: 80px;margin-top: 80px;margin-bottom: 35px;-webkit-border-radius: 6px;-moz-border-radius: 6px;border-radius: 6px;}.m-checkAllCourse:hover {background-color: #aad215;}@media screen and (min-width: 1210px) {.m-school .box .schoolItm {margin-right: 81px;}.m-school .box .schoolItm img {width: 240px;height: 89px;}.m-teacher .tas .one {width: 261px;}.m-hot .cardBox {margin-left: -21px;}.m-hot .cardBox .courseCard {margin-left: 21px;}.m-hot .cardBox .courseCard .img {width: 387px;height: 218px;}.m-hot .cardBox .courseCard .name {width: 380px;}.m-hot .cardBox .courseCard .school {width: 380px;}.m-recommend {height: 226px;}.m-recommend .content .courseItm img {width: 401px;height: 226px;}}</style>
<style type="text/css">.loading-mask {display: block;width: 100%;height: 100%;opacity: 0.4;filter: alpha(opacity=40);background-image:url(data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==);position:fixed;_position:absolute;top:0;bottom:0;left:0;right:0;}</style>
<style type="text/css">.auto-1431393385516 {padding:5px 0 5px 15px;font-size:12px;line-height:16px;}.auto-1431393385516 .warningbox{color:#cc3333;}.auto-1431393385516 .warningbox .warning{float:left;line-height:22px;}.auto-1431393385516 .warningbox .wrong{float:left;background: url(http://mc.stu.126.net/res/images/ui/ui_sprite.png?eca43f780d8dd17d8d22c71cf615902a) no-repeat -40px -156px;height:20px;width:20px;}.auto-1431393385516 .warningbox .warn{float:left;background: url(http://mc.stu.126.net/res/images/ui/ui_sprite.png?eca43f780d8dd17d8d22c71cf615902a) no-repeat 0 -156px;height:20px;width:20px;}.auto-1431393385516 .warningbox .right{float:left;background: url(http://mc.stu.126.net/res/images/ui/ui_sprite.png?eca43f780d8dd17d8d22c71cf615902a) no-repeat -20px -156px;height:20px;width:20px;}.auto-1431393385516 .warningbox .normal{float:left;background: url(http://mc.stu.126.net/res/images/ui/ui_sprite.png?eca43f780d8dd17d8d22c71cf615902a) no-repeat -62px -156px;height:20px;width:20px;}.auto-1431393385516 .warningbox .righttxt{color:#666;}.auto-1431393385516 .loadingbox{color:#999;}.auto-1431393385516 .loadingbox .loading{float:left;line-height:22px;}.auto-1431393385516 .loadingbox .icon{float:left;background: url(http://mc.stu.126.net/res/images/ui/loading_circle.gif?00ef871b291bc03a497d608a5bd8ec99) no-repeat 0 0;height:16px;width:20px;}.auto-1431393385517.u-tipinput-error{border-color:red;}.auto-1431393385517 {position:relative;z-index:102;}.auto-1431393385517 .u-cmtedtip{position:absolute;}.auto-1431393385517 .u-cmtedtip.right{top:-23px;right:0;}.auto-1431393385517 .u-cmtedtip.left{bottom:-25px;left:0;}.auto-1431393385517 .s-fc9{color:red;}.auto-1431393385517 .tip{position:absolute;left:100%;top:0;padding-left:14px;width:200px;}.auto-1431393385517 .tip .ct{background:#ffffcc;font-size:12px;line-height:16px;color:#333;padding:5px 6px;border:1px solid #c2b06b;}.auto-1431393385518{position:fixed;_position:absolute;z-index:100;top:0;bottom:0;left:0;right:0;width:100%;height:100%;background-image:url(data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==);}.auto-1431393385519{position:absolute;z-index:1000;border:1px solid #aaa;background:#fff;}.auto-1431393385519 .zbar{line-height:30px;background:#8098E7;border-bottom:1px solid #aaa;}.auto-1431393385519 .zcnt{padding:10px 5px;}.auto-1431393385519 .zttl{margin-right:20px;text-align:left;}.auto-1431393385519 .zcls{position:absolute;top:5px;right:0;width:20px;height:20px;line-height:20px;cursor:pointer;}.auto-1431393385520 {padding:0; width:578px; height:366px;}.auto-1431393385521 .date{margin-right: 5px;}.auto-1431393385522{padding:20px 20px 32px 20px;}.auto-1431393385522 .u-title{color:#ccc;height:35px;line-height:35px;margin:0 8px 0 10px;}.auto-1431393385522 .u-row{margin-bottom:10px;}.auto-1431393385522 .u-error{color:red;padding-top:10px;}.auto-1431393385522 .u-edit{position: relative;z-index: 101;background: #FAFAFA;border: 1px solid #DFDFDF;-webkit-box-shadow: inset 1px 1px 2px #DFDFDF;-moz-box-shadow: inset 1px 1px 2px #dfdfdf;box-shadow: inset 1px 1px 2px #DFDFDF;}.auto-1431393385522 .ipt{display:block;font-size: 14px;position: relative;z-index: 101;line-height:35px;height:35px;width: 290px;resize: none;background: transparent;border: none;color: #444;overflow:hidden;}.auto-1431393385527 {width: 400px;height: 270px;padding:20px;}.auto-1431393385527 .imgArea{width: 450px;height: 58px;}.auto-1431393385527 .imgArea img{width: 135px;height: 45px;border:1px solid #ddd;margin:0 12px 0 0;}.auto-1431393385527 .imgArea .replacePicBtn{margin:8px 0 0;}.auto-1431393385527 .row{margin:25px 0 0;}.auto-1431393385527 .row .lb{display:block;width:68px;height:30px;line-height:30px;margin:0 10px 0 0;float:left;}.auto-1431393385527 .row .ipt{width:302px;height:35px;}.auto-1431393385527 .btns{padding:0 11px 0 0;}.auto-1431393385527 .cancelBtn{margin:6px 8px 0 0;}.auto-1431393385528 {width: 610px;height: 200px;text-align:center;font-size:12px;color:#666;}.auto-1431393385528 .lector,.auto-1431393385528 .addLector{width: 156px;height:96px;margin:0 0 0 47px;}.auto-1431393385528 .lector .delBtn{display:none;top:2px;right:2px;z-index:2;cursor:pointer;}.auto-1431393385528 .lector .mask{display:none;width:153px;height: 96px;top:0;left:0;border:1px solid #ddd;}.auto-1431393385528 .lector .mask .editBtn{display:block;width:43px;height: 20px;margin:30px auto;font-size:17px;font-weight:bold;}.auto-1431393385528 .lector:hover .mask{display:block;background-color:rgba(0,0,0,0.6);}.auto-1431393385528 .lector img{width:153px;height:53px;border-bottom:1px solid #ddd;margin:0 0 5px;}.auto-1431393385528 .lector:hover .delBtn{display:inline-block;font-size:14px;color:#fff;}.auto-1431393385528 .addLector .sinfo{margin:10px 0 0 ;}.auto-1431393385528.readonly .lector:hover .mask{display:none;}.auto-1431393385528.readonly .lector:hover .delBtn{display:none;}.auto-1431393385529 {width: 720px;height: 1099px;padding:25px;border:15px solid #a8abaf;font-family:"微软雅黑";}.auto-1431393385529 .midCtn{width: 700px;height: 1081px; padding:8px;border:3px solid #404e5f;}.auto-1431393385529 .innerCtn{background: url("http://img0.ph.126.net/y7FLm6W0kxgJCWOPVbGHiQ==/6619438231212290010.jpg") right 0 no-repeat;width: 690px;height: 1071px;line-height: normal;border:5px solid #c7a682;}.auto-1431393385529 .uniPic{width:155px;height:155px;border-radius:115px;top:265px;left:31px;}.auto-1431393385529 .mainCtn{width:509px;height:325px;padding-top:294px;margin:0 0 0 181px;text-align:center;}.auto-1431393385529 .mainCtn h3{font-size:43px;color:#333;padding:0 8px 0 0;margin: 15px 0 35px;}.auto-1431393385529 .mainCtn .txt{font-size:24px;color:#333;}.auto-1431393385529 .mainCtn .courseSN{color:#333;font-size: 21px;margin:0 0 50px;}.auto-1431393385529 .mainCtn .cName{font-size:36px;color:#333;padding:0 5px;}.auto-1431393385529 .lectors{width:650px;height:242px;margin:90px auto 0;line-height: 21px;}.auto-1431393385529 .foot{width: 240px;height:160px;margin:0 auto;color:#333;}.auto-1431393385529 .foot img{width:230px;height:80px;border-bottom:1px solid #ddd;margin:0 0 5px;}.auto-1431393385529 .foot .uni{height:18px;overflow:hidden;}.auto-1431393385529 .foot .professor{height:18px;overflow:hidden;}.auto-1431393385529 .foot .editorBox{width:490px;margin:19px 0 0 -243px;}.auto-1431393385529 .foot .editorBox{width:490px;margin:19px 0 0 -243px;}.auto-1431393385529 .foot .uploadTxBtn{display: block;width: 60px;height: 20px;right:242px;bottom:45px;color:#61A500;}.auto-1431393385529 .foot .editorBox .u-tipinput{float:left;margin:0 15px 0 0;}.auto-1431393385529 .bottom{width: 670px;height:100px;margin:0 auto;}.auto-1431393385529 .bottom .qrImg{width: 73px;height:90px;text-align:center;background-color:#e9e8e8;padding:6px;margin:0 15px 0 0;}.auto-1431393385529 .bottom .qrImg p{color:#333;font-size:14px;height:23px;line-height:23px;}.auto-1431393385529 .bottom .courseInfo{width:565px;bottom:20px;left:105px;}.auto-1431393385529 .bottom .courseInfo .des{word-wrap: break-word;color:#808183;}.auto-1431393385529 .bottom .certNO{bottom:0;left:105px;color:#333;font-size:14px;}.auto-1431393385530{font-size:12px;line-height:160%;}.auto-1431393385530 a{margin:0 2px;padding:2px 8px;color:#333;border:1px solid #aaa;text-decoration:none;}.auto-1431393385530 .js-disabled{cursor:default;}.auto-1431393385530 .js-selected{cursor:default;background:#bbb;}.auto-1431393385531 .item-highlight{background:#E0F2E2;}.auto-1431393385531 .m-data-lists{position:relative;z-index:100;}.auto-1431393385531 .u-pager{position:relative;z-index:99;}.auto-1431393385532{position:absolute;background:#fff;}.auto-1431393385533{width:210px;border:1px solid #aaa;font-size:14px;text-align:center;}.auto-1431393385533 .zact{line-height:30px;overflow:hidden;zoom:1;}.auto-1431393385533 .zact .zfl{float:left;}.auto-1431393385533 .zact .zfr{float:right;}.auto-1431393385533 .zact .zbtn{padding:0 5px;cursor:pointer;}.auto-1431393385533 .zact .ztxt{margin-left:10px;}.auto-1431393385533 .zday{table-layout:fixed;border-collapse:collapse;width:100%;}.auto-1431393385533 .zday th{font-weight:normal;}.auto-1431393385533 .zday a{display:block;height:22px;line-height:22px;color:#333;text-decoration:none;}.auto-1431393385533 .zday a:hover{background:#eee;}.auto-1431393385533 .zday a.js-extended{color:#aaa;}.auto-1431393385533 .zday a.js-selected,.auto-1431393385533 .zday a.js-selected:hover{background:#DAE4E7;}.auto-1431393385533 .zday a.js-disabled,.auto-1431393385533 .zday a.js-disabled:hover{background:#fff;color:#eee;cursor:default;}.auto-1431393385534 .disable{color:#aaa;}.auto-1431393385534 .dateDay,.dateMin{border:1px solid #ccc;width:70px;height:18px;padding:4px 3px;margin:2px 5px 0 0;cursor:pointer;background-color:#fff;text-align:center;}.auto-1431393385534 .dateMin{width:50px;margin-right:10px;background-position:41px -197px;}.auto-1431393385534 .dateDayLayer{top:26px;left:1px;z-index:10001;}.auto-1431393385534 .dateMinLayer{z-index:10001;top:26px;left:83px;width:57px;height:165px;background-color:#FFF;border:1px solid #ddd;box-shadow:0px 0px 4px 0px #DDD;overflow-y:scroll;font-size:13px;}.auto-1431393385534 .dateMinLayer span{display:block;height:22px;line-height:22px;color:#999;padding:0 0 0 3px;cursor:pointer;}.auto-1431393385534 .dateMinLayer span:hover{background-color:#ececee;}.auto-1431393385534 .dateDayLayer .zcard{box-shadow:0px 0px 4px 0px #DDD;border:1px solid #ddd;}.auto-1431393385534 .dateDayLayer .zact{font-size:12px;color:#333;font-family:sans-serif;}.auto-1431393385534 .dateDayLayer .zbtn{color:#747474;}.auto-1431393385534 .dateDayLayer .zday .js-extended{color:#ccc;}.auto-1431393385534 .dateDayLayer .zday th{font-size: 12px;font-family: sans-serif;text-align:center;color:#333;}.auto-1431393385534 .dateDayLayer .zday td{font-size: 12px;font-family: sans-serif;text-align:center;color:#333;}.auto-1431393385534 .dateDayLayer .zday a{font-size:10px;font-family:sans-serif;color:#666;}.auto-1431393385534 .dateDayLayer .zday a.js-selected{background-color:#83b600;color:#fff;}.auto-1431393385534 .dateDayLayer .zday a.js-disabled{color:#eee;}.auto-1431393385535 .status{color: #ec681e;}.auto-1431393385536 {background: url("/res/images/pages/cert/certBg.jpg");width: 842px;height: 620px;line-height: normal;font-family:"微软雅黑";}.auto-1431393385536 .main{width:550px;height:130px;padding:180px 0 0;margin:0 auto;}.auto-1431393385536 .main h3{font-size:22px;color:#333;padding:0 8px 0 0;height:30px;}.auto-1431393385536 .main .txt{font-size:16px;}.auto-1431393385536 .main .cName{font-size:24px;color:#7da914;height:45px;line-height:45px;padding:0 5px;}.auto-1431393385536 .course{width:753px;height:72px;margin:20px auto 13px;}.auto-1431393385536 .course img{width:136px;height:70px;margin:0 10px 0 0;}.auto-1431393385536 .course .info{width:601px;height:72px;color:#333;}.auto-1431393385536 .course .info .cName{padding:0 5px 0 0;}.auto-1431393385536 .course .info .des{width:606px;height:51px;margin:3px 0 0;color:#666;font-family:"serif";line-height: 17px;word-wrap: break-word;}.auto-1431393385536 .lectors{width:650px;height:150px;margin:0 auto;line-height: 21px;}.auto-1431393385536 .bottom{width: 842px;height:30px;text-align:center;}.auto-1431393385537 {padding:0px;max-width:800px;min-width:400px;}.auto-1431393385537 .header{border-bottom:1px solid #eee;padding:5px 20px 10px 20px;}.auto-1431393385537 .header .title{font-size:20px;margin-right:10px;}.auto-1431393385537 .header .type{font-size:14px;margin-right:5px;}.auto-1431393385537 .header .score{font-size:14px;}.auto-1431393385537 .footer {padding:10px 20px;font-size:14px;}.auto-1431393385537 .footer .item{margin-bottom:10px;margin-top:10px;}.auto-1431393385537 .footer .item .left{width:18%;}.auto-1431393385537 .footer .item .rightitem{width:81%;word-break: break-word;word-wrap: break-word;}.auto-1431393385537 .footer .item .right{color:#619d26}.auto-1431393385537 .footer .item .wrong{color:#bd4b26}.auto-1431393385537 .opts{padding: 20px 10px; border-top:1px solid #eee;}.auto-1431393385537 .save{margin-right:20px;}.auto-1431393385537 .u-btn{padding:0 4px;height:30px;line-height:30px;}.auto-1431393385538-parent{position:absolute;left:-1px;border:1px solid #d5d5d5;width:100%;background:#FAFAFA;z-index:999;}.auto-1431393385538-parent .j-cnt{width:100%;color:#444;visibility:hidden;}.auto-1431393385538-parent .zitm-top{width:100%;text-indent:6px;line-height:32px;cursor:pointer;}.auto-1431393385538-parent .zitm,.auto-1431393385538-parent .zitm p{width:100%;text-indent:6px;line-height:32px;cursor:pointer;}.auto-1431393385538-parent .zitm a{display:block;overflow:hidden;text-overflow:ellipsis;font-size:12px}.auto-1431393385538-parent .zitm:hover,.auto-1431393385538-parent .zitm:hover p{color:#16914E;background-color:#ECECEE;}.auto-1431393385538-parent .js-selected,.auto-1431393385538-parent .js-selected p{color:#16914E;background-color:#ECECEE;}.auto-1431393385539 .topinfo{margin-bottom:5px;}.auto-1431393385539 .tipinfo{margin-top:5px;line-height:20px;width:305px;}.auto-1431393385539 .tit{height:20px;line-height:20px;padding-bottom:5px;font-size:14px;}.auto-1431393385539 .dateDay,.dateMin {text-align: center;}.auto-1431393385539 .timeSet{top:35px;left:0;z-index:3000;}.auto-1431393385539 .timesetType{margin:5px 15px;}.auto-1431393385539 .timesetType input{vertical-align:middle;margin-right:5px;}.auto-1431393385539 .timesetType label{vertical-align:middle;}.auto-1431393385539 .timesetting .timedetail{margin: 5px 0 10px 36px;}.auto-1431393385539 .timesetting .timedetail .desc{height:30px;line-height:30px;}.auto-1431393385539 .timesetting .timedetail .dateMinLayer{left:143px;}.auto-1431393385539 .timesetting .timedetail .dateDayLayer{left:60px;}.auto-1431393385539 .timeSet .zday a{font-size: 10px;font-family: sans-serif;color: #666;}.auto-1431393385539 .timeSet .zday .js-extended{color:#ccc;}.auto-1431393385539 .timeSet .zday .js-selected{background-color:#83b600;color:#fff;}.auto-1431393385539 .timeSet .zday th{font-size: 12px;font-family: sans-serif;text-align: center;}.auto-1431393385539 .timeSet .zact span{font-size: 12px;font-family: sans-serif;}.auto-1431393385539 .m-feedbackinfo{padding: 5px 0 5px 10px;}.auto-1431393385539 .u-sugInput .u-btn{margin-left:10px;padding:4px 8px;line-height:22px;}.auto-1431393385539 .fb{height:22px;}.auto-1431393385539 .fb .m-feedbackinfo{padding:0px;}.auto-1431393385539 .schoollector{vertical-align:middle;margin-right:3px;}.auto-1431393385539 .lectorfrom{height:33px;line-height:33px;margin-left:10px;}.auto-1431393385539 .u-btn{padding:0 4px;height:30px;line-height:30px;}.auto-1431393385540 {padding:20px;}.auto-1431393385540 .save{margin-right:20px;}.auto-1431393385540 .fb{height:22px;}.auto-1431393385540 .fb .m-feedbackinfo{padding:0px;}.auto-1431393385540 .u-btn{padding:0 4px;height:30px;line-height:30px;}.auto-1431393385541 {}.auto-1431393385541 .sicon{font-size:16px;color:#ddd;position:absolute;top:3px;right:10px;cursor:pointer;}.auto-1431393385542 {margin-top:20px;}.auto-1431393385542 .ctinfo{height:65px;line-height:30px;}.auto-1431393385542 .title{font-size:14px;}.auto-1431393385542 .cnt{width:650px}.auto-1431393385542 .cnt .zarea{height:450px;}.auto-1431393385542 .opts{margin-bottom:10px;padding:0 0 0 50px;}.auto-1431393385542 .messc{width: 20px;height: 20px;vertical-align: middle;}.auto-1431393385542 .mess{vertical-align: middle;}.auto-1431393385542 .publish{height:28px;}.auto-1431393385542 .mail{font-size:12px;margin:6px 0 10px;}.auto-1431393385542 .draft,.publish{margin-right:10px;}.auto-1431393385542 .draft{height:28px;}.auto-1431393385542 .tipinfo{width:660px;}.auto-1431393385542 .opts .u-btn{min-width:60px;height:30px;line-height:30px;padding:0 5px;font-size:12px;}.auto-1431393385543 {line-height:22px;}.auto-1431393385543.loading{background: url(http://mc.stu.126.net/res/images/ui/loading_circle.gif?00ef871b291bc03a497d608a5bd8ec99) no-repeat 10px 6px; text-indent: 20px;}.auto-1431393385544.u-appbanner{ display: none; border-bottom:1px solid #aaa; width:100%;}.auto-1431393385544.u-appbanner .wrap{ padding:45px 40px; height:95px; min-width:880px; background:#fff;}.auto-1431393385544.u-appbanner .wrap .logo{ width:465px; height:85px;}.auto-1431393385544.u-appbanner .wrap .lookbtn{ width:198px; height:85px; margin-right:25px;}.auto-1431393385544.u-appbanner .wrap .dlbtn{ width:198px; height:85px;}.auto-1431393385544.u-appbanner .wrap .close{ width:35px; height:35px; margin:27px 0 0 100px;}.auto-1431393385544.u-appbanner .tip{ display:none; min-width:880px; background:#07853a; padding:45px 40px; height:95px;}.auto-1431393385544.u-appbanner .tip p{ width:740px; color:#fff; line-height:1.4; font-size:2.6em;; font-family:Hiragino Sans GB;}.auto-1431393385544.u-appbanner .tip .jt{ width:106px; height:46px; margin-top:20px;}.auto-1431393385544.u-appbanner.pc{ display:none;}.auto-1431393385544.u-appbanner.iphone{ display:block;}.auto-1431393385544.u-appbanner.ipad{ display:block;}.auto-1431393385544.u-appbanner.ipad .wrap{ padding:20px; height:66px;}.auto-1431393385544.u-appbanner.ipad .wrap .logo{ width:229px; height:66px;}.auto-1431393385544.u-appbanner.ipad .wrap .lookbtn{ width:139px; height:60px; margin-right:25px;}.auto-1431393385544.u-appbanner.ipad .wrap .dlbtn{ width:139px; height:60px;}.auto-1431393385544.u-appbanner.ipad .wrap .close{ width:30px; height:30px; margin:16px 0 0 35px;}.auto-1431393385544.u-appbanner.ipad .tip{ padding:20px; height:66px;}.auto-1431393385544.u-appbanner.ipad .tip p{ line-height:1.3;}.auto-1431393385544.u-appbanner.ipad .tip .jt{ margin-top:10px;}.auto-1431393385544.u-appbanner.android{ display:block;}.auto-1431393385544.u-appbanner.android .wrap .lookbtn{ display:none;}.auto-1431393385545{ width:120px; top:6px; right:-120px; height:28px;}.auto-1431393385545 .publishbtn{ line-height:22px; margin-left:8px; padding:2px 9px; min-width:0px;}.auto-1431393385545 .statustxt{ line-height:28px; margin-left:8px;}.auto-1431393385546 .anonyInfo{color:#999;}.auto-1431393385546 .userInfo .type{margin:0 5px;padding:0 5px;display:inline-block;line-height:18px;color:#fff;text-align:center;}.auto-1431393385546 .userInfo .type.lector{background-color:#6CB4FB;}.auto-1431393385546 .userInfo .type.manager{background-color:#9BCE00;}.auto-1431393385546 .userInfo .type.assist{background-color:#6CB4FB;}.auto-1431393385547 div{cursor:pointer;width:15px;height:15px;background: url(http://mc.stu.126.net/res/images/ui/forum_icon.png?b12539c2400cc76ad30262bdf7e12cbd) no-repeat -4999px -4999px;}.auto-1431393385547 .num{color:#666;line-height:15px;max-width:45px;margin:0 3px;}.auto-1431393385547 .up{ background-position:2px -22px;}.auto-1431393385547 .up.hvr:hover{ background-position:-15px -22px;}.auto-1431393385547 .up.voted{ background-position:-33px -22px;}.auto-1431393385547 .down{ background-position:2px -44px;}.auto-1431393385547 .down.hvr:hover{ background-position:-15px -44px;}.auto-1431393385547 .down.voted{ background-position:-33px -44px;}.auto-1431393385548 .bd{color:#ddd;margin:0 5px;}.auto-1431393385548 .manBtn span{display:inline-block;vertical-align:middle;height:14px;width:14px;background:url(http://mc.stu.126.net/res/images/ui/forum_icon.png?b12539c2400cc76ad30262bdf7e12cbd) no-repeat -55px -45px;}.auto-1431393385548 .manBtn:hover span{background-position:-72px -45px;}.u-manList{position:absolute;width:110px;background-color:#fff;border:1px solid #DDD;border-bottom:none;}.u-manList li{line-height:30px;text-align:center;border-bottom:1px solid #DDD;cursor:pointer;}.u-manList li:hover a{color:#61A500;}.auto-1431393385549{background-color:#f8f8f8;padding:10px 20px;}.auto-1431393385549 .title{max-width:68%;word-break:break-word;}.auto-1431393385549 .content{padding-top:10px;}.auto-1431393385549 .infobar{padding:10px 0;border-bottom:1px solid #eee;}.auto-1431393385549 .infobar .time{padding:0 10px;}.auto-1431393385549 .optbar{padding-top:10px;}.auto-1431393385549 .optBox,.auto-1431393385549 .votebox{margin-top:5px;}.auto-1431393385549 .optBox .divider{color:#ddd;margin:0 5px;}.auto-1431393385549 .optbar a.u-btn-sm{line-height:18px;}.auto-1431393385549 .optbar a i{vertical-align:-3px;display:inline-block;width:20px;height:16px;background:url(http://mc.stu.126.net/res/images/ui/forum_icon.png?b12539c2400cc76ad30262bdf7e12cbd) no-repeat -4999px -4999px;}.auto-1431393385549 .optbar .replyBtn i{background-position: -54px 1px;}.auto-1431393385549 .followBtn{margin-right:10px;}.auto-1431393385549 .optbar .followBtn.unfollowed i{background-position: -78px 0;}.auto-1431393385549 .optbar .followBtn.unfollowed .second,.auto-1431393385549 .optbar .followBtn.unfollowed .secHvr{display:none;}.auto-1431393385549 .optbar .followBtn.followed i{background-position: -53px -24px;}.auto-1431393385549 .optbar .followBtn.followed .first,.auto-1431393385549 .optbar .followBtn.followed .secHvr{display:none;}.auto-1431393385549 .optbar .followBtn.followed:hover .second{display:none;}.auto-1431393385549 .optbar .followBtn.followed:hover .secHvr{display:inline-block;padding-left:8px;}.auto-1431393385550 {}.auto-1431393385550 .rich-opt{margin-top:10px;}.auto-1431393385550 .rich-opt label{cursor:pointer;color:#999;vertical-align:top;}.auto-1431393385550 .rich-opt label:hover{color:#61a500;}.auto-1431393385550 .rich-opt label input{margin-top:3px;}.auto-1431393385550 .rich-opt .cancelbtn{margin-right:10px;line-height:24px;}.auto-1431393385550 .unlogin{position:absolute;top:0;left:0;bottom:0;right:0;}.auto-1431393385550 .unlogin .autowrap {margin:115px auto;width:188px;}.auto-1431393385550 .unlogin .autowrap p{line-height:26px;color:#666;margin-right:10px;}.auto-1431393385551{width:100%;padding-top:15px;}.auto-1431393385551 .tagCt{margin-bottom:6px;}.auto-1431393385551 .bar{padding:10px 0;}.auto-1431393385551 .time{padding-left:10px;}.auto-1431393385551 .opt{cursor:pointer;}.auto-1431393385551 .divider{color:#ddd;margin:0 5px;}.auto-1431393385552 {width:550px;}.auto-1431393385552 .small{background-color:#fff;border:1px solid #dfdfdf;padding:10px;color:#999;}.auto-1431393385552 .small a{color:#61A500;}.auto-1431393385552 .small.j-logupbox{cursor:pointer;}.auto-1431393385552 .ui-richEditor .rich-editor .ztbar{padding:0 4px;}.auto-1431393385553{border-bottom:1px solid #eee;}.auto-1431393385553 .m-detailInfoItem .bar{padding:5px 0;}.auto-1431393385554 .triangle{position:absolute;top:0;width:14px;height:10px;background: url(http://mc.stu.126.net/res/images/ui/forum_icon.png?b12539c2400cc76ad30262bdf7e12cbd) no-repeat -100px -2px;}.auto-1431393385554 .wrap{margin-top:7px;border:1px solid #ddd;border-radius:2px;}.auto-1431393385554 .wrap .m-comment-pool{padding:0 10px;}.auto-1431393385554 .wrap .m-comment-pool .j-list{padding-bottom:20px;}.auto-1431393385554 .wrap .m-add-comment{padding:15px 10px;background-color:#fafafa;border-top:1px solid #eee;position:relative;}.auto-1431393385555{border-bottom:1px solid #eee;zoom:1;}.auto-1431393385555 .m-commentWrapper{margin:-5px 0 15px 0;}.auto-1431393385555 .m-commentWrapper .wrap{width:570px; position:relative;}.auto-1431393385556 {line-height:20px;color:#666;}.auto-1431393385556 .divider{display:inline-block;border-left:1px solid #ddd;margin:0px 10px 0px 5px;}.auto-1431393385556 a{color:#666;cursor:pointer;}.auto-1431393385556 a:hover{color:#61A500;text-decoration:none;}.auto-1431393385556 a span{background:url(http://mc.stu.126.net/res/images/ui/forum_icon.png?b12539c2400cc76ad30262bdf7e12cbd) no-repeat -4999px -4999px;margin-left:4px;display:inline-block;width:12px;height:20px;vertical-align:top;}.auto-1431393385556 .upsort,.auto-1431393385556 .downsort{color:#61A500;}.auto-1431393385556 .double.nosort span{background-position:0 2px;}.auto-1431393385556 .double.downsort span{background-position:-20px 2px;}.auto-1431393385556 .double.upsort span{background-position:-38px 2px;}.auto-1431393385557 .rinfobox{padding:30px 0 10px 0; border-bottom:1px solid #eee;}.auto-1431393385557 .allbox{margin-bottom:20px;}.auto-1431393385558 .userInfo .type{float:left; font-size:12px; margin:0 5px;padding:0 5px;display:inline-block;line-height:18px;color:#fff;text-align:center;}.auto-1431393385558 .userInfo .type.lector{background-color:#6CB4FB;}.auto-1431393385558 .userInfo .type.manager{background-color:#9BCE00;}.auto-1431393385558 .userInfo .type.assist{background-color:#9BCE00;}.auto-1431393385558 .userInfo .username{max-width:80%;}.auto-1431393385559 {}.auto-1431393385559 .row{margin-top:20px;}.auto-1431393385559 .row .txt{font-size:14px;line-height:30px;}.auto-1431393385559 .row .ct{margin-left:8px;}.auto-1431393385559 .row .ct.ct1{margin-left:12px;}.auto-1431393385559 .row.row1{margin-left:80px;}.auto-1431393385560 { width:406px; height:160px; margin:0 20px 0 20px;}.auto-1431393385560 .info{ color:#333; font-size:14px;line-height:23px;margin:0 0 50px;font-family:"微软雅黑";}.auto-1431393385560 .info span{ color:#d62727;}.auto-1431393385560 .ok{ margin:0 0 0 147px;}.auto-1431393385561 .u-body{width:900px}.auto-1431393385561 .j-payBlock{margin-right: 25px;width: 139px;height: 48px;border:1px solid #cccccc;cursor:pointer;background: url(/res/images/pages/pay/payList.png);}.auto-1431393385561 .active{border:1px solid #21a557;}.auto-1431393385561 .active .check{top: 0px;right: 0px;width: 23px;height: 24px;background: url(/res/images/pages/pay/payIcons.png) 0 -161px;}.auto-1431393385561 .drop{right:3px;bottom:3px;border-top:5px solid #fff;border-left:5px solid #fff;border-right:5px solid #cccccc;border-bottom:5px solid #cccccc;width:1px;height:1px;}.auto-1431393385561 .pay0{background-position: 0px 0px;}.auto-1431393385561 .pay0-disable{background-position: 0px -50px;}.auto-1431393385561 .pay1{background-position: -140px 0px;}.auto-1431393385561 .pay1-disable{background-position: -140px -50px;}.auto-1431393385561 .pay2{background:url(/res/images/pages/pay/bankLogo.png);background-position: 0px -864px;}.auto-1431393385561 .pay2-disable{background-position: 0px -864px;}.auto-1431393385561 .j-info{margin-left:-18px;padding-left: 30px;height: 48px;display:none;font-size:12px;}.auto-1431393385561 .j-info .warning{display:block;width:23px;height:23px;left:0;top:0;background: url(/res/images/ui/ui_sprite.png) no-repeat -2px -48px;}.auto-1431393385561 .j-info a{color:#10ae58;}.auto-1431393385561 .attention{line-height: 18px;width: 180px;}.auto-1431393385561 .j-bankList{padding-top:10px;width: 670px ;top:50px;z-index: 2;}.auto-1431393385561 .bankContent{background-color: #eeeeee;border: 1px solid #cccccc;}.auto-1431393385561 .bankListHide{display: none;}.auto-1431393385561 .j-fullList ul{margin: 0 0;}.auto-1431393385561 .j-fullList ul li{list-style: none;float: left;margin-left: 20px;margin-top: 10px;width: 140px;height: 50px;border: 1px solid #cccccc;cursor:pointer;background:url(/res/images/pages/pay/bankLogo.png);}.auto-1431393385561 .j-shortList{padding: 10px 0;}.auto-1431393385561 .j-shortList ul{margin: 0px 0 ;}.auto-1431393385561 .j-shortList ul li{float: left;margin-left: 25px;margin-top: 10px;font-size:12px;}.auto-1431393385561 .j-shortList li a{color:#666666;}.auto-1431393385561 .j-shortList li a:hover{color:#10ae58;}.auto-1431393385562 .main{width:550px;padding:20px 20px 0;}.auto-1431393385562 .tip{ padding:0 0 10px 87px;}.auto-1431393385562 .tit{ width:80px;text-align:right;padding:5px 10px 0 0;}.auto-1431393385562 .fb{ height:30px; padding: 0 0 0 87px;}.auto-1431393385562 .fb .m-feedbackinfo{ padding-left:0;}.auto-1431393385562 a.u-btn{ display: block;width:100px;margin:20px auto;}.auto-1431393385562 a.u-btn.lg{width:150px;}.auto-1431393385562 .result{ width:388px;margin:10px auto;}.auto-1431393385562 .result h3{ margin:0 0 20px;font-size:20px;line-height: 19px;}.auto-1431393385562 .result ul li{ list-style-type: disc;font-size:14px;line-height:28px;color: #666;}.auto-1431393385562 .result .wrong{ font-size:28px;color:#ce5251;margin-right: 16px;float: left;}.auto-1431393385562 .result .right{ font-size:28px;color:#8bb721;margin-right: 16px;float: left;}.u-scrtop{z-index:103;width:56px;height:56px;position:fixed;background: url(http://mc.stu.126.net/res/images/ui/ui_sprite.png?eca43f780d8dd17d8d22c71cf615902a) -287px -92px;bottom:30px;right:30px;}.u-scrtop:hover{background-position: -344px -92px;}.u-scrtop{display:none;opacity: 0; transition: opacity 0.3s; -webkit-transition: opacity 0.3s; -moz-transition: opacity 0.3s; -o-transition: opacity 0.3s;}.auto-1431393385564 .main{padding:20px;}.auto-1431393385564 .fb{ margin: 0 0 20px;}.auto-1431393385564 .fb .m-feedbackinfo{ padding-left:0;}.auto-1431393385564 .btns{width:112px;margin:0 auto;}.auto-1431393385564 .u-btn{width:100px;}</style>
<style type="text/css">.sp_bd ul li{ width:230px; float:left; height:205px; margin-right:15px; margin-bottom:20px}.sp_bd ul li span{ display:block;}.sp_bd ul li span.tp{ width:225px; height:160px; margin-bottom:10px; padding-top:5px; padding-left:5px; background:url(/images/yuexue2/video/5sp_tpbg.png) no-repeat; position:relative}.sp_bd ul li span.tp a{ display:block; position:relative; width:220px; height:164px;}.sp_bd ul li span.tp .bg{filter: progid:DXImageTransform.Microsoft.Alpha(opacity=80); opacity: 0.8; display:none; background:#000; width:220px; height:164px; position:absolute;top:5px;left:5px}.sp_bd ul li span.tp .bg a{ display:block; width:83px; height:27px; color:#fff; line-height:27px; text-align:center; border:1px solid #fff; margin-left:78px; margin-top:40px; background:#a47a1c;font-family:FZSEJW;-moz-border-radius: 5px;      /* Gecko browsers */ -webkit-border-radius: 5px;   /* Webkit browsers */border-radius:5px;            /* W3C syntax */}.sp_bd ul li span.tp a.fx{ margin-top:20px; background:#598624}.sp_bd ul li span.tp:hover .bg{ display:block}.sp_bd ul li span.ms{ width:230px; height:30px; background:url(/images/yuexue2/video/5sp_ico05.png) no-repeat}.sp_bd ul li span.ms h3{ height:30px; color:#f8efe0; line-height:30px; padding-left:15px; font-family:FZSEJW; font-size:16px}</style>
<style type="text/css">
.yd_upLoad{margin:0 auto 0 auto;width:90%;}
.yd_upLoad .yd_content{margin:0 auto 0 20;width:710px;}
.yd_upLoad .yd_c{height:30px;text-align:right;width:100%;}
.title{margin-top:20px;}
.title span{display:block;height:40px;line-height:40px;}
.title div{display:inline;height:40px;margin-left:5px;margin-top:5px;width:600px;}
.title div input{border:1px solid #ccc;font-size:18px;height:28px;line-height:28px;margin-top:1px;margin-right:36px;width:536px;}
.fl{float:left;}
.select{display:inline;float:left;margin-left:5px;}
.select li{display:inline;float:left;margin-left:10px;height:40px;}
.select li.li_01{background:url(/images/yuexue2/video/Img_08.png) 0 0 no-repeat;height:180px;margin-left:30px;width:122px;position:relative;}
.select li.li_01 img{height:170px;width:116px;position:absolute;top:3px;left:3px;z-index:99;}
.select li.li_02{margin-left:20px;}
.select li.li_03{font-size:14px;height:38px;width:356px;position:relative;z-index:99;}
.select li.li_03 .jd{background:url(/images/yuexue2/video/Img_26.png) 0 0 no-repeat;height:20px;width:252px;position:absolute;left:0;top:5px;z-index:100;}
.select li.li_03 .jdp{background:none;color:#FFF;height:20px;line-height:20px;text-align:center;width:252px;position:absolute;left:0;top:5px;z-index:101;}
.select li.li_03 .time{background:none;height:20px;line-height:20px;text-align:center;width:90px;position:absolute;right:0;top:4px;z-index:101;}
.select li.li_04{background:url(/images/yuexue2/video/Img_28.png) 0 0 no-repeat;height:148px;width:214px;position:relative;z-index:99;}
.select li.li_04 img{height:134px;width:200px;position:absolute;top:5px;left:6px;}
.uploadfile{width:94px;height:38px;position: absolute;opacity:0;filter:alpha(opacity=0);right:0;top:0;}
.uploadaccfile{width:94px;height:38px;position: absolute;opacity:0;filter:alpha(opacity=0);right:0;top:0;}
</style>
<script id="VideouploadRec" type="text/x-jsrender">
<form id="videouploadform" style="border: 1px solid #ccc;margin-left:20px;margin-top: 15px;" name="videouploadform" action="/P.tojson" method="post" enctype="multipart/form-data">
<input type="hidden" id="sysName" name="sysName" value="yuexue">
<input type="hidden" id="oprID" name="oprID" value="Video">
<input type="hidden" id="actions" name="actions" value="uploadVideoList">
<input type="hidden" id="op_type" name="op_type" value="save">
<input type="hidden" id="post_folder_id" name="folder_id" value="">
<input type="hidden" id="attachment_id" name="attachment_id" value="">
<input type="hidden" id="post_subject_id" name="subject_id" value="">
<input type="hidden" id="post_grade_code" name="grade_code" value="">
<input type="hidden" id="post_class_id" name="class_id" value="">
<input type="hidden" name="share_level" id="share_level" value="10"/>
<div class="yd_upLoad">
	<div class="yd_c fr">
       <div style="float: left; margin: 10px 0px 0px 240px;color: #f90; font-size: 14px;"><p style=" display:inline;font-size: 16px;">当前所选章节:</p><p style=" display:inline;font-size: 16px;" id="select_f"></p></div>
	   <div style="float: right;">
	    <span class="all_in cBtnNormal fl" style="margin-right: 10px;margin-top: 10px;"> <a href="javascript:void(0)" onclick="uploadVideoList();"><i style="color:#fff">上传</i></a></span>
	    <span class="all_in cBtnSilver fl" style="margin-right: 10px;;margin-top: 10px;"> <a href="javascript:void(0)" onclick="cancel_courseware()"><i style="color:#000">取消</i></a></span>
	  </div>
	</div>
	<div class="yd_content">
     <table>
       <!-- 标题 S -->
       <tr class="title">
        <td>
		  <span class="fl">文件标题：</span>
        </td>
        <td colspan="3">
			<div class="fl"><input type="text" name="paper_name_show" id="paper_name_show" value="" class="fr" /><input type="hidden" name="paper_name" id="paper_name_hide" value="" class="fr" /></div>
        </td>
        </tr>
		<tr>
			<td>
				 <span class="fl" style="width:80px;">所属资源包：</span>
			</td>
            <td>
			 <div class="fl" style="margin-left:30px;">
			   <select name="resource_id" id="resource_id" style="width:160px;height:30px;font-size:18px;">
			   </select>
			 </div>
			<span class="all_in cBtnNormal fl" style="margin-left: 10px;"> <a href="javascript:void(0)" onclick="addResource();"><i style="color:#fff">添加</i></a></span>
            </td>
		</tr>
		<!-- 标题 E -->
		<!-- 类型 S -->
		<tr class="title">
			<td><span class="fl">文件类型：</span></td>
            <td>
			 <div class="fl" style="margin-left:30px;">
			   <select name="resource_type" id="resource_type_id" onchange="selectResourceType(this.value)"  style="width:160px;height:30px;font-size:20px;">
			     <option value="">-----------</option>
			     <option value="2010">视  频</option>
			     <option value="2020">Flash</option>
			     <option value="2030">PPT</option>
                 <option value="2040">Word</option>
			   </select>
			 </div>
            </td>
            <td colspan="2"></td>
		</tr>
		<!-- 类型 E -->
       <!-- 选择文件 S -->
		<tr class="title">
			<td><span class="fl">上传文件：</span></td>
			<td colspan="3">
             <ul class="select">
				<li style="height:40px;width:220px;">
				   <span id="vidoe_file" style="position:relative;width:74px;height:38px;margin-left:16px;margin-top:5px;">
                     <p class="cBtnNormal">选择文件</p>
                      <!--"filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);"属性为解决在ie8中opacity：0 设置透明度不起效果-->
				      <input id="video_attachment" name="video_attachment" type="file" style="width:94px;height:38px;position: absolute;opacity:0;right:0;top:0;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" accept=".mp4,.avi,.mp3,.swf,.ppt,.pptx" onchange="return selectVideo(this);"/>
				   </span>
				   <span id="vidoe_name" style="display:none;">
				   </span>
				</li>				
				<li class="li_03">
					<div class="jd"><img id="video_bar" name="video_bar" src="/images/yuexue2/video/Img_27.png" alt="" style="height:20px;width:0%;" /></div>
					<div class="jdp" id="video_percent" name="video_percent">已上传0%</div>
					<div class="time" id="video_time" name="video_time">00:00:00</div>
				</li>
			 </ul>
            </td>
		</tr>
		<!-- 选择文件 E -->
       	<!-- 封面 S -->
		<tr class="title">
			<td valign="top"><span class="fl">文件封面：</span></td>
			<td colspan="3">
             <ul class="select">
				<li style="width:76px;height:33px;padding-top:8px;color:#000000;text-align:center;">
				   <input type='checkbox' id="cover_path" name='cover_path' value="/images/yuexue2/message/2.jpg" onclick="setDefaultCoverImg();" checked/>默认
				</li>
				<li style="height:40px;">
				   <span id="vidoe_file" style="position:relative;width:74px;height:38px;margin-left:16px;margin-top:5px;"><p class="cBtnNormal">选择封面</p>
				      <input id="cover_attachment"  name="cover_attachment" type="file" class="uploadfile" accept=".jpg,.png,.bmp,.gif" onchange="return selectCoverImg(this);"/>
				   </span>
				</li>				
				<li id="cover_default" class="li_04">
				   <img id="cover_img" src="/images/yuexue2/video/2.jpg"/>				   
				</li>
                <li id="cover_local" class="li_04" style="display:none;">
				   <div id="image-wrap"></div>
				</li>				
			 </ul>
            </td>
		</tr>
		<!-- 封面 E -->
       <tr id="id_hint"><td colspan="4" style="color: red;text-align: center;">温馨提示：推荐使用Google、Firefox浏览器，让上传显示效果达到最佳</td></tr>
  </talbe>
  </div>
</div>
</form>
</script>
<script id="BookFolderRec" type="text/x-jsrender">
 {{for Data}}
	{{if parent_folder_id !== "-1"}}
	<li class="fn-clear treenode" data-code="{{:folder_code}}">
     {{if child_count > 0}}
     <div class="fn-clear tree-{{titleTags folder_code 1/}}">
      <a href="javascript:void(0)" onclick="treeToggle('{{:folder_code}}',this)" class="icon icon_0"></a>
     {{else}}
     <div class="fn-clear tree-{{titleTags folder_code 2/}}">
      <a href="javascript:void(0)" class="icon icon_1"></a>
     {{/if}}
      <a href="javascript:loadCoursewareData(20,'{{:folder_code}}','{{:folder_id}}','{{:folder_name}}',1)" title="{{:folder_name}}" class="item " id="{{:folder_id}}" data-nselect="{{:folder_code}}">{{nameTags folder_name folder_code/}}</a> 
     </div>
  </li>
  {{/if}}
 {{/for}}

</script>
<script id="VideoRec" type="text/x-jsrender">
{{for Data}}
<li class="{{:paper_id}}">
  <div class="u-clist f-cb f-pr j-href" style="height:auto;">
   <div  id="auto-id-{{:paper_id}}" style="background-color:#eee;font-size: 12px;height: 35px;"data-href="/course/cau-251001">       
    <div style="width:500px;float:left"> 
      <span class="t1 f-f0 f-nowrp" style="clear:both;line-height:35px;margin:20px;">
         <a title="{{:paper_name}}" class="fgxs" style="color:#000;" onclick="openVideoView('{{:paper_id}}','{{:resource_type}}','{{:paper_name}}','{{:folder_id}}','{{:attachment_id}}')">{{courseName:paper_name}}</a>	
     </span> 
		<span style="float:right;line-height: 35px;font-size:14px;color:#000;font-family: "Arial"">上传时间：{{:create_date}}</span>
    </div>
 <div style="float:right;margin-top:10px;width:80px;">共浏览{{:readcount}}次</div>
    <div>    
     <span style="line-height:35px;float:right;margin-right:20px;">
      <a class="paper_edit cBtnNormal" onclick="delPaper('{{:paper_id}}');">删除</a>&nbsp;&nbsp;
	  <span class="paper_edit cBtnNormal" onclick="updateResource('{{:paper_id}}');" >编辑</span>
     </span>
    </div>
   </div>
  </div>
</li>
{{/for}}
</script>
<script id="ReList" type="text/x-jsrender">
	{{for Data}}
	  <option value="{{:resource_id}}">{{:resource_name}}</option>
	{{/for}}
</script>
<script>
var _folderid;
var _attachmentid;
var _subjectid;
var _gradecode;
var _classid;
var _resource_type;
var _ext_type = ".mp4,.flv,.mov";
//新增选项
bc_cfg.coursewareIndex = 1;
$(document).ready(function(){
   $(".nav").children("a").removeClass("active");
   $("#th_course").addClass("active");
   loadClass();
   var htmlTemp = $("#VideouploadRec").render({_index:++bc_cfg.coursewareIndex});
	 $("#coursewareList").prepend(htmlTemp);
	 $("#videouploadform").hide();
	 top.setMethod("loadCoursewareData","$('#resourc_type').val(),$('#folder_code').val(),$('#folder_id').val(),$('#folder_name').val()");
   var video_bar = $("#video_bar");
   var video_percent = $("#video_percent");
   var video_time = $("#video_time");
   /****  包含附件,采用ajaxForm提交 begin ****/
	var options = {
			beforeSubmit:function(formData, jqForm, options) {
	 			//var queryString = $.param(formData);
	 			$("#paper_name_hide").val(encodeURI($("#paper_name_show").val()));
	 			return true;
	 		},
	 		uploadProgress: function(event, position, total, percentComplete) {
	 			video_time.html(timer(curTime, percentComplete));
	 			
	 			var percentVal = percentComplete + '%'; //获得进度
	 			video_bar.width(percentVal); //上传进度条宽度变宽
	 			video_percent.html("已上传" + percentVal); //显示上传进度百分比
	 		}, 	
			success:(function(data,status) {
				if(data.error_description) {
					toastr.error("保存失败！错误信息[" + data.error_description + "]", "错误信息");
					errer_courseware();
				} else {
					toastr.clear();
					toastr.success("提交成功!");
					cancel_courseware();
					loadCoursewareData("20",$("#folder_code").val(),$("#folder_id").val(),$("#folder_name").val(),1);
				}
			}),
			error:(function(data,status){
				toastr.error("保存失败！错误信息[" + data.error_description + "]", "错误信息");
			}),
			dataType:"json"
	  }
	  $('#videouploadform').submit(function() {
			$(this).ajaxSubmit(options);
			return false; //必须返回false,阻止页面提交
	  });

	  $.imageFileVisible({wrapSelector: "#image-wrap",
       fileSelector: "#cover_attachment",
       width: 200,
       height: 134
    });
   
 getOs();

});
 //判断浏览器类型
function  getOs(){
 var ua = navigator.userAgent.toLowerCase(); 
 if(ua.indexOf("chrome")>0 || ua.indexOf("firefox")>0){
   $("#id_hint").remove();
 }
  
}
$.views.tags({
	titleTags:function(_val,_level){
		var _title = "title2";
		if (_level==1){
			_title = "title2";
			if (_val.split(",").length == 3){
			   _title = "title3";
			} else if (_val.split(",").length > 3){
			   _title = "title4";
			}
		} else {
			_title = "title3";
			if (_val.split(",").length == 2){
				_title = "title2";
			} else if (_val.split(",").length == 4){
			   _title = "title4";
			} else if (_val.split(",").length > 4){
			   _title = "title5";
			}
		}
		return _title;		
	},
	nameTags:function(_val,_code){
		var _len = 22;
		if (_code.split(",").length == 4){
		  _len = 20;
		} else if (_code.split(",").length == 5){
		  _len = 17;
		} else if (_code.split(",").length > 6){
		  _len = 15;
		}
		return BcUtil.cutstr(_val,_len,true);
	}
});

$.views.converters({
  nameConverter:function(value){
	 return BcUtil.cutstr(value,20,true);
  },
  courseName:function(value){
 	return BcUtil.cutstr(value,30,true);
  }
});
Array.prototype.S=String.fromCharCode(2);
Array.prototype.in_array=function(e)
{
    var r=new RegExp(this.S+e+this.S);
    return (r.test(this.S+this.join(this.S)+this.S));
}

//加载资源包
function loadResource(){
	var bcReq = new BcRequest('yuexue','Attachment','getResource');
	bcReq.setExtraPs({"PageSize":"0"});
	bcReq.setSuccFn(function(data,status){	
	   $("#resource_id").empty();
	    $("#resource_id").html("<option value='' >------------</option>");
	   	var _ResListData = $("#ReList").render(data);
		$("#resource_id").append(_ResListData);
	});
	bcReq.postData();
}
//添加资源包
function addResource(){
	$.fancybox.open({
		href:"addRes.jsp",
		type:'iframe',
		width:620,
		height:520,
		padding:0,
		margin:0,
		closeBtn:true,
		iframe:{scrolling:'no',preload:false}
	});
}

function loadClass(){
	var bcReq = new BcRequest('yuexue','teacher','getTeachClassMOOC');
	bcReq.setExtraPs({"PageNo":1, "PageSize": -1});
	bcReq.setSuccFn(function(data,status){
		if (data.Data.length>0){
		   var _rec = data.Data[0];
           bc_cfg.subjectData = data.Data;
		   bc_cfg.classid = _rec.classid;
		   bc_cfg.classnm = _rec.classnm;
		   bc_cfg.subjectid = _rec.subjectid;
		   bc_cfg.gradename = _rec.gradename;
		   bc_cfg.gradecode = _rec.gradecode;
		   bc_cfg.subjname = _rec.subjname;
		   bc_cfg.subject_book_id = _rec.subject_book_id;
		   bc_cfg.foldername = _rec.folder_name;
		   bc_cfg.foldercode = _rec.folder_code;
		   $("#subjectnm").html(bc_cfg.gradename + _rec.subjname);
	     
	      <%if(StringUtil.isNotEmpty(subjectid)){%>
			   bc_cfg.subjectid = '<%=subjectid%>';
			   bc_cfg.gradecode = '<%=gradecode%>';
			   bc_cfg.paperid = '<%=paperid%>';
			   bc_cfg.foldercode = '<%=foldercode%>';
			   for(var i=0;i<data.Data.length;i++){
			   	 var _rec2 = data.Data[i];
			   	 if(_rec2.subjectid=='<%=subjectid%>' && _rec2.gradecode=='<%=gradecode%>'){
			   	 	 $("#subjectnm").html(_rec2.gradename + _rec2.subjname);
			   	 }
			   }
		   <%}%>
		   $("#qry_subject_id").val(bc_cfg.subjectid);
		   $("#qry_grade_code").val(bc_cfg.gradecode);
		   genSemester();
           getBookChildFolder();
		}
		
		<%if(StringUtil.isEmpty(paperid)){%>
			var subject_arr = [];
			if(data.Data.length>1){
				$(".changeSubjet").css("visibility","visible");
				var _subjectnmList = "";
				var req_subjectid_gradecode = '<%=subjectid%>-<%=gradecode%>';
				for(var i=0;i<data.Data.length;i++){
					var _rec = data.Data[i];
					var subjectid_gradecode = _rec.subjectid+'-'+_rec.gradecode;
					if(!subject_arr.in_array(subjectid_gradecode)){
						subject_arr.push(subjectid_gradecode);
						var activeClass ="";
						if(req_subjectid_gradecode!='-'){
						  activeClass = subjectid_gradecode==req_subjectid_gradecode?"active":"";
						} else {
						  activeClass = subject_arr.length==1?"active":"";
						}
						_subjectnmList += '<a href="javascript:void(0)" onclick="chooseSubject(this)" class="'+activeClass+'" data-subjectid="'+_rec.subjectid+'" data-gradecode="'+_rec.gradecode+'" data-gradename="'+_rec.gradename+'" data-subjname="'+_rec.subjname+'" data-classnm="'+_rec.classnm+'" data-foldercode="'+_rec.folder_code+'">'+_rec.gradename + _rec.subjname+'</a><br/>';
					}
				}
				$("#subjectnmList").html(_subjectnmList);
				if(subject_arr.length <= 1){
					$(".changeSubjet").css("visibility","hidden");
				}
			}
		<%}%>
	});
	bcReq.postData();
}

//创建学科切换下拉框
function genSemester(){
    $("#semester").empty();
    var subject_arr = [];
    for(var i=0;i<bc_cfg.subjectData.length;i++){
    	var _rec = bc_cfg.subjectData[i];
    	var subjectid_gradecode = _rec.subjectid + "_" +_rec.gradecode;
    	var subjectid_gradecode2 = bc_cfg.subjectid + "_" + bc_cfg.gradecode;
    	if(subjectid_gradecode == subjectid_gradecode2 && !subject_arr.in_array(subjectid_gradecode+"_"+_rec.folder_name)){
    	  subject_arr.push(subjectid_gradecode+"_"+_rec.folder_name);
    	  $("#semester").append("<option value='"+_rec.folder_code+"'>"+_rec.gradename + _rec.subjname + _rec.folder_name+"</option>");
    	}
    }
}
//切换学科
function chooseSubject(_obj){
    $(_obj).parent().find('a').removeClass("active");
    $(_obj).addClass("active");
    bc_cfg.subjectid = $(_obj).attr('data-subjectid');
    bc_cfg.gradecode = $(_obj).attr('data-gradecode');
    bc_cfg.gradename = $(_obj).attr('data-gradename');
    bc_cfg.classnm = $(_obj).attr('data-classnm');
    bc_cfg.subjname = $(_obj).attr('data-subjname');
    bc_cfg.foldercode = $(_obj).attr('data-foldercode');
    $("#qry_subject_id").val(bc_cfg.subjectid);
    $("#qry_grade_code").val(bc_cfg.gradecode);
    $("#subjectnm").html(bc_cfg.gradename + bc_cfg.subjname);
    genSemester();
    getBookChildFolder();
    BcMOOCSetSession(bc_cfg.subjectid,bc_cfg.gradecode,bc_cfg.foldercode);//加入session中
}
  function getBookChildFolder(){  
		var bcReq = new BcRequest('yuexue','Knowledge','getBookChildFolderMooc');
		bcReq.setExtraPs({"qry_subjectid":bc_cfg.subjectid,"qry_gradecode":bc_cfg.gradecode,"qry_foldercode":bc_cfg.foldercode, "PageNo":1, "PageSize": -1});
		bcReq.setSuccFn(function(data,status){
			var _rowContent = $("#BookFolderRec").render(data);
			$(".numList").html(_rowContent);
			if(data.Data.length>0){
			  loadCoursewareData(20,data.Data[0].folder_code,data.Data[0].folder_id,data.Data[0].folder_name,1);
			}else{
			   $(".videoList").html("<div style='text-align: center;color:red;'>暂无内容!</div>");
               $("#ext-comp-1012").hide();
			}
		});
		bcReq.postData();
	}
function loadCoursewareData(resourc_type,folder_code,folder_id,folder_name,pageNo) {
	//下拉框中类型选项动态变化
	if(resourc_type!=20){
		$("option[value='"+resourc_type+"']").attr("selected","selected");
	}
	$("#folder_id").val(folder_id);
	$("#folder_name").val(folder_name);
	$("#select_f").html(folder_name);
	$("#folder_code").val(folder_code);
	$("#resourc_type").val(resourc_type);
	$(".filter-box").children("div").children("ul").children("li").children("a").removeClass("active");
	$(".numList").children("li").children("div").children("a").removeClass("active");
	$("#"+folder_id).addClass("active");
	if(resourc_type=="20"){
	  $("#auto-id-all").addClass("active");
	}else if(resourc_type=="2010"){
	  $("#auto-id-video").addClass("active");
	}else if(resourc_type=="2020"){
	  $("#auto-id-f").addClass("active");
	}else if(resourc_type=="2030"){
	  $("#auto-id-p").addClass("active");
	}else if(resourc_type=="2040"){
	  $("#auto-id-w").addClass("active");
	}
	$("#loadingMask").show();
	$("#loadingPb").show();
	var SortField=$("#orderby").val();
    var bcReq = new BcRequest('yuexue','Video','queryTechVideo');
	bcReq.setExtraPs({"qry_resource_type":resourc_type,"PageSize":"10","PageNo":pageNo,"qry_folder_id":folder_code,"qry_all":0,"qry_subject_id":$("#qry_subject_id").val(),"SortField":SortField,"SortDir":"DESC"}); 
	bcReq.setSuccFn(function(data,status){
	    $("#loadingMask").hide();
	    $("#loadingPb").hide();
        $(".videoList").empty();
        if(data.Data.length>0){
			var _videoContent = $("#VideoRec").render(data);
	        $(".videoList").append(_videoContent);
	        $("#ext-comp-1012").show();
            top.setPageInfo(data);
        }else{
          $(".videoList").html("<div style='text-align: center;color:red;'>暂无内容!</div>");
          $("#ext-comp-1012").hide();
        }
	});
	bcReq.postData();
}
function treeToggle(_folder_code,_obj){
	$(".treenode").each(function(){
     if($(this).attr('data-code').indexOf(_folder_code+",")==0){
    	if($(this).is(":hidden")) {
    		$(this).show(600);
    	} else {
    		$(this).hide(600);
    	}
     }
   });
}
function coursewareUpload(){
$("#coursewareList").show();
loadResource();
var folderid=$("#folder_id").val(); 
var subjectid=$("#qry_subject_id").val();
var gradecode=$("#qry_grade_code").val();
   /* var urlPath ="/mooc/teacher/courseware_upload.jsp?folderid="+folderid+"&subjectid="+subjectid+"&gradecode="+gradecode+"&classid="+bc_cfg.classid+"&M="+Math.random();
   $.fancybox.open({
		'href':urlPath,
		'type':'iframe',
		'width':700,
		'height':488,
		'padding':0,
		'margin':20,
		'centerOnScroll':true,
		'autoScale':false,
		'closeBtn':false,
		'overlay':true,
		'modal':true
	}); */
	
	if(bc_cfg.new_coureware){
		toastr.warning("有新增课件未提交，请先提交或取消新增课件");
		return;
	 }
	 $("#videouploadform").show();
	
	  _folderid = folderid;;
	  _subjectid =subjectid
	 _gradecode = gradecode;
	 _classid = bc_cfg.classid;
	 if($("#folder_code").val()==$("#folder_id").val()){
	    toastr.warning("请选择左侧具体章节！");
	 }
	$("#post_folder_id").val(_folderid);
	$("#post_subject_id").val(_subjectid);	  
	$("#post_grade_code").val(_gradecode);	  
	$("#post_class_id").val(_classid);
	 bc_cfg.new_coureware=true;
	
}	
function openVideoView(paper_id,resource_type,paper_name,folder_id,attachment_id){
    window.open("/mooc/teacher/coursewarePlay.jsp?attachment_id="+attachment_id+"&folder_id="
    +folder_id+"&paper_id="+paper_id+"&paper_name="+encodeURI(encodeURI(paper_name))); 
}

// 发给学生
function openSendPaper(_paperid){
	var urlPath="/mooc/teacher/send_paper.jsp?paperid="+_paperid;
    $.fancybox.open({href:urlPath,type:'iframe',width:650,height:600,padding:0,margin:0,closeBtn:true});
}
//删除
function delPaper(_paperid){
   zebra_confirm('你确定要删除吗?',
  	 function(){				      
		  var bReq = new BcRequest("yuexue","ExamPaper","deleteExamPaper");
		  bReq.setExtraPs({"paper_id":_paperid});
		  bReq.setSuccFn(function(data,status){
			 //  $("."+_paperid).remove();
			   loadCoursewareData("20",$("#folder_code").val(),$("#folder_id").val(),$("#folder_name").val(),1);
			   toastr.success('数据已删除');
		  });
		  bReq.postData();
	 },
	 function(){},
     function(){}
  );
}

function selectVideo(obj) {
	// return selectInputFile(obj, ".mp4,.avi,.mp3", "请选择正确的视频文件！", true);
	//超过
/* 	var msgstr = "";
	if($("#resource_type_id").val()=="2030"){
	  var filezise=document.getElementById("video_attachment").files[0].size;
	  if(filezise>20*1024*1024){
	   msgstr = msgstr + "请选择小于20MB的Office文件！";
	    toastr.error(msgstr, "错误信息");    	
    	return;
	  }
	} */
	// 隐藏文件选择框 显示文件名
	$("#vidoe_name").show();
	var allfn = getFileName(obj.value);
	var fileExt = getFileExt(obj.value);
	if (allfn.length >= 12) {
		fn = allfn.substring(allfn.length - 12, allfn.length);
	} else {
		fn = allfn;
	}
	// 如果为空 将上传文件名给paper_name
	if($("#paper_name_show").val() == "") {
		$("#paper_name_show").val(allfn);
	}
	$("#vidoe_name").html("..." + fn + "." + fileExt).attr("title", allfn);
	//$("#vidoe_name").html("..." + fn);
	$("#vidoe_file").hide();
}
function setDefaultVideo() {
	// 隐藏文件选择框 显示文件名
	$("#vidoe_name").hide();
	$("#vidoe_name").html("");
	$("#vidoe_file").show();
}

//得到上传的文件名
function getFileName(fileName) {
	if (fileName != "") {
		if (fileName.length > 1 && fileName) {	
			var lstart = fileName.lastIndexOf("\\");	
			var ldot = fileName.lastIndexOf(".");
			var name = fileName.substring(lstart + 1, ldot);
			return name;
		}
		return "";	
	}
}
var __resource_type;
function selectResourceType(_resource_type){

    if (_resource_type == '2010' || _resource_type == '2050') {
		_ext_type = ".mp4,.flv,.mov";
	} else if (_resource_type == '2020') {
		_ext_type = ".swf";
	} else if (_resource_type == '2030') {
		_ext_type = ".ppt,.pptx";
	}else if (_resource_type == '2040') {
		_ext_type = ".doc,.docx";
	}
	if(__resource_type){ setDefaultVideo();}
	$("#video_attachment").attr("accept", _ext_type);
	$("#resource_type").val(_resource_type);
	__resource_type = _resource_type;
}
function selectInputFile(obj, validext, msgstr, istoastr) {
	if (obj.value != "") {
		var iserror = false;
	    // 检查上传的文件类型
	    var ext = getFileExt(obj.value);
	    if (validext.indexOf(ext) != -1) {
	    	return true;
	    } else {
	    	iserror = true;
	    }
	    
	    if (iserror) {
	    	if (istoastr) {
	    		toastr.info(msgstr);
	    	}
	    	obj.value = "";
	    	return false;
	    }
    }
}

function getFileExt(fileName) {
	if (fileName.length > 1 && fileName) {		
		var ldot = fileName.lastIndexOf(".");
		var type = fileName.substring(ldot + 1);
		return type;
	}
	return "";
}
function setDefaultCoverImg(){
   if ($("#cover_path").attr("checked")){	
	   $("#cover_default").show(); 
	   $("#cover_local").hide();       
   }	  
}
function selectCoverImg(obj){
   	$("#cover_local").show();
    $("#cover_default").hide();
    selectInputFile(obj, ".jpg,.png,.gif,.bmp", "请选择正确的图片文件！", true); 
    $("#cover_path").attr("checked", false);
	return true;
}


function uploadVideoList() {
    // 最后的提交还要校验文件类型
	// 检查封面的文件类型
   var msgstr = "";
   var reg = /["@^&()']/im; 
   var iserror = false;
   var _resource_type=$("#resource_type").val();
   
    curTime = new Date(); // 提交记录当前时间以便计算剩余时间
    var covera = $('input[name="cover_attachment"]');
    var videoa = $('input[name="video_attachment"]');


	if ($.trim($("input[name='paper_name_show']").val()) == ""){
   		msgstr = msgstr + "请输入文件名称！";
    	iserror = true;
	}else if(reg.test($("input[name='paper_name_show']").val())){
	    msgstr = msgstr + "文件名称包含非法字符！";
    	iserror = true;
   }else if($("input[name='paper_name_show']").val().length>50){
	    msgstr = msgstr + "文件名称长度必须<=50！";
    	iserror = true;
	}else{
	   $("#paper_name_hide").val(encodeURI($("#paper_name_show").val()));
	}    
	
	if(_resource_type==""){
     msgstr = msgstr + "请选择上传的文件类型！";
     iserror = true;
    } 
    
    if (videoa.val() == ""){
   		msgstr = msgstr + "请选择上传的文件！";
    	iserror = true;
	}
	   
    // 检查视频上传的文件类型
    var ext = getFileExt(videoa.val())
    if (_ext_type.indexOf(ext.toLowerCase()) != -1) {
    } else {
    	msgstr = msgstr + "请选择正确的文件！";
    	iserror = true;
    }
	
/* 	if($("#resource_type_id").val()=="2030"){
	  var filezise=document.getElementById("video_attachment").files[0].size;
	  if(filezise>20*1024*1024){
	   msgstr = msgstr + "请选择小于20MB的Office文件！";
    	iserror = true;
	  }
	} */
    var ext = getFileExt(covera.val());
  	if (".jpg,.png,.gif,.bmp".indexOf(ext.toLowerCase()) != -1) {
    } else {
    	msgstr = msgstr + "请选择正确的图片文件！";
    	iserror = true;
    }
    
    if (iserror) {
	    toastr.error(msgstr, "错误信息");    	
    } else {
 		toastr.info("正在保存,请稍后...");
		$("#videouploadform").submit();
	} 
}
var curTime;
//新增课件取消按钮
function cancel_courseware(){
    //将页面初始化
	$("#paper_name_show").val("");
	$("#paper_name_hide").val("");
    $("#cover_path").attr("checked", true);
	setDefaultCoverImg();
	setDefaultVideo();
	$("#video_time").html("00:00:00");
	$("#resource_type_id").val("");
	clearVideo();
	$("#video_bar").width("0%");
	$("#video_percent").html("已上传0%");
    $("#videouploadform").hide();
    bc_cfg.new_coureware=false;
}
function errer_courseware(){
	setDefaultVideo();
	$("#video_time").html("00:00:00");
	clearVideo();
	$("#video_bar").width("0%");
	$("#video_percent").html("已上传0%");
	bc_cfg.new_coureware=false;
}
function clearVideo() {
	var file = document.getElementById("video_attachment");
	if (file.outerHTML) { // for IE, Opera, Safari, Chrome
		file.outerHTML = file.outerHTML;
	} else { // FF(包括3.5)
     	file.value = "";
	}
}
function timer(initTime, percentComplete) {
	if (percentComplete == 100) {
		return "00:00:00";
	} else {
	    // 剩余时间 = 已进行时间 / 已完成百分比 * 剩下百分比
	    var ts = ((new Date()) - initTime) / percentComplete * (100 - percentComplete); //计算剩余的毫秒数      
		var dd = parseInt(ts / 1000 / 60 / 60 / 24, 10);//计算剩余的天数      
		var hh = parseInt(ts / 1000 / 60 / 60 % 24, 10);//计算剩余的小时数      
		var mm = parseInt(ts / 1000 / 60 % 60, 10);//计算剩余的分钟数      
		var ss = parseInt(ts / 1000 % 60, 10);//计算剩余的秒数      
		dd = checkTime(dd);      
		hh = checkTime(hh);      
		mm = checkTime(mm);      
		ss = checkTime(ss);
		
		return hh + ":" + mm + ":" + ss;
	}  
}
//修改课件
function updateResource(pid){
	$.fancybox.open({
		href:"update_exam.jsp?paper_id="+pid,
		type:'iframe',
		width:600,
		height:450,
		padding:0,
		margin:0,
		closeBtn:true,
		iframe:{scrolling:'no',preload:false}
	});
}
function checkTime(i) {
	if (i < 10) {        
		i = "0" + i;        
	}        
	return i;        
} 
</script>
 </head> 
 <body class="headerQuestion">
 <%@ include file="/mooc/public/top.jsp"%>
	<form id="praxesForm" name="praxesForm">
		<input type="hidden" id="paper_id" name="paper_id" value=""/>
		<input type="hidden" id="folder_id" name="folder_id" value=""/>
		<input type="hidden" id="folder_name" name="folder_name" value=""/>
		<input type="hidden" id="folder_code" name="folder_code" value=""/>
		<input type="hidden" id="resourc_type" name="resourc_type" value=""/>
		<input type="hidden" id="qry_subject_id" name="qry_subject_id" value=""/>
		<input type="hidden" id="qry_grade_code" name="qry_grade_code" value=""/>
		<input type="hidden" id="qry_folder_code" name="qry_folder_code" value=""/>
	</form>
  <div class="header"> 
   <div class="topNav"> 
    <div class="miniBox cf"> 
    <!--  <p class="info fr"> <a href="http://www.tizi.com/teacher/paper/preview/2" class="cBtnNormal topNavcBtnNormal">课件管理</a></p>--> 
     <p class="fl"> <span class="title">课件管理</span></p>
    </div> 
   </div> 
  </div> 
  <!--主要部分开始--> 
  <div class="wrap"> 
   <div class="mainContent fl"> 
    <div class="mainWrap main-wrap"> 
     <div style="height: 863.667px;" class="mainContainer" pagename="paper_question" status="paper_question"> 
      <div class="child-content fn-clear"> 
       <div style="height: 864px; overflow-y: scroll;" class="content"> 
        <div class="content-wrap"> 
         <div class="filter-box"> 
          <div style="float:left;width:70%;">
          <ul class="fn-clear posr fliterBoxSelect"> 
           <li class="firstSelect"><a href="javascript:void(0)" id="auto-id-all" onclick="loadCoursewareData(20,$('#folder_code').val(),$('#folder_id').val(),$('#folder_name').val(),1)" data-qtype="0">全部课件</a></li> 
           <li><a href="javascript:void(0)" data-qtype="3" id="auto-id-video" onclick="loadCoursewareData(2010,$('#folder_code').val(),$('#folder_id').val(),$('#folder_name').val(),1)">视频</a></li> 
           <li><a href="javascript:void(0)" data-qtype="18" id="auto-id-p" onclick="loadCoursewareData(2030,$('#folder_code').val(),$('#folder_id').val(),$('#folder_name').val(),1)">PPT</a></li> 
           <li><a href="javascript:void(0)" data-qtype="19" id="auto-id-f" onclick="loadCoursewareData(2020,$('#folder_code').val(),$('#folder_id').val(),$('#folder_name').val(),1)">Flash</a></li>
           <li><a href="javascript:void(0)" data-qtype="20" id="auto-id-w" onclick="loadCoursewareData(2040,$('#folder_code').val(),$('#folder_id').val(),$('#folder_name').val(),1)">Word</a></li>
          </ul> 
          </div>  
           <div class="fr">
           <p> 
                                             排序：
              <select name="orderby" id="orderby" onchange="loadCoursewareData($('#resourc_type').val(),$('#folder_code').val(),$('#folder_id').val(),$('#folder_name').val(),1)"  style="width:150px;height:30px;font-size:12px;">
			     <option value="t7.create_date">按最新创建时间</option>
			     <option value="readcount">按浏览次数</option>
			   </select>
              <a href="javascript:void(0)" onclick="coursewareUpload()" class="cBtnNormal topNavcBtnNormal">新增</a>
           </p>
           </div>
         </div> 
         <div id="coursewareList" style="display:none;margin-top:40px;">
            <!-- 上传 -->
         </div>
         <div class="question-list" style="width:auto;height:auto;margin-top:40px;">
               <ul class="videoList" style="margin:0px;padding:0px;">
			  	</ul>
         </div>
         <!-- 分页begin -->
             <%@ include file="/mooc/public/pageinfo.jsp"%>
          <!-- 分页end -->
        </div> 
       </div> 
       <div style="height: 853.667px;" class="child-menu"> 
        <div style="height: 863.667px;" class="drag-line">
         <a style="margin-top: 431.833px;" class="drag-handle"></a>
        </div>
        <div class="type-box fn-clear">
         <div class="type-list"> 
          <!-- 不能删除的a开始 --> 
          <a class="current-type" style="display:none" data-cselect="59" data-sid="2"><span>初中数学知识点库</span></a> 
          <!-- 不能删除的a结束 --> 
          <select id="semester" onchange="bc_cfg.foldercode=this.value;getBookChildFolder()"></select>  
         </div> 
        </div>
        <div style="height: 829px; overflow-y: scroll;" class="tree-list"> 
         <ul style="display:block;" class="numList">
         </ul> 
        </div> 
       </div> 
      </div> 
     </div> 
    </div> 
   </div> 
   <div class="leftBar fl"> 
    <div style="height: 863.667px;" class="mainMenu main-menu fl"> 
     <!--个人中心开始--> 
     <div class="memberCenter"> 
      <!-- 当前学科开始 --> 
      <!-- class="subject-chose" data-subject="2" 这2个不能删,很多都依赖这2个 --> 
      <div class="currentSubject subject-chose posr" data-subject="2"> 
       <div class="hd"> 
         <h2 class="fl"><span id="subjectnm"></span></h2>         
         <div class="changeSubjet fr" style="visibility:hidden">
          更换学科
         <div class="bd undis"> 
          <dl>
           <dd id="subjectnmList"> 
           </dd> 
          </dl> 
          <dl>
         </div> 
        </div> 
       </div>
      </div> 
      <!-- 当前学科结束 --> 
      <!--左侧选择学科下拉开始--> 
      <ul> 
       <li> <a class="active" href="/mooc/teacher/main.jsp">课件管理</a> </li>
       <li> <a href="/mooc/teacher/paper_new.jsp">试卷组卷</a> </li> 
       <li> <a href="/mooc/teacher/exam_manager.jsp">试卷管理</a> </li>
       <li> <a href="/mooc/teacher/resource_manager.jsp">我的资源包</a> </li>  
      </ul> 
      <div style="display: block;" class="preview-btn"> 
       
      </div> 
      
     </div> 
     <!--个人中心结束--> 
    </div> 
    <div style="height: 863.667px;" class="hidden-arrow fl">
     <a style="margin-top: 423.833px;" class="hide-menu"></a>
    </div> 
   </div> 
  </div> 
  
  <!--主要部分结束--> 
  <div id="errormsg" style="display:none"></div> 
  <div class="footer" id="footer" style="display:none"> 
   <span>2015 </span> 
  </div>
    <div id="loadingMask" class="loading-mask" style="z-index: 10051; display: none;"></div> 
    <div id="loadingPb" class="u-loading f-cb" style="z-index: 10052; display: none;"></div> 
	<script type="text/javascript" src="/images/mooc/question/common.js"></script>
	<script type="text/javascript" src="/images/mooc/question/paper.js"></script>
 </body>
</html>