//������������Ľ���
function focusIt(obj){
  try{
    var ele = mtb;
  }catch(e){
    try{
	  obj.focus();
	  return;
	}catch(e){
	  return;
	}
  }
  for (var i=0;i<ele.length;i++){
    swH(i);
	try{
	  obj.focus();
	}catch(e){
	  continue;
	}
	break;
  }
  try{
    obj.focus();
  }catch(e){
    return false;
  }return true;
}
var customtypes = new Array(
"num",
"plus",
"int",
"email",
"link",
"password",
"name",
"date"
);

var custommessage = new Array(
"����һ����Ч�����֡�",
"����һ����Ч����������",
"����һ����Ч��������",
"����һ����Ч�ĵ����ʼ���ַ��",
"����һ����Ч�����ӣ���ȷ�������������ĵ�ַ������http://www.cnlist.com��",
"����һ����Ч�����룬����ֻ��ʹ����ĸ���»��������֣����ܰ���������ո�",
"����һ����Ч�����ƣ�����ֻ��ʹ����ĸ���»��������֣����ܰ���������ո�",
"����һ����Ч�����ڡ�"
);

function checkForm(oForm){
  window.event.returnValue = false;

  var eles = oForm.all.tags("input");
  for (var i=0;i<eles.length;i++){
    var ele = eles[i];
    if(!checkele(ele)) return false;
  }

  eles = oForm.all.tags("textarea");
  for (var i=0;i<eles.length;i++){
    var ele = eles[i];
    if(!checkele(ele)) return false;
  }
  window.event.returnValue = true;
  return true;
}

function checkele(ele)
{
	var ct = ele.getAttribute("customtype");
    var req = ele.getAttribute("req");
    var dn = ele.getAttribute("displayname");
    if (null == dn){
      dn = ele.name;
    }
    dn = "��" + dn + "��";
    
    var nr = new String(ele.value);
    if ("1" == req){
      if (nr.length < 1){
        alert(dn + "������ʡ�ԣ����������롣");
        //ele.focus();
        focusIt(ele);
        return false;
      }
    }
    if ((("" == req)||(null == ct)) &&(ele.value == "")) return true;

    //����Զ�������
    for (var j=0;j<customtypes.length;j++){
      if (ct == customtypes[j]){
        eval("var rtn = check_" + customtypes[j] + "(ele.value);");
        if (!rtn){
          alert(dn + custommessage[j]);
          //ele.focus();
          focusIt(ele);
          return false;
        }
      }
    }
    return true;
}

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

function check_name(s){
  return check_password(s);
}
function check_date(DateString){
    if (DateString==null) return false;
    if (Dilimeter=='' || Dilimeter==null)
    var Dilimeter = '-';
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
    if (DateString.length<8 && DateString.length>19) {
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
    tempArray = tempymd.split(Dilimeter);
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

function numericInput(n)
{
	var n; if(!n) n = 0;
	var kc = event.keyCode;
	//alert(event.srcElement.onselectstart)
	if((kc<48||kc>57)&&(kc<96||kc>105)&&kc!=190&&kc!=110&&kc!=144&&kc!=8&&kc!=13&&kc!=46 &&kc!=37&&kc!=38&&kc!=39&&kc!=40&&kc!=16&&kc!=35&&kc!=36) {
		event.returnValue = false; event.keyCode = 0;
	}
else {
		if((kc>47 && kc<58)||(kc>95&&kc<106))
		{
			var obv = event.srcElement.value;

			if((obv.indexOf(".")>=0&&(kc==190||kc==110))
				|| (n>0&&obv.indexOf(".")>0&&(obv.length-obv.indexOf(".")-1)>=n)){
				event.returnValue = false;	event.keyCode = 0;
			}
		}
	}
}
//������������
//i ����λ����������Ϊ0
//d С��λ��������С��λΪ0������-1
//s ������+|-��ȱʡΪ''
function numinput(i,d,s)
{
	var d; if(!d) d = 0;
	var i; if(!i) i = 0;
	var s; if(!s) s = "";
	
	var kc = event.keyCode;
	if((kc == 0)||(kc == 8)||(kc == 9)||(kc == 13)||(kc == 27)) {
		return;
	}	
	
	var tes = '/^';
	if(""==s)		tes = tes+'(\\+|-)?';
	else if("+"==s)	tes = tes+"\\+?";
	else if("-"==s)	tes = tes+"\\-";
	
	tes=tes+"(";
	if(i>0)
	{
		for(j=0;j<i;j++)
		{
			tes = tes+"\\d?";
		}
	}
	else
	{
		tes = tes+"\\d+";
	}
	if(d>0)
	{
		tes = tes+"(\\.";
		for(j=0;j<d;j++)
		{
			tes=tes+"\\d?";
		}
		tes=tes+")?";
	}
	else if(d<0)
	{
		tes=tes+"(\\.(\\d+)?)?";
	}
	tes=tes+")?$/i";
		
	var obv = event.srcElement.value;
	var oRange = document.selection.createRange();
	var oldtxt = oRange.text;
	oRange.text = String.fromCharCode(event.keyCode);
	
	if("false"==""+eval(tes+".test(\""+event.srcElement.value+"\")"))
	{
		if('' != oldtxt)
		{
			document.selection.clear();
			oRange.text=oldtxt;
		}
		else
		{
			event.srcElement.value = obv;				
		}		
	}
	else
	{
		if('' != oldtxt)
		{
			oRange.collapse(true);
			document.selection.clear();
			oRange.text = String.fromCharCode(event.keyCode);
		}
	}	
	event.returnValue = false;	event.keyCode = 0;
}