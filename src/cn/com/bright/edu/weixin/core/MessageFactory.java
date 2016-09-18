package cn.com.bright.edu.weixin.core;

import java.util.Map;

import cn.brightcom.jraf.util.StringUtil;
import cn.com.bright.edu.weixin.core.impl.MessageImpl;
import cn.com.bright.edu.weixin.pojo.WeiXinUser;
import cn.com.bright.edu.weixin.util.MessageUtil;

public class MessageFactory {
	public Map<String, String> requestMap = null;
	public String commandCode = "11";
	public String eventKey = "";
	public String reqContent = "";
	public String splitStr = "#";
	public String reqTitle = "";
	
	public String getInfo(Map<String, String> _requestMap){
		String respContent = "";
		requestMap = _requestMap;
		reqContent = MessageUtil.replaceBlank(requestMap.get("Content"));
		eventKey = requestMap.get("EventKey");
		if(StringUtil.isNotEmpty(eventKey)){
			reqContent = eventKey;
		}
		String[] contents = reqContent.split(splitStr);
		commandCode = contents[0];
		
		Message message = new MessageImpl();
		String fromUserName = requestMap.get("FromUserName");
		message.setCommandCode(commandCode);
		message.setFromUserName(fromUserName);
		WeiXinUser weiXinUser = null;
		WeiXinUser[] weiXinUserList = null;
		
		
		if(contents.length == 1){
			if("22".equals(commandCode)){// ��������
				return message.getTipsInfo();
			} else if("vote-2".equals(commandCode)){// ΢���ʾ����
				return message.getTipsInfo();
			} else {
				weiXinUserList = message.getBindAccountList(fromUserName);
				if(weiXinUserList==null || weiXinUserList.length ==0){
					return message.getTipsInfo();
				}
			}
		} else if(contents.length == 2){
			if("21".equals(commandCode)){
				respContent = message.unbindAccount(fromUserName,contents[1]); // ����˻���
				return respContent;
			} else {
				weiXinUser = message.getBindAccount(fromUserName,contents[1]);
				if(null != weiXinUser){
					weiXinUserList = new WeiXinUser[]{weiXinUser};
				} else {
					message.setCommandCode("21");
					respContent = "�������֤�����벻���˻����б��У�����֤�����������Ƿ�������Ȱ��˻����ٳ��ԣ�\n\n" + message.getTipsInfo();
					return respContent;
				}
			}
		} else {
			if("21".equals(commandCode)){// �˻���
				respContent = message.checkBindAccount(fromUserName,contents); // ����˻��Ƿ��Ѱ�
				if("OK".equals(respContent)){
					respContent = message.validationAccount(contents);
					if("OK".equals(respContent)){
						boolean bindResult = message.bindAccount(fromUserName,contents[1],contents[2]);
						if(bindResult){
							respContent = "���˻��ɹ���\n\n"+ message.getTipsInfo();
						}
					}
				}
			} else {
				respContent = "������Ϣ����\n\n"+message.getTipsInfo();
			}
			return respContent;
		}
				
		for(int i=0;i<weiXinUserList.length;i++){
			weiXinUser = weiXinUserList[i];
			message = weiXinUser.getMessage();
			if(message==null) continue;
			
			message.setCommandCode(commandCode);
			if("11".equals(commandCode)){// ������Ϣ
				reqTitle = "������Ϣ";
				respContent += message.getBaseInfo(weiXinUser.getIdCard());
			} else if("12".equals(commandCode)){// ������
				reqTitle = "������";
				respContent += message.getApproveResult(weiXinUser.getIdCard());
			} else if("13".equals(commandCode)){// ��������ϸ�б�
				reqTitle = "��������ϸ�б�";
				respContent += message.getApproveResultList(weiXinUser.getIdCard());
			} else if("22".equals(commandCode)){// ��������
				reqTitle = "��������";
				respContent += message.resetPW(weiXinUser.getIdCard());
			}
			if(i < (weiXinUserList.length-1)) respContent+= "\n\n";
		}
		
		// û�в�ѯ������ʱ������ʾ��Ϣ
		if(StringUtil.isEmpty(respContent)){
			respContent = message.getTipsInfo();
		}
		return respContent;
	}

	public String getReqTitle() {
		return reqTitle;
	}

	public void setReqTitle(String reqTitle) {
		this.reqTitle = reqTitle;
	}	
}
