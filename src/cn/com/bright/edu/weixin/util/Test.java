package cn.com.bright.edu.weixin.util;

import java.util.Date;

import cn.brightcom.jraf.util.DatetimeUtil;
import cn.com.bright.edu.weixin.message.resp.RespTextMessage;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 回复文本消息
		RespTextMessage textMessage = new RespTextMessage();
		textMessage.setToUserName("ADADxzczxc");
		textMessage.setFromUserName("12344567ADADxzczxc<a href=\"http://www.szeb.edu.cn/\"></a>");
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		textMessage.setFuncFlag(0);
		MessageUtil messageUtil = new MessageUtil();
		System.out.println(messageUtil.textMessageToXml(textMessage));
		System.out.println(DatetimeUtil.getNow("yyyy-MM-dd HH:mm:ss"));
		System.out.println(MessageUtil.replaceBlank("asd asd @ asdasd !\nwww\tedd"));
	}

}
