package cn.com.bright.edu.weixin.pojo;

import cn.com.bright.edu.weixin.core.Message;
import cn.com.bright.edu.weixin.core.impl.ChildrenMessageImpl;
import cn.com.bright.edu.weixin.core.impl.MinBanMessageImpl;
import cn.com.bright.edu.weixin.core.impl.TeacherMessageImpl;

/**
 * 微信用户类
 * 
 * @author lhbo
 * @date 2014-01-09
 */
public class WeiXinUser {
	private String winXinCode;
	private String userType;
	private String idCard;
	private String status;
	private String createDate;
	private String modifyDate;
	private Message message;
	
	public String getWinXinCode() {
		return winXinCode;
	}
	public void setWinXinCode(String winXinCode) {
		this.winXinCode = winXinCode;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
		if("00".equals(this.userType)){
			message = new ChildrenMessageImpl();
		} else if("01".equals(this.userType)){
			message = new MinBanMessageImpl();
		} else if("02".equals(this.userType)){
			message = new TeacherMessageImpl();
		}
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}	
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
}
