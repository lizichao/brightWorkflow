String.prototype.trim = function()
{
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.len=function(){ 
	return this.replace(/[^\x00-\xff]/g,"**").length; 
}

var ua = navigator.userAgent.toLowerCase();
var iecheck = function(r){return r.test(ua);};
var isIE = !iecheck(/opera/) && iecheck(/msie/);
function createXmlHttp() {
	var objXMLHttp;
	if (window.XMLHttpRequest) {
		objXMLHttp = new XMLHttpRequest();
	}
	else {
		var MSXML = ['MSXML2.XMLHTTP.5.0', 'MSXML2.XMLHTTP.4.0', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP', 'Microsoft.XMLHTTP'];
		for(var n = 0; n < MSXML.length; n ++) {
			try {
				objXMLHttp = new ActiveXObject(MSXML[n]);        
				break;
			} catch(e) {
			}
		}
	 }
	 return objXMLHttp;
}
function getResultByURL(url,funcname,async) {
	var xmlhttp=createXmlHttp();
	if(url.indexOf("?") > 0){
		url += "&r=" + Math.random();
	}else{
		url += "?r=" + Math.random();
	}
	xmlhttp.open("post",url,async==true);
	if(async==true){
		xmlhttp.onreadystatechange= function () {
			if(xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				funcname(xmlhttp);
			}
		};
	}
	xmlhttp.send();
	if(async!=true && xmlhttp.status == 200){
		funcname(xmlhttp);
	}
}
function createXMLDoc(){
    var xmlDoc;
    if (isIE) {
        xmlDoc = new ActiveXObject("MSXML2.DOMDocument.3.0");
        xmlDoc.async = false;
        while (xmlDoc.readyState != 4) {};
    }
    else{
    	xmlDoc = document.implementation.createDocument("", "", null);
    }
    return xmlDoc;
}
function getElementValue(item){
    if(isIE){
        return item.text;
    }else{
        return item.childNodes.length==0?"":item.childNodes[0].nodeValue;
    }
}
if ( ! isIE){
      XMLDocument.prototype.loadXML = function(xmlString)
     {
        var childNodes = this.childNodes;
        for (var i = childNodes.length - 1; i >= 0; i--)
            this.removeChild(childNodes[i]);

        var dp = new DOMParser();
        var newDOM = dp.parseFromString(xmlString, "text/xml");
        var newElt = this.importNode(newDOM.documentElement, true);
        this.appendChild(newElt);
     };

    // check for XPath implementation
    if( document.implementation.hasFeature("XPath", "3.0"))
     {
       // prototying the XMLDocument
        XMLDocument.prototype.selectNodes = function(cXPathString, xNode)
        {
          if( !xNode ) { xNode = this; }
          var oNSResolver = this.createNSResolver(this.documentElement)
          var aItems = this.evaluate(cXPathString, xNode, oNSResolver,
                        XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null)
          var aResult = [];
          aResult.length = aItems.snapshotLength;
          for( var i = 0; i < aItems.snapshotLength; i++)
           {
              aResult[i] =   aItems.snapshotItem(i);
           }
           aResult.item=function(index){
            return aResult[index]
           }
          return aResult;
        }

       // prototying the Element
        Element.prototype.selectNodes = function(cXPathString)
        {
          if(this.ownerDocument.selectNodes)
           {
             return this.ownerDocument.selectNodes(cXPathString, this);
           }
          else{throw "For XML Elements Only";}
        }
        // prototying the XMLDocument
        XMLDocument.prototype.selectSingleNode = function(cXPathString, xNode)
        {
          if( !xNode ) { xNode = this; }
          var xItems = this.selectNodes(cXPathString, xNode);
          if( xItems.length > 0 )
           {
             return xItems[0];
           }
          else
           {
             return null;
           }
        }

       // prototying the Element
        Element.prototype.selectSingleNode = function(cXPathString)
        {
          if(this.ownerDocument.selectSingleNode)
           {
             return this.ownerDocument.selectSingleNode(cXPathString, this);
           }
          else{throw "For XML Elements Only";}
        }
     }
}
function hideIt(obj){
	if(typeof(obj)=="undefined" || obj==null) return;
	//if(isIE){
		obj.style.display='none';
	//}else{
		//obj.style.visibility='hidden';
	//}
}
function showIt(obj){
	if(typeof(obj)=="undefined" || obj==null) return;
	//if(isIE){
		obj.style.display='';
	//}else{
		//obj.style.visibility='visible';
	//}
}
function cancelEvent()
{
	if(isIE)
		event.returnValue=false;
	else
		event.preventDefault();
}
function focusIt(obj){
	try{
	  	obj.focus();
	  	return;
	}catch(e){
	  	return;
	}
}

function createNewFormElement(inputForm, elementName, elementValue,_type){
	var newElement;
	if(isIE){
		newElement = document.createElement("<input name="+elementName+">");
		newElement.type= _type; 
		newElement.value =elementValue;
	}
	else{
		newElement = document.createElement("input");
		newElement.setAttribute("name",elementName);
		newElement.setAttribute("type",_type);
		newElement.setAttribute("value",elementValue);
	}
	inputForm.appendChild(newElement);
	return newElement;
}

function fillForm(_form,xmlstring,hfield){
	if(typeof(deviceNo) != 'undefined'){
		var ccno=_form['c_cardno'];
		if(ccno)
		{
			_form.c_cardno.value=deviceNo;
		}
		else
		{
			createNewFormElement(_form,'c_cardno',deviceNo,'hidden');
		}
	}
	var xstr=xmlstring||'';
	if(''==xstr) return;
	var xmlDoc=createXMLDoc();
	xmlDoc.loadXML(xmlstring);
	var data=xmlDoc.childNodes[0];
	var cmap={};
	var fields=_form.elements;
	for(var i =0;i<fields.length;i++){
		var field=fields[i];
		var name=field.name;
		cmap[name]=name;
		switch(field.type){
			case "text":
			case "password":
			case "textarea":
			case "hidden":
			case "select-one":
				//if duplicate names found
				if(Object.prototype.toString.call(_form[name])=='[object Array]'){// is array
					var xfields=data.selectNodes(name);
					if(xfields==null){
						continue;
					}
					var nfields=new Array();
					var pointer=0;
					for(var j=0;j<fields.length;j++){
						if(fields[j].name==name){
							fields[j].value=getElementValue(xfields.item(pointer++));
						}
					}
				}
				else {// is single field
					var xfield=data.selectSingleNode(name);
					if(xfield!=null){
						field.value=getElementValue(xfield);
					}
				}
				break;
			case "select-multiple":
				var xfields=data.selectNodes(name);
				for(var j=0;j<field.options.length;j++){
					field.options[j].selected=false;
					var xfield=data.selectSingleNode(name+"[.='"+field.options[j].value+"']");
					if(xfield!=null){
						field.options[j].selected=true;
					}
				}
				break;
			case "checkbox":
			case "radio":
				field.checked=false;
				var xfield=data.selectSingleNode(name+"[.='"+field.value+"']");
				if(xfield!=null){
					field.checked=true;
				}
				break;
			
		}
	}
	//隐藏域
	if(hfield==true){
		var cs=data.childNodes;
		for(var i=0; i<cs.length;i++){
			var n=cs[i].nodeName;
			var t=getElementValue(cs[i]);
			if(!cmap[n])
				createNewFormElement(_form,n,t,'hidden');
		}
	}
};

function chkError(ele,msg){
	alert(msg);
	focusIt(ele);
};
var customtypes = {
"num":"无效数字",
"plus":"无效正整数",
"int":"无效整数",
"email":"无效Email地址",
"link":"无效链接地址",
"password":"只能使用字母、下划线与数字",
"name":"只能使用字母、下划线与数字",
"date":"无效日期",
"idcard":"无效身份证号",
"zipcode":"无效邮编",
"mobile":"无效手机号",
"phone":"无效电话"
};
function check_email(s){
  var re = /^\w+@(\w)+((.(\w)+)+)?$/i;
  return re.test(s);
}
function check_num(s){
  var re = /^(\+|-)?\d+(.\d+)?$/i;
  return re.test(s);
}
function check_int(s){
  var re = /^(\+|-)?\d+$/i;
  return re.test(s);
}
function check_plus(s) {
  var re = /^[1-9]\d*$/i
  return re.test(s);
}
function check_link(s){
  var re = /^(http|mailto|ftp|https|telnet):\/{2}/i;
  return re.test(s);
}
function check_password(s){
  var re = /^\w+$/i;
  return re.test(s);
}
function check_zipcode(s){
	return /^[1-9]\d{5}$/.test(val);
}
function check_name(s){
  return check_password(s);
}
function check_mobile(s){
	return /^0*(13|15|18)\d{9}$/.test(s);
}
function check_phone(s){
	return /^(\d{3,4}-)?\d{7,8}(-\d{3,4})?$/.test(s);
}
function check_idcard(s){
	var len=s.length;
	if(len==15)return /^([1-9]\d{5})(\d{2})(0?[1-9]|1[0-2])((0?[1-9])|((1|2)[0-9])|30|31)(\d{3})$/.test(s);
	if(len==18)return /^([1-9]\d{5})(18|19|20)(\d{2})(0?[1-9]|1[0-2])((0?[1-9])|((1|2)[0-9])|30|31)(\d{3})(\d|X)$/.test(s);
	return false;
}
function check_date(DateString,dl){
    if (DateString==null) return false;
    var Dilimeter = '-';
    if(dl)
    	Dilimeter=dl;
	if (Dilimeter.indexOf("/")>0)
	{
		Dilimeter="/";
	}
    var tempy='';
    var tempm='';
    var tempd='';
	var tempH="";
	var tempM="";
	var tempS="";
	var tempymd="";
	var temphms="";
    var tempArray;
    if ((DateString.length<8 && DateString.length>19) ||(DateString.length==8 && ""!=Dilimeter)){
        return false;
	}
	if (DateString.indexOf(" ")>0)
	{
		temp=DateString.split(" ");
		tempymd=temp[0];
		temphms=temp[1];
	}
	else
	{
		tempymd=DateString;
		temphms="00:00:00";
	}
	if(""==Dilimeter){
		tempArray=[];
		tempArray[0]=tempymd.substring(0,4);
		tempArray[1]=tempymd.substring(4,6);
		tempArray[2]=tempymd.substring(6);
	}
    else{
    	tempArray = tempymd.split(Dilimeter);
    }
    if (tempArray.length!=3) {
        return false;
	}
    if (tempArray[0].length==4)
    {
        tempy = tempArray[0];
        tempd = tempArray[2];
		tempm = tempArray[1];
    }
    else
    {
        tempy = tempArray[2];
        tempd = tempArray[1];
		tempm = tempArray[0];
    }
	tempArray = temphms.split(":");
	if (tempArray.length>3  || tempArray.length<2) {
		return false;
	}
	switch (tempArray.length) {
		case 2:
			tempH=tempArray[0];
			tempM=tempArray[1];
			tempS="00";
			break;
		case 3:
			tempH=tempArray[0];
			tempM=tempArray[1];
			tempS=tempArray[2];
			break;
	}
	var tDateString = tempy + '/'+tempm + '/'+tempd+' '+tempH+":"+tempM+":"+tempS;
	var tempDate = new Date(tDateString);
	if (isNaN(tempDate)) {
		return false;
	}
    if ((tempDate.getFullYear().toString()==tempy) && (tempDate.getMonth()==parseInt(tempm,10)-1) && (tempDate.getDate()==parseInt(tempd,10)) && (tempDate.getHours().toString()==parseInt(tempH,10)) && (tempDate.getMinutes().toString()==parseInt(tempM,10)) && (tempDate.getSeconds().toString()==parseInt(tempS,10)))
    {
        return true;
    }
    else
    {
        return false;
    }
}
function checkForm(_form){
	var fields=_form.elements;
	for(var i =0;i<fields.length;i++)
	{
		var ele = fields[i];
		if(!checkFormFld(ele))
			return false;
	}
	return true;
};
function checkFormFld(ele){
	var ct = ele.getAttribute("customtype");
	var req = ele.getAttribute("req");
	var dn = ele.getAttribute("displayname");
	var minlen = ele.getAttribute("minlen");
	var scriptstr = ele.getAttribute("scriptstr");
	var ronly = ele.getAttribute("readonly");
	var dable = ele.getAttribute("disabled");
	if(ronly || dable)
		return true;
	
	switch(ele.type)
	{
		case "text":
		case "password":
		case "hidden":
		case "select-one":
		case "textarea":
	    if (null == dn)
	    {
	    	dn = ele.name;
	    }
	    dn = "“" + dn + "”";
	    var nr = new String(ele.value);

	    if ((null != minlen)&&(nr.trim().length < minlen))
	    {
	        chkError(ele,dn + "的长度必须大于" + minlen);
	        return false;
	    }
	    
	    if ("1" == req)
	    {
	    	if (nr.length < 1)
	    	{
	        	chkError(ele,dn + "为必输项");
	        	return false;
	      	}
	    }
	    if("0" == req) return true;
	    if ((("" == req)||(null == ct)) &&(ele.value == "")) return true;
	
	    //检查自定义类型
	    if(null != scriptstr && scriptstr.trim().length>0)
	    {
			var reg = RegExp('^' + scriptstr + '$',"g");
			if(!reg.test(ele.value.replace(/\r\n/g,"")))
			{
				chkError(ele,dn + "格式不正确");
				return false;
			}
		}
		//检查自定义类型
		if(null !=ct && ct.trim().length>0){
			var ctmsg=customtypes[ct];
			var chkfunc;
			if (ctmsg){
				chkfunc="var rtn = check_" + ct + "(ele.value);";
			}
			else{
				chkfunc="var rtn="+ct;
			}
			try
			{
				window.eval(chkfunc);
		        if (rtn!=true){
					chkError(ele,dn + (ctmsg||rtn));
					return false;
				}
			}catch(e){alert("error:"+e.message);}
		}

		if ("textarea" == ele.type)
		{
			
		}
		break;
	}
	return true;
};