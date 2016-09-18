function JspParamUtil(){};
//JspParamUtil.paramData('c_identitytype','Y','Identity_Type',[['','全部']],[['style','font-size:11px;width:85px'],['onChange','changeVal()']])
JspParamUtil.paramData = function (s, v, e, b, c){
  var a;
  if (_sYs_paRm_liSt) {
      var d = _sYs_paRm_liSt[e];
      if (d) {
          a = d.items ? d.items: []
      }
  }
  if(b) a = b.concat(a);
  var s = JspParamUtil.genSelect(s,a,c,v)
  return s
}

JspParamUtil.genSelect = function (selectName, opts, params, val)
{
    var s = "<select id='" + selectName + "' name='" + selectName + "'";
    if(params){
    	for (var i = 0; i < params.length; i++) {
        var opt = params[i];
        s += " " + opt[0] + "='" + opt[1]+"'";
    	}
    }
    s += " >";
    for (var i = 0; i < opts.length; i++) {
        var opt = opts[i];
        s += "<option value='" + opt[0] + "'";
        if(opt[0] == val) s += " selected";
        s += ">" + opt[1] + "</option>";
    }
    s += "</select>";
    return s;
}

//JspParamUtil.paramVal('Identity_Type','00','无')
JspParamUtil.paramVal = function (e, v, b){
  var a;
  if (_sYs_paRm_liSt) {
      var d = _sYs_paRm_liSt[e];
      if (d) {
          a = d.items ? d.items: []
      }
  }  
  var s="";
  if(a){
	  for (var i = 0; i < a.length; i++) {
	      var opt = a[i];
	      if(opt[0] == v) s = opt[1];
	  }
	}
  if(b) s = (s=="")?b:s; 
  return s;
}

//保留二位小数，不足二位补零
JspParamUtil.changeTwoDecimal = function (x){
	if(x){
		var f_x = parseFloat(x);
		if (isNaN(f_x)){
			f_x = x;
		}
		var f_x = Math.round(x*100)/100;
		var s_x = f_x.toString();
		var pos_decimal = s_x.indexOf('.');
		if (pos_decimal < 0)
		{
			pos_decimal = s_x.length;
			s_x += '.';
		}
		while (s_x.length <= pos_decimal + 2)
		{
			s_x += '0';
		}
		return s_x;
	} else {
		return "";
	}
}

//金额转换
JspParamUtil.rmbMoneyRenderer = function (val, meta, rec, rowIdx, colIdx, ds){
	if(val){
    return '￥'+JspParamUtil.changeTwoDecimal(val);
  } else {
		return '';
	}
}

//鼠标浮动提示
JspParamUtil.tipValue = function (val, meta, rec, rowIdx, colIdx, ds){
  if(val){
		return '<div ext:qtitle="" ext:qtip="' + val + '">'+ val +'</div>';
	} else {
		return '';
	}
}

//卡号处理，中间八位*号代替
JspParamUtil.cardnoRenderer = function (val, meta, rec, rowIdx, colIdx, ds){
  if(val){
  	if(val.length > 4){
  		return val.substr(0,4)+"********"+val.substr(val.length-4, val.length);
  	}
		return val;
	} else {
		return '';
	}
}