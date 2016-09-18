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
 * ���ķ�����
 * 
 * @author lhbo
 * @date 2014-01-05
 */
public class CoreService {
	/**
	 * ����΢�ŷ���������
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// Ĭ�Ϸ��ص��ı���Ϣ����
			String respContent = "";//"�������쳣�����Ժ��ԣ�";
			String undefinedContent = "ϵͳ�޷�ʶ����������,����΢�Ź��ں�����Ĳ˵�,�鿴������ʾ,������ʾ���ݽ�������,лл����֧�֣�";
			MessageFactory messageFactory = null;
	
			// xml�������
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			
			
			//��������
			/*requestMap.put("FromUserName",request.getParameter("FromUserName"));
			requestMap.put("ToUserName",request.getParameter("ToUserName"));
			requestMap.put("MsgType",request.getParameter("MsgType"));
			requestMap.put("EventKey",request.getParameter("EventKey"));
			requestMap.put("Content",request.getParameter("Content"));
			*/
			
			// ���ͷ��ʺţ�open_id��
			String fromUserName = requestMap.get("FromUserName");
			// �����ʺ�
			String toUserName = requestMap.get("ToUserName");
			// ��Ϣ����
			String msgType = requestMap.get("MsgType");
			// ��Ϣ����
			String replyContent = request.getParameter("Content");
			
			if(StringUtil.isEmpty(replyContent)){
				replyContent = "";
			}
			
			if (StringUtil.isEmpty(replyContent)){
				replyContent = "";
			}
			
			//�û�״̬
			//String supuserStatus="";
			// �ı���Ϣ
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				 respContent = "��Ϣ���ͳɹ���\n";  
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
			// ͼƬ��Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "ͼƬ���ͳɹ���\n";
			}
			// ����λ����Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "λ�÷��ͳɹ���\n";
			}
			// ������Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "���ӷ��ͳɹ���\n" ;
			}
			// ��Ƶ��Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "��Ƶ���ͳɹ���\n" ;
			}
			// ��Ƶ��Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO) 
					|| msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_SHORTVIDEO)) {
				MediaManager media = new MediaManager();
				media.addAttachment(requestMap.get("MediaId"), fromUserName);
				respContent = "��Ƶ�ϴ��ɹ�!\n";
			}
			// �¼�����
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// �¼�����
				String eventType = requestMap.get("Event");
				// ����
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "��л����עWO����΢�Ź��ںţ�";
					//respContent = "�װ��ļҳ������ǣ�����Ϊ����С������ϵͳ�ĵ�½������ż��𣿻���Ϊ�������������������𣿹�ע���ſƼ���������΢�ţ��Ϳ������ɲ�ѯ��\n������ǰ�������á����˻���Ȼ�����Ϳ���ʹ�����¹��ܣ�\n��1�� ��ѯ������Ϣ\n��2�� ��ѯ������\n��3�� ��ѯ��������\n��4�� ������������";
					WeiXinAccount.getInstance().subscribe(fromUserName);
				}
				// ȡ������
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO ȡ�����ĺ��û����ղ������ںŷ��͵���Ϣ����˲���Ҫ�ظ���Ϣ
					WeiXinAccount.getInstance().unsubscribe(fromUserName);
				}
				// ģ����Ϣ
				else if (eventType.equals(MessageUtil.EVENT_TYPE_TEMPLATESENDJOBFINISH)) {
					// TODO ��ģ����Ϣ����������ɺ�΢�ŷ������Ὣ�Ƿ��ʹ�ɹ���Ϊ֪ͨ�����͵���������������д�ķ��������õ�ַ��
					WeiXinTemplate.getInstance().receiveMessage(requestMap);
				}
				// �Զ���˵�����¼�
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// �¼�KEYֵ���봴���Զ���˵�ʱָ����KEYֵ��Ӧ
					String eventKey = requestMap.get("EventKey");
					/*
					SupVoteHandle svh = new SupVoteHandle();
					supuserStatus = svh.getUserStatus(fromUserName);
					requestMap.put("SupUserStatus",supuserStatus);
					if (!"vote-2".equals(eventKey)){
						if (!"0".equals(supuserStatus) && !"4".equals(supuserStatus)){//��������������
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
				respContent = "��Ϣ���ͳɹ�.��\n";
			}
			
			if(messageFactory!=null && StringUtil.isNotEmpty(messageFactory.getReqTitle())){			
				// ����ͼ����Ϣ
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
				article.setPicUrl(""); // ��ͼƬ��Ϊ��
				article.setUrl(""); // �����ͼ����Ϣ��ת������Ϊ��
				articleList.add(article);
				newsMessage.setArticleCount(articleList.size());
				newsMessage.setArticles(articleList);
				respMessage = MessageUtil.newsMessageToXml(newsMessage);				
			} else {
				// �ظ��ı���Ϣ
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