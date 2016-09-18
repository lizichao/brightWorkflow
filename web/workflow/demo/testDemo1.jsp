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
        var top = element.position().top; //当前元素对象element距离浏览器上边缘的距离 
        var pos = element.css("position"); //当前元素距离页面document顶部的距离 
        $(window).scroll(function() { //侦听滚动时 
            var scrolls = $(this).scrollTop(); 
            if (scrolls > top) { //如果滚动到页面超出了当前元素element的相对页面顶部的高度 
                if (window.XMLHttpRequest) { //如果不是ie6 
                    element.css({ //设置css 
                        position: "fixed", //固定定位,即不再跟随滚动 
                        top: 0 //距离页面顶部为0 
                    }).addClass("shadow"); //加上阴影样式.shadow 
                } else { //如果是ie6 
                    element.css({ 
                        top: scrolls  //与页面顶部距离 
                    });     
                } 
            }else { 
                element.css({ //如果当前元素element未滚动到浏览器上边缘，则使用默认样式 
                    position: pos, 
                    top: top 
                }).removeClass("shadow");//移除阴影样式.shadow 
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
        <li><a href="#">宝贝</a></li> 
        <li class="cur"><a href="#">评价详情（123）</a></li> 
        <li><a href="#">成交记录（68件）</a></li> 
    </ul> 
</div> 

<body>