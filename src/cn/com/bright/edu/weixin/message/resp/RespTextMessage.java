package cn.com.bright.edu.weixin.message.resp;

import cn.brightcom.jraf.util.StringUtil;

/**
 * 消息文本类
 * 
 * @author lhbo
 * @date 2014-01-05
 */
public class RespTextMessage {
	String ToUserName = "";
	String FromUserName = "";
	long CreateTime = 0;
	String MsgType = "";
	int FuncFlag = 0;
	String Content = "";
	
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public int getFuncFlag() {
		return FuncFlag;
	}
	public void setFuncFlag(int funcFlag) {
		FuncFlag = funcFlag;
	}	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		if(StringUtil.isNotEmpty(content)){
			Content = content.replaceAll("null", "");
		} else {
			Content = "";
		}
	}
}
