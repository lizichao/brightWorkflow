
////cookie 操作
/**
* 保存
* @param {Object} name
* @param {Object} value
*/
saveCookie = function(name, value){
    //保存一个月
    var saveDate = new Date((new Date()).getTime() + 30 * 24 * 3600 * 1000);
    //console.log(saveDate.toUTCString());
    Cookies.set(name, value, saveDate);
}
/**
* 获取
* @param {Object} name
*/
getCookie = function(name){
    return Cookies.get(name);
}
/**
* 清除
* @param {Object} name
*/
clearCookie = function(name){
    Cookies.clear(name);
}
/// Cookies 对象
var Cookies = {};
Cookies.set = function(name, value){
    var argv = arguments;
    var argc = arguments.length;
    var expires = (argc > 2) ? argv[2] : null;
    var path = (argc > 3) ? argv[3] : '/';
    var domain = (argc > 4) ? argv[4] : null;
    var secure = (argc > 5) ? argv[5] : false;
    document.cookie = name + "=" + encodeURIComponent(value) +
    ((expires == null) ? "" : ("; expires=" + expires.toUTCString())) +
    ((path == null) ? "" : ("; path=" + path)) +
    ((domain == null) ? "" : ("; domain=" + domain)) +
    ((secure == true) ? "; secure" : "");
};

Cookies.get = function(name){
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;
    var j = 0;
    while (i < clen) {
        j = i + alen;
        if (document.cookie.substring(i, j) == arg) 
            return Cookies.getCookieVal(j);
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0) 
            break;
    }
    return null;
};

Cookies.clear = function(name){
    if (Cookies.get(name)) {
        document.cookie = name + "=" +
        "; expires=Thu, 01-Jan-70 00:00:01 GMT";
    }
};

Cookies.getCookieVal = function(offset){
    var endstr = document.cookie.indexOf(";", offset);
    if (endstr == -1) {
        endstr = document.cookie.length;
    }
    return decodeURIComponent(document.cookie.substring(offset, endstr));
};

var RESP_BLANK='$$_blank$$';
Ext.chart.Chart.CHART_URL = '/ext/resources/charts.swf';
Ext.BLANK_IMAGE_URL = '/ext/resources/images/default/s.gif';

var themeValue = getCookie('sysTheme');
if (themeValue)
{
	Ext.util.CSS.swapStyleSheet('xtheme','/ext/resources/css/'+themeValue+'.css');
}
else{
	themeValue = "xtheme-default";
    Ext.util.CSS.swapStyleSheet('xtheme','/ext/resources/css/xtheme-default.css');
}



LoginPanel=function(config){
	var loginform=this;
	//this.succFn=Ext.emptyFn;	
	LoginPanel.superclass.constructor.call(this,Ext.apply(config||{},{
		//applyTo: 'login-panel',
		labelWidth : 75,
		id : 'loginform',
		shadow: true,
		bodyStyle : 'padding:5px 5px 0',
		width : 'auto',
		defaults : {
			width : 150
		},
		defaultType : 'textfield',
		
		lSuccFn : function(){
			JrafSession.load();
			if(typeof loginform.succFn == 'function')
				loginform.succFn.call(this);
		},

		items : [{
					id : 'sysmsg',
					xtype: 'label',
					anchor:'99%',
					style: 'color:red;'
				},{
					fieldLabel : '用户名',
					name : 'usercode',
					allowBlank : false,
					emptyText:"请输入用户名",
					blankText : '请输入用户名'
				}, {
					fieldLabel : '密　码',
					name : 'userpwd',
					inputType : 'password',
					allowBlank : false
				},{
					name:'captchaCode',
					fieldLabel:'验证码',
					width:150,
					blankText : '验证码不能为空',
					style: 'font-size:14px;background:url(/platform/public/img.jsg?t='+new Date()+'); background-repeat: no-repeat; background-position: 90px 1px;center right no-repeat;'
				}],

		buttons : [{
					text : '登　录',
					type: 'submit',
					iconCls: "house",
					handler : function() {
						var jr = new JrafRequest('pcmc','sm_permission','login');
                        jr.setForm(loginform);
                        jr.setSuccFn(loginform.lSuccFn);
                        jr.postData();
					}
				  },{
					text : '重　置',
					iconCls: "page-refresh",
					handler : function() {
						loginform.getForm().reset();
					}
				  }]
	}));
};
Ext.extend(LoginPanel, Ext.FormPanel,{});

function exportGridDataToExcel(_girdId){
    var _grid = Ext.getCmp(_girdId);
    try {   
        var xls = new ActiveXObject("Excel.Application");   
    }    
    catch (e) {   
        alert("要打印该表，您必须安装Excel电子表格软件，同时浏览器须使用“ActiveX 控件”，您的浏览器须允许执行控件。 请点击【帮助】了解浏览器设置方法！");   
        //return "";   
    }  

	var _gridTitle = arguments[1]?arguments[1]:"统计数据导出";
	
    xls.visible = true; //设置excel为可见    
    var xlBook = xls.Workbooks.Add;   
    var xlSheet = xlBook.Worksheets(1);   
       
    var cm = _grid.getColumnModel();   
    var colCount = cm.getColumnCount();   
    var temp_obj = [];   
    //只下载没有隐藏的列(isHidden()为true表示隐藏,其他都为显示)    
    //临时数组,存放所有当前显示列的下标    
    for (i = 1; i < colCount; i++) {   
        if (cm.isHidden(i) == true) {   
        }   
        else {   
            temp_obj.push(i);   
        }   
    } 
	//设置标题
	
	var colNameArray = ['0','A','B','C','D','E','F','G','H','I','J','K','L','M','N','0','P','Q','R','S','T','U','V','W','X','Y','Z'];
	var mergeCol = "A1:"+colNameArray[temp_obj.length]+"1";	
	xlSheet.Range(mergeCol).MergeCells=true;//合并单元格
	xlSheet.Rows("1:1").RowHeight=40; 

	xlSheet.Cells(1,1).HorizontalAlignment=3;//设置左右对齐方式
	xlSheet.Cells(1,1).VerticalAlignment=2;//设置上下对齐方式 
	xlSheet.Cells(1,1).Font.Size=20;
    xlSheet.Cells(1,1).Font.Name="黑体";
	xlSheet.Cells(1,1).Font.Bold=true;
	xlSheet.Cells(1,1).Value=_gridTitle;

    for (i = 1; i <= temp_obj.length; i++) {   
        //显示列的列标题    
        xlSheet.Cells(2, i).Value = cm.getColumnHeader(temp_obj[i - 1]);   
    }   
    var _store = _grid.getStore();   
    var recordCount = _store.getCount();   
    var view = _grid.getView();   
    for (i = 1; i <= recordCount; i++) {   
        for (j = 1; j <= temp_obj.length; j++) {   
            //EXCEL数据从第二行开始,故row = i + 1;    
            xlSheet.Cells(i + 2, j).Value = view.getCell(i - 1, temp_obj[j - 1]).innerText;   
        }   
    }
	//增加汇总行
	recordCount =  recordCount+2;
	xlSheet.Cells(recordCount+1, 1).Value = "合计:";
    xlSheet.Cells(recordCount+1, 1).HorizontalAlignment=4;//设置左右对齐方式

    for (i = 2; i <= temp_obj.length; i++) {   
        //显示列的列标题 
		var colName = colNameArray[i];         
		var cellText = "=SUM("+colName+"3:"+colName+recordCount.toString()+")";		
        xlSheet.Cells(recordCount+1, i).Value = cellText;   
    }
	//增加导出日期
    xlSheet.Cells(recordCount+2, temp_obj.length-1).Value = "导出日期:"; 
	var dt = new Date();
    xlSheet.Cells(recordCount+2, temp_obj.length).Value = dt.getFullYear().toString()+'年'+(dt.getMonth()+1).toString()+'月'+dt.getDate().toString()+'日'+dt.getHours().toString()+'时'+dt.getMinutes().toString()+'分'; 

    xlSheet.Columns.AutoFit;   
    xls.ActiveWindow.Zoom = 100   
    xls.UserControl = true; //很重要,不能省略,不然会出问题 意思是excel交由用户控制    
    xls = null;   
    xlBook = null;   
    xlSheet = null; 
}

/*********************日历组件部分**************************** begin */  
var dateArray = new Array();  
var printDateArray = function(){
	var content;
    if(dateArray != null && dateArray != "") {
		content =  dateArray.join(',');  
	}
    Ext.getCmp('calendarContent').setValue(content);
	dateArray.length = 0;
	var _calendarWin = Ext.getCmp('CalendarWin');
	if (_calendarWin)
	{
	   _calendarWin.hide(); 
	}
};  
Ext.MyDatePicker = Ext.extend(Ext.DatePicker, {  
	todayText : '确定',  
	okText : ' 确定 ',  
	cancelText : '取消',  
	todayTip : '{0} (Spacebar)',  
	minText : 'This date is before the minimum date',   
	maxText : 'This date is after the maximum date',  
	format : 'Y-m-d',  
	disabledDaysText : 'Disabled',  
	disabledDatesText : 'Disabled',  
	dayNames : Date.dayNames,  
	nextText : 'Next Month (Control+Right)',  
	prevText : 'Previous Month (Control+Left)',  
	monthYearText : 'Choose a month (Control+Up/Down to move years)',  
	startDay : 0,  
	showToday : true,  
	  
	initComponent : function(){  
		Ext.MyDatePicker.superclass.initComponent.call(this);  
		this.value = this.value ?  
				 this.value.clearTime() : new Date().clearTime();  
		this.addEvents(  
			'select'  
		);  
		if(this.handler){  
			this.on('select', this.handler,  this.scope || this);  
		}  
		this.initDisabledDays();  
	},  
	onRender : function(container, position){  
		var m = [  
			 '<table cellspacing="0" width="100%">',  
				'<tr>',  
					'<td class="x-date-left">',  
						'<a href="#" title="', this.prevText ,'"> </a>',  
					'</td>',  
					'<td class="x-date-middle" align="center"></td>',  
					'<td class="x-date-right">',  
						'<a href="#" title="', this.nextText ,'"> </a>',  
					'</td>',  
				'</tr>',  
				'<tr>',  
					'<td colspan="3">',  
						'<table class="x-date-inner" cellspacing="0">',  
			                  '<thead>', 
								'<tr>'  
				],  
				dn = this.dayNames,  
				i;  
		for(i = 0; i < 7; i++){  
			var d = this.startDay+i;  
			if(d > 6){  
				d = d-7;  
			}  
			m.push('<td><div align="center"><span >', '星期'+dn[d].substr(0,1), '</span></div></td>');//星期标题  
		}  
		m[m.length] = '</tr></thead><tr>';  
		for(i = 0; i < 42; i++) {  
			if(i % 7 === 0 && i !== 0){  
				m[m.length] = '</tr><tr>';  
			}  
			m[m.length] = '<td height="50px" align="center" style="border:1 solid #F0F0F0"><h hidefocus="on" class="x-date-date" tabIndex="1" ><em><span></span></em></h></td>';  
		}  
		m.push('</tr></tbody></table></td></tr>',  
				this.showToday ? '<tr><td colspan="3" class="x-date-bottom" align="center"></td></tr>' : '',  
				'</table><div class="x-date-mp"></div>');  
  
		var el = document.createElement('div');  
		el.className = 'x-date-picker';  
		el.innerHTML = m.join('');  
  
		container.dom.insertBefore(el, position);  
  
		this.el = Ext.get(el);  
		this.eventEl = Ext.get(el.firstChild);  
  
		this.prevRepeater = new Ext.util.ClickRepeater(this.el.child('td.x-date-left a'), {  
			handler: this.showPrevMonth,  
			scope: this,  
			preventDefault:true,  
			stopDefault:true  
		});  
  
		this.nextRepeater = new Ext.util.ClickRepeater(this.el.child('td.x-date-right a'), {  
			handler: this.showNextMonth,  
			scope: this,  
			preventDefault:true,  
			stopDefault:true  
		});  
  
		this.monthPicker = this.el.down('div.x-date-mp');  
		this.monthPicker.enableDisplayMode('block');  
  
		this.keyNav = new Ext.KeyNav(this.eventEl, {  
			'left' : function(e){  
				if(e.ctrlKey){  
					this.showPrevMonth();  
				}else{  
					this.update(this.activeDate.add('d', -1));  
				}  
			},  
  
			'right' : function(e){  
				if(e.ctrlKey){  
					this.showNextMonth();  
				}else{  
					this.update(this.activeDate.add('d', 1));  
				}  
			},  
  
			'up' : function(e){  
				if(e.ctrlKey){  
					this.showNextYear();  
				}else{  
					this.update(this.activeDate.add('d', -7));  
				}  
			},  
  
			'down' : function(e){  
				if(e.ctrlKey){  
					this.showPrevYear();  
				}else{  
					this.update(this.activeDate.add('d', 7));  
				}  
			},  
  
			'pageUp' : function(e){  
				this.showNextMonth();  
			},  
  
			'pageDown' : function(e){  
				this.showPrevMonth();  
			},  
  
			'enter' : function(e){  
				e.stopPropagation();  
				return true;  
			},  
  
			scope : this  
		});  
  
		this.el.unselectable();  
  
		this.cells = this.el.select('table.x-date-inner tbody td');  
		this.textNodes = this.el.query('table.x-date-inner tbody span');  
  
		this.mbtn = new Ext.Button({  
			text: ' ',  
			tooltip: this.monthYearText,  
			renderTo: this.el.child('td.x-date-middle', true)  
		});  
		this.mbtn.el.child('em').addClass('x-btn-arrow');  
  
		if(this.showToday){  
			this.todayKeyListener = this.eventEl.addKeyListener(Ext.EventObject.SPACE, this.selectToday,  this);  
			var today = (new Date()).dateFormat(this.format);  
			this.todayBtn = new Ext.Button({  
				renderTo: this.el.child('td.x-date-bottom', true),  
				text: String.format(this.todayText, today),  
				tooltip: String.format(this.todayTip, today),  
				handler: this.selectToday,  
				scope: this  
			});  
		}  
		this.mon(this.eventEl, 'mousewheel', this.handleMouseWheel, this);  
		this.mon(this.eventEl, 'click', this.handleDateClick,  this);  
		this.mon(this.mbtn, 'click', this.showMonthPicker, this);  
		this.onEnable(true);  
	},  
	selectToday : function(){  
		if(this.todayBtn && !this.todayBtn.disabled){  
			printDateArray();   //返回结果  
		}  
	},  
	handleDateClick : function(e, t){  
		//this.fireEvent('select', this, this.value);  
	},  
	update : function(date, forceRefresh){  
		var vd = this.activeDate, vis = this.isVisible();  
		this.activeDate = date;  
		if(!forceRefresh && vd && this.el){  
			var t = date.getTime();  
			if(vd.getMonth() == date.getMonth() && vd.getFullYear() == date.getFullYear()){  
				this.cells.removeClass('x-date-selected');  
				this.cells.each(function(c){  
				   if(c.dom.firstChild.dateValue == t){  
					   c.addClass('x-date-selected');  
					   if(vis){  
						   Ext.fly(c.dom.firstChild).focus(50);  
					   }  
					   return false;  
				   }  
				});  
				return;  
			}  
		}  
		var days = date.getDaysInMonth();  
		var firstOfMonth = date.getFirstDateOfMonth();  
		var startingPos = firstOfMonth.getDay()-this.startDay;  
  
		if(startingPos <= this.startDay){  
			startingPos += 7;  
		}  
  
		var pm = date.add('mo', -1);  
		var prevStart = pm.getDaysInMonth()-startingPos;  
  
		var cells = this.cells.elements;  
		var textEls = this.textNodes;  
		days += startingPos;  
  
		// convert everything to numbers so it's fast  
		var day = 86400000;  
		var d = (new Date(pm.getFullYear(), pm.getMonth(), prevStart)).clearTime();  
		var today = new Date().clearTime().getTime();  
		var sel = date.clearTime().getTime();  
		var min = this.minDate ? this.minDate.clearTime() : Number.NEGATIVE_INFINITY;  
		var max = this.maxDate ? this.maxDate.clearTime() : Number.POSITIVE_INFINITY;  
		var ddMatch = this.disabledDatesRE;  
		var ddText = this.disabledDatesText;  
		var ddays = this.disabledDays ? this.disabledDays.join('') : false;  
		var ddaysText = this.disabledDaysText;  
		var format = this.format;  
  
		if(this.showToday){  
			var td = new Date().clearTime();  
			var disable = (td < min || td > max ||  
				(ddMatch && format && ddMatch.test(td.dateFormat(format))) ||  
				(ddays && ddays.indexOf(td.getDay()) != -1));  
  
			if(!this.disabled){  
				this.todayBtn.setDisabled(disable);  
				this.todayKeyListener[disable ? 'disable' : 'enable']();  
			}  
		}  
  
		var setCellClass = function(cal, cell){  
			cell.title = '';  
			var t = d.getTime();  
			cell.firstChild.dateValue = t;  
			if(t == today){  
				cell.className += ' x-date-today';  
				cell.title = cal.todayText;  
			}  
			if(t == sel){  
				cell.className += ' x-date-selected';  
				if(vis){  
					Ext.fly(cell.firstChild).focus(50);  
				}  
			}  
			// disabling  
			if(t < min) {  
				cell.className = ' x-date-disabled';  
				cell.title = cal.minText;  
				return;  
			}  
			if(t > max) {  
				cell.className = ' x-date-disabled';  
				cell.title = cal.maxText;  
				return;  
			}  
			if(ddays){  
				if(ddays.indexOf(d.getDay()) != -1){  
					cell.title = ddaysText;  
					cell.className = ' x-date-disabled';  
				}  
			}  
			if(ddMatch && format){  
				var fvalue = d.dateFormat(format);  
				if(ddMatch.test(fvalue)){  
					cell.title = ddText.replace('%0', fvalue);  
					cell.className = ' x-date-disabled';  
				}  
			}  
		};  
  
		var i = 0;  
		for(; i < startingPos; i++) {  
			var tempId = "fruit"+i;  
			d.setDate(d.getDate()+1);  
			var fvalue = d.dateFormat(format);  
			textEls[i].innerHTML = '<div align="center"><font size ="2" color = "#8B8378" >'+(++prevStart)+'</font>'+'<br><input type="checkbox" id ="'+tempId+'"  value ="'+fvalue+'"><font size="2" color = "#8B8378"  >排课</font></div>';  
			cells[i].className = 'x-date-prevday';  
			setCellClass(this, cells[i]);  
			Ext.get(tempId).on('click',function(e,f){  
				if(Ext.get(f.id).dom.checked){  
					dateArray[f.id.substring(5,f.id.length)] = f.value;  
				}else{  
					dateArray[f.id.substring(5,f.id.length)] = "";  
				}  
			});   
		}  
		for(; i < days; i++){  
			var tempId = "fruit"+i;  
			var intDay = i - startingPos + 1;  
			d.setDate(d.getDate()+1);  
			var fvalue = d.dateFormat(format);  
			textEls[i].innerHTML = '<div align="center"><font size ="2">'+(intDay)+'</font>'+'<br><input type="checkbox" id ="'+tempId+'"  value ="'+fvalue+'"><font size="2">排课</font></div>';  
			cells[i].className = 'x-date-active';  
			setCellClass(this, cells[i]);  
			Ext.get(tempId).on('click',function(e,f){     
				if(Ext.get(f.id).dom.checked){  
					dateArray[f.id.substring(5,f.id.length)] = f.value;  
				}else{  
					dateArray[f.id.substring(5,f.id.length)] = "";  
				}  
			});    
		}  
		var extraDays = 0;  
		for(; i < 42; i++) {  
			 var tempId = "fruit"+i;  
			 d.setDate(d.getDate()+1);  
			 var fvalue = d.dateFormat(format);  
			 textEls[i].innerHTML = '<div align="center"><font size ="2" color = "#8B8378">'+(++extraDays)+'</font>'+'<br><input type="checkbox" id ="'+tempId+'"  value ="'+fvalue+'"><font size="2" color ="#8B8378">排课</font></div>';  
			 Ext.get(tempId).on('click',function(e,f){     
				if(Ext.get(f.id).dom.checked){  
					dateArray[f.id.substring(5,f.id.length)] = f.value;  
				}else{  
					dateArray[f.id.substring(5,f.id.length)] = "";  
				}  
			});    
			   
			   
			 cells[i].className = 'x-date-nextday';  
			 setCellClass(this, cells[i]);  
		}  
		this.mbtn.setText(this.monthNames[date.getMonth()] + ' ' + date.getFullYear());  
  
		if(!this.internalRender){  
			var main = this.el.dom.firstChild;  
			var w = main.offsetWidth;  
			this.el.setWidth(w + this.el.getBorderWidth('lr'));  
			Ext.fly(main).setWidth(w);  
			this.internalRender = true;  
			// opera does not respect the auto grow header center column  
			// then, after it gets a width opera refuses to recalculate  
			// without a second pass  
			if(Ext.isOpera && !this.secondPass){  
				main.rows[0].cells[1].style.width = (w - (main.rows[0].cells[0].offsetWidth+main.rows[0].cells[2].offsetWidth)) + 'px';  
				this.secondPass = true;  
				this.update.defer(10, this, [date]);  
			}  
		}  
	},  
	beforeDestroy : function() {  
		if(this.rendered){  
			Ext.destroy(  
				this.keyNav,  
				this.monthPicker,  
				this.eventEl,  
				this.mbtn,  
				this.nextRepeater,  
				this.prevRepeater,  
				this.cells.el,  
				this.todayBtn  
			);  
			delete this.textNodes;  
			delete this.cells.elements;  
		}  
	}  
});  
Ext.reg('mydatepicker', Ext.MyDatePicker);  
var myDate = new Ext.MyDatePicker();  
/*********************日历组件部分**************************** end */  