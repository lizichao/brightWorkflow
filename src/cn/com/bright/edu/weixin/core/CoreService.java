package cn.com.bright.edu.weixin.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.weixin.message.resp.Article;
import cn.com.bright.edu.weixin.message.resp.NewsMessage;
import cn.com.bright.edu.weixin.message.resp.RespTextMessage;
import cn.com.bright.edu.weixin.util.MessageUtil;
import cn.com.bright.yuexue.teach.MediaManager;

/**
 * 核心服务类
 * 
 * @author lhbo
 * @date 2014-01-05
 */
public class CoreService {
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "";//"请求处理异常，请稍候尝试！";
			String undefinedContent = "系统无法识别您的命令,请点击微信公众号下面的菜单,查看操作提示,按照提示内容进行输入,谢谢您的支持！";
			MessageFactory messageFactory = null;
	
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			
			
			//开发测试
			/*requestMap.put("FromUserName",request.getParameter("FromUserName"));
			requestMap.put("ToUserName",request.getParameter("ToUserName"));
			requestMap.put("MsgType",request.getParameter("MsgType"));
			requestMap.put("EventKey",request.getParameter("EventKey"));
			requestMap.put("Content",request.getParameter("Content"));
			*/
			
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 消息内容
			String replyContent = request.getParameter("Content");
			
			if(StringUtil.isEmpty(replyContent)){
				replyContent = "";
			}
			
			if (StringUtil.isEmpty(replyContent)){
				replyContent = "";
			}
			
			//用户状态
			//String supuserStatus="";
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				 respContent = "消息发送成功！\n";  
				/*
				SupVoteHandle svh = new SupVoteHandle();
				supuserStatus = svh.getUserStatus(fromUserName);
				requestMap.put("SupUserStatus",supuserStatus);
				if (!"0".equals(supuserStatus)
					&&  !"4".equals(supuserStatus) 
					&&  replyContent.indexOf("#")<0){
					respContent = svh.process(requestMap);
				}else{
					messageFactory = new MessageFactory();
					respContent = messageFactory.getInfo(requestMap);
				}
				messageFactory = new MessageFactory();
				respContent = messageFactory.getInfo(requestMap);
				*/
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "图片发送成功！\n";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "位置发送成功！\n";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "链接发送成功！\n" ;
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "音频发送成功！\n" ;
			}
			// 视频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO) 
					|| msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_SHORTVIDEO)) {
				MediaManager media = new MediaManager();
				media.addAttachment(requestMap.get("MediaId"), fromUserName);
				respContent = "视频上传成功!\n";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "感谢您关注WO课堂微信公众号！";
					//respContent = "亲爱的家长朋友们，还在为忘记小孩补贴系统的登陆密码而着急吗？还在为看不到核验结果而焦虑吗？关注亮信科技技术服务微信，就可以轻松查询！\n首先请前往“设置”绑定账户，然后您就可以使用以下功能：\n（1） 查询基本信息\n（2） 查询核验结果\n（3） 查询核验详情\n（4） 进行密码重置";
					WeiXinAccount.getInstance().subscribe(fromUserName);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
					WeiXinAccount.getInstance().unsubscribe(fromUserName);
				}
				// 模版消息
				else if (eventType.equals(MessageUtil.EVENT_TYPE_TEMPLATESENDJOBFINISH)) {
					// TODO 在模版消息发送任务完成后，微信服务器会将是否送达成功作为通知，发送到开发者中心中填写的服务器配置地址中
					WeiXinTemplate.getInstance().receiveMessage(requestMap);
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");
					/*
					SupVoteHandle svh = new SupVoteHandle();
					supuserStatus = svh.getUserStatus(fromUserName);
					requestMap.put("SupUserStatus",supuserStatus);
					if (!"vote-2".equals(eventKey)){
						if (!"0".equals(supuserStatus) && !"4".equals(supuserStatus)){//清空两个表的数据
							svh.clearCacheData(fromUserName);
						}
						messageFactory = new MessageFactory();
						respContent = messageFactory.getInfo(requestMap);
					} else {
						respContent = svh.process(requestMap);				    	
					}
					messageFactory = new MessageFactory();
					respContent = messageFactory.getInfo(requestMap);
					*/
				}
			} else {
				respContent = "消息发送成功.！\n";
			}
			
			if(messageFactory!=null && StringUtil.isNotEmpty(messageFactory.getReqTitle())){			
				// 创建图文消息
				NewsMessage newsMessage = new NewsMessage();  
				newsMessage.setToUserName(fromUserName);  
				newsMessage.setFromUserName(toUserName);  
				newsMessage.setCreateTime(new Date().getTime());  
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
				newsMessage.setFuncFlag(0);
				
				List<Article> articleList = new ArrayList<Article>();
				Article article = new Article();
				article.setTitle(messageFactory.getReqTitle());
				article.setDescription(respContent);
				article.setPicUrl(""); // 将图片置为空
				article.setUrl(""); // 将点击图文消息跳转链接置为空
				articleList.add(article);
				newsMessage.setArticleCount(articleList.size());
				newsMessage.setArticles(articleList);
				respMessage = MessageUtil.newsMessageToXml(newsMessage);				
			} else {
				// 回复文本消息
				RespTextMessage textMessage = new RespTextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				textMessage.setFuncFlag(0);
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}
	
}