<%@ page contentType="text/html; charset=GBK" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>修改流程</title>
<link href="/workflow/css/style.css" rel="stylesheet">
<link href="/workflow/js/checkbox/red.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>
<script src="/workflow/js/checkbox/icheck.min.js"></script>
<script type="text/javascript" src="/js/jquery/common.js"></script>
<script src="/js/jquery/jsrender.js" type="text/javascript"></script>
<script type="text/javascript" src="/workflow/common/jquery-ui-1.9.2.min.js"></script>


<script type="text/javascript">
function submitUser(){
	var dealWay = $('input[name="wayRadio"]:checked').val();
	var submitUsers = [];//提交选择的人
	var nextPrincipalUsers ="";
	$("#to option").each(function(i) {
		 if(i==0 && dealWay =='1'){
			 nextPrincipalUsers = $(this).attr("value");
		 }
		 submitUsers.push($(this).attr("value"));
	});
	
	var addUsers = [];//要加的人
	var removeUsers = [];//要减的人
	var unChangeUsers = [];//不变的人
	
	   for (var i=0;i<selectdUser.length;i++){//selectUser当时选择的会签人，会签部门比提交选择的部门多的是要减的人
		   var eachUserId = selectdUser[i].userId;
		    var len=0;
		    for (var j=0;j<submitUsers.length;j++){
			   if(eachUserId !=submitUsers[j]){
				   len++;
			   }else{
				   break;
			   }
			}
		    if(len == submitUsers.length){
		    	removeUsers.push(eachUserId);
		    }
		}
	
	
	   for (var i=0;i<submitUsers.length;i++){//selectUser当时选择的会签人，提交选择的部门比会签部门多的是要加的人
		   var len=0;
		    for (var j=0;j<selectdUser.length;j++){
			   if(submitUsers[i] !=selectdUser[j].userId){
				   len++;
			   }else{
				   break;
			   }
			}
		    if(len == selectdUser.length){
		    	addUsers.push(submitUsers[i]);
		    }
		}
	   
	   for (var i=0;i<selectdUser.length;i++){//selectUser当时选择的会签人，会签部门比提交选择的部门多的是要减的人
		    var eachUserId = selectdUser[i].userId;
		    for (var j=0;j<submitUsers.length;j++){
			   if(eachUserId ==submitUsers[j]){
				   unChangeUsers.push(eachUserId);
				   break;
			   }
			}
		}

	var bcReq = new BcRequest('workflow','componentAction','editCounterInfo');
	bcReq.setExtraPs({
		"processInstanceId":processInstanceId,
		"processDefKey":processDefKey,
		"processDefName":processDefName,
		"taskDefKey":taskDefKey,
		"taskId":taskId,
		"dealWay":dealWay,
		'addUser':addUsers.join(","),
		'removeUser':removeUsers.join(","),
		'unChangeUser':unChangeUsers.join(","),
		'majorUser' :nextPrincipalUsers,
		'submitUsers' :submitUsers.join(","),
		'businessKey' :businessKey,
		'multiKind' :multiKind
	});
	bcReq.setSuccFn(function(data,status){
		 window.parent.location.reload();
		 iframeObj.closeDialog();
	});
	bcReq.postData();
}

var iframeObj = window.parent.frames[0];
var paramObject = window.dialogVal() || iframeObj.dialogVal();

var processInstanceId = paramObject.processInstanceId;
var taskId = paramObject.taskId;
var taskDefKey = paramObject.taskDefKey;
var processDefKey = paramObject.processDefKey;
var processDefName = paramObject.processDefName;
var businessKey = paramObject.businessKey;
var multiKind = paramObject.multiKind;

var userData = paramObject.infoData;
var selectdUser = userData.Data;
var optionalUser = userData.Data2;
$(document).ready(function(){
	   for (var i=0;i<optionalUser.length;i++){
		      var configHandlerEach = optionalUser[i];
		      $("#from").append( "<option value=\'"+configHandlerEach.userId+"\'>"+configHandlerEach.userName+"</option>" )
		}
	   var majorUser = [];
	   var assistUser = [];
	   for (var i=0;i<selectdUser.length;i++){
		   var selectdUserEach = selectdUser[i];
		   var isMajor = selectdUserEach.isMajor;
		   if(isMajor == '1'){
			   majorUser.push(selectdUserEach);
		   }else{
			   assistUser.push(selectdUserEach)
		   }
	   }
	   if(majorUser.length==0){
		   $('input[name="wayRadio"][value=2]').attr('checked','checked');
	   }
	 
	   
	   var newSelectdUser = majorUser.concat(assistUser);
	   for (var i=0;i<newSelectdUser.length;i++){
		      var selectdUserEach = newSelectdUser[i];
		      var delegateUserId = selectdUserEach.delegateUserId;
		      var isMajor = selectdUserEach.isMajor;
		      var toName = '';
		      if(delegateUserId){
		    	  toName = selectdUserEach.delegateUserName+"(代"+selectdUserEach.userName+")";
		      }else{
		    	  toName = selectdUserEach.userName;
		      }
		      $("#to").append( "<option value=\'"+selectdUserEach.userId+"\'>"+toName+"</option>" )
		}
})


 $(function(){  
		$("#from").dblclick(function(){
			var $option = $("#from option:selected");
			$option.appendTo("#to");
		});
		
		$("#to").dblclick(function(){
			var $option = $("#from option:selected");
			$option.appendTo("#from");
		});
        //选择一项  
        $("#addOne").click(function(){  
            $("#from option:selected").clone().appendTo("#to");  
            $("#from option:selected").remove();  
        });  
  
        //选择全部  
        $("#addAll").click(function(){  
            $("#from option").clone().appendTo("#to");  
            $("#from option").remove();  
        });  
          
        //移除一项  
        $("#removeOne").click(function(){  
            $("#to option:selected").clone().appendTo("#from");  
            $("#to option:selected").remove();  
        });  
  
        //移除全部  
        $("#removeAll").click(function(){  
            $("#to option").clone().appendTo("#from");  
            $("#to option").remove();  
        });  
  
        //移至顶部  
        $("#Top").click(function(){  
            var allOpts = $("#to option");  
            var selected = $("#to option:selected");  
            if(selected.get(0).index!=0){  
                for(i=0;i<selected.length;i++){  
                   var item = $(selected.get(i));  
                   var top = $(allOpts.get(0));  
                   item.insertBefore(top);  
                }  
            }  
        });  
  
        //上移一行  
        $("#Up").click(function(){  
            var selected = $("#to option:selected");  
            if(selected.get(0).index!=0){  
                selected.each(function(){  
                    $(this).prev().before($(this));  
                });  
            }  
        });  
  
        //下移一行  
        $("#Down").click(function(){  
            var allOpts = $("#to option");  
            var selected = $("#to option:selected");  
            if(selected.get(selected.length-1).index!=allOpts.length-1){  
                for(i=selected.length-1;i>=0;i--){  
                   var item = $(selected.get(i));  
                   item.insertAfter(item.next());  
                }  
            }  
        });  
  
        //移至底部  
        $("#Buttom").click(function(){  
            var allOpts = $("#to option");  
            var selected = $("#to option:selected");  
            if(selected.get(selected.length-1).index!=allOpts.length-1){  
                for(i=selected.length-1;i>=0;i--){  
                   var item = $(selected.get(i));  
                   var buttom = $(allOpts.get(length-1));  
                   item.insertAfter(buttom);  
                }  
            }  
        });  
    });  
</script>
</head>
<body>

<div class="hi-box hi-xzclr" style="height: 468px;">   
<div class="hi-main">

<div class="hi-hd">
<h3><i class="ico-xzclr"></i>选择人员</h3> 
</div>

<div class="hi-xzcl">
<div class="hi-tims hi-tims-1">
  <select  name="from" id="from" size="11" multiple="multiple">
  </select> 
</div>

<div class="hi-yd"><ul>
	  <li id="addAll">>></li>
	  <li id="addOne">></li>
	  <li id="removeOne"><</li>
	  <li id="removeAll"><<</li>
  </ul>
</div>

<div class="hi-tims hi-tims-2">
<select name="to" id="to" size="11" multiple="multiple">
</select> 
</div>

<div class="hi-db">
<ul>
  <li id="Top">置顶</li>
  <li id="Up">上移</li>
  <li id="Down">下移</li>
  <li id="Buttom">置底</li>
</ul></div>

</div>

<div class="hi-page">
<div id="dealWay" class="hi-btn" style="text-align:left">
    <span>办理方式</span>&nbsp;
    <input type="radio" style="height:10px;margin:0" id='wayRadio' name='wayRadio' value='1' checked="checked"/>主办&nbsp;
    <input type="radio" style="height:10px;margin:0" id='wayRadio' name='wayRadio' value='2' onclick=""/>分办&nbsp;
 <!--  <input type="radio" style="height:10px;margin:0" id='wayRadio' name='wayRadio' value='3' onclick=""/> 阅知&nbsp;&nbsp; --> 
</div> 
<div class="hi-btn"><input name="" type="button" value="提 交" class=" hi-radius3"  onclick="submitUser()" /></div>  
</div>


</div>
</div>


<script type="text/javascript">
$(".hi-tims-1 option:odd").addClass("odd-row");
$(".hi-tims-2 option:odd").addClass("odd-row");
</script>
</body>
</html>