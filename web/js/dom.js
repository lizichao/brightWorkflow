if(!document.all){
/*�ű�û�н�������⼰����:

2.IE��,����ʹ��()��[]��ȡ���������;Firefox��,ֻ��ʹ��[]��ȡ���������.
�������:ͳһʹ��[]��ȡ���������.
3.IE��,����ʹ�û�ȡ�������Եķ�������ȡ�Զ�������,Ҳ����ʹ��getAttribute()��ȡ�Զ�������;Firefox��,ֻ��ʹ��getAttribute()��ȡ�Զ�������.
�������:ͳһͨ��getAttribute()��ȡ�Զ�������.
4.IE��,HTML�����ID������Ϊdocument���������������ֱ��ʹ��;Firefox������.
5.Firefox��,����ʹ����HTML����ID��ͬ�ı�����;IE�����ܡ�
�������:ʹ��document.getElementById("idName")����document.idName.��ò�ҪȡHTML����ID��ͬ�ı�����,�Լ��ٴ���;����������ʱ,һ�ɼ���var,�Ա�������.
6.IE��input.type����Ϊֻ��;����Firefox��input.type����Ϊ��д.
8.IE��,����ͨ��showModalDialog��showModelessDialog��ģ̬�ͷ�ģ̬����;Firefox������
9.Firefox��body��body��ǩû�б��������ȫ����֮ǰ�ʹ��ڣ���IE��body�������body��ǩ���������ȫ����֮��Ŵ���
10.
*/
	//�ĵ�����
	HTMLDocument.prototype.__defineGetter__("all",function(){return this.getElementsByName("*");});

	HTMLFormElement.constructor.prototype.item=function(s){return this.elements[s];};

	HTMLCollection.prototype.item=function(s){return this[s];};

	//�¼�����
	window.constructor.prototype.__defineGetter__("event",function(){
		for(var o=arguments.callee.caller,e=null;o!=null;o=o.caller){
			e=o.arguments[0];
			if(e&&(e instanceof Event))
			return e;
		}
		return null;
	});

	window.constructor.prototype.attachEvent=HTMLDocument.prototype.attachEvent=HTMLElement.prototype.attachEvent=function(e,f){
		this.addEventListener(e.replace(/^on/i,""),f,false);
	};

	window.constructor.prototype.detachEvent=HTMLDocument.prototype.detachEvent=HTMLElement.prototype.detachEvent=function(e,f){
		this.removeEventListener(e.replace(/^on/i,""),f,false);
	};


	with(window.Event.constructor.prototype){
		__defineGetter__("srcElement",function(){return this.target;});

		__defineSetter__("returnValue",function(b){if(!b)this.preventDefault();});

		__defineSetter__("cancelBubble",function(b){if(b)this.stopPropagation();});

		__defineGetter__("fromElement",function(){
			var o=(this.type=="mouseover"&&this.relatedTarget)||(this.type=="mouseout"&&this.target)||null;
			if(o)
				while(o.nodeType!=1)
					o=o.parentNode;
			return o;
		});

		__defineGetter__("toElement",function(){
			var o=(this.type=="mouseover"&&this.target)||(this.type=="mouseout"&&this.relatedTarget)||null;
			if(o)
				while(o.nodeType!=1)
			o=o.parentNode;
			return o;
		});

		__defineGetter__("x",function(){return this.pageX;});

		__defineGetter__("y",function(){return this.pageY;});

		__defineGetter__("offsetX",function(){return this.layerX;});

		__defineGetter__("offsetY",function(){return this.layerY;});
}
//�ڵ��������
with(window.Node.prototype){
replaceNode=function(o){
this.parentNode.replaceChild(o,this);}

removeNode=function(b){
if(b)
return this.parentNode.removeChild(this);
var range=document.createRange();
range.selectNodeContents(this);
return this.parentNode.replaceChild(range.extractContents(),this);}

swapNode=function(o){
return this.parentNode.replaceChild(o.parentNode.replaceChild(this,o),this);}

contains=function(o){
return o?((o==this)?true:arguments.callee(o.parentNode)):false;}
}
//HTMLԪ�ؼ���
with(window.HTMLElement.prototype){
__defineGetter__("parentElement",function(){
return (this.parentNode==this.ownerDocument)?null:this.parentNode;});

__defineGetter__("children",function(){
var c=[];
for(var i=0,cs=this.childNodes;i<cs.length;i++){
if(cs[i].nodeType==1)
c.push(cs[i]);}
return c;});

__defineGetter__("canHaveChildren",function(){
return !/^(area|base|basefont|col|frame|hr|img|br|input|isindex|link|meta|param)$/i.test(this.tagName);});

__defineSetter__("outerHTML",function(s){
var r=this.ownerDocument.createRange();
r.setStartBefore(this);
void this.parentNode.replaceChild(r.createContextualFragment(s),this);
return s;});
__defineGetter__("outerHTML",function(){
var as=this.attributes;
var str="<"+this.tagName;
for(var i=0,al=as.length;i<al;i++){
if(as[i].specified)
str+=" "+as[i].name+"=""+as[i].value+""";}
return this.canHaveChildren?str+">":str+">"+this.innerHTML+"</"+this.tagName+">";});

__defineSetter__("innerText",function(s){
return this.innerHTML=document.createTextNode(s);});
__defineGetter__("innerText",function(){
var r=this.ownerDocument.createRange();
r.selectNodeContents(this);
return r.toString();});

__defineSetter__("outerText",function(s){
void this.parentNode.replaceChild(document.createTextNode(s),this);
return s});
__defineGetter__("outerText",function(){
var r=this.ownerDocument.createRange();
r.selectNodeContents(this);
return r.toString();});

insertAdjacentElement=function(s,o){
return (s=="beforeBegin"&&this.parentNode.insertBefore(o,this))||(s=="afterBegin"&&this.insertBefore(o,this.firstChild))||(s=="beforeEnd"&&this.appendChild(o))||(s=="afterEnd"&&((this.nextSibling)&&this.parentNode.insertBefore(o,this.nextSibling)||this.parentNode.appendChild(o)))||null;}

insertAdjacentHTML=function(s,h){
var r=this.ownerDocument.createRange();
r.setStartBefore(this);
this.insertAdjacentElement(s,r.createContextualFragment(h));}

insertAdjacentText=function(s,t){
this.insertAdjacentElement(s,document.createTextNode(t));}
}
//XMLDOM����
window.ActiveXObject=function(s){
switch(s){
case "XMLDom":
document.implementation.createDocument.call(this,"text/xml","", null);
//domDoc = document.implementation.createDocument("text/xml","", null);
break;
}
}

XMLDocument.prototype.LoadXML=function(s){
for(var i=0,cs=this.childNodes,cl=childNodes.length;i<cl;i++)
this.removeChild(cs[i]);
this.appendChild(this.importNode((new DOMParser()).parseFromString(s,"text/xml").documentElement,true));}

XMLDocument.prototype.selectSingleNode=Element.prototype.selectSingleNode=function(s){
return this.selectNodes(s)[0];}
XMLDocument.prototype.selectNodes=Element.prototype.selectNodes=function(s){
var rt=[];
for(var i=0,rs=this.evaluate(s,this,this.createNSResolver(this.ownerDocument==null?this.documentElement:this.ownerDocument.documentElement),XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null),sl=rs.snapshotLength;i<sl;i++)
rt.push(rs.snapshotItem(i));
return rt;}

XMLDocument.prototype.__proto__.__defineGetter__("xml",function(){
try{
return new XMLSerializer().serializeToString(this);}
catch(e){
return document.createElement("div").appendChild(this.cloneNode(true)).innerHTML;}});
Element.prototype.__proto__.__defineGetter__("xml",function(){
try{
return new XMLSerializer().serializeToString(this);}
catch(e){
return document.createElement("div").appendChild(this.cloneNode(true)).innerHTML;}});

XMLDocument.prototype.__proto__.__defineGetter__("text",function(){
return this.firstChild.textContent;});

Element.prototype.__proto__.__defineGetter__("text",function(){
return this.textContent;});
Element.prototype.__proto__.__defineSetter__("text",function(s){
return this.textContent=s;});

}