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
	var submitDepts = [];//提交选择的部门
	var nextPrincipalDepartments ="";
	$("#to option").each(function(i) {
		 if(i==0 && dealWay =='1'){
			 nextPrincipalDepartments = $(this).attr("value");
		 }
		submitDepts.push($(this).attr("value"));
	});
	
	var addDepts = [];//要加的部门
	var removeDepts = [];//要减的部门
	
	   for (var i=0;i<selectdDept.length;i++){//selectDept当时选择的会签部门，会签部门比提交选择的部门多的是要减的部门
		   var eachDeptId = selectdDept[i].deptId;
		    var len=0;
		    for (var j=0;j<submitDepts.length;j++){
			   if(eachDeptId !=submitDepts[j]){
				   len++;
			   }else{
				   break;
			   }
			}
		    if(len == submitDepts.length){
		    	removeDepts.push(eachDeptId);
		    }
		}
	
	
	   for (var i=0;i<submitDepts.length;i++){//selectDept当时选择的会签部门，提交选择的部门比会签部门多的是要加的部门
		   var len=0;
		    for (var j=0;j<selectdDept.length;j++){
			   if(submitDepts[i] !=selectdDept[j].deptId){
				   len++;
			   }else{
				   break;
			   }
			}
		    if(len == selectdDept.length){
		    	addDepts.push(submitDepts[i]);
		    }
		}

	var bcReq = new BcRequest('workflow','receiveFileAction','editCounterInfo');
	bcReq.setExtraPs({
		"processInstanceId":processInstanceId,
		"processDefKey":processDefKey,
		"taskDefKey":taskDefKey,
		"dealWay":dealWay,
		'addDept':addDepts.join(","),
		'removeDept':removeDepts.join(","),
		'majorDept' :nextPrincipalDepartments,
		'submitDepts' :submitDepts.join(","),
		'businessKey' :businessKey
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
var businessKey = paramObject.businessKey;

var deptData = paramObject.deptData;
var selectdDept = deptData.Data;
var optionalDept = deptData.Data2;
$(document).ready(function(){
	 $("#departmentDiv").css("height","452px")
	   for (var i=0;i<optionalDept.length;i++){
		      var configHandlerEach = optionalDept[i];
		      $("#from").append( "<option value=\'"+configHandlerEach.deptId+"\'>"+configHandlerEach.deptName+"</option>" )
		}
	   
	   for (var i=0;i<selectdDept.length;i++){
		      var configHandlerEach = selectdDept[i];
		      $("#to").append( "<option value=\'"+configHandlerEach.deptId+"\'>"+configHandlerEach.deptName+"</option>" )
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