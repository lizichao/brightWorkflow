var TeacherCommon = {};
1 == $(".tree-list").length && $(".tree-list").height($(window).height() - 62 - 80 - 26).css("overflow-y", "scroll"),
$(".type-box").find("select").find("option").length >= 1 && $(".tree-list").height($(window).height() - 80 - 70).css("overflow-y", "scroll"),
TeacherCommon.leftSlidown = function(json) {
    var _id = json.id,
    _con = json.con,
    _dis = json.dis,
    _active = json.active;
    $(_id).hover(function() {
        $(this).find("h2").attr("id", _active),
        $(_con).addClass(_dis)
    },
    function() {
        $(this).find("h2").attr("id", ""),
        $(_con).removeClass(_dis)
    })
},
void 0 != $(".mainContainer").attr("pagename") && $(".mainMenu li").each(function(i) {
    $(".mainContainer").attr("pagename") == $(".mainMenu li").eq(i).attr("name") && ($(".mainMenu li").eq(i).find("a").attr("class", "active"), "paper_cequestion" == $(this).attr("name") && $(this).find("a").css({
        background: "#c00",
        color: "#fff"
    }))
}),
TeacherCommon.subjectSlidown = function() {
    $(".currentSubject").hover(function() {
        $(this).find(".bd").show()
    },
    function() {
        $(this).find(".bd").hide()
    })
},
TeacherCommon.subjectSlidown(),
TeacherCommon.demoAnimation = {
    init: function() {
        this.outPaper()
    },
    outPaper: function() {
        $(".demoAnimation .outPaper").click(function() {
            $.tiziDialog({
                title: $(".demoAnimation .outPaper").html(),
                content: '<object id="mutiupload" name="mutiupload" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="800" height="560"><param name="movie" value="' + staticUrlName + 'flash/outPaper.swf" /><param name="wmode" value="transparent" /><param name="allowScriptAccess" value="always" /><!--[if !IE]>--><object type="application/x-shockwave-flash" data="' + staticUrlName + 'flash/outPaper.swf" width="800" height="560" wmode="transparent" allowScriptAccess="always"><!--<![endif]--><span class="flashNotice"><a href="http://www.adobe.com/go/getflashplayer" target="_blank"><img src="' + staticUrlName + 'image/common/get_flash_player.gif" alt="下载Flash播放器" /></a><span>您需要安装11.4.0以上的版本的Flash播放器，才能正常访问此页面。</span><!--[if !IE]>--></object><!--<![endif]--></object>',
                icon: null,
                ok: null,
                width: 800,
                height: 560
            })
        })
    }
},
Common.comValidform.detectFlashSupport(function() {
    $(".demoAnimation").remove()
},
function() {
    TeacherCommon.demoAnimation.init()
}),
Teacher = {},
Teacher.tableStyleFn = function() {
    $(".tableStyle tr").hover(function() {
        $(this).addClass("tdf1").siblings().removeClass("tdf1")
    },
    function() {
        $(".tableStyle tr").removeClass("tdf1")
    })
},
Teacher.tableStyleFn(),
1024 == screen.width && $.browser.msie && "6.0" == $.browser.version && !$.support.style && $(".question-list").css("width", "97%").css("overflow", "hidden"),
Teacher.typeListUlHeight = function() {
    var obj = $(".child-menu .type-box .type-list");
    obj.length > 0 && obj.each(function(i) {
        $(this).find("ul").css("width", "178px"),
        1 == i && $(this).find("ul").css("margin-left", "-92px"),
        $(this).find("ul").height() > 300 ? $(this).find("ul").css("height", "300px") : $(this).find("ul").css("height", "auto")
    }),
    1 == obj.length && obj.find("ul").css("width", "138px")
},
Teacher.typeListUlHeight(),
Teacher.paper = {},
Teacher.paper.common = {
    ErrorNoQuestion: "您还没有添加题目",
    number_change: function(id) {
        $(id).each(function() {
            switch ($(this).text()) {
            case "1":
                $(this).text("一");
                break;
            case "2":
                $(this).text("二");
                break;
            case "3":
                $(this).text("三");
                break;
            case "4":
                $(this).text("四");
                break;
            case "5":
                $(this).text("五");
                break;
            case "6":
                $(this).text("六");
                break;
            case "7":
                $(this).text("七");
                break;
            case "8":
                $(this).text("八");
                break;
            case "9":
                $(this).text("九");
                break;
            case "10":
                $(this).text("十");
                break;
            case "11":
                $(this).text("十一");
                break;
            case "12":
                $(this).text("十二");
                break;
            case "13":
                $(this).text("十三");
                break;
            case "14":
                $(this).text("十四");
                break;
            case "15":
                $(this).text("十五");
                break;
            case "16":
                $(this).text("十六");
                break;
            case "17":
                $(this).text("十七");
                break;
            case "18":
                $(this).text("十八");
                break;
            case "19":
                $(this).text("十九");
                break;
            case "20":
                $(this).text("二十");
                break;
            default:
                return
            }
        })
    }
};