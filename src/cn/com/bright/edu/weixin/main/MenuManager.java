package cn.com.bright.edu.weixin.main;

import cn.com.bright.edu.weixin.pojo.AccessToken;
import cn.com.bright.edu.weixin.pojo.Button;
import cn.com.bright.edu.weixin.pojo.CommonButton;
import cn.com.bright.edu.weixin.pojo.ComplexButton;
import cn.com.bright.edu.weixin.pojo.Menu;
import cn.com.bright.edu.weixin.util.WeixinUtil;

/**
 * 菜单管理器类
 * 
 * @author lhbo
 * @date 2014-01-05
 */
public class MenuManager {
	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId = "wx88d367e78b555d8c";
		// 第三方用户唯一凭证密钥
		String appSecret = "3a9b3b20e5c7602ba574425c5e4c6226";
		
		// 调用接口获取access_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

		if (null != at) {
			// 调用接口创建菜单
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());

			// 判断菜单创建结果
			if (0 == result)
				System.out.println("菜单创建成功！");
			else
				System.out.println("菜单创建失败，错误码：" + result);
		}
	}

	/**
	 * 组装菜单数据
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		CommonButton btn11 = new CommonButton();
		btn11.setName("课件管理");
		btn11.setType("view");
		btn11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fteacher%2Fpaper.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");

		CommonButton btn12 = new CommonButton();
		btn12.setName("上传课件");
		btn12.setType("view");
		btn12.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fteacher%2Fteacher_upload.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		
		CommonButton btn21 = new CommonButton();
		btn21.setName("今日试卷");
		btn21.setType("view");
		btn21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fstudent%2Ftoday_papering.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		
		CommonButton btn22 = new CommonButton();
		btn22.setName("今日学习");
		btn22.setType("view");
		btn22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fstudent%2Ftoday_learning.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		
		CommonButton btn31 = new CommonButton();
		btn31.setName("操作指南");
		btn31.setType("view");
		btn31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fhelp.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		
		CommonButton btn32 = new CommonButton();
		btn32.setName("账号绑定");
		btn32.setType("view");
		btn32.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fbinding.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		
		CommonButton btn33 = new CommonButton();
		btn33.setName("公开学习");
		btn33.setType("view");
		btn33.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fopen_learning.jsp&response_type=code&scope=snsapi_base&state=lxkj#wechat_redirect");
		

		CommonButton btn34 = new CommonButton();
		btn34.setName("公开学习2");
		btn34.setType("view");
		btn34.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx88d367e78b555d8c&redirect_uri=http%3A%2F%2Fweike.75510010.com%2Fweixin%2Fuserinfo.jsp&response_type=code&scope=snsapi_userinfo&state=lxkj#wechat_redirect");
		
		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("教师专区");
		mainBtn1.setSub_button(new CommonButton[] { btn11, btn12});

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("学生专区");
		mainBtn2.setSub_button(new CommonButton[] { btn21, btn22 });

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("沃课堂");
		mainBtn3.setSub_button(new CommonButton[] { btn31, btn32, btn33});

		/**
		 * 这是公众号lhbosz目前的菜单结构，每个一级菜单都有二级菜单项<br>
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 * 比如，第三个一级菜单项不是"更多体验"，而直接是"幽默笑话"，那么menu应该这样定义：<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
		
		return menu;
	}
}
