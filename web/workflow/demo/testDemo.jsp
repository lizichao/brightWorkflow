<%@ page contentType="text/html; charset=GBK" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<style type="text/css">
#nav{width:720px; height:42px; position:absolute; margin-left:20px; border:1px solid #d3d3d3;  
background:#f7f7f7;-moz-border-radius:2px; -webkit-border-radius:2px; border-radius:2px; } 
#nav li{float:left; height:42px; line-height:42px; padding:0 10px; border-right: 
1px solid #d3d3d3; font-size:14px; font-weight:bold} 
#nav li.cur{background:#f1f1f1; border-top:1px solid #f60} 
#nav li a{text-decoration:none;} 
#nav li.cur a{color:#333} 
#nav li a:hover{color:#f30} 
.shadow{-moz-box-shadow:1px 1px 2px rgba(0,0,0,.2); -webkit-box-shadow:1px 1px 2px  
rgba(0,0,0,.2); box-shadow:1px 1px 2px rgba(0,0,0,.2);} 
</style>

<script type="text/javascript" src="/js/jquery/jquery-1.10.2.min.js"></script>

<script>
$.fn.smartFloat = function() { 
    var position = function(element) { 
        var top = element.position().top; //��ǰԪ�ض���element����������ϱ�Ե�ľ��� 
        var pos = element.css("position"); //��ǰԪ�ؾ���ҳ��document�����ľ��� 
        $(window).scroll(function() { //��������ʱ 
            var scrolls = $(this).scrollTop(); 
            if (scrolls > top) { //���������ҳ�泬���˵�ǰԪ��element�����ҳ�涥���ĸ߶� 
                if (window.XMLHttpRequest) { //�������ie6 
                    element.css({ //����css 
                        position: "fixed", //�̶���λ,�����ٸ������ 
                        top: 0 //����ҳ�涥��Ϊ0 
                    }).addClass("shadow"); //������Ӱ��ʽ.shadow 
                } else { //�����ie6 
                    element.css({ 
                        top: scrolls  //��ҳ�涥������ 
                    });     
                } 
            }else { 
                element.css({ //�����ǰԪ��elementδ������������ϱ�Ե����ʹ��Ĭ����ʽ 
                    position: pos, 
                    top: top 
                }).removeClass("shadow");//�Ƴ���Ӱ��ʽ.shadow 
            } 
        }); 
    }; 
    return $(this).each(function() { 
        position($(this));                          
    }); 
}; 

$(document).ready(function(){
	   $("#nav").smartFloat(); 
	})
</script>

</head> 
<body style="height:1800px">

<div id="nav"> 
    <ul> 
        <li><a href="#">����</a></li> 
        <li class="cur"><a href="#">�������飨123��</a></li> 
        <li><a href="#">�ɽ���¼��68����</a></li> 
    </ul> 
</div> 

<body>