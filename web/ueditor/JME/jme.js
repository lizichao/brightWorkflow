
UE.registerUI('jmeDialog',function(editor,uiName){

    //����dialog
    var dialog = new UE.ui.Dialog({
        //ָ����������ҳ���·��������ֻ��֧��ҳ��,��Ϊ��addCustomizeDialog.js��ͬĿ¼�����������·��
        iframeUrl:'/ueditor/JME/mathdialog.html?M='+Math.random(),
        //��Ҫָ����ǰ�ı༭��ʵ��
        editor:editor,
        //ָ��dialog������
        name:"math_frame",
        //dialog�ı���
        title:"��ʽ�༭��",

        //ָ��dialog����Χ��ʽ
        cssRules:"width:500px;height:300px;",

        //���������buttons�ʹ���dialog��ȷ����ȡ��
		
        buttons:[
            {
                className:'edui-okbutton',
                label:'ȷ��',
                onclick:function () {
                    dialog.close(true);
                }
            },
            {
                className:'edui-cancelbutton',
                label:'ȡ��',
                onclick:function () {
                    dialog.close(false);
                }
            }
        ]
		
		});
        
    //�ο�addCustomizeButton.js
    var btn = new UE.ui.Button({
        name:'mathquill',
        title:'��ʽ�༭',
        //��Ҫ��ӵĶ�����ʽ��ָ��iconͼ�꣬����Ĭ��ʹ��һ���ظ���icon
        cssRules :'background-position: -200px -40px;',
        onclick:function () {
            //��Ⱦdialog
            dialog.render();
            dialog.open();
        }
    });

    return btn;
})