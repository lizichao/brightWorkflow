//工具包相对路径
var toolsPath = "";

//初始化xmlHtmlReqeust
var request = false;
createRequest();

//是否自动设置长宽
var autosizeable = true;
//宽度和页面的差值(autosizeable == true时有效)	
var xDiffValue = 20;
//高度和页面的差值(autosizeable == true时有效)
var yDiffValue = 280;
var yBackupValue = -1;
//全局按钮css
var buttonCss = "nyan4";
//defaultTableCss
defaultTableStyle = "";


//保存本页Chart的xml
var xmlcontentContenxt = [];
var tableStrContenxt = [];
//保存本页的 table
var tableContentContenxt = [];
var myChartContenxt = [];//null
var fileNameContenxt = [];
//是否显示menu bar
var isDisplayMenuBar = true;
var isDisplayHistory = true;
var isDisplayChangeStyle= true;
var isDisplayExport= true;
var selectOption = [{"key":"MSLine","value":"线图"},
                    {"key":"Pie2D","value":"饼图"},
	                {"key":"MSColumn3D","value":"三维柱图"},
	                {"key":"ScrollColumn2D","value":"可滚动柱图"},
	                {"key":"ScrollStackedColumn2D","value":"可滚动柱图(堆叠)"},
	                {"key":"StackedColumn3D","value":"三维柱图(堆叠)"},
	                {"key":"ScrollArea2D","value":"区域图"}];
//chartdivid 页面上放置图表div的id
//tabledivid 页面上放置表格div的id
//chartType  图表的类型，目前的类型如下:
//width 	 flash的宽
//height     flash的高
//dataURL    请求数据的URL
//params     需要定义的参数
//jsfunction 当点击时执行的js
//params     格式为key=value
function fillChart(chartdivid,tabledivid,chartType,width,height,dataURL,inputIds,jsfunction,tableCss,tableStyle){
	var paramsStr = "";
	if(inputIds != null ){
		paramsStr = inputToParam(inputIds);
	}
	fill(chartdivid,tabledivid,chartType,width,height,dataURL,paramsStr,jsfunction,tableCss,tableStyle,true,true);
}
function fill(chartdivid,tabledivid,chartType,width,height,dataURL,paramsStr,jsfunction,tableCss,tableStyle,isSaveHistory,isRefreshXml){
		if(isSaveHistory != false){
			saveHistory(arguments,chartdivid);
		}
		tableStyle += defaultTableStyle;
		var chartContentDiv = chartdivid;
		var tableContentDiv = tabledivid;
		//根据menu bar 设置当前参数
		//图表类型切换
	 	if(isDisplayMenuBar){
	 		if(!createMenubar(chartdivid,width,chartType,tabledivid) && isDisplayChangeStyle){
	 			chartType = document.getElementById(chartdivid + "_selector").value;
	 		}
	 		chartContentDiv  = chartdivid + "_content";
	 		tableContentDiv  = tabledivid + "_content";
	 	}
	 	
		//处理长宽
		if(autosizeable){
			//width = document.body.clientWidth - xDiffValue;
			width = document.body.clientWidth - xDiffValue;
			if(tabledivid != ''){
				height = 675 - yDiffValue;
			}
			else{
				height = 675 - yDiffValue + 100;
			}
		}
		//参数处理
 		var url = dataURL;
 		url = url + "&charttype=" + chartType;
 		if(jsfunction != null && jsfunction != ""){
 			url = url + "&jsfunction=" + jsfunction;
 		}
 		if(tableCss != null && tableCss != ""){
 			url = url + "&tableCss=" + tableCss;
 		}
 		if(tableStyle != null && tableStyle != ""){
 			url = url + "&tableStyle=" + tableStyle;
 		}
 		url += "&toolsPath=" + toolsPath;
 		url += "&titleSubfix=" + getTitleSubfix();
 		url += paramsStr;
 		chartTypePath = toolsPath + chartType + ".swf" + "?ChartNoDataText=抱歉，无可供显示的数据！";
 		//图表flash的id默认为chartdivid + '_swf'
 		
 		myChartContenxt[chartdivid] = new FusionCharts(chartTypePath, chartdivid + '_swf', width,height, "0", "1");
 		if(isRefreshXml == true){
	        xmlcontentContenxt[chartdivid] = "";
	        tableStrContenxt[chartdivid] = "";
	        var xmlHttp = request;
	        xmlHttp.open("GET", encodeURI(url), false);
	        xmlHttp.onreadystatechange = function()
	        {
	       	  if(xmlHttp.readyState==4)
	       	    {
	       		  var requestContent =  xmlHttp.responseText;
	       		  var str = requestContent.split('{xml|table}');
	       		  xmlcontentContenxt[chartdivid] = str[0];
	       		  tableStrContenxt[chartdivid] = str[1];
	       		  fileNameContenxt[chartdivid] = str[2];
	       	    }
	       	};
        xmlHttp.send(null);
 		}
 		myChartContenxt[chartdivid].setTransparent(false);
        //var wintest = window.open();
        //wintest.document.write("<textarea>" + xmlcontent+"</textarea>");
 		myChartContenxt[chartdivid].setDataXML(xmlcontentContenxt[chartdivid]);
 		myChartContenxt[chartdivid].render(chartContentDiv);
        //给table加横向滚动条
        var tablehtml = "<div style='overflow-x:auto;width:" + width + ";'>" + tableStrContenxt[chartdivid] + "</div>";
        if(tabledivid != ''){
        	document.getElementById(tableContentDiv).innerHTML = tablehtml;
        	tableContentContenxt[chartdivid] = tableStrContenxt[chartdivid];
        }
        else{
        	tableContentContenxt[chartdivid] = "";
        }
        
        if(isDisplayMenuBar && isDisplayHistory){
        	refreshButtonState(chartdivid);
        }
    }
	
	/*
	 * 根据传入的input输入框的id数组，将input输入框的内容转换成 &iptId1=iptValue1&iptId2=iptValue2&...的形式
	 */
	function inputToParam(inputIds){
		var params =  "";
		for(var i = 0; i < inputIds.length;i++){
			var inputName = inputIds[i];
			params += getInputValue(inputName);
		}
		return params;
	}
	
	function getInputValue(inputName){
		var params = "";
		if(inputName != null && inputName != ''){
			//如果check:前缀将checkBox的值转换成true or false
			//如果select:前缀的将下拉框查找key
			//如果value:前缀，将直接将value的值添加
			//默认为input.value取值
			if(inputName.indexOf('check:') != -1){
				var inputNameStr = inputName.substr(6);
				var inputValue = "";
				var input = document.getElementById(inputNameStr);
				if(input == null) return "";
				if(input.checked == true){
					inputValue = "true";
				}
				else{
					inputValue = "false";
				}
				params += "&" + inputNameStr + "=" + inputValue;
			}
			else if(inputName.indexOf('select:') != -1){
				var inputNameStr = inputName.substr(7);
				var inputValue = "";
				var selector = document.getElementById(inputNameStr);
				if(selector == null) return "";
				inputValue = selector.value; 
				params += "&" + inputNameStr + "=" + inputValue;
			}
			else if(inputName.indexOf('value:') != -1){
				var valueStr = inputName.substr(6);
				params += "&" + valueStr;
			}
			else{
				var input = document.getElementById(inputName);
				if(input == null) return "";
				var inputValue = input.value;
				params += "&" + inputName + "=" + inputValue;
			}
		}
		return params;
		
	}

	//参数，url,pramName1,pramValue1,pramName2,pramValue2,pramName3,pramValue3 ...
	function openNewWithForm(url,isClose){
		var newWin = window.open('','','toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,copyhistory=yes,left=100,top=100,width=800,height=500');
		newWin.document.writeln("请稍候...");
		newWin.document.writeln("<form id='form1' action='" + url + "' method='post'>");
		for(var i=1; i<arguments.length-1; i=i+2){
			newWin.document.writeln("<input type='hidden' name='" + arguments[i] + "' value=\"" + arguments[i+1] + "\"/>");
		}
		newWin.document.writeln("</form>");
		newWin.document.writeln("<script type='text/javascript'>");
		newWin.document.writeln("document.getElementById('form1').submit();");
		newWin.document.writeln("<\/script>"); 
		return false;
	}
	//支持多浏览器的XMLHttpRequest
 	function createRequest() {
 		  try {
 		   	request = new XMLHttpRequest();
 		  } catch (trymicrosoft) {
 		    try {
 		      request = new ActiveXObject("Msxml2.XMLHTTP");
 		    } catch (othermicrosoft) {
 		      try {
 		        request = new ActiveXObject("Microsoft.XMLHTTP");
 		      } catch (failed) {
 		        request = false;
 		      }
 		    }
 		  }
 		  if (!request)
 		    alert("不支持此浏览器请求数据，请使用IE或firefox!");
 	}
 	
 	//保存flash为图片
 	function saveAsImage(divId){
		  var chartToPrint1 = getChartFromId(divId + "_swf");
		  chartToPrint1.saveAsImage();
		  return false;
	}
 	//保存表格为excel
 	function saveAsExcel(chartdivid){
 		document.getElementById('filename_' + chartdivid).value = fileNameContenxt[chartdivid];
		document.getElementById('content_' + chartdivid).value = tableContentContenxt[chartdivid];//document.getElementById('divContent_' + chartdivid).innerHTML;
		document.getElementById('form1_' + chartdivid).submit();
 	}
 	//工具条
 	function createMenubar(chartdivid,width,selectedValue,tabledivid){
 		if(document.getElementById(chartdivid + "_menuBar") != null){
 			return false;
 		}
 		
 		var charDiv = document.getElementById(chartdivid);
    	var div1 = document.createElement('DIV');
    	div1.id = chartdivid + "_menuBar";
    	div1.style.textAlign = "right";
    	div1.style.width = "100%";
    	var menuBarHtml = "";
    	var selectorHtml = createTypeSelector(chartdivid,selectedValue);
    	var historyButton = createHistoryButton(chartdivid);
    	var exportButton = createExportButton(chartdivid,buttonCss);
    	menuBarHtml += "<form id='form1_" + chartdivid + "' action='" + toolsPath + "saveToExecl.jsp" + "' method='post'>";
    	menuBarHtml += "<input type='hidden' name='content' id='content_" + chartdivid + "' />";
    	menuBarHtml += "<input type='hidden' name='filename' id='filename_" + chartdivid + "' />";
    	//menuBarHtml += "<div style='display:none;' id='divContent_" + chartdivid + "'>" + tableContentContenxt[chartdivid] + "</div>";
    	menuBarHtml += "</form>";
    	menuBarHtml += "<table width='100px'><tr>";
    	if(isDisplayHistory){
	    	menuBarHtml += "<td style='border-width:0;'>";
	    	menuBarHtml += historyButton;//历史按钮
	    	menuBarHtml += "</td>";
 		}
    	if(isDisplayChangeStyle){
	    	menuBarHtml += "<td style='border-width:0;'>";
	    	menuBarHtml += selectorHtml;//类型选择
	    	menuBarHtml += "</td>";
    	}
    	if(isDisplayExport){
	    	menuBarHtml += "<td style='border-width:0;'>";
	    	menuBarHtml += exportButton;//导出按钮
	    	menuBarHtml += "</td>";
    	}
    	menuBarHtml += "</tr></table>"
    	div1.innerHTML = menuBarHtml;
        charDiv.insertBefore(div1,charDiv.firstChild);
        
        var div2 = document.createElement('DIV');
        div2.id = chartdivid + "_content";
        div2.style.width = width;
        charDiv.appendChild(div2);
        
        //如果table内容为空，则不显示导出按钮
        if(tabledivid != ""){
        	var tableDiv = document.getElementById(tabledivid);
	        var div3 = document.createElement('DIV');
	        div3.id = chartdivid + "_tableBar";
	        div3.style.width = "100%";	
	        div3.style.textAlign = "right";
	        div3.innerHTML = createExportTableButton(buttonCss,chartdivid);
	        tableDiv.insertBefore(div3,tableDiv.firstChild);
	        
	        var div4 = document.createElement('DIV');
	        div4.id = tabledivid + "_content";
	        div4.style.width = width;
	        tableDiv.appendChild(div4);
        }
        return true;
 	}
 	function createExportTableButton(cssName,chartdivid){
 		var buttonHtml = "";
 		buttonHtml += "<input type='button' class='" + cssName + "' onclick=\"saveAsExcel('" + chartdivid+ "');\" value='导出表格'/>";
 		return buttonHtml;
 	}
 	function createExportButton(chartdivid,cssName){
 		var buttonHtml = "";
 		buttonHtml += "<input type='button'  class='" + cssName + "' onclick=\"saveAsImage('" + chartdivid+ "');\" value='导出图片' />";
 		return buttonHtml;
 	}
 	//类型选择
 	function createTypeSelector(chartdivid,selectedValue){
 		var selectorHtml = "";
 		selectorHtml += "<select onchange=\"dofillChart(currentPram['" + chartdivid + "']);return false;\" id='" + chartdivid +"_selector'>";
 		for(var i=0;i<selectOption.length;i++)
 		{
 			selectorHtml += "<option ";
 			if(selectedValue == selectOption[i].key){
 				selectorHtml += "selected='selected'";
 			}
 			selectorHtml += "value='" + selectOption[i].key;
 			selectorHtml +="'>";	
 			selectorHtml += selectOption[i].value;
 			selectorHtml += "</option>"
 		}
 		selectorHtml += "</select>";
 		return selectorHtml;
 	}
	//历史相关
 	function createHistoryButton(chartdivid){
 		var selectorHtml = "";
 		selectorHtml += "<a href='#'  onclick=\"chartPreHistory('" + chartdivid + "');return false;\"><img id='preButtonImg_" + chartdivid + "' src='" + toolsPath +  "/image/leftbt.gif' border=0/></a>";
 		selectorHtml += "&nbsp;&nbsp;&nbsp;&nbsp;"
 		selectorHtml += "<a href='#'  onclick=\"chartNextHistory('" + chartdivid + "');return false;\"><img id='nextButtonImg_" + chartdivid + "' src='" + toolsPath +  "/image/rightbt.gif' border=0/></a>";
 		return selectorHtml;
 	}
 	var currentPram = [];
 	var prePram = [];
 	var nextPram = [];
 	function saveHistory(arguments,chartdivid){
 		if(prePram[chartdivid] == null){
 			prePram[chartdivid] = [];
 		}
 		if(nextPram[chartdivid] == null){
 			nextPram[chartdivid] = [];
 		}
 		if(!currentPram[chartdivid]){
 			currentPram[chartdivid] = arguments;
 		}
 		else{
 			prePram[chartdivid].push(currentPram[chartdivid]);
 			currentPram[chartdivid] = arguments;
 		}
 		nextPram[chartdivid] = new Array();
 	}
 	function chartPreHistory(chartdivid){
 		var pram = prePram[chartdivid].pop();
 		if(!pram){
	 		
 		}
 		else{
 			nextPram[chartdivid].push(currentPram[chartdivid]);
	 		currentPram[chartdivid] = pram;
	 		dofillChart(pram);
 		}
 	}
 	function chartNextHistory(chartdivid){
 		var pram = nextPram[chartdivid].pop();
 		if(!pram){
 		}
 		else{
	 		prePram[chartdivid].push(currentPram[chartdivid]);
	 		currentPram[chartdivid] = pram;
	 		dofillChart(pram);
 		}
 	}
 	function dofillChart(arguments){
 		if(!arguments){
 		}
 		else{
 			fill(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5],arguments[6],arguments[7],arguments[8],arguments[9],false,true);
 		}
 	}
 	//刷新按钮
 	function refreshButtonState(chartdivid){
 		var b1 = document.getElementById("preButtonImg_" + chartdivid);
 		var b2 = document.getElementById("nextButtonImg_" + chartdivid);
 		if(prePram[chartdivid].length == 0){
 			b1.src = toolsPath +  "image/leftbt_blank.gif";
 		}
 		else{
 			b1.src= toolsPath +  "image/leftbt.gif";
 		}
 		if(nextPram[chartdivid].length == 0){
 			b2.src= toolsPath +  "image/rightbt_blank.gif";
 		}
 		else{
 			b2.src= toolsPath +  "image/rightbt.gif";
 		}
 	}
 	
 	//设置标题的后缀，需要增加后缀的页面在页面上重写此方法。
	function getTitleSubfix(){
		var fix = "(";
		var dwName = document.getElementById("dwName");
		if(document.getElementById("dwName") == null) {
			return "";
		} 
		var name = dwName.value;
		if(name.length > 30){
			name = name.substr(0,30) + "...";
		}
		if(name ==""){
			fix +="全部)";
		}else {
			fix +=name+")";
		}
 		return fix;
 	}
 	var temwidth=0;
 	if(document.body !=null)
 		temwidth=document.body.clientWidth;
 	window.onresize = function d(){
 		if(temwidth==0)
 			temwidth=document.body.clientWidth;
 		if(document.body.clientWidth != temwidth){
 			temwidth=document.body.clientWidth;
 		for(var charId in currentPram){
 			fill(currentPram[charId][0],currentPram[charId][1],currentPram[charId][2],currentPram[charId][3],currentPram[charId][4],currentPram[charId][5],currentPram[charId][6],currentPram[charId][7],currentPram[charId][8],currentPram[charId][9],false,false);
 		}
 	}
 		
 	};
 	
 	