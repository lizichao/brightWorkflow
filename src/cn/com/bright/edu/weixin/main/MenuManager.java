package cn.com.bright.edu.weixin.main;

import cn.com.bright.edu.weixin.pojo.AccessToken;
import cn.com.bright.edu.weixin.pojo.Button;
import cn.com.bright.edu.weixin.pojo.CommonButton;
import cn.com.bright.edu.weixin.pojo.ComplexButton;
import cn.com.bright.edu.weixin.pojo.Menu;
import cn.com.bright.edu.weixin.util.WeixinUtil;

/**
 * �˵���������
 * 
 * @author lhbo
 * @date 2014-01-05
 */
public class MenuManager {
	public static void main(String[] args) {
		// �������û�Ψһƾ֤
		String appId = "wx88d367e78b555d8c";
		// �������û�Ψһƾ֤��Կ
		String appSecret = "3a9b3b20e5c7602ba574425c5e4c6226";
		
		// ���ýӿڻ�ȡaccess_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

		if (null != at) {
			// ���ýӿڴ����˵�
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());

			// �жϲ˵��������
			if (0 == result)
				System.out.println("�˵������ɹ���");
			else
				System.out.println("�˵�����ʧ�ܣ������룺" + result);
		}
	}

	/**
	 * ��װ�˵�����
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		CommonButton btn11 = new CommonButton();
		btn11.setName("�μ�����");
		btn11.setType("view");
		btn11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fteacher%2Fpaper.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");

		CommonButton btn12 = new CommonButton();
		btn12.setName("�ϴ��μ�");
		btn12.setType("view");
		btn12.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fteacher%2Fteacher_upload.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		
		CommonButton btn21 = new CommonButton();
		btn21.setName("�����Ծ�");
		btn21.setType("view");
		btn21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fstudent%2Ftoday_papering.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		
		CommonButton btn22 = new CommonButton();
		btn22.setName("����ѧϰ");
		btn22.setType("view");
		btn22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fstudent%2Ftoday_learning.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		
		CommonButton btn31 = new CommonButton();
		btn31.setName("����ָ��");
		btn31.setType("view");
		btn31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fhelp.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		
		CommonButton btn32 = new CommonButton();
		btn32.setName("�˺Ű�");
		btn32.setType("view");
		btn32.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fbinding.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		
		CommonButton btn33 = new CommonButton();
		btn33.setName("����ѧϰ");
		btn33.setType("view");
		btn33.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fopen_learning.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		

		CommonButton btn34 = new CommonButton();
		btn34.setName("����ѧϰ2");
		btn34.setType("view");
		btn34.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fuserinfo.jsp&response_type=code&scope=snsapi_userinfo&state=lxkj#wechat_redirect");
		
		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("��ʦר��");
		mainBtn1.setSub_button(new CommonButton[] { btn11, btn12});

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("ѧ��ר��");
		mainBtn2.setSub_button(new CommonButton[] { btn21, btn22 });

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("�ֿ���");
		mainBtn3.setSub_button(new CommonButton[] { btn31, btn32, btn33});

		/**
		 * ���ǹ��ں�lhboszĿǰ�Ĳ˵��ṹ��ÿ��һ���˵����ж����˵���<br>
		 * 
		 * ��ĳ��һ���˵���û�ж����˵��������menu����ζ����أ�<br>
		 * ���磬������һ���˵����"��������"����ֱ����"��ĬЦ��"����ômenuӦ���������壺<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
		
		return menu;
	}
}
