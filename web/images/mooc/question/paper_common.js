var paperQuestionOrigin = 0,
homeworkQuestionOrigin = 0;
Teacher.paper.paper_common = {
    questionCart: $(".question-cart"),
    hiddenArrow: $(".hidden-arrow a"),
    menuLink: $(".menu a"),
    subjectBox: $(".subject-box"),
    subjectLink: $(".subject-chose"),
    subjectList: $(".subject-list"),
    mainMenu: $(".main-menu"),
    mainWrap: $(".main-wrap"),
    mainContainer: $(".preview-content"),
    childMenu: $(".child-menu"),
    init: function() {
        Teacher.paper.paper_common.initBase(),
        Teacher.paper.paper_common.get_question_cart(this.subjectLink.data("subject")),
        $(window).resize(function() {
            Teacher.paper.paper_common.initBase()
        }),
        this.menuLink.click(function() {
            var this_a = this;
            this.menuLink.removeClass("active"),
            $(this_a).addClass("active")
        }),
        this.subjectBox.hover(function() {
            var this_a = Teacher.paper.paper_common;
            this_a.subjectLink.addClass("subject-active"),
            this_a.subjectList.show()
        },
        function() {
            var this_a = Teacher.paper.paper_common;
            this_a.subjectLink.removeClass("subject-active"),
            this_a.subjectList.hide()
        }),
        this.hiddenArrow.click(function() {
            var this_a = this;
            Teacher.paper.paper_common.hiddenArrowClick(this_a)
        }),
        this.questionCart.on("click", "a.del-btn",
        function() {
            var this_a = this;
            Teacher.paper.paper_common.delQuestionCartClick(this_a)
        }),
        this.questionCart.on("click", "a.empty-btn",
        function() {
            Teacher.paper.paper_common.emptyQuestionCartClick()
        })
    },
    randerQuestion: function() {},
    hiddenArrowClick: function(this_a) {
        var thisStatus = $(this_a).attr("class");
        switch (thisStatus) {
        case "hide-menu":
            this.mainMenu.hide(),
            this.mainWrap.addClass("full-width"),
            $(this_a).attr("class", "show-menu");
            break;
        case "show-menu":
            this.mainMenu.show(),
            this.mainWrap.removeClass("full-width"),
            $(this_a).attr("class", "hide-menu")
        }
    },
    delQuestionCartClick: function(this_a) {
        var id = $(this_a).data("id"),
        subject_id = this.subjectLink.data("subject");
        return "0" == $(this_a).parent().parent().children(".second-col").text() ? !1 : ($.tiziDialog({
            content: "确定清空当前题型？",
            ok: function() {
                var this_a = Teacher.paper.paper_common;
                this_a.remove_question_from_cart(id, subject_id,
                function() {
                    $("#question-type" + id).find(".question-item-box").remove(),
                    $("#menu-outer-ul-question-type" + id).find(".question-item").remove(),
                    this_a.orderSort(),
                    this_a.orderType()
                },
                function() {})
            },
            cancel: !0
        }), void 0)
    },
    emptyQuestionCartClick: function() {
        var id = 0,
        subject_id = $(".subject-chose").data("subject"),
        sum = 0;
        return $(".question-table").find(".second-col").each(function() {
            sum += parseInt($(this).text())
        }),
        "0" == sum ? !1 : ($.tiziDialog({
            content: "确定清空全部题型？",
            ok: function() {
                var this_a = Teacher.paper.paper_common;
                this_a.remove_question_from_cart(id, subject_id,
                function() {
                    $(".paper-body").find(".ui-sortable").empty(),
                    $(".preview-content").find(".question-type-box").find(".question-item-box").remove(),
                    this_a.orderSort(),
                    this_a.orderType()
                },
                function() {})
            },
            cancel: !0
        }), void 0)
    },
    get_question_cart: function(sidVal) {
        $(".preview-btn").hide()/*,
        $.tizi_ajax({
            url: baseUrlName + "paper/paper_question/get_question_cart",
            type: "GET",
            dataType: "json",
            data: {
                sid: sidVal,
                ver: (new Date).valueOf()
            },
            success: function(data) {
                1 == data.errorcode ? (Teacher.paper.paper_common.randerCart(data.question_cart), $(".preview-btn").show()) : $.tiziDialog({
                    content: data.error
                })
            }
        })*/
        alert(1);
        var str = '{"question_cart":{"question_list":[],"question_type_list":[{"name":"\u5355\u9009\u9898","count":0,"id":78541204},{"name":"\u586b\u7a7a\u9898","count":0,"id":78541205},{"name":"\u8ba1\u7b97\u9898","count":0,"id":78541206},{"name":"\u89e3\u7b54\u9898","count":0,"id":78541207},{"name":"\u5224\u65ad\u9898","count":0,"id":78541208}],"question_total":0,"question_section_total":{"1":0,"2":0},"html":"\u003Ch4\u003E\u5df2\u9009\u9898\u76ee(\u003Cspan\u003E0\u003C\/span\u003E)\u003Ca href=\"javascript:void(0)\" class=\"green-text empty-btn\"\u003E\u5168\u90e8\u6e05\u7a7a\u003C\/a\u003E\u003C\/h4\u003E\n\u003Cdiv class=\"question-table\"\u003E\n \u003Ctable class=\"data-table\"\u003E\n \u003Ctr\u003E\n \u003Ctd class=\"first-col\" id=\"left-question-cart-name78541204\"\u003E\u5355\u9009\u9898\u003C\/td\u003E\n \u003Ctd class=\"second-col\"\u003E0\u003C\/td\u003E\n \u003Ctd class=\"third-col\"\u003E\u003Cdiv class=\"percent-line\"\u003E\u003Cdiv class=\"percent-text\"\u003E\u003Cspan\u003E0\u003C\/span\u003E%\u003C\/div\u003E\u003C\/div\u003E\u003C\/td\u003E\n \u003Ctd class=\"forth-col\"\u003E\u003Ca href=\"javascript:void(0)\" class=\"del-btn\" title=\"\u5220\u9664\u6b64\u9898\u578b\u4e2d\u7684\u6240\u6709\u9898\u76ee\" data-id=\"78541204\"\u003E\u5220\u9664\u003C\/a\u003E\u003C\/td\u003E\n \u003C\/tr\u003E\n \u003Ctr\u003E\n \u003Ctd class=\"first-col\" id=\"left-question-cart-name78541205\"\u003E\u586b\u7a7a\u9898\u003C\/td\u003E\n \u003Ctd class=\"second-col\"\u003E0\u003C\/td\u003E\n \u003Ctd class=\"third-col\"\u003E\u003Cdiv class=\"percent-line\"\u003E\u003Cdiv class=\"percent-text\"\u003E\u003Cspan\u003E0\u003C\/span\u003E%\u003C\/div\u003E\u003C\/div\u003E\u003C\/td\u003E\n \u003Ctd class=\"forth-col\"\u003E\u003Ca href=\"javascript:void(0)\" class=\"del-btn\" title=\"\u5220\u9664\u6b64\u9898\u578b\u4e2d\u7684\u6240\u6709\u9898\u76ee\" data-id=\"78541205\"\u003E\u5220\u9664\u003C\/a\u003E\u003C\/td\u003E\n \u003C\/tr\u003E\n \u003Ctr\u003E\n \u003Ctd class=\"first-col\" id=\"left-question-cart-name78541206\"\u003E\u8ba1\u7b97\u9898\u003C\/td\u003E\n \u003Ctd class=\"second-col\"\u003E0\u003C\/td\u003E\n \u003Ctd class=\"third-col\"\u003E\u003Cdiv class=\"percent-line\"\u003E\u003Cdiv class=\"percent-text\"\u003E\u003Cspan\u003E0\u003C\/span\u003E%\u003C\/div\u003E\u003C\/div\u003E\u003C\/td\u003E\n \u003Ctd class=\"forth-col\"\u003E\u003Ca href=\"javascript:void(0)\" class=\"del-btn\" title=\"\u5220\u9664\u6b64\u9898\u578b\u4e2d\u7684\u6240\u6709\u9898\u76ee\" data-id=\"78541206\"\u003E\u5220\u9664\u003C\/a\u003E\u003C\/td\u003E\n \u003C\/tr\u003E\n \u003Ctr\u003E\n \u003Ctd class=\"first-col\" id=\"left-question-cart-name78541207\"\u003E\u89e3\u7b54\u9898\u003C\/td\u003E\n \u003Ctd class=\"second-col\"\u003E0\u003C\/td\u003E\n \u003Ctd class=\"third-col\"\u003E\u003Cdiv class=\"percent-line\"\u003E\u003Cdiv class=\"percent-text\"\u003E\u003Cspan\u003E0\u003C\/span\u003E%\u003C\/div\u003E\u003C\/div\u003E\u003C\/td\u003E\n \u003Ctd class=\"forth-col\"\u003E\u003Ca href=\"javascript:void(0)\" class=\"del-btn\" title=\"\u5220\u9664\u6b64\u9898\u578b\u4e2d\u7684\u6240\u6709\u9898\u76ee\" data-id=\"78541207\"\u003E\u5220\u9664\u003C\/a\u003E\u003C\/td\u003E\n \u003C\/tr\u003E\n \u003Ctr\u003E\n \u003Ctd class=\"first-col\" id=\"left-question-cart-name78541208\"\u003E\u5224\u65ad\u9898\u003C\/td\u003E\n \u003Ctd class=\"second-col\"\u003E0\u003C\/td\u003E\n \u003Ctd class=\"third-col\"\u003E\u003Cdiv class=\"percent-line\"\u003E\u003Cdiv class=\"percent-text\"\u003E\u003Cspan\u003E0\u003C\/span\u003E%\u003C\/div\u003E\u003C\/div\u003E\u003C\/td\u003E\n \u003Ctd class=\"forth-col\"\u003E\u003Ca href=\"javascript:void(0)\" class=\"del-btn\" title=\"\u5220\u9664\u6b64\u9898\u578b\u4e2d\u7684\u6240\u6709\u9898\u76ee\" data-id=\"78541208\"\u003E\u5220\u9664\u003C\/a\u003E\u003C\/td\u003E\n \u003C\/tr\u003E\n \u003C\/table\u003E\n\u003C\/div\u003E"},"errorcode":true,"token":"","callback":false}';
        console.log(str);
        //data = JSON.parse(str);//eval("("+str+")");
        //alert(data.question_cart);
        str = '{"question_cart":{"question_list":[],"question_type_list":[{"name":"单选题","count":0,"id":78541204},{"name":"填空题","count":0,"id":78541205},{"name":"计算题","count":0,"id":78541206},{"name":"解答题","count":0,"id":78541207},{"name":"判断题","count":0,"id":78541208}],"question_total":0,"question_section_total":{"1":0,"2":0},"html":"<h4>已选题目(<span>0</span>)<a href="javascript:void(0)" class="green-text empty-btn">全部清空</a></h4><div class="question-table"><table class="data-table"><tr><td class="first-col" id="left-question-cart-name78541204">单选题</td><td class="second-col">0</td><td class="third-col"><div class="percent-line"><div class="percent-text"><span>0</span>%</div></div></td><td class="forth-col"><a href="javascript:void(0)" class="del-btn" title="删除此题型中的所有题目" data-id="78541204">删除</a></td></tr><tr><td class="first-col" id="left-question-cart-name78541205">填空题</td><td class="second-col">0</td><td class="third-col"><div class="percent-line"><div class="percent-text"><span>0</span>%</div></div></td><td class="forth-col"><a href="javascript:void(0)" class="del-btn" title="删除此题型中的所有题目" data-id="78541205">删除</a></td></tr><tr><td class="first-col" id="left-question-cart-name78541206">计算题</td><td class="second-col">0</td><td class="third-col"><div class="percent-line"><div class="percent-text"><span>0</span>%</div></div></td><td class="forth-col"><a href="javascript:void(0)" class="del-btn" title="删除此题型中的所有题目" data-id="78541206">删除</a></td></tr><tr><td class="first-col" id="left-question-cart-name78541207">解答题</td><td class="second-col">0</td><td class="third-col"><div class="percent-line"><div class="percent-text"><span>0</span>%</div></div></td><td class="forth-col"><a href="javascript:void(0)" class="del-btn" title="删除此题型中的所有题目" data-id="78541207">删除</a></td></tr><tr><td class="first-col" id="left-question-cart-name78541208">判断题</td><td class="second-col">0</td><td class="third-col"><div class="percent-line"><div class="percent-text"><span>0</span>%</div></div></td><td class="forth-col"><a href="javascript:void(0)" class="del-btn" title="删除此题型中的所有题目" data-id="78541208">删除</a></td></tr></table></div>"},"errorcode":true,"token":"","callback":false}';
        data = JSON.parse(str);
        alert(data.question_cart);
        Teacher.paper.paper_common.randerCart(data.question_cart), $(".preview-btn").show();
    },
    remove_question_from_cart: function(qtypeVal, sidVal, success, err) {
        var this_a = Teacher.paper.paper_common;
        $.tizi_ajax({
            url: baseUrlName + "paper/paper_question/remove_question_from_cart",
            type: "POST",
            dataType: "json",
            data: {
                qtype: qtypeVal,
                sid: sidVal
            },
            success: function(data) {
                1 == data.errorcode ? (success(), this_a.randerCart(data.question_cart), this_a.randerQuestion()) : (err(), $.tiziDialog({
                    content: data.error
                }))
            }
        })
    },
    initBase: function() {
        Common.getLeftBar({
            id: "#leftBar"
        }),
        Common.headerNav({
            id: "#navSlidown",
            ul: "ul",
            dis: "dis"
        });
        var initHeight = $(window).height() - $(".header").height() - $(".footer").height();
        $(".mainContainer").css("height", initHeight),
        $(".mainContainer .content").css("height", initHeight).css("overflow-y", "scroll"),
        $(".preview-content").css("height", initHeight - 60),
        $(".drag-line").css("height", initHeight),
        $(".drag-line a").css("margin-top", initHeight / 2 + "px"),
        $(".ui-resizable-eu").css("height", initHeight + 10),
        $(".child-menu").css("height", initHeight - 10),
        $(".main-menu,.hidden-arrow,iframe").height(initHeight),
        this.hiddenArrow.css("margin-top", initHeight / 2 - 8 + "px")
    },
    percentShow: function() {
        var totleNum = parseInt(this.mainMenu.find("h4").children("span").text()),
        questionArr = [];
        $(".second-col").each(function() {
            questionArr.push(parseInt($(this).text()))
        });
        var percentArr = $.map(questionArr,
        function(n) {
            return 0 == totleNum ? 0 : Math.round(100 * n / totleNum)
        });
        $(".percent-text span").each(function(index) {
            $(this).text(percentArr[index])
        }),
        $(".percent-line").each(function(index) {
            $(this).width(Math.round(percentArr[index] / 2))
        })
    },
    randerCart: function(data) {
        void 0 != data && (this.questionCart.html(data.html), this.percentShow())
    },
    orderSort: function(ele) {
        var con = this.mainContainer.find(".question-index"),
        ele = this.childMenu.find(".list-order");
        ele.each(function(index, element) {
            $(element).text(index + 1)
        }),
        con.each(function(index, element) {
            $(element).text(index + 1 + "、")
        })
    },
    orderType: function() {
        var righttype = $(".preview-content").find(".type-title-nu"),
        lefttype = $(".child-menu").find(".menu-questiontype-nu");
        righttype.each(function(index, element) {
            $(element).text(index + 1)
        }),
        lefttype.each(function(index, element) {
            $(element).text(index + 1)
        }),
        Teacher.paper.common.number_change(".type-title-nu"),
        Teacher.paper.common.number_change(".menu-questiontype-nu")
    }
};